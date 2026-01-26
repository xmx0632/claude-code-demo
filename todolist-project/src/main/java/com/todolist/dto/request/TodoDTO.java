package com.todolist.dto.request;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.List;

/**
 * Todo Request DTO
 *
 * @author TodoList Team
 * @since 1.0.0
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Schema(description = "Todo Request")
public class TodoDTO {

    @Schema(description = "Todo ID (required for update)")
    private Long id;

    @Schema(description = "Title", example = "Complete project documentation", required = true)
    @NotBlank(message = "Title cannot be empty")
    @Size(max = 200, message = "Title length cannot exceed 200 characters")
    private String title;

    @Schema(description = "Description", example = "Write detailed technical documentation")
    @Size(max = 1000, message = "Description length cannot exceed 1000 characters")
    private String description;

    @Schema(description = "Priority", example = "HIGH")
    private String priority;

    @Schema(description = "Due Date", example = "2026-01-30T23:59:59Z")
    private LocalDateTime dueDate;

    @Schema(description = "Category ID List", example = "[1, 2]")
    private List<Long> categoryIds;
}
