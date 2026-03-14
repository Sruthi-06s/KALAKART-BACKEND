package com.kalakart.controller;

import com.kalakart.model.Cart;
import com.kalakart.service.CartService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import java.util.List;
import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/cart")
@CrossOrigin(origins = "https://kalakart-frontend.onrender.com")
public class CartController {
    
    @Autowired
    private CartService cartService;
    
    // Add to cart
    @PostMapping("/add/{userId}/{productId}")
    public Cart addToCart(
            @PathVariable Long userId,
            @PathVariable Long productId,
            @RequestParam(defaultValue = "1") int quantity) {
        return cartService.addToCart(userId, productId, quantity);
    }
    
    // Get user's cart
    @GetMapping("/{userId}")
    public List<Cart> getUserCart(@PathVariable Long userId) {
        return cartService.getUserCart(userId);
    }
    
    // Get cart total
    @GetMapping("/{userId}/total")
    public Map<String, Object> getCartTotal(@PathVariable Long userId) {
        double total = cartService.getCartTotal(userId);
        List<Cart> items = cartService.getUserCart(userId);
        
        Map<String, Object> response = new HashMap<>();
        response.put("total", total);
        response.put("itemCount", items.size());
        response.put("items", items);
        
        return response;
    }
    
    // Update quantity
    @PutMapping("/update/{cartId}")
    public Cart updateQuantity(
            @PathVariable Long cartId,
            @RequestParam int quantity) {
        return cartService.updateQuantity(cartId, quantity);
    }
    
    // Remove from cart
    @DeleteMapping("/remove/{cartId}")
    public String removeFromCart(@PathVariable Long cartId) {
        cartService.removeFromCart(cartId);
        return "Item removed from cart";
    }
    
    // Clear cart
    @DeleteMapping("/clear/{userId}")
    public String clearCart(@PathVariable Long userId) {
        cartService.clearCart(userId);
        return "Cart cleared";
    }
}