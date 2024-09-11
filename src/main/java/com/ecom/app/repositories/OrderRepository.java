package com.ecom.app.repositories;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import com.ecom.app.entities.Order;


@Repository
public interface OrderRepository extends JpaRepository<Order, Long> {

	
	List<Order> findAllByEmail(String email);
	
     
    @Query("SELECT o FROM Order o WHERE o.email = ?1 AND o.id = ?2")
    Order findOrderByEmailAndOrderId(String email, Long orderId);

    
   
}
