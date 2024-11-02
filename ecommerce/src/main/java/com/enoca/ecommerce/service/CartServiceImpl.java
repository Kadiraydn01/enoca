    package com.enoca.ecommerce.service;

    import com.enoca.ecommerce.entity.Cart;
    import com.enoca.ecommerce.entity.Customer;
    import com.enoca.ecommerce.entity.Order;
    import com.enoca.ecommerce.entity.Product;
    import com.enoca.ecommerce.repository.CartRepository;
    import com.enoca.ecommerce.repository.ProductRepository;
    import jakarta.transaction.Transactional;
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
        @Transactional
        public Cart addProductToCart(Product product, int quantity, Cart cart) {
            product = productRepository.findById(product.getId()).orElse(null);

            if (product != null) {
                // Sepetteki ürünleri kontrol et ve var olanı güncelle veya yeni ürün ekle
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

                productRepository.save(product);
                return cartRepository.save(cart);
            }

            return null;
        }




        @Override
        @Transactional
        public Cart removeProductFromCart(Product product, int quantity, Cart cart) {
            // Ürünü veritabanından bul
            product = productRepository.findById(product.getId()).orElse(null);
            if (product != null) {
                // Sepetteki ürünü bul
                Product existingProductInCart = null;
                for (Product p : cart.getProducts()) {
                    if (p.getId().equals(product.getId())) {
                        existingProductInCart = p;
                        break;
                    }
                }

                if (existingProductInCart != null) {
                    // Mevcut ürün miktarı
                    int currentQuantity = existingProductInCart.getQuantity();

                    // Çıkarılacak miktar mevcut miktardan fazla mı kontrol et
                    if (quantity > currentQuantity) {
                        throw new IllegalArgumentException("Cannot remove more than available quantity in cart.");
                    }

                    // Yeni miktarı hesapla
                    int newQuantity = currentQuantity - quantity;

                    // Sepetteki ürün miktarı sıfırsa ürünü kaldır
                    if (newQuantity == 0) {
                        cart.getProducts().remove(existingProductInCart);
                        existingProductInCart.setCart(null); // Customer cart değerini null yap
                    } else {
                        // Miktarı güncelle
                        existingProductInCart.setQuantity(newQuantity);
                    }

                    // Stok miktarını artır
                    product.setStock(product.getStock() + quantity);

                    // Toplam fiyatı güncelle
                    double totalPrice = cart.getProducts().stream()
                            .mapToDouble(p -> p.getPrice() * p.getQuantity())
                            .sum();
                    cart.setTotalPrice(totalPrice);

                    // Product içindeki quantity miktarını güncelle
                    existingProductInCart.setQuantity(newQuantity); // Bu satırı ekleyin

                    // Güncellemeleri kaydet
                    productRepository.save(product); // Stok güncelleme
                    if (newQuantity > 0) {
                        productRepository.save(existingProductInCart); // Sadece miktar sıfır değilse mevcut ürünü güncelle
                    }
                    return cartRepository.save(cart);
                }
            }
            return null;
        }


        @Override
        @Transactional
        public Cart increaseProductQuantity(Product product, int quantity, Cart cart) {
            // Sepette ürünü bul
            Product existingProductInCart = null;
            for (Product p : cart.getProducts()) {
                if (p.getId().equals(product.getId())) {
                    existingProductInCart = p;
                    break;
                }
            }

            if (existingProductInCart != null) {
                // Ürün zaten sepette mevcut, miktarı artır
                int newQuantity = existingProductInCart.getQuantity() + quantity;

                // Stok kontrolü
                if (quantity > product.getStock()) {
                    throw new IllegalArgumentException("Requested quantity exceeds available stock.");
                }

                existingProductInCart.setQuantity(newQuantity);
                existingProductInCart.setStock(product.getStock() - quantity);
            } else {
                // Ürün sepette yok, sepete ekle
                product.setQuantity(quantity); // İlk miktarı ayarla
                cart.getProducts().add(product);
            }

            // Toplam fiyatı güncelle
            double totalPrice = cart.getProducts().stream()
                    .mapToDouble(p -> p.getPrice() * p.getQuantity())
                    .sum();
            cart.setTotalPrice(totalPrice);

            // Güncellemeleri kaydet
            return cartRepository.save(cart);
        }

        @Override
        @Transactional
        public Cart decreaseProductQuantity(Product product, int quantity, Cart cart) {


            // Sepette ürünü buluyoruz
            Product existingProductInCart = null;
            for (Product p : cart.getProducts()) {
                if (p.getId().equals(product.getId())) {
                    existingProductInCart = p;
                    break;
                }
            }

            if (existingProductInCart != null) {


                // Mevcut ürün miktarını kontrol ediyoruz

                int currentQuantity = existingProductInCart.getQuantity();

                // Çıkarılacak miktar mevcut miktardan fazla mı onu kontrol ediyoruz.

                if (quantity > currentQuantity) {
                    throw new IllegalArgumentException("Cannot remove more than available quantity in cart.");
                }

                // Yeni miktarı hesapla
                int newQuantity = currentQuantity - quantity;

                // Stok miktarını artır
                product.setStock(product.getStock() + quantity);
                product.setQuantity(newQuantity);

                // Sepetteki ürün miktarı sıfırsa ürünü kaldır
                if (newQuantity <= 0) {
                    cart.getProducts().remove(existingProductInCart);
                    existingProductInCart.setCart(null); // Ürün sepet referansını kaldır
                } else {
                    // Miktarı güncelle
                    existingProductInCart.setQuantity(newQuantity);
                }

                // Toplam fiyatı güncelle
                double totalPrice = cart.getProducts().stream()
                        .mapToDouble(p -> p.getPrice() * p.getQuantity())
                        .sum();
                cart.setTotalPrice(totalPrice);

                // Güncellemeleri kaydet
                productRepository.save(product); // Ürünü güncelle
                return cartRepository.save(cart); // Sepeti güncelle
            }

            return null; // Ürün sepet bulunamadıysa null döner
        }



        @Override
        public Cart cartToOrder(Cart cart, Order order) {
            order.setOrderDetails(cart.getOrders());
            order.setCustomer(cart.getCustomer());
            cartRepository.delete(cart);
            return cart;

        }
    }