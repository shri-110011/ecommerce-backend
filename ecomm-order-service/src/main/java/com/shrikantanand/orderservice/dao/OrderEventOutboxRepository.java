package com.shrikantanand.orderservice.dao;

import java.util.List;

import org.springframework.data.domain.PageRequest;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import com.shrikantanand.orderservice.entity.OrderEventOutbox;

public interface OrderEventOutboxRepository extends JpaRepository<OrderEventOutbox, Integer> {

	@Query("""
			select oe from OrderEventOutbox oe 
			where oe.nextRetryAt <= current_timestamp 
			and oe.isProcessed = 'N' 
			order by createdDateTime asc
			""")
	List<OrderEventOutbox> getAllPendingProductEvents(PageRequest ofSize);

}
