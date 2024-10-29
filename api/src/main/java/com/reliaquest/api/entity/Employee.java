package com.reliaquest.api.entity;
import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.Optional;
import java.util.UUID;

public class Employee {

	@JsonProperty("id")
	private UUID id;
    
	@JsonProperty("employee_name")
	private String employeeName;
	
	@JsonProperty("employee_salary")
    private int employeeSalary;
    
	@JsonProperty("employee_age")
	private int employeeAge;
    
	@JsonProperty("employee_title")
	private String employeeTitle;
    
	@JsonProperty("employee_email")
	private String employeeEmail;
    
    public Employee() {
    	
    }
    
    public Employee(UUID id, String employeeName, int employeeSalary, int employeeAge, String employeeTitle, String employeeEmail) {
    	this.id = id;
        this.employeeName = employeeName;
        this.employeeSalary = employeeSalary;
        this.employeeAge = employeeAge;
        this.employeeTitle = employeeTitle;
        this.employeeEmail = employeeEmail;
    }
    
	public UUID getId() {
		return id;
	}
	public void setId(UUID id) {
		this.id = id;
	}
	public String getEmployeeName() {
		return employeeName;
	}
	public void setEmployeeName(String employeeName) {
		this.employeeName = employeeName;
	}
	public int getEmployeeSalary() {
		return employeeSalary;
	}
	public void setEmployeeSalary(int employeesalary) {
		this.employeeSalary = employeesalary;
	}
	public int getEmployeeAge() {
		return employeeAge;
	}
	public void setEmployeeAge(int employeeAge) {
		this.employeeAge = employeeAge;
	}
	public String getEmployeeTitle() {
		return employeeTitle;
	}
	public void setEmployeeTitle(String employeeTitle) {
		this.employeeTitle = employeeTitle;
	}
	public String getEmployeeEmail() {
		return employeeEmail;
	}
	public void setEmployeeEmail(String employeeEmail) {
		this.employeeEmail = employeeEmail;
	}

}
