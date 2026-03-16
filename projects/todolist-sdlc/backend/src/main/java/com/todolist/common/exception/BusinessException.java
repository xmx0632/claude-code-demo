package com.todolist.common.exception;

import lombok.Getter;

/**
 * 业务异常
 *
 * @author Claude Code
 * @since 2026-03-16
 */
@Getter
public class BusinessException extends RuntimeException {

    private final Integer code;

    public BusinessException(String message) {
        super(message);
        this.code = 400;
    }

    public BusinessException(Integer code, String message) {
        super(message);
        this.code = code;
    }
}
