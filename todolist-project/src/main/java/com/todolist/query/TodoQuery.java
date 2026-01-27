package com.todolist.query;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * Todo Query Object
 *
 * @author TodoList Team
 * @since 1.0.0
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class TodoQuery {

    private Integer page;

    private Integer size;

    private String status;

    private String priority;

    private String dueDateFilter;

    private Long categoryId;

    private String keyword;

    private String sortBy;

    private String sortOrder;
}
