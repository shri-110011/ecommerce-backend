package com.shrikantanand.orderservice.exception;

import java.util.List;

import com.shrikantanand.orderservice.api.response.ErrorDetail;
import com.shrikantanand.orderservice.enumeration.OrderErrorCode;

import lombok.Getter;

@Getter
public class InsufficientStockException extends RuntimeException {
	
	private static final long serialVersionUID = 1L;
	
	private final OrderErrorCode errorCode;
	
	private final List<ErrorDetail> details;

	public InsufficientStockException(OrderErrorCode errorCode, List<ErrorDetail> details) {
		super("Stock reservation failed for an item because of insufficient stock!");
		this.errorCode = errorCode;
		this.details = details;
	}
	
}
