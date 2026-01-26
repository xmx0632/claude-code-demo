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
 * User Registration Request DTO
 *
 * @author TodoList Team
 * @since 1.0.0
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Schema(description = "User Registration Request")
public class UserRegisterDTO {

    @Schema(description = "Username", example = "testuser", required = true)
    @NotBlank(message = "Username cannot be empty")
    @Size(min = 3, max = 50, message = "Username length must be between 3-50 characters")
    @Pattern(regexp = "^[a-zA-Z0-9_]+$", message = "Username can only contain letters, numbers, and underscores")
    private String username;

    @Schema(description = "Password", example = "Test1234", required = true)
    @NotBlank(message = "Password cannot be empty")
    @Size(min = 8, max = 20, message = "Password length must be between 8-20 characters")
    @Pattern(regexp = "^(?=.*[A-Za-z])(?=.*\\d).+$", message = "Password must contain letters and numbers")
    private String password;

    @Schema(description = "Confirm Password", example = "Test1234", required = true)
    @NotBlank(message = "Confirm password cannot be empty")
    private String confirmPassword;
}
