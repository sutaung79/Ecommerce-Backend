package com.ecom.app.repositories;

import com.ecom.app.entities.Cart;
import com.ecom.app.entities.CartItem;
import com.ecom.app.entities.Category;
import com.ecom.app.entities.Product;
import com.ecom.app.entities.User;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertEquals;

@DataJpaTest
@Transactional
public class CartRepositoryTest {

    @Autowired
    private CartRepository cartRepository;

    @Autowired
    private UserRepository userRepository;
    
    @Autowired
    private CategoryRepository categoryRepository;

    @Autowired
    private ProductRepository productRepository;

    private Long cartId;  
    @BeforeEach
    void setUp() {
    	
        User user = new User();
        user.setEmail("test@example.com");
        user.setFirstName("Test");
        user.setLastName("User");
        user.setPassword("password");
        user.setMobileNumber("1234567890");
        user = userRepository.save(user);


        Category cat1 = new Category();
        cat1.setCategoryName("groceries");
        cat1 = categoryRepository.save(cat1);

        
        Product product = new Product();
        product.setCategory(cat1);
        product.setProductName("Test Product");
        product.setImage("default2.png");
        product.setDescription("Delicious");
        product.setQuantity(40);
        product.setPrice(5.0); 
        product.setDiscount(1.2);
        product = productRepository.save(product);

        
        
        
        Cart cart = new Cart();
        cart.setUser(user);
        cart.setTotalPrice(100.0);

        CartItem cartItem = new CartItem();
        cartItem.setProduct(product);
        cartItem.setCart(cart);
        cartItem.setQuantity(1);

        cart.setCartItems(List.of(cartItem));
        cart = cartRepository.save(cart);

        // Store the cart ID for testing
        this.cartId = cart.getCartId();
    }

    @Test
    void testFindCartByEmailAndCartId() {
        Cart cart = cartRepository.findCartByEmailAndCartId("test@example.com", cartId); // Use the cartId set in setUp
        assertNotNull(cart);
        assertEquals("test@example.com", cart.getUser().getEmail());
    }

    @Test
    void testFindCartsByProductId() {
        List<Cart> carts = cartRepository.findCartsByProductId(1L);
        assertNotNull(carts);
        assertEquals(1, carts.size());
        assertEquals("Test Product", carts.get(0).getCartItems().get(0).getProduct().getProductName());
    }
}
