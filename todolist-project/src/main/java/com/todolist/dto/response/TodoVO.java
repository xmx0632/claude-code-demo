package com.todolist.dto.response;

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
public class TodoVO {

    private Long id;

    private String title;

    private String description;

    private String status;

    private String priority;

    private LocalDateTime dueDate;

    private LocalDateTime completedAt;

    private List<CategoryVO> categories;

    private LocalDateTime createdAt;

    private LocalDateTime updatedAt;
}
