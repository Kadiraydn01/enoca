package com.enoca.ecommerce.service;

import com.enoca.ecommerce.entity.Order;
import com.enoca.ecommerce.repository.OrderRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.util.List;

@Service
public class OrderServiceImpl implements OrderService{

    private final OrderRepository orderRepository;

    @Autowired
    public OrderServiceImpl(OrderRepository orderRepository) {
        this.orderRepository = orderRepository;

    }

    @Override
    public List<Order> findAll () {
        return orderRepository.findAll();
    }
    @Override
    public Order findById (Long id){
        return orderRepository.findById(id).orElse(null);
    }
    @Override
    public Order deleteById (Long id){
        Order order = orderRepository.findById(id).orElse(null);
        if (order != null) {
            orderRepository.deleteById(id);
            return order;
        } else {
            return null;
        }
    }
    @Override
    public Order update (Order order){
        return orderRepository.save(order);
    }
    @Override
    public List<Order> findByUserId (Long userId){
        return orderRepository.findByCustomerId(userId);
    }


}
    

