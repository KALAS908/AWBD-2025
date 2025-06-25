package com.example.demo.controller;

import com.example.demo.Controller.OrderController;
import com.example.demo.Models.Enums.OrderStatus;
import com.example.demo.Models.Enums.PhoneModel;
import com.example.demo.dto.order.OrderRequestDto;
import com.example.demo.dto.order.OrderResponseDto;
import com.example.demo.dto.product.ProductResponseDto;
import com.example.demo.service.OrderService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

import java.util.*;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(OrderController.class)
@AutoConfigureMockMvc(addFilters = false)
class OrderControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockitoBean
    private OrderService orderService;

    @Autowired
    private ObjectMapper objectMapper;

    @Test
    void getOrderById_ShouldReturnOrder() throws Exception {
        // Arrange
        UUID orderId = UUID.randomUUID();
        UUID userId = UUID.randomUUID();

        OrderResponseDto responseDto = new OrderResponseDto(
                orderId, userId, Set.of(), 99.99, OrderStatus.PENDING);

        Mockito.when(orderService.getOrderById(orderId))
                .thenReturn(responseDto);

        // Act & Assert
        mockMvc.perform(get("/api/orders/{id}", orderId))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(orderId.toString()))
                .andExpect(jsonPath("$.userId").value(userId.toString()));
    }

    @Test
    void deleteOrder_ShouldReturnNoContentStatus() throws Exception {
        // Arrange
        UUID orderId = UUID.randomUUID();
        Mockito.doNothing().when(orderService).deleteOrder(orderId);

        // Act & Assert
        mockMvc.perform(delete("/api/orders/{id}", orderId))
                .andExpect(status().isNoContent());
    }

    @Test
    void getAllOrders_ShouldReturnListOfOrders() throws Exception {
        // Arrange
        UUID userId1 = UUID.randomUUID();
        UUID userId2 = UUID.randomUUID();

        OrderResponseDto order1 = new OrderResponseDto(
                UUID.randomUUID(), userId1, Set.of(), 99.99, OrderStatus.PENDING);
        OrderResponseDto order2 = new OrderResponseDto(
                UUID.randomUUID(), userId2, Set.of(), 149.99, OrderStatus.COMPLETED);

        Mockito.when(orderService.getAllOrders())
                .thenReturn(Arrays.asList(order1, order2));

        // Act & Assert
        mockMvc.perform(get("/api/orders"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.length()").value(2))
                .andExpect(jsonPath("$[0].totalPrice").value(99.99))
                .andExpect(jsonPath("$[1].totalPrice").value(149.99));
    }

    @Test
    void addProductsToOrder_ShouldReturnUpdatedOrder() throws Exception {
        // Arrange
        UUID orderId = UUID.randomUUID();
        UUID productId1 = UUID.randomUUID();
        UUID productId2 = UUID.randomUUID();
        Set<UUID> productIds = Set.of(productId1, productId2);

        OrderResponseDto responseDto = new OrderResponseDto(
                orderId, UUID.randomUUID(), Set.of(), 199.99, OrderStatus.PENDING);

        Mockito.when(orderService.addProductsToOrder(eq(orderId), eq(productIds)))
                .thenReturn(responseDto);

        // Act & Assert
        mockMvc.perform(post("/api/orders/{id}/products", orderId)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(productIds)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.totalPrice").value(199.99));
    }
}
