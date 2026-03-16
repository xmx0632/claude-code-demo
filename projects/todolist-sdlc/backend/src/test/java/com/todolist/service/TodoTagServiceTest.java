package com.todolist.service;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.todolist.common.exception.BusinessException;
import com.todolist.dto.TodoTagsDTO;
import com.todolist.entity.Tag;
import com.todolist.entity.Todo;
import com.todolist.entity.TodoTag;
import com.todolist.mapper.TagMapper;
import com.todolist.mapper.TodoMapper;
import com.todolist.mapper.TodoTagMapper;
import com.todolist.service.impl.TodoTagServiceImpl;
import com.todolist.vo.TagVO;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;

import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

/**
 * TodoTagService 单元测试
 *
 * @author Claude Code
 * @since 2026-03-16
 */
@ExtendWith(MockitoExtension.class)
@DisplayName("任务标签关联服务 - 单元测试")
class TodoTagServiceTest {

    @Mock
    private TodoTagMapper todoTagMapper;

    @Mock
    private TodoMapper todoMapper;

    @Mock
    private TagMapper tagMapper;

    private TodoTagServiceImpl todoTagService;

    private Todo testTodo;
    private Tag testTag1, testTag2;
    private TodoTagsDTO testTodoTagsDTO;

    @BeforeEach
    void setUp() {
        todoTagService = new TodoTagServiceImpl(todoTagMapper, todoMapper, tagMapper);

        // 设置安全上下文
        SecurityContextHolder.getContext().setAuthentication(
                new UsernamePasswordAuthenticationToken(1L, null, Collections.emptyList())
        );

        // 创建测试数据
        testTodo = new Todo();
        testTodo.setId(1L);
        testTodo.setUserId(1L);
        testTodo.setTitle("测试任务");

        testTag1 = new Tag();
        testTag1.setId(1L);
        testTag1.setUserId(1L);
        testTag1.setName("工作");
        testTag1.setColor("#FF6B6B");

        testTag2 = new Tag();
        testTag2.setId(2L);
        testTag2.setUserId(1L);
        testTag2.setName("重要");
        testTag2.setColor("#45B7D1");

        testTodoTagsDTO = new TodoTagsDTO();
        testTodoTagsDTO.setTagIds(Arrays.asList(1L, 2L));
    }

    @AfterEach
    void tearDown() {
        SecurityContextHolder.clearContext();
    }

    @Test
    @DisplayName("为任务添加标签 - 成功")
    void addTags_Success() {
        // Arrange
        when(todoMapper.selectById(1L)).thenReturn(testTodo);
        when(tagMapper.selectList(any(LambdaQueryWrapper.class)))
                .thenReturn(Arrays.asList(testTag1, testTag2));
        when(todoTagMapper.selectTagIdsByTodoId(1L)).thenReturn(Collections.emptyList());
        when(todoTagMapper.insert(any(TodoTag.class))).thenReturn(1);
        when(tagMapper.selectByTodoId(1L)).thenReturn(Arrays.asList(testTag1, testTag2));

        // Act
        List<TagVO> result = todoTagService.addTags(1L, testTodoTagsDTO);

        // Assert
        assertNotNull(result);
        assertEquals(2, result.size());
        verify(todoTagMapper, times(2)).insert(any(TodoTag.class));
    }

    @Test
    @DisplayName("为任务添加标签 - 部分标签已存在")
    void addTags_PartialExists() {
        // Arrange
        when(todoMapper.selectById(1L)).thenReturn(testTodo);
        when(tagMapper.selectList(any(LambdaQueryWrapper.class)))
                .thenReturn(Arrays.asList(testTag1, testTag2));
        when(todoTagMapper.selectTagIdsByTodoId(1L)).thenReturn(Arrays.asList(1L));
        when(todoTagMapper.insert(any(TodoTag.class))).thenReturn(1);
        when(tagMapper.selectByTodoId(1L)).thenReturn(Arrays.asList(testTag1, testTag2));

        // Act
        List<TagVO> result = todoTagService.addTags(1L, testTodoTagsDTO);

        // Assert
        assertNotNull(result);
        assertEquals(2, result.size());
        verify(todoTagMapper, times(1)).insert(any(TodoTag.class));
    }

    @Test
    @DisplayName("为任务添加标签 - 任务不存在")
    void addTags_TodoNotFound() {
        // Arrange
        when(todoMapper.selectById(1L)).thenReturn(null);

        // Act & Assert
        BusinessException exception = assertThrows(BusinessException.class, () -> {
            todoTagService.addTags(1L, testTodoTagsDTO);
        });

        assertEquals("任务不存在", exception.getMessage());
    }

    @Test
    @DisplayName("为任务添加标签 - 任务不属于当前用户")
    void addTags_TodoNotOwned() {
        // Arrange
        Todo otherTodo = new Todo();
        otherTodo.setId(1L);
        otherTodo.setUserId(999L);
        when(todoMapper.selectById(1L)).thenReturn(otherTodo);

        // Act & Assert
        BusinessException exception = assertThrows(BusinessException.class, () -> {
            todoTagService.addTags(1L, testTodoTagsDTO);
        });

        assertEquals("任务不存在", exception.getMessage());
    }

    @Test
    @DisplayName("为任务添加标签 - 包含无效标签")
    void addTags_InvalidTag() {
        // Arrange
        when(todoMapper.selectById(1L)).thenReturn(testTodo);
        when(tagMapper.selectList(any(LambdaQueryWrapper.class)))
                .thenReturn(Collections.singletonList(testTag1)); // 只返回一个标签

        // Act & Assert
        BusinessException exception = assertThrows(BusinessException.class, () -> {
            todoTagService.addTags(1L, testTodoTagsDTO);
        });

        assertEquals("存在无效的标签", exception.getMessage());
    }

    @Test
    @DisplayName("移除任务标签 - 成功")
    void removeTag_Success() {
        // Arrange
        when(todoMapper.selectById(1L)).thenReturn(testTodo);
        when(todoTagMapper.delete(any(LambdaQueryWrapper.class))).thenReturn(1);

        // Act
        todoTagService.removeTag(1L, 1L);

        // Assert
        verify(todoTagMapper, times(1)).delete(any(LambdaQueryWrapper.class));
    }

    @Test
    @DisplayName("移除任务标签 - 任务不存在")
    void removeTag_TodoNotFound() {
        // Arrange
        when(todoMapper.selectById(1L)).thenReturn(null);

        // Act & Assert
        BusinessException exception = assertThrows(BusinessException.class, () -> {
            todoTagService.removeTag(1L, 1L);
        });

        assertEquals("任务不存在", exception.getMessage());
    }

    @Test
    @DisplayName("查询任务的所有标签 - 成功")
    void getTagsByTodoId_Success() {
        // Arrange
        when(todoMapper.selectById(1L)).thenReturn(testTodo);
        when(tagMapper.selectByTodoId(1L)).thenReturn(Arrays.asList(testTag1, testTag2));

        // Act
        List<TagVO> result = todoTagService.getTagsByTodoId(1L);

        // Assert
        assertNotNull(result);
        assertEquals(2, result.size());
        assertEquals("工作", result.get(0).getName());
        assertEquals("重要", result.get(1).getName());
    }

    @Test
    @DisplayName("查询任务的所有标签 - 无标签")
    void getTagsByTodoId_NoTags() {
        // Arrange
        when(todoMapper.selectById(1L)).thenReturn(testTodo);
        when(tagMapper.selectByTodoId(1L)).thenReturn(Collections.emptyList());

        // Act
        List<TagVO> result = todoTagService.getTagsByTodoId(1L);

        // Assert
        assertNotNull(result);
        assertTrue(result.isEmpty());
    }

    @Test
    @DisplayName("批量更新任务标签 - 成功")
    void updateTags_Success() {
        // Arrange
        when(todoMapper.selectById(1L)).thenReturn(testTodo);
        when(tagMapper.selectList(any(LambdaQueryWrapper.class)))
                .thenReturn(Arrays.asList(testTag1, testTag2));
        when(todoTagMapper.deleteByTodoId(1L)).thenReturn(1);
        when(todoTagMapper.insert(any(TodoTag.class))).thenReturn(1);
        when(tagMapper.selectByTodoId(1L)).thenReturn(Arrays.asList(testTag1, testTag2));

        // Act
        List<TagVO> result = todoTagService.updateTags(1L, testTodoTagsDTO);

        // Assert
        assertNotNull(result);
        assertEquals(2, result.size());
        verify(todoTagMapper, times(1)).deleteByTodoId(1L);
        verify(todoTagMapper, times(2)).insert(any(TodoTag.class));
    }

    @Test
    @DisplayName("批量更新任务标签 - 清空所有标签")
    void updateTags_ClearAll() {
        // Arrange
        TodoTagsDTO emptyDto = new TodoTagsDTO();
        emptyDto.setTagIds(Collections.emptyList());

        when(todoMapper.selectById(1L)).thenReturn(testTodo);
        when(todoTagMapper.deleteByTodoId(1L)).thenReturn(1);

        // Act
        List<TagVO> result = todoTagService.updateTags(1L, emptyDto);

        // Assert
        assertNotNull(result);
        assertTrue(result.isEmpty());
        verify(todoTagMapper, times(1)).deleteByTodoId(1L);
        verify(todoTagMapper, never()).insert(any(TodoTag.class));
    }

    @Test
    @DisplayName("批量更新任务标签 - 包含无效标签")
    void updateTags_InvalidTag() {
        // Arrange
        when(todoMapper.selectById(1L)).thenReturn(testTodo);
        when(tagMapper.selectList(any(LambdaQueryWrapper.class)))
                .thenReturn(Collections.singletonList(testTag1));

        // Act & Assert
        BusinessException exception = assertThrows(BusinessException.class, () -> {
            todoTagService.updateTags(1L, testTodoTagsDTO);
        });

        assertEquals("存在无效的标签", exception.getMessage());
    }

    @Test
    @DisplayName("清空任务的所有标签 - 成功")
    void clearTags_Success() {
        // Arrange
        when(todoMapper.selectById(1L)).thenReturn(testTodo);
        when(todoTagMapper.deleteByTodoId(1L)).thenReturn(2);

        // Act
        todoTagService.clearTags(1L);

        // Assert
        verify(todoTagMapper, times(1)).deleteByTodoId(1L);
    }

    @Test
    @DisplayName("清空任务的所有标签 - 任务不存在")
    void clearTags_TodoNotFound() {
        // Arrange
        when(todoMapper.selectById(1L)).thenReturn(null);

        // Act & Assert
        BusinessException exception = assertThrows(BusinessException.class, () -> {
            todoTagService.clearTags(1L);
        });

        assertEquals("任务不存在", exception.getMessage());
    }
}
