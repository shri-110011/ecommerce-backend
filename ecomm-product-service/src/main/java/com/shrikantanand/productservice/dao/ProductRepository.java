package com.shrikantanand.productservice.dao;

import org.springframework.data.jpa.repository.JpaRepository;

import com.shrikantanand.productservice.entity.Product;

public interface ProductRepository extends JpaRepository<Product, Integer> {

}
