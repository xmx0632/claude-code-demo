package com.todolist.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Data;

import java.time.LocalDate;

/**
 * 任务DTO
 *
 * @author Claude Code
 * @since 2026-03-16
 */
@Data
public class TodoDTO {

    @NotBlank(message = "任务标题不能为空")
    @Size(max = 200, message = "标题最长200字符")
    private String title;

    @Size(max = 2000, message = "描述最长2000字符")
    private String description;

    /** 优先级: 0-低, 1-中, 2-高 */
    private Integer priority;

    /** 分类ID */
    private Long categoryId;

    /** 截止日期 */
    private LocalDate dueDate;
}
