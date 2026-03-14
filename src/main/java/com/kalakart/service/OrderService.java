package com.kalakart.service;

import com.kalakart.model.*;
import com.kalakart.repository.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.util.List;

@Service
public class OrderService {
    
    @Autowired
    private OrderRepository orderRepository;
    
    @Autowired
    private OrderItemRepository orderItemRepository;
    
    @Autowired
    private CartRepository cartRepository;
    
    @Autowired
    private UserRepository userRepository;
    
    @Autowired
    private ProductRepository productRepository;
    
    // Place order from cart
    @Transactional
    public Order placeOrderFromCart(Long userId, String shippingAddress, String paymentMethod) {
        
        // Get user
        User user = userRepository.findById(userId)
            .orElseThrow(() -> new RuntimeException("User not found"));
        
        // Get user's cart
        List<Cart> cartItems = cartRepository.findByUser(user);
        
        if (cartItems.isEmpty()) {
            throw new RuntimeException("Cart is empty");
        }
        
        // Create new order
        Order order = new Order();
        order.setUser(user);
        order.setShippingAddress(shippingAddress);
        order.setPaymentMethod(paymentMethod);
        
        double totalAmount = 0.0;
        
        // Create order items from cart
        for (Cart cart : cartItems) {
            OrderItem orderItem = new OrderItem();
            orderItem.setProduct(cart.getProduct());
            orderItem.setQuantity(cart.getQuantity());
            orderItem.setPrice(cart.getProduct().getPrice()); // Save price at time of order
            orderItem.setOrder(order);
            
            order.getItems().add(orderItem);
            
            totalAmount += cart.getProduct().getPrice() * cart.getQuantity();
            
            // Update product stock
            Product product = cart.getProduct();
            product.setStock(product.getStock() - cart.getQuantity());
            productRepository.save(product);
        }
        
        order.setTotalAmount(totalAmount);
        
        // Save order (this will also save order items due to cascade)
        Order savedOrder = orderRepository.save(order);
        
        // Clear the cart
        cartRepository.deleteByUser(user);
        
        return savedOrder;
    }
    
    // Place single item order (direct buy)
    @Transactional
    public Order placeSingleItemOrder(Long userId, Long productId, int quantity, 
                                      String shippingAddress, String paymentMethod) {
        
        User user = userRepository.findById(userId)
            .orElseThrow(() -> new RuntimeException("User not found"));
            
        Product product = productRepository.findById(productId)
            .orElseThrow(() -> new RuntimeException("Product not found"));
        
        // Check stock
        if (product.getStock() < quantity) {
            throw new RuntimeException("Insufficient stock");
        }
        
        // Create new order
        Order order = new Order();
        order.setUser(user);
        order.setShippingAddress(shippingAddress);
        order.setPaymentMethod(paymentMethod);
        
        // Create order item
        OrderItem orderItem = new OrderItem();
        orderItem.setProduct(product);
        orderItem.setQuantity(quantity);
        orderItem.setPrice(product.getPrice());
        orderItem.setOrder(order);
        
        order.getItems().add(orderItem);
        order.setTotalAmount(product.getPrice() * quantity);
        
        // Update stock
        product.setStock(product.getStock() - quantity);
        productRepository.save(product);
        
        return orderRepository.save(order);
    }
    
    // Get user orders
    public List<Order> getUserOrders(Long userId) {
        User user = userRepository.findById(userId)
            .orElseThrow(() -> new RuntimeException("User not found"));
            
        return orderRepository.findByUserOrderByOrderDateDesc(user);
    }
    
    // Get order by ID
    public Order getOrderById(Long orderId) {
        return orderRepository.findById(orderId)
            .orElseThrow(() -> new RuntimeException("Order not found"));
    }
    
    // Get order by order number
    public Order getOrderByNumber(String orderNumber) {
        Order order = orderRepository.findByOrderNumber(orderNumber);
        if (order == null) {
            throw new RuntimeException("Order not found");
        }
        return order;
    }
    
    // Get all orders (admin)
    public List<Order> getAllOrders() {
        return orderRepository.findAll();
    }
    
    // Update order status
    public Order updateOrderStatus(Long orderId, String status) {
        Order order = getOrderById(orderId);
        order.setStatus(status);
        return orderRepository.save(order);
    }
    
    // Cancel order
    @Transactional
    public Order cancelOrder(Long orderId) {
        Order order = getOrderById(orderId);
        
        // Can only cancel if status is PLACED
        if (!"PLACED".equals(order.getStatus())) {
            throw new RuntimeException("Order cannot be cancelled - status is " + order.getStatus());
        }
        
        // Restore stock
        for (OrderItem item : order.getItems()) {
            Product product = item.getProduct();
            product.setStock(product.getStock() + item.getQuantity());
            productRepository.save(product);
        }
        
        order.setStatus("CANCELLED");
        return orderRepository.save(order);
    }
    
    public List<Order> saveAll(List<Order> orders) {
        return orderRepository.saveAll(orders);
    }
    public List<OrderItem> saveAllItems(List<OrderItem> orderItems) {
        return orderItemRepository.saveAll(orderItems);
    }
}