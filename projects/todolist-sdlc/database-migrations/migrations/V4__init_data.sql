-- =============================================
-- Flyway Migration: V4__init_data.sql
-- 描述: 初始化测试数据
-- 作者: Claude Code
-- 日期: 2026-03-16
-- =============================================

-- 插入测试用户
-- 邮箱: test@test.com
-- 密码: 123456 (BCrypt加密)
INSERT INTO t_user (email, password, nickname, status) VALUES
('test@test.com', '$2a$10$ixwGGvf8cXW.zgYxmDJh1.JkUJeHDMB8tvdH7aoH7Lz97tlAX9EiG', '测试用户', 1);

-- 插入测试标签（为测试用户准备）
INSERT INTO t_tag (user_id, name, color) VALUES
(1, '工作', '#FF6B6B'),
(1, '个人', '#4ECDC4'),
(1, '重要', '#45B7D1'),
(1, '紧急', '#EF4444'),
(1, '学习', '#BB8FCE');

-- 插入测试分类
INSERT INTO t_category (user_id, name, color, sort_order) VALUES
(1, '工作', '#409EFF', 1),
(1, '生活', '#67C23A', 2),
(1, '学习', '#E6A23C', 3);

-- 插入测试任务
INSERT INTO t_todo (user_id, category_id, title, description, status, priority, due_date) VALUES
(1, 1, '完成项目需求文档并评审', '需要完成用户管理模块的详细需求文档，包括接口定义和数据模型设计', 0, 2, '2026-03-20'),
(1, 1, '修复登录页面样式问题', '登录按钮在移动端显示不正确，需要调整响应式布局', 1, 2, '2026-03-18'),
(1, 2, '购买生活用品', '周末去超市采购：牛奶、面包、水果、洗洁精', 0, 1, '2026-03-17'),
(1, 3, '学习 Spring Boot 3.2 新特性', '重点学习虚拟线程、观察者模式改进', 0, 1, '2026-03-25'),
(1, 1, '优化数据库查询性能', '任务列表查询较慢，需要添加索引和优化SQL', 0, 2, '2026-03-22');

-- 为测试任务添加标签
INSERT INTO t_todo_tag (todo_id, tag_id) VALUES
-- 任务1: 工作 + 重要
(1, 1), (1, 3),
-- 任务2: 工作 + 重要 + 紧急
(2, 1), (2, 3), (2, 4),
-- 任务3: 个人
(3, 2),
-- 任务4: 学习
(4, 5),
-- 任务5: 工作 + 重要
(5, 1), (5, 3);
