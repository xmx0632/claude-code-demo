# Database Migrations Documentation

> **Project**: TodoList 待办事项管理系统
> **Database**: MySQL 8.0+
> **Migration Tool**: Flyway 9.x
> **Version**: 1.0
> **Created**: 2026-01-26
> **Author**: Database Administrator

---

## Table of Contents

1. [Overview](#overview)
2. [Migration Strategy](#migration-strategy)
3. [Migration History](#migration-history)
4. [Migration Details](#migration-details)
5. [Rollback Procedures](#rollback-procedures)
6. [Verification Queries](#verification-queries)
7. [Troubleshooting](#troubleshooting)

---

## Overview

This document provides comprehensive information about database migrations for the TodoList application. All migrations are managed using **Flyway**, an open-source database migration tool that ensures version control and reproducibility of database schemas.

### Migration Location

```
src/main/resources/db/migration/
```

### Naming Convention

Flyway migrations follow this naming pattern:

```
V{version}__{description}.sql

Example:
V1__init_schema.sql
V2__create_user_table.sql
```

### Database Configuration

- **Database Name**: `todolist`
- **Charset**: `utf8mb4`
- **Collation**: `utf8mb4_unicode_ci`
- **Timezone**: `UTC`
- **Engine**: InnoDB

---

## Migration Strategy

### Version Control

Each migration has a unique version number that increments sequentially:

- V1: Initial schema setup
- V2: User table creation
- V3: Todo table creation
- V4: Category table creation
- V5: Todo-Category relationship table creation
- V6: Sample data insertion

### Execution Order

Migrations are executed in version order (V1 → V2 → V3 → ...). Flyway tracks which migrations have been applied in the `flyway_schema_history` table.

### Transaction Management

- **DDL Statements**: Executed with implicit commit (MySQL limitation)
- **DML Statements**: Wrapped in transactions where appropriate
- **Error Handling**: Failed migrations halt execution and must be manually resolved

### Idempotency

All migrations are designed to be idempotent where possible:

- Use `CREATE TABLE IF NOT EXISTS`
- Use `INSERT ... ON DUPLICATE KEY UPDATE` for data insertion
- Include checks before creating indexes or constraints

---

## Migration History

### Summary

| Version | Description | Type | Date | Author |
|---------|-------------|------|------|--------|
| V1 | Initialize database schema | DDL | 2026-01-26 | DBA |
| V2 | Create sys_user table | DDL + DML | 2026-01-26 | DBA |
| V3 | Create sys_todo table | DDL | 2026-01-26 | DBA |
| V4 | Create sys_category table | DDL + DML | 2026-01-26 | DBA |
| V5 | Create sys_todo_category table | DDL | 2026-01-26 | DBA |
| V6 | Insert sample data | DML | 2026-01-26 | DBA |

### Current State

- **Total Migrations**: 6
- **Database Version**: V6
- **Tables Created**: 4
- **Indexes Created**: 15
- **Foreign Keys**: 4
- **Sample Records**: 1 user, 10 todos, 3 categories, 13 associations

---

## Migration Details

### V1__init_schema.sql

**Description**: Initialize database and set basic configuration

**Actions**:
- Creates `todolist` database if not exists
- Sets charset to `utf8mb4`
- Sets collation to `utf8mb4_unicode_ci`
- Configures timezone to UTC

**Rollback**: Drop database (⚠️ **CAUTION**: Destroys all data)

```sql
DROP DATABASE IF EXISTS `todolist`;
```

**Verification**:
```sql
SHOW DATABASES LIKE 'todolist';

SELECT DEFAULT_CHARACTER_SET_NAME, DEFAULT_COLLATION_NAME
FROM information_schema.SCHEMATA
WHERE SCHEMA_NAME = 'todolist';

SELECT @@session.time_zone;
```

---

### V2__create_user_table.sql

**Description**: Create user table with default admin account

**Actions**:
- Creates `sys_user` table with authentication fields
- Creates unique index on `username`
- Creates index on `deleted` for soft delete queries
- Inserts default admin user (username: `admin`, password: `admin123`)

**Schema**:
- Primary Key: `id` (BIGINT, AUTO_INCREMENT)
- Unique Key: `username` (VARCHAR 50)
- Fields: `password`, `locked`, `locked_at`, `last_login_at`, `created_at`, `updated_at`, `deleted`
- Indexes: `uk_username`, `idx_deleted`

**Rollback**:
```sql
DROP TABLE IF EXISTS `sys_user`;
```

**⚠️ Warning**: Dropping `sys_user` will cascade delete all related data due to foreign key constraints.

**Verification**:
```sql
DESCRIBE sys_user;

SHOW INDEX FROM sys_user;

SELECT id, username, locked, deleted, created_at
FROM sys_user
WHERE username = 'admin';
```

---

### V3__create_todo_table.sql

**Description**: Create todo table with indexes and foreign keys

**Actions**:
- Creates `sys_todo` table with task management fields
- Creates foreign key to `sys_user` (CASCADE DELETE)
- Creates indexes for common query patterns
- Implements optimistic locking with `version` field

**Schema**:
- Primary Key: `id` (BIGINT, AUTO_INCREMENT)
- Foreign Key: `user_id` → `sys_user.id`
- Fields: `title`, `description`, `status`, `priority`, `due_date`, `completed_at`, `version`, `created_at`, `updated_at`, `deleted`
- Indexes: `idx_user_id`, `idx_status`, `idx_priority`, `idx_due_date`, `idx_created_at`, `idx_deleted`

**Business Rules**:
- Status values: `PENDING`, `DONE`
- Priority values: `HIGH`, `MEDIUM`, `LOW`
- Optimistic locking via `version` field

**Rollback**:
```sql
DROP TABLE IF EXISTS `sys_todo`;
```

**⚠️ Warning**: Dropping `sys_todo` will cascade delete related data in `sys_todo_category`.

**Verification**:
```sql
DESCRIBE sys_todo;

SHOW INDEX FROM sys_todo;

SELECT
  CONSTRAINT_NAME,
  TABLE_NAME,
  COLUMN_NAME,
  REFERENCED_TABLE_NAME,
  REFERENCED_COLUMN_NAME
FROM information_schema.KEY_COLUMN_USAGE
WHERE TABLE_SCHEMA = 'todolist'
  AND TABLE_NAME = 'sys_todo'
  AND REFERENCED_TABLE_NAME IS NOT NULL;

SELECT status, COUNT(*) as count
FROM sys_todo
WHERE deleted = 0
GROUP BY status;
```

---

### V4__create_category_table.sql

**Description**: Create category table with default categories

**Actions**:
- Creates `sys_category` table for organizing todos
- Creates foreign key to `sys_user` (CASCADE DELETE)
- Inserts default categories for admin user: `工作`, `个人`, `学习`

**Schema**:
- Primary Key: `id` (BIGINT, AUTO_INCREMENT)
- Foreign Key: `user_id` → `sys_user.id`
- Fields: `name`, `color`, `created_at`, `updated_at`, `deleted`
- Indexes: `idx_user_id`, `idx_deleted`

**Business Rules**:
- Color format: Hexadecimal `#RRGGBB`
- Category names unique per user (application-level validation)

**Rollback**:
```sql
DROP TABLE IF EXISTS `sys_category`;
```

**⚠️ Warning**: Dropping `sys_category` will cascade delete related data in `sys_todo_category`.

**Verification**:
```sql
DESCRIBE sys_category;

SHOW INDEX FROM sys_category;

SELECT id, user_id, name, color, created_at
FROM sys_category
WHERE user_id = 1 AND deleted = 0
ORDER BY id;
```

---

### V5__create_todo_category_table.sql

**Description**: Create many-to-many relationship table

**Actions**:
- Creates `sys_todo_category` table for todo-category associations
- Creates unique constraint to prevent duplicate associations
- Creates foreign keys to both `sys_todo` and `sys_category` (CASCADE DELETE)

**Schema**:
- Primary Key: `id` (BIGINT, AUTO_INCREMENT)
- Foreign Keys: `todo_id` → `sys_todo.id`, `category_id` → `sys_category.id`
- Fields: `created_at`
- Unique Key: `uk_todo_category` (todo_id, category_id)
- Indexes: `idx_todo_id`, `idx_category_id`

**Business Rules**:
- A todo can have multiple categories
- A category can contain multiple todos
- Same todo cannot be associated with same category twice

**Rollback**:
```sql
DROP TABLE IF EXISTS `sys_todo_category`;
```

**Verification**:
```sql
DESCRIBE sys_todo_category;

SHOW INDEX FROM sys_todo_category;

SELECT
  CONSTRAINT_NAME,
  TABLE_NAME,
  COLUMN_NAME,
  REFERENCED_TABLE_NAME,
  REFERENCED_COLUMN_NAME
FROM information_schema.KEY_COLUMN_USAGE
WHERE TABLE_SCHEMA = 'todolist'
  AND TABLE_NAME = 'sys_todo_category'
  AND REFERENCED_TABLE_NAME IS NOT NULL;

SELECT todo_id, COUNT(*) as category_count
FROM sys_todo_category
GROUP BY todo_id;
```

---

### V6__insert_default_data.sql

**Description**: Insert sample data for demonstration

**Actions**:
- Inserts 10 sample todos for admin user
- Creates 13 todo-category associations
- Demonstrates various states, priorities, and due dates
- Wraps all inserts in a transaction

**Sample Data Breakdown**:
- **Total Todos**: 10
  - Pending: 7 (High: 2, Medium: 3, Low: 2)
  - Completed: 3 (High: 2, Medium: 1)
- **Category Associations**: 13
  - 工作 (Work): 9 todos
  - 个人 (Personal): 1 todo
  - 学习 (Learning): 4 todos

**Rollback**:
```sql
DELETE FROM sys_todo_category;
DELETE FROM sys_todo WHERE id <= 10;
```

**Verification**:
```sql
SELECT id, title, status, priority, due_date, created_at
FROM sys_todo
WHERE deleted = 0
ORDER BY created_at DESC;

SELECT
  t.id as todo_id,
  t.title as todo_title,
  c.name as category_name,
  c.color as category_color
FROM sys_todo t
LEFT JOIN sys_todo_category tc ON t.id = tc.todo_id
LEFT JOIN sys_category c ON tc.category_id = c.id
WHERE t.deleted = 0
ORDER BY t.id, c.name;
```

---

## Rollback Procedures

### Complete Database Reset

⚠️ **DANGER**: This will delete all data. Use only in development environments.

```sql
-- Drop all tables in correct order (respecting foreign keys)
DROP TABLE IF EXISTS sys_todo_category;
DROP TABLE IF EXISTS sys_todo;
DROP TABLE IF EXISTS sys_category;
DROP TABLE IF EXISTS sys_user;

-- Optionally drop the database
DROP DATABASE IF EXISTS `todolist`;
```

### Partial Rollback

To rollback specific migrations while preserving data:

1. **Identify the migration to rollback**:
```sql
SELECT version, description, installed_on, success
FROM flyway_schema_history
ORDER BY installed_rank;
```

2. **Manually revert changes** by writing and executing rollback SQL (see individual migration sections above)

3. **Update Flyway history** (⚠️ **Advanced users only**):
```sql
DELETE FROM flyway_schema_history
WHERE version = 'VX';
```

### Cascade Deletion Warning

Due to foreign key constraints with `ON DELETE CASCADE`:

- Dropping `sys_user` will delete all todos and categories
- Dropping `sys_todo` will delete all todo-category associations
- Dropping `sys_category` will delete all todo-category associations

Always verify foreign key dependencies before dropping tables.

---

## Verification Queries

### Schema Verification

**Check all tables exist**:
```sql
SELECT TABLE_NAME, TABLE_TYPE, ENGINE, TABLE_COLLATION
FROM information_schema.TABLES
WHERE TABLE_SCHEMA = 'todolist'
ORDER BY TABLE_NAME;
```

Expected output:
- sys_category
- sys_todo
- sys_todo_category
- sys_user

**Check all foreign keys**:
```sql
SELECT
  CONSTRAINT_NAME,
  TABLE_NAME,
  COLUMN_NAME,
  REFERENCED_TABLE_NAME,
  REFERENCED_COLUMN_NAME
FROM information_schema.KEY_COLUMN_USAGE
WHERE TABLE_SCHEMA = 'todolist'
  AND REFERENCED_TABLE_NAME IS NOT NULL
ORDER BY TABLE_NAME, CONSTRAINT_NAME;
```

**Check all indexes**:
```sql
SELECT
  TABLE_NAME,
  INDEX_NAME,
  COLUMN_NAME,
  SEQ_IN_INDEX,
  NON_UNIQUE
FROM information_schema.STATISTICS
WHERE TABLE_SCHEMA = 'todolist'
ORDER BY TABLE_NAME, INDEX_NAME, SEQ_IN_INDEX;
```

### Data Verification

**Count records in all tables**:
```sql
SELECT 'sys_user' as table_name, COUNT(*) as record_count FROM sys_user WHERE deleted = 0
UNION ALL
SELECT 'sys_todo', COUNT(*) FROM sys_todo WHERE deleted = 0
UNION ALL
SELECT 'sys_category', COUNT(*) FROM sys_category WHERE deleted = 0
UNION ALL
SELECT 'sys_todo_category', COUNT(*) FROM sys_todo_category;
```

Expected output (after V6):
- sys_user: 1
- sys_todo: 10
- sys_category: 3
- sys_todo_category: 13

**Verify data integrity**:
```sql
-- Check for orphaned todos (no user)
SELECT COUNT(*) as orphaned_todos
FROM sys_todo t
LEFT JOIN sys_user u ON t.user_id = u.id
WHERE u.id IS NULL AND t.deleted = 0;

-- Check for orphaned categories (no user)
SELECT COUNT(*) as orphaned_categories
FROM sys_category c
LEFT JOIN sys_user u ON c.user_id = u.id
WHERE u.id IS NULL AND c.deleted = 0;

-- Check for orphaned todo-category associations
SELECT COUNT(*) as orphaned_associations
FROM sys_todo_category tc
LEFT JOIN sys_todo t ON tc.todo_id = t.id
LEFT JOIN sys_category c ON tc.category_id = c.id
WHERE t.id IS NULL OR c.id IS NULL;
```

All queries should return 0.

### Flyway Verification

**Check migration history**:
```sql
SELECT
  installed_rank,
  version,
  description,
  type,
  script,
  checksum,
  installed_by,
  installed_on,
  execution_time,
  success
FROM flyway_schema_history
ORDER BY installed_rank;
```

All migrations should show `success = 1` (true).

---

## Troubleshooting

### Common Issues

#### 1. Migration Fails with "Table Already Exists"

**Problem**: Migration script tries to create a table that already exists.

**Solution**:
```sql
-- Check existing tables
SHOW TABLES;

-- Drop conflicting table (⚠️ verify data first)
DROP TABLE IF EXISTS table_name;

-- Or mark migration as successful (⚠️ advanced)
INSERT INTO flyway_schema_history (installed_rank, version, description, type, script, checksum, installed_by, installed_on, execution_time, success)
VALUES (1, 'V2', 'create_user_table', 'SQL', 'V2__create_user_table.sql', NULL, USER(), NOW(), 0, 1);
```

#### 2. Foreign Key Constraint Fails

**Problem**: Migration fails due to foreign key constraint violation.

**Solution**:
```sql
-- Check foreign key status
SELECT
  CONSTRAINT_NAME,
  TABLE_NAME,
  COLUMN_NAME,
  REFERENCED_TABLE_NAME,
  REFERENCED_COLUMN_NAME
FROM information_schema.KEY_COLUMN_USAGE
WHERE TABLE_SCHEMA = 'todolist'
  AND REFERENCED_TABLE_NAME IS NOT NULL;

-- Temporarily disable foreign key checks (⚠️ use with caution)
SET FOREIGN_KEY_CHECKS = 0;
-- Execute your migration
SET FOREIGN_KEY_CHECKS = 1;
```

#### 3. Character Set Issues

**Problem**: Data insertion fails with character set errors.

**Solution**:
```sql
-- Verify database character set
SELECT DEFAULT_CHARACTER_SET_NAME, DEFAULT_COLLATION_NAME
FROM information_schema.SCHEMATA
WHERE SCHEMA_NAME = 'todolist';

-- Verify table character sets
SELECT TABLE_NAME, TABLE_COLLATION
FROM information_schema.TABLES
WHERE TABLE_SCHEMA = 'todolist';

-- Convert table if needed
ALTER TABLE table_name CONVERT TO CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci;
```

#### 4. Timezone Issues

**Problem**: Timestamps are incorrect due to timezone mismatch.

**Solution**:
```sql
-- Check current timezone
SELECT @@session.time_zone;
SELECT @@global.time_zone;

-- Set timezone to UTC
SET time_zone = '+00:00';

-- Or set in application configuration
-- spring.datasource.url=jdbc:mysql://localhost:3306/todolist?serverTimezone=UTC
```

### Migration Repair

If Flyway history becomes corrupted or out of sync:

1. **Baseline existing database**:
```bash
flyway baseline -baselineVersion=1 -baselineDescription="Initial baseline"
```

2. **Repair checksums**:
```bash
flyway repair
```

3. **Manual intervention** (last resort):
```sql
-- Update flyway history manually
UPDATE flyway_schema_history
SET success = 1
WHERE version = 'VX';
```

⚠️ Always backup database before manual repairs.

---

## Best Practices

### Development

1. **Always version control migration scripts**
2. **Test migrations on local database first**
3. **Write reversible migrations when possible**
4. **Include rollback instructions in comments**
5. **Use transactions for DML operations**
6. **Add comments explaining complex logic**
7. **Verify after each migration**

### Production

1. **Never modify committed migrations**
2. **Always create new migrations for changes**
3. **Backup database before migration**
4. **Test migrations in staging environment**
5. **Schedule migrations during low-traffic periods**
6. **Monitor migration execution time**
7. **Have rollback plan ready**

### Security

1. **Use environment variables for credentials**
2. **Never commit passwords to version control**
3. **Limit database user permissions** (SELECT, INSERT, UPDATE, DELETE only)
4. **Encrypt sensitive data at application level**
5. **Audit migration execution logs**

---

## Related Documentation

- [Database Design Document](../detailed-design/database-design.md)
- [API Specifications](../detailed-design/api-specs.md)
- [Data Models](../detailed-design/data-models.md)
- [Architecture Design](../architecture/architecture.md)

---

## Change Log

| Date | Version | Author | Changes |
|------|---------|--------|---------|
| 2026-01-26 | 1.0 | DBA | Initial migration documentation |

---

**Document Status**: Complete
**Last Updated**: 2026-01-26
**Next Review**: After major schema changes
