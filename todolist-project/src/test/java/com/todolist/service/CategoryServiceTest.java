package com.todolist.service;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.todolist.common.exception.BusinessException;
import com.todolist.domain.entity.Category;
import com.todolist.domain.entity.TodoCategory;
import com.todolist.dto.request.CategoryDTO;
import com.todolist.dto.response.CategoryVO;
import com.todolist.mapper.CategoryMapper;
import com.todolist.mapper.TodoCategoryMapper;
import com.todolist.security.SecurityUtils;
import com.todolist.service.impl.CategoryServiceImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockedStatic;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Arrays;
import java.util.List;

import static org.assertj.core.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.*;

/**
 * Category Service Unit Tests
 *
 * Tests for CategoryServiceImpl covering:
 * - Category CRUD operations
 * - Name uniqueness validation
 * - Todo count tracking
 * - Data isolation and authorization
 * - Category usage validation before deletion
 *
 * @author TodoList Team
 * @since 1.0.0
 */
@ExtendWith(MockitoExtension.class)
@DisplayName("Category Service Tests")
class CategoryServiceTest {

    @Mock
    private CategoryMapper categoryMapper;

    @Mock
    private TodoCategoryMapper todoCategoryMapper;

    @InjectMocks
    private CategoryServiceImpl categoryService;

    private CategoryDTO categoryDTO;
    private Category testCategory;
    private List<Category> testCategories;

    @BeforeEach
    void setUp() {
        // Arrange: Set up test data
        categoryDTO = CategoryDTO.builder()
                .name("Work")
                .color("#FF0000")
                .build();

        testCategory = Category.builder()
                .id(1L)
                .userId(1L)
                .name("Work")
                .color("#FF0000")
                .build();

        Category testCategory2 = Category.builder()
                .id(2L)
                .userId(1L)
                .name("Personal")
                .color("#00FF00")
                .build();

        testCategories = Arrays.asList(testCategory, testCategory2);
    }

    @Test
    @DisplayName("Should successfully get category list")
    void selectList_Success() {
        // Arrange
        try (MockedStatic<SecurityUtils> securityUtilsMock = mockStatic(SecurityUtils.class)) {
            securityUtilsMock.when(SecurityUtils::getCurrentUserId).thenReturn(1L);
            when(categoryMapper.selectList(any(LambdaQueryWrapper.class))).thenReturn(testCategories);
            when(todoCategoryMapper.selectCount(any(LambdaQueryWrapper.class))).thenReturn(5L);

            // Act
            List<CategoryVO> result = categoryService.selectList();

            // Assert
            assertThat(result).isNotNull();
            assertThat(result).hasSize(2);
            assertThat(result.get(0).getName()).isEqualTo("Work");
            assertThat(result.get(0).getTodoCount()).isEqualTo(5);

            verify(categoryMapper).selectList(any(LambdaQueryWrapper.class));
        }
    }

    @Test
    @DisplayName("Should throw exception when user not authenticated for list")
    void selectList_NotAuthenticated() {
        // Arrange
        try (MockedStatic<SecurityUtils> securityUtilsMock = mockStatic(SecurityUtils.class)) {
            securityUtilsMock.when(SecurityUtils::getCurrentUserId).thenReturn(null);

            // Act & Assert
            assertThatThrownBy(() -> categoryService.selectList())
                    .isInstanceOf(BusinessException.class)
                    .hasFieldOrPropertyWithValue("code", 401)
                    .hasMessage("User not authenticated");

            verify(categoryMapper, never()).selectList(any(LambdaQueryWrapper.class));
        }
    }

    @Test
    @DisplayName("Should get category by id successfully")
    void getById_Success() {
        // Arrange
        try (MockedStatic<SecurityUtils> securityUtilsMock = mockStatic(SecurityUtils.class)) {
            securityUtilsMock.when(SecurityUtils::getCurrentUserId).thenReturn(1L);
            when(categoryMapper.selectById(1L)).thenReturn(testCategory);
            when(todoCategoryMapper.selectCount(any(LambdaQueryWrapper.class))).thenReturn(3L);

            // Act
            CategoryVO result = categoryService.getById(1L);

            // Assert
            assertThat(result).isNotNull();
            assertThat(result.getId()).isEqualTo(1L);
            assertThat(result.getName()).isEqualTo("Work");
            assertThat(result.getColor()).isEqualTo("#FF0000");
            assertThat(result.getTodoCount()).isEqualTo(3);

            verify(categoryMapper).selectById(1L);
        }
    }

    @Test
    @DisplayName("Should throw exception when category not found")
    void getById_NotFound() {
        // Arrange
        try (MockedStatic<SecurityUtils> securityUtilsMock = mockStatic(SecurityUtils.class)) {
            securityUtilsMock.when(SecurityUtils::getCurrentUserId).thenReturn(1L);
            when(categoryMapper.selectById(999L)).thenReturn(null);

            // Act & Assert
            assertThatThrownBy(() -> categoryService.getById(999L))
                    .isInstanceOf(BusinessException.class)
                    .hasFieldOrPropertyWithValue("code", 404)
                    .hasMessage("Category not found");

            verify(categoryMapper).selectById(999L);
        }
    }

    @Test
    @DisplayName("Should throw exception when accessing another user's category")
    void getById_NoPermission() {
        // Arrange
        testCategory.setUserId(2L);
        try (MockedStatic<SecurityUtils> securityUtilsMock = mockStatic(SecurityUtils.class)) {
            securityUtilsMock.when(SecurityUtils::getCurrentUserId).thenReturn(1L);
            when(categoryMapper.selectById(1L)).thenReturn(testCategory);

            // Act & Assert
            assertThatThrownBy(() -> categoryService.getById(1L))
                    .isInstanceOf(BusinessException.class)
                    .hasFieldOrPropertyWithValue("code", 403)
                    .hasMessage("No permission to access this category");

            verify(categoryMapper).selectById(1L);
        }
    }

    @Test
    @DisplayName("Should successfully create a new category")
    void insert_Success() {
        // Arrange
        try (MockedStatic<SecurityUtils> securityUtilsMock = mockStatic(SecurityUtils.class)) {
            securityUtilsMock.when(SecurityUtils::getCurrentUserId).thenReturn(1L);
            when(categoryMapper.selectCount(any(LambdaQueryWrapper.class))).thenReturn(0L);
            when(categoryMapper.insert(any(Category.class))).thenAnswer(invocation -> {
                Category category = invocation.getArgument(0);
                category.setId(1L);
                return 1;
            });

            // Act
            Long result = categoryService.insert(categoryDTO);

            // Assert
            assertThat(result).isEqualTo(1L);

            verify(categoryMapper).selectCount(any(LambdaQueryWrapper.class));
            verify(categoryMapper).insert(any(Category.class));
        }
    }

    @Test
    @DisplayName("Should create category with default color when not provided")
    void insert_WithDefaultColor() {
        // Arrange
        categoryDTO.setColor(null);
        try (MockedStatic<SecurityUtils> securityUtilsMock = mockStatic(SecurityUtils.class)) {
            securityUtilsMock.when(SecurityUtils::getCurrentUserId).thenReturn(1L);
            when(categoryMapper.selectCount(any(LambdaQueryWrapper.class))).thenReturn(0L);
            when(categoryMapper.insert(any(Category.class))).thenAnswer(invocation -> {
                Category category = invocation.getArgument(0);
                category.setId(1L);
                return 1;
            });

            // Act
            Long result = categoryService.insert(categoryDTO);

            // Assert
            assertThat(result).isEqualTo(1L);

            verify(categoryMapper).selectCount(any(LambdaQueryWrapper.class));
            verify(categoryMapper).insert(any(Category.class));
        }
    }

    @Test
    @DisplayName("Should throw exception when category name already exists")
    void insert_NameAlreadyExists() {
        // Arrange
        try (MockedStatic<SecurityUtils> securityUtilsMock = mockStatic(SecurityUtils.class)) {
            securityUtilsMock.when(SecurityUtils::getCurrentUserId).thenReturn(1L);
            when(categoryMapper.selectCount(any(LambdaQueryWrapper.class))).thenReturn(1L);

            // Act & Assert
            assertThatThrownBy(() -> categoryService.insert(categoryDTO))
                    .isInstanceOf(BusinessException.class)
                    .hasFieldOrPropertyWithValue("code", 409)
                    .hasMessage("Category name already exists");

            verify(categoryMapper).selectCount(any(LambdaQueryWrapper.class));
            verify(categoryMapper, never()).insert(any(Category.class));
        }
    }

    @Test
    @DisplayName("Should successfully update category")
    void update_Success() {
        // Arrange
        categoryDTO.setId(1L);
        categoryDTO.setName("Updated Work");
        categoryDTO.setColor("#0000FF");

        try (MockedStatic<SecurityUtils> securityUtilsMock = mockStatic(SecurityUtils.class)) {
            securityUtilsMock.when(SecurityUtils::getCurrentUserId).thenReturn(1L);
            when(categoryMapper.selectById(1L)).thenReturn(testCategory);
            when(categoryMapper.selectCount(any(LambdaQueryWrapper.class))).thenReturn(0L);

            // Act
            categoryService.update(categoryDTO);

            // Assert
            verify(categoryMapper).selectById(1L);
            verify(categoryMapper).selectCount(any(LambdaQueryWrapper.class));
            verify(categoryMapper).updateById(any(Category.class));
        }
    }

    @Test
    @DisplayName("Should throw exception when updating category without id")
    void update_NullId() {
        // Arrange
        categoryDTO.setId(null);

        // Act & Assert
        assertThatThrownBy(() -> categoryService.update(categoryDTO))
                .isInstanceOf(BusinessException.class)
                .hasMessage("Category ID cannot be empty");

        verify(categoryMapper, never()).selectById(anyLong());
    }

    @Test
    @DisplayName("Should throw exception when updating to existing name")
    void update_NameAlreadyExists() {
        // Arrange
        categoryDTO.setId(1L);
        categoryDTO.setName("Personal"); // Different name

        try (MockedStatic<SecurityUtils> securityUtilsMock = mockStatic(SecurityUtils.class)) {
            securityUtilsMock.when(SecurityUtils::getCurrentUserId).thenReturn(1L);
            when(categoryMapper.selectById(1L)).thenReturn(testCategory);
            when(categoryMapper.selectCount(any(LambdaQueryWrapper.class))).thenReturn(1L);

            // Act & Assert
            assertThatThrownBy(() -> categoryService.update(categoryDTO))
                    .isInstanceOf(BusinessException.class)
                    .hasFieldOrPropertyWithValue("code", 409)
                    .hasMessage("Category name already exists");

            verify(categoryMapper).selectById(1L);
            verify(categoryMapper).selectCount(any(LambdaQueryWrapper.class));
            verify(categoryMapper, never()).updateById(any(Category.class));
        }
    }

    @Test
    @DisplayName("Should throw exception when updating another user's category")
    void update_NoPermission() {
        // Arrange
        categoryDTO.setId(1L);
        testCategory.setUserId(2L);

        try (MockedStatic<SecurityUtils> securityUtilsMock = mockStatic(SecurityUtils.class)) {
            securityUtilsMock.when(SecurityUtils::getCurrentUserId).thenReturn(1L);
            when(categoryMapper.selectById(1L)).thenReturn(testCategory);

            // Act & Assert
            assertThatThrownBy(() -> categoryService.update(categoryDTO))
                    .isInstanceOf(BusinessException.class)
                    .hasFieldOrPropertyWithValue("code", 403)
                    .hasMessage("No permission to modify this category");

            verify(categoryMapper).selectById(1L);
            verify(categoryMapper, never()).updateById(any(Category.class));
        }
    }

    @Test
    @DisplayName("Should successfully delete categories")
    void deleteByIds_Success() {
        // Arrange
        Long[] ids = {1L, 2L};
        try (MockedStatic<SecurityUtils> securityUtilsMock = mockStatic(SecurityUtils.class)) {
            securityUtilsMock.when(SecurityUtils::getCurrentUserId).thenReturn(1L);
            when(categoryMapper.selectById(1L)).thenReturn(testCategory);
            when(categoryMapper.selectById(2L)).thenReturn(testCategory);
            when(todoCategoryMapper.selectCount(any(LambdaQueryWrapper.class))).thenReturn(0L);

            // Act
            categoryService.deleteByIds(ids);

            // Assert
            verify(categoryMapper, times(2)).selectById(anyLong());
            verify(categoryMapper).deleteById(1L);
            verify(categoryMapper).deleteById(2L);
        }
    }

    @Test
    @DisplayName("Should throw exception when deleting category in use")
    void deleteByIds_CategoryInUse() {
        // Arrange
        Long[] ids = {1L};
        try (MockedStatic<SecurityUtils> securityUtilsMock = mockStatic(SecurityUtils.class)) {
            securityUtilsMock.when(SecurityUtils::getCurrentUserId).thenReturn(1L);
            when(categoryMapper.selectById(1L)).thenReturn(testCategory);
            when(todoCategoryMapper.selectCount(any(LambdaQueryWrapper.class))).thenReturn(5L);

            // Act & Assert
            assertThatThrownBy(() -> categoryService.deleteByIds(ids))
                    .isInstanceOf(BusinessException.class)
                    .hasFieldOrPropertyWithValue("code", 409)
                    .hasMessage("Category is in use and cannot be deleted");

            verify(categoryMapper).selectById(1L);
            verify(todoCategoryMapper).selectCount(any(LambdaQueryWrapper.class));
            verify(categoryMapper, never()).deleteById(1L);
        }
    }

    @Test
    @DisplayName("Should skip deletion when category belongs to another user")
    void deleteByIds_SkipOtherUserCategories() {
        // Arrange
        Long[] ids = {1L, 2L};
        Category otherUserCategory = Category.builder()
                .id(2L)
                .userId(2L)
                .name("Other Category")
                .build();

        try (MockedStatic<SecurityUtils> securityUtilsMock = mockStatic(SecurityUtils.class)) {
            securityUtilsMock.when(SecurityUtils::getCurrentUserId).thenReturn(1L);
            when(categoryMapper.selectById(1L)).thenReturn(testCategory);
            when(categoryMapper.selectById(2L)).thenReturn(otherUserCategory);
            when(todoCategoryMapper.selectCount(any(LambdaQueryWrapper.class))).thenReturn(0L);

            // Act
            categoryService.deleteByIds(ids);

            // Assert
            verify(categoryMapper).deleteById(1L);
            verify(categoryMapper, never()).deleteById(2L);
        }
    }

    @Test
    @DisplayName("Should allow updating category with same name")
    void update_SameName() {
        // Arrange
        categoryDTO.setId(1L);
        categoryDTO.setName("Work"); // Same name
        try (MockedStatic<SecurityUtils> securityUtilsMock = mockStatic(SecurityUtils.class)) {
            securityUtilsMock.when(SecurityUtils::getCurrentUserId).thenReturn(1L);
            when(categoryMapper.selectById(1L)).thenReturn(testCategory);

            // Act
            categoryService.update(categoryDTO);

            // Assert
            verify(categoryMapper).selectById(1L);
            verify(categoryMapper, never()).selectCount(any(LambdaQueryWrapper.class));
            verify(categoryMapper).updateById(any(Category.class));
        }
    }
}
