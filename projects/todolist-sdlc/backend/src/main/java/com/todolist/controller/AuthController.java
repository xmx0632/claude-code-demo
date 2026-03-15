package com.todolist.controller;

import com.todolist.common.response.R;
import com.todolist.dto.LoginDTO;
import com.todolist.dto.RegisterDTO;
import com.todolist.service.AuthService;
import com.todolist.vo.LoginVO;
import com.todolist.vo.UserVO;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

/**
 * 认证控制器
 *
 * @author Claude Code
 * @since 2026-03-16
 */
@RestController
@RequestMapping("/api/auth")
@RequiredArgsConstructor
public class AuthController {

    private final AuthService authService;

    /**
     * 用户注册
     */
    @PostMapping("/register")
    public R<UserVO> register(@Valid @RequestBody RegisterDTO dto) {
        UserVO user = authService.register(dto);
        return R.ok(user, "注册成功");
    }

    /**
     * 用户登录
     */
    @PostMapping("/login")
    public R<LoginVO> login(@Valid @RequestBody LoginDTO dto) {
        LoginVO loginVO = authService.login(dto);
        return R.ok(loginVO, "登录成功");
    }

    /**
     * 获取当前用户信息
     */
    @GetMapping("/info")
    public R<UserVO> info() {
        UserVO user = authService.getCurrentUser();
        return R.ok(user);
    }

    /**
     * 退出登录
     */
    @PostMapping("/logout")
    public R<Void> logout() {
        return R.ok(null, "退出成功");
    }
}
