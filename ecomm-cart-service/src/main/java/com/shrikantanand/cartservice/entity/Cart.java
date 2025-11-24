package com.shrikantanand.cartservice.entity;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import com.shrikantanand.cartservice.enumeration.CartStatus;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import lombok.Data;

@Entity
@Table(name = "cart")
@Data
public class Cart {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "cart_id")
	private Integer cartId;
	
	@Column(name = "user_id")
	private Integer userId;
	
	@Column(name = "order_id")
	private Integer orderId;
	
	@Column(name = "created_datetime")
	private LocalDateTime createdDateTime;
	
	@Column(name = "expiration_datetime")
	private LocalDateTime expirationDatetime;
	
	@Column(name = "last_updated_datetime")
	private LocalDateTime lastUpdatedDatetime;
	
	@Column(name = "status")
	private CartStatus status;
	
	@OneToMany(mappedBy = "cart")
	private List<CartItem> cartItems;
	
	public void addCartItem(CartItem cartItem) {
		if(this.cartItems == null) {
			this.cartItems = new ArrayList<>();
		}
		this.cartItems.add(cartItem);
		cartItem.setCart(this);
	}
	
}
