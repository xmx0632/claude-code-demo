-- =====================================================
-- TodoList Database Migration Script
-- Version: V3
-- Description: Create sys_todo table with indexes and foreign keys
-- Author: Database Administrator
-- Date: 2026-01-26
-- =====================================================
-- This script creates the sys_todo table which stores todo items.
-- The table includes status tracking, priority levels, due dates,
-- optimistic locking for concurrency control, and soft delete support.
-- =====================================================

USE `todolist`;

-- Drop table if exists (for rollback purposes)
-- DROP TABLE IF EXISTS `sys_todo`;

-- Create sys_todo table
CREATE TABLE IF NOT EXISTS `sys_todo` (
  `id` BIGINT NOT NULL AUTO_INCREMENT COMMENT 'Todo ID - Primary Key',
  `user_id` BIGINT NOT NULL COMMENT 'User ID - Foreign key to sys_user',
  `title` VARCHAR(200) NOT NULL COMMENT 'Todo title - Brief description of the task',
  `description` TEXT DEFAULT NULL COMMENT 'Detailed description - Additional task information',
  `status` VARCHAR(20) NOT NULL DEFAULT 'PENDING' COMMENT 'Status (PENDING=pending, DONE=completed)',
  `priority` VARCHAR(20) NOT NULL DEFAULT 'MEDIUM' COMMENT 'Priority level (HIGH=high, MEDIUM=medium, LOW=low)',
  `due_date` DATETIME DEFAULT NULL COMMENT 'Due date - When the task should be completed',
  `completed_at` DATETIME DEFAULT NULL COMMENT 'Completion timestamp - When the task was marked as done',
  `version` INT NOT NULL DEFAULT 0 COMMENT 'Version number - For optimistic locking',
  `created_at` DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT 'Creation timestamp',
  `updated_at` DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT 'Last update timestamp',
  `deleted` TINYINT NOT NULL DEFAULT 0 COMMENT 'Soft delete flag (0=active, 1=deleted)',

  PRIMARY KEY (`id`),
  KEY `idx_user_id` (`user_id`),
  KEY `idx_status` (`status`),
  KEY `idx_priority` (`priority`),
  KEY `idx_due_date` (`due_date`),
  KEY `idx_created_at` (`created_at`),
  KEY `idx_deleted` (`deleted`),
  CONSTRAINT `fk_todo_user` FOREIGN KEY (`user_id`)
    REFERENCES `sys_user` (`id`)
    ON DELETE CASCADE

) ENGINE=InnoDB
  DEFAULT CHARSET=utf8mb4
  COLLATE=utf8mb4_unicode_ci
  COMMENT='Todo table - Stores todo items and tasks';

-- =====================================================
-- Business Rules:
-- =====================================================
-- 1. Each todo must belong to a user (user_id is required)
-- 2. Status values: PENDING (default), DONE
-- 3. Priority values: HIGH, MEDIUM (default), LOW
-- 4. Optimistic locking using version field prevents concurrent update conflicts
-- 5. Soft delete implemented (deleted=1 means deleted)
-- 6. When a user is deleted, all their todos are cascade deleted
-- 7. completed_at should be set when status changes to DONE
-- =====================================================

-- =====================================================
-- Index Strategy:
-- =====================================================
-- idx_user_id: For querying todos by user (data isolation)
-- idx_status: For filtering by status (pending vs completed)
-- idx_priority: For filtering by priority level
-- idx_due_date: For sorting and filtering by due date
-- idx_created_at: For chronological sorting
-- idx_deleted: For soft delete queries
-- =====================================================

-- =====================================================
-- Verification Queries:
-- =====================================================
-- Check table structure:
-- DESCRIBE sys_todo;
--
-- Verify indexes:
-- SHOW INDEX FROM sys_todo;
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
--   AND TABLE_NAME = 'sys_todo'
--   AND REFERENCED_TABLE_NAME IS NOT NULL;
--
-- Count todos by status:
-- SELECT status, COUNT(*) as count
-- FROM sys_todo
-- WHERE deleted = 0
-- GROUP BY status;
--
-- Count todos by priority:
-- SELECT priority, COUNT(*) as count
-- FROM sys_todo
-- WHERE deleted = 0
-- GROUP BY priority;
-- =====================================================

-- =====================================================
-- Rollback Instructions:
-- =====================================================
-- To rollback this migration:
-- 1. DROP TABLE IF EXISTS `sys_todo`;
-- 2. Note: This will cascade delete related data in sys_todo_category
--    due to foreign key constraints
-- =====================================================
