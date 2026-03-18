package com.todolist.service;

import com.todolist.dto.TodoTagsDTO;
import com.todolist.vo.TagVO;

import java.util.List;

/**
 * 任务标签关联服务接口
 *
 * @author Claude Code
 * @since 2026-03-16
 */
public interface TodoTagService {

    /**
     * 为任务添加单个标签
     *
     * @param todoId 任务ID
     * @param tagId  标签ID
     * @return 标签列表
     */
    List<TagVO> addTag(Long todoId, Long tagId);

    /**
     * 为任务批量添加标签
     *
     * @param todoId 任务ID
     * @param dto    标签DTO
     * @return 标签列表
     */
    List<TagVO> addTags(Long todoId, TodoTagsDTO dto);

    /**
     * 移除任务标签
     *
     * @param todoId 任务ID
     * @param tagId  标签ID
     */
    void removeTag(Long todoId, Long tagId);

    /**
     * 查询任务的所有标签
     *
     * @param todoId 任务ID
     * @return 标签列表
     */
    List<TagVO> getTagsByTodoId(Long todoId);

    /**
     * 批量更新任务标签（完全替换）
     *
     * @param todoId 任务ID
     * @param dto    标签DTO
     * @return 标签列表
     */
    List<TagVO> updateTags(Long todoId, TodoTagsDTO dto);

    /**
     * 清空任务的所有标签
     *
     * @param todoId 任务ID
     */
    void clearTags(Long todoId);
}
