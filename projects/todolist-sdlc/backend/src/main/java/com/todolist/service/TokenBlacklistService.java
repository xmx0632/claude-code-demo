package com.todolist.service;

import java.util.concurrent.ConcurrentHashMap;

/**
 * Token 黑名单服务
 * 用于存储已登出的 Token，防止其继续使用
 *
 * @author Claude Code
 * @since 2026-03-19
 */
public interface TokenBlacklistService {

    /**
     * 将 Token 加入黑名单
     *
     * @param token   Token 字符串
     * @param expiryMs 过期时间（毫秒）
     */
    void blacklist(String token, long expiryMs);

    /**
     * 检查 Token 是否在黑名单中
     *
     * @param token Token 字符串
     * @return true-已加入黑名单，false-未加入
     */
    boolean isBlacklisted(String token);

    /**
     * 清理过期的 Token
     */
    void cleanupExpiredTokens();
}
