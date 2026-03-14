package com.kalakart.controller;

import com.kalakart.model.Order;
import com.kalakart.service.OrderService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import java.util.List;
import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/orders")
@CrossOrigin(origins = "https://kalakart-frontend.onrender.com")
public class OrderController {
    
    @Autowired
    private OrderService orderService;
    
    // Place order from cart
    @PostMapping("/place/{userId}")
    public Order placeOrderFromCart(
            @PathVariable Long userId,
            @RequestParam String shippingAddress,
            @RequestParam String paymentMethod) {
        
        return orderService.placeOrderFromCart(userId, shippingAddress, paymentMethod);
    }
    
    // Place single item order
    @PostMapping("/place/{userId}/product/{productId}")
    public Order placeSingleItemOrder(
            @PathVariable Long userId,
            @PathVariable Long productId,
            @RequestParam int quantity,
            @RequestParam String shippingAddress,
            @RequestParam String paymentMethod) {
        
        return orderService.placeSingleItemOrder(userId, productId, quantity, 
                                                shippingAddress, paymentMethod);
    }
    
    // Get user orders
    @GetMapping("/user/{userId}")
    public List<Order> getUserOrders(@PathVariable Long userId) {
        return orderService.getUserOrders(userId);
    }
    
    // Get order by ID
    @GetMapping("/{orderId}")
    public Order getOrderById(@PathVariable Long orderId) {
        return orderService.getOrderById(orderId);
    }
    
    // Get order by order number
    @GetMapping("/number/{orderNumber}")
    public Order getOrderByNumber(@PathVariable String orderNumber) {
        return orderService.getOrderByNumber(orderNumber);
    }
    
    // Get order summary
    @GetMapping("/{orderId}/summary")
    public Map<String, Object> getOrderSummary(@PathVariable Long orderId) {
        Order order = orderService.getOrderById(orderId);
        
        Map<String, Object> summary = new HashMap<>();
        summary.put("orderNumber", order.getOrderNumber());
        summary.put("orderDate", order.getOrderDate());
        summary.put("totalAmount", order.getTotalAmount());
        summary.put("status", order.getStatus());
        summary.put("itemCount", order.getItems().size());
        summary.put("shippingAddress", order.getShippingAddress());
        summary.put("paymentMethod", order.getPaymentMethod());
        
        return summary;
    }
    
    // ===== ADMIN ENDPOINTS =====
    
    // Get all orders (admin only)
    @GetMapping("/admin/all")
    public List<Order> getAllOrders() {
        return orderService.getAllOrders();
    }
    
    // Update order status (admin only)
    @PutMapping("/admin/{orderId}/status")
    public Order updateOrderStatus(
            @PathVariable Long orderId,
            @RequestParam String status) {
        return orderService.updateOrderStatus(orderId, status);
    }
    
    // Cancel order (user or admin)
    @PutMapping("/{orderId}/cancel")
    public Order cancelOrder(@PathVariable Long orderId) {
        return orderService.cancelOrder(orderId);
    }
}