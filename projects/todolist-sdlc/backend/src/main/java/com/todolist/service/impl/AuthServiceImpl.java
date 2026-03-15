package com.todolist.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.todolist.common.exception.BusinessException;
import com.todolist.dto.LoginDTO;
import com.todolist.dto.RegisterDTO;
import com.todolist.entity.User;
import com.todolist.mapper.UserMapper;
import com.todolist.security.JwtUtils;
import com.todolist.service.AuthService;
import com.todolist.util.SecurityUtils;
import com.todolist.vo.LoginVO;
import com.todolist.vo.UserVO;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.BeanUtils;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

/**
 * 认证服务实现
 *
 * @author Claude Code
 * @since 2026-03-16
 */
@Service
@RequiredArgsConstructor
public class AuthServiceImpl extends ServiceImpl<UserMapper, User> implements AuthService {

    private final PasswordEncoder passwordEncoder;
    private final JwtUtils jwtUtils;

    @Override
    public UserVO register(RegisterDTO dto) {
        // 检查邮箱是否已注册
        Long count = this.lambdaQuery()
                .eq(User::getEmail, dto.getEmail())
                .count();
        if (count > 0) {
            throw new BusinessException("邮箱已被注册");
        }

        // 创建用户
        User user = new User();
        user.setEmail(dto.getEmail());
        user.setPassword(passwordEncoder.encode(dto.getPassword()));
        user.setNickname(dto.getNickname() != null ? dto.getNickname() : dto.getEmail().split("@")[0]);
        user.setStatus(1);

        this.save(user);

        return convertToVO(user);
    }

    @Override
    public LoginVO login(LoginDTO dto) {
        // 查询用户
        User user = this.getOne(new LambdaQueryWrapper<User>()
                .eq(User::getEmail, dto.getEmail()));

        if (user == null) {
            throw new BusinessException("邮箱或密码错误");
        }

        // 验证密码
        if (!passwordEncoder.matches(dto.getPassword(), user.getPassword())) {
            throw new BusinessException("邮箱或密码错误");
        }

        // 检查状态
        if (user.getStatus() != 1) {
            throw new BusinessException("账号已被禁用");
        }

        // 生成Token
        String token = jwtUtils.generateToken(user.getId());

        // 构建返回
        LoginVO loginVO = new LoginVO();
        loginVO.setToken(token);
        loginVO.setUser(convertToVO(user));

        return loginVO;
    }

    @Override
    public UserVO getCurrentUser() {
        Long userId = SecurityUtils.getCurrentUserId();
        User user = this.getById(userId);
        if (user == null) {
            throw new BusinessException("用户不存在");
        }
        return convertToVO(user);
    }

    private UserVO convertToVO(User user) {
        UserVO vo = new UserVO();
        BeanUtils.copyProperties(user, vo);
        return vo;
    }
}
