---
name: code-review
description: 审查 Java 代码，检查规范、潜在问题和优化建议。Code Review 时使用。
allowed-tools: ["Read", "Grep", "Glob", "Bash"]
---

# Java 代码审查助手

全面审查 Java 代码质量，提供改进建议。

## 审查维度

### 1. 代码规范
- 命名规范（类名、方法名、变量名）
- 代码格式（缩进、空行）
- 注释完整性
- 包导入顺序

### 2. 潜在 Bug
- 空指针风险
- 资源泄漏（流、连接）
- 并发问题
- 边界条件处理

### 3. 性能问题
- SQL 查询优化
- 循环效率
- 缓存使用
- 集合操作

### 4. 安全问题
- SQL 注入
- XSS 攻击
- 权限校验
- 敏感信息暴露

### 5. 设计问题
- 代码复用
- 耦合度
- SOLID 原则
- 设计模式应用

## 使用方法

### 审查整个项目

```
/code-review
```

### 审查单个文件

```
/code-review src/main/java/com/example/UserController.java
```

### 审查目录

```
/code-review src/main/java/com/example/service/
```

## 输出格式

审查结果以 Markdown 格式输出，包含：
- 总体评分（A/B/C/D）
- 问题列表（按优先级排序）
- 优化建议
- 重构建议

## 最佳实践

**定期审查时机：**
- 提交 PR 前
- 合并代码后
- 重要功能上线前

**结合使用：**
- `/code-review` 审查代码
- `/sql-optimizer` 优化数据库查询
- `/test-gen` 补充测试用例
