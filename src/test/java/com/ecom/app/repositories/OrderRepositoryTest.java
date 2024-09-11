package com.ecom.app.repositories;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.context.ActiveProfiles;

import com.ecom.app.entities.Order;

import java.time.LocalDate;
import java.util.List;

@DataJpaTest
@ActiveProfiles("test")
public class OrderRepositoryTest {

    @Autowired
    private OrderRepository orderRepository;

    @BeforeEach
    public void setUp() {
        // Clear existing data to ensure a clean state
        orderRepository.deleteAll();
        
        Order order1 = new Order();
        order1.setEmail("test1@example.com");
        order1.setOrderId(1L);
        order1.setOrderDate(LocalDate.now());
        order1.setOrderStatus("Processing");
        orderRepository.save(order1);

        Order order2 = new Order();
        order2.setEmail("test1@example.com");
        order2.setOrderId(2L);
        order2.setOrderDate(LocalDate.now()); 
        order2.setOrderStatus("Shipped");
        orderRepository.save(order2);
    }

    @Test
    public void testFindAllByEmail() {
        List<Order> orders = orderRepository.findAllByEmail("test1@example.com");
        orders.forEach(order -> System.out.println("Saved Order ID: " + order.getOrderId()));
        assertThat(orders).hasSize(2);
        assertThat(orders).extracting(Order::getOrderId).containsExactlyInAnyOrder(5L, 6L);
    }

    @Test
    public void testFindAllByEmail_NoMatch() {
        List<Order> orders = orderRepository.findAllByEmail("nonexistent@example.com");
        assertThat(orders).isEmpty();
    }

    @Test
    public void testFindOrderByEmailAndOrderId() {
        Order foundOrder = orderRepository.findOrderByEmailAndOrderId("test1@example.com", 1L);
        assertThat(foundOrder).isNotNull();
        assertThat(foundOrder.getOrderId()).isEqualTo(1L);
        assertThat(foundOrder.getEmail()).isEqualTo("test1@example.com");
    }

    @Test
    public void testFindOrderByEmailAndOrderId_NotFound() {
        Order foundOrder = orderRepository.findOrderByEmailAndOrderId("test1@example.com", 999L);
        assertThat(foundOrder).isNull();
    }
}
