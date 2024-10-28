package com.reliaquest.api.request;

import javax.validation.constraints.NotNull;

public class DeleteEmployeeRequest {
	
	@NotNull
	private String name;

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

}
