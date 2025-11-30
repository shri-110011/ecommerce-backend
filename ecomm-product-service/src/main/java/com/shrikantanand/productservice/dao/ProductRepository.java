package com.shrikantanand.productservice.dao;

import java.math.BigDecimal;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import com.shrikantanand.productservice.entity.Product;

public interface ProductRepository extends JpaRepository<Product, Integer> {

	@Query("select p.price from Product p where p.productId = :productId")
	BigDecimal getProductPrice(int productId);

}
