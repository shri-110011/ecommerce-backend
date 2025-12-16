package com.shrikantanand.orderservice.exception;

import com.shrikantanand.orderservice.enumeration.OrderErrorCode;

import lombok.Getter;

@Getter
public class DependencyFailureException extends RuntimeException {

	private static final long serialVersionUID = 1L;
	
	private final OrderErrorCode errorCode;
	
	private final int status;
	
	public DependencyFailureException(OrderErrorCode errorCode, int status, 
			String message) {
		super(message);
		this.errorCode = errorCode;
		this.status = status;
	}
	
}
