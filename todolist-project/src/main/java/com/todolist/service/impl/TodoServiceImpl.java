package com.todolist.service.impl;

import cn.hutool.core.util.StrUtil;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.todolist.common.exception.BusinessException;
import com.todolist.common.util.BeanConv;
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
import com.todolist.service.ITodoService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

/**
 * Todo Service Implementation
 *
 * @author TodoList Team
 * @since 1.0.0
 */
@Slf4j
@Service
@RequiredArgsConstructor
public class TodoServiceImpl implements ITodoService {

    private final TodoMapper todoMapper;
    private final CategoryMapper categoryMapper;
    private final TodoCategoryMapper todoCategoryMapper;

    @Override
    public IPage<TodoVO> selectList(TodoQuery query) {
        // Get current user ID
        Long userId = SecurityUtils.getCurrentUserId();
        if (userId == null) {
            throw new BusinessException(401, "User not authenticated");
        }

        // Set query parameters
        if (query.getPage() == null || query.getPage() < 1) {
            query.setPage(1);
        }
        if (query.getSize() == null || query.getSize() < 1 || query.getSize() > 100) {
            query.setSize(20);
        }
        if (StrUtil.isBlank(query.getSortBy())) {
            query.setSortBy("createdAt");
        }
        if (StrUtil.isBlank(query.getSortOrder())) {
            query.setSortOrder("desc");
        }

        // Set user ID for data isolation
        // Note: You would need to add userId field to TodoQuery or use a ThreadLocal approach

        // Execute pagination query
        Page<Todo> page = new Page<>(query.getPage(), query.getSize());
        IPage<Todo> todoPage = todoMapper.selectListPage(page, query);

        // Convert to VO
        IPage<TodoVO> voPage = todoPage.convert(todo -> convertToVO(todo));

        return voPage;
    }

    @Override
    public TodoVO getById(Long id) {
        Long userId = SecurityUtils.getCurrentUserId();
        if (userId == null) {
            throw new BusinessException(401, "User not authenticated");
        }

        Todo todo = todoMapper.selectById(id);
        if (todo == null) {
            throw new BusinessException(404, "Todo not found");
        }

        // Data isolation check
        if (!todo.getUserId().equals(userId)) {
            throw new BusinessException(403, "No permission to access this todo");
        }

        return convertToVO(todo);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public Long insert(TodoDTO dto) {
        Long userId = SecurityUtils.getCurrentUserId();
        if (userId == null) {
            throw new BusinessException(401, "User not authenticated");
        }

        // Build todo entity
        Todo todo = Todo.builder()
                .userId(userId)
                .title(dto.getTitle())
                .description(dto.getDescription())
                .status(TodoStatus.PENDING.getCode())
                .priority(dto.getPriority() != null ? dto.getPriority() : TodoPriority.MEDIUM.getCode())
                .dueDate(dto.getDueDate())
                .version(0)
                .build();

        todoMapper.insert(todo);

        // Insert category associations
        if (dto.getCategoryIds() != null && !dto.getCategoryIds().isEmpty()) {
            List<TodoCategory> associations = dto.getCategoryIds().stream()
                    .map(categoryId -> TodoCategory.builder()
                            .todoId(todo.getId())
                            .categoryId(categoryId)
                            .build())
                    .collect(Collectors.toList());
            todoCategoryMapper.batchInsert(associations);
        }

        log.info("Todo created: id={}, title={}", todo.getId(), todo.getTitle());
        return todo.getId();
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void update(TodoDTO dto) {
        if (dto.getId() == null) {
            throw new BusinessException("Todo ID cannot be empty");
        }

        Long userId = SecurityUtils.getCurrentUserId();
        if (userId == null) {
            throw new BusinessException(401, "User not authenticated");
        }

        Todo existingTodo = todoMapper.selectById(dto.getId());
        if (existingTodo == null) {
            throw new BusinessException(404, "Todo not found");
        }

        // Data isolation check
        if (!existingTodo.getUserId().equals(userId)) {
            throw new BusinessException(403, "No permission to modify this todo");
        }

        // Update todo
        existingTodo.setTitle(dto.getTitle());
        existingTodo.setDescription(dto.getDescription());
        if (dto.getPriority() != null) {
            existingTodo.setPriority(dto.getPriority());
        }
        if (dto.getDueDate() != null) {
            existingTodo.setDueDate(dto.getDueDate());
        }

        todoMapper.updateById(existingTodo);

        // Update category associations
        todoCategoryMapper.deleteByTodoId(dto.getId());
        if (dto.getCategoryIds() != null && !dto.getCategoryIds().isEmpty()) {
            List<TodoCategory> associations = dto.getCategoryIds().stream()
                    .map(categoryId -> TodoCategory.builder()
                            .todoId(dto.getId())
                            .categoryId(categoryId)
                            .build())
                    .collect(Collectors.toList());
            todoCategoryMapper.batchInsert(associations);
        }

        log.info("Todo updated: id={}", dto.getId());
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void deleteByIds(Long[] ids) {
        Long userId = SecurityUtils.getCurrentUserId();
        if (userId == null) {
            throw new BusinessException(401, "User not authenticated");
        }

        for (Long id : ids) {
            Todo todo = todoMapper.selectById(id);
            if (todo != null && todo.getUserId().equals(userId)) {
                todoMapper.deleteById(id);
                // Associations will be cascade deleted by database foreign key
            }
        }

        log.info("Todos deleted: ids={}", ids);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public TodoVO toggleStatus(Long id) {
        Long userId = SecurityUtils.getCurrentUserId();
        if (userId == null) {
            throw new BusinessException(401, "User not authenticated");
        }

        Todo todo = todoMapper.selectById(id);
        if (todo == null) {
            throw new BusinessException(404, "Todo not found");
        }

        // Data isolation check
        if (!todo.getUserId().equals(userId)) {
            throw new BusinessException(403, "No permission to modify this todo");
        }

        // Toggle status
        if (TodoStatus.PENDING.getCode().equals(todo.getStatus())) {
            todo.setStatus(TodoStatus.DONE.getCode());
            todo.setCompletedAt(LocalDateTime.now());
        } else {
            todo.setStatus(TodoStatus.PENDING.getCode());
            todo.setCompletedAt(null);
        }

        todoMapper.updateById(todo);

        log.info("Todo status toggled: id={}, status={}", id, todo.getStatus());
        return convertToVO(todo);
    }

    /**
     * Convert Todo entity to VO with categories
     */
    private TodoVO convertToVO(Todo todo) {
        TodoVO vo = BeanConv.convert(todo, TodoVO.class);

        // Load categories
        List<Long> categoryIds = todoCategoryMapper.selectCategoryIdsByTodoId(todo.getId());
        if (categoryIds != null && !categoryIds.isEmpty()) {
            List<CategoryVO> categories = categoryIds.stream()
                    .map(categoryId -> {
                        com.todolist.domain.entity.Category category = categoryMapper.selectById(categoryId);
                        if (category != null) {
                            return CategoryVO.builder()
                                    .id(category.getId())
                                    .name(category.getName())
                                    .color(category.getColor())
                                    .build();
                        }
                        return null;
                    })
                    .filter(cat -> cat != null)
                    .collect(Collectors.toList());
            vo.setCategories(categories);
        } else {
            vo.setCategories(new ArrayList<>());
        }

        return vo;
    }
}
