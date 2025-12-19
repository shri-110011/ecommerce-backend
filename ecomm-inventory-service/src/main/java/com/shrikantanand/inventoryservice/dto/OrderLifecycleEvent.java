package com.shrikantanand.inventoryservice.dto;

import com.shrikantanand.inventoryservice.enumeration.OrderLifecycleEventType;

import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class OrderLifecycleEvent {
	
	private Integer reservationId;
	
	private OrderLifecycleEventType eventType;

}
