package com.kalakart.service;

import com.kalakart.model.Cart;
import com.kalakart.model.User;
import com.kalakart.model.Product;
import com.kalakart.repository.CartRepository;
import com.kalakart.repository.UserRepository;
import com.kalakart.repository.ProductRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.util.List;
import java.util.Optional;

@Service
public class CartService {
    
    @Autowired
    private CartRepository cartRepository;
    
    @Autowired
    private UserRepository userRepository;
    
    @Autowired
    private ProductRepository productRepository;
    
    // Add item to cart
    @Transactional
    public Cart addToCart(Long userId, Long productId, int quantity) {
        User user = userRepository.findById(userId)
            .orElseThrow(() -> new RuntimeException("User not found"));
            
        Product product = productRepository.findById(productId)
            .orElseThrow(() -> new RuntimeException("Product not found"));
        
        // Check if product already in cart
        Optional<Cart> existingCart = cartRepository.findByUserAndProductId(user, productId);
        
        if (existingCart.isPresent()) {
            // Update quantity if already exists
            Cart cart = existingCart.get();
            cart.setQuantity(cart.getQuantity() + quantity);
            return cartRepository.save(cart);
        } else {
            // Create new cart item
            Cart cart = new Cart();
            cart.setUser(user);
            cart.setProduct(product);
            cart.setQuantity(quantity);
            return cartRepository.save(cart);
        }
    }
    
    // Get user's cart
    public List<Cart> getUserCart(Long userId) {
        User user = userRepository.findById(userId)
            .orElseThrow(() -> new RuntimeException("User not found"));
            
        return cartRepository.findByUser(user);
    }
    
    // Update cart item quantity
    @Transactional
    public Cart updateQuantity(Long cartId, int quantity) {
        Cart cart = cartRepository.findById(cartId)
            .orElseThrow(() -> new RuntimeException("Cart item not found"));
        
        if (quantity <= 0) {
            cartRepository.delete(cart);
            return null;
        } else {
            cart.setQuantity(quantity);
            return cartRepository.save(cart);
        }
    }
    
    // Remove item from cart
    @Transactional
    public void removeFromCart(Long cartId) {
        Cart cart = cartRepository.findById(cartId)
            .orElseThrow(() -> new RuntimeException("Cart item not found"));
            
        cartRepository.delete(cart);
    }
    
    // Clear entire cart (after order placement) - FIXED WITH TRANSACTIONAL
    @Transactional
    public void clearCart(Long userId) {
        try {
            System.out.println("🗑️ Clearing cart for user ID: " + userId);
            
            User user = userRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("User not found with id: " + userId));
            
            List<Cart> cartItems = cartRepository.findByUser(user);
            System.out.println("Found " + cartItems.size() + " items to delete");
            
            if (!cartItems.isEmpty()) {
                cartRepository.deleteAll(cartItems);
                System.out.println("✅ Cart cleared successfully");
            } else {
                System.out.println("Cart already empty");
            }
            
        } catch (Exception e) {
            System.err.println("❌ Error clearing cart: " + e.getMessage());
            e.printStackTrace();
            throw new RuntimeException("Failed to clear cart: " + e.getMessage());
        }
    }
    
    // Get cart total
    public double getCartTotal(Long userId) {
        List<Cart> cartItems = getUserCart(userId);
        
        return cartItems.stream()
            .mapToDouble(Cart::getTotalPrice)
            .sum();
    }
    public List<Cart> saveAll(List<Cart> cartItems) {
        return cartRepository.saveAll(cartItems);
    }
}