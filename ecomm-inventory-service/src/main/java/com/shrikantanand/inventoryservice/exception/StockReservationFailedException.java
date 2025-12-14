package com.shrikantanand.inventoryservice.exception;

public class StockReservationFailedException extends RuntimeException {
	
	private static final long serialVersionUID = 1L;

	public StockReservationFailedException(String message) {
		super(message);
	}
	
}
