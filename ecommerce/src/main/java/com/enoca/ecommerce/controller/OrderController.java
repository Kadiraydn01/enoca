package com.enoca.ecommerce.controller;


import com.enoca.ecommerce.entity.*;
import com.enoca.ecommerce.service.CartService;
import com.enoca.ecommerce.service.CustomerService;
import com.enoca.ecommerce.service.OrderService;
import com.enoca.ecommerce.service.ProductService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/order")
public class OrderController {

    private final OrderService orderService;
    private final CustomerService customerService;
    private final ProductService productService;
    private final CartService cartService;  // CartService ekleniyor

    @Autowired
    public OrderController(OrderService orderService, CustomerService customerService, ProductService productService, CartService cartService) {
        this.orderService = orderService;
        this.customerService = customerService;
        this.productService = productService;
        this.cartService = cartService;
    }

    @PostMapping("/createFromCart/{cartId}")
    public ResponseEntity<Order> createOrderFromCart(@PathVariable Long cartId, @RequestBody Order order) {
        Cart cart = cartService.getCart(cartId);
        if (cart == null) {
            return ResponseEntity.badRequest().build();
        }

        try {
            Order createdOrder = cartService.cartToOrder(cart, order.getAddress());
            return ResponseEntity.ok(createdOrder);
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().body(null);
        }
    }


    @GetMapping("/list")
    public ResponseEntity<List<Order>> findAll() {
        List<Order> orders = orderService.findAll();
        return orders != null ? ResponseEntity.ok(orders) : ResponseEntity.notFound().build();
    }

    @GetMapping("/findById/{id}")
    public ResponseEntity<Order> findById(@PathVariable Long id) {
        Order order = orderService.findById(id);
        return order != null ? ResponseEntity.ok(order) : ResponseEntity.notFound().build();
    }

    @DeleteMapping("/delete/{id}")
    public void deleteById(@PathVariable Long id) {
        orderService.deleteById(id);
    }

    @PutMapping("/update/{id}")
    public ResponseEntity<Order> update(@PathVariable Long id, @RequestBody Order order) {
        Order updatedOrder = orderService.update(order);
        return updatedOrder != null ? ResponseEntity.ok(updatedOrder) : ResponseEntity.notFound().build();
    }

    @GetMapping("/findByUserId/{userId}")
    public ResponseEntity<List<Order>> findByUserId(@PathVariable Long userId) {
        List<Order> orders = orderService.findByUserId(userId);
        return orders != null ? ResponseEntity.ok(orders) : ResponseEntity.notFound().build();
    }
}

