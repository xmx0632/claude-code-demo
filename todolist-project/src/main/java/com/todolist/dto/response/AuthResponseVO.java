package com.todolist.dto.response;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * Authentication Response VO
 *
 * @author TodoList Team
 * @since 1.0.0
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Schema(description = "Authentication Response")
public class AuthResponseVO {

    @Schema(description = "User ID")
    private Long userId;

    @Schema(description = "Username")
    private String username;

    @Schema(description = "Access Token")
    private String token;

    @Schema(description = "Refresh Token")
    private String refreshToken;

    @Schema(description = "Token Expiration Time (seconds)")
    private Long expiresIn;
}
