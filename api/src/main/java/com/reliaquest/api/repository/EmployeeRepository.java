package com.reliaquest.api.repository;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

import org.springframework.stereotype.Repository;

import com.reliaquest.api.entity.Employee;
import com.reliaquest.api.request.EmployeeRequest;

@Repository
public class EmployeeRepository {
	
	private List<Employee> employeeList = new ArrayList<>();
	
	public EmployeeRepository() {
		//employeeList.add(new Employee("4a3a170b-22cd-4ac2-aad1-9bb5b34a1507", "Tiger Nixon", 320800, 61, "Vice Chair Executive Principal of Chief Operations Implementation Specialist", "tnixon@company.com"));
		//employeeList.add(new Employee("4a3a170b-22cd-4ac2-aad1-9bb5b34a1506", "John Abc", 220000, 56, "Chief Financial Officer", "jabc@company.com"));
		//employeeList.add(new Employee("4a3a170b-22cd-4ac2-aad1-9bb5b34a1505", "Mike Def", 140000, 39, "Financial Advisor", "mdef@company.com"));
		//employeeList.add(new Employee("4a3a170b-22cd-4ac2-aad1-9bb5b34a1504", "Sally Ghi", 210000, 37, "Purchasing Manager", "sghi@company.com"));
		//employeeList.add(new Employee("4a3a170b-22cd-4ac2-aad1-9bb5b34a1503", "Robert Jkl", 175000, 33, "Purchasing Clerk", "rjkl@company.com"));
		//employeeList.add(new Employee("4a3a170b-22cd-4ac2-aad1-9bb5b34a1502", "Lara Mno", 230000, 55, "Finance Director", "lmno@company.com"));
		//employeeList.add(new Employee("4a3a170b-22cd-4ac2-aad1-9bb5b34a1501", "Peter Pqr", 221000, 40, "Purchasing Manager", "ppqr@company.com"));
		//employeeList.add(new Employee("4a3a170b-22cd-4ac2-aad1-9bb5b34a1500", "Smith Stu", 130000, 37, "Senior Finance Analyst", "sstu@company.com"));
		//employeeList.add(new Employee("4a3a170b-22cd-4ac2-aad1-9bb5b34a1499", "Venkat Vwx", 185000, 41, "Payroll Manager", "vvwx@company.com"));
		//employeeList.add(new Employee("4a3a170b-22cd-4ac2-aad1-9bb5b34a1498", "Yara Yz", 150000, 31, "Purchasing Agent", "yyz@company.com"));
		//employeeList.add(new Employee("4a3a170b-22cd-4ac2-aad1-9bb5b34a1497", "Ashley Aa", 70000, 23, "Accountant", "aaa@company.com"));
		//employeeList.add(new Employee("4a3a170b-22cd-4ac2-aad1-9bb5b34a1496", "Brandon Bb", 70500, 24, "Accountant", "bbb@company.com"));
		//employeeList.add(new Employee("4a3a170b-22cd-4ac2-aad1-9bb5b34a1495", "Chris Cc", 200500, 52, "Vice President Finance", "ccc@company.com"));
		//employeeList.add(new Employee("4a3a170b-22cd-4ac2-aad1-9bb5b34a1494", "David Dd", 90000, 28, "Purchasing Manager", "ddd@company.com"));
		//employeeList.add(new Employee("4a3a170b-22cd-4ac2-aad1-9bb5b34a1493", "Edward Ee", 95000, 29, "Credit Analyst", "eee@company.com"));
	}
	
	public List<Employee> findAll(){
		return employeeList;
	}
	
	public Employee findById(String id) {
		for(Employee employee : employeeList) {
			if(employee.getId().equals(id)) {
				return employee;
			}
		}
		return null;
	}
	
	public List<Employee> findBySearchString(String searchString) {
		return employeeList.stream()
						   .filter(emp -> emp.getEmployee_name()
						   .toLowerCase()
						   .contains(searchString.toLowerCase()))
						   .collect(Collectors.toList());
	}
	
	public int findHighestSalary() {
		employeeList.stream().sorted();
		return employeeList.get(0).getEmployee_salary();
	}
	
	public List<String> findTopTenEarningNames(){
		return employeeList.stream()
						.sorted(Comparator.comparingInt(Employee::getEmployee_salary).reversed())
						.limit(10)
						.map(x -> x.getEmployee_name())
						.collect(Collectors.toList());
	}
	
	public Employee addEmployee(EmployeeRequest employeeRequest) {
		Employee newEmployee = new Employee();
		
		employeeList.add(newEmployee);
		return newEmployee;
	}
	
	public String deleteEmployee(String id) {
		for(Employee emp : employeeList) {
			if(emp.getId().equals(id)) {
				employeeList.remove(emp);
				return "Employee with id "+id+" has been successfully deleted";
			}
		}
		return "Employee not found";
	}

}
