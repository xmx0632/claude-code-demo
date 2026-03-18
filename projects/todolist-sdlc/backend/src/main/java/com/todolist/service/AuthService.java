package com.todolist.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.todolist.dto.LoginDTO;
import com.todolist.dto.RegisterDTO;
import com.todolist.entity.User;
import com.todolist.vo.LoginVO;
import com.todolist.vo.UserVO;

/**
 * 认证服务接口
 *
 * @author Claude Code
 * @since 2026-03-16
 */
public interface AuthService extends IService<User> {

    /**
     * 用户注册
     */
    UserVO register(RegisterDTO dto);

    /**
     * 用户登录
     */
    LoginVO login(LoginDTO dto);

    /**
     * 获取当前用户
     */
    UserVO getCurrentUser();

    /**
     * 用户登出
     *
     * @paramToken JWT Token
     */
    void logout(String token);
}
