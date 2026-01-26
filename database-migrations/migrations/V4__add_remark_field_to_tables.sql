-- ========================================
-- V4: 为多个表添加备注字段
-- ========================================
-- 演示如何在同一个迁移中修改多个表

USE ruoyi_example;

-- 用户表添加备注
ALTER TABLE sys_user
ADD COLUMN remark VARCHAR(500) DEFAULT NULL COMMENT '备注';

-- 角色表添加备注
ALTER TABLE sys_role
ADD COLUMN remark VARCHAR(500) DEFAULT NULL COMMENT '备注';

-- 部门表添加备注
ALTER TABLE sys_dept
ADD COLUMN remark VARCHAR(500) DEFAULT NULL COMMENT '备注';
