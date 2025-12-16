package com.shrikantanand.inventoryservice.api.response;

import java.util.List;

import lombok.Value;

@Value
public class ApiError {
	
	private String errorCode;
	
    private List<ErrorDetail> details;

}
