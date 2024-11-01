package com.enoca.ecommerce.controller;
import com.enoca.ecommerce.dto.CartResponse;
import com.enoca.ecommerce.entity.Cart;
import com.enoca.ecommerce.entity.Customer;
import com.enoca.ecommerce.entity.Product;
import com.enoca.ecommerce.service.CartService;
import com.enoca.ecommerce.service.CustomerService;
import com.enoca.ecommerce.service.ProductService;
import com.enoca.ecommerce.util.DtoConverter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;

@RestController
@RequestMapping("/cart")
public class CartController {
    @Autowired
    private CartService cartService;
    @Autowired
    private CustomerService customerService;
    @Autowired
    private ProductService productService;



    @PostMapping("/add")
    public ResponseEntity<CartResponse> createCart(@RequestBody Cart cart, @RequestParam long customerId) {
        Customer customer = customerService.getCustomer(customerId);
        if (customer != null) {
            Cart createdCart = cartService.createCart(cart, customer);
            CartResponse response = DtoConverter.convertToCartResponse(createdCart);
            return ResponseEntity.ok(response);
        } else {
            return ResponseEntity.notFound().build();
        }
    }
    @GetMapping("/{id}")
    public ResponseEntity<CartResponse> getCart(@PathVariable long id) {
        Cart cart = cartService.getCart(id);
        if (cart != null) {
            CartResponse response = DtoConverter.convertToCartResponse(cart);
            return ResponseEntity.ok(response);
        } else {
            return ResponseEntity.notFound().build();
        }
    }

    @PutMapping("/update")
    public ResponseEntity<CartResponse> updateCart(@RequestBody Cart cart) {
        Cart updatedCart = cartService.updateCart(cart);
        if (updatedCart != null) {
            CartResponse response = DtoConverter.convertToCartResponse(updatedCart);
            return ResponseEntity.ok(response);
        } else {
            return ResponseEntity.notFound().build();
        }
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteCart(@PathVariable long id) {
        cartService.deleteCart(id);
        return ResponseEntity.noContent().build();
    }

    @PostMapping("/{cartId}/addProduct/{productId}")
    public ResponseEntity<CartResponse> addToCart(@PathVariable long cartId, @PathVariable long productId, @RequestParam int quantity) {
        Cart cart = cartService.getCart(cartId);
        Product product = productService.getProduct(productId);
        if (cart != null && product != null) {
            Cart updatedCart = cartService.addProductToCart(product, quantity, cart);
            CartResponse response = DtoConverter.convertToCartResponse(updatedCart);
            return ResponseEntity.ok(response);
        } else {
            return ResponseEntity.notFound().build();
        }
    }

    @PostMapping("/{cartId}/removeProduct/{productId}")
    public ResponseEntity<CartResponse> removeFromCart(@PathVariable long cartId, @PathVariable long productId, @RequestParam int quantity) {
        Cart cart = cartService.getCart(cartId);
        Product product = productService.getProduct(productId);
        if (cart != null && product != null) {
            Cart updatedCart = cartService.removeProductFromCart(product, quantity, cart);
            CartResponse response = DtoConverter.convertToCartResponse(updatedCart);
            return ResponseEntity.ok(response);
        } else {
            return ResponseEntity.notFound().build();
        }
    }

    @PostMapping("/{cartId}/increaseProduct/{productId}")
    public ResponseEntity<Cart> increaseQuantity(@PathVariable long cartId, @PathVariable long productId, @RequestParam int quantity) {
        Cart cart = cartService.getCart(cartId);
        Product product = productService.getProduct(productId);
        if (cart != null && product != null) {
            return ResponseEntity.ok(cartService.increaseProductQuantity(product, quantity, cart));   
        } else {
            return ResponseEntity.notFound().build();
        }
    }

    @PostMapping("/{cartId}/decreaseProduct/{productId}")
    public ResponseEntity<Cart> decreaseQuantity(@PathVariable long cartId, @PathVariable long productId, @RequestParam int quantity) {
        Cart cart = cartService.getCart(cartId);
        Product product = productService.getProduct(productId);
        if (cart != null && product != null) {
            return ResponseEntity.ok(cartService.decreaseProductQuantity(product, quantity, cart));
        } else {
            return ResponseEntity.notFound().build();
        }
    }

}
