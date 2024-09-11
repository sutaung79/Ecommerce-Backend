package com.ecom.app.controllers;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.*;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import com.ecom.app.dto.CartDTO;
import com.ecom.app.services.CartService;

import java.util.ArrayList;
import java.util.List;

class CartControllerTest {

    @InjectMocks
    private CartController cartController;

    @Mock
    private CartService cartService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void testAddProductToCart() {
        
        CartDTO cartDTO = new CartDTO();
        when(cartService.addProductToCart(anyLong(), anyLong(), anyInt())).thenReturn(cartDTO);

        ResponseEntity<CartDTO> response = cartController.addProductToCart(1L, 1L, 1);

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.CREATED);
        assertThat(response.getBody()).isEqualTo(cartDTO);
        verify(cartService).addProductToCart(1L, 1L, 1);
    }

    @Test
    void testGetCarts() {
        
        List<CartDTO> cartDTOs = new ArrayList<>();
        when(cartService.getAllCarts()).thenReturn(cartDTOs);

        ResponseEntity<List<CartDTO>> response = cartController.getCarts();

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.FOUND);
        assertThat(response.getBody()).isEqualTo(cartDTOs);
        verify(cartService).getAllCarts();
    }

   

    @Test
    void testUpdateCartProduct() {
        
    	CartDTO cartDTO = new CartDTO();
        when(cartService.updateProductQuantityInCart(anyLong(), anyLong(), anyInt())).thenReturn(cartDTO);

        ResponseEntity<CartDTO> response = cartController.updateCartProduct(1L, 1L, 2);

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(response.getBody()).isEqualTo(cartDTO);
        verify(cartService).updateProductQuantityInCart(1L, 1L, 2);
    }
    

    @Test
    void testDeleteProductFromCart() {
        
        String status = "Product removed";
        when(cartService.deleteProductFromCart(anyLong(), anyLong())).thenReturn(status);

        ResponseEntity<String> response = cartController.deleteProductFromCart(1L, 1L);

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(response.getBody()).isEqualTo(status);
        verify(cartService).deleteProductFromCart(1L, 1L);
    }
}

