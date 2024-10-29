package com.reliaquest.api;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
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
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.reliaquest.api.controller.EmployeeController;
import com.reliaquest.api.entity.Employee;
import com.reliaquest.api.request.EmployeeRequest;
import com.reliaquest.api.service.EmployeeService;

public class EmployeeControllerTest {

    @Mock
    private EmployeeService employeeService;

    @InjectMocks
    private EmployeeController employeeController;
    
    @Autowired
    private ObjectMapper objectMapper;    

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
                .andExpect(jsonPath("$.employee_name").value(employee.getEmployeeName()));

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
        EmployeeRequest employeeRequest = new EmployeeRequest("Oliver Vandervort", 60000, 30, "Software Engineer");
        UUID mockUUID = UUID.fromString("123e4567-e89b-12d3-a456-556642440000");
        Employee employeeResponse = new Employee(mockUUID, "Oliver Vandervort", 60000, 30, "Software Engineer", "ovandervort@example.com");

        // Mock the service method
        when(employeeService.createEmployee(any(EmployeeRequest.class))).thenReturn(employeeResponse);

        // Act & Assert: Perform POST request and verify the response
        mockMvc.perform(post("/api/v1/employee")
                .contentType(MediaType.APPLICATION_JSON)
                .content("{\"employee_name\":\"John Doe\",\"employee_age\":30,\"employee_title\":\"Software Engineer\",\"employee_salary\":60000}"))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.employee_name").value("Oliver Vandervort"))
                .andExpect(jsonPath("$.employee_age").value(30))
                .andExpect(jsonPath("$.employee_title").value("Software Engineer"))
                .andExpect(jsonPath("$.employee_salary").value(60000));
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
