package com.example.demo.service;

import com.example.demo.Models.Category;
import com.example.demo.Models.Product;
import com.example.demo.Models.Review;
import com.example.demo.dto.product.ProductRequestDto;
import com.example.demo.dto.product.ProductResponseDto;
import com.example.demo.dto.review.ReviewResponseDto;
import com.example.demo.exception.ResourceNotFoundException;
import com.example.demo.repository.CategoryRepository;
import com.example.demo.repository.ProductRepository;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
@AllArgsConstructor
public class ProductService {
    private final ProductRepository productRepository;
    private final CategoryRepository categoryRepository;

    // Create
    public ProductResponseDto createProduct(ProductRequestDto productRequestDto) {
        Category category = categoryRepository.findById(productRequestDto.getCategoryId())
                .orElseThrow(() -> new ResourceNotFoundException("Category not found"));

        Product product = new Product();
        product.setName(productRequestDto.getName());
        product.setPrice(productRequestDto.getPrice());
        product.setPhoneModel(productRequestDto.getPhoneModel());
        product.setCategory(category);

        Product savedProduct = productRepository.save(product);
        return mapToDto(savedProduct);
    }

    // Read
    public ProductResponseDto getProductById(UUID id) {
        Product product = productRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Product not found with id: " + id));
        return mapToDto(product);
    }

    public List<ProductResponseDto> getAllProducts() {
        return productRepository.findAll().stream()
                .map(this::mapToDto)
                .collect(Collectors.toList());
    }

    // Update
    public ProductResponseDto updateProduct(UUID id, ProductRequestDto productRequestDto) {
        Product product = productRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Product not found with id: " + id));

        if (productRequestDto.getName() != null) product.setName(productRequestDto.getName());
        if (productRequestDto.getPrice() != null) product.setPrice(productRequestDto.getPrice());
        if (productRequestDto.getPhoneModel() != null) product.setPhoneModel(productRequestDto.getPhoneModel());
        if (productRequestDto.getCategoryId() != null) {
            Category category = categoryRepository.findById(productRequestDto.getCategoryId())
                    .orElseThrow(() -> new ResourceNotFoundException("Category not found"));
            product.setCategory(category);
        }

        Product updatedProduct = productRepository.save(product);
        return mapToDto(updatedProduct);
    }

    // Delete
    public void deleteProduct(UUID id) {
        if (!productRepository.existsById(id)) {
            throw new ResourceNotFoundException("Product not found with id: " + id);
        }
        productRepository.deleteById(id);
    }

    // Helper method to map Product to ProductResponseDto
    private ProductResponseDto mapToDto(Product product) {
        return new ProductResponseDto(
                product.getId(),
                product.getName(),
                product.getPrice(),
                product.getPhoneModel(),
                product.getCategory(),
                product.getReviews().stream()
                        .map(this::mapReviewToDto)
                        .collect(Collectors.toSet())
        );
    }

    private ReviewResponseDto mapReviewToDto(Review review) {
        return new ReviewResponseDto(
                review.getId(),
                review.getUser().getId(),
                review.getProduct().getId(),
                review.getRating(),
                review.getComment()
        );
    }


    public List<ProductResponseDto> getProductsByCategory(UUID categoryId) {
        categoryRepository.findById(categoryId)
                .orElseThrow(() -> new ResourceNotFoundException("Category not found"));

        return productRepository.findByCategoryId(categoryId).stream()
                .map(this::mapToDto)
                .collect(Collectors.toList());
    }
}
