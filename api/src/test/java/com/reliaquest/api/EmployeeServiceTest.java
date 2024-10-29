package com.reliaquest.api;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.UUID;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.test.web.client.MockRestServiceServer;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.server.ResponseStatusException;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.reliaquest.api.entity.Employee;
import com.reliaquest.api.exception.EmployeeException;
import com.reliaquest.api.exception.GeneralException;
import com.reliaquest.api.repository.EmployeeRepository;
import com.reliaquest.api.request.EmployeeRequest;
import com.reliaquest.api.response.EmployeeResponse;
import com.reliaquest.api.service.EmployeeService;

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
    
    private final String BASE_URL = "http://localhost:8112/api/v1/employee";
    
    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        mockServer = MockRestServiceServer.createServer(restTemplate);
    }

    @Test
    void testGetAllEmployees() {
    	UUID mockUUID1 = UUID.fromString("123e4567-e89b-12d3-a456-556642440000");
    	UUID mockUUID2 = UUID.fromString("123e4567-e89b-12d3-a456-556642550000");
        List<Employee> mockEmployeeList = Arrays.asList(new Employee(mockUUID1, "Oliver Vandervort", 50000, 30, "Developer", "ovandervort@example.com"), 
        												new Employee(mockUUID2, "Juliann Durgan", 60000, 28, "Manager", "jdurgan@example.com"));
        EmployeeResponse mockResponse = new EmployeeResponse();
        mockResponse.setData(mockEmployeeList);

        when(restTemplate.getForEntity(BASE_URL, EmployeeResponse.class)).thenReturn(new ResponseEntity<>(mockResponse, HttpStatus.OK));

        List<Employee> result = employeeService.getAllEmployees();

        assertEquals(mockEmployeeList.size(), result.size());
        verify(restTemplate, times(1)).getForEntity(BASE_URL, EmployeeResponse.class);    	
    }

    @Test
    void testGetEmployeesByNameSearch() {
    	UUID mockUUID1 = UUID.fromString("123e4567-e89b-12d3-a456-556642440000");
    	UUID mockUUID2 = UUID.fromString("123e4567-e89b-12d3-a456-556642550000");
        Employee mockEmployee1 = new Employee(mockUUID1, "Oliver Vandervort", 50000, 30, "Developer", "ovandervort@example.com");
        Employee mockEmployee2 = new Employee(mockUUID2, "Juliann Durgan", 60000, 28, "Manager", "jdurgan@example.com");
        EmployeeResponse mockResponse = new EmployeeResponse(Arrays.asList(mockEmployee1, mockEmployee2), "OK");

        when(restTemplate.getForEntity(anyString(), eq(EmployeeResponse.class)))
                .thenReturn(new ResponseEntity<>(mockResponse, HttpStatus.OK));

        List<Employee> employees = employeeService.getEmployeesByNameSearch("Oliv");

        assertNotNull(employees);
        assertEquals(1, employees.size());
        assertEquals("Oliver Vandervort", employees.get(0).getEmployeeName());
    }
    
    @Test
    void testGetEmployeesByNameSearchFailure() {
        when(restTemplate.getForEntity(anyString(), eq(EmployeeResponse.class)))
        .thenReturn(ResponseEntity.notFound().build());

		ResponseStatusException exception = assertThrows(ResponseStatusException.class,
						() -> employeeService.getEmployeeById("xyz"));
		assertTrue(exception.getStatusCode().is4xxClientError());
		assertTrue(exception.getMessage().contains("not found"));
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
        assertEquals("Oliver Vandervort", employee.getEmployeeName());
        verify(restTemplate, times(1)).getForEntity(anyString(), eq(EmployeeResponse.class));
    }

    @Test
    void testGetHighestSalaryOfEmployees() {
    	UUID mockUUID1 = UUID.fromString("123e4567-e89b-12d3-a456-556642440000");
    	UUID mockUUID2 = UUID.fromString("123e4567-e89b-12d3-a456-556642550000");
        String email1 = "ovandervort@example.com";
        String email2 = "jdurgan@example.com";
        List<Employee> mockEmployeeList = Arrays.asList(new Employee(mockUUID1, "Oliver Vandervort", 50000, 30, "Developer", email1), new Employee(mockUUID2, "Juliann Durgan", 80000, 28, "Manager", "jdurgan@example.com"));
        EmployeeResponse mockResponse = new EmployeeResponse();
                
        when(restTemplate.getForEntity(BASE_URL, EmployeeResponse.class))
        .thenReturn(ResponseEntity.ok(new EmployeeResponse(mockEmployeeList, "OK")));

	    int highestSalary = employeeService.getHighestSalaryOfEmployees();
	
	    assertThat(highestSalary).isEqualTo(80000);
    }
    
    @Test
    public void testGetHighestSalaryOfEmployeesEmptyList() {
      EmployeeResponse mockResponse = new EmployeeResponse(Collections.emptyList(), "OK");
      when(restTemplate.getForEntity(BASE_URL, EmployeeResponse.class))
          .thenReturn(ResponseEntity.ok(mockResponse));
      int highestSalary = employeeService.getHighestSalaryOfEmployees();
      assertEquals(0, highestSalary);
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
    public void testGetTopTenHighestEarningEmployeeNamesFailure() {
      when(restTemplate.getForEntity(anyString(), eq(EmployeeResponse.class)))
          .thenReturn(ResponseEntity.badRequest().build());

      assertThrows(Exception.class, () -> employeeService.getTopTenHighestEarningEmployeeNames());
    }    

    @Test
    public void testCreateEmployeeValidationFailure() {
		EmployeeRequest invalidRequest = new EmployeeRequest("", 30, 80000, "Software Engineer"); // Empty name should fail validation
		when(employeeRepository.addEmployee(any(Employee.class))).thenThrow(new UnsupportedOperationException("Shouldn't be called"));
		
		assertThrows(ResponseStatusException.class, () -> employeeService.createEmployee(invalidRequest));
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
    public void testDeleteEmployeeByIdNotFound() {
    	when(restTemplate.getForEntity(anyString(), eq(EmployeeResponse.class)))
        .thenReturn(ResponseEntity.notFound().build());
		ResponseStatusException exception = assertThrows(ResponseStatusException.class,
				() -> employeeService.deleteEmployeeById("xyz"));
		assertTrue(exception.getStatusCode().is4xxClientError());
		assertTrue(exception.getMessage().contains("not found"));
    }    
    

//    @Test
//    void testGetHighestSalaryOfEmployeesFailure() {
//    	  when(restTemplate.getForEntity(BASE_URL, EmployeeResponse.class))
//          .thenReturn(ResponseEntity.badRequest().build());
//    	  ResponseStatusException exception = assertThrows(ResponseStatusException.class,
//  				() -> employeeService.getHighestSalaryOfEmployees());
//    	  assertThrows(ResponseStatusException.class, () -> employeeService.getHighestSalaryOfEmployees());
//    }
       
}