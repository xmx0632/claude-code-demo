-- V1.0.1__create_tag_table.sql
-- 任务标签表
-- 功能：为用户提供任务标签管理功能，支持按标签对任务进行分类和筛选
-- 作者：Claude Code
-- 日期：2026-03-16

CREATE TABLE t_tag (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    user_id BIGINT NOT NULL,
    name VARCHAR(20) NOT NULL,
    color VARCHAR(7) DEFAULT '#999999',
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);

ALTER TABLE t_tag ADD CONSTRAINT uk_user_name UNIQUE (user_id, name);
