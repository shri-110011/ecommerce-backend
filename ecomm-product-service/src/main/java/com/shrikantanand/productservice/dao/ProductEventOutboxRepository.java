package com.shrikantanand.productservice.dao;

import java.util.List;

import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import com.shrikantanand.productservice.entity.ProductEventOutbox;

public interface ProductEventOutboxRepository extends JpaRepository<ProductEventOutbox, Integer> {

	@Query("""
			select pe from ProductEventOutbox pe 
			where pe.nextRetryAt <= current_timestamp 
			and pe.isProcessed = 'N' 
			order by createdDateTime asc
			""")
	List<ProductEventOutbox> getAllPendingProductEvents(Pageable pageable);

}
