package com.shrikantanand.productservice.dao;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Lock;
import org.springframework.data.jpa.repository.Query;

import com.shrikantanand.productservice.dto.PriceValidationItem;
import com.shrikantanand.productservice.entity.Product;

import jakarta.persistence.LockModeType;

public interface ProductRepository extends JpaRepository<Product, Integer> {

	@Query("select p.price from Product p where p.productId = :productId")
	BigDecimal getProductPrice(int productId);

	@Lock(LockModeType.PESSIMISTIC_WRITE)
	@Query("select p from Product p where p.productId = :productId")
	Optional<Product> getProductForUpdate(Integer productId);

	@Query(""" 
			select max(pph.currentPriceVersion) from ProductPriceHistory pph 
			where pph.product.productId = :productId
			""")
	Integer getLatestPriceVersion(Integer productId);

	@Query(""" 
			select new com.shrikantanand.productservice.dto.PriceValidationItem(
			p.productId, p.price) from Product p 
			where p.productId in :productIds 
			and p.isActive = 'Y'
			""")
	List<PriceValidationItem> getProductsPrice(List<Integer> productIds);

}
