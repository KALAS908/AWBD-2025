package com.example.demo.controller;

import com.example.demo.Controller.OrderController;
import com.example.demo.Controller.ReviewController;
import com.example.demo.Models.Enums.OrderStatus;
import com.example.demo.dto.order.OrderRequestDto;
import com.example.demo.dto.order.OrderResponseDto;
import com.example.demo.dto.product.ProductResponseDto;
import com.example.demo.dto.review.ReviewRequestDto;
import com.example.demo.dto.review.ReviewResponseDto;
import com.example.demo.service.OrderService;
import com.example.demo.service.ReviewService;
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


@WebMvcTest(ReviewController.class)
class ReviewControllerTest {
    @Autowired
    private MockMvc mockMvc;

    @MockitoBean
    private ReviewService reviewService;

    @Autowired
    private ObjectMapper objectMapper;

    private UUID reviewId;
    private UUID userId;
    private UUID productId;
    private ReviewRequestDto requestDto;
    private ReviewResponseDto responseDto;

    @BeforeEach
    void setUp() {
        reviewId = UUID.randomUUID();
        userId = UUID.randomUUID();
        productId = UUID.randomUUID();

        requestDto = new ReviewRequestDto(
                userId,
                productId,
                5,
                "Great product!"
        );

        responseDto = new ReviewResponseDto(
                reviewId,
                userId,
                productId,
                5,
                "Great product!"
        );
    }

    @Test
    void createReview_ShouldReturnCreatedStatus() throws Exception {
        when(reviewService.createReview(any(ReviewRequestDto.class)))
                .thenReturn(responseDto);

        mockMvc.perform(post("/api/reviews")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(requestDto)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.rating").value(5))
                .andExpect(jsonPath("$.comment").value("Great product!"));
    }

    @Test
    void getReviewById_ShouldReturnOkStatus() throws Exception {
        when(reviewService.getReviewById(reviewId))
                .thenReturn(responseDto);

        mockMvc.perform(get("/api/reviews/{id}", reviewId))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.rating").value(5));
    }

    @Test
    void getAllReviews_ShouldReturnOkStatus() throws Exception {
        when(reviewService.getAllReviews())
                .thenReturn(Arrays.asList(responseDto));

        mockMvc.perform(get("/api/reviews"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].rating").value(5));
    }

    @Test
    void getReviewsByProduct_ShouldReturnOkStatus() throws Exception {
        when(reviewService.getReviewsByProduct(productId))
                .thenReturn(Arrays.asList(responseDto));

        mockMvc.perform(get("/api/reviews/product/{productId}", productId))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].rating").value(5));
    }

    @Test
    void deleteReview_ShouldReturnNoContentStatus() throws Exception {
        doNothing().when(reviewService).deleteReview(reviewId);

        mockMvc.perform(delete("/api/reviews/{id}", reviewId))
                .andExpect(status().isNoContent());
    }
}
