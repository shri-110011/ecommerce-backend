package com.shrikantanand.inventoryservice.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import com.shrikantanand.inventoryservice.dto.ProductStockDTO;
import com.shrikantanand.inventoryservice.entity.Inventory;

public interface InventoryRepository extends JpaRepository<Inventory, Integer> {

	@Query("""
			select count(i) from Inventory i where i.productId = :productId
			""")
	public int findByProductId(int productId);

	@Query("""
			select new com.shrikantanand.inventoryservice.dto.ProductStockDTO(i.productId, i.actualStock) 
			from Inventory i where i.productId in :productIds
			""")
	public List<ProductStockDTO> getProductsStockInfo(List<Integer> productIds);

}
