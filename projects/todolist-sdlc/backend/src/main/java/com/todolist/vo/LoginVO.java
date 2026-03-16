package com.todolist.vo;

import lombok.Data;

/**
 * 登录响应VO
 *
 * @author Claude Code
 * @since 2026-03-16
 */
@Data
public class LoginVO {

    /** JWT Token */
    private String token;

    /** 用户信息 */
    private UserVO user;
}
