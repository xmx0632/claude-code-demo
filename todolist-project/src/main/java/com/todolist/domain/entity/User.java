package com.todolist.domain.entity;

import com.baomidou.mybatisplus.annotation.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.time.LocalDateTime;

/**
 * User Entity
 *
 * @author TodoList Team
 * @since 1.0.0
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@TableName("sys_user")
public class User implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * User ID
     */
    @TableId(value = "id", type = IdType.AUTO)
    private Long id;

    /**
     * Username
     */
    @TableField("username")
    private String username;

    /**
     * Password (BCrypt encrypted)
     */
    @TableField("password")
    private String password;

    /**
     * Account locked status
     */
    @TableField("locked")
    private Boolean locked;

    /**
     * Lock timestamp
     */
    @TableField("locked_at")
    private LocalDateTime lockedAt;

    /**
     * Last login timestamp
     */
    @TableField("last_login_at")
    private LocalDateTime lastLoginAt;

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
