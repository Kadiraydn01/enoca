package com.enoca.ecommerce.controller;


import com.enoca.ecommerce.entity.Customer;
import com.enoca.ecommerce.entity.Order;
import com.enoca.ecommerce.entity.OrderDetail;
import com.enoca.ecommerce.entity.Product;
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

    @Autowired
    public OrderController(OrderService orderService, CustomerService customerService, ProductService productService) {
        this.orderService = orderService;
        this.customerService = customerService;
        this.productService = productService;
    }

    @PostMapping("/add")
    public ResponseEntity<Order> saveOrder(@RequestBody Order order) {
        Customer customer = customerService.getCustomer(order.getCustomer().getId());
        if (customer == null) {
            return ResponseEntity.badRequest().build();
        }
        order.setCustomer(customer);


        List<OrderDetail> orderDetails = order.getOrderDetails();
        for (OrderDetail orderDetail : orderDetails) {
            Product product = productService.getProduct(orderDetail.getProduct().getId());
            if (product == null) {
                return ResponseEntity.badRequest().build();
            }
            orderDetail.setProduct(product);
            orderDetail.setOrder(order);
        }

        Order createdOrder = orderService.save(order);
        if (createdOrder != null) {
            return ResponseEntity.ok(createdOrder);
        } else {
            return ResponseEntity.badRequest().build();
        }
    }

    @GetMapping("/list")
    public ResponseEntity<List<Order>> findAll() {
          List<Order> orders = orderService.findAll();
            if (orders != null) {
                return ResponseEntity.ok(orders);
            } else {
                return ResponseEntity.notFound().build();
            }   
    }


    @GetMapping("/findById/{id}")
    public ResponseEntity<Order> findById(@PathVariable Long id) {

        Order order = orderService.findById(id);
        if (order != null) {
            return ResponseEntity.ok(order);
        } else {
            return ResponseEntity.notFound().build();
        }
    }


    @DeleteMapping("/delete/{id}")
    public void deleteById(@PathVariable Long id) {
        orderService.deleteById(id);
    }



    @PutMapping("/update/{id}")
    public ResponseEntity<Order> update(@PathVariable Long id, @RequestBody Order order) {
        Order updatedOrder = orderService.update(order);  
        if (updatedOrder != null) {
            return ResponseEntity.ok(updatedOrder);
        } else {
            return ResponseEntity.notFound().build();
        }
    }


    @GetMapping("/findByUserId/{userId}")
    public ResponseEntity<List<Order>> findByUserId(@PathVariable Long userId) {
        List<Order> orders = orderService.findByUserId(userId);
        if (orders != null) {
            return ResponseEntity.ok(orders);
        } else {
            return ResponseEntity.notFound().build();
        }
    }
}
