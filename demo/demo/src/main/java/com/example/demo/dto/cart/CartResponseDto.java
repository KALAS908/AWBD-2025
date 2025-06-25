package com.example.demo.dto.cart;


import com.example.demo.Models.CartItem;
import com.example.demo.dto.product.ProductResponseDto;
import com.example.demo.service.ProductService;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class CartResponseDto {
    private String userId;
    private String productId;
    private int quantity;
    private ProductResponseDto product;

}
