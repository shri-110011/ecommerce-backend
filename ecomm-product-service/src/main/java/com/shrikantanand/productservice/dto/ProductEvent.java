package com.shrikantanand.productservice.dto;

import com.shrikantanand.productservice.enumeration.ProductEventType;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ProductEvent {
	
	private Integer productId;
	
	private ProductEventType eventType;

}
