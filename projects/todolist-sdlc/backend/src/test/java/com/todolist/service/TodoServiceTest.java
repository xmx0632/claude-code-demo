package com.todolist.service;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.todolist.dto.TodoDTO;
import com.todolist.dto.TodoQueryDTO;
import com.todolist.entity.Todo;
import com.todolist.mapper.TodoMapper;
import com.todolist.service.impl.TodoServiceImpl;
import com.todolist.vo.TodoVO;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Collections;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

/**
 * TodoService 单元测试
 *
 * @author Claude Code
 * @since 2026-03-16
 */
@ExtendWith(MockitoExtension.class)
class TodoServiceTest {

    @Mock
    private TodoMapper todoMapper;

    @InjectMocks
    private TodoServiceImpl todoService;

    private Todo testTodo;
    private TodoDTO testTodoDTO;

    @BeforeEach
    void setUp() {
        testTodo = new Todo();
        testTodo.setId(1L);
        testTodo.setUserId(1L);
        testTodo.setTitle("测试任务");
        testTodo.setDescription("测试描述");
        testTodo.setStatus(0);

        testTodoDTO = new TodoDTO();
        testTodoDTO.setTitle("新任务");
        testTodoDTO.setDescription("新描述");
    }

    @Test
    @DisplayName("分页查询任务列表 - 成功")
    void pageList_Success() {
        // Arrange
        TodoQueryDTO query = new TodoQueryDTO();
        query.setPage(1);
        query.setSize(10);

        Page<Todo> page = new Page<>(1, 10);
        page.setRecords(Collections.singletonList(testTodo));
        page.setTotal(1);

        when(todoMapper.selectPage(any(Page.class), any(LambdaQueryWrapper.class)))
                .thenReturn(page);

        // Act
        IPage<TodoVO> result = todoService.pageList(query);

        // Assert
        assertNotNull(result);
        assertEquals(1, result.getTotal());
        assertEquals(1, result.getRecords().size());
        assertEquals("测试任务", result.getRecords().get(0).getTitle());
    }

    @Test
    @DisplayName("创建任务 - 成功")
    void create_Success() {
        // Arrange
        when(todoMapper.insert(any(Todo.class))).thenReturn(1);

        // Act
        TodoVO result = todoService.create(testTodoDTO);

        // Assert
        assertNotNull(result);
        assertEquals("新任务", result.getTitle());
        verify(todoMapper, times(1)).insert(any(Todo.class));
    }

    @Test
    @DisplayName("完成任务 - 成功")
    void complete_Success() {
        // Arrange
        when(todoMapper.selectOne(any(LambdaQueryWrapper.class))).thenReturn(testTodo);
        when(todoMapper.updateById(any(Todo.class))).thenReturn(1);

        // Act
        TodoVO result = todoService.complete(1L);

        // Assert
        assertNotNull(result);
        assertEquals(1, result.getStatus()); // COMPLETED
        verify(todoMapper, times(1)).updateById(any(Todo.class));
    }

    @Test
    @DisplayName("删除任务 - 成功")
    void delete_Success() {
        // Arrange
        when(todoMapper.delete(any(LambdaQueryWrapper.class))).thenReturn(1);

        // Act
        todoService.delete(1L);

        // Assert
        verify(todoMapper, times(1)).delete(any(LambdaQueryWrapper.class));
    }

    @Test
    @DisplayName("创建任务 - 标题为空应失败")
    void create_EmptyTitle_ShouldFail() {
        // Arrange
        TodoDTO emptyDto = new TodoDTO();
        emptyDto.setTitle("");

        // Act & Assert
        assertThrows(Exception.class, () -> todoService.create(emptyDto));
    }
}
