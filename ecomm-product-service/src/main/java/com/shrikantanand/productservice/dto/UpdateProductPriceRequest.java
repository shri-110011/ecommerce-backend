package com.shrikantanand.productservice.dto;

import java.math.BigDecimal;

import jakarta.validation.constraints.NotNull;
import lombok.Value;

@Value
public class UpdateProductPriceRequest {
	
	@NotNull(message = "must not be null")
	private BigDecimal newPrice;

}
