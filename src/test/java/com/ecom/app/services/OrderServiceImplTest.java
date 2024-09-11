package com.ecom.app.services;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.*;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.modelmapper.ModelMapper;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;

import com.ecom.app.api.response.OrderResponse;
import com.ecom.app.dto.OrderDTO;
import com.ecom.app.entities.Cart;
import com.ecom.app.entities.CartItem;
import com.ecom.app.entities.Order;
import com.ecom.app.entities.Payment;
import com.ecom.app.entities.Product;
import com.ecom.app.exceptions.APIException;
import com.ecom.app.exceptions.ResourceNotFoundException;
import com.ecom.app.repositories.CartItemRepository;
import com.ecom.app.repositories.CartRepository;
import com.ecom.app.repositories.OrderItemRepository;
import com.ecom.app.repositories.OrderRepository;
import com.ecom.app.repositories.PaymentRepository;
import com.ecom.app.repositories.UserRepository;

class OrderServiceImplTest {

    @InjectMocks
    private OrderServiceImpl orderService;

    @Mock
    private UserRepository userRepo;

    @Mock
    private CartRepository cartRepo;

    @Mock
    private OrderRepository orderRepo;

    @Mock
    private PaymentRepository paymentRepo;

    @Mock
    private OrderItemRepository orderItemRepo;

    @Mock
    private CartItemRepository cartItemRepo;

    @Mock
    private UserService userService;

    @Mock
    private CartService cartService;

    @Mock
    private ModelMapper modelMapper;

    private Cart cart;
    private Order order;
    private OrderDTO orderDTO;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);

        cart = new Cart();
        cart.setCartId(1L);
        cart.setTotalPrice(100.0);
        cart.setCartItems(new ArrayList<>());

        order = new Order();
        order.setOrderId(1L);
        order.setEmail("test@example.com");
        order.setOrderDate(LocalDate.now());
        order.setTotalAmount(100.0);

        orderDTO = new OrderDTO();
        orderDTO.setOrderId(1L);
    }


    
    @Test
    void testPlaceOrder() {
        
        when(cartRepo.findCartByEmailAndCartId("test@example.com", 1L)).thenReturn(cart);
        when(orderRepo.save(any(Order.class))).thenReturn(order);
        when(paymentRepo.save(any(Payment.class))).thenReturn(new Payment());
        when(modelMapper.map(any(Order.class), eq(OrderDTO.class))).thenReturn(orderDTO);

        
        CartItem cartItem = new CartItem();
        Product product = new Product();
        product.setQuantity(10);  
        cartItem.setProduct(product);
        cartItem.setQuantity(2);
        cart.setCartItems(List.of(cartItem));

        
        OrderDTO result = orderService.placeOrder("test@example.com", 1L, "CREDIT_CARD");

        assertThat(result).isNotNull();
        verify(orderRepo).save(any(Order.class));
        verify(paymentRepo).save(any(Payment.class));
    }


    @Test
    void testPlaceOrder_EmptyCart() {
       
        when(cartRepo.findCartByEmailAndCartId("test@example.com", 1L)).thenReturn(cart);

        APIException exception = assertThrows(APIException.class, () -> {
            orderService.placeOrder("test@example.com", 1L, "CREDIT_CARD");
        });
        assertThat(exception.getMessage()).isEqualTo("Cart is empty");
    }

    @Test
    void testGetOrdersByUser() {
     
        when(orderRepo.findAllByEmail("test@example.com")).thenReturn(List.of(order));
        when(modelMapper.map(any(Order.class), eq(OrderDTO.class))).thenReturn(orderDTO);

        List<OrderDTO> result = orderService.getOrdersByUser("test@example.com");

        assertThat(result).isNotEmpty();
        assertThat(result.size()).isEqualTo(1);
    }

    @Test
    void testGetOrdersByUser_NoOrders() {
        when(orderRepo.findAllByEmail("test@example.com")).thenReturn(new ArrayList<>());

        APIException exception = assertThrows(APIException.class, () -> {
            orderService.getOrdersByUser("test@example.com");
        });
        assertThat(exception.getMessage()).isEqualTo("No orders placed yet by the user with email: test@example.com");
    }

    @Test
    void testGetOrder() {
        when(orderRepo.findOrderByEmailAndOrderId("test@example.com", 1L)).thenReturn(order);
        when(modelMapper.map(any(Order.class), eq(OrderDTO.class))).thenReturn(orderDTO);
        OrderDTO result = orderService.getOrder("test@example.com", 1L);
        assertThat(result).isNotNull();
    }

    @Test
    void testGetOrder_NotFound() {
        when(orderRepo.findOrderByEmailAndOrderId("test@example.com", 1L)).thenReturn(null);
        ResourceNotFoundException exception = assertThrows(ResourceNotFoundException.class, () -> {
            orderService.getOrder("test@example.com", 1L);
        });
        assertThat(exception.getMessage()).contains("Order");
    }

    @Test
    void testGetAllOrders() {
        List<Order> orders = List.of(order);
        Page<Order> pageOrders = new PageImpl<>(orders);
        when(orderRepo.findAll(any(PageRequest.class))).thenReturn(pageOrders);
        when(modelMapper.map(any(Order.class), eq(OrderDTO.class))).thenReturn(orderDTO);
        OrderResponse response = orderService.getAllOrders(0, 10, "orderDate", "asc");
        assertThat(response).isNotNull();
        assertThat(response.getContent()).isNotEmpty();
        assertThat(response.getPageNumber()).isEqualTo(0);
    }

    @Test
    void testUpdateOrder() {
        when(orderRepo.findOrderByEmailAndOrderId("test@example.com", 1L)).thenReturn(order);
        when(modelMapper.map(any(Order.class), eq(OrderDTO.class))).thenReturn(orderDTO);
        OrderDTO result = orderService.updateOrder("test@example.com", 1L, "Shipped");
        assertThat(result).isNotNull();
        verify(orderRepo).findOrderByEmailAndOrderId("test@example.com", 1L);
    }

    @Test
    void testUpdateOrder_NotFound() {
        when(orderRepo.findOrderByEmailAndOrderId("test@example.com", 1L)).thenReturn(null);
        ResourceNotFoundException exception = assertThrows(ResourceNotFoundException.class, () -> {
            orderService.updateOrder("test@example.com", 1L, "Shipped");
        });
        assertThat(exception.getMessage()).contains("Order");
    }
}
