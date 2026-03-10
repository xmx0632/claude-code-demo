-- =====================================================
-- TodoList Database Migration Script
-- Version: V4
-- Description: Create sys_category table with default categories
-- Author: Database Administrator
-- Date: 2026-01-26
-- =====================================================
-- This script creates the sys_category table which stores
-- category/tag information for organizing todos. Each category
-- belongs to a user and has a color for visual identification.
-- =====================================================

USE `todolist`;

-- Drop table if exists (for rollback purposes)
-- DROP TABLE IF EXISTS `sys_category`;

-- Create sys_category table
CREATE TABLE IF NOT EXISTS `sys_category` (
  `id` BIGINT NOT NULL AUTO_INCREMENT COMMENT 'Category ID - Primary Key',
  `user_id` BIGINT NOT NULL COMMENT 'User ID - Foreign key to sys_user',
  `name` VARCHAR(50) NOT NULL COMMENT 'Category name - Display name',
  `color` VARCHAR(7) NOT NULL DEFAULT '#000000' COMMENT 'Color code - Hex format #RRGGBB',
  `created_at` DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT 'Creation timestamp',
  `updated_at` DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT 'Last update timestamp',
  `deleted` TINYINT NOT NULL DEFAULT 0 COMMENT 'Soft delete flag (0=active, 1=deleted)',

  PRIMARY KEY (`id`),
  KEY `idx_user_id` (`user_id`),
  KEY `idx_deleted` (`deleted`),
  CONSTRAINT `fk_category_user` FOREIGN KEY (`user_id`)
    REFERENCES `sys_user` (`id`)
    ON DELETE CASCADE

) ENGINE=InnoDB
  DEFAULT CHARSET=utf8mb4
  COLLATE=utf8mb4_unicode_ci
  COMMENT='Category table - Stores categories/tags for organizing todos';

-- Insert default categories for admin user (user_id = 1)
-- These categories provide a starting point for the admin user
INSERT INTO `sys_category` (`user_id`, `name`, `color`, `created_at`, `updated_at`)
VALUES
  (1, '工作', '#FF5733', NOW(), NOW()),
  (1, '个人', '#33FF57', NOW(), NOW()),
  (1, '学习', '#3357FF', NOW(), NOW())
ON DUPLICATE KEY UPDATE `updated_at` = NOW();

-- =====================================================
-- Business Rules:
-- =====================================================
-- 1. Each category must belong to a user (user_id is required)
-- 2. Category name should be unique within a user (enforced at application layer)
-- 3. Color format: hexadecimal #RRGGBB (e.g., #FF5733 for red-orange)
-- 4. Soft delete implemented (deleted=1 means deleted)
-- 5. When a user is deleted, all their categories are cascade deleted
-- 6. Default categories are created for the admin user for demonstration
-- =====================================================

-- =====================================================
-- Index Strategy:
-- =====================================================
-- idx_user_id: For querying categories by user (data isolation)
-- idx_deleted: For soft delete queries
-- =====================================================

-- =====================================================
-- Verification Queries:
-- =====================================================
-- Check table structure:
-- DESCRIBE sys_category;
--
-- Verify indexes:
-- SHOW INDEX FROM sys_category;
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
--   AND TABLE_NAME = 'sys_category'
--   AND REFERENCED_TABLE_NAME IS NOT NULL;
--
-- View default categories:
-- SELECT id, user_id, name, color, created_at
-- FROM sys_category
-- WHERE user_id = 1 AND deleted = 0
-- ORDER BY id;
--
-- Count categories by user:
-- SELECT user_id, COUNT(*) as category_count
-- FROM sys_category
-- WHERE deleted = 0
-- GROUP BY user_id;
-- =====================================================

-- =====================================================
-- Rollback Instructions:
-- =====================================================
-- To rollback this migration:
-- 1. DROP TABLE IF EXISTS `sys_category`;
-- 2. Note: This will cascade delete related data in sys_todo_category
--    due to foreign key constraints
-- =====================================================
