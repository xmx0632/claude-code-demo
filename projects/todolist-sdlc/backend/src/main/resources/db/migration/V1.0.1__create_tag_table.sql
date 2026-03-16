-- V1.0.1__create_tag_table.sql
-- 任务标签表
-- 功能：为用户提供任务标签管理功能，支持按标签对任务进行分类和筛选
-- 作者：Claude Code
-- 日期：2026-03-16

CREATE TABLE t_tag (
    id BIGINT AUTO_INCREMENT PRIMARY KEY COMMENT '主键ID',
    user_id BIGINT NOT NULL COMMENT '用户ID',
    name VARCHAR(20) NOT NULL COMMENT '标签名称',
    color VARCHAR(7) DEFAULT '#999999' COMMENT '标签颜色（HEX格式）',
    created_at DATETIME DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    updated_at DATETIME DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',

    -- 唯一约束：同一用户下标签名唯一
    UNIQUE KEY uk_user_name (user_id, name),

    -- 索引：按用户ID查询标签列表
    KEY idx_user_id (user_id),

    -- 外键：关联用户表
    CONSTRAINT fk_tag_user FOREIGN KEY (user_id) REFERENCES user(id) ON DELETE CASCADE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='任务标签表';
