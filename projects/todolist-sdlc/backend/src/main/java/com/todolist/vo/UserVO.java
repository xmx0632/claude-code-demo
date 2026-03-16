package com.todolist.vo;

import lombok.Data;

import java.time.LocalDateTime;

/**
 * 用户信息VO
 *
 * @author Claude Code
 * @since 2026-03-16
 */
@Data
public class UserVO {

    private Long id;

    private String email;

    private String nickname;

    private String avatar;

    private LocalDateTime createdAt;
}
