-- MySQL 初始化脚本
-- 在容器首次启动时自动执行

-- 创建 ruoyi_example 数据库（如果不存在）
CREATE DATABASE IF NOT EXISTS ruoyi_example DEFAULT CHARSET utf8mb4 COLLATE utf8mb4_unicode_ci;

-- 创建 todolist_project 数据库（如果不存在）
CREATE DATABASE IF NOT EXISTS todolist_project DEFAULT CHARSET utf8mb4 COLLATE utf8mb4_unicode_ci;

-- 创建 todolist_sdlc 数据库（如果不存在）
CREATE DATABASE IF NOT EXISTS todolist_sdlc DEFAULT CHARSET utf8mb4 COLLATE utf8mb4_unicode_ci;

-- 创建应用用户
CREATE USER IF NOT EXISTS 'ruoyi'@'%' IDENTIFIED BY 'ruoyi_password';
CREATE USER IF NOT EXISTS 'todolist'@'%' IDENTIFIED BY 'todolist_password';

-- 授予权限
GRANT ALL PRIVILEGES ON ruoyi_example.* TO 'ruoyi'@'%';
GRANT ALL PRIVILEGES ON todolist.* TO 'todolist'@'%';
-- root 用户拥有所有数据库的访问权限

-- 刷新权限
FLUSH PRIVILEGES;

-- 注意：数据库表结构由 Flyway 迁移脚本创建
-- 请在容器启动后执行各项目的迁移脚本
