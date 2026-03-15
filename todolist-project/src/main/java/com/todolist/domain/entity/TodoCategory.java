package com.todolist.domain.entity;

import com.baomidou.mybatisplus.annotation.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.time.LocalDateTime;

/**
 * Todo-Category Association Entity
 *
 * @author TodoList Team
 * @since 1.0.0
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@TableName("sys_todo_category")
public class TodoCategory implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * Association ID
     */
    @TableId(value = "id", type = IdType.AUTO)
    private Long id;

    /**
     * Todo ID
     */
    @TableField("todo_id")
    private Long todoId;

    /**
     * Category ID
     */
    @TableField("category_id")
    private Long categoryId;

    /**
     * Creation timestamp
     */
    @TableField(value = "created_at", fill = FieldFill.INSERT)
    private LocalDateTime createdAt;
}
