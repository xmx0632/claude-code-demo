package com.todolist.dto.request;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * Category Request DTO
 *
 * @author TodoList Team
 * @since 1.0.0
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Schema(description = "Category Request")
public class CategoryDTO {

    @Schema(description = "Category ID (required for update)")
    private Long id;

    @Schema(description = "Category Name", example = "Work", required = true)
    @NotBlank(message = "Category name cannot be empty")
    @Size(min = 1, max = 50, message = "Category name length must be between 1-50 characters")
    private String name;

    @Schema(description = "Color", example = "#FF0000")
    @Pattern(regexp = "^#[0-9A-Fa-f]{6}$", message = "Invalid color format, must be hex color code")
    private String color;
}
