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

import com.ecom.app.api.response.CategoryResponse;
import com.ecom.app.dto.CategoryDTO;
import com.ecom.app.entities.Category;
import com.ecom.app.services.CategoryService;

class CategoryControllerTest {

    @InjectMocks
    private CategoryController categoryController;

    @Mock
    private CategoryService categoryService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void testCreateCategory() {
        
    	Category category = new Category();
        CategoryDTO categoryDTO = new CategoryDTO();
        when(categoryService.createCategory(any(Category.class))).thenReturn(categoryDTO);

        ResponseEntity<CategoryDTO> response = categoryController.createCategory(category);

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.CREATED);
        assertThat(response.getBody()).isEqualTo(categoryDTO);
        verify(categoryService).createCategory(category);
    }

    @Test
    void testGetCategories() {
        
    	CategoryResponse categoryResponse = new CategoryResponse();
        when(categoryService.getCategories(anyInt(), anyInt(), anyString(), anyString())).thenReturn(categoryResponse);

        ResponseEntity<CategoryResponse> response = categoryController.getCategories(1, 10, "name", "asc");

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.FOUND);
        assertThat(response.getBody()).isEqualTo(categoryResponse);
        verify(categoryService).getCategories(1, 10, "name", "asc");
    }

    @Test
    void testUpdateCategory() {
        
    	Category category = new Category();
        CategoryDTO categoryDTO = new CategoryDTO();
        when(categoryService.updateCategory(any(Category.class), anyLong())).thenReturn(categoryDTO);

        ResponseEntity<CategoryDTO> response = categoryController.updateCategory(category, 1L);

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(response.getBody()).isEqualTo(categoryDTO);
        verify(categoryService).updateCategory(category, 1L);
    }

    @Test
    void testDeleteCategory() {
        
    	String status = "Category deleted successfully";
        when(categoryService.deleteCategory(anyLong())).thenReturn(status);

        ResponseEntity<String> response = categoryController.deleteCategory(1L);

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(response.getBody()).isEqualTo(status);
        verify(categoryService).deleteCategory(1L);
    }
}
