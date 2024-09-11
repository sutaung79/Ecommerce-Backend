package com.ecom.app.services;

import static org.mockito.Mockito.*;
import static org.junit.jupiter.api.Assertions.*;
import static org.assertj.core.api.Assertions.*;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.modelmapper.ModelMapper;
import org.springframework.dao.DataIntegrityViolationException;

import com.ecom.app.config.AppConstants;
import com.ecom.app.dto.AddressDTO;
import com.ecom.app.dto.CartDTO;
import com.ecom.app.dto.ProductDTO;
import com.ecom.app.dto.UserDTO;
import com.ecom.app.entities.Address;
import com.ecom.app.entities.Cart;
import com.ecom.app.entities.CartItem;
import com.ecom.app.entities.Product;
import com.ecom.app.entities.Role;
import com.ecom.app.entities.User;
import com.ecom.app.exceptions.APIException;
import com.ecom.app.exceptions.ResourceNotFoundException;
import com.ecom.app.repositories.AddressRepository;
import com.ecom.app.repositories.RoleRepository;
import com.ecom.app.repositories.UserRepository;

import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.ArrayList;
import java.util.List;
import java.util.Collections;
import java.util.Optional;

public class UserServiceImplTest {

    @Mock
    private UserRepository userRepo;

    @Mock
    private ModelMapper modelMapper;
    
    @Mock
    private AddressRepository addressRepo;
    
    @Mock
    private RoleRepository roleRepo;
    
    @Mock
    private PasswordEncoder passwordEncoder;
    
    @Mock
    private CartService cartService;
   

    @InjectMocks
    private UserServiceImpl userService;
   

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void testGetUserById_UserExists() {
        
        Long userId = 1L;
        User user = new User();
        Address address = new Address();
        Cart cart = new Cart();
        Product product = new Product();
        CartItem cartItem = new CartItem();
        user.setAddresses(Collections.singletonList(address));
        user.setCart(cart);
        cart.setCartItems(Collections.singletonList(cartItem));
        cartItem.setProduct(product);

        UserDTO userDTO = new UserDTO();
        AddressDTO addressDTO = new AddressDTO();
        CartDTO cartDTO = new CartDTO();
        ProductDTO productDTO = new ProductDTO();

        when(userRepo.findById(userId)).thenReturn(Optional.of(user));
        when(modelMapper.map(user, UserDTO.class)).thenReturn(userDTO);
        when(modelMapper.map(address, AddressDTO.class)).thenReturn(addressDTO);
        when(modelMapper.map(cart, CartDTO.class)).thenReturn(cartDTO);
        when(modelMapper.map(product, ProductDTO.class)).thenReturn(productDTO);

        UserDTO result = userService.getUserById(userId);

        assertThat(result).isNotNull();
        assertThat(result.getAddress()).isEqualTo(addressDTO);
        assertThat(result.getCart()).isEqualTo(cartDTO);
        assertThat(result.getCart().getProducts()).containsExactly(productDTO);
    }

    @Test
    void testGetUserById_UserNotFound() {
        
    	Long userId = 1L;
        when(userRepo.findById(userId)).thenReturn(Optional.empty());

        assertThrows(ResourceNotFoundException.class, () -> userService.getUserById(userId));
    }
    
    
    
    
    @Test
    void testRegisterUser_Success() {

    	UserDTO userDTO = new UserDTO();
        userDTO.setAddress(new AddressDTO(1L,"street", "building", "city", "province", "country", "pincode"));
        User user = new User();
        Address address = new Address("street", "building", "city", "province", "country", "pincode");
        Role role = new Role();
       
        when(modelMapper.map(userDTO, User.class)).thenReturn(user);
        when(roleRepo.findById(AppConstants.USER_ID)).thenReturn(Optional.of(role));
        when(addressRepo.findByStreetAndBuildingAndCityAndProvinceAndCountryAndPincode(
                "street", "building", "city", "province", "country", "pincode")).thenReturn(null);
        when(addressRepo.save(any(Address.class))).thenReturn(address);
        when(userRepo.save(any(User.class))).thenReturn(user);
        when(modelMapper.map(user, UserDTO.class)).thenReturn(userDTO);
        when(modelMapper.map(address, AddressDTO.class)).thenReturn(userDTO.getAddress());

        UserDTO result = userService.registerUser(userDTO);

        assertThat(result).isNotNull();
        assertThat(result.getAddress()).isEqualTo(userDTO.getAddress());
    }

    
    @Test
    void testRegisterUser_UserAlreadyExists() {

    	UserDTO userDTO = new UserDTO();
        userDTO.setEmail("existing@example.com");
        userDTO.setAddress(new AddressDTO(1L,"street", "building", "city", "province", "country", "pincode"));
        
        User user = new User();
        Address address = new Address("street", "building", "city", "province", "country", "pincode");
        Role role = new Role();

        when(modelMapper.map(userDTO, User.class)).thenReturn(user);
        when(roleRepo.findById(AppConstants.USER_ID)).thenReturn(Optional.of(role));
        when(addressRepo.findByStreetAndBuildingAndCityAndProvinceAndCountryAndPincode(
                "street", "building", "city", "province", "country", "pincode")).thenReturn(address);
        when(userRepo.save(any(User.class))).thenThrow(new DataIntegrityViolationException("User already exists"));

        assertThrows(APIException.class, () -> userService.registerUser(userDTO));
    }
    
    
    @Test
    void testDeleteUser_UserExists() {

    	Long userId = 1L;
        User user = new User();
        Cart cart = new Cart();
        List<CartItem> cartItems = new ArrayList<>();
        
        Product product = new Product();
        product.setProductId(1L);
        
        CartItem cartItem = new CartItem();
        cartItem.setProduct(product);
        cartItems.add(cartItem);
        
        cart.setCartItems(cartItems);
        user.setCart(cart);
        user.setUserId(userId);
        
        when(userRepo.findById(userId)).thenReturn(Optional.of(user));
        when(userRepo.existsById(userId)).thenReturn(true);


        String result = userService.deleteUser(userId);

        assertEquals("User with userId " + userId + " deleted successfully!!!", result);
        verify(userRepo).delete(user);
        verify(cartService).deleteProductFromCart(cart.getCartId(), product.getProductId());
    }

    @Test
    void testDeleteUser_UserDoesNotExist() {

    	Long userId = 1L;
        when(userRepo.findById(userId)).thenReturn(Optional.empty());

        assertThrows(ResourceNotFoundException.class, () -> userService.deleteUser(userId));
    }
    
    
    @Test
    void testUserExistsByEmail() {
       
        String email = "test@example.com";
        when(userRepo.existsByEmail(email)).thenReturn(true);

        boolean exists = userService.userExistsByEmail(email);

        assertTrue(exists);
        verify(userRepo).existsByEmail(email);
    }



}
