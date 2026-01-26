package com.todolist.dto.request;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * Refresh Token Request DTO
 *
 * @author TodoList Team
 * @since 1.0.0
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Schema(description = "Refresh Token Request")
public class RefreshTokenDTO {

    @Schema(description = "Refresh Token", required = true)
    @NotBlank(message = "Refresh token cannot be empty")
    private String refreshToken;
}
