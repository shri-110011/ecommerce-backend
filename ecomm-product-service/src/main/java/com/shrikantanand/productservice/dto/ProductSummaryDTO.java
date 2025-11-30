package com.shrikantanand.productservice.dto;

import java.math.BigDecimal;

import com.shrikantanand.productservice.entity.Product;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@AllArgsConstructor
@NoArgsConstructor()
public class ProductSummaryDTO {
	
	private Integer productId;
	
	private String productName;
	
	private BigDecimal price;
	
	public ProductSummaryDTO(Product product) {
		this.productId = product.getProductId();
		this.productName = product.getProductName();
		this.price = product.getPrice();
	}

}
