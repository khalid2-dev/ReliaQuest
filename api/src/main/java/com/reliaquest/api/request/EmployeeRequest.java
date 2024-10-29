package com.reliaquest.api.request;

import java.util.Optional;

import javax.validation.constraints.Max;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;

public class EmployeeRequest {

	private String id;
	
	@NotNull(message = "Name must not be empty")
    private String employeeName;
	
	@Min(value = 1, message = "Salary must be greater than zero")
	@NotNull
    private int employeeSalary;
	
	@Min(value = 16, message = "Age must be at least 16")
	@Max(value = 75, message = "Age must not be more than 75")
	@NotNull
    private int employeeAge;
	
    @NotNull(message = "Title must not be empty")
    private String employeeTitle;
    
    private String employeeEmail;
    
    public EmployeeRequest() {
    }
    
	public EmployeeRequest(@NotNull(message = "Name must not be empty") String employeeName,
			@Min(value = 1, message = "Salary must be greater than zero") @NotNull int employeeSalary,
			@Min(value = 16, message = "Age must be at least 16") @Max(value = 75, message = "Age must not be more than 75") @NotNull int employeeAge,
			@NotNull(message = "Title must not be empty") String employeeTitle) {
		this.employeeName = employeeName;
		this.employeeSalary = employeeSalary;
		this.employeeAge = employeeAge;
		this.employeeTitle = employeeTitle;
	}
	public String getId() {
		return id;
	}
	public void setId(String id) {
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
	public void setEmployeeSalary(int employeeSalary) {
		this.employeeSalary = employeeSalary;
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
	public Optional<String> getEmployeeEmail() {
		return Optional.ofNullable(employeeEmail);
	}
	public void setEmployeeEmail(String employeeEmail) {
		this.employeeEmail = employeeEmail;
	}

}
