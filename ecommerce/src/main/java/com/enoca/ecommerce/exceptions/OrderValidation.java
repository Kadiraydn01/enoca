package com.enoca.ecommerce.exceptions;

import org.springframework.http.HttpStatus;

public class OrderValidation {

    public static void validateOrder(long orderId) {
        if (orderId <= 0) {
            throw new OrderException("Order not found: " + orderId, HttpStatus.BAD_REQUEST);
        }
    }
    public static void isNotValidProductStock(long stock) {
        if (stock <= 0) {
            throw new OrderException("Product stock not valid:" + stock,HttpStatus.BAD_REQUEST);
        }
    }
}
