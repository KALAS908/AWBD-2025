package com.example.demo.service;

import com.example.demo.Models.Product;
import com.example.demo.Models.Review;
import com.example.demo.Models.User;
import com.example.demo.dto.review.ReviewRequestDto;
import com.example.demo.dto.review.ReviewResponseDto;
import com.example.demo.exception.ResourceNotFoundException;
import com.example.demo.repository.ProductRepository;
import com.example.demo.repository.ReviewRepository;
import com.example.demo.repository.UserRepository;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
@AllArgsConstructor
public class ReviewService {
    private final ReviewRepository reviewRepository;
    private final UserRepository userRepository;
    private final ProductRepository productRepository;

    // Create
    public ReviewResponseDto createReview(ReviewRequestDto reviewRequestDto) {
        User user = userRepository.findById(reviewRequestDto.getUserId())
                .orElseThrow(() -> new ResourceNotFoundException("User not found"));

        Product product = productRepository.findById(reviewRequestDto.getProductId())
                .orElseThrow(() -> new ResourceNotFoundException("Product not found"));

        Review review = new Review();
        review.setUser(user);
        review.setProduct(product);
        review.setRating(reviewRequestDto.getRating());
        review.setComment(reviewRequestDto.getComment());

        Review savedReview = reviewRepository.save(review);
        return mapToDto(savedReview);
    }

    // Read
    public ReviewResponseDto getReviewById(UUID id) {
        Review review = reviewRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Review not found"));
        return mapToDto(review);
    }

    public List<ReviewResponseDto> getAllReviews() {
        return reviewRepository.findAll().stream()
                .map(this::mapToDto)
                .collect(Collectors.toList());
    }

    public List<ReviewResponseDto> getReviewsByProduct(UUID productId) {
        return reviewRepository.findByProductId(productId).stream()
                .map(this::mapToDto)
                .collect(Collectors.toList());
    }

    public List<ReviewResponseDto> getReviewsByUser(UUID userId) {
        return reviewRepository.findByUserId(userId).stream()
                .map(this::mapToDto)
                .collect(Collectors.toList());
    }

    // Update
    public ReviewResponseDto updateReview(UUID id, ReviewRequestDto reviewRequestDto) {
        Review review = reviewRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Review not found"));

        if (reviewRequestDto.getRating() != null) {
            review.setRating(reviewRequestDto.getRating());
        }
        if (reviewRequestDto.getComment() != null) {
            review.setComment(reviewRequestDto.getComment());
        }

        Review updatedReview = reviewRepository.save(review);
        return mapToDto(updatedReview);
    }

    // Delete
    public void deleteReview(UUID id) {
        if (!reviewRepository.existsById(id)) {
            throw new ResourceNotFoundException("Review not found");
        }
        reviewRepository.deleteById(id);
    }

    // Helper method
    private ReviewResponseDto mapToDto(Review review) {
        return new ReviewResponseDto(
                review.getId(),
                review.getUser().getId(),
                review.getProduct().getId(),
                review.getRating(),
                review.getComment()
        );
    }
}
