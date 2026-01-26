# Flyway 迁移脚本编写规范

## 命名规范

### 文件命名格式

```
V<版本号>__<描述>.sql
```

### 命名规则

1. **版本号**：使用数字点分隔（1, 1.1, 1.2, 2, 2.1）
2. **双下划线**：版本和描述之间用 `__` 分隔
3. **描述**：使用小写字母和下划线
4. **后缀**：必须是 `.sql`

### 示例

```
V1__init_schema.sql                    # 初始化
V2__create_user_table.sql              # 创建表
V2.1__add_index_on_username.sql       # 小版本（索引）
V3__add_user_status_field.sql         # 添加字段
V4__migrate_legacy_data.sql           # 数据迁移
R__create_views.sql                    # 可重复迁移
```

## 迁移类型

### 1. 版本迁移 (Versioned)

格式：`V<版本>__<描述>.sql`

- 每个版本只能执行一次
- 版本号必须递增
- 适用于大多数数据库变更

### 2. 可重复迁移 (Repeatable)

格式：`R__<描述>.sql`

- 每次内容变化都会重新执行
- 没有版本号
- 适用于视图、存储过程等

## SQL 编写规范

### 1. 增量变更原则

**✅ 正确**：使用 ALTER/CREATE INDEX

```sql
-- 添加字段
ALTER TABLE sys_user ADD COLUMN status VARCHAR(10) DEFAULT '0';

-- 创建索引
CREATE INDEX idx_status ON sys_user(status);
```

**❌ 错误**：DROP + CREATE

```sql
DROP TABLE IF EXISTS sys_user;
CREATE TABLE sys_user (...);  -- 会丢失数据！
```

### 2. 使用 USE 语句

```sql
-- ========================================
-- V1__init_schema.sql
-- ========================================

USE your_database;

-- 你的 SQL 语句
```

### 3. 注释规范

```sql
-- ========================================
-- V3__add_user_status.sql
-- ========================================
-- 作者: 张三
-- 日期: 2024-01-15
-- JIRA: PROJ-123
-- 描述: 为用户表添加状态字段
-- ========================================

USE ruoyi_example;

-- 添加状态字段
ALTER TABLE sys_user
ADD COLUMN status VARCHAR(10) DEFAULT '0' COMMENT '用户状态（0正常 1停用）';
```

### 4. 事务处理

每个迁移脚本默认在一个事务中执行。如需多个事务，请在文档中说明。

### 5. 索引和约束

```sql
-- 创建索引
CREATE INDEX idx_username ON sys_user(user_name);

-- 复合索引
CREATE INDEX idx_status_dept ON sys_user(status, dept_id);

-- 外键（注意性能影响）
ALTER TABLE sys_user_role
ADD CONSTRAINT fk_user
FOREIGN KEY (user_id) REFERENCES sys_user(user_id);
```

### 6. 数据迁移

```sql
-- 迁移历史数据
INSERT INTO sys_user (user_id, user_name, nick_name)
SELECT id, username, name
FROM legacy_user
WHERE NOT EXISTS (
    SELECT 1 FROM sys_user WHERE user_id = legacy_user.id
);

-- 更新现有数据
UPDATE sys_user
SET status = '0'
WHERE status IS NULL;
```

## 迁移检查清单

在提交迁移脚本前，确保：

- [ ] 版本号正确递增
- [ ] 文件命名符合规范
- [ ] SQL 语法正确
- [ ] 使用增量变更（ALTER，不是 DROP+CREATE）
- [ ] 外键依赖关系正确
- [ ] 索引命名符合规范
- [ ] 添加必要的 COMMENT
- [ ] 包含文件头注释
- [ ] 测试环境已验证
- [ ] 准备了回滚方案

## 常见场景示例

### 添加字段

```sql
ALTER TABLE table_name
ADD COLUMN column_name VARCHAR(255) DEFAULT '' COMMENT '字段说明';
```

### 修改字段

```sql
ALTER TABLE table_name
MODIFY COLUMN column_name VARCHAR(500) COMMENT '新的字段说明';
```

### 添加索引

```sql
-- 普通索引
CREATE INDEX idx_column ON table_name(column_name);

-- 唯一索引
CREATE UNIQUE INDEX uk_column ON table_name(column_name);

-- 复合索引
CREATE INDEX idx_col1_col2 ON table_name(col1, col2);
```

### 创建表

```sql
CREATE TABLE IF NOT EXISTS table_name (
    id BIGINT NOT NULL AUTO_INCREMENT COMMENT '主键',
    field1 VARCHAR(255) NOT NULL COMMENT '字段1',
    field2 INT DEFAULT 0 COMMENT '字段2',
    create_time DATETIME DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    update_time DATETIME DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
    PRIMARY KEY (id),
    KEY idx_field1 (field1)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='表说明';
```

### 数据迁移

```sql
-- 从旧表迁移数据
INSERT INTO new_table (id, name)
SELECT id, name
FROM old_table
WHERE create_time > '2024-01-01';

-- 更新数据
UPDATE table_name
SET status = '1'
WHERE status = '0' AND deleted = 1;
```

## 性能考虑

### 大数据量操作

```sql
-- 分批处理大数据量
-- 示例：每次更新 1000 条
-- 可以编写存储过程或应用层处理
```

### 避免锁表

```sql
-- 添加字段使用 DEFAULT 值（MySQL 5.6+ 在线 DDL）
ALTER TABLE large_table
ADD COLUMN new_column VARCHAR(255) DEFAULT '';

-- 避免在业务高峰期执行耗时操作
```

## 回滚策略

虽然 Flyway Community 不支持自动回滚，但应准备回滚脚本：

```sql
-- 回滚 V3__add_user_status.sql
-- 文件名: V3__add_user_status.rollback.sql

-- 删除添加的字段
ALTER TABLE sys_user DROP COLUMN status;

-- 或使用 ALTER TABLE ... MODIFY COLUMN 恢复原始结构
```

## 团队协作

### 分支开发

使用分支前缀避免版本冲突：

```
feature-add-email/
├── V3__feature_add_email__create_email_table.sql
└── V3.1__feature_add_email__add_email_index.sql
```

### Code Review

迁移脚本需要：
1. 团队成员 Review
2. 测试环境验证
3. DBA 审核（生产环境）

## 相关文档

- [Flyway 官方文档](https://flywaydb.org/documentation/)
- [MySQL 在线 DDL](https://dev.mysql.com/doc/refman/8.0/en/innodb-online-ddl.html)
