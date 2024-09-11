package com.ecom.app.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import com.ecom.app.api.response.ProductResponse;
import com.ecom.app.config.AppConstants;
import com.ecom.app.dto.ProductDTO;
import com.ecom.app.entities.Product;
import com.ecom.app.services.ProductService;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;

@RestController
@RequestMapping("/api")
@SecurityRequirement(name = "E-Commerce Application")
@Tag(name = "Product Operations", description = "Endpoints related to product management")
public class ProductController {

    @Autowired
    private ProductService productService;

    @PostMapping("/admin/categories/{categoryId}/product")
    @Operation(
        summary = "Add a product",
        description = "Add a new product to a specific category by providing the product details and category ID."
    )
    @ApiResponse(responseCode = "201", description = "Product successfully created")
    public ResponseEntity<ProductDTO> addProduct(@Valid @RequestBody Product product, @PathVariable Long categoryId) {
        ProductDTO savedProduct = productService.addProduct(categoryId, product);
        return new ResponseEntity<ProductDTO>(savedProduct, HttpStatus.CREATED);
    }

    @GetMapping("/public/products")
    @Operation(
        summary = "Get all products",
        description = "Retrieve a paginated list of all available products with optional sorting by various fields."
    )
    @ApiResponse(responseCode = "302", description = "Products successfully found")
    public ResponseEntity<ProductResponse> getAllProducts(
            @RequestParam(name = "pageNumber", defaultValue = AppConstants.PAGE_NUMBER, required = false) Integer pageNumber,
            @RequestParam(name = "pageSize", defaultValue = AppConstants.PAGE_SIZE, required = false) Integer pageSize,
            @RequestParam(name = "sortBy", defaultValue = AppConstants.SORT_PRODUCTS_BY, required = false) String sortBy,
            @RequestParam(name = "sortOrder", defaultValue = AppConstants.SORT_DIR, required = false) String sortOrder) {

        ProductResponse productResponse = productService.getAllProducts(pageNumber, pageSize, sortBy, sortOrder);
        return new ResponseEntity<ProductResponse>(productResponse, HttpStatus.FOUND);
    }

    @GetMapping("/public/categories/{categoryId}/products")
    @Operation(
        summary = "Get products by category",
        description = "Retrieve products belonging to a specific category with pagination and sorting options."
    )
    @ApiResponse(responseCode = "302", description = "Products successfully found")
    public ResponseEntity<ProductResponse> getProductsByCategory(@PathVariable Long categoryId,
            @RequestParam(name = "pageNumber", defaultValue = AppConstants.PAGE_NUMBER, required = false) Integer pageNumber,
            @RequestParam(name = "pageSize", defaultValue = AppConstants.PAGE_SIZE, required = false) Integer pageSize,
            @RequestParam(name = "sortBy", defaultValue = AppConstants.SORT_PRODUCTS_BY, required = false) String sortBy,
            @RequestParam(name = "sortOrder", defaultValue = AppConstants.SORT_DIR, required = false) String sortOrder) {

        ProductResponse productResponse = productService.searchByCategory(categoryId, pageNumber, pageSize, sortBy, sortOrder);
        return new ResponseEntity<ProductResponse>(productResponse, HttpStatus.FOUND);
    }
    
    @GetMapping("/public/products/keyword/{keyword}")
    @Operation(
        summary = "Search products by keyword",
        description = "Search for products using a keyword with optional pagination and sorting."
    )
    @ApiResponse(responseCode = "302", description = "Products successfully found")
    public ResponseEntity<ProductResponse> getProductsByKeyword(@PathVariable String keyword,
            @RequestParam(name = "pageNumber", defaultValue = AppConstants.PAGE_NUMBER, required = false) Integer pageNumber,
            @RequestParam(name = "pageSize", defaultValue = AppConstants.PAGE_SIZE, required = false) Integer pageSize,
            @RequestParam(name = "sortBy", defaultValue = AppConstants.SORT_PRODUCTS_BY, required = false) String sortBy,
            @RequestParam(name = "sortOrder", defaultValue = AppConstants.SORT_DIR, required = false) String sortOrder) {

        ProductResponse productResponse = productService.searchProductByKeyword(keyword, pageNumber, pageSize, sortBy, sortOrder);
        return new ResponseEntity<ProductResponse>(productResponse, HttpStatus.FOUND);
    }

    @PutMapping("/admin/products/{productId}")
    @Operation(
        summary = "Update a product",
        description = "Update the details of an existing product by providing the product ID and the updated product details."
    )
    @ApiResponse(responseCode = "200", description = "Product successfully updated")
    public ResponseEntity<ProductDTO> updateProduct(@RequestBody Product product, @PathVariable Long productId) {
        ProductDTO updatedProduct = productService.updateProduct(productId, product);
        return new ResponseEntity<ProductDTO>(updatedProduct, HttpStatus.OK);
    }

    @DeleteMapping("/admin/products/{productId}")
    @Operation(
        summary = "Delete a product",
        description = "Delete an existing product by providing the product ID."
    )
    @ApiResponse(responseCode = "200", description = "Product successfully deleted")
    public ResponseEntity<String> deleteProductByCategory(@PathVariable Long productId) {
        String status = productService.deleteProduct(productId);
        return new ResponseEntity<String>(status, HttpStatus.OK);
    }
}
