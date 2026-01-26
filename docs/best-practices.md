# 最佳实践

## Skills 开发规范

### 组织结构

推荐按功能模块组织 Skills：

```
.claude/skills/
├── database/        # 数据库相关
├── code-gen/        # 代码生成
├── review/          # 代码审查
└── docs/            # 文档生成
```

### 命名约定

| 类型 | 规范 | 示例 |
|------|------|------|
| Skill 名称 | 小写 + 连字符 | `ruoyi-crud` |
| 目录名称 | 同 Skill 名称 | `ruoyi-crud/` |
| 参数名 | 描述性 | `$TABLE_NAME` |

### 文档注释

```markdown
---
name: skill-name
description: 一句话描述

# 技能标题

## 功能
详细说明功能

## 使用方法
示例命令

## 注意事项
重要提示
---
```

## 调试技巧

### 查看 Skill 执行日志

```powershell
claude --debug
```

### 测试 Skill

```powershell
# 测试 Skill
/skill-name test-argument

# 查看详细输出
claude "使用 skill-name 分析"
```

### 常见问题排查

| 问题 | 原因 | 解决方案 |
|------|------|----------|
| Skill 不生效 | 未找到 SKILL.md | 检查文件位置和命名 |
| 参数无法识别 | $ARGUMENTS 格式错误 | 检查参数传递语法 |
| 工具调用失败 | 权限不足 | 添加到 allowed-tools |

## 性能优化

### 减少文件扫描

```yaml
---
allowed-tools: ["Grep"]
---
```

限制工具使用可以减少不必要的文件扫描。

### 使用上下文缓存

```markdown
第一次读取文件后缓存结果，避免重复读取。
```

### 并行执行

对于独立任务，考虑使用多个 Skills 并行执行。

## 团队协作

### Skills 分享

**项目级 Skills**：
```
your-project/.claude/skills/
```
提交到版本控制，团队成员自动共享。

**组织级 Skills**：
```
.claude/skills/
```
通过管理设置部署到组织。

### 版本控制

```bash
# .gitignore
.claude/cache/
.claude/settings.local.json
```

### Code Review

使用 `/code-review` Skill 审查 Skills 本身的代码质量。

## 进阶技巧

### 组合多个 Skills

```
# 先生成代码，再审查
/ruoyi-crud sys_user
/code-review
```

### 创建 Skill 链

在 Skill 中调用其他 Skill：

```markdown
## 执行流程

1. 使用 /ruoyi-crud 生成代码
2. 使用 /code-review 审查代码
3. 使用 /test-gen 生成测试
```

## 下一步

- [附录](./appendix.md)
