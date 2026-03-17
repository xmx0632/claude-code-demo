-- =====================================================
-- TodoList Database Migration Script
-- Version: V2
-- Description: Create sys_user table with default admin user
-- Author: Database Administrator
-- Date: 2026-01-26
-- =====================================================
-- This script creates the sys_user table which stores user account
-- information. The table includes authentication fields, account
-- status tracking, and soft delete support.
-- =====================================================

USE `todolist`;

-- Drop table if exists (for rollback purposes)
-- DROP TABLE IF EXISTS `sys_user`;

-- Create sys_user table
CREATE TABLE IF NOT EXISTS `sys_user` (
  `id` BIGINT NOT NULL AUTO_INCREMENT COMMENT 'User ID - Primary Key',
  `username` VARCHAR(50) NOT NULL COMMENT 'Username - Unique identifier for login',
  `password` VARCHAR(255) NOT NULL COMMENT 'Password - BCrypt encrypted hash',
  `locked` BOOLEAN NOT NULL DEFAULT FALSE COMMENT 'Account lock status - TRUE if locked due to failed login attempts',
  `locked_at` DATETIME DEFAULT NULL COMMENT 'Account lock timestamp - When the account was locked',
  `last_login_at` DATETIME DEFAULT NULL COMMENT 'Last successful login timestamp',
  `created_at` DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT 'Account creation timestamp',
  `updated_at` DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT 'Last update timestamp',
  `deleted` TINYINT NOT NULL DEFAULT 0 COMMENT 'Soft delete flag (0=active, 1=deleted)',

  PRIMARY KEY (`id`),
  UNIQUE KEY `uk_username` (`username`),
  KEY `idx_deleted` (`deleted`)

) ENGINE=InnoDB
  DEFAULT CHARSET=utf8mb4
  COLLATE=utf8mb4_unicode_ci
  COMMENT='User table - Stores system user accounts and authentication data';

-- Insert default admin user
-- Username: admin
-- Password: admin123 (BCrypt hash: $2a$10$N.zmdr9k7uOCQb376NoUnuTJ8iAt6Z5EHsM8lE9lBOsl7iKTVKIUi)
-- Note: This default password should be changed after first login
INSERT INTO `sys_user` (`username`, `password`, `created_at`, `updated_at`)
VALUES ('admin',
        '$2a$10$N.zmdr9k7uOCQb376NoUnuTJ8iAt6Z5EHsM8lE9lBOsl7iKTVKIUi',
        NOW(),
        NOW())
ON DUPLICATE KEY UPDATE `updated_at` = NOW();

-- =====================================================
-- Business Rules:
-- =====================================================
-- 1. Username must be unique across all users
-- 2. Username length: 3-50 characters
-- 3. Password is encrypted using BCrypt algorithm
-- 4. Soft delete implemented (deleted=1 means deleted)
-- 5. Account locks after 5 consecutive failed login attempts for 30 minutes
-- =====================================================

-- =====================================================
-- Verification Queries:
-- =====================================================
-- Check table structure:
-- DESCRIBE sys_user;
--
-- Verify indexes:
-- SHOW INDEX FROM sys_user;
--
-- Verify default admin user:
-- SELECT id, username, locked, deleted, created_at
-- FROM sys_user
-- WHERE username = 'admin';
--
-- Count total users:
-- SELECT COUNT(*) as total_users FROM sys_user WHERE deleted = 0;
-- =====================================================

-- =====================================================
-- Rollback Instructions:
-- =====================================================
-- To rollback this migration:
-- 1. DROP TABLE IF EXISTS `sys_user`;
-- 2. Note: This will cascade delete related data in sys_todo,
--    sys_category, and sys_todo_category due to foreign keys
-- =====================================================
