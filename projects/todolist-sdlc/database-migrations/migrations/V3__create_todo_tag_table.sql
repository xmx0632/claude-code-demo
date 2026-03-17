-- V3__create_todo_tag_table.sql
-- 任务标签关联表（多对多）
-- 功能：实现任务与标签的多对多关联关系
-- 作者：Claude Code
-- 日期：2026-03-16

CREATE TABLE t_todo_tag (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    todo_id BIGINT NOT NULL,
    tag_id BIGINT NOT NULL,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);

ALTER TABLE t_todo_tag ADD CONSTRAINT uk_todo_tag UNIQUE (todo_id, tag_id);
