package com.shrikantanand.inventoryservice.repository;

import java.time.LocalDateTime;
import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
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

	@Modifying
	@Query("""
			update Inventory i 
			set i.actualStock = i.actualStock - :requestedQuantity, 
			i.lastUpdatedDateTime = :lastUpdatedDateTime, 
			lastUpdatedBy = :lastUpdatedBy 
			where i.productId = :productId and 
			i.actualStock >= :requestedQuantity
			""")
	public int deductStock(Integer productId, Integer requestedQuantity, 
			LocalDateTime lastUpdatedDateTime, String lastUpdatedBy);

}
