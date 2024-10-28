package com.reliaquest.api.response;

import java.util.List;

public class ErrorResponse {
	
    private String status;
    private List<String> error;
    
    public ErrorResponse(String status, List<String> error) {
        this.status = status;
        this.error = error;
    }
    
	public String getStatus() {
		return status;
	}
	public void setStatus(String status) {
		this.status = status;
	}

	public List<String> getError() {
		return error;
	}

	public void setError(List<String> error) {
		this.error = error;
	}

}
