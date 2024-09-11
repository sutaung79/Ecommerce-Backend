package com.ecom.app.controllers;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import com.ecom.app.api.response.OrderResponse;
import com.ecom.app.config.AppConstants;
import com.ecom.app.dto.OrderDTO;
import com.ecom.app.services.OrderService;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;

@RestController
@RequestMapping("/api")
@SecurityRequirement(name = "E-Commerce Application")
@Tag(name = "Order Operations", description = "Endpoints related to order management")
public class OrderController {

    @Autowired
    public OrderService orderService;

    
    @PostMapping("/public/users/{emailId}/carts/{cartId}/payments/{paymentMethod}/order")
    @Operation(
        summary = "Place an order",
        description = "Place an order for products in the cart by providing user email, cart ID, and payment method."
    )
    @ApiResponse(responseCode = "201", description = "Order successfully placed")
    public ResponseEntity<OrderDTO> orderProducts(@PathVariable String emailId, @PathVariable Long cartId, @PathVariable String paymentMethod) {
        OrderDTO order = orderService.placeOrder(emailId, cartId, paymentMethod);
        return new ResponseEntity<OrderDTO>(order, HttpStatus.CREATED);
    }
    
    

    @GetMapping("/admin/orders")
    @Operation(
        summary = "Get all orders",
        description = "Retrieve a paginated and sorted list of all orders in the system."
    )
    @ApiResponses(value = {
        @ApiResponse(responseCode = "302", description = "Orders successfully found"),
        @ApiResponse(responseCode = "404", description = "No orders found")
    })
    public ResponseEntity<OrderResponse> getAllOrders(
            @RequestParam(name = "pageNumber", defaultValue = AppConstants.PAGE_NUMBER, required = false) Integer pageNumber,
            @RequestParam(name = "pageSize", defaultValue = AppConstants.PAGE_SIZE, required = false) Integer pageSize,
            @RequestParam(name = "sortBy", defaultValue = AppConstants.SORT_ORDERS_BY, required = false) String sortBy,
            @RequestParam(name = "sortOrder", defaultValue = AppConstants.SORT_DIR, required = false) String sortOrder) {

        OrderResponse orderResponse = orderService.getAllOrders(pageNumber, pageSize, sortBy, sortOrder);
        return new ResponseEntity<OrderResponse>(orderResponse, HttpStatus.FOUND);
    }

    
    
    @GetMapping("/public/users/{emailId}/orders")
    @Operation(
        summary = "Get all orders for a user",
        description = "Retrieve a list of all orders placed by a specific user, identified by their email."
    )
    @ApiResponse(responseCode = "302", description = "Orders successfully found")
    public ResponseEntity<List<OrderDTO>> getOrdersByUser(@PathVariable String emailId) {
        List<OrderDTO> orders = orderService.getOrdersByUser(emailId);
        return new ResponseEntity<List<OrderDTO>>(orders, HttpStatus.FOUND);
    }
    
    

    @GetMapping("/public/users/{emailId}/orders/{orderId}")
    @Operation(
        summary = "Get a specific order for a user",
        description = "Retrieve the details of a specific order placed by a user, identified by their email and order ID."
    )
    @ApiResponse(responseCode = "302", description = "Order successfully found")
    public ResponseEntity<OrderDTO> getOrderByUser(@PathVariable String emailId, @PathVariable Long orderId) {
        OrderDTO order = orderService.getOrder(emailId, orderId);
        return new ResponseEntity<OrderDTO>(order, HttpStatus.FOUND);
    }
    
    
    

    @PutMapping("/admin/users/{emailId}/orders/{orderId}/orderStatus/{orderStatus}")
    @Operation(
        summary = "Update order status",
        description = "Update the status of a specific order placed by a user, identified by their email and order ID."
    )
    @ApiResponse(responseCode = "200", description = "Order status successfully updated")
    public ResponseEntity<OrderDTO> updateOrderByUser(@PathVariable String emailId, @PathVariable Long orderId, @PathVariable String orderStatus) {
        OrderDTO order = orderService.updateOrder(emailId, orderId, orderStatus);
        return new ResponseEntity<OrderDTO>(order, HttpStatus.OK);
    }

}
