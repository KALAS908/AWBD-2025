package com.example.demo.controller;

import com.example.demo.Controller.ProductController;
import com.example.demo.dto.product.ProductRequestDto;
import com.example.demo.dto.product.ProductResponseDto;
import com.example.demo.service.ProductService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

import java.util.Arrays;
import java.util.UUID;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(ProductController.class)
@AutoConfigureMockMvc(addFilters = false)
class ProductControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockitoBean
    private ProductService productService;

    @Autowired
    private ObjectMapper objectMapper;

    @Test
    void createProduct_ShouldReturnCreated() throws Exception {
        UUID categoryId = UUID.randomUUID();
        UUID productId = UUID.randomUUID();

        ProductRequestDto requestDto = new ProductRequestDto();
        requestDto.setName("Smartphone");
        requestDto.setPrice(999.99);
        requestDto.setPhoneModel(null); // or valid enum
        requestDto.setCategoryId(categoryId);

        ProductResponseDto responseDto = new ProductResponseDto(productId, "Smartphone", 999.99, null, null, null);

        Mockito.when(productService.createProduct(any(ProductRequestDto.class))).thenReturn(responseDto);

        mockMvc.perform(post("/api/products")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(requestDto)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.name").value("Smartphone"));
    }

    @Test
    void getProductById_ShouldReturnProduct() throws Exception {
        UUID productId = UUID.randomUUID();
        ProductResponseDto responseDto = new ProductResponseDto(productId, "Smartphone", 999.99, null, null, null);

        Mockito.when(productService.getProductById(productId)).thenReturn(responseDto);

        mockMvc.perform(get("/api/products/{id}", productId))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(productId.toString()));
    }

    @Test
    void getAllProducts_ShouldReturnList() throws Exception {
        ProductResponseDto dto1 = new ProductResponseDto(UUID.randomUUID(), "Laptop", 1500.0, null, null, null);
        ProductResponseDto dto2 = new ProductResponseDto(UUID.randomUUID(), "Phone", 800.0, null, null, null);

        Mockito.when(productService.getAllProducts()).thenReturn(Arrays.asList(dto1, dto2));

        mockMvc.perform(get("/api/products"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.length()" ).value(2));
    }

    @Test
    void updateProduct_ShouldReturnUpdated() throws Exception {
        UUID productId = UUID.randomUUID();
        ProductRequestDto requestDto = new ProductRequestDto();
        requestDto.setName("Updated Phone");
        requestDto.setPrice(899.99);
        requestDto.setPhoneModel(null);
        requestDto.setCategoryId(UUID.randomUUID());

        ProductResponseDto responseDto = new ProductResponseDto(productId, "Updated Phone", 899.99, null, null, null);

        Mockito.when(productService.updateProduct(eq(productId), any(ProductRequestDto.class))).thenReturn(responseDto);

        mockMvc.perform(put("/api/products/{id}", productId)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(requestDto)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.name").value("Updated Phone"));
    }

    @Test
    void deleteProduct_ShouldReturnNoContent() throws Exception {
        UUID productId = UUID.randomUUID();

        Mockito.doNothing().when(productService).deleteProduct(productId);

        mockMvc.perform(delete("/api/products/{id}", productId))
                .andExpect(status().isNoContent());
    }
}
