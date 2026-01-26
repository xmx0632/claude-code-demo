package com.todolist.domain.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * Todo Status Enum
 *
 * @author TodoList Team
 * @since 1.0.0
 */
@Getter
@AllArgsConstructor
public enum TodoStatus {

    /**
     * Pending
     */
    PENDING("PENDING", "Pending"),

    /**
     * Completed
     */
    DONE("DONE", "Done");

    /**
     * Status code
     */
    private final String code;

    /**
     * Status description
     */
    private final String description;

    /**
     * Get enum by code
     *
     * @param code Status code
     * @return Status enum
     */
    public static TodoStatus fromCode(String code) {
        for (TodoStatus status : TodoStatus.values()) {
            if (status.getCode().equals(code)) {
                return status;
            }
        }
        throw new IllegalArgumentException("Invalid status code: " + code);
    }
}
