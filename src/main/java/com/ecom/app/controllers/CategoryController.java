package com.ecom.app.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import com.ecom.app.api.response.CategoryResponse;
import com.ecom.app.config.AppConstants;
import com.ecom.app.dto.CategoryDTO;
import com.ecom.app.entities.Category;
import com.ecom.app.services.CategoryService;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;

import jakarta.validation.Valid;

@RestController
@RequestMapping("/api")
@SecurityRequirement(name = "E-Commerce Application")
@Tag(name = "Category Operations", description = "Endpoints related to category management")
public class CategoryController {

    @Autowired
    private CategoryService categoryService;

    
    @PostMapping("/admin/category")
    @Operation(
        summary = "Create a new category",
        description = "Create a new category by providing category details."
    )
    @ApiResponse(responseCode = "201", description = "Category successfully created")
    public ResponseEntity<CategoryDTO> createCategory(@Valid @RequestBody Category category) {
        CategoryDTO savedCategoryDTO = categoryService.createCategory(category);
        return new ResponseEntity<CategoryDTO>(savedCategoryDTO, HttpStatus.CREATED);
    }

    
    
    
    @GetMapping("/public/categories")
    @Operation(
        summary = "Get all categories",
        description = "Retrieve a paginated and sorted list of categories."
    )
    @ApiResponses(value = {
        @ApiResponse(responseCode = "302", description = "Categories successfully found"),
        @ApiResponse(responseCode = "404", description = "No categories found")
    })
    public ResponseEntity<CategoryResponse> getCategories(
            @RequestParam(name = "pageNumber", defaultValue = AppConstants.PAGE_NUMBER, required = false) Integer pageNumber,
            @RequestParam(name = "pageSize", defaultValue = AppConstants.PAGE_SIZE, required = false) Integer pageSize,
            @RequestParam(name = "sortBy", defaultValue = AppConstants.SORT_CATEGORIES_BY, required = false) String sortBy,
            @RequestParam(name = "sortOrder", defaultValue = AppConstants.SORT_DIR, required = false) String sortOrder) {
        
        CategoryResponse categoryResponse = categoryService.getCategories(pageNumber, pageSize, sortBy, sortOrder);
        return new ResponseEntity<CategoryResponse>(categoryResponse, HttpStatus.FOUND);
    }
    
    
    

    @PutMapping("/admin/categories/{categoryId}")
    @Operation(
        summary = "Update a category",
        description = "Update an existing category by providing the updated category details and category ID."
    )
    @ApiResponse(responseCode = "200", description = "Category successfully updated")
    public ResponseEntity<CategoryDTO> updateCategory(@RequestBody Category category, @PathVariable Long categoryId) {
        CategoryDTO categoryDTO = categoryService.updateCategory(category, categoryId);
        return new ResponseEntity<CategoryDTO>(categoryDTO, HttpStatus.OK);
    }
    
    

    @DeleteMapping("/admin/categories/{categoryId}")
    @Operation(
        summary = "Delete a category",
        description = "Delete a category by providing the category ID."
    )
    @ApiResponse(responseCode = "200", description = "Category successfully deleted")
    public ResponseEntity<String> deleteCategory(@PathVariable Long categoryId) {
        String status = categoryService.deleteCategory(categoryId);
        return new ResponseEntity<String>(status, HttpStatus.OK);
    }
}
