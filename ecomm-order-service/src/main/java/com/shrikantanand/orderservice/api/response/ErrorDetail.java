package com.shrikantanand.orderservice.api.response;

import lombok.Value;

@Value
public class ErrorDetail {
	
	private String key;
	
    private Object value;

}
