package com.reliaquest.api.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.reliaquest.api.entity.Employee;
import com.reliaquest.api.entity.EmployeeRequest;
import com.reliaquest.api.service.EmployeeService;


@RestController
@RequestMapping("/employee")
public class EmployeeController implements IEmployeeController<Employee, EmployeeRequest>{
	
	@Autowired
	private EmployeeService employeeService;
	
	/**
	 * Returns a list of all employees
	 * @return returns a list of all employees
	 */
	@Override
	public ResponseEntity<List<Employee>> getAllEmployees() {
		// TODO Auto-generated method stub
		return ResponseEntity.ok(employeeService.getAllEmployees());
	}

	/**
	 * Returns all employees whose name contains or matches the string input provided
	 * @param name as search string
	 * @return returns all employees whose name contains or matches the string input provided
	 */
	@Override
	public ResponseEntity<List<Employee>> getEmployeesByNameSearch(String searchString) {
		// TODO Auto-generated method stub
		return null;
	}

	/**
	 * Returns a single employee by a given id
	 * @param employee id as string
	 * @return returns a single employee
	 */
	@Override
	public ResponseEntity<Employee> getEmployeeById(String id) {
		// TODO Auto-generated method stub
		return null;
	}

	/**
	 * Returns a single integer indicating the highest salary of amongst all employees
	 * @return returns a single integer indicating the highest salary of amongst all employees
	 */
	@Override
	public ResponseEntity<Integer> getHighestSalaryOfEmployees() {
		// TODO Auto-generated method stub
		return null;
	}

	/**
	 * Returns a list of the top 10 employees based off of their salaries
	 * @return returns a list of the top 10 employees based off of their salaries
	 */
	@Override
	public ResponseEntity<List<String>> getTopTenHighestEarningEmployeeNames() {
		// TODO Auto-generated method stub
		return null;
	}

	/**
	 * Returns a single employee, if created, otherwise error
	 * @param EmployeeRequest
	 * @return returns a single employee, if created
	 * @throws error if failed
	 */
	@Override
	public ResponseEntity<Employee> createEmployee(EmployeeRequest employeeRequest) {
		// TODO Auto-generated method stub
		return null;
	}

	/**
	 * Deletes the employee with specified id given
	 * @param employee id as string
	 * @return delete the employee with specified id given
	 * @throws error if failed
	 */
	@Override
	public ResponseEntity<String> deleteEmployeeById(String id) {
		// TODO Auto-generated method stub
		return null;
	}

}
