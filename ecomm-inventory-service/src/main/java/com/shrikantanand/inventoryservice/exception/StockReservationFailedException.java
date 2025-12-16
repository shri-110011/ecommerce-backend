package com.shrikantanand.inventoryservice.exception;

import java.util.List;

import com.shrikantanand.inventoryservice.api.response.ErrorDetail;
import com.shrikantanand.inventoryservice.enumeration.ReservationErrorCode;

import lombok.Getter;

@Getter
public class StockReservationFailedException extends RuntimeException {
	
	private static final long serialVersionUID = 1L;
	
	private final ReservationErrorCode errorCode;
	
	private final List<ErrorDetail> details;

	public StockReservationFailedException(ReservationErrorCode errorCode, List<ErrorDetail> details) {
		super();
		this.errorCode = errorCode;
		this.details = details;
	}
	
}
