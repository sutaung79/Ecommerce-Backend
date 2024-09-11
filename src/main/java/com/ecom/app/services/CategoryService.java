package com.ecom.app.services;

import com.ecom.app.api.response.CategoryResponse;
import com.ecom.app.dto.CategoryDTO;
import com.ecom.app.entities.Category;

public interface CategoryService {

	CategoryDTO createCategory(Category category);

	CategoryResponse getCategories(Integer pageNumber, Integer pageSize, String sortBy, String sortOrder);

	CategoryDTO updateCategory(Category category, Long categoryId);

	String deleteCategory(Long categoryId);
}
