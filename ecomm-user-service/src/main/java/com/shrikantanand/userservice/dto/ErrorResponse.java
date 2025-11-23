package com.shrikantanand.userservice.dto;

import lombok.Value;

@Value
public class ErrorResponse {
	
	private int statusCode;
	
	private String message;
	
	private long timestamp;

}
