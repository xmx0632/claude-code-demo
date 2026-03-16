-- V1.0.2__create_todo_tag_table.sql
-- 任务标签关联表（多对多）
-- 功能：实现任务与标签的多对多关联关系
-- 作者：Claude Code
-- 日期：2026-03-16

CREATE TABLE t_todo_tag (
    id BIGINT AUTO_INCREMENT PRIMARY KEY COMMENT '主键ID',
    todo_id BIGINT NOT NULL COMMENT '任务ID',
    tag_id BIGINT NOT NULL COMMENT '标签ID',
    created_at DATETIME DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',

    -- 唯一约束：同一任务不能重复添加同一标签
    UNIQUE KEY uk_todo_tag (todo_id, tag_id),

    -- 索引：按标签ID查询任务列表（用于标签筛选）
    KEY idx_tag_id (tag_id),

    -- 索引：按任务ID查询标签列表（用于显示任务标签）
    KEY idx_todo_id (todo_id),

    -- 外键：关联任务表（级联删除）
    CONSTRAINT fk_todo_tag_todo FOREIGN KEY (todo_id) REFERENCES todo(id) ON DELETE CASCADE,

    -- 外键：关联标签表（级联删除）
    CONSTRAINT fk_todo_tag_tag FOREIGN KEY (tag_id) REFERENCES t_tag(id) ON DELETE CASCADE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='任务标签关联表';
