package com.todolist.common.response;

import com.baomidou.mybatisplus.core.metadata.IPage;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.util.List;

/**
 * Pagination Response Object
 *
 * @param <T> Data type
 * @author TodoList Team
 * @since 1.0.0
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class TableDataInfo<T> implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * Current page records
     */
    private List<T> records;

    /**
     * Total records
     */
    private Long total;

    /**
     * Current page number
     */
    private Long page;

    /**
     * Page size
     */
    private Long size;

    /**
     * Total pages
     */
    private Long pages;

    /**
     * Build from MyBatis-Plus IPage
     */
    public static <T> TableDataInfo<T> build(IPage<T> page) {
        return TableDataInfo.<T>builder()
                .records(page.getRecords())
                .total(page.getTotal())
                .page(page.getCurrent())
                .size(page.getSize())
                .pages(page.getPages())
                .build();
    }

    /**
     * Build empty result
     */
    public static <T> TableDataInfo<T> empty() {
        return TableDataInfo.<T>builder()
                .records(new java.util.ArrayList<>())
                .total(0L)
                .page(1L)
                .size(20L)
                .pages(0L)
                .build();
    }
}
