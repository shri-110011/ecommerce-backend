package com.shrikantanand.orderservice.exception;

import java.util.List;

import com.shrikantanand.orderservice.api.response.ErrorDetail;
import com.shrikantanand.orderservice.enumeration.OrderErrorCode;

import lombok.Getter;

@Getter
public class PriceValidationFailedException extends RuntimeException {
	
	private static final long serialVersionUID = 1L;
	
	private final OrderErrorCode errorCode;
	
	private final List<ErrorDetail> details;
	
	public PriceValidationFailedException(OrderErrorCode errorCode) {
		super();
		this.errorCode = errorCode;
		this.details = null;
	}
	
	public PriceValidationFailedException(OrderErrorCode errorCode, List<ErrorDetail> details) {
		super();
		this.errorCode = errorCode;
		this.details = details;
	}
	
}
