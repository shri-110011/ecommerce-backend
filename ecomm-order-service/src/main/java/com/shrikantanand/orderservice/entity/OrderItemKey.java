package com.shrikantanand.orderservice.entity;

import jakarta.persistence.Embeddable;
import lombok.Data;

@Embeddable
@Data
public class OrderItemKey {
	
	private Integer orderId;
	
	private Integer productId;

}
