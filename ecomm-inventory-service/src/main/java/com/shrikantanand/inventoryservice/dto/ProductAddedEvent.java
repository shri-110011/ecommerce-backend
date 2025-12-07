package com.shrikantanand.inventoryservice.dto;

import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class ProductAddedEvent {
	
	private Integer productId;

}
