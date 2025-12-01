package com.shrikantanand.productservice.entity;

import java.math.BigDecimal;
import java.time.LocalDateTime;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import lombok.Data;

@Entity
@Table(name = "product")
@Data
public class Product {
	
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "product_id")
	private Integer productId;
	
	@Column(name = "product_name")
	private String productName;
	
	@Column(name = "price")
	private BigDecimal price;
	
	@Column(name = "actual_stock")
	private Integer actualStock;
	
	@Column(name = "price_version")
	private Integer priceVersion;
	
	@Column(name = "is_active")
	private Character isActive;
	
	@Column(name = "created_datetime")
	private LocalDateTime createdDateTime;
	
	@Column(name = "created_by")
	private String createdBy;
	
	@Column(name = "last_updated_datetime")
	private LocalDateTime lastUpdatedDateTime;
	
	@Column(name = "last_updated_by")
	private String lastUpdatedBy;
	
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "category_id")
	private Category category;
	
}
