-- =====================================================
-- TodoList Database Migration Script
-- Version: V1
-- Description: Initialize database schema and configuration
-- Author: Database Administrator
-- Date: 2026-01-26
-- =====================================================
-- This script is idempotent and can be run multiple times safely.
-- It creates the database if it doesn't exist and sets up the
-- basic configuration including charset, collation, and timezone.
--
-- 兼容性说明：
-- - 使用 MySQL 条件注释 /*! ... */ 包裹 MySQL 特定语法
-- - H2 会将其视为普通注释并跳过
-- - H2 运行在 MySQL 模式（MODE=MySQL）兼容大部分语法
-- =====================================================

-- MySQL 特定：创建数据库（H2 会忽略）
/*! CREATE DATABASE IF NOT EXISTS `todolist`
DEFAULT CHARACTER SET utf8mb4
COLLATE utf8mb4_unicode_ci */;

-- MySQL 特定：选择数据库（H2 会忽略）
USE `todolist`;

-- MySQL 特定：设置时区（H2 会忽略）
/*! SET time_zone = '+00:00' */;

-- =====================================================
-- Verification Queries:
-- =====================================================
-- Verify database creation:
-- SHOW DATABASES LIKE 'todolist';
--
-- Verify charset and collation:
-- SELECT DEFAULT_CHARACTER_SET_NAME, DEFAULT_COLLATION_NAME
-- FROM information_schema.SCHEMATA
-- WHERE SCHEMA_NAME = 'todolist';
--
-- Verify timezone:
-- SELECT @@session.time_zone;
-- =====================================================
