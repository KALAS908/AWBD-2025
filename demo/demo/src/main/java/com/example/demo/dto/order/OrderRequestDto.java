package com.example.demo.dto.order;

import com.example.demo.Models.Enums.OrderStatus;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
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
public class OrderRequestDto {

    @NotNull(message = "User ID cannot be null")
    private UUID userId;

    @NotEmpty(message = "Product IDs cannot be empty")
    private Set<UUID> productIds;

    @NotNull(message = "Order status cannot be null")
    private OrderStatus status;
}
