package com.ecom.app.controllers;

import static org.mockito.Mockito.*;
import static org.assertj.core.api.Assertions.*;

import java.util.ArrayList;
import java.util.List;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import com.ecom.app.api.response.OrderResponse;
import com.ecom.app.dto.OrderDTO;
import com.ecom.app.services.OrderService;

class OrderControllerTest {

    @InjectMocks
    private OrderController orderController;

    @Mock
    private OrderService orderService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void testOrderProducts() {
      
        OrderDTO orderDTO = new OrderDTO();
        when(orderService.placeOrder(anyString(), anyLong(), anyString())).thenReturn(orderDTO);

        ResponseEntity<OrderDTO> response = orderController.orderProducts("test@example.com", 1L, "credit_card");

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.CREATED);
        assertThat(response.getBody()).isEqualTo(orderDTO);
        verify(orderService).placeOrder("test@example.com", 1L, "credit_card");
    }

    @Test
    void testGetAllOrders() {
       
        OrderResponse orderResponse = new OrderResponse();
        when(orderService.getAllOrders(anyInt(), anyInt(), anyString(), anyString())).thenReturn(orderResponse);

        ResponseEntity<OrderResponse> response = orderController.getAllOrders(1, 10, "date", "asc");

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.FOUND);
        assertThat(response.getBody()).isEqualTo(orderResponse);
        verify(orderService).getAllOrders(1, 10, "date", "asc");
    }

    @Test
    void testGetOrdersByUser() {
        List<OrderDTO> orders = new ArrayList<>();
        when(orderService.getOrdersByUser(anyString())).thenReturn(orders);

        ResponseEntity<List<OrderDTO>> response = orderController.getOrdersByUser("test@example.com");

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.FOUND);
        assertThat(response.getBody()).isEqualTo(orders);
        verify(orderService).getOrdersByUser("test@example.com");
    }

    @Test
    void testGetOrderByUser() {
        
        OrderDTO orderDTO = new OrderDTO();
        when(orderService.getOrder(anyString(), anyLong())).thenReturn(orderDTO);

        ResponseEntity<OrderDTO> response = orderController.getOrderByUser("test@example.com", 1L);

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.FOUND);
        assertThat(response.getBody()).isEqualTo(orderDTO);
        verify(orderService).getOrder("test@example.com", 1L);
    }

    @Test
    void testUpdateOrderByUser() {
        
        OrderDTO orderDTO = new OrderDTO();
        when(orderService.updateOrder(anyString(), anyLong(), anyString())).thenReturn(orderDTO);

        ResponseEntity<OrderDTO> response = orderController.updateOrderByUser("test@example.com", 1L, "shipped");

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(response.getBody()).isEqualTo(orderDTO);
        verify(orderService).updateOrder("test@example.com", 1L, "shipped");
    }
}

