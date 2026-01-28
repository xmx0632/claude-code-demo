package com.todolist.domain.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * Todo Priority Enum
 *
 * @author TodoList Team
 * @since 1.0.0
 */
@Getter
@AllArgsConstructor
public enum TodoPriority {

    /**
     * High priority
     */
    HIGH("HIGH", "High", 3),

    /**
     * Medium priority
     */
    MEDIUM("MEDIUM", "Medium", 2),

    /**
     * Low priority
     */
    LOW("LOW", "Low", 1);

    /**
     * Priority code
     */
    private final String code;

    /**
     * Priority description
     */
    private final String description;

    /**
     * Weight
     */
    private final Integer weight;

    /**
     * Get enum by code
     *
     * @param code Priority code
     * @return Priority enum
     */
    public static TodoPriority fromCode(String code) {
        for (TodoPriority priority : TodoPriority.values()) {
            if (priority.getCode().equals(code)) {
                return priority;
            }
        }
        throw new IllegalArgumentException("Invalid priority code: " + code);
    }
}
