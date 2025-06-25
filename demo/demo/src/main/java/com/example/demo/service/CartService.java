package com.example.demo.service;

import com.example.demo.Models.CartItem;
import com.example.demo.Models.Product;
import com.example.demo.Models.User;
import com.example.demo.repository.CartItemRepository;
import com.example.demo.repository.ProductRepository;
import com.example.demo.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class CartService {
    private final CartItemRepository cartItemRepo;
    private final UserRepository userRepo;
    private final ProductRepository productRepo;

    public List<CartItem> getCart(UUID userId) {
        User user = userRepo.findById(userId).orElseThrow();
        return cartItemRepo.findByUser(user);
    }

    public void addToCart(UUID userId, UUID productId, int quantity) {
        User user = userRepo.findById(userId).orElseThrow();
        Product product = productRepo.findById(productId).orElseThrow();

        CartItem item = cartItemRepo.findByUser(user).stream()
                .filter(ci -> ci.getProduct().getId().equals(productId))
                .findFirst()
                .orElse(null);

        if (item == null) {
            item = CartItem.builder()
                    .user(user)
                    .product(product)
                    .quantity(quantity)
                    .build();
        } else {
            item.setQuantity(item.getQuantity() + quantity);
        }
        cartItemRepo.save(item);
    }

    public void removeFromCart(UUID userId, UUID productId) {
        User user = userRepo.findById(userId).orElseThrow();
        cartItemRepo.findByUser(user).stream()
                .filter(ci -> ci.getProduct().getId().equals(productId))
                .findFirst()
                .ifPresent(cartItemRepo::delete);
    }

    public void clearCart(UUID userId) {
        User user = userRepo.findById(userId).orElseThrow();
        cartItemRepo.deleteByUser(user);
    }
}
