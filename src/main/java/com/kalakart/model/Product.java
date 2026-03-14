package com.kalakart.model;

import jakarta.persistence.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "products")
public class Product {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    private String name;
    
    private String description;
    
    private double price;
    
    private int stock;
    
    private String category;
    
    private String state; // e.g., "TAMIL_NADU", "RAJASTHAN"
    
    private String imageUrl;
    
    private LocalDateTime createdAt;
    
    @ManyToOne
    @JoinColumn(name = "artisan_id")
    private User artisan;
    
    // Constructors
    public Product() {}
    
    public Product(String name, String description, double price, int stock, 
                   String category, String state, String imageUrl, User artisan) {
        this.name = name;
        this.description = description;
        this.price = price;
        this.stock = stock;
        this.category = category;
        this.state = state;
        this.imageUrl = imageUrl;
        this.artisan = artisan;
        this.createdAt = LocalDateTime.now();
    }
    
    // Getters and Setters
    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }
    
    public String getName() { return name; }
    public void setName(String name) { this.name = name; }
    
    public String getDescription() { return description; }
    public void setDescription(String description) { this.description = description; }
    
    public double getPrice() { return price; }
    public void setPrice(double price) { this.price = price; }
    
    public int getStock() { return stock; }
    public void setStock(int stock) { this.stock = stock; }
    
    public String getCategory() { return category; }
    public void setCategory(String category) { this.category = category; }
    
    public String getState() { return state; }
    public void setState(String state) { this.state = state; }
    
    public String getImageUrl() { return imageUrl; }
    public void setImageUrl(String imageUrl) { this.imageUrl = imageUrl; }
    
    public LocalDateTime getCreatedAt() { return createdAt; }
    public void setCreatedAt(LocalDateTime createdAt) { this.createdAt = createdAt; }
    
    public User getArtisan() { return artisan; }
    public void setArtisan(User artisan) { this.artisan = artisan; }
}