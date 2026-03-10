# Claude Code Skills 目录

本目录包含符合 Claude Code 官方规范的自定义 Skills。

## 📁 目录结构

```
.claude/skills/
├── ruoyi-crud/           # Ruoyi CRUD 代码生成器
├── code-review/          # Java 代码审查助手
├── test-gen/             # 单元测试生成器
├── api-doc/              # API 文档生成器
├── flyway-migration/     # Flyway 数据库迁移助手
├── sql-optimizer/        # SQL 优化助手
└── mermaid-diagram/      # Mermaid 图表生成器
```

## 🎯 Claude Code 官方规范

### Skill 定义文件

每个 Skill 必须包含 `skill.md` 文件（注意是小写的 `skill.md`）：

```markdown
---
name: skill-name
description: 一句话描述何时使用此技能
allowed-tools: ["Read", "Write", "Edit"]
---

# Skill 标题

技能的详细说明...
```

### 必需字段

- `name`: Skill 名称（与目录名匹配）
- `description`: 何时使用此 Skill（触发条件）

### 可选字段

- `allowed-tools`: 限制可用的工具
- `disable-model-invocation`: 是否禁用模型调用
- `user-invocable`: 用户是否可直接调用（默认：true）
- `context`: 执行环境（fork/none）
- `agent`: 子代理类型

## 🚀 可用 Skills

### 1. ruoyi-crud

**用途**: 为 Ruoyi 项目生成完整的 CRUD 代码

**使用**:
```bash
/ruoyi-crud sys_user
```

### 2. code-review

**用途**: 审查 Java 代码，检查规范、潜在问题和优化建议

**使用**:
```bash
/code-review
/code-review src/main/java/com/example/UserController.java
```

### 3. test-gen

**用途**: 为 Service 层方法生成单元测试

**使用**:
```bash
/test-gen UserService
```

### 4. api-doc

**用途**: 解析 Spring Boot Controller 并生成 API 文档

**使用**:
```bash
/api-doc
/api-doc UserController
```

### 5. flyway-migration

**用途**: 管理 Flyway 数据库迁移脚本

**使用**:
```bash
/flyway-migration create --table=sys_user --type=add_column
```

### 6. sql-optimizer

**用途**: 分析 MyBatis SQL 并提供优化建议

**使用**:
```bash
/sql-optimizer
/sql-optimizer UserMapper.xml
```

### 7. mermaid-diagram

**用途**: 生成各种类型的 Mermaid 图表

**使用**:
```bash
/mermaid-diagram --type=architecture --title="系统架构"
```

## 📝 与旧结构的对比

### 旧结构（Skills-Collection/）

```
Skills-Collection/
├── ruoyi-crud/
│   └── SKILL.md      # 大写的 SKILL.md
└── ...
```

**问题**：
- 使用大写的 `SKILL.md`，不符合官方规范
- Skills 不会被 Claude Code 自动识别
- 需要手动指定路径

### 新结构（.claude/skills/）

```
.claude/skills/
├── ruoyi-crud/
│   └── skill.md      # 小写的 skill.md
└── ...
```

**优势**：
- ✅ 使用小写的 `skill.md`，符合官方规范
- ✅ Skills 会被 Claude Code **自动识别**
- ✅ 支持自动补全和智能提示
- ✅ 更好的 IDE 集成

## 🔄 迁移指南

### 从旧结构迁移

如果你有旧的自定义 Skills，按照以下步骤迁移：

1. **创建新目录**
```bash
mkdir -p .claude/skills/your-skill
```

2. **复制并重命名文件**
```bash
cp Skills-Collection/your-skill/SKILL.md .claude/skills/your-skill/skill.md
```

3. **更新 YAML frontmatter**（如果需要）
确保文件名是 `skill.md`（小写）

4. **测试 Skill**
```bash
/your-skill
```

5. **清理旧文件**（可选）
```bash
# 确认新 Skill 工作正常后
rm -rf Skills-Collection/your-skill
```

## 🎨 开发新 Skill

### 步骤 1: 创建目录

```bash
mkdir -p .claude/skills/my-new-skill
```

### 步骤 2: 创建 skill.md

```bash
cat > .claude/skills/my-new-skill/skill.md << 'EOF'
---
name: my-new-skill
description: 我的新技能描述
allowed-tools: ["Read", "Write"]
user-invocable: true
---

# My New Skill

执行我的技能: **$ARGUMENTS**
EOF
```

### 步骤 3: 测试 Skill

```bash
/my-new-skill "测试参数"
```

## 📚 相关文档

- [Claude Code 官方文档](https://code.claude.com/docs)
- [项目 CLAUDE.md](../../CLAUDE.md)
- [Skills 开发指南](../../docs/skills-guide.md)

## ⚠️ 注意事项

1. **文件名必须是 `skill.md`**（小写）
2. **目录名 = Skill 命令名**
3. **YAML frontmatter 必需**
4. **描述要清晰**：说明何时使用此 Skill
5. **测试你的 Skill**：确保它能正常工作

## 🤝 贡献

欢迎贡献新的 Skills！

1. Fork 项目
2. 创建你的 Skill
3. 测试并提交 PR
4. 添加文档说明

---

**最后更新**: 2025-03-09
