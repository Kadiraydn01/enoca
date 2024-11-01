package com.enoca.ecommerce.dto;

import com.enoca.ecommerce.entity.Product;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class OrderDetailResponse {
    private long id;
    private long quantity;
    private double totalPrice;
    private ProductResponse productResponse;


}
