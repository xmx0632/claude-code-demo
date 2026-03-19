# Claude Code Skills 目录

本目录包含符合 Claude Code 官方规范的自定义 Skills。

## 📁 目录结构

```
.claude/skills/
├── SDLC 阶段 Skills
│   ├── requirements-analysis/   # 阶段 1: 需求分析
│   ├── architecture-design/     # 阶段 3: 架构设计
│   ├── detailed-design/         # 阶段 4: 详细设计
│   ├── code-development/        # 阶段 6: 代码开发
│   ├── testing/                 # 阶段 7-11: 测试验证
│   ├── documentation/           # 阶段 12-13: 文档编写
│   └── deployment/              # 阶段 14-15: 部署升级
│
├── 开发工具 Skills
│   ├── ruoyi-crud/              # Ruoyi CRUD 代码生成器
│   ├── code-review/             # Java 代码审查助手
│   ├── test-gen/                # 单元测试生成器
│   ├── api-doc/                 # API 文档生成器
│   ├── flyway-migration/        # Flyway 数据库迁移助手
│   ├── sql-optimizer/           # SQL 优化助手
│   ├── mermaid-diagram/         # Mermaid 图表生成器
│   ├── ui-ux-pro-max/           # UI/UX 设计助手
│   ├── qa-browse/               # QA 测试浏览器（新版）
│   └── qa-browse-legacy/        # QA 测试浏览器（兼容 macOS 11.x）
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

> **注意**: 所有 Skills 命令都带有 `sdlc-` 前缀

### 1. sdlc-ruoyi-crud

**用途**: 为 Ruoyi 项目生成完整的 CRUD 代码

**使用**:
```bash
/sdlc-ruoyi-crud sys_user
```

### 2. sdlc-code-review

**用途**: 审查 Java 代码，检查规范、潜在问题和优化建议

**使用**:
```bash
/sdlc-code-review
/sdlc-code-review src/main/java/com/example/UserController.java
```

### 3. sdlc-test-gen

**用途**: 为 Service 层方法生成单元测试

**使用**:
```bash
/sdlc-test-gen UserService
```

### 4. sdlc-api-doc

**用途**: 解析 Spring Boot Controller 并生成 API 文档

**使用**:
```bash
/sdlc-api-doc
/sdlc-api-doc UserController
```

### 5. sdlc-flyway-migration

**用途**: 管理 Flyway 数据库迁移脚本

**使用**:
```bash
/sdlc-flyway-migration create --table=sys_user --type=add_column
```

### 6. sdlc-sql-optimizer

**用途**: 分析 MyBatis SQL 并提供优化建议

**使用**:
```bash
/sdlc-sql-optimizer
/sdlc-sql-optimizer UserMapper.xml
```

### 7. sdlc-mermaid-diagram

**用途**: 生成各种类型的 Mermaid 图表

**使用**:
```bash
/sdlc-mermaid-diagram --type=architecture --title="系统架构"
```

### 8. sdlc-qa-browse

**用途**: 快速无头浏览器，用于 QA 测试和站点验证

**使用**:
```bash
/sdlc-qa-browse
```

### 9. sdlc-qa-browse-legacy

**用途**: 快速无头浏览器（兼容旧版 macOS Big Sur 11.x）

**使用**:
```bash
/sdlc-qa-browse-legacy
```

**说明**:
- **Playwright 1.58.2** (需要 macOS 12+)
- **Playwright 1.25.2** (兼容 macOS 11.x)

如果你的电脑是 macOS Big Sur 11.x 或更早版本，请使用 `sdlc-qa-browse-legacy`。

---

## 🏢 多项目 / 微服务项目使用指南

当 `projects/` 目录下有多个项目或微服务时，**推荐使用切换工作目录的方式**来指定 Skill 作用的项目。

### 推荐方式：切换工作目录

```bash
# 切换到目标项目目录
cd projects/microservices/user-service

# 现在 AI 知道你在 user-service 中工作
# 以下命令都会作用于 user-service 项目
/sdlc-code-review
/sdlc-test-gen UserService
/sdlc-api-doc
```

### 微服务项目示例

```
projects/
└── microservices/
    ├── user-service/        # 用户服务
    │   ├── src/main/java/
    │   └── pom.xml
    ├── order-service/       # 订单服务
    │   ├── src/main/java/
    │   └── pom.xml
    └── product-service/     # 商品服务
        ├── src/main/java/
        └── pom.xml
```

### 典型工作流

```bash
# 1. 切换到 user-service
cd projects/microservices/user-service

# 2. 进行代码审查
/sdlc-code-review

# 3. 生成单元测试
/sdlc-test-gen UserService

# 4. 生成 API 文档
/sdlc-api-doc

# 5. 切换到 order-service
cd ../order-service

# 6. 在 order-service 中继续工作
/sdlc-sql-optimizer OrderMapper.xml
```

### 其他指定方式

如果不想切换目录，也可以在对话中明确指定：

```
请审查 projects/microservices/user-service/src/main/java/UserController.java 的代码
```

或者在命令中传入路径：

```bash
/sdlc-code-review projects/microservices/user-service/src/main/java
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
