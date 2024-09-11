package com.ecom.app.exceptions;

import java.util.HashMap;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.core.AuthenticationException;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.MissingPathVariableException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import com.ecom.app.api.response.APIResponse;

import jakarta.validation.ConstraintViolationException;

@RestControllerAdvice
public class GlobalExceptionHandler {
	
	private static final Logger logger = LoggerFactory.getLogger(GlobalExceptionHandler.class);

	@ExceptionHandler(BadCredentialsException.class)
    public ResponseEntity<APIResponse> handleBadCredentials(BadCredentialsException e) {
		String message = e.getMessage();
		APIResponse res = new APIResponse(message, false);
		return new ResponseEntity<APIResponse>(res, HttpStatus.UNAUTHORIZED);
        
    }
	

	
	
	@ExceptionHandler(AccessDeniedException.class)
    public ResponseEntity<APIResponse> handleAccessDeniedException(AccessDeniedException e) {
        String message = "Access denied: You do not have permission to access this resource.";
        APIResponse res = new APIResponse(message, false);
        return new ResponseEntity<>(res, HttpStatus.FORBIDDEN);
    }
	

	
	@ExceptionHandler(ResourceNotFoundException.class)
	public ResponseEntity<APIResponse> myResourceNotFoundException(ResourceNotFoundException e) {
		String message = e.getMessage();

		APIResponse res = new APIResponse(message, false);

		return new ResponseEntity<APIResponse>(res, HttpStatus.NOT_FOUND);
	}

	@ExceptionHandler(APIException.class)
	public ResponseEntity<APIResponse> myAPIException(APIException e) {
		String message = e.getMessage();

		APIResponse res = new APIResponse(message, false);

		return new ResponseEntity<APIResponse>(res, HttpStatus.BAD_REQUEST);
	}

	
	@ExceptionHandler(ConstraintViolationException.class)
	    public ResponseEntity<Map<String, String>> handleConstraintViolation(ConstraintViolationException e) {
	        Map<String, String> errors = new HashMap<>();

	        e.getConstraintViolations().forEach(violation -> {
	            String fieldName = violation.getPropertyPath().toString();
	            String message = violation.getMessage();
	            errors.put(fieldName, message);
	        });

	        logger.error("Validation errors: {}", errors);

	        return new ResponseEntity<>(errors, HttpStatus.BAD_REQUEST);
	}
	

	
	@ExceptionHandler(AuthenticationException.class)
	public ResponseEntity<String> myAuthenticationException(AuthenticationException e) {

		String res = e.getMessage();
		
		return new ResponseEntity<String>(res, HttpStatus.BAD_REQUEST);
	}
	
	@ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<Map<String, String>> handleMethodArgumentNotValid(MethodArgumentNotValidException ex) {
        Map<String, String> errors = new HashMap<>();
        ex.getBindingResult().getAllErrors().forEach(error -> {
            String fieldName = ((FieldError) error).getField();
            String message = error.getDefaultMessage();
            errors.put(fieldName, message);
        });
        logger.error("Validation errors: {}", errors);
        return new ResponseEntity<>(errors, HttpStatus.BAD_REQUEST);
    }

	@ExceptionHandler(MissingPathVariableException.class)
	public ResponseEntity<APIResponse> myMissingPathVariableException(MissingPathVariableException e) {
		APIResponse res = new APIResponse(e.getMessage(), false);

		return new ResponseEntity<APIResponse>(res, HttpStatus.BAD_REQUEST);
	}
	
	@ExceptionHandler(DataIntegrityViolationException.class)
	public ResponseEntity<APIResponse> myDataIntegrityException(DataIntegrityViolationException e) {
		APIResponse res = new APIResponse(e.getMessage(), false);

		return new ResponseEntity<APIResponse>(res, HttpStatus.BAD_REQUEST);
	}
	

}


