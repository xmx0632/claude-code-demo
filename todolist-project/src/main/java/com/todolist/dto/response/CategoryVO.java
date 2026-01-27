package com.todolist.dto.response;

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
public class CategoryVO {

    private Long id;

    private String name;

    private String color;

    private Integer todoCount;

    private LocalDateTime createdAt;

    private LocalDateTime updatedAt;
}
