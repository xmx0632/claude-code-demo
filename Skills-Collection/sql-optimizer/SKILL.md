---
name: sql-optimizer
description: 分析 MyBatis SQL 并提供优化建议。优化数据库查询时使用。
allowed-tools: ["Read", "Grep", "Glob", "Bash"]
---

# SQL 优化助手

分析 MyBatis Mapper XML 和注解中的 SQL 语句，提供性能优化建议。

## 检查维度

### 1. 索引问题
- 缺少索引的查询
- 索引未生效
- 冗余索引

### 2. 查询问题
- SELECT *
- N+1 查询
- 全表扫描
- 子查询优化

### 3. 性能问题
- 大结果集处理
- 死锁风险
- 连接池配置
- 慢查询

### 4. 最佳实践
- 批量操作
- 事务边界
- 缓存策略

## 使用方法

### 分析所有 Mapper

```
/sql-optimizer
```

### 分析单个 Mapper

```
/sql-optimizer UserMapper.xml
```

### 生成优化报告

```
/sql-optimizer --report=docs/sql-analysis.md
```

## 优化建议格式

```markdown
## SQL 优化报告

### 问题 1：缺少索引（严重）

**位置**: UserMapper.xml:45
**SQL**: SELECT * FROM sys_user WHERE username = 'xxx'
**问题**: username 字段无索引
**建议**: 添加索引 CREATE INDEX idx_username ON sys_user(username)
**收益**: 查询速度提升 10-100 倍

### 问题 2：N+1 查询（中等）

**位置**: UserService.java:120
**代码**: 循环中查询用户角色
**建议**: 使用 JOIN 或批量查询
```

## 优化技巧

### 索引优化原则
- WHERE 条件字段
- JOIN 关联字段
- ORDER BY 字段
- 高频查询字段

### 查询优化
- 避免 SELECT *
- 使用 LIMIT 限制结果集
- 批量操作代替循环
- 使用缓存减少查询

### MyBatis-Plus 最佳实践
- 使用 @Select 注解
- 启用 SQL 日志
- 配置分页插件
- 使用条件构造器

## 注意事项

- 优化前先备份数据
- 在测试环境验证
- 逐步优化，不要一次性改动太多
- 优化后使用 EXPLAIN 验证效果

## 相关工具

- MySQL EXPLAIN
- 慢查询日志
- Performance Schema
