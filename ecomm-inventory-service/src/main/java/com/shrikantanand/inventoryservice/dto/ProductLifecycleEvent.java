package com.shrikantanand.inventoryservice.dto;

import com.shrikantanand.inventoryservice.enumeration.ProductLifecycleEventType;

import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class ProductLifecycleEvent {
	
	private Integer productId;
	
	private ProductLifecycleEventType eventType;

}
