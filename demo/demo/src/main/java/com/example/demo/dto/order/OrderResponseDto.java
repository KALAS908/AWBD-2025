package com.example.demo.dto.order;

import com.example.demo.Models.Enums.OrderStatus;
import com.example.demo.dto.product.ProductResponseDto;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.Set;
import java.util.UUID;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class OrderResponseDto {
    private UUID id;
    private UUID userId;
    private Set<ProductResponseDto> products;
    private Double totalPrice;
    private OrderStatus status;
}