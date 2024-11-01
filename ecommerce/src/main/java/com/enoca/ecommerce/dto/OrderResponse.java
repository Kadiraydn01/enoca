package com.enoca.ecommerce.dto;

import com.enoca.ecommerce.entity.OrderDetail;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;
import java.util.stream.Collectors;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class OrderResponse{
    private long id;
    private String address;
    private List<OrderDetailResponse> orderDetails;
    private CustomerResponse customer;
    private double totalPrice;

   
}
