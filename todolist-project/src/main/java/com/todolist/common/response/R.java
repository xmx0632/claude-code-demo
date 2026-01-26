package com.todolist.common.response;

import com.todolist.common.constant.HttpStatus;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.time.LocalDateTime;

/**
 * Universal Response Object
 *
 * @param <T> Data type
 * @author TodoList Team
 * @since 1.0.0
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Schema(description = "Universal Response")
public class R<T> implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * Status code
     */
    @Schema(description = "Status code")
    private Integer code;

    /**
     * Response message
     */
    @Schema(description = "Response message")
    private String message;

    /**
     * Response data
     */
    @Schema(description = "Response data")
    private T data;

    /**
     * Timestamp
     */
    @Schema(description = "Timestamp")
    private LocalDateTime timestamp;

    /**
     * Success response
     */
    public static <T> R<T> ok() {
        return R.<T>builder()
                .code(HttpStatus.SUCCESS)
                .message("Operation successful")
                .timestamp(LocalDateTime.now())
                .build();
    }

    /**
     * Success response with data
     */
    public static <T> R<T> ok(T data) {
        return R.<T>builder()
                .code(HttpStatus.SUCCESS)
                .message("Operation successful")
                .data(data)
                .timestamp(LocalDateTime.now())
                .build();
    }

    /**
     * Success response with custom message
     */
    public static <T> R<T> ok(String message, T data) {
        return R.<T>builder()
                .code(HttpStatus.SUCCESS)
                .message(message)
                .data(data)
                .timestamp(LocalDateTime.now())
                .build();
    }

    /**
     * Success response with custom code and message
     */
    public static <T> R<T> ok(Integer code, String message, T data) {
        return R.<T>builder()
                .code(code)
                .message(message)
                .data(data)
                .timestamp(LocalDateTime.now())
                .build();
    }

    /**
     * Fail response
     */
    public static <T> R<T> fail(String message) {
        return R.<T>builder()
                .code(HttpStatus.ERROR)
                .message(message)
                .timestamp(LocalDateTime.now())
                .build();
    }

    /**
     * Fail response with custom code
     */
    public static <T> R<T> fail(Integer code, String message) {
        return R.<T>builder()
                .code(code)
                .message(message)
                .timestamp(LocalDateTime.now())
                .build();
    }

    /**
     * Created response
     */
    public static <T> R<T> created(T data) {
        return R.<T>builder()
                .code(HttpStatus.CREATED)
                .message("Created successfully")
                .data(data)
                .timestamp(LocalDateTime.now())
                .build();
    }

    /**
     * Created response with custom message
     */
    public static <T> R<T> created(String message, T data) {
        return R.<T>builder()
                .code(HttpStatus.CREATED)
                .message(message)
                .data(data)
                .timestamp(LocalDateTime.now())
                .build();
    }
}
