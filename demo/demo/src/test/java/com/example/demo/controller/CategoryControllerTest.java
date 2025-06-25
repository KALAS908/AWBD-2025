package com.example.demo.controller;
import com.example.demo.Controller.CategoryController;
import com.example.demo.dto.category.CategoryRequestDto;
import com.example.demo.dto.category.CategoryResponseDto;
import com.example.demo.service.CategoryService;
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

@WebMvcTest(CategoryController.class)
@AutoConfigureMockMvc(addFilters = false)
class CategoryControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockitoBean
    private CategoryService categoryService;

    @Autowired
    private ObjectMapper objectMapper;

    @Test
    void updateCategory_ShouldReturnOkStatus() throws Exception {
        UUID categoryId = UUID.randomUUID();
        CategoryRequestDto requestDto = new CategoryRequestDto("Smartphones", "Mobile devices");
        CategoryResponseDto responseDto = new CategoryResponseDto(categoryId, "Smartphones", "Mobile devices");

        Mockito.when(categoryService.updateCategory(eq(categoryId), any(CategoryRequestDto.class)))
                .thenReturn(responseDto);

        mockMvc.perform(put("/api/categories/{id}", categoryId)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(requestDto)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.name").value("Smartphones"));
    }

    @Test
    void deleteCategory_ShouldReturnNoContentStatus() throws Exception {
        UUID categoryId = UUID.randomUUID();

        Mockito.doNothing().when(categoryService).deleteCategory(categoryId);

        mockMvc.perform(delete("/api/categories/{id}", categoryId))
                .andExpect(status().isNoContent());
    }

    @Test
    void getAllCategories_ShouldReturnList() throws Exception {
        CategoryResponseDto dto1 = new CategoryResponseDto(UUID.randomUUID(), "Laptops", "Computers");
        CategoryResponseDto dto2 = new CategoryResponseDto(UUID.randomUUID(), "Phones", "Smartphones");

        Mockito.when(categoryService.getAllCategories())
                .thenReturn(Arrays.asList(dto1, dto2));

        mockMvc.perform(get("/api/categories"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.length()").value(2))
                .andExpect(jsonPath("$[0].name").value("Laptops"))
                .andExpect(jsonPath("$[1].name").value("Phones"));
    }
}
