package com.example.library.dto.response;

import lombok.Data;

import java.util.List;

/**
 * 分页响应DTO
 *
 * @param <T> 数据类型
 * @author Claude Code
 */
@Data
public class PageResponse<T> {

    /**
     * 数据列表
     */
    private List<T> records;

    /**
     * 总记录数
     */
    private Long total;

    /**
     * 当前页
     */
    private Integer current;

    /**
     * 每页大小
     */
    private Integer size;

    /**
     * 总页数
     */
    private Integer pages;

    /**
     * 从Spring Data分页转换
     */
    public static <T, R> PageResponse<T> of(org.springframework.data.domain.Page<R> page, Class<T> clazz) {
        // 这里需要在Service层进行转换
        PageResponse<T> response = new PageResponse<>();
        response.setCurrent(page.getNumber() + 1);
        response.setSize(page.getSize());
        response.setTotal(page.getTotalElements());
        response.setPages(page.getTotalPages());
        return response;
    }
}
