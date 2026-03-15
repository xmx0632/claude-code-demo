package com.todolist.entity;

import com.baomidou.mybatisplus.annotation.*;
import lombok.Data;

import java.time.LocalDate;
import java.time.LocalDateTime;

/**
 * 任务实体
 *
 * @author Claude Code
 * @since 2026-03-16
 */
@Data
@TableName("todo")
public class Todo {

    @TableId(type = IdType.AUTO)
    private Long id;

    /** 用户ID */
    private Long userId;

    /** 分类ID */
    private Long categoryId;

    /** 任务标题 */
    private String title;

    /** 任务描述 */
    private String description;

    /** 状态: 0-待办, 1-进行中, 2-已完成 */
    private Integer status;

    /** 优先级: 0-低, 1-中, 2-高 */
    private Integer priority;

    /** 截止日期 */
    private LocalDate dueDate;

    /** 完成时间 */
    private LocalDateTime completedAt;

    /** 创建时间 */
    @TableField(fill = FieldFill.INSERT)
    private LocalDateTime createdAt;

    /** 更新时间 */
    @TableField(fill = FieldFill.INSERT_UPDATE)
    private LocalDateTime updatedAt;
}
