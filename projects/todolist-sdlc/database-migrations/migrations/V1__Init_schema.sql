-- =============================================
-- Flyway Migration: V1__Init_schema.sql
-- 描述: TodoList 数据库初始化（兼容 MySQL 和 H2）
-- 作者: Claude Code (DB Administrator Role)
-- 日期: 2026-03-16
-- =============================================

-- =============================================
-- MySQL 特定配置
-- H2 会忽略这些语句
-- =============================================

-- MySQL: 创建数据库（如果不存在）
-- H2: 自动忽略（H2 在连接时自动创建内存数据库）
CREATE DATABASE IF NOT EXISTS todolist CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci;

-- MySQL: 使用数据库
-- H2: 自动忽略（已在连接 URL 中指定）
USE todolist;

-- =============================================
-- 创建表（兼容 MySQL 和 H2）
-- H2 运行在 MySQL 模式 (MODE=MySQL)
-- =============================================

-- 创建用户表
CREATE TABLE IF NOT EXISTS t_user (
    id BIGINT PRIMARY KEY AUTO_INCREMENT,
    email VARCHAR(100) NOT NULL UNIQUE,
    password VARCHAR(255) NOT NULL,
    nickname VARCHAR(50),
    avatar VARCHAR(255),
    status TINYINT DEFAULT 1,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);

-- 创建分类表
CREATE TABLE IF NOT EXISTS t_category (
    id BIGINT PRIMARY KEY AUTO_INCREMENT,
    user_id BIGINT NOT NULL,
    name VARCHAR(50) NOT NULL,
    color VARCHAR(20) DEFAULT '#409EFF',
    sort_order INT DEFAULT 0,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    FOREIGN KEY (user_id) REFERENCES t_user(id) ON DELETE CASCADE
);

-- 创建任务表
CREATE TABLE IF NOT EXISTS t_todo (
    id BIGINT PRIMARY KEY AUTO_INCREMENT,
    user_id BIGINT NOT NULL,
    category_id BIGINT,
    title VARCHAR(200) NOT NULL,
    description TEXT,
    status TINYINT DEFAULT 0,
    priority TINYINT DEFAULT 1,
    due_date DATE,
    completed_at TIMESTAMP,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    FOREIGN KEY (user_id) REFERENCES t_user(id) ON DELETE CASCADE,
    FOREIGN KEY (category_id) REFERENCES t_category(id) ON DELETE SET NULL
);
