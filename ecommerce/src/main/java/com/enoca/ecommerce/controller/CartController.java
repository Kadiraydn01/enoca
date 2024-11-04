package com.enoca.ecommerce.controller;
import com.enoca.ecommerce.entity.Cart;
import com.enoca.ecommerce.entity.Customer;
import com.enoca.ecommerce.entity.Product;
import com.enoca.ecommerce.service.CartService;
import com.enoca.ecommerce.service.CustomerService;
import com.enoca.ecommerce.service.ProductService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;


@RestController
@RequestMapping("/cart")
public class CartController {

    private final CartService cartService;

    private final CustomerService customerService;

    private final ProductService productService;

    @Autowired
    public CartController(CartService cartService, CustomerService customerService, ProductService productService) {
        this.cartService = cartService;
        this.customerService = customerService;
        this.productService = productService;
    }



    @PostMapping("/add")
    public ResponseEntity<Cart> createCart(@RequestBody Cart cart, @RequestParam long customerId) {
        Customer customer = customerService.getCustomer(customerId);
        if (customer != null) {
            Cart createdCart = cartService.createCart(cart, customer);
            return ResponseEntity.ok(createdCart);
        } else {
            return ResponseEntity.notFound().build();
        }
    }
    @GetMapping("/{id}")
    public ResponseEntity<Cart> getCart(@PathVariable long id) {
        Cart cart = cartService.getCart(id);
        if (cart != null) {
            return ResponseEntity.ok(cart);
        } else {
            return ResponseEntity.notFound().build();
        }
    }

    @PutMapping("/update")
    public ResponseEntity<Cart> updateCart(@RequestBody Cart cart) {
        Cart updatedCart = cartService.updateCart(cart);
        if (updatedCart != null) {
            return ResponseEntity.ok(updatedCart);

        } else {
            return ResponseEntity.notFound().build();
        }
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> clearCart(@PathVariable long id) {
        cartService.clearCart(id);
        return ResponseEntity.noContent().build();
    }

    @PostMapping("/{cartId}/addProduct/{productId}")
    public ResponseEntity<Cart> addToCart(@PathVariable long cartId, @PathVariable long productId, @RequestParam int quantity) {
        Cart cart = cartService.getCart(cartId);
        Product product = productService.getProduct(productId);
        if (cart != null && product != null) {
            Cart updatedCart = cartService.addProductToCart(product, quantity, cart);
            return ResponseEntity.ok(updatedCart);

        } else {
            return ResponseEntity.notFound().build();
        }
    }

    @PostMapping("/{cartId}/removeProduct/{productId}")
    public ResponseEntity<Cart> removeFromCart(@PathVariable long cartId, @PathVariable long productId, @RequestParam int quantity) {
        Cart cart = cartService.getCart(cartId);
        Product product = productService.getProduct(productId);
        if (cart != null && product != null) {
            Cart updatedCart = cartService.removeProductFromCart(product, quantity, cart);
            return ResponseEntity.ok(updatedCart);

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
