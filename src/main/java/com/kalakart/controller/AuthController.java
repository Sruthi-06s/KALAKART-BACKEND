package com.kalakart.controller;

import com.kalakart.dto.RegisterRequest;
import com.kalakart.model.User;
import com.kalakart.dto.LoginRequest;
import com.kalakart.dto.AuthResponse;
import com.kalakart.service.AuthService;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/auth")
@CrossOrigin(origins = "https://kalakart-frontend.onrender.com")
public class AuthController {
    
    @Autowired
    private AuthService authService;
    
    @PostMapping("/register")
    public String register(@RequestBody RegisterRequest request) {
        return authService.register(request);
    }
    
    // NEW LOGIN ENDPOINT
    @PostMapping("/login")
    public AuthResponse login(@RequestBody LoginRequest request) {
        return authService.login(request);
    }
    
    
    @PostMapping("/admin/import")
    public ResponseEntity<String> importUsers(@RequestBody List<User> users) {
        try {
            for (User user : users) {
                user.setId(null);
            }
            authService.saveAll(users);
            return ResponseEntity.ok("Successfully imported " + users.size() + " users");
        } catch (Exception e) {
            return ResponseEntity.badRequest().body("Import failed: " + e.getMessage());
        }
    }
}