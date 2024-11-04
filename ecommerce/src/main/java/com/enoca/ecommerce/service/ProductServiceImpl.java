package com.enoca.ecommerce.service;

import com.enoca.ecommerce.entity.Product;
import com.enoca.ecommerce.repository.ProductRepository;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ProductServiceImpl implements ProductService {
    @Autowired
    private final ProductRepository productRepository;

    public ProductServiceImpl(ProductRepository productRepository) {
        this.productRepository = productRepository;
    }

    @Override
    public Product addProduct(Product product) {
        return productRepository.save(product);
    }

    @Override
    public List<Product> listProducts() {
        return productRepository.findAll();
    }

    @Override
    public Product getProduct(Long id) {
        return productRepository.findById(id).orElse(null);
    }

    @Override
    public Product updateProduct(Long id, Product updatedProduct) {
        Product existingProduct = productRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Product not found"));

        if (updatedProduct.getName() != null) {
            existingProduct.setName(updatedProduct.getName());
        }
        if (updatedProduct.getDescription() != null) {
            existingProduct.setDescription(updatedProduct.getDescription());
        }
        if (updatedProduct.getPrice() != null) {
            existingProduct.setPrice(updatedProduct.getPrice());
        }
        if (updatedProduct.getStock() != null) {
            existingProduct.setStock(updatedProduct.getStock());
        }
        if (updatedProduct.getImage() != null) {
            existingProduct.setImage(updatedProduct.getImage());
        }

        return productRepository.save(existingProduct);
    }

    @Override
    public Product deleteProduct(Long id) {
        Product product = productRepository.findById(id).orElse(null);
        if (product != null) {
            productRepository.deleteById(id);
            return product;
        } else {
            return null;
        }
    }
}
