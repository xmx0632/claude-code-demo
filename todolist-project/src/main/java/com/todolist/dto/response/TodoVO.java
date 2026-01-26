package com.todolist.dto.response;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.List;

/**
 * Todo Response VO
 *
 * @author TodoList Team
 * @since 1.0.0
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Schema(description = "Todo Response")
public class TodoVO {

    @Schema(description = "Todo ID")
    private Long id;

    @Schema(description = "Title")
    private String title;

    @Schema(description = "Description")
    private String description;

    @Schema(description = "Status")
    private String status;

    @Schema(description = "Priority")
    private String priority;

    @Schema(description = "Due Date")
    private LocalDateTime dueDate;

    @Schema(description = "Completion Time")
    private LocalDateTime completedAt;

    @Schema(description = "Category List")
    private List<CategoryVO> categories;

    @Schema(description = "Creation Time")
    private LocalDateTime createdAt;

    @Schema(description = "Update Time")
    private LocalDateTime updatedAt;
}
