package com.todolist.dto.request;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;
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
public class TodoDTO {

    private Long id;

    @NotBlank(message = "Title cannot be empty")
    @Size(max = 200, message = "Title length cannot exceed 200 characters")
    private String title;

    @Size(max = 1000, message = "Description length cannot exceed 1000 characters")
    private String description;

    private String priority;

    private LocalDateTime dueDate;

    private List<Long> categoryIds;
}
