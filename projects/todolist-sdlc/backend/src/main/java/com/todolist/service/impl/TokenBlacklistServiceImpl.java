package com.todolist.service.impl;

import com.todolist.service.TokenBlacklistService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import java.util.concurrent.ConcurrentHashMap;

/**
 * Token 黑名单服务实现（内存版本）
 * 注意：重启服务后黑名单会清空，生产环境建议使用 Redis
 *
 * @author Claude Code
 * @since 2026-03-19
 */
@Slf4j
@Service
public class TokenBlacklistServiceImpl implements TokenBlacklistService {

    /**
     * 黑名单存储：Token -> 过期时间戳
     */
    private final ConcurrentHashMap<String, Long> blacklist = new ConcurrentHashMap<>();

    @Override
    public void blacklist(String token, long expiryMs) {
        long expiryTime = System.currentTimeMillis() + expiryMs;
        blacklist.put(token, expiryTime);
        log.debug("Token 已加入黑名单，过期时间: {}", expiryTime);
    }

    @Override
    public boolean isBlacklisted(String token) {
        Long expiryTime = blacklist.get(token);
        if (expiryTime == null) {
            return false;
        }

        // 检查是否已过期
        if (System.currentTimeMillis() > expiryTime) {
            blacklist.remove(token);
            return false;
        }

        return true;
    }

    @Override
    @Scheduled(fixedRate = 3600000) // 每小时清理一次
    public void cleanupExpiredTokens() {
        long now = System.currentTimeMillis();
        int removed = 0;
        for (String key : blacklist.keySet()) {
            Long expiryTime = blacklist.get(key);
            if (expiryTime != null && now > expiryTime) {
                blacklist.remove(key);
                removed++;
            }
        }
        if (removed > 0) {
            log.info("清理了 {} 个过期的黑名单 Token", removed);
        }
    }
}
