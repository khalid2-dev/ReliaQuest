package com.reliaquest.api.controller;

import java.util.List;

import javax.validation.Valid;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.reliaquest.api.entity.Employee;
import com.reliaquest.api.request.EmployeeRequest;
import com.reliaquest.api.service.EmployeeService;


@RestController
@RequestMapping("/api/v1/employee")
public class EmployeeController implements IEmployeeController<Employee, EmployeeRequest>{
	
	public static final Log LOGGER = LogFactory.getLog(EmployeeController.class); 
	
	@Autowired
	private EmployeeService employeeService;
	
	/**
	 * Returns a list of all employees
	 * @return returns a list of all employees
	 */
	@Override
	public ResponseEntity<List<Employee>> getAllEmployees() {
		LOGGER.info("Entering getAllEmployees ::");
		return ResponseEntity.ok(employeeService.getAllEmployees());
	}

	/**
	 * Returns all employees whose name contains or matches the string input provided
	 * @param name as search string
	 * @return returns all employees whose name contains or matches the string input provided
	 */
	@Override
	public ResponseEntity<List<Employee>> getEmployeesByNameSearch(@PathVariable("searchString") String searchString) {
		LOGGER.info("Entering getEmployeesByNameSearch ::");
		return ResponseEntity.ok(employeeService.getEmployeesByNameSearch(searchString));
	}

	/**
	 * Returns a single employee based on a given id
	 * @param employee id as string
	 * @return returns a single employee object
	 */
	@Override
	public ResponseEntity<Employee> getEmployeeById(@PathVariable("id") String id) {
		//employeeService.getEmployeeById(id);
		LOGGER.info("Entering getEmployeeById ::");
		LOGGER.info("ID to search :: "+id);
		return ResponseEntity.ok(employeeService.getEmployeeById(id));
	}

	/**
	 * Returns a single integer indicating the highest salary of amongst all employees
	 * @return returns a single integer indicating the highest salary of amongst all employees
	 */
	@Override
	public ResponseEntity<Integer> getHighestSalaryOfEmployees() {
		LOGGER.info("Entering getHighestSalaryOfEmployees ::");
		return ResponseEntity.ok(employeeService.getHighestSalaryOfEmployees());
	}

	/**
	 * Returns a list of the top 10 employees based off of their salaries
	 * @return returns a list of the top 10 employees based off of their salaries
	 */
	@Override
	public ResponseEntity<List<String>> getTopTenHighestEarningEmployeeNames() {
		LOGGER.info("Entering getTopTenHighestEarningEmployeeNames ::");
		return ResponseEntity.ok(employeeService.getTopTenHighestEarningEmployeeNames());
	}

	/**
	 * Returns a single employee, if created, otherwise error
	 * @param EmployeeRequest
	 * @return returns a single employee, if created
	 * @throws error if failed
	 */
	@Override
	public ResponseEntity<Employee> createEmployee(@Valid @RequestBody EmployeeRequest employeeRequest) {
		LOGGER.info("Entering createEmployee ::");
        return ResponseEntity.status(HttpStatus.CREATED).body(employeeService.createEmployee(employeeRequest));
	}

	/**
	 * Deletes the employee with specified id given
	 * @param employee id as string
	 * @return delete the employee with specified id given
	 * @throws error if failed
	 */
	@Override
	public ResponseEntity<String> deleteEmployeeById(@PathVariable("id") String id) {
		LOGGER.info("Entering deleteEmployeeById ::");
		LOGGER.info("ID to be deleted :: "+id);
		return ResponseEntity.ok(employeeService.deleteEmployeeById(id));
	}

}
