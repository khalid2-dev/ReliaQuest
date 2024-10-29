package com.reliaquest.api;

import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.client.match.MockRestRequestMatchers.content;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.util.Arrays;
import java.util.List;
import java.util.UUID;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultMatcher;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import com.reliaquest.api.controller.EmployeeController;
import com.reliaquest.api.entity.Employee;
import com.reliaquest.api.request.EmployeeRequest;
import com.reliaquest.api.service.EmployeeService;
import static org.hamcrest.Matchers.containsString;

public class EmployeeControllerTest {

    @Mock
    private EmployeeService employeeService;

    @InjectMocks
    private EmployeeController employeeController;

    @Autowired
    private MockMvc mockMvc;

    @BeforeEach
    public void setup() {
        MockitoAnnotations.openMocks(this);
        mockMvc = MockMvcBuilders.standaloneSetup(employeeController).build();
    }

    @Test
    public void testGetAllEmployees() throws Exception {
    	UUID mockUUID1 = UUID.fromString("123e4567-e89b-12d3-a456-556642440000");
    	UUID mockUUID2 = UUID.fromString("123e4567-e89b-12d3-a456-556642550000");
        List<Employee> employees = Arrays.asList(
        		new Employee(mockUUID1, "Oliver Vandervort", 50000, 30, "Developer", "ovandervort@example.com"), 
        		new Employee(mockUUID2, "Juliann Durgan", 60000, 28, "Manager", "jdurgan@example.com"));
        when(employeeService.getAllEmployees()).thenReturn(employees);

        mockMvc.perform(get("http://localhost:8112/api/v1/employee"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.size()").value(employees.size()));

        verify(employeeService, times(1)).getAllEmployees();
    }

    @Test
    public void testGetEmployeesByNameSearch() throws Exception {
        String searchString = "Oliver";
        UUID mockUUID1 = UUID.fromString("123e4567-e89b-12d3-a456-556642440000");
        List<Employee> employees = Arrays.asList(new Employee(mockUUID1, "Oliver Vandervort", 50000, 30, "Developer", "ovandervort@example.com"));
        when(employeeService.getEmployeesByNameSearch(searchString)).thenReturn(employees);

        mockMvc.perform(get("/api/v1/employee/search/{searchString}", searchString))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.size()").value(employees.size()));

        verify(employeeService, times(1)).getEmployeesByNameSearch(searchString);
    }

    @Test
    public void testGetEmployeeById() throws Exception {
        String employeeId = "123e4567-e89b-12d3-a456-556642440000";
        UUID mockUUID1 = UUID.fromString("123e4567-e89b-12d3-a456-556642440000");
        Employee employee = new Employee(mockUUID1, "Oliver Vandervort", 50000, 30, "Developer", "ovandervort@example.com");
        when(employeeService.getEmployeeById(employeeId)).thenReturn(employee);

        mockMvc.perform(get("http://localhost:8112/api/v1/employee/{id}", employeeId))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.employee_name").value(employee.getEmployee_name()));

        verify(employeeService, times(1)).getEmployeeById(employeeId);
    }

    @Test
    public void testGetHighestSalaryOfEmployees() throws Exception {
        int highestSalary = 481004;
        when(employeeService.getHighestSalaryOfEmployees()).thenReturn(highestSalary);

        mockMvc.perform(get("/api/v1/employee/highestSalary"))
                .andExpect(status().isOk())
                .equals(highestSalary);

        verify(employeeService, times(1)).getHighestSalaryOfEmployees();
    }

    @Test
    public void testGetTopTenHighestEarningEmployeeNames() throws Exception {
        List<String> topEarners = Arrays.asList("Oliver Vandervort", "Juliann Durgan");
        when(employeeService.getTopTenHighestEarningEmployeeNames()).thenReturn(topEarners);

        mockMvc.perform(get("http://localhost:8112/api/v1/employee/topTenHighestEarningEmployeeNames"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.size()").value(topEarners.size()));

        verify(employeeService, times(1)).getTopTenHighestEarningEmployeeNames();
    }

    @Test
    public void testCreateEmployee() throws Exception {
        EmployeeRequest request = new EmployeeRequest("John Doe",100000,30,"Developer");
        UUID mockUUID1 = UUID.fromString("123e4567-e89b-12d3-a456-556642440000");
        Employee employee = new Employee(mockUUID1, "Oliver Vandervort", 50000, 30, "Developer", "ovandervort@example.com");
        when(employeeService.createEmployee(request)).thenReturn(employee);

        mockMvc.perform(post("/api/v1/employee")
                .contentType("application/json")
                .content("{\"employee_name\": \"Oliver Vandervort\", \"employee_salary\": 50000\", \"employee_age\": 30\", \"employee_title\": \"Developer}"))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.employee_name").value(employee.getEmployee_name()));

        verify(employeeService, times(1)).createEmployee(request);
    }

    @Test
    public void testDeleteEmployeeById() throws Exception {
        String employeeId = "123e4567-e89b-12d3-a456-556642440000";
        String deleteMessage = "Employee with id ("+employeeId+") has been successfully deleted";
        when(employeeService.deleteEmployeeById(employeeId)).thenReturn(deleteMessage);

        mockMvc.perform(delete("/api/v1/employee/{id}", employeeId))
                .andExpect(status().isOk());

        verify(employeeService, times(1)).deleteEmployeeById(employeeId);
    }
}
