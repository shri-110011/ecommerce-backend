package com.shrikantanand.orderservice.dto;

import lombok.Value;

@Value
public class OrderReservationId {
	
	private Integer orderId;
	
	private Integer reservationId;

}
