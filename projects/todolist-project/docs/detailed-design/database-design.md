# 数据库设计文档 (Database Design)

> **项目**: TodoList 待办事项管理系统
> **版本**: 1.0
> **创建日期**: 2026-01-26
> **作者**: 后端开发工程师
> **状态**: 草稿 | 评审中 | 已批准

---

## 文档修订历史

| 版本 | 日期 | 作者 | 修订说明 | 审批人 |
|------|------|------|----------|--------|
| 1.0  | 2026-01-26 | 后端开发工程师 | 初始版本 | 待审批 |

---

## 1. 数据库概述

### 1.1 数据库选型

**数据库**: MySQL 8.0+

**选择理由**:
- 开源免费,社区活跃
- 事务支持完善(ACID)
- 索引和查询优化成熟
- 支持全文索引(未来搜索功能)
- 云服务支持好(AWS RDS, 阿里云 RDS)

**字符集**: utf8mb4
**排序规则**: utf8mb4_unicode_ci
**时区**: UTC

### 1.2 数据库命名规范

**表命名**:
- 格式: `sys_模块名_实体名`
- 示例: `sys_user`, `sys_todo`, `sys_category`

**字段命名**:
- 使用小写字母和下划线
- 示例: `user_id`, `created_at`, `due_date`

**索引命名**:
- 主键索引: `PRIMARY`
- 唯一索引: `uk_字段名`
- 普通索引: `idx_字段名`

### 1.3 数据库迁移工具

**工具**: Flyway 9.x

**迁移脚本位置**: `src/main/resources/db/migration`

**命名规则**:
```
V{版本号}__{描述}.sql

示例:
V1__init_schema.sql
V2__create_user_table.sql
V3__create_todo_table.sql
V4__create_category_table.sql
V5__create_todo_category_table.sql
```

---

## 2. 数据库表设计

### 2.1 sys_user (用户表)

**表描述**: 存储系统用户信息

**表结构**:

| 字段名 | 类型 | 长度 | 允许NULL | 默认值 | 键 | 说明 |
|--------|------|------|----------|--------|-----|------|
| id | BIGINT | - | NO | AUTO_INCREMENT | PK | 用户ID |
| username | VARCHAR | 50 | NO | - | UK | 用户名 |
| password | VARCHAR | 255 | NO | - | - | 密码(BCrypt) |
| locked | BOOLEAN | - | NO | false | - | 是否锁定 |
| locked_at | DATETIME | - | YES | NULL | - | 锁定时间 |
| last_login_at | DATETIME | - | YES | NULL | - | 最后登录时间 |
| created_at | DATETIME | - | NO | CURRENT_TIMESTAMP | - | 创建时间 |
| updated_at | DATETIME | - | NO | CURRENT_TIMESTAMP ON UPDATE | - | 更新时间 |
| deleted | TINYINT | - | NO | 0 | IDX | 删除标记 |

**DDL**:
```sql
CREATE TABLE `sys_user` (
  `id` BIGINT NOT NULL AUTO_INCREMENT COMMENT '用户ID',
  `username` VARCHAR(50) NOT NULL COMMENT '用户名',
  `password` VARCHAR(255) NOT NULL COMMENT '密码(BCrypt加密)',
  `locked` BOOLEAN NOT NULL DEFAULT FALSE COMMENT '账户是否锁定',
  `locked_at` DATETIME DEFAULT NULL COMMENT '锁定时间',
  `last_login_at` DATETIME DEFAULT NULL COMMENT '最后登录时间',
  `created_at` DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `updated_at` DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
  `deleted` TINYINT NOT NULL DEFAULT 0 COMMENT '删除标记(0-正常, 1-已删除)',
  PRIMARY KEY (`id`),
  UNIQUE KEY `uk_username` (`username`),
  KEY `idx_deleted` (`deleted`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci COMMENT='用户表';
```

**索引说明**:
- `PRIMARY KEY (id)`: 主键索引
- `UNIQUE KEY uk_username (username)`: 用户名唯一索引
- `KEY idx_deleted (deleted)`: 删除标记索引(软删除查询优化)

**业务规则**:
- 用户名唯一,长度 3-50 字符
- 密码使用 BCrypt 加密存储
- 软删除,deleted=1 表示已删除
- 连续登录失败 5 次锁定 30 分钟

---

### 2.2 sys_todo (待办事项表)

**表描述**: 存储待办事项信息

**表结构**:

| 字段名 | 类型 | 长度 | 允许NULL | 默认值 | 键 | 说明 |
|--------|------|------|----------|--------|-----|------|
| id | BIGINT | - | NO | AUTO_INCREMENT | PK | 待办事项ID |
| user_id | BIGINT | - | NO | - | FK, IDX | 用户ID |
| title | VARCHAR | 200 | NO | - | - | 标题 |
| description | TEXT | - | YES | NULL | - | 描述 |
| status | VARCHAR | 20 | NO | 'PENDING' | IDX | 状态 |
| priority | VARCHAR | 20 | NO | 'MEDIUM' | IDX | 优先级 |
| due_date | DATETIME | - | YES | NULL | IDX | 截止日期 |
| completed_at | DATETIME | - | YES | NULL | - | 完成时间 |
| version | INT | - | NO | 0 | - | 版本号(乐观锁) |
| created_at | DATETIME | - | NO | CURRENT_TIMESTAMP | IDX | 创建时间 |
| updated_at | DATETIME | - | NO | CURRENT_TIMESTAMP ON UPDATE | - | 更新时间 |
| deleted | TINYINT | - | NO | 0 | IDX | 删除标记 |

**DDL**:
```sql
CREATE TABLE `sys_todo` (
  `id` BIGINT NOT NULL AUTO_INCREMENT COMMENT '待办事项ID',
  `user_id` BIGINT NOT NULL COMMENT '用户ID',
  `title` VARCHAR(200) NOT NULL COMMENT '标题',
  `description` TEXT DEFAULT NULL COMMENT '描述',
  `status` VARCHAR(20) NOT NULL DEFAULT 'PENDING' COMMENT '状态(PENDING-待处理, DONE-已完成)',
  `priority` VARCHAR(20) NOT NULL DEFAULT 'MEDIUM' COMMENT '优先级(HIGH-高, MEDIUM-中, LOW-低)',
  `due_date` DATETIME DEFAULT NULL COMMENT '截止日期',
  `completed_at` DATETIME DEFAULT NULL COMMENT '完成时间',
  `version` INT NOT NULL DEFAULT 0 COMMENT '版本号(乐观锁)',
  `created_at` DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `updated_at` DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
  `deleted` TINYINT NOT NULL DEFAULT 0 COMMENT '删除标记(0-正常, 1-已删除)',
  PRIMARY KEY (`id`),
  KEY `idx_user_id` (`user_id`),
  KEY `idx_status` (`status`),
  KEY `idx_priority` (`priority`),
  KEY `idx_due_date` (`due_date`),
  KEY `idx_created_at` (`created_at`),
  KEY `idx_deleted` (`deleted`),
  CONSTRAINT `fk_todo_user` FOREIGN KEY (`user_id`) REFERENCES `sys_user` (`id`) ON DELETE CASCADE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci COMMENT='待办事项表';
```

**索引说明**:
- `PRIMARY KEY (id)`: 主键索引
- `KEY idx_user_id (user_id)`: 用户ID索引(数据隔离)
- `KEY idx_status (status)`: 状态索引(状态过滤)
- `KEY idx_priority (priority)`: 优先级索引(优先级过滤)
- `KEY idx_due_date (due_date)`: 截止日期索引(时间过滤)
- `KEY idx_created_at (created_at)`: 创建时间索引(排序)
- `KEY idx_deleted (deleted)`: 删除标记索引
- `FOREIGN KEY (user_id)`: 外键,关联用户表

**业务规则**:
- 每个待办事项必须属于一个用户
- 状态可选值: PENDING(待处理), DONE(已完成)
- 优先级可选值: HIGH(高), MEDIUM(中), LOW(低)
- 使用乐观锁(version)防止并发更新冲突
- 软删除

---

### 2.3 sys_category (分类表)

**表描述**: 存储分类/标签信息

**表结构**:

| 字段名 | 类型 | 长度 | 允许NULL | 默认值 | 键 | 说明 |
|--------|------|------|----------|--------|-----|------|
| id | BIGINT | - | NO | AUTO_INCREMENT | PK | 分类ID |
| user_id | BIGINT | - | NO | - | FK, IDX | 用户ID |
| name | VARCHAR | 50 | NO | - | - | 分类名称 |
| color | VARCHAR | 7 | NO | '#000000' | - | 颜色 |
| created_at | DATETIME | - | NO | CURRENT_TIMESTAMP | - | 创建时间 |
| updated_at | DATETIME | - | NO | CURRENT_TIMESTAMP ON UPDATE | - | 更新时间 |
| deleted | TINYINT | - | NO | 0 | IDX | 删除标记 |

**DDL**:
```sql
CREATE TABLE `sys_category` (
  `id` BIGINT NOT NULL AUTO_INCREMENT COMMENT '分类ID',
  `user_id` BIGINT NOT NULL COMMENT '用户ID',
  `name` VARCHAR(50) NOT NULL COMMENT '分类名称',
  `color` VARCHAR(7) NOT NULL DEFAULT '#000000' COMMENT '颜色(十六进制)',
  `created_at` DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `updated_at` DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
  `deleted` TINYINT NOT NULL DEFAULT 0 COMMENT '删除标记(0-正常, 1-已删除)',
  PRIMARY KEY (`id`),
  KEY `idx_user_id` (`user_id`),
  KEY `idx_deleted` (`deleted`),
  CONSTRAINT `fk_category_user` FOREIGN KEY (`user_id`) REFERENCES `sys_user` (`id`) ON DELETE CASCADE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci COMMENT='分类表';
```

**索引说明**:
- `PRIMARY KEY (id)`: 主键索引
- `KEY idx_user_id (user_id)`: 用户ID索引(数据隔离)
- `KEY idx_deleted (deleted)`: 删除标记索引
- `FOREIGN KEY (user_id)`: 外键,关联用户表

**业务规则**:
- 每个分类必须属于一个用户
- 分类名称在同一用户下唯一(应用层验证)
- 颜色使用十六进制格式: #RRGGBB
- 软删除

---

### 2.4 sys_todo_category (待办-分类关联表)

**表描述**: 存储待办事项与分类的多对多关系

**表结构**:

| 字段名 | 类型 | 长度 | 允许NULL | 默认值 | 键 | 说明 |
|--------|------|------|----------|--------|-----|------|
| id | BIGINT | - | NO | AUTO_INCREMENT | PK | 关联ID |
| todo_id | BIGINT | - | NO | - | FK, IDX | 待办事项ID |
| category_id | BIGINT | - | NO | - | FK, IDX | 分类ID |
| created_at | DATETIME | - | NO | CURRENT_TIMESTAMP | - | 创建时间 |

**DDL**:
```sql
CREATE TABLE `sys_todo_category` (
  `id` BIGINT NOT NULL AUTO_INCREMENT COMMENT '关联ID',
  `todo_id` BIGINT NOT NULL COMMENT '待办事项ID',
  `category_id` BIGINT NOT NULL COMMENT '分类ID',
  `created_at` DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  PRIMARY KEY (`id`),
  UNIQUE KEY `uk_todo_category` (`todo_id`, `category_id`),
  KEY `idx_todo_id` (`todo_id`),
  KEY `idx_category_id` (`category_id`),
  CONSTRAINT `fk_tc_todo` FOREIGN KEY (`todo_id`) REFERENCES `sys_todo` (`id`) ON DELETE CASCADE,
  CONSTRAINT `fk_tc_category` FOREIGN KEY (`category_id`) REFERENCES `sys_category` (`id`) ON DELETE CASCADE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci COMMENT='待办-分类关联表';
```

**索引说明**:
- `PRIMARY KEY (id)`: 主键索引
- `UNIQUE KEY uk_todo_category (todo_id, category_id)`: 联合唯一索引,防止重复关联
- `KEY idx_todo_id (todo_id)`: 待办事项ID索引
- `KEY idx_category_id (category_id)`: 分类ID索引
- `FOREIGN KEY (todo_id)`: 外键,关联待办事项表
- `FOREIGN KEY (category_id)`: 外键,关联分类表

**业务规则**:
- 同一个待办事项不能重复关联同一个分类
- 待办事项删除时,级联删除关联关系
- 分类删除时,级联删除关联关系

---

## 3. ER 图 (实体关系图)

```
┌─────────────────┐
│    sys_user     │
│─────────────────│
│ id (PK)         │
│ username (UK)   │
│ password        │
│ locked          │
│ created_at      │
│ updated_at      │
│ deleted         │
└────────┬────────┘
         │ 1
         │
         │ N
    ┌────┴─────────────────────────┐
    │                              │
    │ 1                            │ 1
┌───┴───────────┐          ┌───────┴──────────┐
│   sys_todo    │          │  sys_category    │
│───────────────│          │──────────────────│
│ id (PK)       │          │ id (PK)          │
│ user_id (FK)  │          │ user_id (FK)     │
│ title         │          │ name             │
│ status        │          │ color            │
│ priority      │          │ created_at       │
│ due_date      │          │ updated_at       │
│ created_at    │          │ deleted          │
│ updated_at    │          └──────────────────┘
│ deleted       │
│ version       │
└───────┬───────┘
        │
        │ M
        │
        │ N
┌───────┴──────────────────────┐
│    sys_todo_category         │
│──────────────────────────────│
│ id (PK)                      │
│ todo_id (FK)                 │
│ category_id (FK)             │
│ created_at                   │
└──────────────────────────────┘
```

**关系说明**:
1. **User (1) ←→ (N) Todo**: 一个用户可以有多个待办事项
2. **User (1) ←→ (N) Category**: 一个用户可以有多个分类
3. **Todo (M) ←→ (N) Category**: 待办事项和分类是多对多关系,通过 `sys_todo_category` 关联

---

## 4. 数据库初始化脚本

### 4.1 V1__init_schema.sql

```sql
-- =====================================================
-- TodoList 数据库初始化脚本
-- 版本: V1
-- 描述: 创建数据库和基础配置
-- 作者: todolist
-- 日期: 2026-01-26
-- =====================================================

-- 创建数据库
CREATE DATABASE IF NOT EXISTS `todolist`
DEFAULT CHARACTER SET utf8mb4
COLLATE utf8mb4_unicode_ci;

USE `todolist`;

-- 设置时区为 UTC
SET time_zone = '+00:00';
```

### 4.2 V2__create_user_table.sql

```sql
-- =====================================================
-- 创建用户表
-- =====================================================

USE `todolist`;

CREATE TABLE IF NOT EXISTS `sys_user` (
  `id` BIGINT NOT NULL AUTO_INCREMENT COMMENT '用户ID',
  `username` VARCHAR(50) NOT NULL COMMENT '用户名',
  `password` VARCHAR(255) NOT NULL COMMENT '密码(BCrypt加密)',
  `locked` BOOLEAN NOT NULL DEFAULT FALSE COMMENT '账户是否锁定',
  `locked_at` DATETIME DEFAULT NULL COMMENT '锁定时间',
  `last_login_at` DATETIME DEFAULT NULL COMMENT '最后登录时间',
  `created_at` DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `updated_at` DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
  `deleted` TINYINT NOT NULL DEFAULT 0 COMMENT '删除标记(0-正常, 1-已删除)',
  PRIMARY KEY (`id`),
  UNIQUE KEY `uk_username` (`username`),
  KEY `idx_deleted` (`deleted`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci COMMENT='用户表';

-- 初始化默认管理员用户(密码: admin123)
-- BCrypt hash of "admin123"
INSERT INTO `sys_user` (`username`, `password`)
VALUES ('admin', '$2a$10$N.zmdr9k7uOCQb376NoUnuTJ8iAt6Z5EHsM8lE9lBOsl7iKTVKIUi');
```

### 4.3 V3__create_todo_table.sql

```sql
-- =====================================================
-- 创建待办事项表
-- =====================================================

USE `todolist`;

CREATE TABLE IF NOT EXISTS `sys_todo` (
  `id` BIGINT NOT NULL AUTO_INCREMENT COMMENT '待办事项ID',
  `user_id` BIGINT NOT NULL COMMENT '用户ID',
  `title` VARCHAR(200) NOT NULL COMMENT '标题',
  `description` TEXT DEFAULT NULL COMMENT '描述',
  `status` VARCHAR(20) NOT NULL DEFAULT 'PENDING' COMMENT '状态(PENDING-待处理, DONE-已完成)',
  `priority` VARCHAR(20) NOT NULL DEFAULT 'MEDIUM' COMMENT '优先级(HIGH-高, MEDIUM-中, LOW-低)',
  `due_date` DATETIME DEFAULT NULL COMMENT '截止日期',
  `completed_at` DATETIME DEFAULT NULL COMMENT '完成时间',
  `version` INT NOT NULL DEFAULT 0 COMMENT '版本号(乐观锁)',
  `created_at` DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `updated_at` DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
  `deleted` TINYINT NOT NULL DEFAULT 0 COMMENT '删除标记(0-正常, 1-已删除)',
  PRIMARY KEY (`id`),
  KEY `idx_user_id` (`user_id`),
  KEY `idx_status` (`status`),
  KEY `idx_priority` (`priority`),
  KEY `idx_due_date` (`due_date`),
  KEY `idx_created_at` (`created_at`),
  KEY `idx_deleted` (`deleted`),
  CONSTRAINT `fk_todo_user` FOREIGN KEY (`user_id`) REFERENCES `sys_user` (`id`) ON DELETE CASCADE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci COMMENT='待办事项表';
```

### 4.4 V4__create_category_table.sql

```sql
-- =====================================================
-- 创建分类表
-- =====================================================

USE `todolist`;

CREATE TABLE IF NOT EXISTS `sys_category` (
  `id` BIGINT NOT NULL AUTO_INCREMENT COMMENT '分类ID',
  `user_id` BIGINT NOT NULL COMMENT '用户ID',
  `name` VARCHAR(50) NOT NULL COMMENT '分类名称',
  `color` VARCHAR(7) NOT NULL DEFAULT '#000000' COMMENT '颜色(十六进制)',
  `created_at` DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `updated_at` DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
  `deleted` TINYINT NOT NULL DEFAULT 0 COMMENT '删除标记(0-正常, 1-已删除)',
  PRIMARY KEY (`id`),
  KEY `idx_user_id` (`user_id`),
  KEY `idx_deleted` (`deleted`),
  CONSTRAINT `fk_category_user` FOREIGN KEY (`user_id`) REFERENCES `sys_user` (`id`) ON DELETE CASCADE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci COMMENT='分类表';

-- 初始化默认分类
INSERT INTO `sys_category` (`user_id`, `name`, `color`)
VALUES
  (1, '工作', '#FF5733'),
  (1, '个人', '#33FF57'),
  (1, '学习', '#3357FF');
```

### 4.5 V5__create_todo_category_table.sql

```sql
-- =====================================================
-- 创建待办-分类关联表
-- =====================================================

USE `todolist`;

CREATE TABLE IF NOT EXISTS `sys_todo_category` (
  `id` BIGINT NOT NULL AUTO_INCREMENT COMMENT '关联ID',
  `todo_id` BIGINT NOT NULL COMMENT '待办事项ID',
  `category_id` BIGINT NOT NULL COMMENT '分类ID',
  `created_at` DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  PRIMARY KEY (`id`),
  UNIQUE KEY `uk_todo_category` (`todo_id`, `category_id`),
  KEY `idx_todo_id` (`todo_id`),
  KEY `idx_category_id` (`category_id`),
  CONSTRAINT `fk_tc_todo` FOREIGN KEY (`todo_id`) REFERENCES `sys_todo` (`id`) ON DELETE CASCADE,
  CONSTRAINT `fk_tc_category` FOREIGN KEY (`category_id`) REFERENCES `sys_category` (`id`) ON DELETE CASCADE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci COMMENT='待办-分类关联表';
```

---

## 5. 索引设计

### 5.1 索引策略

**主键索引**:
- 所有表都使用自增 BIGINT 作为主键
- 主键自动创建聚簇索引

**唯一索引**:
- `sys_user.username`: 用户名唯一性
- `sys_todo_category(todo_id, category_id)`: 防止重复关联

**普通索引**:
- 所有外键字段创建索引
- 常用查询字段创建索引
- 软删除字段(deleted)创建索引

**联合索引**:
- 考虑查询条件组合,可适当添加联合索引
- 例如: `(user_id, status, created_at)`

### 5.2 索引使用建议

**适合创建索引的场景**:
- WHERE 条件字段
- ORDER BY 排序字段
- JOIN 关联字段
- 频繁查询的字段

**不适合创建索引的场景**:
- 数据区分度低的字段(如性别)
- 频繁更新的字段
- TEXT/BLOB 类型字段
- 小表(行数 < 1000)

### 5.3 索引优化建议

1. **覆盖索引**: 查询只使用索引字段,避免回表
2. **最左前缀原则**: 联合索引使用时遵循最左前缀
3. **索引选择性**: 选择性高的字段优先创建索引
4. **避免索引失效**: 避免在索引列上使用函数

---

## 6. 数据完整性

### 6.1 主键约束

- 所有表都有主键 `id`
- 使用 AUTO_INCREMENT 自增

### 6.2 外键约束

| 表 | 外键字段 | 关联表 | 关联字段 | 级联规则 |
|----|----------|--------|----------|----------|
| sys_todo | user_id | sys_user | id | CASCADE |
| sys_category | user_id | sys_user | id | CASCADE |
| sys_todo_category | todo_id | sys_todo | id | CASCADE |
| sys_todo_category | category_id | sys_category | id | CASCADE |

### 6.3 唯一约束

- `sys_user.username`: 用户名唯一
- `sys_todo_category(todo_id, category_id)`: 关联唯一

### 6.4 非空约束

所有字段都设置了是否允许 NULL,关键字段如主键、外键、业务关键字段都设置为 NOT NULL。

### 6.5 默认值

- 布尔类型: 默认 FALSE
- 时间类型: 默认 CURRENT_TIMESTAMP
- 状态字段: 设置默认值(如 status='PENDING')
- 软删除: deleted=0

### 6.6 检查约束

MySQL 8.0.16+ 支持 CHECK 约束,可添加:
```sql
ALTER TABLE sys_todo
ADD CONSTRAINT chk_status CHECK (status IN ('PENDING', 'DONE'));

ALTER TABLE sys_todo
ADD CONSTRAINT chk_priority CHECK (priority IN ('HIGH', 'MEDIUM', 'LOW'));
```

---

## 7. 数据归档策略

### 7.1 软删除

- 所有业务表都使用软删除
- `deleted=0`: 正常
- `deleted=1`: 已删除

### 7.2 数据归档

**归档策略**:
- 已完成且超过 1 年的待办事项
- 已删除且超过 6 个月的数据

**归档方式**:
- 定时任务将数据迁移到归档表
- 归档表: `sys_todo_archive`, `sys_category_archive`

---

## 8. 数据库性能优化

### 8.1 查询优化

**慢查询日志**:
```sql
-- 开启慢查询日志
SET GLOBAL slow_query_log = 'ON';
SET GLOBAL long_query_time = 2;
```

**查询优化建议**:
- 避免 SELECT *
- 使用索引覆盖
- 合理使用 LIMIT
- 避免子查询,使用 JOIN

### 8.2 分页优化

**使用 MyBatis-Plus 分页插件**:
```java
@Configuration
public class MyBatisPlusConfig {

    @Bean
    public MybatisPlusInterceptor paginationInterceptor() {
        MybatisPlusInterceptor interceptor = new MybatisPlusInterceptor();
        interceptor.addInnerInterceptor(new PaginationInnerInterceptor(DbType.MYSQL));
        return interceptor;
    }
}
```

### 8.3 连接池配置

**HikariCP 配置** (application.yml):
```yaml
spring:
  datasource:
    hikari:
      minimum-idle: 5
      maximum-pool-size: 20
      idle-timeout: 30000
      connection-timeout: 30000
      max-lifetime: 1800000
```

---

## 9. 数据库备份与恢复

### 9.1 备份策略

**全量备份**:
- 每天凌晨 2:00 执行
- 保留最近 7 天的备份

**增量备份**:
- 每小时执行一次
- 保留最近 24 小时的备份

**备份命令**:
```bash
# 全量备份
mysqldump -u root -p todolist > /backup/todolist_$(date +%Y%m%d).sql

# 增量备份(使用 binlog)
mysqlbinlog --start-datetime="2026-01-26 00:00:00" \
           --stop-datetime="2026-01-26 23:59:59" \
           mysql-bin.000001 > /backup/incremental.sql
```

### 9.2 恢复策略

**全量恢复**:
```bash
mysql -u root -p todolist < /backup/todolist_20260126.sql
```

**增量恢复**:
```bash
mysql -u root -p todolist < /backup/incremental.sql
```

---

## 10. 数据库监控

### 10.1 监控指标

**性能指标**:
- QPS (Queries Per Second)
- TPS (Transactions Per Second)
- 慢查询数量
- 连接数使用情况
- 缓冲池命中率

**可用性指标**:
- 数据库运行时间
- 主从延迟
- 锁等待时间

### 10.2 监控工具

- **Prometheus + Grafana**: 指标收集和可视化
- **MySQL Slow Query Log**: 慢查询分析
- **Performance Schema**: 性能监控
- **MySQL Enterprise Monitor**: 商业监控工具

---

## 11. 数据库安全

### 11.1 用户权限管理

**最小权限原则**:
```sql
-- 创建应用专用数据库用户
CREATE USER 'todolist_app'@'%' IDENTIFIED BY 'strong_password';

-- 授予必要权限
GRANT SELECT, INSERT, UPDATE, DELETE ON todolist.* TO 'todolist_app'@'%';

-- 刷新权限
FLUSH PRIVILEGES;
```

### 11.2 数据加密

**传输加密**:
- 使用 SSL/TLS 连接

**存储加密**:
- 密码使用 BCrypt 加密
- 敏感字段可使用 AES 加密(可选)

### 11.3 SQL 注入防护

- 使用参数化查询(MyBatis 预编译)
- 输入验证和过滤
- 最小权限原则

---

## 12. 相关文档

- API 规范: `docs/detailed-design/api-specs.md`
- 数据模型: `docs/detailed-design/data-models.md`
- 类设计: `docs/detailed-design/class-design.md`
- 架构设计: `docs/architecture/architecture.md`

---

## 批准

| 角色 | 姓名 | 签名 | 日期 |
|------|------|------|------|
| 技术负责人 | | | |
| 数据库管理员 | | | |
| 后端开发负责人 | | | |
