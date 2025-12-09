package com.shrikantanand.productservice.dto;

import com.shrikantanand.productservice.enumeration.ProductLifecycleEventType;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ProductLifecycleEvent {
	
	private Integer productId;
	
	private ProductLifecycleEventType eventType;

}
