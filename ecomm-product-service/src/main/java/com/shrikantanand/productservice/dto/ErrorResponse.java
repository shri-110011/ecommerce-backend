package com.shrikantanand.productservice.dto;

import lombok.Value;

@Value
public class ErrorResponse {

	private Integer status;
	
	private String message;
	
	private Long timeStamp;
	
}
