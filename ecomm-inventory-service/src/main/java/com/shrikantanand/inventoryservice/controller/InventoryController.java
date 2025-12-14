package com.shrikantanand.inventoryservice.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.shrikantanand.inventoryservice.dto.ErrorResponse;
import com.shrikantanand.inventoryservice.dto.ReserveItemsRequest;
import com.shrikantanand.inventoryservice.dto.ReserveItemsResponse;
import com.shrikantanand.inventoryservice.dto.StockValidationRequest;
import com.shrikantanand.inventoryservice.dto.StockValidationResponse;
import com.shrikantanand.inventoryservice.exception.StockReservationFailedException;
import com.shrikantanand.inventoryservice.service.InventoryService;

import jakarta.validation.Valid;

@RestController
@RequestMapping("/api/inventory")
public class InventoryController {
	
	@Autowired
	private InventoryService inventoryService;
	
	@PostMapping("/stock/validate")
	public StockValidationResponse validateStock(@RequestBody @Valid 
			StockValidationRequest request) {
		return inventoryService.validateStock(request);
	}
	
	@PostMapping("/stock/reserve")
	public ReserveItemsResponse reserveItems(@RequestBody @Valid ReserveItemsRequest request) {
		return inventoryService.reserveItems(request);
	}
	
	@ExceptionHandler(StockReservationFailedException.class)
	public ResponseEntity<ErrorResponse> handleStockReservationFailed(
			StockReservationFailedException exc) {
		int status = HttpStatus.BAD_REQUEST.value();
		String message = exc.getMessage();
		long timestamp = System.currentTimeMillis();
		ErrorResponse errorResponse = new ErrorResponse(status, message, timestamp);
		return ResponseEntity.badRequest().body(errorResponse);
	}

}
