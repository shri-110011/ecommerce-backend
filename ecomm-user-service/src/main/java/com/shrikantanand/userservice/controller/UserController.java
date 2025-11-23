package com.shrikantanand.userservice.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.shrikantanand.userservice.dto.AddUserRequest;
import com.shrikantanand.userservice.dto.AddUserResponse;
import com.shrikantanand.userservice.dto.ErrorResponse;
import com.shrikantanand.userservice.exception.UserException;
import com.shrikantanand.userservice.service.UserService;

import jakarta.validation.Valid;

@RestController
@RequestMapping("/api/users")
public class UserController {
	
	@Autowired
	UserService userService;
	
	@PostMapping
	public AddUserResponse registerUser(@Valid @RequestBody AddUserRequest request) {
		return userService.registerUser(request);
	}
	
	@PostMapping("/validate-user/{id}")
	public String validateUser(@PathVariable int id) {
		return userService.validateUser(id);
	}
	
	@ExceptionHandler(UserException.class)
	public ResponseEntity<ErrorResponse> handleUserException(UserException ex) {
	    ErrorResponse errorResponse = new ErrorResponse(
	            ex.getStatusCode(),
	            ex.getMessage(),
	            System.currentTimeMillis()
	    );
	    return new ResponseEntity<>(errorResponse, HttpStatus.INTERNAL_SERVER_ERROR);
	}

}
