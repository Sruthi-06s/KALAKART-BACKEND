package com.kalakart.service;

import com.kalakart.dto.RegisterRequest;
import com.kalakart.dto.LoginRequest;
import com.kalakart.dto.AuthResponse;
import com.kalakart.model.User;
import com.kalakart.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.util.List;
import java.util.Optional;

@Service
public class AuthService {
    
    @Autowired
    private UserRepository userRepository;
    
    public String register(RegisterRequest request) {
        Optional<User> existingUser = userRepository.findByEmail(request.getEmail());
        if (existingUser.isPresent()) {
            return "Email already registered";
        }
        
        User user = new User();
        user.setName(request.getName());
        user.setEmail(request.getEmail());
        user.setPassword(request.getPassword());
        user.setRole(request.getRole());
        
        // Artisans need approval
        if ("ARTISAN".equals(request.getRole())) {
            user.setApproved(false);
        } else {
            user.setApproved(true);
        }
        
        userRepository.save(user);
        return "Registration successful";
    }
    
    // FIXED LOGIN METHOD - Returns ALL user data
    public AuthResponse login(LoginRequest request) {
        Optional<User> userOptional = userRepository.findByEmail(request.getEmail());
        
        if (userOptional.isEmpty()) {
            return new AuthResponse("User not found", null, null, null, null);
        }
        
        User user = userOptional.get();
        
        if (!user.getPassword().equals(request.getPassword())) {
            return new AuthResponse("Invalid password", null, null, null, null);
        }
        
        if ("ARTISAN".equals(user.getRole()) && !user.isApproved()) {
            return new AuthResponse("Artisan not approved by admin", null, null, null, null);
        }
        
        // Return COMPLETE user data including ID, name, email
        return new AuthResponse(
            "Login successful",
            user.getRole(),
            user.getId(),
            user.getName(),
            user.getEmail()
        );
    }
    
    // ========== ADMIN METHODS ==========
    
    // Get all users
    public List<User> getAllUsers() {
        return userRepository.findAll();
    }
    
    // Get all artisans (pending and approved)
    public List<User> getAllArtisans() {
        return userRepository.findByRole("ARTISAN");
    }
    
    // Get pending artisans (not approved)
    public List<User> getPendingArtisans() {
        return userRepository.findByRoleAndApprovedFalse("ARTISAN");
    }
    
    // Approve an artisan
    public User approveArtisan(Long id) {
        Optional<User> userOptional = userRepository.findById(id);
        
        if (userOptional.isPresent()) {
            User user = userOptional.get();
            
            // Check if user is actually an artisan
            if ("ARTISAN".equals(user.getRole())) {
                user.setApproved(true);
                return userRepository.save(user);
            } else {
                throw new RuntimeException("User is not an artisan");
            }
        } else {
            throw new RuntimeException("User not found with id: " + id);
        }
    }
    
    // Delete user (admin only)
    public void deleteUser(Long id) {
        userRepository.deleteById(id);
    }
}