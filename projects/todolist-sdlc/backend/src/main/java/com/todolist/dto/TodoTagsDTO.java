package com.todolist.dto;

import jakarta.validation.constraints.NotEmpty;
import lombok.Data;

import java.util.List;

/**
 * 任务标签DTO
 *
 * @author Claude Code
 * @since 2026-03-16
 */
@Data
public class TodoTagsDTO {

    /**
     * 标签ID列表
     */
    @NotEmpty(message = "请选择标签")
    private List<Long> tagIds;
}
