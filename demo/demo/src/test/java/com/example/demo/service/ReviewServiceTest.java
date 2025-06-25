package com.example.demo.service;

import com.example.demo.Models.Product;
import com.example.demo.Models.Review;
import com.example.demo.Models.User;
import com.example.demo.dto.review.ReviewRequestDto;
import com.example.demo.dto.review.ReviewResponseDto;
import com.example.demo.repository.ProductRepository;
import com.example.demo.repository.ReviewRepository;
import com.example.demo.repository.UserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class ReviewServiceTest {
    @Mock
    private ReviewRepository reviewRepository;

    @Mock
    private UserRepository userRepository;

    @Mock
    private ProductRepository productRepository;

    @InjectMocks
    private ReviewService reviewService;

    private UUID reviewId;
    private UUID userId;
    private UUID productId;
    private User user;
    private Product product;
    private Review review;
    private ReviewRequestDto requestDto;

    @BeforeEach
    void setUp() {
        reviewId = UUID.randomUUID();
        userId = UUID.randomUUID();
        productId = UUID.randomUUID();

        user = new User();
        user.setId(userId);

        product = new Product();
        product.setId(productId);

        review = new Review();
        review.setId(reviewId);
        review.setUser(user);
        review.setProduct(product);
        review.setRating(5);
        review.setComment("Great product!");

        requestDto = new ReviewRequestDto(userId, productId, 5, "Great product!");
    }


    @Test
    void getReviewsByProduct_ShouldReturnFilteredList() {
        List<Review> reviews = Arrays.asList(review);
        when(productRepository.findById(productId)).thenReturn(Optional.of(product));
        when(reviewRepository.findByProductId(productId)).thenReturn(reviews);

        List<ReviewResponseDto> results = reviewService.getReviewsByProduct(productId);

        assertEquals(1, results.size());
        assertEquals(5, results.get(0).getRating());
    }


}

