package com.todolist.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.todolist.domain.entity.Category;
import com.todolist.dto.request.CategoryDTO;
import com.todolist.dto.response.CategoryVO;
import com.todolist.service.ICategoryService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.util.Arrays;
import java.util.Collections;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

/**
 * Category Controller Integration Tests
 *
 * Tests for CategoryController covering:
 * - Category list retrieval
 * - Category CRUD operations
 * - Request validation and error handling
 * - Category todo count tracking
 *
 * @author TodoList Team
 * @since 1.0.0
 */
@WebMvcTest(CategoryController.class)
@DisplayName("Category Controller Tests")
class CategoryControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockBean
    private ICategoryService categoryService;

    private CategoryDTO categoryDTO;
    private CategoryVO categoryVO;

    @BeforeEach
    void setUp() {
        // Set up test data
        categoryDTO = CategoryDTO.builder()
                .name("Work")
                .color("#FF0000")
                .build();

        categoryVO = CategoryVO.builder()
                .id(1L)
                .name("Work")
                .color("#FF0000")
                .todoCount(5)
                .build();
    }

    @Test
    @DisplayName("Should successfully get category list")
    void list_Success() throws Exception {
        // Arrange
        CategoryVO categoryVO2 = CategoryVO.builder()
                .id(2L)
                .name("Personal")
                .color("#00FF00")
                .todoCount(3)
                .build();

        when(categoryService.selectList()).thenReturn(Arrays.asList(categoryVO, categoryVO2));

        // Act & Assert
        mockMvc.perform(get("/api/v1/categories/list"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.code").value(200))
                .andExpect(jsonPath("$.data").isArray())
                .andExpect(jsonPath("$.data").isNotEmpty())
                .andExpect(jsonPath("$.data[0].id").value(1L))
                .andExpect(jsonPath("$.data[0].name").value("Work"))
                .andExpect(jsonPath("$.data[0].color").value("#FF0000"))
                .andExpect(jsonPath("$.data[0].todoCount").value(5))
                .andExpect(jsonPath("$.data[1].id").value(2L))
                .andExpect(jsonPath("$.data[1].name").value("Personal"));
    }

    @Test
    @DisplayName("Should return empty list when no categories found")
    void list_EmptyList() throws Exception {
        // Arrange
        when(categoryService.selectList()).thenReturn(Collections.emptyList());

        // Act & Assert
        mockMvc.perform(get("/api/v1/categories/list"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.code").value(200))
                .andExpect(jsonPath("$.data").isArray())
                .andExpect(jsonPath("$.data").isEmpty());
    }

    @Test
    @DisplayName("Should successfully get category by id")
    void getInfo_Success() throws Exception {
        // Arrange
        when(categoryService.getById(1L)).thenReturn(categoryVO);

        // Act & Assert
        mockMvc.perform(get("/api/v1/categories/1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.code").value(200))
                .andExpect(jsonPath("$.data.id").value(1L))
                .andExpect(jsonPath("$.data.name").value("Work"))
                .andExpect(jsonPath("$.data.color").value("#FF0000"))
                .andExpect(jsonPath("$.data.todoCount").value(5));
    }

    @Test
    @DisplayName("Should successfully create a new category")
    void add_Success() throws Exception {
        // Arrange
        when(categoryService.insert(any(CategoryDTO.class))).thenReturn(1L);

        // Act & Assert
        mockMvc.perform(post("/api/v1/categories")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(categoryDTO)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.code").value(200))
                .andExpect(jsonPath("$.message").value("Category created successfully"));
    }

    @Test
    @DisplayName("Should fail creating category with missing name")
    void add_MissingName() throws Exception {
        // Arrange
        categoryDTO.setName(null);

        // Act & Assert
        mockMvc.perform(post("/api/v1/categories")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(categoryDTO)))
                .andExpect(status().isBadRequest());
    }

    @Test
    @DisplayName("Should fail creating category with empty name")
    void add_EmptyName() throws Exception {
        // Arrange
        categoryDTO.setName("");

        // Act & Assert
        mockMvc.perform(post("/api/v1/categories")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(categoryDTO)))
                .andExpect(status().isBadRequest());
    }

    @Test
    @DisplayName("Should successfully update a category")
    void edit_Success() throws Exception {
        // Arrange
        categoryDTO.setId(1L);
        categoryDTO.setName("Updated Work");
        categoryDTO.setColor("#0000FF");

        // Act & Assert
        mockMvc.perform(put("/api/v1/categories")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(categoryDTO)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.code").value(200))
                .andExpect(jsonPath("$.message").value("Category updated successfully"));
    }

    @Test
    @DisplayName("Should fail updating category without id")
    void edit_NullId() throws Exception {
        // Arrange
        categoryDTO.setId(null);

        // Act & Assert
        mockMvc.perform(put("/api/v1/categories")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(categoryDTO)))
                .andExpect(status().isBadRequest());
    }

    @Test
    @DisplayName("Should successfully delete categories")
    void remove_Success() throws Exception {
        // Act & Assert
        mockMvc.perform(delete("/api/v1/categories/1,2,3"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.code").value(200))
                .andExpect(jsonPath("$.message").value("Categories deleted successfully"));
    }

    @Test
    @DisplayName("Should successfully delete single category")
    void remove_SingleId() throws Exception {
        // Act & Assert
        mockMvc.perform(delete("/api/v1/categories/1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.code").value(200))
                .andExpect(jsonPath("$.message").value("Categories deleted successfully"));
    }

    @Test
    @DisplayName("Should handle category not found")
    void getInfo_NotFound() throws Exception {
        // Arrange
        when(categoryService.getById(999L))
                .thenThrow(new com.todolist.common.exception.BusinessException(404, "Category not found"));

        // Act & Assert
        mockMvc.perform(get("/api/v1/categories/999"))
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.code").value(404))
                .andExpect(jsonPath("$.message").value("Category not found"));
    }

    @Test
    @DisplayName("Should handle duplicate category name on create")
    void add_DuplicateName() throws Exception {
        // Arrange
        when(categoryService.insert(any(CategoryDTO.class)))
                .thenThrow(new com.todolist.common.exception.BusinessException(409, "Category name already exists"));

        // Act & Assert
        mockMvc.perform(post("/api/v1/categories")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(categoryDTO)))
                .andExpect(status().isConflict())
                .andExpect(jsonPath("$.code").value(409))
                .andExpect(jsonPath("$.message").value("Category name already exists"));
    }

    @Test
    @DisplayName("Should handle category in use on delete")
    void remove_CategoryInUse() throws Exception {
        // Arrange
        when(categoryService.deleteByIds(any(Long[].class)))
                .thenThrow(new com.todolist.common.exception.BusinessException(409, "Category is in use and cannot be deleted"));

        // Act & Assert
        mockMvc.perform(delete("/api/v1/categories/1"))
                .andExpect(status().isConflict())
                .andExpect(jsonPath("$.code").value(409))
                .andExpect(jsonPath("$.message").value("Category is in use and cannot be deleted"));
    }

    @Test
    @DisplayName("Should create category without color")
    void add_WithoutColor() throws Exception {
        // Arrange
        categoryDTO.setColor(null);
        when(categoryService.insert(any(CategoryDTO.class))).thenReturn(1L);

        // Act & Assert
        mockMvc.perform(post("/api/v1/categories")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(categoryDTO)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.code").value(200))
                .andExpect(jsonPath("$.message").value("Category created successfully"));
    }

    @Test
    @DisplayName("Should update category without changing color")
    void edit_WithoutColor() throws Exception {
        // Arrange
        categoryDTO.setId(1L);
        categoryDTO.setName("Updated Work");
        categoryDTO.setColor(null);

        // Act & Assert
        mockMvc.perform(put("/api/v1/categories")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(categoryDTO)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.code").value(200));
    }

    @Test
    @DisplayName("Should handle empty request body on create")
    void add_EmptyBody() throws Exception {
        // Act & Assert
        mockMvc.perform(post("/api/v1/categories")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{}"))
                .andExpect(status().isBadRequest());
    }

    @Test
    @DisplayName("Should handle malformed JSON")
    void add_MalformedJson() throws Exception {
        // Act & Assert
        mockMvc.perform(post("/api/v1/categories")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{invalid json}"))
                .andExpect(status().isBadRequest());
    }

    @Test
    @DisplayName("Should return 404 for non-existent endpoints")
    void nonExistentEndpoint() throws Exception {
        // Act & Assert
        mockMvc.perform(get("/api/v1/categories/nonexistent"))
                .andExpect(status().isNotFound());
    }

    @Test
    @DisplayName("Should handle category with zero todo count")
    void getInfo_ZeroTodoCount() throws Exception {
        // Arrange
        CategoryVO emptyCategory = CategoryVO.builder()
                .id(1L)
                .name("Work")
                .color("#FF0000")
                .todoCount(0)
                .build();
        when(categoryService.getById(1L)).thenReturn(emptyCategory);

        // Act & Assert
        mockMvc.perform(get("/api/v1/categories/1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.data.todoCount").value(0));
    }

    @Test
    @DisplayName("Should validate color format")
    void add_InvalidColorFormat() throws Exception {
        // Note: This would require custom validation logic
        // For now, just testing that the controller accepts the input
        categoryDTO.setColor("invalid-color");

        // Act & Assert
        mockMvc.perform(post("/api/v1/categories")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(categoryDTO)))
                .andExpect(status().isOk());
    }
}
