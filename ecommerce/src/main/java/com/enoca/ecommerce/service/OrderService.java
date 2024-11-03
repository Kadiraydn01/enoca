package com.enoca.ecommerce.service;

import com.enoca.ecommerce.entity.Order;

import java.util.List;

public interface OrderService {

    List<Order> findAll();
    Order findById(Long id);
    Order deleteById(Long id);
    Order update(Order order);
    List<Order> findByUserId(Long userId);

       
}
