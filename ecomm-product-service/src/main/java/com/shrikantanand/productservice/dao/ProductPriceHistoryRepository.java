package com.shrikantanand.productservice.dao;

import org.springframework.data.jpa.repository.JpaRepository;

import com.shrikantanand.productservice.entity.ProductPriceHistory;

public interface ProductPriceHistoryRepository extends JpaRepository<ProductPriceHistory, Integer> {

}
