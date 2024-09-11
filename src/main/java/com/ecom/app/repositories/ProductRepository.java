package com.ecom.app.repositories;


import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.ecom.app.entities.Category;
import com.ecom.app.entities.Product;


@Repository
public interface ProductRepository extends JpaRepository<Product, Long> {
	
	Page<Product> findByCategory(Category category, Pageable pageable);

	
    Page<Product> findByProductNameContainingIgnoreCase(String keyword, Pageable pageable);


}

