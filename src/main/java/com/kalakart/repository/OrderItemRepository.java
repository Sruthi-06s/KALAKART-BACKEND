package com.kalakart.repository;

import com.kalakart.model.OrderItem;
import org.springframework.data.jpa.repository.JpaRepository;

public interface OrderItemRepository extends JpaRepository<OrderItem, Long> {
    // We'll use basic CRUD methods
}