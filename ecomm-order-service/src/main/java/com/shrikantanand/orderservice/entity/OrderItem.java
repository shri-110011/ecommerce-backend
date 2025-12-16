package com.shrikantanand.orderservice.entity;

import java.math.BigDecimal;

import jakarta.persistence.Column;
import jakarta.persistence.EmbeddedId;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.MapsId;
import jakarta.persistence.Table;
import lombok.Data;

@Entity
@Table(name = "order_item")
@Data
public class OrderItem {
	
	@EmbeddedId
	private OrderItemKey orderItemKey;
	
	@Column(name = "quantity")
	private Integer quantity;
	
	@Column(name = "price_at_purchase")
	private BigDecimal priceAtPurchase;
	
	@Column(name = "price_version")
	private Integer priceVersion;
	
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "order_id")
	@MapsId("orderId")
	private Order order;

}
