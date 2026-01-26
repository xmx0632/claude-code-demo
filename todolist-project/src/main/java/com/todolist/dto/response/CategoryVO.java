package com.todolist.dto.response;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

/**
 * Category Response VO
 *
 * @author TodoList Team
 * @since 1.0.0
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Schema(description = "Category Response")
public class CategoryVO {

    @Schema(description = "Category ID")
    private Long id;

    @Schema(description = "Category Name")
    private String name;

    @Schema(description = "Color")
    private String color;

    @Schema(description = "Todo Count")
    private Integer todoCount;

    @Schema(description = "Creation Time")
    private LocalDateTime createdAt;

    @Schema(description = "Update Time")
    private LocalDateTime updatedAt;
}
