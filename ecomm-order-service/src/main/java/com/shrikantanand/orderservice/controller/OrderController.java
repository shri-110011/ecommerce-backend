package com.shrikantanand.orderservice.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.shrikantanand.orderservice.api.response.ApiResponse;
import com.shrikantanand.orderservice.dto.OrderRequest;
import com.shrikantanand.orderservice.dto.OrderResponse;
import com.shrikantanand.orderservice.enumeration.OrderErrorCode;
import com.shrikantanand.orderservice.exception.DependencyFailureException;
import com.shrikantanand.orderservice.exception.InsufficientStockException;
import com.shrikantanand.orderservice.exception.PriceValidationFailedException;
import com.shrikantanand.orderservice.service.OrderService;

import jakarta.validation.Valid;

@RestController
@RequestMapping("/api/orders")
public class OrderController {
	
	@Autowired
	private OrderService orderService;
	
	@PostMapping("/place-order")
	public ApiResponse<OrderResponse> placeOrder(@RequestBody @Valid OrderRequest request) {
		return orderService.placeOrder(request);
	}
	
	@ExceptionHandler(InsufficientStockException.class)
	public ResponseEntity<ApiResponse<OrderResponse>> handleInsufficientStockException(
			InsufficientStockException exc) {
		int status = HttpStatus.CONFLICT.value();
		String errorCode = exc.getErrorCode().name();
		return ResponseEntity.status(status).body(ApiResponse.failure(exc.getMessage(), 
				errorCode, exc.getDetails()));
	}
	
	@ExceptionHandler(DependencyFailureException.class)
	public ResponseEntity<ApiResponse<OrderResponse>> handleDependencyFailureException(
			DependencyFailureException exc) {
		int status = exc.getStatus();
		String errorCode = exc.getErrorCode().name();
		return ResponseEntity.status(status).body(ApiResponse.failure(exc.getMessage(), 
				errorCode, null));
	}

	@ExceptionHandler(PriceValidationFailedException.class)
	public ResponseEntity<ApiResponse<OrderResponse>> handlePriceValidationFailed(
			PriceValidationFailedException exc) {
		if(exc.getErrorCode() == OrderErrorCode.INVALID_ITEM) {
			int status = HttpStatus.BAD_REQUEST.value();
			String message = "Price validation failed for an item because it was not found!";
			String errorCode = exc.getErrorCode().name();
			return ResponseEntity.status(status).body(ApiResponse.failure(message, 
					errorCode, exc.getDetails()));
		}
		else if(exc.getErrorCode() == OrderErrorCode.PRICE_MISMATCH) {
			int status = HttpStatus.CONFLICT.value();
			String message = "Price validation failed because for one or more items price has drifted!";
			String errorCode = exc.getErrorCode().name();
			return ResponseEntity.status(status).body(ApiResponse.failure(message, 
					errorCode, exc.getDetails()));
		}
		return null;
	}
	
}
