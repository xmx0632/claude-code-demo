# SDLC Framework - 快速开始指南

欢迎使用 SDLC Framework！本指南将帮助你快速上手这个完整的软件开发生命周期框架。

---

## 什么是 SDLC Framework？

SDLC Framework 是一个基于 Claude Code 的完整软件开发工作流框架，它提供了：

- **15 个开发阶段**: 从需求分析到系统部署的完整覆盖
- **8 种 Subagent 角色**: 模拟真实团队协作
- **专业模板**: 确保文档质量和一致性
- **质量门禁**: 每个阶段都有质量检查点
- **灵活工作流**: 支持完整 SDLC、敏捷 Sprint、Bug 修复等多种场景

---

## 快速开始

### 步骤 1: 确保环境准备就绪

确保你已安装：
- Claude Code
- Git
- 基本开发工具（根据你的项目类型）

### 步骤 2: 导航到你的项目

```bash
cd /path/to/your/project
```

### 步骤 3: 开始需求分析

```bash
/requirements-analysis "你的项目描述"
```

例如：
```bash
/requirements-analysis "创建一个用户认证系统，支持邮箱和手机号登录"
```

### 步骤 4: 查看生成的文档

需求分析完成后，查看生成的文档：

```bash
cat docs/requirements/requirements-spec.md
cat docs/requirements/user-stories.md
```

### 步骤 5: 继续下一个阶段

```bash
/architecture-design
```

就这样！你已经开始了第一个 SDLC 流程。

---

## 核心概念

### Skills

Skills 是可重用的能力单元，可以通过命令调用：

```bash
/skill-name [参数]
```

**示例 Skills**:
- `/requirements-analysis` - 需求分析
- `/architecture-design` - 架构设计
- `/ruoyi-crud` - CRUD 代码生成
- `/test-gen` - 单元测试生成
- `/code-review` - 代码审查

### Subagents

Subagents 是具有特定角色和职责的专门化 AI 代理：

| 角色 | 职责 |
|------|------|
| Product Manager | 需求收集、用户故事定义 |
| Architect | 系统架构设计、技术选型 |
| Backend Developer | 后端代码实现 |
| Frontend Developer | 前端代码实现 |
| QA Engineer | 测试策略、质量保证 |
| DevOps Engineer | 部署、CI/CD |
| DB Administrator | 数据库设计、迁移 |
| Technical Writer | 文档编写 |

Subagents 会被 Skills 自动调用。

### 模板

模板确保文档的一致性和专业性。每个阶段都有相应的模板：

```
SDLC-Framework/01-requirements-analysis/templates/
├── requirements-template.md
├── user-stories-template.md
├── acceptance-criteria-template.md
└── stakeholders-template.md
```

### 工作流

工作流编排多个 Skills 和 Subagents：

- **Full SDLC**: 完整的 15 阶段工作流
- **Agile Sprint**: 敏捷 Sprint 工作流
- **Bug Fix**: Bug 修复工作流

---

## 常用命令

### 项目级别命令

```bash
# 执行完整的 SDLC
/sdlc-full "项目描述"

# 从特定阶段恢复
/sdlc-resume --from-stage=architecture-design

# 执行特定阶段
/sdlc-stage --stage=requirements-analysis
```

### 阶段级别命令

```bash
# 阶段 1: 需求分析
/requirements-analysis "需求描述"

# 阶段 3: 架构设计
/architecture-design

# 阶段 4: 详细设计
/detailed-design

# 阶段 5: 数据库迁移
/flyway-migration create --table=sys_user

# 阶段 6: 代码生成
/ruoyi-crud sys_user

# 阶段 7: 单元测试
/test-gen UserService

# 代码审查
/code-review
```

---

## 完整示例

让我们从一个简单的例子开始：创建一个用户管理功能。

### 1. 需求分析

```bash
/requirements-analysis "创建用户管理功能，支持用户增删改查"
```

**输出**:
- `docs/requirements/requirements-spec.md`
- `docs/requirements/user-stories.md`
- `docs/requirements/acceptance-criteria.md`
- `docs/requirements/stakeholders.md`

### 2. 架构设计

```bash
/architecture-design
```

**输出**:
- `docs/architecture/architecture.md`
- `docs/architecture/adr-records.md`
- `docs/architecture/component-diagram.md`

### 3. 详细设计

```bash
/detailed-design
```

**输出**:
- `docs/detailed-design/api-specs.md`
- `docs/detailed-design/data-models.md`

### 4. 数据库迁移

```bash
/flyway-migration create --table=sys_user --type=create_table
```

**输出**:
- `projects/ruoyi-example/database-migrations/migrations/V1__create_table_sys_user.sql`
- `projects/ruoyi-example/database-migrations/rollback/V1__rollback.sql`

### 5. 代码生成

```bash
/ruoyi-crud sys_user
```

**输出**:
- `src/main/java/.../controller/SysUserController.java`
- `src/main/java/.../service/ISysUserService.java`
- `src/main/java/.../service/impl/SysUserServiceImpl.java`
- `src/main/java/.../mapper/SysUserMapper.java`
- `src/main/java/.../domain/SysUser.java`

### 6. 单元测试

```bash
/test-gen SysUserService
```

**输出**:
- `src/test/java/.../service/SysUserServiceTest.java`

### 7. 代码审查

```bash
/code-review
```

**输出**:
- 代码审查报告

---

## 项目结构

使用框架后，你的项目将包含：

```
your-project/
├── docs/                          # 项目文档
│   ├── requirements/              # 需求文档
│   ├── design/                    # 设计文档
│   ├── architecture/              # 架构文档
│   ├── detailed-design/           # 详细设计
│   ├── testing/                   # 测试文档
│   ├── acceptance/                # 验收文档
│   ├── user/                      # 用户文档
│   ├── operations/                # 运维文档
│   ├── deployment/                # 部署文档
│   └── upgrade/                   # 升级文档
├── src/                           # 源代码
│   ├── main/java/                 # Java 源代码
│   ├── test/java/                 # 单元测试
│   └── test-integration/java/     # 集成测试
└── projects/                      # 项目目录
    └── ruoyi-example/             # 示例项目
        └── database-migrations/   # 数据库迁移
            ├── migrations/        # Flyway 迁移脚本
            └── rollback/          # 回滚脚本
```

---

## 自定义配置

### 项目级配置

创建项目配置文件 `.sdlc/config.yaml`:

```yaml
framework_version: "1.0"

stages:
  - requirements-analysis
  - architecture-design
  - code-development
  - testing

quality_gates:
  code_coverage: 0.9  # 更严格的覆盖率要求

custom_templates:
  requirements: .sdlc/templates/custom-requirements.md
```

### 自定义模板

1. 复制默认模板到项目目录：
```bash
cp SDLC-Framework/01-requirements-analysis/templates/requirements-template.md .sdlc/templates/
```

2. 修改模板以满足你的需求

3. 在 `.sdlc/config.yaml` 中引用自定义模板

---

## 故障排查

### 问题 1: Skill 未找到

**症状**: `Error: Skill 'xxx' not found`

**解决方案**:
1. 检查 Skill 名称是否正确
2. 确认 Skill 是否在 Skills-Collection 中
3. 对于新 Skills，确保已创建 SKILL.md 文件

### 问题 2: 模板未找到

**症状**: `Error: Template 'xxx' not found`

**解决方案**:
1. 检查模板路径是否正确
2. 确认模板文件存在
3. 使用绝对路径或相对于项目根目录的路径

### 问题 3: 质量门禁失败

**症状**: `Quality gate failed: xxx`

**解决方案**:
1. 查看具体失败的标准
2. 修复相关问题
3. 重新执行质量检查

### 问题 4: 阶段依赖未满足

**症状**: `Stage dependency not satisfied: xxx`

**解决方案**:
1. 确保前置阶段已完成
2. 检查输出文件是否存在
3. 使用 `/sdlc-resume --from-stage=xxx` 从正确的阶段恢复

---

## 下一步

- 阅读 [Subagent 指南](./subagent-guide.md) 了解如何使用 Subagents
- 阅读 [技能集成指南](./skill-integration-guide.md) 了解如何集成自定义 Skills
- 阅读 [最佳实践](./best-practices.md) 了解框架使用的最佳实践
- 查看 [完整工作流文档](../workflows/full-sdlc-workflow.md) 了解所有 15 个阶段

---

## 获取帮助

如果遇到问题：

1. 查看本文档的故障排查部分
2. 查看相关阶段的文档
3. 查看框架的 README
4. 提交 Issue 到项目仓库

---

祝使用愉快！🚀
