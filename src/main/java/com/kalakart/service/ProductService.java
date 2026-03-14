package com.kalakart.service;

import com.kalakart.model.Product;
import com.kalakart.model.User;
import com.kalakart.repository.ProductRepository;
import com.kalakart.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.util.List;
import java.util.Optional;

@Service
public class ProductService {
    
    @Autowired
    private ProductRepository productRepository;
    
    @Autowired
    private UserRepository userRepository;
    
    // Get all products (for browsing)
    public List<Product> getAllProducts() {
        return productRepository.findAll();
    }
    
    // Get product by ID
    public Product getProductById(Long id) {
        Optional<Product> product = productRepository.findById(id);
        return product.orElse(null);
    }
    
    // Get products by state
    public List<Product> getProductsByState(String state) {
        return productRepository.findByState(state.toUpperCase());
    }
    
    // Get products by category
    public List<Product> getProductsByCategory(String category) {
        return productRepository.findByCategory(category);
    }
    
    // Search products by name
    public List<Product> searchProducts(String keyword) {
        return productRepository.findByNameContainingIgnoreCase(keyword);
    }
    
    // Get products by artisan
    public List<Product> getProductsByArtisan(Long artisanId) {
        return productRepository.findByArtisanId(artisanId);
    }
    
    // Add product (only by approved artisans)
    public Product addProduct(Product product, Long artisanId) {
        Optional<User> artisanOpt = userRepository.findById(artisanId);
        
        if (artisanOpt.isPresent()) {
            User artisan = artisanOpt.get();
            
            // Check if artisan is approved
            if (!"ARTISAN".equals(artisan.getRole()) || !artisan.isApproved()) {
                throw new RuntimeException("Only approved artisans can add products");
            }
            
            product.setArtisan(artisan);
            return productRepository.save(product);
        } else {
            throw new RuntimeException("Artisan not found");
        }
    }
    
    // Update product
    public Product updateProduct(Long id, Product productDetails) {
        Optional<Product> productOpt = productRepository.findById(id);
        
        if (productOpt.isPresent()) {
            Product product = productOpt.get();
            
            product.setName(productDetails.getName());
            product.setDescription(productDetails.getDescription());
            product.setPrice(productDetails.getPrice());
            product.setStock(productDetails.getStock());
            product.setCategory(productDetails.getCategory());
            product.setState(productDetails.getState());
            product.setImageUrl(productDetails.getImageUrl());
            
            return productRepository.save(product);
        }
        
        return null;
    }
    
    // Delete product
    public void deleteProduct(Long id) {
        productRepository.deleteById(id);
    }
 // In ProductService.java
    public List<Product> saveAll(List<Product> products) {
        return productRepository.saveAll(products);
    }
}