package com.shrikantanand.productservice.dto;

import java.math.BigDecimal;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import lombok.Value;

@Value
public class AddProductRequest {
	
	@NotNull(message = "must not be null")
	@NotBlank(message = "must contain at least one non-whitespace character")
	private String productName;
	
	@NotNull(message = "must not be null")
	@Positive(message = "must be positive")
	private BigDecimal productPrice;
	
	@NotNull(message = "must not be null")
	private Byte categoryId;

}
