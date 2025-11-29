package com.shrikantanand.productservice.dto;

import java.math.BigDecimal;

import lombok.Value;
import lombok.experimental.NonFinal;

@Value
@NonFinal
public class ProductSummaryDTO {
	
	private int productId;
	
	private BigDecimal price;

}
