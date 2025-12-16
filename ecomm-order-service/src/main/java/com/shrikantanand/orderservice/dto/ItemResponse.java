package com.shrikantanand.orderservice.dto;

import lombok.Value;
import lombok.experimental.NonFinal;

@Value
@NonFinal
public class ItemResponse {

	private Integer productId;

	private Integer requestedQuantity;
	
}
