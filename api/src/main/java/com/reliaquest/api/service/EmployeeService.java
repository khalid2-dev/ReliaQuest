package com.reliaquest.api.service;

import java.util.Comparator;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.server.ResponseStatusException;

import com.reliaquest.api.controller.EmployeeController;
import com.reliaquest.api.entity.Employee;
import com.reliaquest.api.repository.EmployeeRepository;
import com.reliaquest.api.request.DeleteEmployeeRequest;
import com.reliaquest.api.request.EmployeeRequest;
import com.reliaquest.api.response.EmployeeResponse;
import com.reliaquest.api.utility.EmployeeUtility;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;

@Service
public class EmployeeService {
	
	public static final Log LOGGER = LogFactory.getLog(EmployeeController.class);
	
    private final String BASE_URL = "http://localhost:8112/api/v1/employee";
    private final RestTemplate restTemplate;
    
    @Autowired
    private EmployeeRepository employeeRepository;
    
    @Autowired
    public EmployeeService(RestTemplate restTemplate) {
        this.restTemplate = restTemplate;
    }
	
    /**
     * This service returns a list of ALL employees
     * @return returns a list of all employees
     */
	public List<Employee> getAllEmployees() {
		LOGGER.info("Entering getAllEmployees service ::");
		ResponseEntity<EmployeeResponse> response = restTemplate.getForEntity(BASE_URL, EmployeeResponse.class);
		if(response.getStatusCode() != HttpStatus.OK) {
			throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Failed to fetch employees");
		}
		return (List<Employee>) response.getBody().getData();
	}

	/**
	 * This service calls an external API and returns all employees whose name contains or matches the string search provided
	 * @param name as search string
	 * @return returns a list of employees whose name contains or matches the string input provided
	 */
	public List<Employee> getEmployeesByNameSearch(String searchString) {
		LOGGER.info("Entering getEmployeesByNameSearch service ::");
		ResponseEntity<EmployeeResponse> response = restTemplate.getForEntity(BASE_URL, EmployeeResponse.class);
		ObjectMapper mapper = new ObjectMapper();
		String res = null;
		try {
			res = mapper.writeValueAsString(response.getBody().getData());
		} catch (JsonProcessingException e1) {
			e1.printStackTrace();
		}
		List<Employee> employeeList = null;
		List<Employee> employeeNameMatchingList = null;
		try {
			employeeList = mapper.readValue(res, new TypeReference<List<Employee>>() {});
			employeeNameMatchingList = employeeList.stream()
					.filter(emp -> emp.getEmployee_name().toLowerCase().contains(searchString.toLowerCase()))
					.collect(Collectors.toList());
			LOGGER.info("Highest salary has been retrieved");
		} catch (JsonMappingException e) {
			e.printStackTrace();
		} catch (JsonProcessingException e) {
			e.printStackTrace();
		}
		if(response.getStatusCode() != HttpStatus.OK) {
			throw new ResponseStatusException(HttpStatus.NOT_FOUND, "No any emplyee with search criteria ("+searchString+") was found");
		}
		return employeeNameMatchingList;
	}

	/**
	 * This service calls an external API and returns a single employee based on a given id
	 * @param employee id as string
	 * @return returns a single employee object
	 */
	public Employee getEmployeeById(String id) {
		LOGGER.info("Entering getEmployeeById service ::");
		ObjectMapper mapper = new ObjectMapper();
		String res = null;
		try {
			ResponseEntity<EmployeeResponse> response = restTemplate.getForEntity(BASE_URL+"/"+id, EmployeeResponse.class);

			res = mapper.writeValueAsString(response.getBody().getData());
		} catch (JsonProcessingException e) {
			LOGGER.error("Error processing JSON response:: ", e);
		} catch(Exception e) {
			e.getMessage();			
			throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Employee with id "+id+" not found");
		}
        JSONObject jsonObject = new JSONObject(res);
        String formattedJson = jsonObject.toString();

		Employee employee = null;
		try {
			employee = mapper.readValue(formattedJson, Employee.class);
		} catch (JsonProcessingException e) {
			LOGGER.error("Error processing JSON response:: ", e);
		}
		LOGGER.info("Employee details for employee id "+id+": "+res);
		return employee;
	}

	/**
	 * This service calls an external API and returns an integer indicating the highest salary of amongst all employees
	 * @return returns a single integer indicating the highest salary of amongst all employees
	 */
	public int getHighestSalaryOfEmployees() {
		LOGGER.info("Entering getHighestSalaryOfEmployees service ::");
		ResponseEntity<EmployeeResponse> response = restTemplate.getForEntity(BASE_URL, EmployeeResponse.class);
		ObjectMapper mapper = new ObjectMapper();
		String res = null;
		try {
			res = mapper.writeValueAsString(response.getBody().getData());
		} catch (JsonProcessingException e1) {
			e1.printStackTrace();
		}
		List<Employee> employeeList = null;
		int highestSalary = 0;
		try {
			employeeList = mapper.readValue(res, new TypeReference<List<Employee>>() {});
			highestSalary = employeeList.stream().mapToInt(e -> e.getEmployee_salary()).max().orElse(0);
			LOGGER.info("Highest salary has been retrieved");
		} catch (JsonMappingException e) {
			e.printStackTrace();
		} catch (JsonProcessingException e) {
			e.printStackTrace();
		}
		return highestSalary;

	}

	/**
	 * This service calls an external API and returns a list of the top 10 employees based off of their salaries
	 * @return returns a list of the top 10 employees based off of their salaries
	 */
	public List<String> getTopTenHighestEarningEmployeeNames() {
		LOGGER.info("Entering getTopTenHighestEarningEmployeeNames service ::");
		ResponseEntity<EmployeeResponse> response = restTemplate.getForEntity(BASE_URL, EmployeeResponse.class);
		ObjectMapper mapper = new ObjectMapper();
		String res = null;
		try {
			res = mapper.writeValueAsString(response.getBody().getData());
		} catch (JsonProcessingException e1) {
			e1.printStackTrace();
		}
		List<Employee> employeeList = null;
		List<String> topTenhighestSalaries = null;
		try {
			employeeList = mapper.readValue(res, new TypeReference<List<Employee>>() {});
			topTenhighestSalaries = employeeList.stream()
					.sorted(Comparator.comparingInt(Employee::getEmployee_salary).reversed())
					.limit(10)
					.map(x -> x.getEmployee_name())
					.collect(Collectors.toList());
			LOGGER.info("Highest 10 salaries have been retrieved");
		} catch (JsonMappingException e) {
			e.printStackTrace();
		} catch (JsonProcessingException e) {
			e.printStackTrace();
		}
		return topTenhighestSalaries;	
	}

	/**
	 * This service calls the employee repository and returns a single employee, if created, otherwise error
	 * @param EmployeeRequest
	 * @return returns a single employee, if created
	 * @throws error if failed
	 */
	public Employee createEmployee(EmployeeRequest employeeRequest) {
		// The Server API is not working, so calling the employee repository instead		
		LOGGER.info("Entering createEmployee service ::");
		List<String> validations = EmployeeUtility.validate(employeeRequest);
		Employee employee = new Employee();
		if(validations.isEmpty()) {
			LOGGER.info("Validation successful ::");
			
	        UUID uuid = UUID.randomUUID();
			
	        employee.setId(uuid);
			employee.setEmployee_name(employeeRequest.getEmployee_name());
			employee.setEmployee_age(employeeRequest.getEmployee_age());
			employee.setEmployee_salary(employeeRequest.getEmployee_salary());
			employee.setEmployee_title(employeeRequest.getEmployee_title());
			
			LOGGER.info("Employee has been successfully created ::");
			return employeeRepository.addEmployee(employee);
		} else {
			LOGGER.info("Validation failed while creating new employee ::");
			throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Validation failed while creating new employee with the following reasons: "+validations.toString());
		}
	}

	/**
	 * This service calls an external API and deletes the employee based on given id
	 * @param employee id as string
	 * @return delete the employee based on given id
	 * @throws error if failed
	 */
	public String deleteEmployeeById(String id) {
		LOGGER.info("Entering deleteEmployeeById service ::");
		Employee employeeToBeDeleted = getEmployeeById(id);
		DeleteEmployeeRequest deleteRequest = new DeleteEmployeeRequest();
		deleteRequest.setName(employeeToBeDeleted.getEmployee_name());

		HttpEntity<DeleteEmployeeRequest> requestEntity = new HttpEntity<>(deleteRequest, null);
		
		ResponseEntity<EmployeeResponse> response = restTemplate.exchange(BASE_URL, HttpMethod.DELETE, requestEntity, EmployeeResponse.class);
		
		ObjectMapper mapper = new ObjectMapper();
		
		String status = "Unable to delete an employee";
		String data = null;
		try {
			data = (String) mapper.writeValueAsString(response.getBody().getData());
		} catch (JsonProcessingException e) {
			LOGGER.error("Error processing JSON response:: ", e);
		}
		
		if(data.contains("true")) {
			try {
				String successStatus = mapper.writeValueAsString(response.getBody().getStatus());
				LOGGER.info("Employee with id ("+id+") has been successfully deleted");
				return successStatus.substring(1, successStatus.length() - 1)+" Employee with id "+id+" has been successfully deleted.";
			} catch (JsonProcessingException e) {
				LOGGER.error("Error processing JSON response:: ", e);
			}
		} 
//		if(employeeToBeDeleted == null) {
//			throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Unable to delete employee with id "+id+". Employee not found.");
//		}
		
		return status;
	}

}
