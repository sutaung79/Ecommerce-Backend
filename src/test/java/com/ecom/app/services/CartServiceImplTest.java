package com.ecom.app.services;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.*;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.modelmapper.ModelMapper;

import com.ecom.app.dto.CartDTO;
import com.ecom.app.entities.Cart;
import com.ecom.app.entities.CartItem;
import com.ecom.app.entities.Product;
import com.ecom.app.exceptions.APIException;
import com.ecom.app.repositories.CartItemRepository;
import com.ecom.app.repositories.CartRepository;
import com.ecom.app.repositories.ProductRepository;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;

class CartServiceImplTest {

    @Mock
    private CartRepository cartRepo;
    

    @Mock
    private ProductRepository productRepo;

    @Mock
    private CartItemRepository cartItemRepo;

    @Mock
    private ModelMapper modelMapper;

    @InjectMocks
    private CartServiceImpl cartService;

    public CartServiceImplTest() {
        MockitoAnnotations.openMocks(this);
    }

    
    private Cart cart;
    private Product product;
    private CartItem cartItem;
    
    @BeforeEach
    void setUp() {
    	
    
    	
        cart = new Cart();
        cart.setCartId(1L);
        cart.setTotalPrice(0.0);

        product = new Product();
        product.setProductId(1L);
        product.setProductName("Test Product");
        product.setQuantity(10);
        product.setSpecialPrice(20.0);
        product.setDiscount(0.1);

        cartItem = new CartItem();
        cartItem.setProduct(product);
        cartItem.setCart(cart);
    }
   
   
    @Test
    void testAddProductToCart_ProductNotAvailable() {
        Cart cart = new Cart();
        cart.setTotalPrice(0.0);

        Product product = new Product();
        product.setProductName("Product Name");
        product.setQuantity(0);  // Product is out of stock

        when(cartRepo.findById(1L)).thenReturn(java.util.Optional.of(cart));
        when(productRepo.findById(1L)).thenReturn(java.util.Optional.of(product));

        APIException thrown = assertThrows(APIException.class, () -> {
            cartService.addProductToCart(1L, 1L, 1);
        });

        assertThat(thrown.getMessage()).isEqualTo("Product Name is not available");
    }
    
    
    @Test
    void testGetAllCarts() {
        Cart cart = new Cart();
        List<Cart> carts = List.of(cart);

        CartDTO cartDTO = new CartDTO();
        when(cartRepo.findAll()).thenReturn(carts);
        when(modelMapper.map(any(Cart.class), eq(CartDTO.class))).thenReturn(cartDTO);

        List<CartDTO> result = cartService.getAllCarts();

        assertThat(result).isNotEmpty();
        verify(cartRepo).findAll();
    }
    
   

    
    @Test
    void testUpdateProductInCarts() {
        Cart cart = new Cart();
        cart.setTotalPrice(200.0);

        Product product = new Product();
        product.setSpecialPrice(150.0);

        CartItem cartItem = new CartItem();
        cartItem.setProduct(product);
        cartItem.setProductPrice(100.0);
        cartItem.setQuantity(2);

        when(cartRepo.findById(1L)).thenReturn(java.util.Optional.of(cart));
        when(productRepo.findById(1L)).thenReturn(java.util.Optional.of(product));
        when(cartItemRepo.findCartItemByProductIdAndCartId(1L, 1L)).thenReturn(cartItem);
        when(cartItemRepo.save(any(CartItem.class))).thenReturn(cartItem);

        cartService.updateProductInCarts(1L, 1L);

        verify(cartItemRepo).save(any(CartItem.class));
    }

    
    @Test
    void testUpdateProductQuantityInCart() {
        Cart cart = new Cart();
        cart.setTotalPrice(200.0);
        
        Product product = new Product();
        product.setQuantity(10);
        product.setSpecialPrice(150.0);
        product.setDiscount(10.0);
        
        CartItem cartItem = new CartItem();
        cartItem.setProduct(product);
        cartItem.setProductPrice(100.0);
        cartItem.setQuantity(2);
        
        cart.setCartItems(List.of(cartItem));

        CartDTO cartDTO = new CartDTO();
        when(modelMapper.map(cart, CartDTO.class)).thenReturn(cartDTO);

        when(cartRepo.findById(1L)).thenReturn(java.util.Optional.of(cart));
        when(productRepo.findById(1L)).thenReturn(java.util.Optional.of(product));
        when(cartItemRepo.findCartItemByProductIdAndCartId(1L, 1L)).thenReturn(cartItem);
        when(cartItemRepo.save(any(CartItem.class))).thenReturn(cartItem);

       
        CartDTO result = cartService.updateProductQuantityInCart(1L, 1L, 3);

        assertThat(result).isNotNull();
        verify(cartItemRepo).save(any(CartItem.class));
        verify(modelMapper).map(cart, CartDTO.class);
    }


    
    
    
    @Test
    void testDeleteProductFromCart() {
        Cart cart = new Cart();
        cart.setTotalPrice(200.0);

        Product product = new Product();
        product.setProductName("Product Name");
        product.setQuantity(10);

        CartItem cartItem = new CartItem();
        cartItem.setProduct(product);
        cartItem.setProductPrice(100.0);
        cartItem.setQuantity(2);

        when(cartRepo.findById(1L)).thenReturn(java.util.Optional.of(cart));
        when(cartItemRepo.findCartItemByProductIdAndCartId(1L, 1L)).thenReturn(cartItem);

        String result = cartService.deleteProductFromCart(1L, 1L);

        assertThat(result).isEqualTo("Product Product Name removed from the cart !!!");

        // Verify that findCartItemByProductIdAndCartId was called exactly once
        verify(cartItemRepo, times(1)).findCartItemByProductIdAndCartId(1L, 1L);
    }



}

