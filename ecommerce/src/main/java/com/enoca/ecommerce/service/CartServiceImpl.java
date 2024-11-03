    package com.enoca.ecommerce.service;

    import com.enoca.ecommerce.entity.*;
    import com.enoca.ecommerce.repository.CartRepository;
    import com.enoca.ecommerce.repository.OrderRepository;
    import com.enoca.ecommerce.repository.ProductRepository;
    import jakarta.transaction.Transactional;
    import org.springframework.beans.factory.annotation.Autowired;
    import org.springframework.stereotype.Service;

    import java.util.ArrayList;
    import java.util.List;
    import java.util.stream.Collectors;

    @Service
    public class CartServiceImpl implements CartService {

        private final CartRepository cartRepository;

        private final ProductRepository productRepository;

        private final ProductService productService;

        private final OrderRepository orderRepository;

        @Autowired
        public CartServiceImpl(CartRepository cartRepository,
                               ProductRepository productRepository,
                               ProductService productService,
                               OrderRepository orderRepository) {
            this.cartRepository = cartRepository;
            this.productRepository = productRepository;
            this.productService = productService;
            this.orderRepository = orderRepository;
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
        public Cart clearCart(long id) {
            Cart cart = getCart(id);
            if (cart != null) {
                // Break the association and adjust quantity and stock for each product
                cart.getProducts().forEach(product -> {
                    // Increase stock by the quantity in the cart
                    int quantityInCart = product.getQuantity();
                    product.setStock(product.getStock() + quantityInCart);

                    // Set quantity to 0 for this product in the cart
                    product.setQuantity(0);

                    // Remove the association with the cart and save the product
                    product.setCart(null);
                    productRepository.save(product);
                });

                // Clear the cart's products list and reset cart totals
                cart.getProducts().clear();
                cart.setTotalPrice(0.0);
                cart.setQuantity(0);

                // Save the updated cart
                cartRepository.save(cart);
            }
            return cart;
        }


        @Override
        @Transactional
        public Cart addProductToCart(Product product, int quantity, Cart cart) {
            product = productRepository.findById(product.getId()).orElse(null);

            if(quantity <= 0){
                throw new IllegalArgumentException("Quantity must be greater than 0.");
            }

            if (product != null) {
                Product existingProductInCart = null;
                for (Product p : cart.getProducts()) {
                    if (p.getId() == product.getId()) {
                        existingProductInCart = p;
                        break;
                    }
                }

                if (existingProductInCart != null) {
                    int newQuantity = existingProductInCart.getQuantity() + quantity;
                    if (newQuantity > product.getStock()) {
                        throw new IllegalArgumentException("Requested quantity exceeds available stock.");
                    }
                    existingProductInCart.setQuantity(newQuantity);
                } else {
                    Product newProduct = new Product();
                    newProduct.setId(product.getId());
                    newProduct.setDescription(product.getDescription());
                    newProduct.setImage(product.getImage());
                    newProduct.setName(product.getName());
                    newProduct.setPrice(product.getPrice());
                    newProduct.setStock(product.getStock() - quantity);
                    newProduct.setQuantity(quantity);
                    newProduct.setCart(cart);
                    if(newProduct.getStock() < 0){
                        throw new IllegalArgumentException("Requested quantity exceeds available stock.");
                    }
                    cart.getProducts().add(newProduct);
                }

                product.setStock(product.getStock() - quantity);

                double totalPrice = cart.getProducts().stream()
                        .mapToDouble(p -> p.getPrice() * p.getQuantity())
                        .sum();
                cart.setTotalPrice(totalPrice);
                cart.setQuantity(cart.getQuantity() + quantity);

                productRepository.save(product);
                return cartRepository.save(cart);
            }

            return null;
        }




        @Override
        @Transactional
        public Cart removeProductFromCart(Product product, int quantity, Cart cart) {
                if(quantity <= 0){
                    throw new IllegalArgumentException("Quantity must be greater than 0.");
                }
            product = productRepository.findById(product.getId()).orElse(null);
            if (product != null) {
                Product existingProductInCart = null;
                for (Product p : cart.getProducts()) {
                    if (p.getId().equals(product.getId())) {
                        existingProductInCart = p;
                        break;
                    }
                }
                if (existingProductInCart != null) {

                    int currentQuantity = existingProductInCart.getQuantity();

                    if (quantity > currentQuantity) {
                        throw new IllegalArgumentException("Cannot remove more than available quantity in cart.");
                    }

                    int newQuantity = currentQuantity - quantity;

                    if (newQuantity == 0) {
                        cart.getProducts().remove(existingProductInCart);
                        existingProductInCart.setCart(null);
                    } else {

                        existingProductInCart.setQuantity(newQuantity);
                    }

                    product.setStock(product.getStock() + quantity);

                    double totalPrice = cart.getProducts().stream()
                            .mapToDouble(p -> p.getPrice() * p.getQuantity())
                            .sum();
                    cart.setTotalPrice(totalPrice);

                    existingProductInCart.setQuantity(newQuantity);

                    productRepository.save(product);
                    if (newQuantity > 0) {
                        productRepository.save(existingProductInCart);
                    }
                    return cartRepository.save(cart);
                }
            }
            return null;
        }


        @Override
        @Transactional
        public Cart increaseProductQuantity(Product product, int quantity, Cart cart) {

            Product existingProductInCart = null;
            for (Product p : cart.getProducts()) {
                if (p.getId().equals(product.getId())) {
                    existingProductInCart = p;
                    break;
                }
            }

            if (existingProductInCart != null) {

                int newQuantity = existingProductInCart.getQuantity() + quantity;

                if (quantity > product.getStock()) {
                    throw new IllegalArgumentException("Requested quantity exceeds available stock.");
                }

                existingProductInCart.setQuantity(newQuantity);
                existingProductInCart.setStock(product.getStock() - quantity);
            } else {
                product.setQuantity(quantity);
                cart.getProducts().add(product);
            }

            double totalPrice = cart.getProducts().stream()
                    .mapToDouble(p -> p.getPrice() * p.getQuantity())
                    .sum();
            cart.setTotalPrice(totalPrice);

            return cartRepository.save(cart);
        }

        @Override
        @Transactional
        public Cart decreaseProductQuantity(Product product, int quantity, Cart cart) {

            Product existingProductInCart = null;
            for (Product p : cart.getProducts()) {
                if (p.getId().equals(product.getId())) {
                    existingProductInCart = p;
                    break;
                }
            }

            if (existingProductInCart != null) {

                int currentQuantity = existingProductInCart.getQuantity();


                if (quantity > currentQuantity) {
                    throw new IllegalArgumentException("Cannot remove more than available quantity in cart.");
                }

                int newQuantity = currentQuantity - quantity;

                product.setStock(product.getStock() + quantity);
                product.setQuantity(newQuantity);

                if (newQuantity <= 0) {
                    cart.getProducts().remove(existingProductInCart);
                    existingProductInCart.setCart(null);
                } else {
                    existingProductInCart.setQuantity(newQuantity);
                }

                double totalPrice = cart.getProducts().stream()
                        .mapToDouble(p -> p.getPrice() * p.getQuantity())
                        .sum();
                cart.setTotalPrice(totalPrice);

                productRepository.save(product);
                return cartRepository.save(cart);
            }

            return null;
        }



        @Override
        @Transactional
        public Order cartToOrder(Cart cart, String address) {

            Order order = new Order();
            order.setCustomer(cart.getCustomer());
            order.setAddress(address);

            List<OrderDetailHistory> orderDetailHistories = cart.getProducts().stream()
                    .map(product -> {
                        OrderDetailHistory history = new OrderDetailHistory();
                        history.setOrder(order);
                        history.setProduct(product);
                        history.setPriceAtPurchase(product.getPrice());
                        history.setQuantity(product.getQuantity());
                        history.setTotalPrice(product.getPrice() * product.getQuantity());
                        return history;
                    })
                    .collect(Collectors.toList());

            order.setOrderDetailHistories(orderDetailHistories);
            order.setTotalPrice(cart.getTotalPrice());

            orderRepository.save(order);

            // Make a copy of products in the cart to update quantities later
            List<Product> productsToUpdate = new ArrayList<>(cart.getProducts());

            // Clear cart details after order creation
            cart.getProducts().forEach(product -> product.setCart(null));
            cart.getProducts().clear();
            cart.setTotalPrice(0.0);
            cart.setQuantity(0);

            cartRepository.save(cart);

            // Set each product's quantity to 0 after clearing the cart
            productsToUpdate.forEach(product -> {
                product.setQuantity(0);
                productRepository.save(product);
            });

            return order;
        }


    }