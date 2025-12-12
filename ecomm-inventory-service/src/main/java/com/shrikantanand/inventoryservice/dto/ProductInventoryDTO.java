package com.shrikantanand.inventoryservice.dto;

import lombok.Value;

@Value
public class ProductInventoryDTO {
	
	private Integer productId;
	
	private Integer avaialableQuantity;

}
