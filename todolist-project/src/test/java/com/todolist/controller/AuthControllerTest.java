package com.todolist.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.todolist.dto.request.RefreshTokenDTO;
import com.todolist.dto.request.UserLoginDTO;
import com.todolist.dto.request.UserRegisterDTO;
import com.todolist.dto.response.AuthResponseVO;
import com.todolist.service.IUserService;
import com.todolist.service.JwtService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

/**
 * Authentication Controller Integration Tests
 *
 * Tests for AuthController covering:
 * - User registration endpoint
 * - User login endpoint
 * - Token refresh endpoint
 * - Logout endpoint
 * - Request validation and error handling
 *
 * @author TodoList Team
 * @since 1.0.0
 */
@WebMvcTest(AuthController.class)
@DisplayName("Authentication Controller Tests")
class AuthControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockBean
    private IUserService userService;

    @MockBean
    private JwtService jwtService;

    private UserRegisterDTO registerDTO;
    private UserLoginDTO loginDTO;
    private RefreshTokenDTO refreshTokenDTO;
    private AuthResponseVO authResponse;

    @BeforeEach
    void setUp() {
        // Set up test data
        registerDTO = UserRegisterDTO.builder()
                .username("testuser")
                .password("Password123!")
                .confirmPassword("Password123!")
                .build();

        loginDTO = UserLoginDTO.builder()
                .username("testuser")
                .password("Password123!")
                .build();

        refreshTokenDTO = RefreshTokenDTO.builder()
                .refreshToken("refresh.token.string")
                .build();

        authResponse = AuthResponseVO.builder()
                .userId(1L)
                .username("testuser")
                .token("jwt.access.token")
                .refreshToken("refresh.token.string")
                .expiresIn(86400L)
                .build();
    }

    @Test
    @DisplayName("Should successfully register a new user")
    void register_Success() throws Exception {
        // Arrange
        when(userService.register(any(UserRegisterDTO.class))).thenReturn(authResponse);

        // Act & Assert
        mockMvc.perform(post("/api/v1/auth/register")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(registerDTO)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.code").value(201))
                .andExpect(jsonPath("$.message").value("Registration successful"))
                .andExpect(jsonPath("$.data.userId").value(1L))
                .andExpect(jsonPath("$.data.username").value("testuser"))
                .andExpect(jsonPath("$.data.token").value("jwt.access.token"))
                .andExpect(jsonPath("$.data.refreshToken").value("refresh.token.string"))
                .andExpect(jsonPath("$.data.expiresIn").value(86400L));
    }

    @Test
    @DisplayName("Should fail registration with missing username")
    void register_MissingUsername() throws Exception {
        // Arrange
        registerDTO.setUsername(null);

        // Act & Assert
        mockMvc.perform(post("/api/v1/auth/register")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(registerDTO)))
                .andExpect(status().isBadRequest());
    }

    @Test
    @DisplayName("Should fail registration with missing password")
    void register_MissingPassword() throws Exception {
        // Arrange
        registerDTO.setPassword(null);

        // Act & Assert
        mockMvc.perform(post("/api/v1/auth/register")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(registerDTO)))
                .andExpect(status().isBadRequest());
    }

    @Test
    @DisplayName("Should successfully login user")
    void login_Success() throws Exception {
        // Arrange
        when(userService.login(any(UserLoginDTO.class))).thenReturn(authResponse);

        // Act & Assert
        mockMvc.perform(post("/api/v1/auth/login")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(loginDTO)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.code").value(200))
                .andExpect(jsonPath("$.message").value("Login successful"))
                .andExpect(jsonPath("$.data.userId").value(1L))
                .andExpect(jsonPath("$.data.username").value("testuser"))
                .andExpect(jsonPath("$.data.token").value("jwt.access.token"));
    }

    @Test
    @DisplayName("Should fail login with missing username")
    void login_MissingUsername() throws Exception {
        // Arrange
        loginDTO.setUsername(null);

        // Act & Assert
        mockMvc.perform(post("/api/v1/auth/login")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(loginDTO)))
                .andExpect(status().isBadRequest());
    }

    @Test
    @DisplayName("Should fail login with missing password")
    void login_MissingPassword() throws Exception {
        // Arrange
        loginDTO.setPassword(null);

        // Act & Assert
        mockMvc.perform(post("/api/v1/auth/login")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(loginDTO)))
                .andExpect(status().isBadRequest());
    }

    @Test
    @DisplayName("Should successfully logout user")
    void logout_Success() throws Exception {
        // Act & Assert
        mockMvc.perform(post("/api/v1/auth/logout"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.code").value(200))
                .andExpect(jsonPath("$.message").value("Logout successful"));
    }

    @Test
    @DisplayName("Should successfully refresh token")
    void refreshToken_Success() throws Exception {
        // Arrange
        when(jwtService.validateToken(anyString())).thenReturn(true);
        when(jwtService.getUserIdFromToken(anyString())).thenReturn(1L);
        when(jwtService.getUsernameFromToken(anyString())).thenReturn("testuser");
        when(jwtService.generateToken(anyLong(), anyString())).thenReturn("new.jwt.token");
        when(jwtService.generateRefreshToken(anyLong())).thenReturn("new.refresh.token");

        // Act & Assert
        mockMvc.perform(post("/api/v1/auth/refresh")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(refreshTokenDTO)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.code").value(200))
                .andExpect(jsonPath("$.message").value("Token refreshed successfully"))
                .andExpect(jsonPath("$.data.userId").value(1L))
                .andExpect(jsonPath("$.data.username").value("testuser"))
                .andExpect(jsonPath("$.data.token").value("new.jwt.token"))
                .andExpect(jsonPath("$.data.refreshToken").value("new.refresh.token"));
    }

    @Test
    @DisplayName("Should fail token refresh with invalid token")
    void refreshToken_InvalidToken() throws Exception {
        // Arrange
        when(jwtService.validateToken(anyString())).thenReturn(false);

        // Act & Assert
        mockMvc.perform(post("/api/v1/auth/refresh")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(refreshTokenDTO)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.code").value(401))
                .andExpect(jsonPath("$.message").value("Invalid or expired refresh token"));
    }

    @Test
    @DisplayName("Should fail token refresh with missing refresh token")
    void refreshToken_MissingRefreshToken() throws Exception {
        // Arrange
        refreshTokenDTO.setRefreshToken(null);

        // Act & Assert
        mockMvc.perform(post("/api/v1/auth/refresh")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(refreshTokenDTO)))
                .andExpect(status().isBadRequest());
    }

    @Test
    @DisplayName("Should return 404 for non-existent endpoints")
    void nonExistentEndpoint() throws Exception {
        // Act & Assert
        mockMvc.perform(post("/api/v1/auth/nonexistent")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isNotFound());
    }

    @Test
    @DisplayName("Should handle empty request body")
    void register_EmptyBody() throws Exception {
        // Act & Assert
        mockMvc.perform(post("/api/v1/auth/register")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{}"))
                .andExpect(status().isBadRequest());
    }

    @Test
    @DisplayName("Should handle malformed JSON")
    void register_MalformedJson() throws Exception {
        // Act & Assert
        mockMvc.perform(post("/api/v1/auth/register")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{invalid json}"))
                .andExpect(status().isBadRequest());
    }
}
