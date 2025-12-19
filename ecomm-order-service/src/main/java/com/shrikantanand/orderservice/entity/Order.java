package com.shrikantanand.orderservice.entity;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import com.shrikantanand.orderservice.enumeration.OrderStatus;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import lombok.Data;

@Entity
@Table(name = "`order`")
@Data
public class Order {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "order_id")
	private Integer orderId;
	
	@Column(name = "user_id")
	private Integer userId;
	
	@Column(name = "reservation_id")
	private Integer reservationId;
	
	@Column(name = "total_amount")
	private BigDecimal totalAmount;
	
	@Column(name = "status")
	@Enumerated(EnumType.STRING)
	private OrderStatus status;
	
	@Column(name = "created_datetime")
	private LocalDateTime createdDateTime;
	
	@Column(name = "last_updated_datetime")
	private LocalDateTime lastUpdatedDateTime;
	
	@OneToMany(mappedBy = "order", 
			cascade = {
					CascadeType.MERGE, CascadeType.PERSIST, 
					CascadeType.REFRESH, CascadeType.DETACH
			}
	)
	private List<OrderItem> orderItems;
	
	public void addOrderItem(OrderItem orderItem) {
		if(orderItems == null) {
			orderItems = new ArrayList<>();
		}
		orderItems.add(orderItem);
		orderItem.setOrder(this);
	}
	
}
