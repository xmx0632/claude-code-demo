package com.todolist.query;

import io.swagger.v3.oas.annotations.media.Schema;
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
@Schema(description = "Todo Query")
public class TodoQuery {

    @Schema(description = "Page Number", example = "1")
    private Integer page;

    @Schema(description = "Page Size", example = "20")
    private Integer size;

    @Schema(description = "Status Filter", example = "PENDING")
    private String status;

    @Schema(description = "Priority Filter", example = "HIGH")
    private String priority;

    @Schema(description = "Due Date Filter", example = "today")
    private String dueDateFilter;

    @Schema(description = "Category ID", example = "1")
    private Long categoryId;

    @Schema(description = "Search Keyword")
    private String keyword;

    @Schema(description = "Sort Field", example = "createdAt")
    private String sortBy;

    @Schema(description = "Sort Order", example = "desc")
    private String sortOrder;
}
