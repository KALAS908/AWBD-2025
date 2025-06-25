package com.example.demo.mapper;
import com.example.demo.Models.CartItem;
import com.example.demo.Models.Product;
import com.example.demo.Models.Review;
import com.example.demo.dto.cart.CartResponseDto;
import com.example.demo.dto.product.ProductResponseDto;
import com.example.demo.dto.review.ReviewResponseDto;

import java.util.stream.Collectors;

public class CartItemMapper {
    public static CartResponseDto toDto(CartItem cartItem) {
        return new CartResponseDto(
                cartItem.getUser().getId().toString(),
                cartItem.getProduct().getId().toString(),
                cartItem.getQuantity(),
                new ProductResponseDto(
                        cartItem.getProduct().getId(),
                        cartItem.getProduct().getName(),
                        cartItem.getProduct().getPrice(),
                        cartItem.getProduct().getPhoneModel(),
                        cartItem.getProduct().getCategory(),
                        new java.util.HashSet<>()
                )
        );
    }

}
