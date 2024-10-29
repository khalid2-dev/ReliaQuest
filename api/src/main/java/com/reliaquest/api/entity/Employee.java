package com.reliaquest.api.entity;
import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.Optional;
import java.util.UUID;

public class Employee {

	@JsonProperty("id")
	private UUID id;
    
	@JsonProperty("employee_name")
	private String employee_name;
	
	@JsonProperty("employee_salary")
    private int employee_salary;
    
	@JsonProperty("employee_age")
	private int employee_age;
    
	@JsonProperty("employee_title")
	private String employee_title;
    
	@JsonProperty("employee_email")
	private String employee_email;
    
    public Employee() {
    	
    }
    
    public Employee(UUID id, String employee_name, int employee_salary, int employee_age, String employee_title, String employee_email) {
    	this.id = id;
        this.employee_name = employee_name;
        this.employee_salary = employee_salary;
        this.employee_age = employee_age;
        this.employee_title = employee_title;
        this.employee_email = employee_email;
    }
    
	public UUID getId() {
		return id;
	}
	public void setId(UUID id) {
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
