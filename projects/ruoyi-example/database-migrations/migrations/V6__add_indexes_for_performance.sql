-- ========================================
-- V6: 添加性能优化索引
-- ========================================
-- 演示如何为已有表添加索引以提升查询性能

USE ruoyi_example;

-- 用户表：添加邮箱索引（用于登录查询）
CREATE INDEX idx_email ON sys_user(email);

-- 用户表：添加手机号索引（用于登录查询）
CREATE INDEX idx_phonenumber ON sys_user(phonenumber);

-- 用户表：添加复合索引（状态+删除标志+部门ID）
CREATE INDEX idx_status_deleted_dept ON sys_user(status, deleted, dept_id);

-- 部门表：添加状态索引
CREATE INDEX idx_dept_status ON sys_dept(status);

-- 角色表：添加状态索引
CREATE INDEX idx_role_status ON sys_role(status);

-- 操作日志表：添加复合索引（操作时间+业务类型）
CREATE INDEX idx_oper_time_type ON sys_oper_log(oper_time, business_type);

-- 用户角色表：添加覆盖索引优化 JOIN 查询
CREATE INDEX idx_user_role_cover ON sys_user_role(user_id, role_id);
