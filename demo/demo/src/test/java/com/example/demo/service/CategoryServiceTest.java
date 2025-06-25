package com.example.demo.service;

import com.example.demo.Models.Category;
import com.example.demo.dto.category.CategoryRequestDto;
import com.example.demo.dto.category.CategoryResponseDto;
import com.example.demo.exception.ResourceNotFoundException;
import com.example.demo.repository.CategoryRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.*;
import java.util.*;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class CategoryServiceTest {

    @Mock
    private CategoryRepository categoryRepository;

    @InjectMocks
    private CategoryService categoryService;

    @Captor
    private ArgumentCaptor<Category> categoryCaptor;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void updateCategory_ShouldReturnUpdatedCategory() {
        UUID categoryId = UUID.fromString("3d75de30-102d-4e18-bcff-83f7b1c2b494");
        CategoryRequestDto requestDto = new CategoryRequestDto("Smartphones", "Mobile devices");
        Category existingCategory = new Category();
        existingCategory.setId(categoryId);
        existingCategory.setName("Old Name");
        existingCategory.setDescription("Old Description");

        when(categoryRepository.findById(categoryId)).thenReturn(Optional.of(existingCategory));
        when(categoryRepository.save(any(Category.class))).thenAnswer(invocation -> invocation.getArgument(0));

        CategoryResponseDto result = categoryService.updateCategory(categoryId, requestDto);

        assertEquals("Smartphones", result.getName());
        assertEquals("Mobile devices", result.getDescription());
        verify(categoryRepository, times(1)).save(categoryCaptor.capture());
        Category savedCategory = categoryCaptor.getValue();
        assertEquals("Smartphones", savedCategory.getName());
        assertEquals("Mobile devices", savedCategory.getDescription());
    }

    @Test
    void deleteCategory_ShouldCallRepositorySave() {
        UUID categoryId = UUID.fromString("3d75de30-102d-4e18-bcff-83f7b1c2b494");
        Category category = new Category();
        category.setId(categoryId);
        category.setDeleted(false);

        when(categoryRepository.existsById(categoryId)).thenReturn(true);
        when(categoryRepository.findById(categoryId)).thenReturn(Optional.of(category));
        when(categoryRepository.save(any(Category.class))).thenReturn(category);

        categoryService.deleteCategory(categoryId);

        verify(categoryRepository, times(1)).save(categoryCaptor.capture());
        Category savedCategory = categoryCaptor.getValue();
        assertTrue(savedCategory.isDeleted());
    }

    @Test
    void getAllCategories_ShouldReturnList() {
        Category category1 = new Category();
        category1.setId(UUID.fromString("3d75de30-102d-4e18-bcff-83f7b1c2b494"));
        category1.setName("Laptops");
        category1.setDescription("Computers");
        category1.setDeleted(false);

        Category category2 = new Category();
        category2.setId(UUID.fromString("569986a8-2560-4610-a420-baef380b2b88"));
        category2.setName("Phones");
        category2.setDescription("Smartphones");
        category2.setDeleted(false);

        when(categoryRepository.findAll()).thenReturn(Arrays.asList(category1, category2));

        List<CategoryResponseDto> result = categoryService.getAllCategories();

        assertEquals(2, result.size());
        assertEquals("Laptops", result.get(0).getName());
        assertEquals("Phones", result.get(1).getName());
        verify(categoryRepository, times(1)).findAll();
    }
}
