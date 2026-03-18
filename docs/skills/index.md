# SDLC Skills 使用指南

本页提供所有 SDLC Skills 的快速导航和使用概览。

## 📊 Skills 总览

**总计**: 18 个专业技能，覆盖完整软件开发生命周期

---

## 🚀 快速导航

### 按阶段分类

#### 需求与分析阶段
| Skill | 说明 | 复杂度 | 文档 |
|-------|------|--------|------|
| `/sdlc-requirements-analysis` | 需求分析阶段，生成需求规格说明书 | 🟡 中 | [SKILL.md](../../.claude/skills/sdlc-requirements-analysis/SKILL.md) |
| `/sdlc-ceo-review` | CEO/创始人视角的计划审视 | 🟡 中 | [SKILL.md](../../.claude/skills/sdlc-ceo-review/SKILL.md) |

#### 设计阶段
| Skill | 说明 | 复杂度 | 文档 |
|-------|------|--------|------|
| `/sdlc-architecture-design` | 系统架构设计阶段 | 🟡 中 | [SKILL.md](../../.claude/skills/sdlc-architecture-design/SKILL.md) |
| `/sdlc-detailed-design` | 系统详细设计阶段 | 🟡 中 | [SKILL.md](../../.claude/skills/sdlc-detailed-design/SKILL.md) |
| `/sdlc-mermaid-diagram` | 生成各种 Mermaid 图表 | 🔴 高 | [使用指南](./mermaid.md) / [SKILL.md](../../.claude/skills/sdlc-mermaid-diagram/SKILL.md) |

#### 开发阶段
| Skill | 说明 | 复杂度 | 文档 |
|-------|------|--------|------|
| `/sdlc-code-development` | 代码开发阶段 | 🟡 中 | [SKILL.md](../../.claude/skills/sdlc-code-development/SKILL.md) |
| `/sdlc-ruoyi-crud` | 为 Ruoyi 项目生成 CRUD 代码 | 🟢 低 | [SKILL.md](../../.claude/skills/sdlc-ruoyi-crud/SKILL.md) |
| `/sdlc-api-doc` | 生成 API 文档 | 🟢 低 | [SKILL.md](../../.claude/skills/sdlc-api-doc/SKILL.md) |
| `/sdlc-sql-optimizer` | 分析和优化 SQL | 🟢 低 | [SKILL.md](../../.claude/skills/sdlc-sql-optimizer/SKILL.md) |

#### 数据库阶段
| Skill | 说明 | 复杂度 | 文档 |
|-------|------|--------|------|
| `/sdlc-flyway-migration` | 管理 Flyway 数据库迁移 | 🔴 高 | [使用指南](./flyway-migration.md) / [SKILL.md](../../.claude/skills/sdlc-flyway-migration/SKILL.md) |

#### 测试阶段
| Skill | 说明 | 复杂度 | 文档 |
|-------|------|--------|------|
| `/sdlc-testing` | 测试阶段执行 | 🟡 中 | [SKILL.md](../../.claude/skills/sdlc-testing/SKILL.md) |
| `/sdlc-qa-browse` | 无头浏览器 QA 测试 | 🔴 高 | [使用指南](./qa-browse.md) / [SKILL.md](../../.claude/skills/sdlc-qa-browse/SKILL.md) |
| `/sdlc-qa-report` | 生成 QA 测试报告 | 🟡 中 | [SKILL.md](../../.claude/skills/sdlc-qa-report/SKILL.md) |
| `/sdlc-test-gen` | 生成单元测试 | 🟢 低 | [SKILL.md](../../.claude/skills/sdlc-test-gen/SKILL.md) |
| `/sdlc-code-review` | 代码质量审查 | 🟡 中 | [SKILL.md](../../.claude/skills/sdlc-code-review/SKILL.md) |

#### 部署与文档阶段
| Skill | 说明 | 复杂度 | 文档 |
|-------|------|--------|------|
| `/sdlc-deployment` | 发布阶段，生成部署指南 | 🟡 中 | [SKILL.md](../../.claude/skills/sdlc-deployment/SKILL.md) |
| `/sdlc-documentation` | 文档编写阶段 | 🟡 中 | [SKILL.md](../../.claude/skills/sdlc-documentation/SKILL.md) |
| `/doc-convert` | Markdown ↔ Word 文档转换 | 🟢 低 | [SKILL.md](../../.claude/skills/doc-convert/SKILL.md) |

#### 团队协作
| Skill | 说明 | 复杂度 | 文档 |
|-------|------|--------|------|
| `/sdlc-retro` | 周度工程复盘 | 🔴 高 | [使用指南](./retro.md) / [SKILL.md](../../.claude/skills/sdlc-retro/SKILL.md) |

---

## 📖 独立使用指南

以下是核心 skills 的详细使用指南：

### 1. [QA 测试指南 (qa-browse)](./qa-browse.md)
无头浏览器测试，支持页面交互、截图、响应式验证。

**适用场景**：
- Web 应用 QA 测试
- 页面截图和视觉验证
- 表单和交互流程测试
- 响应式布局检查

### 2. [Mermaid 图表指南 (mermaid)](./mermaid.md)
生成各种类型的 Mermaid 图表。

**适用场景**：
- 系统架构图
- 流程图和时序图
- ER 图和类图

### 3. [数据库迁移指南 (flyway-migration)](./flyway-migration.md)
Flyway 数据库迁移管理。

**适用场景**：
- 数据库版本控制
- 迁移脚本创建和验证
- 数据库回滚

### 4. [团队复盘指南 (retro)](./retro.md)
周度工程复盘和质量分析。

**适用场景**：
- 提交历史分析
- 代码质量指标
- 团队协作评估

---

## 🎯 按使用场景查找

### 我想...
| 场景 | 使用 Skill |
|------|-----------|
| 分析需求 | `/sdlc-requirements-analysis` |
| 设计系统架构 | `/sdlc-architecture-design` |
| 生成数据库迁移脚本 | `/sdlc-flyway-migration` |
| 测试 Web 应用 | `/sdlc-qa-browse` |
| 生成测试报告 | `/sdlc-qa-report` |
| 审查代码质量 | `/sdlc-code-review` |
| 生成 API 文档 | `/sdlc-api-doc` |
| 生成 CRUD 代码 | `/sdlc-ruoyi-crud` |
| 绘制架构图 | `/sdlc-mermaid-diagram` |
| 部署到生产 | `/sdlc-deployment` |
| 写用户手册 | `/sdlc-documentation` |
| 团队复盘 | `/sdlc-retro` |

---

## 🔧 快速开始

### 第一次使用？

1. **选择你的 SDLC 阶段**：需求、设计、开发、测试、部署
2. **查看对应 Skills**：上表按阶段分类
3. **阅读使用指南**：复杂 skill 有独立指南
4. **直接调用**：在 Claude Code 中输入 `/skill-name`

### 示例

```bash
# 分析需求
/sdlc-requirements-analysis "用户认证系统需要支持微信登录"

# 设计架构
/sdlc-architecture-design

# 生成 CRUD 代码
/sdlc-ruoyi-crud sys_user

# QA 测试
/sdlc-qa-browse

# 代码审查
/sdlc-code-review src/main/java/
```

---

## 📚 相关资源

- [SKILLS-Collection 开发指南](../skills-guide.md) - 如何开发自定义 Skill
- [SDLC Framework README](../../SDLC-Framework/README.md) - 完整工作流框架
- [项目 README](../../README.md) - 项目概览
