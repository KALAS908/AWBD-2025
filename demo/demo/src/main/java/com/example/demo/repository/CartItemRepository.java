package com.example.demo.repository;

import com.example.demo.Models.CartItem;
import com.example.demo.Models.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.UUID;

public interface CartItemRepository extends JpaRepository<CartItem, UUID> {
    List<CartItem> findByUser(User user);
    void deleteByUser(User user);
}
