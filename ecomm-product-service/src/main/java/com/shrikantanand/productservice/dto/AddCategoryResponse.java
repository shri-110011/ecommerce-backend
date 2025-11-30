package com.shrikantanand.productservice.dto;

import java.time.LocalDateTime;

import com.fasterxml.jackson.annotation.JsonFormat;

import lombok.Value;

@Value
public class AddCategoryResponse {
	
	private Byte categoryId;
	
	private String categoryName;
	
	@JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
	private LocalDateTime createdDateTime;
	
	private String createdBy;
	
	private Character isActive;
	
	private String message;

}
