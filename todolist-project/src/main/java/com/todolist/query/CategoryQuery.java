package com.todolist.query;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * Category Query Object
 *
 * @author TodoList Team
 * @since 1.0.0
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Schema(description = "Category Query")
public class CategoryQuery {

    @Schema(description = "Sort Field", example = "createdAt")
    private String sortBy;

    @Schema(description = "Sort Order", example = "desc")
    private String sortOrder;
}
