package com.todolist.domain.entity;

import com.baomidou.mybatisplus.annotation.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.time.LocalDateTime;

/**
 * Todo Entity
 *
 * @author TodoList Team
 * @since 1.0.0
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@TableName("sys_todo")
public class Todo implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * Todo ID
     */
    @TableId(value = "id", type = IdType.AUTO)
    private Long id;

    /**
     * User ID
     */
    @TableField("user_id")
    private Long userId;

    /**
     * Title
     */
    @TableField("title")
    private String title;

    /**
     * Description
     */
    @TableField("description")
    private String description;

    /**
     * Status (PENDING, DONE)
     */
    @TableField("status")
    private String status;

    /**
     * Priority (HIGH, MEDIUM, LOW)
     */
    @TableField("priority")
    private String priority;

    /**
     * Due date
     */
    @TableField("due_date")
    private LocalDateTime dueDate;

    /**
     * Completion timestamp
     */
    @TableField("completed_at")
    private LocalDateTime completedAt;

    /**
     * Version for optimistic locking
     */
    @Version
    @TableField("version")
    private Integer version;

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
