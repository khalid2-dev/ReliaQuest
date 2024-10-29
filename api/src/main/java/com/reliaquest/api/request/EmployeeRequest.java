package com.reliaquest.api.request;

import java.util.Optional;

import javax.validation.constraints.Max;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;

public class EmployeeRequest {

	private String id;
	
	@NotNull(message = "Name must not be empty")
    private String employee_name;
	
	@Min(value = 1, message = "Salary must be greater than zero")
	@NotNull
    private int employee_salary;
	
	@Min(value = 16, message = "Age must be at least 16")
	@Max(value = 75, message = "Age must not be more than 75")
	@NotNull
    private int employee_age;
	
    @NotNull(message = "Title must not be empty")
    private String employee_title;
    
    private String employee_email;
    
    public EmployeeRequest() {
    }
    
	public EmployeeRequest(String id, @NotNull(message = "Name must not be empty") String employee_name,
			@Min(value = 1, message = "Salary must be greater than zero") @NotNull int employee_salary,
			@Min(value = 16, message = "Age must be at least 16") @Max(value = 75, message = "Age must not be more than 75") @NotNull int employee_age,
			@NotNull(message = "Title must not be empty") String employee_title, String employee_email) {
		super();
		this.id = id;
		this.employee_name = employee_name;
		this.employee_salary = employee_salary;
		this.employee_age = employee_age;
		this.employee_title = employee_title;
		this.employee_email = employee_email;
	}
	public String getId() {
		return id;
	}
	public void setId(String id) {
		this.id = id;
	}
	public String getEmployee_name() {
		return employee_name;
	}
	public void setEmployee_name(String employee_name) {
		this.employee_name = employee_name;
	}
	public int getEmployee_salary() {
		return employee_salary;
	}
	public void setEmployee_salary(int employee_salary) {
		this.employee_salary = employee_salary;
	}
	public int getEmployee_age() {
		return employee_age;
	}
	public void setEmployee_age(int employee_age) {
		this.employee_age = employee_age;
	}
	public String getEmployee_title() {
		return employee_title;
	}
	public void setEmployee_title(String employee_title) {
		this.employee_title = employee_title;
	}
	public Optional<String> getEmployee_email() {
		return Optional.ofNullable(employee_email);
	}
	public void setEmployee_email(String employee_email) {
		this.employee_email = employee_email;
	}

}
