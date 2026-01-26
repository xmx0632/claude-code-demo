package com.todolist.dto.response;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * User Statistics VO
 *
 * @author TodoList Team
 * @since 1.0.0
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Schema(description = "User Statistics")
public class UserStatisticsVO {

    @Schema(description = "Total Todos")
    private Integer totalTodos;

    @Schema(description = "Completed Todos")
    private Integer completedTodos;

    @Schema(description = "Pending Todos")
    private Integer pendingTodos;

    @Schema(description = "Completion Rate")
    private BigDecimal completionRate;
}
