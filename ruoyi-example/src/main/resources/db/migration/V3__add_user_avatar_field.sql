-- ========================================
-- V3: 为用户表添加头像字段
-- ========================================
-- 这是一个增量变更的示例
-- 使用 ALTER TABLE 添加新字段，而不是重建表

USE ruoyi_example;

-- 添加头像字段
ALTER TABLE sys_user
ADD COLUMN avatar VARCHAR(255) DEFAULT '' COMMENT '用户头像';

-- 添加索引
CREATE INDEX idx_avatar ON sys_user(avatar);

-- 为现有用户设置默认头像
UPDATE sys_user
SET avatar = '/default-avatar.png'
WHERE avatar = '' OR avatar IS NULL;
