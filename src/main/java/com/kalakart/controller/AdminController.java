package com.kalakart.controller;

import com.kalakart.model.User;
import com.kalakart.model.Order;
import com.kalakart.model.Product;
import com.kalakart.service.AuthService;
import com.kalakart.service.OrderService;
import com.kalakart.service.ProductService;  // Use the existing ProductService
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/admin")
@CrossOrigin(origins = "*")
public class AdminController {
    
    @Autowired
    private AuthService authService;
    
    @Autowired
    private OrderService orderService;
    
    @Autowired
    private ProductService productService;  // This already exists!
    
    // ========== USER MANAGEMENT ==========
    
    @GetMapping("/users")
    public List<User> getAllUsers() {
        return authService.getAllUsers();
    }
    
    @GetMapping("/artisans")
    public List<User> getAllArtisans() {
        return authService.getAllArtisans();
    }
    
    @GetMapping("/artisans/pending")
    public List<User> getPendingArtisans() {
        return authService.getPendingArtisans();
    }
    
    @PutMapping("/approve/{id}")
    public User approveArtisan(@PathVariable Long id) {
        return authService.approveArtisan(id);
    }
    
    @DeleteMapping("/user/{id}")
    public String deleteUser(@PathVariable Long id) {
        authService.deleteUser(id);
        return "User deleted successfully";
    }
    
    // ========== PRODUCT MANAGEMENT (using existing ProductService methods) ==========
    
    @GetMapping("/products")
    public List<Product> getAllProducts() {
        return productService.getAllProducts();  // This already exists in ProductService
    }
    
    // ========== ORDER MANAGEMENT (using existing OrderService methods) ==========
    
    @GetMapping("/orders")
    public List<Order> getAllOrders() {
        return orderService.getAllOrders();  // This already exists in OrderService
    }
    
    @GetMapping("/orders/{orderId}")
    public Order getOrderById(@PathVariable Long orderId) {
        return orderService.getOrderById(orderId);  // This already exists
    }
    
    // ========== SIMPLE ANALYTICS ==========
    
    @GetMapping("/analytics/revenue")
    public Map<String, Object> getTotalRevenue() {
        Map<String, Object> response = new HashMap<>();
        List<Order> allOrders = orderService.getAllOrders();
        double totalRevenue = 0;
        for (Order order : allOrders) {
            totalRevenue += order.getTotalAmount();
        }
        response.put("totalRevenue", totalRevenue);
        return response;
    }
    
    @GetMapping("/analytics/stats")
    public Map<String, Object> getDashboardStats() {
        Map<String, Object> stats = new HashMap<>();
        
        stats.put("totalUsers", authService.getAllUsers().size());
        stats.put("totalArtisans", authService.getAllArtisans().size());
        stats.put("totalProducts", productService.getAllProducts().size());
        stats.put("totalOrders", orderService.getAllOrders().size());
        stats.put("pendingArtisans", authService.getPendingArtisans().size());
        
        double totalRevenue = 0;
        for (Order order : orderService.getAllOrders()) {
            totalRevenue += order.getTotalAmount();
        }
        stats.put("totalRevenue", totalRevenue);
        
        return stats;
    }
}