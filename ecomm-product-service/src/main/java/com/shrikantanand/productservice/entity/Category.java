package com.shrikantanand.productservice.entity;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonIgnore;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import lombok.Data;

@Entity
@Table(name = "category")
@Data
public class Category {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "category_id")
	private Byte categoryId;
	
	@Column(name = "category_name")
	private String categoryName;
	
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
	
	@OneToMany(mappedBy = "category")
	@JsonIgnore
	private List<Product> products;
	
	public void addProduct(Product product) {
		if(products == null) {
			products = new ArrayList<>();
		}
		products.add(product);
		product.setCategory(this);
	}
	
}
