-- MySQL 初始化脚本
-- 在容器首次启动时自动执行

-- 创建数据库（如果不存在）
CREATE DATABASE IF NOT EXISTS ruoyi_example DEFAULT CHARSET utf8mb4 COLLATE utf8mb4_unicode_ci;

-- 创建应用用户
CREATE USER IF NOT EXISTS 'ruoyi'@'%' IDENTIFIED BY 'ruoyi_password';

-- 授予权限
GRANT ALL PRIVILEGES ON ruoyi_example.* TO 'ruoyi'@'%';

-- 刷新权限
FLUSH PRIVILEGES;

-- 使用数据库
USE ruoyi_example;

-- 注意：数据库表结构由 Flyway 迁移脚本创建
-- 请在容器启动后执行 database-migrations 组件中的迁移脚本
