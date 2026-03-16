package com.todolist.dto;

import lombok.Data;

/**
 * 标签查询DTO
 *
 * @author Claude Code
 * @since 2026-03-16
 */
@Data
public class TagQueryDTO {

    /**
     * 页码
     */
    private Integer page = 1;

    /**
     * 每页数量
     */
    private Integer size = 10;

    /**
     * 标签名称（模糊搜索）
     */
    private String name;

    /**
     * 排序字段
     */
    private String orderBy = "createdAt";

    /**
     * 排序方式
     */
    private String orderDirection = "DESC";
}
