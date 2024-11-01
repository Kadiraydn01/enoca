package com.enoca.ecommerce.service;

import com.enoca.ecommerce.entity.Order;
import com.enoca.ecommerce.entity.OrderDetail;
import com.enoca.ecommerce.entity.Product;
import com.enoca.ecommerce.exceptions.OrderException;
import com.enoca.ecommerce.exceptions.OrderValidation;
import com.enoca.ecommerce.repository.OrderDetailRepository;
import com.enoca.ecommerce.repository.OrderRepository;
import com.enoca.ecommerce.repository.ProductRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.util.List;

@Service
public class OrderServiceImpl implements OrderService{
    @Autowired
    OrderRepository orderRepository;
    @Autowired
    OrderDetailRepository orderDetailRepository;
    @Autowired
    ProductService productService;


    @Override
    public Order save(Order order) {

        List<OrderDetail> orderDetails = order.getOrderDetails();

        for (OrderDetail orderDetail : orderDetails) {
            
            Product product = productService.getProduct(orderDetail.getProduct().getId());
            OrderValidation.isNotValidProductStock(product.getStock());
            product.setStock(product.getStock() - orderDetail.getQuantity());
            
            productService.updateProduct(product);
        }
        return orderRepository.save(order);
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
    

