package com.shrikantanand.productservice.exception;

import java.util.HashMap;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;

import com.shrikantanand.productservice.dto.ErrorResponse;

@ControllerAdvice
public class GlobalExceptionHandler {
	
	private static final Logger logger = LoggerFactory.getLogger(GlobalExceptionHandler.class);

    @ExceptionHandler(MethodArgumentNotValidException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ResponseBody
    public Map<String, String> handleMethodArgumentNotValid(MethodArgumentNotValidException ex) {
    	logger.error("Inside handleMethodArgumentNotValid()");
    	logger.error(ex.getBindingResult().getAllErrors().toString());
    	
        Map<String, String> errors = new HashMap<>();
        ex.getBindingResult().getAllErrors().forEach((error) -> {
            String fieldName = ((FieldError) error).getField();
            String errorMessage = error.getDefaultMessage();
            errors.put(fieldName, errorMessage);
        });
        
        logger.error(errors.toString());
        return errors;
    }
    
    @ExceptionHandler(HttpMessageNotReadableException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ResponseBody
    public Map<String, String> handleInvalidRequest(HttpMessageNotReadableException ex) {
    	logger.error("Inside handleInvalidRequest()");
    	logger.error(ex.getMessage());
    	
        Map<String, String> errors = new HashMap<>();
        errors.put("message", "Some fields in the request body have have wrong type of values. " 
        + "Please ensure the correct data types are used.");
        return errors;
    }
    
    @ExceptionHandler(CategoryNotFoundException.class)
	public ResponseEntity<ErrorResponse> handleCategoryNotFoundException(
			CategoryNotFoundException ex) {
	    ErrorResponse errorResponse = new ErrorResponse(HttpStatus.NOT_FOUND.value(),
	            ex.getMessage(),
	            System.currentTimeMillis());
	    return new ResponseEntity<>(errorResponse, HttpStatus.NOT_FOUND);
	}
    
}