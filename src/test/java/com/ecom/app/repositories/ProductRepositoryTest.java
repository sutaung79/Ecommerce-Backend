package com.ecom.app.repositories;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.test.context.ActiveProfiles;

import com.ecom.app.entities.Category;
import com.ecom.app.entities.Product;

import java.util.List;

@DataJpaTest
@ActiveProfiles("test")
public class ProductRepositoryTest {

    @Autowired
    private ProductRepository productRepository;
    
    @Autowired
    private CategoryRepository categoryRepository;

    @BeforeEach
    public void setUp() {
    	    
    		Category category1 = new Category();
    		category1.setCategoryName("Electronic devices");
    		category1 = categoryRepository.save(category1); 
    		
    	    Product product1 = new Product();
    	    product1.setProductName("Smartphone");
    	    product1.setDescription("A high-end smartphone with 128GB storage.");
    	    product1.setPrice(699.99);
    	    product1.setDiscount(10.0);
    	    product1.setSpecialPrice(629.99); 
    	    product1.setQuantity(30);
    	    product1.setImage("smartphone.png");
    	    product1.setCategory(category1); 
    	    productRepository.save(product1);

    	    Product product2 = new Product();
    	    product2.setProductName("Smartwatch");
    	    product2.setDescription("A smartwatch with various health tracking features.");
    	    product2.setPrice(199.99);
    	    product2.setDiscount(15.0);
    	    product2.setSpecialPrice(169.99); 
    	    product2.setQuantity(20);
    	    product2.setImage("smartwatch.png");
    	    product2.setCategory(category1);
    	    productRepository.save(product2);

    	    Product product3 = new Product();
    	    product3.setProductName("Laptop");
    	    product3.setDescription("A lightweight laptop with 16GB RAM and 512GB SSD.");
    	    product3.setPrice(999.99);
    	    product3.setDiscount(20.0);
    	    product3.setSpecialPrice(799.99);
    	    product3.setQuantity(10);
    	    product3.setImage("laptop.png");
    	    product3.setCategory(category1);
    	    productRepository.save(product3);
    	}


    @Test
    public void testFindByProductNameContainingIgnoreCase() {
        
        Pageable pageable = PageRequest.of(0, 10);
        Page<Product> resultPage = productRepository.findByProductNameContainingIgnoreCase("smart", pageable);

        List<Product> products = resultPage.getContent();
        assertThat(products).hasSize(2);
        assertThat(products).extracting(Product::getProductName).containsExactlyInAnyOrder("Smartphone", "Smartwatch");
    }

    @Test
    public void testFindByProductNameContainingIgnoreCase_NoMatch() {
   
        Pageable pageable = PageRequest.of(0, 10);

        Page<Product> resultPage = productRepository.findByProductNameContainingIgnoreCase("nonexistent", pageable);

        List<Product> products = resultPage.getContent();
        assertThat(products).isEmpty();
    }
}

