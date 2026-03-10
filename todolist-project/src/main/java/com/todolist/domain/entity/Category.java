package com.todolist.domain.entity;

import com.baomidou.mybatisplus.annotation.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.time.LocalDateTime;

/**
 * Category Entity
 *
 * @author TodoList Team
 * @since 1.0.0
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@TableName("sys_category")
public class Category implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * Category ID
     */
    @TableId(value = "id", type = IdType.AUTO)
    private Long id;

    /**
     * User ID
     */
    @TableField("user_id")
    private Long userId;

    /**
     * Category name
     */
    @TableField("name")
    private String name;

    /**
     * Color (hex format)
     */
    @TableField("color")
    private String color;

    /**
     * Creation timestamp
     */
    @TableField(value = "created_at", fill = FieldFill.INSERT)
    private LocalDateTime createdAt;

    /**
     * Update timestamp
     */
    @TableField(value = "updated_at", fill = FieldFill.INSERT_UPDATE)
    private LocalDateTime updatedAt;

    /**
     * Soft delete flag (0-active, 1-deleted)
     */
    @TableLogic
    @TableField("deleted")
    private Integer deleted;
}
