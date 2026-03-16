package com.todolist.service;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.todolist.common.exception.BusinessException;
import com.todolist.dto.LoginDTO;
import com.todolist.dto.RegisterDTO;
import com.todolist.entity.User;
import com.todolist.mapper.UserMapper;
import com.todolist.security.JwtUtils;
import com.todolist.service.impl.AuthServiceImpl;
import com.todolist.vo.LoginVO;
import com.todolist.vo.UserVO;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.test.util.ReflectionTestUtils;

import java.time.LocalDateTime;
import java.util.Collections;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.*;

/**
 * AuthService 单元测试
 *
 * @author Claude Code
 * @since 2026-03-16
 */
@ExtendWith(MockitoExtension.class)
@DisplayName("认证服务 - 单元测试")
class AuthServiceTest {

    @Mock
    private UserMapper userMapper;

    @Mock
    private PasswordEncoder passwordEncoder;

    @Mock
    private JwtUtils jwtUtils;

    private AuthServiceImpl authService;

    private User testUser;
    private RegisterDTO testRegisterDTO;
    private LoginDTO testLoginDTO;

    @BeforeEach
    void setUp() {
        authService = new AuthServiceImpl(passwordEncoder, jwtUtils);
        ReflectionTestUtils.setField(authService, "baseMapper", userMapper);

        // 创建测试用户
        testUser = new User();
        testUser.setId(1L);
        testUser.setEmail("test@example.com");
        testUser.setPassword("$2a$10$encodedPassword");
        testUser.setNickname("测试用户");
        testUser.setStatus(1);
        testUser.setCreatedAt(LocalDateTime.now());

        // 创建注册DTO
        testRegisterDTO = new RegisterDTO();
        testRegisterDTO.setEmail("new@example.com");
        testRegisterDTO.setPassword("password123");
        testRegisterDTO.setNickname("新用户");

        // 创建登录DTO
        testLoginDTO = new LoginDTO();
        testLoginDTO.setEmail("test@example.com");
        testLoginDTO.setPassword("password123");
    }

    @AfterEach
    void tearDown() {
        SecurityContextHolder.clearContext();
    }

    // ==================== register 测试 ====================

    @Test
    @DisplayName("用户注册 - 成功")
    void register_Success() {
        // Arrange
        lenient().when(userMapper.selectCount(any(LambdaQueryWrapper.class))).thenReturn(0L);
        when(passwordEncoder.encode(anyString())).thenReturn("$2a$10$encodedPassword");
        when(userMapper.insert(any(User.class))).thenAnswer(invocation -> {
            User user = invocation.getArgument(0);
            user.setId(1L);
            return 1;
        });

        // Act
        UserVO result = authService.register(testRegisterDTO);

        // Assert
        assertNotNull(result);
        assertEquals("new@example.com", result.getEmail());
        assertEquals("新用户", result.getNickname());
        verify(userMapper, times(1)).selectCount(any(LambdaQueryWrapper.class));
        verify(passwordEncoder, times(1)).encode("password123");
        verify(userMapper, times(1)).insert(any(User.class));
    }

    @Test
    @DisplayName("用户注册 - 邮箱已存在")
    void register_EmailExists() {
        // Arrange
        lenient().when(userMapper.selectCount(any(LambdaQueryWrapper.class))).thenReturn(1L);

        // Act & Assert
        BusinessException exception = assertThrows(BusinessException.class, () -> {
            authService.register(testRegisterDTO);
        });

        assertEquals("邮箱已被注册", exception.getMessage());
        verify(userMapper, never()).insert(any(User.class));
    }

    @Test
    @DisplayName("用户注册 - 昵称为空时使用邮箱前缀")
    void register_WithoutNickname() {
        // Arrange
        RegisterDTO dto = new RegisterDTO();
        dto.setEmail("user@example.com");
        dto.setPassword("password123");
        // 不设置昵称

        lenient().when(userMapper.selectCount(any(LambdaQueryWrapper.class))).thenReturn(0L);
        when(passwordEncoder.encode(anyString())).thenReturn("$2a$10$encodedPassword");
        when(userMapper.insert(any(User.class))).thenAnswer(invocation -> {
            User user = invocation.getArgument(0);
            user.setId(1L);
            return 1;
        });

        // Act
        UserVO result = authService.register(dto);

        // Assert
        assertNotNull(result);
        assertEquals("user", result.getNickname());
    }

    @Test
    @DisplayName("用户注册 - 设置状态为正常")
    void register_StatusSetToActive() {
        // Arrange
        lenient().when(userMapper.selectCount(any(LambdaQueryWrapper.class))).thenReturn(0L);
        when(passwordEncoder.encode(anyString())).thenReturn("$2a$10$encodedPassword");
        when(userMapper.insert(any(User.class))).thenAnswer(invocation -> {
            User user = invocation.getArgument(0);
            user.setId(1L);
            return 1;
        });

        // Act
        authService.register(testRegisterDTO);

        // Assert
        verify(userMapper, times(1)).insert(argThat(user ->
            user.getStatus() != null && user.getStatus() == 1
        ));
    }

    // ==================== login 测试 ====================

    @Test
    @DisplayName("用户登录 - 成功")
    void login_Success() {
        // Arrange
        lenient().when(userMapper.selectOne(any(LambdaQueryWrapper.class), any(Boolean.class))).thenReturn(testUser);
        when(passwordEncoder.matches(anyString(), anyString())).thenReturn(true);
        when(jwtUtils.generateToken(1L)).thenReturn("jwt-token-123");

        // Act
        LoginVO result = authService.login(testLoginDTO);

        // Assert
        assertNotNull(result);
        assertEquals("jwt-token-123", result.getToken());
        assertNotNull(result.getUser());
        assertEquals(1L, result.getUser().getId());
        assertEquals("test@example.com", result.getUser().getEmail());
        assertEquals("测试用户", result.getUser().getNickname());
    }

    @Test
    @DisplayName("用户登录 - 邮箱不存在")
    void login_EmailNotFound() {
        // Arrange
        lenient().when(userMapper.selectOne(any(LambdaQueryWrapper.class), any(Boolean.class))).thenReturn(null);

        // Act & Assert
        BusinessException exception = assertThrows(BusinessException.class, () -> {
            authService.login(testLoginDTO);
        });

        assertEquals("邮箱或密码错误", exception.getMessage());
        verify(passwordEncoder, never()).matches(anyString(), anyString());
        verify(jwtUtils, never()).generateToken(anyLong());
    }

    @Test
    @DisplayName("用户登录 - 密码错误")
    void login_WrongPassword() {
        // Arrange
        lenient().when(userMapper.selectOne(any(LambdaQueryWrapper.class), any(Boolean.class))).thenReturn(testUser);
        when(passwordEncoder.matches(anyString(), anyString())).thenReturn(false);

        // Act & Assert
        BusinessException exception = assertThrows(BusinessException.class, () -> {
            authService.login(testLoginDTO);
        });

        assertEquals("邮箱或密码错误", exception.getMessage());
        verify(jwtUtils, never()).generateToken(anyLong());
    }

    @Test
    @DisplayName("用户登录 - 账号被禁用")
    void login_AccountDisabled() {
        // Arrange
        testUser.setStatus(0);
        lenient().when(userMapper.selectOne(any(LambdaQueryWrapper.class), any(Boolean.class))).thenReturn(testUser);
        when(passwordEncoder.matches(anyString(), anyString())).thenReturn(true);

        // Act & Assert
        BusinessException exception = assertThrows(BusinessException.class, () -> {
            authService.login(testLoginDTO);
        });

        assertEquals("账号已被禁用", exception.getMessage());
        verify(jwtUtils, never()).generateToken(anyLong());
    }

    @Test
    @DisplayName("用户登录 - 生成Token成功")
    void login_GeneratesToken() {
        // Arrange
        lenient().when(userMapper.selectOne(any(LambdaQueryWrapper.class), any(Boolean.class))).thenReturn(testUser);
        when(passwordEncoder.matches(anyString(), anyString())).thenReturn(true);
        when(jwtUtils.generateToken(1L)).thenReturn("generated-jwt-token");

        // Act
        LoginVO result = authService.login(testLoginDTO);

        // Assert
        assertNotNull(result.getToken());
        assertEquals("generated-jwt-token", result.getToken());
        verify(jwtUtils, times(1)).generateToken(1L);
    }

    // ==================== getCurrentUser 测试 ====================

    @Test
    @DisplayName("获取当前用户 - 成功")
    void getCurrentUser_Success() {
        // Arrange
        SecurityContextHolder.getContext().setAuthentication(
                new UsernamePasswordAuthenticationToken(1L, null, Collections.emptyList())
        );
        when(userMapper.selectById(1L)).thenReturn(testUser);

        // Act
        UserVO result = authService.getCurrentUser();

        // Assert
        assertNotNull(result);
        assertEquals(1L, result.getId());
        assertEquals("test@example.com", result.getEmail());
        assertEquals("测试用户", result.getNickname());
    }

    @Test
    @DisplayName("获取当前用户 - 用户不存在")
    void getCurrentUser_NotFound() {
        // Arrange
        SecurityContextHolder.getContext().setAuthentication(
                new UsernamePasswordAuthenticationToken(999L, null, Collections.emptyList())
        );
        when(userMapper.selectById(999L)).thenReturn(null);

        // Act & Assert
        BusinessException exception = assertThrows(BusinessException.class, () -> {
            authService.getCurrentUser();
        });

        assertEquals("用户不存在", exception.getMessage());
    }

    @Test
    @DisplayName("获取当前用户 - 未登录")
    void getCurrentUser_NotLoggedIn() {
        // Arrange - 不设置安全上下文，模拟未登录状态
        // getCurrentUserId() 返回 null，selectById(null) 返回 null
        when(userMapper.selectById(null)).thenReturn(null);

        // Act & Assert
        BusinessException exception = assertThrows(BusinessException.class, () -> {
            authService.getCurrentUser();
        });

        assertEquals("用户不存在", exception.getMessage());
    }
}
