package com.shrikantanand.userservice.exception;

public class UserException extends RuntimeException {

	private static final long serialVersionUID = 1L;
	
	private int statusCode;

	public UserException(int statusCode, String message) {
		super(message);
		this.statusCode = statusCode;
	}
	
	public int getStatusCode() {
		return statusCode;
	}

}
