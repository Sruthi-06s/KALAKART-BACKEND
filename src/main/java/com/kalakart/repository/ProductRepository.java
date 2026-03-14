package com.kalakart.repository;

import com.kalakart.model.Product;
import com.kalakart.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;

public interface ProductRepository extends JpaRepository<Product, Long> {
    
    // Find by state (for state-wise browsing)
    List<Product> findByState(String state);
    
    // Find by category
    List<Product> findByCategory(String category);
    
    // Find by artisan
    List<Product> findByArtisan(User artisan);
    
    // Find by artisan ID
    List<Product> findByArtisanId(Long artisanId);
    
    // Search by name (contains)
    List<Product> findByNameContainingIgnoreCase(String keyword);
}