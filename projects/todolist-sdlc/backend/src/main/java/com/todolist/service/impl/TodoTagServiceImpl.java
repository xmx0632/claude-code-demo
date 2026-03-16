package com.todolist.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.todolist.common.exception.BusinessException;
import com.todolist.dto.TodoTagsDTO;
import com.todolist.entity.Tag;
import com.todolist.entity.Todo;
import com.todolist.entity.TodoTag;
import com.todolist.mapper.TagMapper;
import com.todolist.mapper.TodoMapper;
import com.todolist.mapper.TodoTagMapper;
import com.todolist.service.TodoTagService;
import com.todolist.util.SecurityUtils;
import com.todolist.vo.TagVO;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

/**
 * 任务标签关联服务实现
 *
 * @author Claude Code
 * @since 2026-03-16
 */
@Slf4j
@Service
@RequiredArgsConstructor
public class TodoTagServiceImpl implements TodoTagService {

    private final TodoTagMapper todoTagMapper;
    private final TodoMapper todoMapper;
    private final TagMapper tagMapper;

    @Override
    @Transactional(rollbackFor = Exception.class)
    public List<TagVO> addTags(Long todoId, TodoTagsDTO dto) {
        Long userId = SecurityUtils.getCurrentUserId();

        // 验证任务归属
        Todo todo = todoMapper.selectById(todoId);
        if (todo == null || !todo.getUserId().equals(userId)) {
            throw new BusinessException("任务不存在");
        }

        // 验证标签归属
        List<Tag> tags = tagMapper.selectList(
                new LambdaQueryWrapper<Tag>()
                        .in(Tag::getId, dto.getTagIds())
                        .eq(Tag::getUserId, userId)
        );

        if (tags.size() != dto.getTagIds().size()) {
            throw new BusinessException("存在无效的标签");
        }

        // 查询现有标签
        List<Long> existingTagIds = todoTagMapper.selectTagIdsByTodoId(todoId);
        Set<Long> existingSet = new HashSet<>(existingTagIds);

        // 添加新标签
        List<TodoTag> newRelations = new ArrayList<>();
        for (Long tagId : dto.getTagIds()) {
            if (!existingSet.contains(tagId)) {
                TodoTag todoTag = new TodoTag();
                todoTag.setTodoId(todoId);
                todoTag.setTagId(tagId);
                newRelations.add(todoTag);
                existingSet.add(tagId);
            }
        }

        if (!newRelations.isEmpty()) {
            newRelations.forEach(todoTagMapper::insert);
        }

        // 返回更新后的标签列表
        return getTagsByTodoId(todoId);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void removeTag(Long todoId, Long tagId) {
        Long userId = SecurityUtils.getCurrentUserId();

        // 验证任务归属
        Todo todo = todoMapper.selectById(todoId);
        if (todo == null || !todo.getUserId().equals(userId)) {
            throw new BusinessException("任务不存在");
        }

        // 删除关联
        todoTagMapper.delete(new LambdaQueryWrapper<TodoTag>()
                .eq(TodoTag::getTodoId, todoId)
                .eq(TodoTag::getTagId, tagId));
    }

    @Override
    public List<TagVO> getTagsByTodoId(Long todoId) {
        Long userId = SecurityUtils.getCurrentUserId();

        // 验证任务归属
        Todo todo = todoMapper.selectById(todoId);
        if (todo == null || !todo.getUserId().equals(userId)) {
            throw new BusinessException("任务不存在");
        }

        List<Tag> tags = tagMapper.selectByTodoId(todoId);
        return tags.stream()
                .map(this::convertToVO)
                .collect(Collectors.toList());
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public List<TagVO> updateTags(Long todoId, TodoTagsDTO dto) {
        Long userId = SecurityUtils.getCurrentUserId();

        // 验证任务归属
        Todo todo = todoMapper.selectById(todoId);
        if (todo == null || !todo.getUserId().equals(userId)) {
            throw new BusinessException("任务不存在");
        }

        // 如果标签列表为空，清空所有标签
        if (dto.getTagIds().isEmpty()) {
            clearTags(todoId);
            return new ArrayList<>();
        }

        // 验证标签归属
        List<Tag> tags = tagMapper.selectList(
                new LambdaQueryWrapper<Tag>()
                        .in(Tag::getId, dto.getTagIds())
                        .eq(Tag::getUserId, userId)
        );

        if (tags.size() != dto.getTagIds().size()) {
            throw new BusinessException("存在无效的标签");
        }

        // 删除现有关联
        todoTagMapper.deleteByTodoId(todoId);

        // 添加新关联
        List<TodoTag> relations = dto.getTagIds().stream()
                .map(tagId -> {
                    TodoTag todoTag = new TodoTag();
                    todoTag.setTodoId(todoId);
                    todoTag.setTagId(tagId);
                    return todoTag;
                })
                .collect(Collectors.toList());

        relations.forEach(todoTagMapper::insert);

        // 返回更新后的标签列表
        return getTagsByTodoId(todoId);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void clearTags(Long todoId) {
        Long userId = SecurityUtils.getCurrentUserId();

        // 验证任务归属
        Todo todo = todoMapper.selectById(todoId);
        if (todo == null || !todo.getUserId().equals(userId)) {
            throw new BusinessException("任务不存在");
        }

        todoTagMapper.deleteByTodoId(todoId);
    }

    /**
     * 转换为VO
     */
    private TagVO convertToVO(Tag tag) {
        TagVO vo = new TagVO();
        BeanUtils.copyProperties(tag, vo);
        return vo;
    }
}
