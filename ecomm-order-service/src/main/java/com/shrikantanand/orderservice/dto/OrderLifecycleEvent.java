package com.shrikantanand.orderservice.dto;

import com.shrikantanand.orderservice.enumeration.OrderLifecycleEventType;

import lombok.Value;

@Value
public class OrderLifecycleEvent {
	
	private Integer reservationId;
	
	private OrderLifecycleEventType eventType;

}
