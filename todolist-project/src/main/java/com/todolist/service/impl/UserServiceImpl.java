package com.todolist.service.impl;

import cn.hutool.core.util.StrUtil;
import com.todolist.common.exception.BusinessException;
import com.todolist.common.util.BeanConv;
import com.todolist.domain.entity.User;
import com.todolist.dto.request.UpdatePasswordDTO;
import com.todolist.dto.request.UserLoginDTO;
import com.todolist.dto.request.UserRegisterDTO;
import com.todolist.dto.response.AuthResponseVO;
import com.todolist.dto.response.UserVO;
import com.todolist.mapper.UserMapper;
import com.todolist.security.SecurityUtils;
import com.todolist.service.IUserService;
import com.todolist.service.JwtService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.LocalDateTime;

/**
 * User Service Implementation
 *
 * @author TodoList Team
 * @since 1.0.0
 */
@Slf4j
@Service
@RequiredArgsConstructor
public class UserServiceImpl implements IUserService {

    private final UserMapper userMapper;
    private final PasswordEncoder passwordEncoder;
    private final JwtService jwtService;

    @Override
    @Transactional(rollbackFor = Exception.class)
    public AuthResponseVO register(UserRegisterDTO dto) {
        // 1. Validate password confirmation
        if (!dto.getPassword().equals(dto.getConfirmPassword())) {
            throw new BusinessException("Passwords do not match");
        }

        // 2. Check username uniqueness
        if (userMapper.existsByUsername(dto.getUsername())) {
            throw new BusinessException(409, "Username already exists");
        }

        // 3. Encrypt password
        String encodedPassword = passwordEncoder.encode(dto.getPassword());

        // 4. Create user
        User user = User.builder()
                .username(dto.getUsername())
                .password(encodedPassword)
                .locked(false)
                .build();
        userMapper.insert(user);

        log.info("User registered successfully: {}", dto.getUsername());

        // 5. Generate tokens
        String token = jwtService.generateToken(user.getId(), user.getUsername());
        String refreshToken = jwtService.generateRefreshToken(user.getId());

        return AuthResponseVO.builder()
                .userId(user.getId())
                .username(user.getUsername())
                .token(token)
                .refreshToken(refreshToken)
                .expiresIn(86400L)
                .build();
    }

    @Override
    public AuthResponseVO login(UserLoginDTO dto) {
        // 1. Query user
        User user = userMapper.selectByUsername(dto.getUsername());
        if (user == null) {
            throw new BusinessException(401, "Invalid username or password");
        }

        // 2. Validate password
        if (!passwordEncoder.matches(dto.getPassword(), user.getPassword())) {
            throw new BusinessException(401, "Invalid username or password");
        }

        // 3. Check account lock status
        if (user.getLocked()) {
            throw new BusinessException(403, "Account is locked");
        }

        // 4. Update last login time
        user.setLastLoginAt(LocalDateTime.now());
        userMapper.updateById(user);

        log.info("User logged in successfully: {}", dto.getUsername());

        // 5. Generate tokens
        String token = jwtService.generateToken(user.getId(), user.getUsername());
        String refreshToken = jwtService.generateRefreshToken(user.getId());

        return AuthResponseVO.builder()
                .userId(user.getId())
                .username(user.getUsername())
                .token(token)
                .refreshToken(refreshToken)
                .expiresIn(86400L)
                .build();
    }

    @Override
    public UserVO getCurrentUserProfile() {
        Long userId = SecurityUtils.getCurrentUserId();
        if (userId == null) {
            throw new BusinessException(401, "User not authenticated");
        }

        User user = userMapper.selectById(userId);
        if (user == null) {
            throw new BusinessException(404, "User not found");
        }

        return BeanConv.convert(user, UserVO.class);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void updatePassword(UpdatePasswordDTO dto) {
        // 1. Validate password confirmation
        if (!dto.getNewPassword().equals(dto.getConfirmPassword())) {
            throw new BusinessException("New passwords do not match");
        }

        // 2. Get current user
        Long userId = SecurityUtils.getCurrentUserId();
        if (userId == null) {
            throw new BusinessException(401, "User not authenticated");
        }

        User user = userMapper.selectById(userId);
        if (user == null) {
            throw new BusinessException(404, "User not found");
        }

        // 3. Validate old password
        if (!passwordEncoder.matches(dto.getOldPassword(), user.getPassword())) {
            throw new BusinessException(400, "Old password is incorrect");
        }

        // 4. Update password
        String encodedPassword = passwordEncoder.encode(dto.getNewPassword());
        user.setPassword(encodedPassword);
        userMapper.updateById(user);

        log.info("Password updated successfully for user: {}", user.getUsername());
    }

    @Override
    public UserVO getByUsername(String username) {
        User user = userMapper.selectByUsername(username);
        if (user == null) {
            throw new BusinessException(404, "User not found");
        }
        return BeanConv.convert(user, UserVO.class);
    }
}
