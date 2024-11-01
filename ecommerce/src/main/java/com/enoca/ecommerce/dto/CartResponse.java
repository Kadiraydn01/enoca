package com.enoca.ecommerce.dto;

import com.enoca.ecommerce.entity.Product;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.List;
@Data
@NoArgsConstructor
@AllArgsConstructor
public class CartResponse {
    private long id;
    private int quantity;
    private double totalPrice;
    private List<ProductResponse> products;

    public void setProducts(List<Product> products) {
        List<ProductResponse> productResponses = new ArrayList<>();
        for (Product product : products) {
            ProductResponse productResponse = new ProductResponse();

            productResponse.setId(product.getId());
            productResponse.setName(product.getName());
            productResponse.setPrice(product.getPrice());
            productResponse.setStock((int) product.getStock());
            productResponses.add(productResponse);
        }
        this.products = productResponses;
    }
}
