-- =====================================================
-- TodoList Database Migration Validation Script
-- Purpose: Verify all migrations were applied successfully
-- Usage: Run this script after executing all migrations
-- Author: Database Administrator
-- Date: 2026-01-26
-- =====================================================

USE `todolist`;

-- =====================================================
-- 1. Database Configuration Validation
-- =====================================================

SELECT '=== Database Configuration ===' as '';

SELECT
  SCHEMA_NAME as 'Database',
  DEFAULT_CHARACTER_SET_NAME as 'Charset',
  DEFAULT_COLLATION_NAME as 'Collation'
FROM information_schema.SCHEMATA
WHERE SCHEMA_NAME = 'todolist';

SELECT @@session.time_zone as 'Timezone';

-- =====================================================
-- 2. Table Structure Validation
-- =====================================================

SELECT '=== Table Structure ===' as '';

SELECT
  TABLE_NAME as 'Table',
  ENGINE as 'Engine',
  TABLE_ROWS as 'Rows',
  DATA_LENGTH as 'Data Size (bytes)',
  INDEX_LENGTH as 'Index Size (bytes)',
  CREATE_TIME as 'Created'
FROM information_schema.TABLES
WHERE TABLE_SCHEMA = 'todolist'
  AND TABLE_TYPE = 'BASE TABLE'
ORDER BY TABLE_NAME;

-- =====================================================
-- 3. Foreign Key Validation
-- =====================================================

SELECT '=== Foreign Keys ===' as '';

SELECT
  CONCAT(TABLE_NAME, '.', COLUMN_NAME) as 'Foreign Key',
  CONCAT('REFERENCES ', REFERENCED_TABLE_NAME, '.', REFERENCED_COLUMN_NAME) as 'References'
FROM information_schema.KEY_COLUMN_USAGE
WHERE TABLE_SCHEMA = 'todolist'
  AND REFERENCED_TABLE_NAME IS NOT NULL
ORDER BY TABLE_NAME, CONSTRAINT_NAME;

-- =====================================================
-- 4. Index Validation
-- =====================================================

SELECT '=== Indexes ===' as '';

SELECT
  TABLE_NAME as 'Table',
  INDEX_NAME as 'Index',
  GROUP_CONCAT(COLUMN_NAME ORDER BY SEQ_IN_INDEX SEPARATOR ', ') as 'Columns',
  CASE NON_UNIQUE
    WHEN 0 THEN 'UNIQUE'
    ELSE 'Non-Unique'
  END as 'Type'
FROM information_schema.STATISTICS
WHERE TABLE_SCHEMA = 'todolist'
GROUP BY TABLE_NAME, INDEX_NAME, NON_UNIQUE
ORDER BY TABLE_NAME, INDEX_NAME;

-- =====================================================
-- 5. Data Validation - Record Counts
-- =====================================================

SELECT '=== Record Counts ===' as '';

SELECT
  'sys_user' as 'Table',
  COUNT(*) as 'Total Records',
  SUM(CASE WHEN deleted = 0 THEN 1 ELSE 0 END) as 'Active Records',
  SUM(CASE WHEN deleted = 1 THEN 1 ELSE 0 END) as 'Deleted Records'
FROM sys_user
UNION ALL
SELECT
  'sys_todo',
  COUNT(*),
  SUM(CASE WHEN deleted = 0 THEN 1 ELSE 0 END),
  SUM(CASE WHEN deleted = 1 THEN 1 ELSE 0 END)
FROM sys_todo
UNION ALL
SELECT
  'sys_category',
  COUNT(*),
  SUM(CASE WHEN deleted = 0 THEN 1 ELSE 0 END),
  SUM(CASE WHEN deleted = 1 THEN 1 ELSE 0 END)
FROM sys_category
UNION ALL
SELECT
  'sys_todo_category',
  COUNT(*),
  COUNT(*),
  0
FROM sys_todo_category;

-- =====================================================
-- 6. Data Validation - User Table
-- =====================================================

SELECT '=== Users ===' as '';

SELECT
  id as 'User ID',
  username as 'Username',
  locked as 'Locked',
  last_login_at as 'Last Login',
  created_at as 'Created'
FROM sys_user
WHERE deleted = 0;

-- =====================================================
-- 7. Data Validation - Todo Status Summary
-- =====================================================

SELECT '=== Todo Status Summary ===' as '';

SELECT
  status as 'Status',
  COUNT(*) as 'Count',
  ROUND(COUNT(*) * 100.0 / (SELECT COUNT(*) FROM sys_todo WHERE deleted = 0), 2) as 'Percentage'
FROM sys_todo
WHERE deleted = 0
GROUP BY status
ORDER BY COUNT(*) DESC;

-- =====================================================
-- 8. Data Validation - Todo Priority Summary
-- =====================================================

SELECT '=== Todo Priority Summary ===' as '';

SELECT
  priority as 'Priority',
  COUNT(*) as 'Count',
  ROUND(COUNT(*) * 100.0 / (SELECT COUNT(*) FROM sys_todo WHERE deleted = 0), 2) as 'Percentage'
FROM sys_todo
WHERE deleted = 0
GROUP BY priority
ORDER BY
  CASE priority
    WHEN 'HIGH' THEN 1
    WHEN 'MEDIUM' THEN 2
    WHEN 'LOW' THEN 3
  END;

-- =====================================================
-- 9. Data Validation - Categories
-- =====================================================

SELECT '=== Categories ===' as '';

SELECT
  id as 'Category ID',
  user_id as 'User ID',
  name as 'Category Name',
  color as 'Color',
  created_at as 'Created'
FROM sys_category
WHERE deleted = 0
ORDER BY user_id, name;

-- =====================================================
-- 10. Data Validation - Todo-Category Associations
-- =====================================================

SELECT '=== Todo-Category Associations ===' as '';

SELECT
  c.name as 'Category',
  COUNT(tc.todo_id) as 'Todo Count',
  GROUP_CONCAT(t.title ORDER BY t.id SEPARATOR ', ') as 'Todos'
FROM sys_category c
LEFT JOIN sys_todo_category tc ON c.id = tc.category_id
LEFT JOIN sys_todo t ON tc.todo_id = t.id
WHERE c.deleted = 0
GROUP BY c.id, c.name
ORDER BY COUNT(tc.todo_id) DESC;

-- =====================================================
-- 11. Data Integrity Checks
-- =====================================================

SELECT '=== Data Integrity Checks ===' as '';

-- Check for orphaned todos (todos without users)
SELECT
  'Orphaned Todos (no user)' as 'Check',
  COUNT(*) as 'Count'
FROM sys_todo t
LEFT JOIN sys_user u ON t.user_id = u.id
WHERE u.id IS NULL AND t.deleted = 0;

-- Check for orphaned categories (categories without users)
SELECT
  'Orphaned Categories (no user)' as 'Check',
  COUNT(*) as 'Count'
FROM sys_category c
LEFT JOIN sys_user u ON c.user_id = u.id
WHERE u.id IS NULL AND c.deleted = 0;

-- Check for orphaned associations (associations without todo or category)
SELECT
  'Orphaned Associations (no todo or category)' as 'Check',
  COUNT(*) as 'Count'
FROM sys_todo_category tc
LEFT JOIN sys_todo t ON tc.todo_id = t.id
LEFT JOIN sys_category c ON tc.category_id = c.id
WHERE t.id IS NULL OR c.id IS NULL;

-- Check for duplicate todo-category associations
SELECT
  'Duplicate Todo-Category Associations' as 'Check',
  COUNT(*) - COUNT(DISTINCT CONCAT(todo_id, '-', category_id)) as 'Count'
FROM sys_todo_category;

-- =====================================================
-- 12. Upcoming Due Dates
-- =====================================================

SELECT '=== Upcoming Due Dates (Next 7 Days) ===' as '';

SELECT
  t.id as 'Todo ID',
  t.title as 'Title',
  t.status as 'Status',
  t.priority as 'Priority',
  t.due_date as 'Due Date',
  DATEDIFF(t.due_date, NOW()) as 'Days Remaining',
  u.username as 'Owner'
FROM sys_todo t
JOIN sys_user u ON t.user_id = u.id
WHERE t.deleted = 0
  AND t.status = 'PENDING'
  AND t.due_date IS NOT NULL
  AND t.due_date BETWEEN NOW() AND DATE_ADD(NOW(), INTERVAL 7 DAY)
ORDER BY t.due_date ASC;

-- =====================================================
-- 13. Overdue Todos
-- =====================================================

SELECT '=== Overdue Todos ===' as '';

SELECT
  t.id as 'Todo ID',
  t.title as 'Title',
  t.priority as 'Priority',
  t.due_date as 'Due Date',
  DATEDIFF(NOW(), t.due_date) as 'Days Overdue',
  u.username as 'Owner'
FROM sys_todo t
JOIN sys_user u ON t.user_id = u.id
WHERE t.deleted = 0
  AND t.status = 'PENDING'
  AND t.due_date < NOW()
ORDER BY t.due_date ASC;

-- =====================================================
-- 14. Flyway Migration History
-- =====================================================

SELECT '=== Flyway Migration History ===' as '';

SELECT
  installed_rank as 'Rank',
  version as 'Version',
  description as 'Description',
  type as 'Type',
  installed_on as 'Installed On',
  execution_time as 'Execution Time (ms)',
  CASE success
    WHEN 1 THEN 'Success'
    ELSE 'Failed'
  END as 'Status'
FROM flyway_schema_history
ORDER BY installed_rank;

-- =====================================================
-- End of Validation Script
-- =====================================================

SELECT '=== Validation Complete ===' as '';
