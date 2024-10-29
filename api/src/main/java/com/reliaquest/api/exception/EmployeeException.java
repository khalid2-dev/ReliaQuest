package com.reliaquest.api.exception;

import java.util.List;

import org.springframework.core.Ordered;
import org.springframework.core.annotation.Order;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.server.ResponseStatusException;

import com.reliaquest.api.response.ErrorResponse;

@Order(Ordered.HIGHEST_PRECEDENCE)
@ControllerAdvice(annotations = RestController.class)
public class EmployeeException {
	
	@ExceptionHandler(ResponseStatusException.class)
	public ResponseEntity<ErrorResponse> statusException(ResponseStatusException ex){
		ErrorResponse response = new ErrorResponse(ex.getReason(), null);
		return ResponseEntity.status(ex.getStatusCode()).body(response);
	}
	
//	@ExceptionHandler(EmployeeNotFoundException.class)
//	public ResponseEntity<ErrorResponse> handleEmployeeNotFound(EmployeeNotFoundException ex) {
//		return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Employee not found. "+ex.getMessage());
//	}
	
	@ExceptionHandler(GeneralException.class)
	public ResponseEntity<ErrorResponse> generalException(GeneralException ex){
		ErrorResponse response = new ErrorResponse("An unexpected error occured.", List.of(ex.getMessage()));
		return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);
	}

}
