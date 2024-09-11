package com.ecom.app.repositories;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import com.ecom.app.entities.Cart;
import com.ecom.app.entities.CartItem;
import com.ecom.app.entities.Category;
import com.ecom.app.entities.Product;

@DataJpaTest
public class CartItemRepositoryTest {

    @Autowired
    private CartItemRepository cartItemRepository;
    
    @Autowired
    private ProductRepository productRepository;
    
    @Autowired
    private CategoryRepository categoryRepository;
    
    @Autowired
    private CartRepository cartRepository;

    private Product product;
    private Cart cart;
    private CartItem cartItem;

    @BeforeEach
    public void setUp() {
    	
        Category category = new Category();
        category.setCategoryName("Electronic devices");
        category = categoryRepository.save(category);

        product = new Product();
        product.setProductName("Smartphone");
        product.setDescription("A high-end smartphone with 128GB storage.");
        product.setPrice(699.99);
        product.setDiscount(10.0);
        product.setSpecialPrice(629.99);
        product.setQuantity(30);
        product.setImage("smartphone.png");
        product.setCategory(category);
        product = productRepository.save(product); 
       
        cart = new Cart();
        cart = cartRepository.save(cart); 

        cartItem = new CartItem();
        cartItem.setCart(cart);
        cartItem.setProduct(product);
        cartItem.setQuantity(2);
        cartItemRepository.save(cartItem);
    }

    
    @Test
    public void testFindProductById() {
        Product foundProduct = cartItemRepository.findProductById(product.getProductId());
        assertThat(foundProduct).isNotNull();
        assertThat(foundProduct.getProductName()).isEqualTo("Smartphone");
    }

    @Test
    public void testFindProductById_NotFound() {
        Product foundProduct = cartItemRepository.findProductById(999L);
        assertThat(foundProduct).isNull();
    }

    @Test
    public void testFindCartItemByCartIdAndProductId() {
        CartItem foundCartItem = cartItemRepository.findCartItemByProductIdAndCartId(cart.getCartId(), product.getProductId());
        assertThat(foundCartItem).isNotNull();
        assertThat(foundCartItem.getCart().getCartId()).isEqualTo(cart.getCartId());
        assertThat(foundCartItem.getProduct().getProductId()).isEqualTo(product.getProductId());
    }

    @Test
    public void testFindCartItemByCartIdAndProductId_NotFound() {
        CartItem foundCartItem = cartItemRepository.findCartItemByProductIdAndCartId(cart.getCartId(), 999L);
        assertThat(foundCartItem).isNull();
    }

   
    @Test
    public void testDeleteCartItemByCartIdAndProductId() {
        CartItem foundCartItem = cartItemRepository.findCartItemByProductIdAndCartId(cart.getCartId(), product.getProductId());
        assertThat(foundCartItem).isNotNull();

        cartItemRepository.delete(foundCartItem);  // Correctly delete the cart item

        CartItem deletedCartItem = cartItemRepository.findCartItemByProductIdAndCartId(cart.getCartId(), product.getProductId());
        assertThat(deletedCartItem).isNull();
    }
}
