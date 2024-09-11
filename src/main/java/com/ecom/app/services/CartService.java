package com.ecom.app.services;

import java.util.List;

import com.ecom.app.dto.CartDTO;


public interface CartService {
	
	
	
	List<CartDTO> getAllCarts();
	
	
	CartDTO updateProductQuantityInCart(Long cartId, Long productId, Integer quantity);
	
	void updateProductInCarts(Long cartId, Long productId);
	
	String deleteProductFromCart(Long cartId, Long productId);
	
	CartDTO addProductToCart(Long cartId, Long productId, Integer quantity);
	

	
}
