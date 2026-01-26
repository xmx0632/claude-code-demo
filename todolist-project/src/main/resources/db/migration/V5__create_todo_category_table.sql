-- =====================================================
-- TodoList Database Migration Script
-- Version: V5
-- Description: Create sys_todo_category table for many-to-many relationship
-- Author: Database Administrator
-- Date: 2026-01-26
-- =====================================================
-- This script creates the sys_todo_category table which implements
-- the many-to-many relationship between todos and categories.
-- A todo can belong to multiple categories, and a category can
-- contain multiple todos.
-- =====================================================

USE `todolist`;

-- Drop table if exists (for rollback purposes)
-- DROP TABLE IF EXISTS `sys_todo_category`;

-- Create sys_todo_category table
CREATE TABLE IF NOT EXISTS `sys_todo_category` (
  `id` BIGINT NOT NULL AUTO_INCREMENT COMMENT 'Relationship ID - Primary Key',
  `todo_id` BIGINT NOT NULL COMMENT 'Todo ID - Foreign key to sys_todo',
  `category_id` BIGINT NOT NULL COMMENT 'Category ID - Foreign key to sys_category',
  `created_at` DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT 'Creation timestamp',

  PRIMARY KEY (`id`),
  UNIQUE KEY `uk_todo_category` (`todo_id`, `category_id`),
  KEY `idx_todo_id` (`todo_id`),
  KEY `idx_category_id` (`category_id`),
  CONSTRAINT `fk_tc_todo` FOREIGN KEY (`todo_id`)
    REFERENCES `sys_todo` (`id`)
    ON DELETE CASCADE,
  CONSTRAINT `fk_tc_category` FOREIGN KEY (`category_id`)
    REFERENCES `sys_category` (`id`)
    ON DELETE CASCADE

) ENGINE=InnoDB
  DEFAULT CHARSET=utf8mb4
  COLLATE=utf8mb4_unicode_ci
  COMMENT='Todo-Category relationship table - Many-to-many association';

-- =====================================================
-- Business Rules:
-- =====================================================
-- 1. A todo can be associated with multiple categories
-- 2. A category can contain multiple todos
-- 3. The same todo cannot be associated with the same category twice
--    (enforced by UNIQUE KEY uk_todo_category)
-- 4. When a todo is deleted, all its category associations are cascade deleted
-- 5. When a category is deleted, all its todo associations are cascade deleted
-- =====================================================

-- =====================================================
-- Index Strategy:
-- =====================================================
-- PRIMARY KEY (id): Auto-increment primary key
-- UNIQUE KEY uk_todo_category (todo_id, category_id): Prevents duplicate associations
-- KEY idx_todo_id (todo_id): For querying categories by todo
-- KEY idx_category_id (category_id): For querying todos by category
-- =====================================================

-- =====================================================
-- Verification Queries:
-- =====================================================
-- Check table structure:
-- DESCRIBE sys_todo_category;
--
-- Verify indexes:
-- SHOW INDEX FROM sys_todo_category;
--
-- Verify foreign keys:
-- SELECT
--   CONSTRAINT_NAME,
--   TABLE_NAME,
--   COLUMN_NAME,
--   REFERENCED_TABLE_NAME,
--   REFERENCED_COLUMN_NAME
-- FROM information_schema.KEY_COLUMN_USAGE
-- WHERE TABLE_SCHEMA = 'todolist'
--   AND TABLE_NAME = 'sys_todo_category'
--   AND REFERENCED_TABLE_NAME IS NOT NULL;
--
-- Count associations by todo:
-- SELECT todo_id, COUNT(*) as category_count
-- FROM sys_todo_category
-- GROUP BY todo_id;
--
-- Count associations by category:
-- SELECT category_id, COUNT(*) as todo_count
-- FROM sys_todo_category
-- GROUP BY category_id;
--
-- Find todos with multiple categories:
-- SELECT t.id, t.title, COUNT(tc.id) as category_count
-- FROM sys_todo t
-- LEFT JOIN sys_todo_category tc ON t.id = tc.todo_id
-- WHERE t.deleted = 0
-- GROUP BY t.id, t.title
-- HAVING category_count > 1
-- ORDER BY category_count DESC;
-- =====================================================

-- =====================================================
-- Rollback Instructions:
-- =====================================================
-- To rollback this migration:
-- DROP TABLE IF EXISTS `sys_todo_category`;
--
-- Note: This table has no dependent tables, so it can be
-- safely dropped without cascading to other tables.
-- =====================================================
