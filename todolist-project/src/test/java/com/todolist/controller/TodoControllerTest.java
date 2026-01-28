package com.todolist.controller;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.todolist.dto.request.TodoDTO;
import com.todolist.dto.response.TodoVO;
import com.todolist.service.ITodoService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.Collections;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

/**
 * Todo Controller Integration Tests
 *
 * Tests for TodoController covering:
 * - Todo list retrieval with pagination
 * - Todo CRUD operations
 * - Status toggle functionality
 * - Request validation and error handling
 *
 * @author TodoList Team
 * @since 1.0.0
 */
@WebMvcTest(TodoController.class)
@DisplayName("Todo Controller Tests")
class TodoControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockBean
    private ITodoService todoService;

    private TodoDTO todoDTO;
    private TodoVO todoVO;
    private IPage<TodoVO> todoPage;

    @BeforeEach
    void setUp() {
        // Set up test data
        todoDTO = TodoDTO.builder()
                .title("Test Todo")
                .description("Test Description")
                .priority("HIGH")
                .dueDate(LocalDateTime.now().plusDays(7))
                .categoryIds(Arrays.asList(1L, 2L))
                .build();

        todoVO = TodoVO.builder()
                .id(1L)
                .title("Test Todo")
                .description("Test Description")
                .status("PENDING")
                .priority("HIGH")
                .dueDate(LocalDateTime.now().plusDays(7))
                .categories(Collections.emptyList())
                .build();

        Page<TodoVO> page = new Page<>(1, 20);
        page.setRecords(Arrays.asList(todoVO));
        page.setTotal(1);
        todoPage = page;
    }

    @Test
    @DisplayName("Should successfully get todo list")
    void list_Success() throws Exception {
        // Arrange
        when(todoService.selectList(any())).thenReturn(todoPage);

        // Act & Assert
        mockMvc.perform(get("/api/v1/todos/list")
                        .param("page", "1")
                        .param("size", "20"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.code").value(200))
                .andExpect(jsonPath("$.rows").isArray())
                .andExpect(jsonPath("$.rows").isNotEmpty())
                .andExpect(jsonPath("$.rows[0].id").value(1L))
                .andExpect(jsonPath("$.rows[0].title").value("Test Todo"))
                .andExpect(jsonPath("$.total").value(1));
    }

    @Test
    @DisplayName("Should successfully get todo by id")
    void getInfo_Success() throws Exception {
        // Arrange
        when(todoService.getById(1L)).thenReturn(todoVO);

        // Act & Assert
        mockMvc.perform(get("/api/v1/todos/1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.code").value(200))
                .andExpect(jsonPath("$.data.id").value(1L))
                .andExpect(jsonPath("$.data.title").value("Test Todo"))
                .andExpect(jsonPath("$.data.description").value("Test Description"))
                .andExpect(jsonPath("$.data.status").value("PENDING"))
                .andExpect(jsonPath("$.data.priority").value("HIGH"));
    }

    @Test
    @DisplayName("Should successfully create a new todo")
    void add_Success() throws Exception {
        // Arrange
        when(todoService.insert(any(TodoDTO.class))).thenReturn(1L);

        // Act & Assert
        mockMvc.perform(post("/api/v1/todos")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(todoDTO)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.code").value(200))
                .andExpect(jsonPath("$.message").value("Todo created successfully"));
    }

    @Test
    @DisplayName("Should fail creating todo with missing title")
    void add_MissingTitle() throws Exception {
        // Arrange
        todoDTO.setTitle(null);

        // Act & Assert
        mockMvc.perform(post("/api/v1/todos")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(todoDTO)))
                .andExpect(status().isBadRequest());
    }

    @Test
    @DisplayName("Should fail creating todo with empty title")
    void add_EmptyTitle() throws Exception {
        // Arrange
        todoDTO.setTitle("");

        // Act & Assert
        mockMvc.perform(post("/api/v1/todos")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(todoDTO)))
                .andExpect(status().isBadRequest());
    }

    @Test
    @DisplayName("Should successfully update a todo")
    void edit_Success() throws Exception {
        // Arrange
        todoDTO.setId(1L);
        todoDTO.setTitle("Updated Todo");

        // Act & Assert
        mockMvc.perform(put("/api/v1/todos")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(todoDTO)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.code").value(200))
                .andExpect(jsonPath("$.message").value("Todo updated successfully"));
    }

    @Test
    @DisplayName("Should successfully delete todos")
    void remove_Success() throws Exception {
        // Act & Assert
        mockMvc.perform(delete("/api/v1/todos/1,2,3"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.code").value(200))
                .andExpect(jsonPath("$.message").value("Todos deleted successfully"));
    }

    @Test
    @DisplayName("Should successfully toggle todo status")
    void toggle_Success() throws Exception {
        // Arrange
        TodoVO toggledTodo = TodoVO.builder()
                .id(1L)
                .title("Test Todo")
                .description("Test Description")
                .status("DONE")
                .priority("HIGH")
                .categories(Collections.emptyList())
                .build();

        when(todoService.toggleStatus(1L)).thenReturn(toggledTodo);

        // Act & Assert
        mockMvc.perform(patch("/api/v1/todos/1/toggle"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.code").value(200))
                .andExpect(jsonPath("$.message").value("Status toggled successfully"))
                .andExpect(jsonPath("$.data.status").value("DONE"));
    }

    @Test
    @DisplayName("Should handle todo list with query parameters")
    void list_WithQueryParameters() throws Exception {
        // Arrange
        when(todoService.selectList(any())).thenReturn(todoPage);

        // Act & Assert
        mockMvc.perform(get("/api/v1/todos/list")
                        .param("page", "1")
                        .param("size", "10")
                        .param("status", "PENDING")
                        .param("priority", "HIGH")
                        .param("sortBy", "createdAt")
                        .param("sortOrder", "desc"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.code").value(200))
                .andExpect(jsonPath("$.rows").isArray());
    }

    @Test
    @DisplayName("Should return empty list when no todos found")
    void list_EmptyList() throws Exception {
        // Arrange
        Page<TodoVO> emptyPage = new Page<>(1, 20);
        emptyPage.setRecords(Collections.emptyList());
        emptyPage.setTotal(0);
        when(todoService.selectList(any())).thenReturn(emptyPage);

        // Act & Assert
        mockMvc.perform(get("/api/v1/todos/list"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.code").value(200))
                .andExpect(jsonPath("$.rows").isArray())
                .andExpect(jsonPath("$.rows").isEmpty())
                .andExpect(jsonPath("$.total").value(0));
    }

    @Test
    @DisplayName("Should handle invalid todo id")
    void getInfo_InvalidId() throws Exception {
        // Arrange
        when(todoService.getById(999L))
                .thenThrow(new com.todolist.common.exception.BusinessException(404, "Todo not found"));

        // Act & Assert
        mockMvc.perform(get("/api/v1/todos/999"))
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.code").value(404))
                .andExpect(jsonPath("$.message").value("Todo not found"));
    }

    @Test
    @DisplayName("Should handle delete with single id")
    void remove_SingleId() throws Exception {
        // Act & Assert
        mockMvc.perform(delete("/api/v1/todos/1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.code").value(200))
                .andExpect(jsonPath("$.message").value("Todos deleted successfully"));
    }

    @Test
    @DisplayName("Should handle update without description")
    void edit_WithoutDescription() throws Exception {
        // Arrange
        todoDTO.setId(1L);
        todoDTO.setTitle("Updated Title");
        todoDTO.setDescription(null);

        // Act & Assert
        mockMvc.perform(put("/api/v1/todos")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(todoDTO)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.code").value(200));
    }

    @Test
    @DisplayName("Should handle create without categories")
    void add_WithoutCategories() throws Exception {
        // Arrange
        todoDTO.setCategoryIds(null);
        when(todoService.insert(any(TodoDTO.class))).thenReturn(1L);

        // Act & Assert
        mockMvc.perform(post("/api/v1/todos")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(todoDTO)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.code").value(200));
    }

    @Test
    @DisplayName("Should handle create with empty category list")
    void add_EmptyCategoryList() throws Exception {
        // Arrange
        todoDTO.setCategoryIds(Collections.emptyList());
        when(todoService.insert(any(TodoDTO.class))).thenReturn(1L);

        // Act & Assert
        mockMvc.perform(post("/api/v1/todos")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(todoDTO)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.code").value(200));
    }

    @Test
    @DisplayName("Should return 404 for invalid endpoints")
    void nonExistentEndpoint() throws Exception {
        // Act & Assert
        mockMvc.perform(get("/api/v1/todos/nonexistent"))
                .andExpect(status().isNotFound());
    }
}
