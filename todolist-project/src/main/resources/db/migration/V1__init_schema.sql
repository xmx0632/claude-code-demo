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
-- =====================================================

-- Create database if not exists
CREATE DATABASE IF NOT EXISTS `todolist`
DEFAULT CHARACTER SET utf8mb4
COLLATE utf8mb4_unicode_ci;

-- Select the database for use
USE `todolist`;

-- Set timezone to UTC for consistent timestamp handling
SET time_zone = '+00:00';

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
