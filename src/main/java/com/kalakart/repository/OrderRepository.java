package com.kalakart.repository;

import com.kalakart.model.Order;
import com.kalakart.model.User;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;

public interface OrderRepository extends JpaRepository<Order, Long> {
    
    // Find orders by user
    List<Order> findByUserOrderByOrderDateDesc(User user);
    
    // Find order by order number
    Order findByOrderNumber(String orderNumber);
    
    // Find orders by status
    List<Order> findByStatus(String status);
    
 }