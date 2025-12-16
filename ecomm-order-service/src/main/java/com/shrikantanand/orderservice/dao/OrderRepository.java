package com.shrikantanand.orderservice.dao;

import org.springframework.data.jpa.repository.JpaRepository;

import com.shrikantanand.orderservice.entity.Order;

public interface OrderRepository extends JpaRepository<Order, Integer> {

}
