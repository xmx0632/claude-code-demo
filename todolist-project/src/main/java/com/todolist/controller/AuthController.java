package com.todolist.controller;

import com.todolist.common.response.R;
import com.todolist.dto.request.RefreshTokenDTO;
import com.todolist.dto.request.UserLoginDTO;
import com.todolist.dto.request.UserRegisterDTO;
import com.todolist.dto.response.AuthResponseVO;
import com.todolist.service.IUserService;
import com.todolist.service.JwtService;
import javax.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

/**
 * Authentication Controller
 *
 * @author TodoList Team
 * @since 1.0.0
 */
@RestController
@RequestMapping("/api/v1/auth")
@RequiredArgsConstructor
public class AuthController {

    private final IUserService userService;
    private final JwtService jwtService;

    @PostMapping("/register")
    public R<AuthResponseVO> register(@Valid @RequestBody UserRegisterDTO dto) {
        AuthResponseVO response = userService.register(dto);
        return R.created("Registration successful", response);
    }

    @PostMapping("/login")
    public R<AuthResponseVO> login(@Valid @RequestBody UserLoginDTO dto) {
        AuthResponseVO response = userService.login(dto);
        return R.ok("Login successful", response);
    }

    @PostMapping("/logout")
    public R<Void> logout() {
        // Token is stateless, client-side token removal is sufficient
        return R.ok("Logout successful");
    }

    @PostMapping("/refresh")
    public R<AuthResponseVO> refreshToken(@Valid @RequestBody RefreshTokenDTO dto) {
        // Validate refresh token
        if (!jwtService.validateToken(dto.getRefreshToken())) {
            return R.fail(401, "Invalid or expired refresh token");
        }

        Long userId = jwtService.getUserIdFromToken(dto.getRefreshToken());
        String username = jwtService.getUsernameFromToken(dto.getRefreshToken());

        // Generate new tokens
        String token = jwtService.generateToken(userId, username);
        String refreshToken = jwtService.generateRefreshToken(userId);

        AuthResponseVO response = AuthResponseVO.builder()
                .userId(userId)
                .username(username)
                .token(token)
                .refreshToken(refreshToken)
                .expiresIn(86400L)
                .build();

        return R.ok("Token refreshed successfully", response);
    }
}
