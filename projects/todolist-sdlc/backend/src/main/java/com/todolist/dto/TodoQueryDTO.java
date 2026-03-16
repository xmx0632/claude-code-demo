package com.todolist.dto;

import lombok.Data;

import java.util.List;

/**
 * 任务查询DTO
 *
 * @author Claude Code
 * @since 2026-03-16
 */
@Data
public class TodoQueryDTO {

    /** 页码 */
    private Integer page = 1;

    /** 每页数量 */
    private Integer size = 10;

    /** 状态: 0-待办, 1-进行中, 2-已完成 */
    private Integer status;

    /** 优先级: 0-低, 1-中, 2-高 */
    private Integer priority;

    /** 分类ID */
    private Long categoryId;

    /** 搜索关键词 */
    private String keyword;

    /**
     * 标签ID列表（AND逻辑：任务必须同时包含所有选中标签）
     */
    private List<Long> tagIds;
}
