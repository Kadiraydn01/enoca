package com.enoca.ecommerce.controller;
import com.enoca.ecommerce.dto.ProductResponse;
import com.enoca.ecommerce.entity.Product;
import com.enoca.ecommerce.service.ProductService;
import com.enoca.ecommerce.util.DtoConverter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/product")
@CrossOrigin(origins = "*")
public class ProductController {

    @Autowired
    private ProductService productService;

    @PostMapping("/add")
    public ResponseEntity<ProductResponse> addProduct(@RequestBody Product product) {

        Product createdProduct = productService.addProduct(product);
        if (createdProduct != null) {
            ProductResponse response = DtoConverter.convertToProductResponse(createdProduct);
            return ResponseEntity.ok(response);
        } else {
            return ResponseEntity.badRequest().build();
        }
    
    }

    @GetMapping("/list")
    public ResponseEntity<List<ProductResponse  >> listProducts() {
        List<Product> products = productService.listProducts();
        if (products != null) {
            List<ProductResponse> responses = DtoConverter.convertToProductResponseList(products);
            return ResponseEntity.ok(responses);
        } else {
            return ResponseEntity.notFound().build();
        }
    }

    @GetMapping("/list/{id}")
    @CrossOrigin(origins = "*")
    public ResponseEntity<ProductResponse> getProduct(@PathVariable Long id) {
        Product product = productService.getProduct(id);
        if (product != null) {
            ProductResponse response = DtoConverter.convertToProductResponse(product);
            return ResponseEntity.ok(response);
        } else {
            return ResponseEntity.notFound().build();
        }
    }

    @PutMapping("/update")
    public ResponseEntity<ProductResponse> updateProduct(@RequestBody Product product) {
        Product updatedProduct = productService.updateProduct(product);
        if (updatedProduct != null) {
            ProductResponse response = DtoConverter.convertToProductResponse(updatedProduct);
            return ResponseEntity.ok(response);
        } else {
            return ResponseEntity.notFound().build();
        }
    }

    @DeleteMapping("/delete/{id}")
    public ResponseEntity<ProductResponse> deleteProduct(@PathVariable Long id) {
        Product deletedProduct = productService.deleteProduct(id);
        if (deletedProduct != null) {
            ProductResponse response = DtoConverter.convertToProductResponse(deletedProduct);
            return ResponseEntity.ok(response);
        } else {
            return ResponseEntity.notFound().build();
        }
    }
}
