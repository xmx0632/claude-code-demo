package com.todolist.vo;

import lombok.Data;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

/**
 * 任务VO
 *
 * @author Claude Code
 * @since 2026-03-16
 */
@Data
public class TodoVO {

    private Long id;

    private String title;

    private String description;

    /** 状态: 0-待办, 1-进行中, 2-已完成 */
    private Integer status;

    /** 优先级: 0-低, 1-中, 2-高 */
    private Integer priority;

    private Long categoryId;

    private String categoryName;

    private String categoryColor;

    private LocalDate dueDate;

    private LocalDateTime completedAt;

    private LocalDateTime createdAt;

    private LocalDateTime updatedAt;

    /**
     * 任务标签列表
     */
    private List<TagVO> tags;
}
