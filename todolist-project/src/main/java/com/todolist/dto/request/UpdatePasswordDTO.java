package com.todolist.dto.request;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * Update Password Request DTO
 *
 * @author TodoList Team
 * @since 1.0.0
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Schema(description = "Update Password Request")
public class UpdatePasswordDTO {

    @Schema(description = "Old Password", required = true)
    @NotBlank(message = "Old password cannot be empty")
    private String oldPassword;

    @Schema(description = "New Password", required = true)
    @NotBlank(message = "New password cannot be empty")
    @Size(min = 8, max = 20, message = "Password length must be between 8-20 characters")
    private String newPassword;

    @Schema(description = "Confirm New Password", required = true)
    @NotBlank(message = "Confirm new password cannot be empty")
    private String confirmPassword;
}
