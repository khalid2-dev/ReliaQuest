package com.reliaquest.api.response;

import java.util.List;

import com.fasterxml.jackson.annotation.JsonProperty;

public class EmployeeResponse {
	
	@JsonProperty("data")
	private Object data;
	
	@JsonProperty("status")
	private String status;
	
	public Object getData() {
		return data;
	}
	public void setData(Object data) {
		this.data = data;
	}
	public String getStatus() {
		return status;
	}
	public void setStatus(String status) {
		this.status = status;
	}
	
}
