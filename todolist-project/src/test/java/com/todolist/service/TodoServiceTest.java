package com.todolist.service;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.todolist.common.exception.BusinessException;
import com.todolist.domain.entity.Category;
import com.todolist.domain.entity.Todo;
import com.todolist.domain.entity.TodoCategory;
import com.todolist.domain.enums.TodoPriority;
import com.todolist.domain.enums.TodoStatus;
import com.todolist.dto.request.TodoDTO;
import com.todolist.dto.response.CategoryVO;
import com.todolist.dto.response.TodoVO;
import com.todolist.mapper.CategoryMapper;
import com.todolist.mapper.TodoCategoryMapper;
import com.todolist.mapper.TodoMapper;
import com.todolist.query.TodoQuery;
import com.todolist.security.SecurityUtils;
import com.todolist.service.impl.TodoServiceImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockedStatic;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;

import static org.assertj.core.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.*;

/**
 * Todo Service Unit Tests
 *
 * Tests for TodoServiceImpl covering:
 * - Todo CRUD operations
 * - Category associations
 * - Status toggle functionality
 * - Data isolation and authorization
 * - Error handling and edge cases
 *
 * @author TodoList Team
 * @since 1.0.0
 */
@ExtendWith(MockitoExtension.class)
@DisplayName("Todo Service Tests")
class TodoServiceTest {

    @Mock
    private TodoMapper todoMapper;

    @Mock
    private CategoryMapper categoryMapper;

    @Mock
    private TodoCategoryMapper todoCategoryMapper;

    @InjectMocks
    private TodoServiceImpl todoService;

    private TodoDTO todoDTO;
    private Todo testTodo;
    private TodoQuery query;
    private Category testCategory;

    @BeforeEach
    void setUp() {
        // Arrange: Set up test data
        todoDTO = TodoDTO.builder()
                .title("Test Todo")
                .description("Test Description")
                .priority(TodoPriority.HIGH.getCode())
                .dueDate(LocalDateTime.now().plusDays(7))
                .categoryIds(Arrays.asList(1L, 2L))
                .build();

        testTodo = Todo.builder()
                .id(1L)
                .userId(1L)
                .title("Test Todo")
                .description("Test Description")
                .status(TodoStatus.PENDING.getCode())
                .priority(TodoPriority.HIGH.getCode())
                .dueDate(LocalDateTime.now().plusDays(7))
                .version(0)
                .build();

        query = new TodoQuery();
        query.setPage(1);
        query.setSize(20);
        query.setStatus(TodoStatus.PENDING.getCode());

        testCategory = Category.builder()
                .id(1L)
                .userId(1L)
                .name("Work")
                .color("#FF0000")
                .build();
    }

    @Test
    @DisplayName("Should successfully get paginated todo list")
    void selectList_Success() {
        // Arrange
        try (MockedStatic<SecurityUtils> securityUtilsMock = mockStatic(SecurityUtils.class)) {
            securityUtilsMock.when(SecurityUtils::getCurrentUserId).thenReturn(1L);

            Page<Todo> page = new Page<>(1, 20);
            page.setRecords(Arrays.asList(testTodo));
            page.setTotal(1);

            when(todoMapper.selectListPage(any(Page.class), any(TodoQuery.class)))
                    .thenReturn(page);
            when(todoCategoryMapper.selectCategoryIdsByTodoId(anyLong()))
                    .thenReturn(Arrays.asList(1L));

            // Act
            IPage<TodoVO> result = todoService.selectList(query);

            // Assert
            assertThat(result).isNotNull();
            assertThat(result.getRecords()).hasSize(1);
            assertThat(result.getRecords().get(0).getTitle()).isEqualTo("Test Todo");

            verify(todoMapper).selectListPage(any(Page.class), any(TodoQuery.class));
        }
    }

    @Test
    @DisplayName("Should throw exception when user not authenticated for list")
    void selectList_NotAuthenticated() {
        // Arrange
        try (MockedStatic<SecurityUtils> securityUtilsMock = mockStatic(SecurityUtils.class)) {
            securityUtilsMock.when(SecurityUtils::getCurrentUserId).thenReturn(null);

            // Act & Assert
            assertThatThrownBy(() -> todoService.selectList(query))
                    .isInstanceOf(BusinessException.class)
                    .hasFieldOrPropertyWithValue("code", 401)
                    .hasMessage("User not authenticated");

            verify(todoMapper, never()).selectListPage(any(Page.class), any(TodoQuery.class));
        }
    }

    @Test
    @DisplayName("Should get todo by id successfully")
    void getById_Success() {
        // Arrange
        try (MockedStatic<SecurityUtils> securityUtilsMock = mockStatic(SecurityUtils.class)) {
            securityUtilsMock.when(SecurityUtils::getCurrentUserId).thenReturn(1L);
            when(todoMapper.selectById(1L)).thenReturn(testTodo);
            when(todoCategoryMapper.selectCategoryIdsByTodoId(1L)).thenReturn(Arrays.asList());

            // Act
            TodoVO result = todoService.getById(1L);

            // Assert
            assertThat(result).isNotNull();
            assertThat(result.getId()).isEqualTo(1L);
            assertThat(result.getTitle()).isEqualTo("Test Todo");

            verify(todoMapper).selectById(1L);
        }
    }

    @Test
    @DisplayName("Should throw exception when todo not found")
    void getById_NotFound() {
        // Arrange
        try (MockedStatic<SecurityUtils> securityUtilsMock = mockStatic(SecurityUtils.class)) {
            securityUtilsMock.when(SecurityUtils::getCurrentUserId).thenReturn(1L);
            when(todoMapper.selectById(999L)).thenReturn(null);

            // Act & Assert
            assertThatThrownBy(() -> todoService.getById(999L))
                    .isInstanceOf(BusinessException.class)
                    .hasFieldOrPropertyWithValue("code", 404)
                    .hasMessage("Todo not found");

            verify(todoMapper).selectById(999L);
        }
    }

    @Test
    @DisplayName("Should throw exception when accessing another user's todo")
    void getById_NoPermission() {
        // Arrange
        testTodo.setUserId(2L); // Different user
        try (MockedStatic<SecurityUtils> securityUtilsMock = mockStatic(SecurityUtils.class)) {
            securityUtilsMock.when(SecurityUtils::getCurrentUserId).thenReturn(1L);
            when(todoMapper.selectById(1L)).thenReturn(testTodo);

            // Act & Assert
            assertThatThrownBy(() -> todoService.getById(1L))
                    .isInstanceOf(BusinessException.class)
                    .hasFieldOrPropertyWithValue("code", 403)
                    .hasMessage("No permission to access this todo");

            verify(todoMapper).selectById(1L);
        }
    }

    @Test
    @DisplayName("Should successfully create a new todo")
    void insert_Success() {
        // Arrange
        try (MockedStatic<SecurityUtils> securityUtilsMock = mockStatic(SecurityUtils.class)) {
            securityUtilsMock.when(SecurityUtils::getCurrentUserId).thenReturn(1L);
            when(todoMapper.insert(any(Todo.class))).thenAnswer(invocation -> {
                Todo todo = invocation.getArgument(0);
                todo.setId(1L);
                return 1;
            });
            when(todoCategoryMapper.batchInsert(anyList())).thenReturn(1);

            // Act
            Long result = todoService.insert(todoDTO);

            // Assert
            assertThat(result).isEqualTo(1L);

            verify(todoMapper).insert(any(Todo.class));
            verify(todoCategoryMapper).batchInsert(anyList());
        }
    }

    @Test
    @DisplayName("Should successfully create todo without categories")
    void insert_WithoutCategories() {
        // Arrange
        todoDTO.setCategoryIds(null);
        try (MockedStatic<SecurityUtils> securityUtilsMock = mockStatic(SecurityUtils.class)) {
            securityUtilsMock.when(SecurityUtils::getCurrentUserId).thenReturn(1L);
            when(todoMapper.insert(any(Todo.class))).thenAnswer(invocation -> {
                Todo todo = invocation.getArgument(0);
                todo.setId(1L);
                return 1;
            });

            // Act
            Long result = todoService.insert(todoDTO);

            // Assert
            assertThat(result).isEqualTo(1L);

            verify(todoMapper).insert(any(Todo.class));
            verify(todoCategoryMapper, never()).batchInsert(anyList());
        }
    }

    @Test
    @DisplayName("Should successfully update todo")
    void update_Success() {
        // Arrange
        todoDTO.setId(1L);
        try (MockedStatic<SecurityUtils> securityUtilsMock = mockStatic(SecurityUtils.class)) {
            securityUtilsMock.when(SecurityUtils::getCurrentUserId).thenReturn(1L);
            when(todoMapper.selectById(1L)).thenReturn(testTodo);

            // Act
            todoService.update(todoDTO);

            // Assert
            verify(todoMapper).selectById(1L);
            verify(todoMapper).updateById(any(Todo.class));
            verify(todoCategoryMapper).deleteByTodoId(1L);
            verify(todoCategoryMapper).batchInsert(anyList());
        }
    }

    @Test
    @DisplayName("Should throw exception when updating todo without id")
    void update_NullId() {
        // Arrange
        todoDTO.setId(null);

        // Act & Assert
        assertThatThrownBy(() -> todoService.update(todoDTO))
                .isInstanceOf(BusinessException.class)
                .hasMessage("Todo ID cannot be empty");

        verify(todoMapper, never()).selectById(anyLong());
    }

    @Test
    @DisplayName("Should throw exception when updating another user's todo")
    void update_NoPermission() {
        // Arrange
        todoDTO.setId(1L);
        testTodo.setUserId(2L);
        try (MockedStatic<SecurityUtils> securityUtilsMock = mockStatic(SecurityUtils.class)) {
            securityUtilsMock.when(SecurityUtils::getCurrentUserId).thenReturn(1L);
            when(todoMapper.selectById(1L)).thenReturn(testTodo);

            // Act & Assert
            assertThatThrownBy(() -> todoService.update(todoDTO))
                    .isInstanceOf(BusinessException.class)
                    .hasFieldOrPropertyWithValue("code", 403)
                    .hasMessage("No permission to modify this todo");

            verify(todoMapper).selectById(1L);
            verify(todoMapper, never()).updateById(any(Todo.class));
        }
    }

    @Test
    @DisplayName("Should successfully delete todos")
    void deleteByIds_Success() {
        // Arrange
        Long[] ids = {1L, 2L};
        try (MockedStatic<SecurityUtils> securityUtilsMock = mockStatic(SecurityUtils.class)) {
            securityUtilsMock.when(SecurityUtils::getCurrentUserId).thenReturn(1L);
            when(todoMapper.selectById(1L)).thenReturn(testTodo);
            when(todoMapper.selectById(2L)).thenReturn(testTodo);

            // Act
            todoService.deleteByIds(ids);

            // Assert
            verify(todoMapper, times(2)).selectById(anyLong());
            verify(todoMapper).deleteById(1L);
            verify(todoMapper).deleteById(2L);
        }
    }

    @Test
    @DisplayName("Should skip deletion when todo belongs to another user")
    void deleteByIds_SkipOtherUserTodos() {
        // Arrange
        Long[] ids = {1L, 2L};
        Todo otherUserTodo = Todo.builder()
                .id(2L)
                .userId(2L)
                .title("Other User Todo")
                .build();

        try (MockedStatic<SecurityUtils> securityUtilsMock = mockStatic(SecurityUtils.class)) {
            securityUtilsMock.when(SecurityUtils::getCurrentUserId).thenReturn(1L);
            when(todoMapper.selectById(1L)).thenReturn(testTodo);
            when(todoMapper.selectById(2L)).thenReturn(otherUserTodo);

            // Act
            todoService.deleteByIds(ids);

            // Assert
            verify(todoMapper).deleteById(1L);
            verify(todoMapper, never()).deleteById(2L);
        }
    }

    @Test
    @DisplayName("Should successfully toggle todo status from pending to done")
    void toggleStatus_PendingToDone() {
        // Arrange
        try (MockedStatic<SecurityUtils> securityUtilsMock = mockStatic(SecurityUtils.class)) {
            securityUtilsMock.when(SecurityUtils::getCurrentUserId).thenReturn(1L);
            when(todoMapper.selectById(1L)).thenReturn(testTodo);
            when(todoCategoryMapper.selectCategoryIdsByTodoId(1L)).thenReturn(Arrays.asList());

            // Act
            TodoVO result = todoService.toggleStatus(1L);

            // Assert
            assertThat(result.getStatus()).isEqualTo(TodoStatus.DONE.getCode());

            verify(todoMapper).selectById(1L);
            verify(todoMapper).updateById(any(Todo.class));
        }
    }

    @Test
    @DisplayName("Should successfully toggle todo status from done to pending")
    void toggleStatus_DoneToPending() {
        // Arrange
        testTodo.setStatus(TodoStatus.DONE.getCode());
        testTodo.setCompletedAt(LocalDateTime.now());

        try (MockedStatic<SecurityUtils> securityUtilsMock = mockStatic(SecurityUtils.class)) {
            securityUtilsMock.when(SecurityUtils::getCurrentUserId).thenReturn(1L);
            when(todoMapper.selectById(1L)).thenReturn(testTodo);
            when(todoCategoryMapper.selectCategoryIdsByTodoId(1L)).thenReturn(Arrays.asList());

            // Act
            TodoVO result = todoService.toggleStatus(1L);

            // Assert
            assertThat(result.getStatus()).isEqualTo(TodoStatus.PENDING.getCode());

            verify(todoMapper).selectById(1L);
            verify(todoMapper).updateById(any(Todo.class));
        }
    }

    @Test
    @DisplayName("Should throw exception when toggling another user's todo")
    void toggleStatus_NoPermission() {
        // Arrange
        testTodo.setUserId(2L);
        try (MockedStatic<SecurityUtils> securityUtilsMock = mockStatic(SecurityUtils.class)) {
            securityUtilsMock.when(SecurityUtils::getCurrentUserId).thenReturn(1L);
            when(todoMapper.selectById(1L)).thenReturn(testTodo);

            // Act & Assert
            assertThatThrownBy(() -> todoService.toggleStatus(1L))
                    .isInstanceOf(BusinessException.class)
                    .hasFieldOrPropertyWithValue("code", 403)
                    .hasMessage("No permission to modify this todo");

            verify(todoMapper).selectById(1L);
            verify(todoMapper, never()).updateById(any(Todo.class));
        }
    }
}
