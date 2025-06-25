package com.example.demo;

import com.example.demo.Models.Category;
import com.example.demo.repository.CategoryRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.TestPropertySource;

import static org.junit.jupiter.api.Assertions.assertNotNull;

@SpringBootTest
@TestPropertySource(locations = "classpath:application-test.properties")
public class CategoryRepositoryTest {
    @Autowired
    private CategoryRepository categoryRepository;

    @Test
    void testSaveCategory() {
        Category category = new Category();
        category.setName("Test");
        categoryRepository.save(category);
        assertNotNull(category.getId());
    }
}
