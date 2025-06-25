package com.example.demo.service;

import com.example.demo.Models.Category;
import com.example.demo.Models.Product;
import com.example.demo.dto.category.CategoryRequestDto;
import com.example.demo.dto.category.CategoryResponseDto;
import com.example.demo.dto.product.ProductResponseDto;
import com.example.demo.exception.ResourceNotFoundException;
import com.example.demo.repository.CategoryRepository;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
@AllArgsConstructor
public class CategoryService {
    private final CategoryRepository categoryRepository;

    // Create
    public CategoryResponseDto createCategory(CategoryRequestDto categoryRequestDto) {
        Category category = new Category();
        category.setName(categoryRequestDto.getName());
        category.setDescription(categoryRequestDto.getDescription());

        Category savedCategory = categoryRepository.save(category);
        return mapToDto(savedCategory);
    }

    // Read
    public CategoryResponseDto getCategoryById(UUID id) {
        Category category = categoryRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Category not found with id: " + id));
        return mapToDto(category);
    }

    public List<CategoryResponseDto> getAllCategories() {
        return categoryRepository.findAll().stream()
                .filter(category -> !category.isDeleted()) // Filter out soft-deleted categories
                .map(this::mapToDto)
                .collect(Collectors.toList());
    }

    // Update
    public CategoryResponseDto updateCategory(UUID id, CategoryRequestDto categoryRequestDto) {
        Category category = categoryRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Category not found with id: " + id));

        if (categoryRequestDto.getName() != null) category.setName(categoryRequestDto.getName());
        if (categoryRequestDto.getDescription() != null) category.setDescription(categoryRequestDto.getDescription());

        Category updatedCategory = categoryRepository.save(category);
        return mapToDto(updatedCategory);
    }

    // Delete
    public void deleteCategory(UUID id) {
        if (!categoryRepository.existsById(id)) {
            throw new ResourceNotFoundException("Category not found with id: " + id);
        }
        //categoryRepository.deleteById(id);

        Category category = categoryRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Category not found with id: " + id));
        category.setDeleted(true); // Soft delete
        categoryRepository.save(category);

    }

    // Helper method to map Category to CategoryResponseDto
    private CategoryResponseDto mapToDto(Category category) {
        return new CategoryResponseDto(
                category.getId(),
                category.getName(),
                category.getDescription()
        );
    }


    public List<ProductResponseDto> getProductsByCategory(UUID categoryId) {
        Category category = categoryRepository.findById(categoryId)
                .orElseThrow(() -> new ResourceNotFoundException("Category not found with id: " + categoryId));

        return category.getProducts().stream()
                .map(this::mapToProductDto)
                .collect(Collectors.toList());
    }

    private ProductResponseDto mapToProductDto(Product product) {
        return new ProductResponseDto(
                product.getId(),
                product.getName(),
                product.getPrice(),
                product.getPhoneModel(),
                null , // Don't include category to avoid recursion,
                null
        );
    }
}

