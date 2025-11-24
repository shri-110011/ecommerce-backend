package com.shrikantanand.productservice.entity;

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
	private byte categoryId;
	
	@Column(name = "category_name")
	private String categoryName;
	
	@OneToMany(mappedBy = "category", cascade = {CascadeType.REFRESH}, fetch = FetchType.LAZY)
	@JsonIgnore
	private List<Product> products;
	
	public void addProduct(Product product) {
		if(this.products == null) {
			this.products = new ArrayList<>();
		}
		this.products.add(product);
		product.setCategory(this);
	}
	
}
