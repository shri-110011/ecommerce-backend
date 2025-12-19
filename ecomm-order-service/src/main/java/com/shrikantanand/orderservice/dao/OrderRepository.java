package com.shrikantanand.orderservice.dao;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import com.shrikantanand.orderservice.dto.OrderReservationId;
import com.shrikantanand.orderservice.entity.Order;

public interface OrderRepository extends JpaRepository<Order, Integer> {

	@Query("""
			select new com.shrikantanand.orderservice.dto.OrderReservationId(o.orderId, 
			o.reservationId) from Order o 
			where o.orderId in (:orderIds)
			""")
	List<OrderReservationId> findReservationIdsforOrderIds(List<Integer> orderIds);

}
