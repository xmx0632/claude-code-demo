package com.todolist.vo;

import lombok.Data;

import java.time.LocalDateTime;

/**
 * 标签响应VO
 *
 * @author Claude Code
 * @since 2026-03-16
 */
@Data
public class TagVO {

    /**
     * 标签ID
     */
    private Long id;

    /**
     * 标签名称
     */
    private String name;

    /**
     * 标签颜色
     */
    private String color;

    /**
     * 关联的任务数量
     */
    private Integer taskCount;

    /**
     * 创建时间
     */
    private LocalDateTime createdAt;

    /**
     * 更新时间
     */
    private LocalDateTime updatedAt;
}
