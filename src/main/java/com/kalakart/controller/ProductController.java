package com.kalakart.controller;

import com.kalakart.model.Product;
import com.kalakart.service.ProductService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@RestController
@RequestMapping("/products")
@CrossOrigin(origins = "https://kalakart-frontend.onrender.com")
public class ProductController {
    
    @Autowired
    private ProductService productService;
    
    // ===== PUBLIC ENDPOINTS (for all users) =====
    
    // Get all products
    @GetMapping
    public List<Product> getAllProducts() {
        return productService.getAllProducts();
    }
    
    // Get product by ID
    @GetMapping("/{id}")
    public Product getProductById(@PathVariable Long id) {
        return productService.getProductById(id);
    }
    
    // Get products by state
    @GetMapping("/state/{state}")
    public List<Product> getProductsByState(@PathVariable String state) {
        return productService.getProductsByState(state);
    }
    
    // Get products by category
    @GetMapping("/category/{category}")
    public List<Product> getProductsByCategory(@PathVariable String category) {
        return productService.getProductsByCategory(category);
    }
    
    // Search products
    @GetMapping("/search")
    public List<Product> searchProducts(@RequestParam String keyword) {
        return productService.searchProducts(keyword);
    }
    
    // Get products by artisan
    @GetMapping("/artisan/{artisanId}")
    public List<Product> getProductsByArtisan(@PathVariable Long artisanId) {
        return productService.getProductsByArtisan(artisanId);
    }
    
    // ===== ARTISAN ENDPOINTS =====
    
    // Add product (artisan only)
    @PostMapping("/artisan/{artisanId}")
    public Product addProduct(@RequestBody Product product, @PathVariable Long artisanId) {
        return productService.addProduct(product, artisanId);
    }
    
    // Update product
    @PutMapping("/{id}")
    public Product updateProduct(@PathVariable Long id, @RequestBody Product product) {
        return productService.updateProduct(id, product);
    }
    
    // Delete product
    @DeleteMapping("/{id}")
    public String deleteProduct(@PathVariable Long id) {
        productService.deleteProduct(id);
        return "Product deleted successfully";
    }
    
    // ===== ADMIN ENDPOINTS (for data migration) =====
    
    // Bulk import products (temporary endpoint for migration)
    @PostMapping("/admin/import")
    public ResponseEntity<String> importProducts(@RequestBody List<Product> products) {
        try {
            // Set IDs to null so PostgreSQL generates new ones
            for (Product product : products) {
                product.setId(null);
            }
            
            // Save all products
            List<Product> savedProducts = productService.saveAll(products);
            return ResponseEntity.ok("Successfully imported " + savedProducts.size() + " products");
        } catch (Exception e) {
            return ResponseEntity.badRequest().body("Import failed: " + e.getMessage());
        }
    }
}