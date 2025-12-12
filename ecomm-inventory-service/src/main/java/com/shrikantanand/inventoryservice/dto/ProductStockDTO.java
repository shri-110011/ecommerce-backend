package com.shrikantanand.inventoryservice.dto;

import lombok.Value;

@Value
public class ProductStockDTO {

	private Integer productId;
	
	private Integer actualStock;
	
}
