---
name: flyway-migration
description: 管理 Flyway 数据库迁移脚本。数据库版本控制时使用。
allowed-tools: ["Read", "Write", "Edit", "Glob", "Bash", "Grep"]
---

# Flyway 数据库迁移助手

自动化管理 Flyway 数据库迁移脚本的生成、验证和执行。

## 功能特性

- 自动生成迁移脚本
- 验证 SQL 语法正确性
- 检查迁移命名规范
- 生成回滚脚本
- 迁移历史查询
- 版本冲突检测

## 使用方法

### 创建新迁移

```
/flyway-migration create --table=sys_user --type=add_column
```

### 验证迁移脚本

```
/flyway-migration validate V2__add_user_status.sql
```

### 生成回滚脚本

```
/flyway-migration rollback V2__add_user_status.sql
```

### 查看迁移历史

```
/flyway-migration history
```

### 修复迁移版本

```
/flyway-migration repair --version=V2.1
```

## 迁移命名规范

Flyway 迁移文件命名格式：

```
V<版本号>__<描述>.sql
```

示例：
- `V1__init_schema.sql` - 初始化数据库结构
- `V1.1__create_user_table.sql` - 创建用户表
- `V2__add_user_status.sql` - 添加用户状态字段
- `V2.1__create_index_on_username.sql` - 创建索引

**命名规则**：
- 版本号必须递增（支持小版本号，如 1.1, 1.2, 2.0）
- 描述使用下划线分隔
- 双下划线 `__` 分隔版本和描述
- 同一版本内可包含多个变更

## 迁移类型

### 1. 版本迁移 (Versioned)

格式：`V<版本>__<描述>.sql`

每次变更都需要递增版本号：
- `V1__init.sql`
- `V2__add_table.sql`
- `V3__modify_column.sql`

### 2. 可重复迁移 (Repeatable)

格式：`R__<描述>.sql`

每次内容变化都会重新执行：
- `R__create_views.sql`
- `R__update_stored_procedures.sql`

## SQL 编写规范

### 增量变更原则

**✅ 正确：增量变更**
```sql
-- V2__add_user_status.sql
ALTER TABLE sys_user ADD COLUMN status VARCHAR(10) DEFAULT '0';
```

**❌ 错误：完整重建**
```sql
-- 不要在已有表上使用 CREATE TABLE
DROP TABLE IF EXISTS sys_user;
CREATE TABLE sys_user (...);
```

### 事务处理

```sql
-- 每个迁移脚本默认在一个事务中执行
-- 多个语句自动一起提交或回滚

-- 示例：添加字段和默认值
ALTER TABLE sys_user ADD COLUMN dept_id BIGINT;
UPDATE sys_user SET dept_id = 100 WHERE dept_id IS NULL;
ALTER TABLE sys_user MODIFY COLUMN dept_id BIGINT NOT NULL;
```

### 索引和约束

```sql
-- V3__add_indexes.sql
-- 创建索引
CREATE INDEX idx_username ON sys_user(user_name);

-- 添加外键
ALTER TABLE sys_user_role
ADD CONSTRAINT fk_user
FOREIGN KEY (user_id) REFERENCES sys_user(user_id);
```

### 数据迁移

```sql
-- V4__migrate_data.sql
-- 迁移历史数据
INSERT INTO sys_user (user_id, user_name, nick_name)
SELECT id, username, name FROM legacy_user;

-- 更新现有数据
UPDATE sys_user
SET status = '0'
WHERE status IS NULL;
```

## 迁移脚本模板

### 添加字段

```sql
-- V<version>__add_<field_name>_to_<table_name>.sql
ALTER TABLE <table_name>
ADD COLUMN <column_name> <data_type> <constraints>;

-- 示例
ALTER TABLE sys_user
ADD COLUMN phone VARCHAR(20) COMMENT '手机号码';
```

### 创建表

```sql
-- V<version>__create_<table_name>.sql
CREATE TABLE <table_name> (
    <column_name> <data_type> <constraints>,
    ...
    PRIMARY KEY (<primary_key>)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='<table_comment>';
```

### 修改字段

```sql
-- V<version>__modify_<column_name>_in_<table_name>.sql
ALTER TABLE <table_name>
MODIFY COLUMN <column_name> <new_data_type> <new_constraints>;
```

### 创建索引

```sql
-- V<version>__create_index_on_<table_name>.sql
CREATE INDEX <index_name>
ON <table_name> (<column_name>);
```

## 验证检查清单

在执行迁移前，确保：

- [ ] 版本号正确递增
- [ ] SQL 语法正确
- [ ] 使用增量变更（ALTER，不是 DROP+CREATE）
- [ ] 外键依赖关系正确
- [ ] 索引命名符合规范
- [ ] 添加必要的 COMMENT
- [ ] 测试环境已验证
- [ ] 准备了回滚方案

## 回滚策略

Flyway 不自动支持回滚，需要手动编写回滚脚本：

### 手动回滚示例

```sql
-- 回滚 V2__add_user_status.sql
ALTER TABLE sys_user DROP COLUMN status;

-- 回滚 V3__create_index.sql
DROP INDEX idx_username ON sys_user;

-- 回滚数据迁移
DELETE FROM sys_user WHERE user_id > 1000;
```

### 建议的回滚流程

1. 记录每次迁移的回滚 SQL
2. 在测试环境验证回滚脚本
3. 生产环境回滚前先备份数据
4. 按版本倒序执行回滚

## Spring Boot 配置

### application.yml

```yaml
spring:
  flyway:
    enabled: true
    locations: classpath:db/migration
    baseline-on-migrate: true
    baseline-version: 0
    validate-on-migrate: true
    out-of-order: false
```

### Maven 依赖

```xml
<dependency>
    <groupId>org.flywaydb</groupId>
    <artifactId>flyway-core</artifactId>
</dependency>
<dependency>
    <groupId>org.flywaydb</groupId>
    <artifactId>flyway-mysql</artifactId>
</dependency>
```

## 最佳实践

1. **小步快跑**：每个迁移只做一件事
2. **向前兼容**：新字段设置默认值
3. **数据备份**：执行前备份数据库
4. **测试验证**：先在测试环境执行
5. **代码审查**：迁移脚本需要 Code Review
6. **文档记录**：重要变更写文档说明
7. **避免数据丢失**：不使用 DROP 或 TRUNCATE
8. **考虑性能**：大批量数据分批处理

## 常见问题

### Q: 迁移失败怎么办？

A: 检查 SQL 语法，修复问题后使用：
```bash
flyway repair
flyway migrate
```

### Q: 如何跳过某个迁移？

A: 不建议跳过，可以修复后重新执行。如必须跳过，手动修改 flyway_schema_history 表。

### Q: 多人协作如何避免版本冲突？

A: 使用分支命名约定：
- 功能分支：`V{version}__{feature}__{description}.sql`
- 合并前检查版本号是否重复

### Q: 已有数据库如何接入 Flyway？

A: 使用 baseline 功能：
```yaml
spring.flyway.baseline-on-migrate: true
spring.flyway.baseline-version: 0
```

## 相关工具

- Flyway 官方文档: https://flywaydb.org/documentation/
- MySQL 迁移最佳实践: https://flywaydb.org/documentation/database/mysql
