package com.enoca.ecommerce.service;

import com.enoca.ecommerce.entity.Cart;
import com.enoca.ecommerce.entity.Customer;
import com.enoca.ecommerce.entity.Order;
import com.enoca.ecommerce.entity.Product;
import org.springframework.stereotype.Service;


public interface CartService {
    Cart createCart(Cart cart, Customer customer);
    Cart getCart(long id);
    Cart updateCart(Cart cart);
    Cart deleteCart(long id);
    Cart addProductToCart(Product product, int quantity, Cart cart);
    Cart removeProductFromCart(Product product, int quantity, Cart cart); 
    Cart increaseProductQuantity(Product product, int quantity, Cart cart); 
    Cart decreaseProductQuantity(Product product,int quantity, Cart cart);
    Cart cartToOrder(Cart cart , Order order);

}