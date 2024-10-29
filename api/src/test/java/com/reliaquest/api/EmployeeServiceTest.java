package com.reliaquest.api;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.mockStatic;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.client.match.MockRestRequestMatchers.requestTo;
import static org.springframework.test.web.client.response.MockRestResponseCreators.withStatus;
import static org.springframework.test.web.client.response.MockRestResponseCreators.withSuccess;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.UUID;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.MediaType;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.test.web.client.MockRestServiceServer;
import org.springframework.web.client.RestTemplate;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.reliaquest.api.entity.Employee;
import com.reliaquest.api.repository.EmployeeRepository;
import com.reliaquest.api.request.EmployeeRequest;
import com.reliaquest.api.response.EmployeeResponse;
import com.reliaquest.api.service.EmployeeService;
import com.reliaquest.api.utility.EmployeeUtility;

class EmployeeServiceTest {

    @Mock
    private RestTemplate restTemplate;

    @Mock
    private EmployeeRepository employeeRepository;

    @InjectMocks
    private EmployeeService employeeService;
    
    @Mock
    private MockRestServiceServer mockServer;
    
    @Autowired
    private ObjectMapper objectMapper;    

//    @BeforeEach
//    void setUp() {
//        MockitoAnnotations.openMocks(this);
//    }
    
    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        mockServer = MockRestServiceServer.createServer(restTemplate);
    }

    @Test
    void testGetAllEmployees() {
    	UUID mockUUID1 = UUID.fromString("123e4567-e89b-12d3-a456-556642440000");
    	UUID mockUUID2 = UUID.fromString("123e4567-e89b-12d3-a456-556642550000");
        Employee mockEmployee1 = new Employee(mockUUID1, "Oliver Vandervort", 50000, 30, "Developer", "ovandervort@example.com");
        Employee mockEmployee2 = new Employee(mockUUID2, "Juliann Durgan", 60000, 28, "Manager", "jdurgan@example.com");
        EmployeeResponse mockResponse = new EmployeeResponse(Arrays.asList(mockEmployee1, mockEmployee2), "HANDLED");
        
        when(restTemplate.getForEntity(anyString(), eq(EmployeeResponse.class)))
                .thenReturn(new ResponseEntity<>(mockResponse, HttpStatus.OK));

        List<Employee> employees = employeeService.getAllEmployees();

        assertNotNull(employees);
        assertEquals(2, employees.size());
        verify(restTemplate, times(1)).getForEntity(anyString(), eq(EmployeeResponse.class));
    }

    @Test
    void testGetEmployeesByNameSearch() {
    	UUID mockUUID1 = UUID.fromString("123e4567-e89b-12d3-a456-556642440000");
    	UUID mockUUID2 = UUID.fromString("123e4567-e89b-12d3-a456-556642550000");
        Employee mockEmployee1 = new Employee(mockUUID1, "Oliver Vandervort", 50000, 30, "Developer", "ovandervort@example.com");
        Employee mockEmployee2 = new Employee(mockUUID2, "Juliann Durgan", 60000, 28, "Manager", "jdurgan@example.com");
        EmployeeResponse mockResponse = new EmployeeResponse(Arrays.asList(mockEmployee1, mockEmployee2), "HANDLED");

        when(restTemplate.getForEntity(anyString(), eq(EmployeeResponse.class)))
                .thenReturn(new ResponseEntity<>(mockResponse, HttpStatus.OK));

        List<Employee> employees = employeeService.getEmployeesByNameSearch("Jane");

        assertNotNull(employees);
        assertEquals(1, employees.size());
        assertEquals("Juliann Durgan", employees.get(0).getEmployee_name());
    }

    @Test
    void testGetEmployeeById() {
    	UUID mockUUID1 = UUID.fromString("123e4567-e89b-12d3-a456-556642440000");
        Employee mockEmployee = new Employee(mockUUID1, "Oliver Vandervort", 50000, 30, "Developer", "ovandervort@example.com");
        EmployeeResponse mockResponse = new EmployeeResponse(mockEmployee, "HANDLED");

        when(restTemplate.getForEntity(anyString(), eq(EmployeeResponse.class)))
                .thenReturn(new ResponseEntity<>(mockResponse, HttpStatus.OK));

        Employee employee = employeeService.getEmployeeById("1");

        assertNotNull(employee);
        assertEquals("Oliver Vandervort", employee.getEmployee_name());
        verify(restTemplate, times(1)).getForEntity(anyString(), eq(EmployeeResponse.class));
    }

    @Test
    void testGetHighestSalaryOfEmployees() {
    	UUID mockUUID1 = UUID.fromString("123e4567-e89b-12d3-a456-556642440000");
    	UUID mockUUID2 = UUID.fromString("123e4567-e89b-12d3-a456-556642550000");
        Employee mockEmployee1 = new Employee(mockUUID1, "Oliver Vandervort", 50000, 30, "Developer", "ovandervort@example.com");
        Employee mockEmployee2 = new Employee(mockUUID2, "Juliann Durgan", 60000, 28, "Manager", "jdurgan@example.com");
        EmployeeResponse mockResponse = new EmployeeResponse(Arrays.asList(mockEmployee1, mockEmployee2), "HANDLED");

        when(restTemplate.getForEntity(anyString(), eq(EmployeeResponse.class)))
                .thenReturn(new ResponseEntity<>(mockResponse, HttpStatus.OK));

        int highestSalary = employeeService.getHighestSalaryOfEmployees();

        assertEquals(60000, highestSalary);
        verify(restTemplate, times(1)).getForEntity(anyString(), eq(EmployeeResponse.class));
    }

    @Test
    void testGetTopTenHighestEarningEmployeeNames() {
    	UUID mockUUID1 = UUID.fromString("123e4567-e89b-12d3-a456-556642440000");
    	UUID mockUUID2 = UUID.fromString("123e4567-e89b-12d3-a456-556642550000");
    	UUID mockUUID3 = UUID.fromString("123e4567-e89b-12d3-a456-556642660000");
        Employee mockEmployee1 = new Employee(mockUUID1, "Oliver Vandervort", 50000, 30, "Developer", "ovandervort@example.com");
        Employee mockEmployee2 = new Employee(mockUUID2, "Juliann Durgan", 60000, 28, "Manager", "jdurgan@example.com");
        Employee mockEmployee3 = new Employee(mockUUID3, "Bob Brown", 40000, 35, "Analyst", "bbrown@example.com");
        EmployeeResponse mockResponse = new EmployeeResponse(Arrays.asList(mockEmployee1, mockEmployee2, mockEmployee3), "HANDLED");

        when(restTemplate.getForEntity(anyString(), eq(EmployeeResponse.class)))
                .thenReturn(new ResponseEntity<>(mockResponse, HttpStatus.OK));

        List<String> topSalaries = employeeService.getTopTenHighestEarningEmployeeNames();

        assertNotNull(topSalaries);
        assertEquals(3, topSalaries.size());
        assertEquals("Juliann Durgan", topSalaries.get(0));
        verify(restTemplate, times(1)).getForEntity(anyString(), eq(EmployeeResponse.class));
    }

    @Test
    void testCreateEmployee() {
        EmployeeRequest employeeRequest = new EmployeeRequest();
        employeeRequest.setEmployee_name("John Doe");
        employeeRequest.setEmployee_age(30);
        employeeRequest.setEmployee_salary(50000);
        employeeRequest.setEmployee_title("Engineer");

        mockStatic(EmployeeUtility.class);
        when(EmployeeUtility.validate(employeeRequest)).thenReturn(Collections.emptyList());

        Employee expectedEmployee = new Employee();
        expectedEmployee.setId(UUID.randomUUID());
        expectedEmployee.setEmployee_name(employeeRequest.getEmployee_name());
        expectedEmployee.setEmployee_age(employeeRequest.getEmployee_age());
        expectedEmployee.setEmployee_salary(employeeRequest.getEmployee_salary());
        expectedEmployee.setEmployee_title(employeeRequest.getEmployee_title());

        when(employeeRepository.addEmployee(any(Employee.class))).thenReturn(expectedEmployee);

        Employee createdEmployee = employeeService.createEmployee(employeeRequest);

        assertNotNull(createdEmployee);
        assertEquals(employeeRequest.getEmployee_name(), createdEmployee.getEmployee_name());
        assertEquals(employeeRequest.getEmployee_age(), createdEmployee.getEmployee_age());
        assertEquals(employeeRequest.getEmployee_salary(), createdEmployee.getEmployee_salary());
        assertEquals(employeeRequest.getEmployee_title(), createdEmployee.getEmployee_title());
        verify(employeeRepository, times(1)).addEmployee(any(Employee.class));
    }

    @Test
    void testDeleteEmployeeById() {
    	UUID mockUUID1 = UUID.fromString("123e4567-e89b-12d3-a456-556642440000");
        Employee mockEmployee = new Employee(mockUUID1, "Oliver Vandervort", 30, 50000, "Developer", "ovandervort@example.com");
        EmployeeResponse mockResponse = new EmployeeResponse(mockEmployee, "HANDLED");

        when(restTemplate.getForEntity(anyString(), eq(EmployeeResponse.class)))
                .thenReturn(new ResponseEntity<>(mockResponse, HttpStatus.OK));
        when(restTemplate.exchange(anyString(), eq(HttpMethod.DELETE), any(), eq(EmployeeResponse.class)))
                .thenReturn(new ResponseEntity<>(mockResponse, HttpStatus.OK));

        String result = employeeService.deleteEmployeeById("1");

        assertNotNull(result);
        verify(restTemplate, times(1)).exchange(anyString(), eq(HttpMethod.DELETE), any(), eq(EmployeeResponse.class));
    }
    

    @Test
    void testGetHighestSalaryOfEmployees1() throws Exception {
        String responseJson = "{ \"data\": [{ \"employee_name\": \"John Doe\", \"employee_salary\": 60000, \"employee_age\": 30, \"employee_title\": \"Developer\" }], \"status\": \"HANDLED\", \"error\": null }";

        mockServer = MockRestServiceServer.createServer(restTemplate);
        mockServer.expect(requestTo("http://localhost:8112/api/v1/employee"))
                  .andRespond(withSuccess(responseJson, MediaType.APPLICATION_JSON));

        int highestSalary = employeeService.getHighestSalaryOfEmployees();

        assertEquals(60000, highestSalary);
        mockServer.verify();
    }
    
    @Test
    void testGetHighestSalaryOfEmployees2() throws Exception {
        Employee mockEmployee = new Employee(UUID.randomUUID(), "John Doe", 70000, 30, "Developer", "john.doe@example.com");
        EmployeeResponse mockResponse = new EmployeeResponse(List.of(mockEmployee), "OK");

        String responseJson = objectMapper.writeValueAsString(mockResponse);

        mockServer.expect(requestTo("http://localhost:8112/api/v1/employee"))
                  .andRespond(withStatus(HttpStatus.OK)
                  .contentType(MediaType.APPLICATION_JSON)
                  .body(responseJson));

        int highestSalary = employeeService.getHighestSalaryOfEmployees();

        assertEquals(70000, highestSalary);
        mockServer.verify();
    }    
}