package com.example.demo.service;

import com.example.demo.Models.Category;
import com.example.demo.Models.Product;
import com.example.demo.dto.product.ProductRequestDto;
import com.example.demo.dto.product.ProductResponseDto;
import com.example.demo.exception.ResourceNotFoundException;
import com.example.demo.repository.CategoryRepository;
import com.example.demo.repository.ProductRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.*;

import java.util.*;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class ProductServiceTest {

    @Mock
    private ProductRepository productRepository;

    @Mock
    private CategoryRepository categoryRepository;

    @InjectMocks
    private ProductService productService;

    private Category category;
    private Product product;
    private UUID categoryId;
    private UUID productId;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        categoryId = UUID.randomUUID();
        productId = UUID.randomUUID();
        category = new Category();
        category.setId(categoryId);
        category.setName("Electronics");
        category.setDescription("Electronic devices");

        product = new Product();
        product.setId(productId);
        product.setName("Smartphone");
        product.setPrice(999.99);
        product.setPhoneModel(null); // set a valid enum if needed
        product.setCategory(category);
        product.setDeleted(false);
    }

    @Test
    void createProduct_ShouldReturnCreatedProduct() {
        ProductRequestDto requestDto = new ProductRequestDto();
        requestDto.setName("Smartphone");
        requestDto.setPrice(999.99);
        requestDto.setPhoneModel(null);
        requestDto.setCategoryId(categoryId);

        when(categoryRepository.findById(categoryId)).thenReturn(Optional.of(category));
        when(productRepository.save(any(Product.class))).thenReturn(product);

        ProductResponseDto responseDto = productService.createProduct(requestDto);

        assertEquals(product.getName(), responseDto.getName());
        assertEquals(product.getPrice(), responseDto.getPrice());
        verify(productRepository, times(1)).save(any(Product.class));
    }

    @Test
    void getProductById_ShouldReturnProduct() {
        when(productRepository.findById(productId)).thenReturn(Optional.of(product));

        ProductResponseDto responseDto = productService.getProductById(productId);

        assertEquals(product.getName(), responseDto.getName());
        verify(productRepository, times(1)).findById(productId);
    }

    @Test
    void getProductById_ShouldThrow_WhenNotFound() {
        when(productRepository.findById(productId)).thenReturn(Optional.empty());

        assertThrows(ResourceNotFoundException.class, () -> productService.getProductById(productId));
    }

    @Test
    void getAllProducts_ShouldReturnList() {
        when(productRepository.findAll()).thenReturn(Collections.singletonList(product));

        List<ProductResponseDto> products = productService.getAllProducts();

        assertEquals(1, products.size());
        verify(productRepository, times(1)).findAll();
    }

    @Test
    void updateProduct_ShouldReturnUpdatedProduct() {
        ProductRequestDto requestDto = new ProductRequestDto();
        requestDto.setName("Updated Smartphone");
        requestDto.setPrice(1099.99);
        requestDto.setPhoneModel(null);
        requestDto.setCategoryId(categoryId);

        when(productRepository.findById(productId)).thenReturn(Optional.of(product));
        when(categoryRepository.findById(categoryId)).thenReturn(Optional.of(category));
        when(productRepository.save(any(Product.class))).thenReturn(product);

        ProductResponseDto responseDto = productService.updateProduct(productId, requestDto);

        assertEquals("Updated Smartphone", responseDto.getName());
        assertEquals(1099.99, responseDto.getPrice());
        verify(productRepository, times(1)).save(any(Product.class));
    }

    @Test
    void deleteProduct_ShouldSoftDelete() {
        when(productRepository.existsById(productId)).thenReturn(true);
        when(productRepository.findById(productId)).thenReturn(Optional.of(product));
        when(productRepository.save(any(Product.class))).thenReturn(product);

        productService.deleteProduct(productId);

        assertTrue(product.isDeleted());
        verify(productRepository, times(1)).save(product);
    }

    @Test
    void getProductsByCategory_ShouldReturnList() {
        when(categoryRepository.findById(categoryId)).thenReturn(Optional.of(category));
        when(productRepository.findByCategoryId(categoryId)).thenReturn(Collections.singletonList(product));

        List<ProductResponseDto> products = productService.getProductsByCategory(categoryId);

        assertEquals(1, products.size());
        verify(productRepository, times(1)).findByCategoryId(categoryId);
    }
}
