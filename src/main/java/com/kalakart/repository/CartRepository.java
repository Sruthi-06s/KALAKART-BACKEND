package com.kalakart.repository;

import com.kalakart.model.Cart;
import com.kalakart.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.transaction.annotation.Transactional;
import java.util.List;
import java.util.Optional;

public interface CartRepository extends JpaRepository<Cart, Long> {
    
    // Find all cart items for a user
    List<Cart> findByUser(User user);
    
    // Find specific product in user's cart
    Optional<Cart> findByUserAndProductId(User user, Long productId);
    
    // Delete all items for a user (when order is placed)
    void deleteByUser(User user);
    
    // Alternative: Delete by user ID directly
    @Modifying
    @Transactional
    @Query("DELETE FROM Cart c WHERE c.user.id = :userId")
    void deleteByUserId(@Param("userId") Long userId);
}