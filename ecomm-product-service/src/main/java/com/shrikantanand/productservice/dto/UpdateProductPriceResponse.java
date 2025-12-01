package com.shrikantanand.productservice.dto;

import java.math.BigDecimal;
import java.time.LocalDateTime;

import com.fasterxml.jackson.annotation.JsonFormat;

import lombok.Value;

@Value
public class UpdateProductPriceResponse {
	
	private Integer productId;
	
	private String productName;
	
	private BigDecimal oldPrice;
	
	private BigDecimal newPrice;
	
	@JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
	private LocalDateTime updatedDateTime;
	
	private String updatedBy;
	
	private String message;

}
