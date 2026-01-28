package com.todolist.common.exception;

import com.todolist.common.response.R;
import lombok.extern.slf4j.Slf4j;
import org.springframework.validation.BindException;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.util.stream.Collectors;

/**
 * Global Exception Handler
 *
 * @author TodoList Team
 * @since 1.0.0
 */
@Slf4j
@RestControllerAdvice
public class GlobalExceptionHandler {

    /**
     * Handle business exception
     */
    @ExceptionHandler(BusinessException.class)
    public R<Void> handleBusinessException(BusinessException e) {
        log.error("Business exception: {}", e.getMessage());
        return R.fail(e.getCode(), e.getMessage());
    }

    /**
     * Handle validation exception (RequestBody)
     */
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public R<Void> handleMethodArgumentNotValidException(MethodArgumentNotValidException e) {
        String errors = e.getBindingResult().getFieldErrors().stream()
                .map(FieldError::getDefaultMessage)
                .collect(Collectors.joining(", "));
        log.error("Parameter validation failed: {}", errors);
        return R.fail(400, "Parameter validation failed: " + errors);
    }

    /**
     * Handle validation exception (ModelAttribute)
     */
    @ExceptionHandler(BindException.class)
    public R<Void> handleBindException(BindException e) {
        String errors = e.getBindingResult().getFieldErrors().stream()
                .map(FieldError::getDefaultMessage)
                .collect(Collectors.joining(", "));
        log.error("Parameter validation failed: {}", errors);
        return R.fail(400, "Parameter validation failed: " + errors);
    }

    /**
     * Handle runtime exception
     */
    @ExceptionHandler(RuntimeException.class)
    public R<Void> handleRuntimeException(RuntimeException e) {
        log.error("Runtime exception", e);
        return R.fail(500, "System error: " + e.getMessage());
    }

    /**
     * Handle all other exceptions
     */
    @ExceptionHandler(Exception.class)
    public R<Void> handleException(Exception e) {
        log.error("System exception", e);
        return R.fail(500, "System error, please contact administrator");
    }
}
