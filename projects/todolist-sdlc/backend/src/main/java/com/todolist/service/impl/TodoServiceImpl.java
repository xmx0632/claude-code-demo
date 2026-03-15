package com.todolist.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.todolist.common.exception.BusinessException;
import com.todolist.dto.TodoDTO;
import com.todolist.dto.TodoQueryDTO;
import com.todolist.entity.Category;
import com.todolist.entity.Todo;
import com.todolist.mapper.CategoryMapper;
import com.todolist.mapper.TodoMapper;
import com.todolist.service.TodoService;
import com.todolist.util.SecurityUtils;
import com.todolist.vo.TodoVO;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;

/**
 * 任务服务实现
 *
 * @author Claude Code
 * @since 2026-03-16
 */
@Service
@RequiredArgsConstructor
public class TodoServiceImpl extends ServiceImpl<TodoMapper, Todo> implements TodoService {

    private final CategoryMapper categoryMapper;

    @Override
    public IPage<TodoVO> pageList(TodoQueryDTO query) {
        Long userId = SecurityUtils.getCurrentUserId();

        LambdaQueryWrapper<Todo> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(Todo::getUserId, userId)
               .eq(query.getStatus() != null, Todo::getStatus, query.getStatus())
               .eq(query.getPriority() != null, Todo::getPriority, query.getPriority())
               .eq(query.getCategoryId() != null, Todo::getCategoryId, query.getCategoryId())
               .like(query.getKeyword() != null, Todo::getTitle, query.getKeyword())
               .orderByDesc(Todo::getCreatedAt);

        Page<Todo> page = new Page<>(query.getPage(), query.getSize());
        IPage<Todo> todoPage = this.page(page, wrapper);

        return todoPage.convert(this::convertToVO);
    }

    @Override
    public TodoVO getDetail(Long id) {
        Long userId = SecurityUtils.getCurrentUserId();

        Todo todo = this.getOne(new LambdaQueryWrapper<Todo>()
                .eq(Todo::getId, id)
                .eq(Todo::getUserId, userId));

        if (todo == null) {
            throw new BusinessException("任务不存在");
        }

        return convertToVO(todo);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public TodoVO create(TodoDTO dto) {
        Long userId = SecurityUtils.getCurrentUserId();

        Todo todo = new Todo();
        BeanUtils.copyProperties(dto, todo);
        todo.setUserId(userId);
        todo.setStatus(0); // 默认待办

        this.save(todo);
        return convertToVO(todo);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public TodoVO update(Long id, TodoDTO dto) {
        Long userId = SecurityUtils.getCurrentUserId();

        Todo todo = this.getOne(new LambdaQueryWrapper<Todo>()
                .eq(Todo::getId, id)
                .eq(Todo::getUserId, userId));

        if (todo == null) {
            throw new BusinessException("任务不存在");
        }

        BeanUtils.copyProperties(dto, todo);
        this.updateById(todo);

        return convertToVO(todo);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void delete(Long id) {
        Long userId = SecurityUtils.getCurrentUserId();

        this.remove(new LambdaQueryWrapper<Todo>()
                .eq(Todo::getId, id)
                .eq(Todo::getUserId, userId));
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public TodoVO complete(Long id) {
        Long userId = SecurityUtils.getCurrentUserId();

        Todo todo = this.getOne(new LambdaQueryWrapper<Todo>()
                .eq(Todo::getId, id)
                .eq(Todo::getUserId, userId));

        if (todo == null) {
            throw new BusinessException("任务不存在");
        }

        todo.setStatus(2); // 已完成
        todo.setCompletedAt(LocalDateTime.now());
        this.updateById(todo);

        return convertToVO(todo);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public TodoVO uncomplete(Long id) {
        Long userId = SecurityUtils.getCurrentUserId();

        Todo todo = this.getOne(new LambdaQueryWrapper<Todo>()
                .eq(Todo::getId, id)
                .eq(Todo::getUserId, userId));

        if (todo == null) {
            throw new BusinessException("任务不存在");
        }

        todo.setStatus(0); // 待办
        todo.setCompletedAt(null);
        this.updateById(todo);

        return convertToVO(todo);
    }

    private TodoVO convertToVO(Todo todo) {
        TodoVO vo = new TodoVO();
        BeanUtils.copyProperties(todo, vo);

        // 查询分类信息
        if (todo.getCategoryId() != null) {
            Category category = categoryMapper.selectById(todo.getCategoryId());
            if (category != null) {
                vo.setCategoryName(category.getName());
                vo.setCategoryColor(category.getColor());
            }
        }

        return vo;
    }
}
