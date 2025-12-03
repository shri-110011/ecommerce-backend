package com.shrikantanand.productservice.dto;

import java.math.BigDecimal;
import java.time.LocalDateTime;

import com.fasterxml.jackson.annotation.JsonFormat;

import lombok.Value;

@Value
public class AddProductResponse {
	
	private Integer productId;
	
	private String productName;
	
	private BigDecimal productPrice;
	
	private Byte categoryId;
	
	@JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
	private LocalDateTime createdDateTime;
	
	private String createdBy;
	
	private Character isActive;
	
	private String message;

}
