package com.reliaquest.api.utility;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.server.ResponseStatusException;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.reliaquest.api.entity.Employee;
import com.reliaquest.api.request.EmployeeRequest;
import com.reliaquest.api.response.EmployeeResponse;

public class EmployeeUtility {
	
    private final String BASE_URL = "http://localhost:8112/api/v1/employee";
    private final RestTemplate restTemplate;
    
    @Autowired
    public EmployeeUtility(RestTemplate restTemplate) {
        this.restTemplate = restTemplate;
    }
	
	public static List<String> validate(EmployeeRequest employeeRequest){
		
		List<String> error = new ArrayList<>();
		
		if(employeeRequest.getEmployee_name() == null || employeeRequest.getEmployee_name().isEmpty()) {
			error.add("Name must not be empty");
		}

		if(!(employeeRequest.getEmployee_age() >= 16 && employeeRequest.getEmployee_age() <= 75)) {
			error.add("Age must be min 16 and max 75");
		}		
		
		if(employeeRequest.getEmployee_salary() <= 0) {
			error.add("Salar should be greater than zero (0)");
		}	

		if(employeeRequest.getEmployee_title() == null || employeeRequest.getEmployee_title().isEmpty()) {
			error.add("Title must not be empty");
		}
		
		return error;
		
	}
	
	public static Employee jsonFotmatterToEmployeeObject(ResponseEntity<EmployeeResponse> response){
        ObjectMapper mapper = new ObjectMapper();
        String res = null;
		try {
			res = mapper.writeValueAsString(response.getBody().getData());
		} catch (JsonProcessingException e1) {
			e1.printStackTrace();
		}
        JSONObject jsonObject = new JSONObject(res);
        String formattedJson = jsonObject.toString();
        
		Employee employee = null;
		try {
			employee = mapper.readValue(formattedJson, Employee.class);
		} catch (JsonProcessingException e) {
			e.printStackTrace();
		}

        return employee;
	}
	
//	public static List<Employee> jsonFotmatterToEmployeeList(ResponseEntity<EmployeeResponse> response){
//        ObjectMapper mapper = new ObjectMapper();
//        String res = null;
//		try {
//			res = mapper.writeValueAsString(response.getBody().getData());
//			System.out.println("RESPONSE: "+res);
//		} catch (JsonProcessingException e1) {
//			e1.printStackTrace();
//		}
//        JSONObject jsonObject = new JSONObject(res);
//        String formattedJson = jsonObject.toString();
//        System.out.println(formattedJson);
//        try {
//            // Use ObjectMapper to convert JSON string to List<Employee>
//            return mapper.readValue(formattedJson, new TypeReference<List<Employee>>() {});
//        } catch (Exception e) {
//            e.getMessage();
//            return Collections.emptyList();
//        }
//        
//        try {
//            // Parse the outer structure
//            EmployeeResponse empResponse = mapper.readValue(res, EmployeeResponse.class);
//            // Access the list of employees in the "data" field
//            return empResponse.getData();
//        } catch (Exception e) {
//            e.printStackTrace();
//            return Collections.emptyList();
//        }
//
//        //return formattedJson;
//	}

}
