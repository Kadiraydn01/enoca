    package com.enoca.ecommerce.service;

    import com.enoca.ecommerce.entity.Cart;
    import com.enoca.ecommerce.entity.Customer;
    import com.enoca.ecommerce.entity.Order;
    import com.enoca.ecommerce.entity.Product;
    import com.enoca.ecommerce.repository.CartRepository;
    import com.enoca.ecommerce.repository.ProductRepository;
    import org.springframework.beans.factory.annotation.Autowired;
    import org.springframework.stereotype.Service;

    import java.util.ArrayList;
    import java.util.List;

    @Service
    public class CartServiceImpl implements CartService {

        private final CartRepository cartRepository;

        private final ProductRepository productRepository;

        private final ProductService productService;

        @Autowired
        public CartServiceImpl(CartRepository cartRepository, ProductRepository productRepository, ProductService productService) {
            this.cartRepository = cartRepository;
            this.productRepository = productRepository;
            this.productService = productService;
        }

        @Override
        public Cart createCart(Cart cart, Customer customer) {

            List<Product> products = new ArrayList<>();
            long totalPrice = 0;
            for (Product product : cart.getProducts()) {
                Product existingProduct = productService.getProduct(product.getId());
                if (existingProduct != null) {
                    products.add(existingProduct);
                    totalPrice +=  (existingProduct.getPrice() * cart.getQuantity());
                }
            }

            cart.setProducts(products);

            cart.setCustomer(customer);


            return cartRepository.save(cart);
        }

        @Override
        public Cart getCart(long id) {
            return cartRepository.findById(id).orElse(null);
        }

        @Override
        public Cart updateCart(Cart cart) {
            return cartRepository.save(cart);
        }

        @Override
        public Cart deleteCart(long id) {
            Cart cart = getCart(id);
            if (cart != null) {
                cartRepository.delete(cart);
            }
            return cart;
        }

        @Override
        public Cart addProductToCart(Product product, int quantity, Cart cart) {
            product = productRepository.findById(product.getId()).orElse(null);
            if (product != null) {
                for (int i = 0; i < quantity; i++) {
                    cart.getProducts().add(product);
                }

                return cartRepository.save(cart);
            }
            return null;
        }

        @Override
        public Cart removeProductFromCart(Product product, int quantity, Cart cart) {
            product = productRepository.findById(product.getId()).orElse(null);
            if (product != null) {
                for (int i = 0; i < quantity; i++) {
                    cart.getProducts().remove(product);
                }

                return cartRepository.save(cart);
            }
            return null;
        }

        public Cart increaseProductQuantity(Product product, int quantity, Cart cart) {

            boolean productExistsInCart = cart.getProducts().stream()
                .anyMatch(p -> p.getId() == product.getId());

            if (!productExistsInCart) {

                cart.getProducts().add(product);
            }


            cart.setQuantity(cart.getQuantity() + quantity);



            return cartRepository.save(cart);
        }

        public Cart decreaseProductQuantity(Product product, int quantity, Cart cart) {

            boolean productExistsInCart = cart.getProducts().stream()
                    .anyMatch(p -> p.getId() == product.getId());

            if (productExistsInCart) {
                cart.getProducts().remove(product);
            }

            cart.setQuantity(Math.max(0,cart.getQuantity() - quantity) );


            return cartRepository.save(cart);
        }

        @Override
        public Cart cartToOrder(Cart cart, Order order) {
            order.setOrderDetails(cart.getOrders());
            order.setCustomer(cart.getCustomer());
            cartRepository.delete(cart);
            return cart;

        }
    }