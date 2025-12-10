package com.shrikantanand.inventoryservice.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import com.shrikantanand.inventoryservice.entity.Inventory;

public interface InventoryRepository extends JpaRepository<Inventory, Integer> {

	@Query("""
			select count(i) from Inventory i where i.productId = :productId
			""")
	public int findByProductId(int productId);

}
