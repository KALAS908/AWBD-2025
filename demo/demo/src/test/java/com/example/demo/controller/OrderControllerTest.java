package com.example.demo.controller;

import com.example.demo.Controller.OrderController;
import com.example.demo.Models.Enums.OrderStatus;
import com.example.demo.dto.order.OrderRequestDto;
import com.example.demo.dto.order.OrderResponseDto;
import com.example.demo.dto.product.ProductResponseDto;
import com.example.demo.service.OrderService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;
import com.fasterxml.jackson.databind.ObjectMapper;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;
import java.util.*;


@WebMvcTest(OrderController.class)
class OrderControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockitoBean
    private OrderService orderService;

    @Autowired
    private ObjectMapper objectMapper;

    private OrderRequestDto requestDto;
    private OrderResponseDto responseDto;
    private UUID orderId;
    private UUID userId;
    private UUID productId;

    @BeforeEach
    void setUp() {
        orderId = UUID.randomUUID();
        userId = UUID.randomUUID();
        productId = UUID.randomUUID();

        requestDto = new OrderRequestDto(
                userId,
                Set.of(productId),
                OrderStatus.PENDING
        );

        responseDto = new OrderResponseDto(
                orderId,
                userId,
                Set.of(new ProductResponseDto()),
                999.99,
                OrderStatus.PENDING
        );
    }

    @Test
    void createOrder_ShouldReturnCreatedStatus() throws Exception {
        when(orderService.createOrder(any(OrderRequestDto.class)))
                .thenReturn(responseDto);

        mockMvc.perform(post("/api/orders")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(requestDto)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.totalPrice").value(999.99))
                .andExpect(jsonPath("$.status").value("PENDING"));
    }

    @Test
    void createOrder_WithInvalidData_ShouldReturnBadRequest() throws Exception {
        OrderRequestDto invalidDto = new OrderRequestDto(
                null,
                null,
                null
        );

        mockMvc.perform(post("/api/orders")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(invalidDto)))
                .andExpect(status().isBadRequest());
    }

    @Test
    void updateOrder_ShouldReturnOkStatus() throws Exception {
        when(orderService.updateOrder(any(UUID.class), any(OrderRequestDto.class)))
                .thenReturn(responseDto);

        mockMvc.perform(put("/api/orders/" + orderId)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(requestDto)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.status").value("PENDING"));

        verify(orderService).updateOrder(eq(orderId), any(OrderRequestDto.class));
    }

    @Test
    void getOrderById_ShouldReturnOkStatus() throws Exception {
        when(orderService.getOrderById(orderId))
                .thenReturn(responseDto);

        mockMvc.perform(get("/api/orders/" + orderId))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.totalPrice").value(999.99))
                .andExpect(jsonPath("$.status").value("PENDING"));

        verify(orderService).getOrderById(orderId);
    }

    @Test
    void getAllOrders_ShouldReturnOkStatus() throws Exception {
        when(orderService.getAllOrders())
                .thenReturn(Arrays.asList(responseDto));

        mockMvc.perform(get("/api/orders"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].totalPrice").value(999.99))
                .andExpect(jsonPath("$[0].status").value("PENDING"));

        verify(orderService).getAllOrders();
    }

    @Test
    void deleteOrder_ShouldReturnNoContentStatus() throws Exception {
        doNothing().when(orderService).deleteOrder(orderId);

        mockMvc.perform(delete("/api/orders/" + orderId))
                .andExpect(status().isNoContent());

        verify(orderService).deleteOrder(orderId);
    }

    @Test
    void addProductsToOrder_ShouldReturnOkStatus() throws Exception {
        Set<UUID> productIds = Set.of(UUID.randomUUID());
        when(orderService.addProductsToOrder(orderId, productIds))
                .thenReturn(responseDto);

        mockMvc.perform(post("/api/orders/" + orderId + "/products")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(productIds)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.totalPrice").value(999.99));

        verify(orderService).addProductsToOrder(eq(orderId), any());
    }

    @Test
    void createOrder_WithNullRequiredFields_ShouldReturnBadRequest() throws Exception {
        OrderRequestDto invalidDto = new OrderRequestDto(
                null,
                null,
                null
        );

        mockMvc.perform(post("/api/orders")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(invalidDto)))
                .andExpect(status().isBadRequest());
    }
}
