package com.shrikantanand.productservice.dto;

import com.shrikantanand.productservice.entity.Product;

import lombok.EqualsAndHashCode;
import lombok.Value;

@Value
@EqualsAndHashCode(callSuper = true)
public class ProductDetailDTO extends ProductSummaryDTO {

	private String productName;

	public ProductDetailDTO(Product product) {
		super(product.getProductId(), product.getPrice());
		this.productName = product.getProductName();
	}
	
}
