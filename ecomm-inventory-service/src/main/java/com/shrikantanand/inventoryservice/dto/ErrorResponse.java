package com.shrikantanand.inventoryservice.dto;

import lombok.Value;

@Value
public class ErrorResponse {
	
	public int status;
	
	public String message;
	
	public long timestamp;

}
