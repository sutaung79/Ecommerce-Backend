package com.ecom.app.controllers;

import static org.mockito.Mockito.*;
import static org.assertj.core.api.Assertions.*;


import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import com.ecom.app.api.response.ProductResponse;
import com.ecom.app.dto.ProductDTO;
import com.ecom.app.entities.Product;
import com.ecom.app.services.ProductService;

class ProductControllerTest {

    @InjectMocks
    private ProductController productController;

    @Mock
    private ProductService productService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void testAddProduct() {
        
        Product product = new Product();
        ProductDTO productDTO = new ProductDTO();
        when(productService.addProduct(anyLong(), any(Product.class))).thenReturn(productDTO);

        ResponseEntity<ProductDTO> response = productController.addProduct(product, 1L);

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.CREATED);
        assertThat(response.getBody()).isEqualTo(productDTO);
        verify(productService).addProduct(1L, product);
    }

    @Test
    void testGetAllProducts() {
        ProductResponse productResponse = new ProductResponse();
        when(productService.getAllProducts(anyInt(), anyInt(), anyString(), anyString())).thenReturn(productResponse);

        ResponseEntity<ProductResponse> response = productController.getAllProducts(1, 10, "name", "asc");

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.FOUND);
        assertThat(response.getBody()).isEqualTo(productResponse);
        verify(productService).getAllProducts(1, 10, "name", "asc");
    }

    @Test
    void testGetProductsByCategory() {
        ProductResponse productResponse = new ProductResponse();
        when(productService.searchByCategory(anyLong(), anyInt(), anyInt(), anyString(), anyString()))
                .thenReturn(productResponse);

        ResponseEntity<ProductResponse> response = productController.getProductsByCategory(1L, 1, 10, "name", "asc");

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.FOUND);
        assertThat(response.getBody()).isEqualTo(productResponse);
        verify(productService).searchByCategory(1L, 1, 10, "name", "asc");
    }

    @Test
    void testGetProductsByKeyword() {
        ProductResponse productResponse = new ProductResponse();
        when(productService.searchProductByKeyword(anyString(), anyInt(), anyInt(), anyString(), anyString()))
                .thenReturn(productResponse);

        ResponseEntity<ProductResponse> response = productController.getProductsByKeyword("keyword", 1, 10, "name", "asc");

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.FOUND);
        assertThat(response.getBody()).isEqualTo(productResponse);
        verify(productService).searchProductByKeyword("keyword", 1, 10, "name", "asc");
    }

    @Test
    void testUpdateProduct() {
        Product product = new Product();
        ProductDTO productDTO = new ProductDTO();
        when(productService.updateProduct(anyLong(), any(Product.class))).thenReturn(productDTO);

        ResponseEntity<ProductDTO> response = productController.updateProduct(product, 1L);

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(response.getBody()).isEqualTo(productDTO);
        verify(productService).updateProduct(1L, product);
    }

   
    
    @Test
    void testDeleteProductByCategory() {
        String status = "Product deleted";
        when(productService.deleteProduct(anyLong())).thenReturn(status);

        ResponseEntity<String> response = productController.deleteProductByCategory(1L);

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(response.getBody()).isEqualTo(status);
        verify(productService).deleteProduct(1L);
    }
}
