package com.example.demo.controller;


import com.example.demo.Controller.CategoryController;
import com.example.demo.dto.category.CategoryRequestDto;
import com.example.demo.dto.category.CategoryResponseDto;
import com.example.demo.service.CategoryService;
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


@WebMvcTest(CategoryController.class)
class CategoryControllerTest {
    @Autowired
    private MockMvc mockMvc;

    @MockitoBean
    private CategoryService categoryService;

    @Autowired
    private ObjectMapper objectMapper;

    private UUID categoryId;
    private CategoryRequestDto requestDto;
    private CategoryResponseDto responseDto;

    @BeforeEach
    void setUp() {
        categoryId = UUID.randomUUID();

        requestDto = new CategoryRequestDto(
                "Smartphones",
                "All types of smartphones"
        );

        responseDto = new CategoryResponseDto(
                categoryId,
                "Smartphones",
                "All types of smartphones"
        );
    }

    @Test
    void createCategory_ShouldReturnCreatedStatus() throws Exception {
        when(categoryService.createCategory(any(CategoryRequestDto.class)))
                .thenReturn(responseDto);

        mockMvc.perform(post("/api/categories")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(requestDto)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.name").value("Smartphones"))
                .andExpect(jsonPath("$.description").value("All types of smartphones"));
    }

    @Test
    void getCategoryById_ShouldReturnOkStatus() throws Exception {
        when(categoryService.getCategoryById(categoryId))
                .thenReturn(responseDto);

        mockMvc.perform(get("/api/categories/{id}", categoryId))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.name").value("Smartphones"));
    }

    @Test
    void getAllCategories_ShouldReturnOkStatus() throws Exception {
        when(categoryService.getAllCategories())
                .thenReturn(Arrays.asList(responseDto));

        mockMvc.perform(get("/api/categories"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].name").value("Smartphones"));
    }

    @Test
    void updateCategory_ShouldReturnOkStatus() throws Exception {
        when(categoryService.updateCategory(eq(categoryId), any(CategoryRequestDto.class)))
                .thenReturn(responseDto);

        mockMvc.perform(put("/api/categories/{id}", categoryId)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(requestDto)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.name").value("Smartphones"));
    }

    @Test
    void deleteCategory_ShouldReturnNoContentStatus() throws Exception {
        doNothing().when(categoryService).deleteCategory(categoryId);

        mockMvc.perform(delete("/api/categories/{id}", categoryId))
                .andExpect(status().isNoContent());
    }
}



