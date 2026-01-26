package com.todolist.service;

import com.todolist.common.exception.BusinessException;
import com.todolist.domain.entity.User;
import com.todolist.dto.request.UpdatePasswordDTO;
import com.todolist.dto.request.UserLoginDTO;
import com.todolist.dto.request.UserRegisterDTO;
import com.todolist.dto.response.AuthResponseVO;
import com.todolist.dto.response.UserVO;
import com.todolist.mapper.UserMapper;
import com.todolist.security.SecurityUtils;
import com.todolist.service.impl.UserServiceImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockedStatic;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.time.LocalDateTime;

import static org.assertj.core.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.*;

/**
 * User Service Unit Tests
 *
 * Tests for UserServiceImpl covering:
 * - User registration with validation
 * - User login with authentication
 * - Profile retrieval
 * - Password update
 * - Error handling and edge cases
 *
 * @author TodoList Team
 * @since 1.0.0
 */
@ExtendWith(MockitoExtension.class)
@DisplayName("User Service Tests")
class UserServiceTest {

    @Mock
    private UserMapper userMapper;

    @Mock
    private PasswordEncoder passwordEncoder;

    @Mock
    private JwtService jwtService;

    @InjectMocks
    private UserServiceImpl userService;

    private UserRegisterDTO registerDTO;
    private UserLoginDTO loginDTO;
    private UpdatePasswordDTO updatePasswordDTO;
    private User testUser;

    @BeforeEach
    void setUp() {
        // Arrange: Set up test data
        registerDTO = UserRegisterDTO.builder()
                .username("testuser")
                .password("Password123!")
                .confirmPassword("Password123!")
                .build();

        loginDTO = UserLoginDTO.builder()
                .username("testuser")
                .password("Password123!")
                .build();

        updatePasswordDTO = UpdatePasswordDTO.builder()
                .oldPassword("Password123!")
                .newPassword("NewPassword456!")
                .confirmPassword("NewPassword456!")
                .build();

        testUser = User.builder()
                .id(1L)
                .username("testuser")
                .password("$2a$10$encodedPassword")
                .locked(false)
                .lastLoginAt(LocalDateTime.now())
                .build();
    }

    @Test
    @DisplayName("Should successfully register a new user")
    void register_Success() {
        // Arrange
        when(userMapper.existsByUsername(anyString())).thenReturn(false);
        when(passwordEncoder.encode(anyString())).thenReturn("$2a$10$encodedPassword");
        when(userMapper.insert(any(User.class))).thenAnswer(invocation -> {
            User user = invocation.getArgument(0);
            user.setId(1L);
            return 1;
        });
        when(jwtService.generateToken(anyLong(), anyString())).thenReturn("jwt.token");
        when(jwtService.generateRefreshToken(anyLong())).thenReturn("refresh.token");

        // Act
        AuthResponseVO response = userService.register(registerDTO);

        // Assert
        assertThat(response).isNotNull();
        assertThat(response.getUserId()).isEqualTo(1L);
        assertThat(response.getUsername()).isEqualTo("testuser");
        assertThat(response.getToken()).isEqualTo("jwt.token");
        assertThat(response.getRefreshToken()).isEqualTo("refresh.token");
        assertThat(response.getExpiresIn()).isEqualTo(86400L);

        verify(userMapper).existsByUsername("testuser");
        verify(passwordEncoder).encode("Password123!");
        verify(userMapper).insert(any(User.class));
        verify(jwtService).generateToken(1L, "testuser");
        verify(jwtService).generateRefreshToken(1L);
    }

    @Test
    @DisplayName("Should throw exception when passwords do not match during registration")
    void register_PasswordMismatch() {
        // Arrange
        registerDTO.setConfirmPassword("DifferentPassword");

        // Act & Assert
        assertThatThrownBy(() -> userService.register(registerDTO))
                .isInstanceOf(BusinessException.class)
                .hasMessage("Passwords do not match");

        verify(userMapper, never()).insert(any(User.class));
    }

    @Test
    @DisplayName("Should throw exception when username already exists")
    void register_UsernameAlreadyExists() {
        // Arrange
        when(userMapper.existsByUsername(anyString())).thenReturn(true);

        // Act & Assert
        assertThatThrownBy(() -> userService.register(registerDTO))
                .isInstanceOf(BusinessException.class)
                .hasFieldOrPropertyWithValue("code", 409)
                .hasMessage("Username already exists");

        verify(userMapper).existsByUsername("testuser");
        verify(userMapper, never()).insert(any(User.class));
    }

    @Test
    @DisplayName("Should successfully login with valid credentials")
    void login_Success() {
        // Arrange
        when(userMapper.selectByUsername(anyString())).thenReturn(testUser);
        when(passwordEncoder.matches(anyString(), anyString())).thenReturn(true);
        when(jwtService.generateToken(anyLong(), anyString())).thenReturn("jwt.token");
        when(jwtService.generateRefreshToken(anyLong())).thenReturn("refresh.token");

        // Act
        AuthResponseVO response = userService.login(loginDTO);

        // Assert
        assertThat(response).isNotNull();
        assertThat(response.getUserId()).isEqualTo(1L);
        assertThat(response.getUsername()).isEqualTo("testuser");
        assertThat(response.getToken()).isEqualTo("jwt.token");

        verify(userMapper).selectByUsername("testuser");
        verify(passwordEncoder).matches("Password123!", "$2a$10$encodedPassword");
        verify(userMapper).updateById(any(User.class));
        verify(jwtService).generateToken(1L, "testuser");
    }

    @Test
    @DisplayName("Should throw exception when username does not exist during login")
    void login_UserNotFound() {
        // Arrange
        when(userMapper.selectByUsername(anyString())).thenReturn(null);

        // Act & Assert
        assertThatThrownBy(() -> userService.login(loginDTO))
                .isInstanceOf(BusinessException.class)
                .hasFieldOrPropertyWithValue("code", 401)
                .hasMessage("Invalid username or password");

        verify(userMapper).selectByUsername("testuser");
        verify(passwordEncoder, never()).matches(anyString(), anyString());
    }

    @Test
    @DisplayName("Should throw exception when password is incorrect")
    void login_InvalidPassword() {
        // Arrange
        when(userMapper.selectByUsername(anyString())).thenReturn(testUser);
        when(passwordEncoder.matches(anyString(), anyString())).thenReturn(false);

        // Act & Assert
        assertThatThrownBy(() -> userService.login(loginDTO))
                .isInstanceOf(BusinessException.class)
                .hasFieldOrPropertyWithValue("code", 401)
                .hasMessage("Invalid username or password");

        verify(passwordEncoder).matches("Password123!", "$2a$10$encodedPassword");
        verify(userMapper, never()).updateById(any(User.class));
    }

    @Test
    @DisplayName("Should throw exception when account is locked")
    void login_AccountLocked() {
        // Arrange
        testUser.setLocked(true);
        when(userMapper.selectByUsername(anyString())).thenReturn(testUser);
        when(passwordEncoder.matches(anyString(), anyString())).thenReturn(true);

        // Act & Assert
        assertThatThrownBy(() -> userService.login(loginDTO))
                .isInstanceOf(BusinessException.class)
                .hasFieldOrPropertyWithValue("code", 403)
                .hasMessage("Account is locked");

        verify(userMapper).selectByUsername("testuser");
        verify(userMapper, never()).updateById(any(User.class));
    }

    @Test
    @DisplayName("Should get current user profile successfully")
    void getCurrentUserProfile_Success() {
        // Arrange
        try (MockedStatic<SecurityUtils> securityUtilsMock = mockStatic(SecurityUtils.class)) {
            securityUtilsMock.when(SecurityUtils::getCurrentUserId).thenReturn(1L);
            when(userMapper.selectById(1L)).thenReturn(testUser);

            // Act
            UserVO userVO = userService.getCurrentUserProfile();

            // Assert
            assertThat(userVO).isNotNull();
            assertThat(userVO.getId()).isEqualTo(1L);
            assertThat(userVO.getUsername()).isEqualTo("testuser");

            verify(userMapper).selectById(1L);
        }
    }

    @Test
    @DisplayName("Should throw exception when user not authenticated")
    void getCurrentUserProfile_NotAuthenticated() {
        // Arrange
        try (MockedStatic<SecurityUtils> securityUtilsMock = mockStatic(SecurityUtils.class)) {
            securityUtilsMock.when(SecurityUtils::getCurrentUserId).thenReturn(null);

            // Act & Assert
            assertThatThrownBy(() -> userService.getCurrentUserProfile())
                    .isInstanceOf(BusinessException.class)
                    .hasFieldOrPropertyWithValue("code", 401)
                    .hasMessage("User not authenticated");

            verify(userMapper, never()).selectById(anyLong());
        }
    }

    @Test
    @DisplayName("Should successfully update password")
    void updatePassword_Success() {
        // Arrange
        try (MockedStatic<SecurityUtils> securityUtilsMock = mockStatic(SecurityUtils.class)) {
            securityUtilsMock.when(SecurityUtils::getCurrentUserId).thenReturn(1L);
            when(userMapper.selectById(1L)).thenReturn(testUser);
            when(passwordEncoder.matches(anyString(), anyString())).thenReturn(true);
            when(passwordEncoder.encode(anyString())).thenReturn("$2a$10$newEncodedPassword");

            // Act
            userService.updatePassword(updatePasswordDTO);

            // Assert
            verify(userMapper).selectById(1L);
            verify(passwordEncoder).matches("Password123!", "$2a$10$encodedPassword");
            verify(passwordEncoder).encode("NewPassword456!");
            verify(userMapper).updateById(any(User.class));
        }
    }

    @Test
    @DisplayName("Should throw exception when new passwords do not match")
    void updatePassword_NewPasswordMismatch() {
        // Arrange
        updatePasswordDTO.setConfirmPassword("DifferentPassword");

        // Act & Assert
        assertThatThrownBy(() -> userService.updatePassword(updatePasswordDTO))
                .isInstanceOf(BusinessException.class)
                .hasMessage("New passwords do not match");

        verify(userMapper, never()).selectById(anyLong());
    }

    @Test
    @DisplayName("Should throw exception when old password is incorrect")
    void updatePassword_OldPasswordIncorrect() {
        // Arrange
        try (MockedStatic<SecurityUtils> securityUtilsMock = mockStatic(SecurityUtils.class)) {
            securityUtilsMock.when(SecurityUtils::getCurrentUserId).thenReturn(1L);
            when(userMapper.selectById(1L)).thenReturn(testUser);
            when(passwordEncoder.matches(anyString(), anyString())).thenReturn(false);

            // Act & Assert
            assertThatThrownBy(() -> userService.updatePassword(updatePasswordDTO))
                    .isInstanceOf(BusinessException.class)
                    .hasFieldOrPropertyWithValue("code", 400)
                    .hasMessage("Old password is incorrect");

            verify(userMapper).selectById(1L);
            verify(passwordEncoder).matches("Password123!", "$2a$10$encodedPassword");
            verify(userMapper, never()).updateById(any(User.class));
        }
    }

    @Test
    @DisplayName("Should get user by username successfully")
    void getByUsername_Success() {
        // Arrange
        when(userMapper.selectByUsername("testuser")).thenReturn(testUser);

        // Act
        UserVO userVO = userService.getByUsername("testuser");

        // Assert
        assertThat(userVO).isNotNull();
        assertThat(userVO.getId()).isEqualTo(1L);
        assertThat(userVO.getUsername()).isEqualTo("testuser");

        verify(userMapper).selectByUsername("testuser");
    }

    @Test
    @DisplayName("Should throw exception when user not found by username")
    void getByUsername_NotFound() {
        // Arrange
        when(userMapper.selectByUsername("nonexistent")).thenReturn(null);

        // Act & Assert
        assertThatThrownBy(() -> userService.getByUsername("nonexistent"))
                .isInstanceOf(BusinessException.class)
                .hasFieldOrPropertyWithValue("code", 404)
                .hasMessage("User not found");

        verify(userMapper).selectByUsername("nonexistent");
    }
}
