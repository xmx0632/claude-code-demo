package com.todolist.dto.request;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * User Login Request DTO
 *
 * @author TodoList Team
 * @since 1.0.0
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Schema(description = "User Login Request")
public class UserLoginDTO {

    @Schema(description = "Username", example = "testuser", required = true)
    @NotBlank(message = "Username cannot be empty")
    private String username;

    @Schema(description = "Password", example = "Test1234", required = true)
    @NotBlank(message = "Password cannot be empty")
    private String password;
}
