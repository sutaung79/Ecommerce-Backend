package com.ecom.app.controllers;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import com.ecom.app.dto.CartDTO;
import com.ecom.app.services.CartService;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;

@RestController
@RequestMapping("/api")
@SecurityRequirement(name = "E-Commerce Application")
@Tag(name = "Cart Operations", description = "Endpoints related to Cart management")
public class CartController {

    @Autowired
    private CartService cartService;

    
    
    @PostMapping("/public/carts/{cartId}/products/{productId}/quantity/{quantity}")
    @Operation(
        summary = "Add product to cart",
        description = "Add a specified quantity of a product to a user's cart."
    )
    @ApiResponse(responseCode = "201", description = "Product successfully added to the cart")
    public ResponseEntity<CartDTO> addProductToCart(@PathVariable Long cartId, @PathVariable Long productId, @PathVariable Integer quantity) {
        CartDTO cartDTO = cartService.addProductToCart(cartId, productId, quantity);
        return new ResponseEntity<CartDTO>(cartDTO, HttpStatus.CREATED);
    }
    
    

    
    @GetMapping("/admin/carts")
    @Operation(
        summary = "Get all carts",
        description = "Retrieve a list of all carts."
    )
    @ApiResponses(value = {
        @ApiResponse(responseCode = "302", description = "Carts successfully found"),
        @ApiResponse(responseCode = "404", description = "No carts found")
    })
    public ResponseEntity<List<CartDTO>> getCarts() {
        List<CartDTO> cartDTOs = cartService.getAllCarts();
        return new ResponseEntity<List<CartDTO>>(cartDTOs, HttpStatus.FOUND);
    }
    
    

    
    @PutMapping("/public/carts/{cartId}/products/{productId}/quantity/{quantity}")
    @Operation(
        summary = "Update product quantity in cart",
        description = "Update the quantity of a specific product in a user's cart."
    )
    @ApiResponse(responseCode = "200", description = "Cart product quantity successfully updated")
    public ResponseEntity<CartDTO> updateCartProduct(@PathVariable Long cartId, @PathVariable Long productId, @PathVariable Integer quantity) {
        CartDTO cartDTO = cartService.updateProductQuantityInCart(cartId, productId, quantity);
        return new ResponseEntity<CartDTO>(cartDTO, HttpStatus.OK);
    }
    
    
    

    @DeleteMapping("/public/carts/{cartId}/product/{productId}")
    @Operation(
        summary = "Remove product from cart",
        description = "Delete a product from a user's cart."
    )
    @ApiResponse(responseCode = "200", description = "Product successfully removed from the cart")
    public ResponseEntity<String> deleteProductFromCart(@PathVariable Long cartId, @PathVariable Long productId) {
        String status = cartService.deleteProductFromCart(cartId, productId);
        return new ResponseEntity<String>(status, HttpStatus.OK);
    }
}
