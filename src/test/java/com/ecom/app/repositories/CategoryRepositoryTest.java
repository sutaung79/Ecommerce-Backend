package com.ecom.app.repositories;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import com.ecom.app.entities.Category;

@DataJpaTest
public class CategoryRepositoryTest {

    @Autowired
    private CategoryRepository categoryRepository;

    @BeforeEach
    public void setUp() {
    	
        Category category1 = new Category();
        category1.setCategoryName("Electronics");
        categoryRepository.save(category1);

        Category category2 = new Category();
        category2.setCategoryName("Books");
        categoryRepository.save(category2);
    }

    @Test
    public void testFindByCategoryName() {

        Category foundCategory = categoryRepository.findByCategoryName("Electronics");
        assertThat(foundCategory).isNotNull();
        assertThat(foundCategory.getCategoryName()).isEqualTo("Electronics");
    }

    @Test
    public void testFindByCategoryName_NotFound() {
        Category foundCategory = categoryRepository.findByCategoryName("NonExistentCategory");
        assertThat(foundCategory).isNull();
    }
}

