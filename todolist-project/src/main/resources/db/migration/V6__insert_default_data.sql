-- =====================================================
-- TodoList Database Migration Script
-- Version: V6
-- Description: Insert sample data for demonstration and testing
-- Author: Database Administrator
-- Date: 2026-01-26
-- =====================================================
-- This script inserts sample todo items and category associations
-- for the admin user to demonstrate the system functionality.
-- The data represents typical use cases for a personal task
-- management application.
-- =====================================================

USE `todolist`;

-- Start transaction for data consistency
START TRANSACTION;

-- =====================================================
-- Insert Sample Todos
-- =====================================================
-- These sample todos demonstrate various states, priorities,
-- and due dates. All belong to the admin user (user_id = 1).

-- Insert sample todos
INSERT INTO `sys_todo`
  (`user_id`, `title`, `description`, `status`, `priority`, `due_date`, `completed_at`, `created_at`, `updated_at`)
VALUES
  -- High priority pending todos
  (1, '完成项目需求文档', '编写详细的产品需求文档，包括功能规格说明', 'PENDING', 'HIGH', DATE_ADD(NOW(), INTERVAL 2 DAY), NULL, NOW(), NOW()),

  (1, '修复登录页面 Bug', '用户反馈在移动端登录页面布局异常，需要紧急修复', 'PENDING', 'HIGH', DATE_ADD(NOW(), INTERVAL 1 DAY), NULL, NOW(), NOW()),

  -- Medium priority pending todos
  (1, '代码审查', '审查团队成员提交的 Pull Request，确保代码质量', 'PENDING', 'MEDIUM', DATE_ADD(NOW(), INTERVAL 3 DAY), NULL, NOW(), NOW()),

  (1, '更新数据库设计文档', '根据最新的需求变更更新数据库设计文档', 'PENDING', 'MEDIUM', DATE_ADD(NOW(), INTERVAL 5 DAY), NULL, NOW(), NOW()),

  (1, '准备周会演示', '准备周五团队会议的项目演示文稿', 'PENDING', 'MEDIUM', DATE_ADD(NOW(), INTERVAL 4 DAY), NULL, NOW(), NOW()),

  -- Low priority pending todos
  (1, '整理技术博客', '整理最近学到的技术知识点，准备写博客文章', 'PENDING', 'LOW', DATE_ADD(NOW(), INTERVAL 7 DAY), NULL, NOW(), NOW()),

  (1, '学习 Spring Boot 3.0 新特性', '学习 Spring Boot 3.0 的新功能和最佳实践', 'PENDING', 'LOW', DATE_ADD(NOW(), INTERVAL 10 DAY), NULL, NOW(), NOW()),

  -- Completed todos (for demonstration)
  (1, '搭建开发环境', '配置本地开发环境，安装 JDK、Maven、MySQL 等工具', 'DONE', 'HIGH', DATE_SUB(NOW(), INTERVAL 5 DAY), DATE_SUB(NOW(), INTERVAL 5 DAY), DATE_SUB(NOW(), INTERVAL 7 DAY), NOW()),

  (1, '创建项目仓库', '在 GitLab 上创建项目仓库并配置 CI/CD 流水线', 'DONE', 'HIGH', DATE_SUB(NOW(), INTERVAL 4 DAY), DATE_SUB(NOW(), INTERVAL 4 DAY), DATE_SUB(NOW(), INTERVAL 6 DAY), NOW()),

  (1, '设计系统架构', '设计系统整体架构，确定技术栈和模块划分', 'DONE', 'MEDIUM', DATE_SUB(NOW(), INTERVAL 3 DAY), DATE_SUB(NOW(), INTERVAL 3 DAY), DATE_SUB(NOW(), INTERVAL 5 DAY), NOW());

-- =====================================================
-- Insert Todo-Category Associations
-- =====================================================
-- Associate todos with categories to demonstrate the
-- many-to-many relationship functionality.

-- Get the todo IDs we just inserted (they should be 1-10)
-- We'll use variables to make the code more readable

-- Association 1: Todo 1 -> 工作 (category 1)
INSERT INTO `sys_todo_category` (`todo_id`, `category_id`, `created_at`)
VALUES (1, 1, NOW())
ON DUPLICATE KEY UPDATE `created_at` = `created_at`;

-- Association 2: Todo 1 -> 学习 (category 3)
INSERT INTO `sys_todo_category` (`todo_id`, `category_id`, `created_at`)
VALUES (1, 3, NOW())
ON DUPLICATE KEY UPDATE `created_at` = `created_at`;

-- Association 3: Todo 2 -> 工作 (category 1)
INSERT INTO `sys_todo_category` (`todo_id`, `category_id`, `created_at`)
VALUES (2, 1, NOW())
ON DUPLICATE KEY UPDATE `created_at` = `created_at`;

-- Association 4: Todo 3 -> 工作 (category 1)
INSERT INTO `sys_todo_category` (`todo_id`, `category_id`, `created_at`)
VALUES (3, 1, NOW())
ON DUPLICATE KEY UPDATE `created_at` = `created_at`;

-- Association 5: Todo 4 -> 工作 (category 1)
INSERT INTO `sys_todo_category` (`todo_id`, `category_id`, `created_at`)
VALUES (4, 1, NOW())
ON DUPLICATE KEY UPDATE `created_at` = `created_at`;

-- Association 6: Todo 4 -> 学习 (category 3)
INSERT INTO `sys_todo_category` (`todo_id`, `category_id`, `created_at`)
VALUES (4, 3, NOW())
ON DUPLICATE KEY UPDATE `created_at` = `created_at`;

-- Association 7: Todo 5 -> 工作 (category 1)
INSERT INTO `sys_todo_category` (`todo_id`, `category_id`, `created_at`)
VALUES (5, 1, NOW())
ON DUPLICATE KEY UPDATE `created_at` = `created_at`;

-- Association 8: Todo 6 -> 个人 (category 2)
INSERT INTO `sys_todo_category` (`todo_id`, `category_id`, `created_at`)
VALUES (6, 2, NOW())
ON DUPLICATE KEY UPDATE `created_at` = `created_at`;

-- Association 9: Todo 7 -> 学习 (category 3)
INSERT INTO `sys_todo_category` (`todo_id`, `category_id`, `created_at`)
VALUES (7, 3, NOW())
ON DUPLICATE KEY UPDATE `created_at` = `created_at`;

-- Association 10: Todo 8 -> 工作 (category 1)
INSERT INTO `sys_todo_category` (`todo_id`, `category_id`, `created_at`)
VALUES (8, 1, NOW())
ON DUPLICATE KEY UPDATE `created_at` = `created_at`;

-- Association 11: Todo 9 -> 工作 (category 1)
INSERT INTO `sys_todo_category` (`todo_id`, `category_id`, `created_at`)
VALUES (9, 1, NOW())
ON DUPLICATE KEY UPDATE `created_at` = `created_at`;

-- Association 12: Todo 10 -> 工作 (category 1)
INSERT INTO `sys_todo_category` (`todo_id`, `category_id`, `created_at`)
VALUES (10, 1, NOW())
ON DUPLICATE KEY UPDATE `created_at` = `created_at`;

-- Association 13: Todo 10 -> 学习 (category 3)
INSERT INTO `sys_todo_category` (`todo_id`, `category_id`, `created_at`)
VALUES (10, 3, NOW())
ON DUPLICATE KEY UPDATE `created_at` = `created_at`;

-- Commit transaction
COMMIT;

-- =====================================================
-- Sample Data Summary:
-- =====================================================
-- Total todos: 10
-- - Pending: 7 (High: 2, Medium: 3, Low: 2)
-- - Completed: 3 (High: 2, Medium: 1)
--
-- Category associations: 13
-- - 工作 (Work): 9 todos
-- - 个人 (Personal): 1 todo
-- - 学习 (Learning): 4 todos
--
-- Some todos have multiple categories (todos 1, 4, 10)
-- =====================================================

-- =====================================================
-- Verification Queries:
-- =====================================================
-- Check all todos:
-- SELECT id, title, status, priority, due_date, created_at
-- FROM sys_todo
-- WHERE deleted = 0
-- ORDER BY created_at DESC;
--
-- Check todos by status:
-- SELECT status, COUNT(*) as count
-- FROM sys_todo
-- WHERE deleted = 0
-- GROUP BY status;
--
-- Check todos by priority:
-- SELECT priority, COUNT(*) as count
-- FROM sys_todo
-- WHERE deleted = 0
-- GROUP BY priority;
--
-- Check category associations:
-- SELECT
--   t.id as todo_id,
--   t.title as todo_title,
--   c.name as category_name,
--   c.color as category_color
-- FROM sys_todo t
-- LEFT JOIN sys_todo_category tc ON t.id = tc.todo_id
-- LEFT JOIN sys_category c ON tc.category_id = c.id
-- WHERE t.deleted = 0
-- ORDER BY t.id, c.name;
--
-- Count todos per category:
-- SELECT
--   c.name as category_name,
--   c.color as category_color,
--   COUNT(tc.todo_id) as todo_count
-- FROM sys_category c
-- LEFT JOIN sys_todo_category tc ON c.id = tc.category_id
-- WHERE c.deleted = 0
-- GROUP BY c.id, c.name, c.color
-- ORDER BY todo_count DESC;
--
-- Find overdue todos:
-- SELECT id, title, priority, due_date
-- FROM sys_todo
-- WHERE deleted = 0
--   AND status = 'PENDING'
--   AND due_date < NOW()
-- ORDER BY due_date ASC;
--
-- Find todos due within next 3 days:
-- SELECT id, title, priority, due_date
-- FROM sys_todo
-- WHERE deleted = 0
--   AND status = 'PENDING'
--   AND due_date BETWEEN NOW() AND DATE_ADD(NOW(), INTERVAL 3 DAY)
-- ORDER BY due_date ASC;
-- =====================================================

-- =====================================================
-- Rollback Instructions:
-- =====================================================
-- To rollback this migration:
--
-- 1. Delete all todo-category associations:
--    DELETE FROM sys_todo_category;
--
-- 2. Delete all todos (except those created manually):
--    DELETE FROM sys_todo WHERE id <= 10;
--
-- Note: This migration only inserts data, so rollback
-- simply involves deleting the inserted records. The
-- table structures remain intact.
-- =====================================================
