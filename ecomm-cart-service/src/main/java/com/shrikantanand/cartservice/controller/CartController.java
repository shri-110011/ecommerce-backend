package com.shrikantanand.cartservice.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.shrikantanand.cartservice.dto.CartItemMutationResponse;
import com.shrikantanand.cartservice.enumeration.ItemQtyUpdateType;
import com.shrikantanand.cartservice.model.CartItem;
import com.shrikantanand.cartservice.service.CartCacheService;

@RestController
@RequestMapping("/api/carts")
public class CartController {
	
	@Autowired
	private CartCacheService cartCacheService;
	
	@PostMapping("/{cartId}/items/{productId}")
	public CartItemMutationResponse addItemToCart(@PathVariable int cartId, 
			@PathVariable int productId) {
		return cartCacheService.addItemToCart(cartId, productId);
	}
	
	@PatchMapping("/{cartId}/items/{productId}")
	public CartItemMutationResponse updateQuantity(@PathVariable int cartId, 
			@PathVariable int productId, @RequestParam ItemQtyUpdateType action) {
		return cartCacheService.updateItemQuantity(cartId, productId, action);
	}

	@GetMapping("/{cartId}/items")
	public List<CartItem> getCartItems(@PathVariable int cartId) {
		return cartCacheService.getCartItems(cartId);
	}

}
