package com.ecom.app.services;

import com.ecom.app.api.response.ProductResponse;
import com.ecom.app.dto.CartDTO;
import com.ecom.app.dto.ProductDTO;
import com.ecom.app.entities.Cart;
import com.ecom.app.entities.CartItem;
import com.ecom.app.entities.Category;
import com.ecom.app.entities.Product;
import com.ecom.app.exceptions.APIException;
import com.ecom.app.repositories.CartRepository;
import com.ecom.app.repositories.CategoryRepository;
import com.ecom.app.repositories.ProductRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.*;

public class ProductServiceImplTest {

    @Mock
    private ProductRepository productRepo;

    @Mock
    private CategoryRepository categoryRepo;

    @Mock
    private CartRepository cartRepo;

    @Mock
    private CartService cartService;


    @Mock
    private ModelMapper modelMapper;

    @InjectMocks
    private ProductServiceImpl productService;

    @Value("${project.image}")
    private String path;

    private Product product;
    private ProductDTO productDTO;
    private Category category;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);

        // Initialize test data
        category = new Category();
        category.setCategoryId(1L);
        categoryRepo.save(category);

        product = new Product();
        product.setProductId(1L);
        product.setProductName("Product Name");
        product.setDescription("Product Description");
        product.setPrice(100.0);
        product.setDiscount(10.0);
        product.setCategory(category);
        product.setImage("default.png");

        
        product.setCartItems(new ArrayList<>());
        product.setOrderItems(new ArrayList<>());

        productDTO = new ProductDTO();
        productDTO.setProductName("Product Name");
        productDTO.setDescription("Product Description");
        productDTO.setPrice(100.0);
        productDTO.setDiscount(10.0);
    }

    @Test
    void testAddProduct() {
        when(categoryRepo.findById(1L)).thenReturn(Optional.of(category));
        when(productRepo.save(product)).thenReturn(product);
        when(modelMapper.map(product, ProductDTO.class)).thenReturn(productDTO);

        ProductDTO result = productService.addProduct(1L, product);

        assertThat(result).isNotNull();
        assertThat(result.getProductName()).isEqualTo("Product Name");
        verify(productRepo).save(product);
    }

    @Test
    void testAddProductThrowsException() {
        when(categoryRepo.findById(1L)).thenReturn(Optional.of(category));
        when(productRepo.findAll()).thenReturn(List.of(product));

        try {
            productService.addProduct(1L, product);
        } catch (APIException e) {
            assertThat(e.getMessage()).contains("Product already exists");
        }
    }

    @Test
    void testGetAllProducts() {
        Page<Product> productPage = new PageImpl<>(List.of(product));
        when(productRepo.findAll(any(PageRequest.class))).thenReturn(productPage);
        when(modelMapper.map(product, ProductDTO.class)).thenReturn(productDTO);

        ProductResponse result = productService.getAllProducts(0, 10, "price", "asc");

        assertThat(result).isNotNull();
        assertThat(result.getContent().size()).isEqualTo(1);
        verify(productRepo).findAll(any(PageRequest.class));
    }

    
    @Test
    void testSearchByCategory() {
        Page<Product> productPage = new PageImpl<>(List.of(product));
        
        when(categoryRepo.findById(1L)).thenReturn(Optional.of(category));
        when(productRepo.findByCategory(category, PageRequest.of(0, 10, Sort.by("price").ascending())))
                .thenReturn(productPage);
        when(modelMapper.map(product, ProductDTO.class)).thenReturn(productDTO);

        ProductResponse result = productService.searchByCategory(1L, 0, 10, "price", "asc");

        assertThat(result).isNotNull();
        assertThat(result.getContent().size()).isEqualTo(1);
        verify(productRepo).findByCategory(category, PageRequest.of(0, 10, Sort.by("price").ascending()));
    }

    

    @Test
    void testSearchProductByKeyword() {
        Page<Product> productPage = new PageImpl<>(List.of(product));
        when(productRepo.findByProductNameContainingIgnoreCase("Product", PageRequest.of(0, 10, Sort.by("price").ascending())))
                .thenReturn(productPage);
        when(modelMapper.map(product, ProductDTO.class)).thenReturn(productDTO);

        ProductResponse result = productService.searchProductByKeyword("Product", 0, 10, "price", "asc");

        assertThat(result).isNotNull();
        assertThat(result.getContent().size()).isEqualTo(1);
        verify(productRepo).findByProductNameContainingIgnoreCase("Product", PageRequest.of(0, 10, Sort.by("price").ascending()));
    }


    
    @Test
    void testUpdateProduct() {
        
        when(productRepo.findById(1L)).thenReturn(Optional.of(product));
        when(productRepo.save(product)).thenReturn(product);
        when(modelMapper.map(product, ProductDTO.class)).thenReturn(productDTO);

        CartItem cartItem = new CartItem();
        cartItem.setProduct(product); 
        
        Cart cart = new Cart();
        cart.setCartId(1L);
        cart.setCartItems(List.of(cartItem));

        when(cartRepo.findCartsByProductId(1L)).thenReturn(List.of(cart));

       
        CartDTO cartDTO = new CartDTO();
        cartDTO.setCartId(1L);
        when(modelMapper.map(cart, CartDTO.class)).thenReturn(cartDTO);

        
        ProductDTO result = productService.updateProduct(1L, product);

        
        assertThat(result).isNotNull();
        assertThat(result.getProductName()).isEqualTo("Product Name");
        verify(productRepo).save(product);
    }

    


    @Test
    void testDeleteProduct() {
        when(productRepo.findById(1L)).thenReturn(Optional.of(product));
        when(cartRepo.findCartsByProductId(1L)).thenReturn(List.of(new Cart()));

        String result = productService.deleteProduct(1L);

        assertThat(result).isEqualTo("Product with productId: 1 deleted successfully !!!");
        verify(productRepo).delete(product);
    }
}
