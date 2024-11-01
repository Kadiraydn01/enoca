package com.enoca.ecommerce.service;

import com.enoca.ecommerce.entity.Product;

import java.util.List;

public interface ProductService {
    Product addProduct(Product product);
    List<Product> listProducts();
    Product getProduct(Long id);
    Product updateProduct(Product product);
    Product deleteProduct(Long id);
}
