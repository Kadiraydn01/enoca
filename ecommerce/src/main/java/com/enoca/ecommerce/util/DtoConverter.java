package com.enoca.ecommerce.util;

import com.enoca.ecommerce.dto.*;
import com.enoca.ecommerce.entity.*;

import java.util.List;
import java.util.stream.Collectors;

public class DtoConverter {
    public static CartResponse convertToCartResponse(Cart cart) {
        CartResponse response = new CartResponse();
        response.setId(cart.getId());
        response.setProducts(cart.getProducts());
        response.setQuantity(cart.getQuantity());
        response.setTotalPrice(cart.getTotalPrice());
        return response;
    }
    public static CustomerResponse convertToCustomerResponse(Customer customer) {
        CustomerResponse response = new CustomerResponse();
        response.setId(customer.getId());
        response.setUserName(customer.getUsername());

        return response;
    }


    public static OrderResponse convertToOrderResponse(Order order) {
        OrderResponse response = new OrderResponse();
        response.setId(order.getId());
        response.setAddress(order.getAddress());
        response.setCustomer(convertToCustomerResponse(order.getCustomer()));

        double totalPrice = 0.0;
        if (order.getOrderDetails() != null) {
            List<OrderDetailResponse> orderDetailResponses = order
                    .getOrderDetails().stream()
                    .map(DtoConverter::convertToOrderDetailResponse)
                    .collect(Collectors.toList());
            response.setOrderDetails(orderDetailResponses);

            totalPrice = orderDetailResponses.stream()
                    .mapToDouble(OrderDetailResponse::getTotalPrice)
                    .sum();
        }

        response.setTotalPrice(totalPrice);

        return response;
    }
    public static ProductResponse convertToProductResponse(Product product) {
        ProductResponse response = new ProductResponse();
        response.setId(product.getId());
        response.setName(product.getName());
        response.setPrice(product.getPrice());
        response.setStock(product.getStock());
        response.setDescription(product.getDescription());
        response.setImage(product.getImage());



        return response;
    }



    public static List<ProductResponse> convertToProductResponseList(List<Product> products) {
        return products.stream().map(DtoConverter::convertToProductResponse).toList();
    }



    public static List<OrderResponse> convertToOrderResponseList(List<Order> orders) {
        return orders.stream().map(DtoConverter::convertToOrderResponse).toList();
    }


    public static OrderDetailResponse convertToOrderDetailResponse(OrderDetail orderDetail) {
        OrderDetailResponse response = new OrderDetailResponse();
        response.setId(orderDetail.getId());
        response.setProductResponse( convertToProductResponse(orderDetail.getProduct()));
        response.setQuantity(orderDetail.getQuantity());
        response.setTotalPrice(orderDetail.getQuantity() * orderDetail.getProduct().getPrice());
    
        return response;
    }
}
