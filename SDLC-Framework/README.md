# SDLC Framework

> 完整的软件开发生命周期框架，基于 Claude Code + Skills + Subagents + SDD 增强

[![Framework Version](https://img.shields.io/badge/version-1.1.0-blue.svg)](https://github.com)
[![License](https://img.shields.io/badge/license-MIT-green.svg)](LICENSE)
[![SDD Enhanced](https://img.shields.io/badge/SDD-Enhanced-purple.svg)](./SDD-IMPROVEMENT-PLAN.md)

---

## 概述

SDLC Framework 是一个生产级的软件开发生命周期框架，旨在帮助开发团队使用 Claude Code 完成从需求分析到系统部署的完整开发流程。它提供了 15 个精心设计的阶段、8 种专业角色、完整的模板体系和灵活的工作流编排。

---

## 核心特性

### 🎯 完整的 SDLC 覆盖

15 个开发阶段，覆盖软件开发的完整生命周期：

1. **需求分析** - 收集和分析业务需求
2. **产品设计** - 创建产品原型和线框图
3. **系统架构设计** - 设计系统架构和技术选型
4. **系统详细设计** - 设计 API、数据模型和类图
5. **数据库迁移脚本** - 创建和管理数据库迁移
6. **前后端代码开发** - 实现业务逻辑
7. **单元测试** - 编写和执行单元测试
8. **集成测试** - 执行集成测试
9. **系统测试方案** - 制定测试策略
10. **测试用例编写** - 编写测试用例
11. **系统验收** - 执行验收测试
12. **系统用户手册** - 编写用户文档
13. **系统运维手册** - 编写运维文档
14. **系统部署说明** - 创建部署指南
15. **系统增量升级说明** - 编写升级指南

### 👥 专业角色模拟

8 种 Subagent 角色，模拟真实团队协作：

| 角色 | 职责 |
|------|------|
| **Product Manager** | 需求收集、干系人管理、用户故事定义 |
| **Architect** | 系统架构设计、技术选型、架构决策 |
| **Backend Developer** | 后端代码实现、API 开发、单元测试 |
| **Frontend Developer** | UI 组件实现、前端集成、用户体验 |
| **QA Engineer** | 测试策略、测试用例、质量保证 |
| **DevOps Engineer** | 部署、CI/CD、基础设施、监控 |
| **DB Administrator** | 数据库设计、迁移、优化 |
| **Technical Writer** | 文档编写、用户手册、API 文档 |

### 📄 专业模板体系

每个阶段都配有专业的模板，确保文档质量和一致性：

- 需求规格说明书模板
- 用户故事模板
- 验收标准模板
- 架构设计文档模板
- API 规范模板
- 测试用例模板
- 部署指南模板
- 等 50+ 个专业模板

### ✅ 质量门禁

每个阶段都有严格的质量门禁：

- 自动化检查（代码覆盖率、测试通过率、安全扫描）
- 手动审查（代码审查、设计审查、干系人批准）
- 确保每个阶段的质量达标后才进入下一阶段

### 🔄 灵活工作流

支持多种工作流模式：

- **Full SDLC**: 完整的 15 阶段工作流
- **Agile Sprint**: 敏捷 Sprint 工作流
- **Bug Fix**: Bug 修复工作流（含框架反馈循环）

### 📋 顶层指导文档（SDD 增强）

借鉴 SDD 方法论，新增两层指导文档：

- **Constitution（项目宪法）**: 定义项目的基本原则和约束
- **Steering Docs（方向指导）**: 针对特定领域的详细规范
- **框架反馈循环**: 从每个 Bug 中学习，持续改进框架

详见 [指导文档索引](./guidance/index.md)

### 🔄 框架反馈循环（SDD 增强）

基于 SDD 方法论，每个 Bug 修复后进行框架反思：

```
Bug 报告 → 复现定位 → 框架反思 → 修复实现 → 验证关闭 → 框架更新
```

**缺口分类**:
- **A: 规范→实现** - 规范清楚但实现跑偏
- **B: 意图→规范** - 需求遗漏导致规范缺失
- **C: 角色协作** - 角色交接时信息丢失

详见 [Bug 修复工作流](./workflows/bug-fix-workflow.md)

### 📦 本地需求管理（SDD 增强）

基于 Markdown 的轻量级需求管理，与 SDLC 深度集成：

```
requirements/
├── backlog/      # 待处理需求
├── active/       # 进行中需求
└── completed/    # 已完成需求
```

**特性**:
- 需求文件内置 SDLC 进度追踪表
- 状态自动流转 (backlog → active → completed)
- 与 SDLC 阶段双向同步

详见 [需求管理工作流](./workflows/requirement-sync-workflow.md)

### 🤖 角色约束智能体（SDD 增强）

自动注入领域知识的约束智能体：

| 智能体 | 关注领域 | 触发阶段 |
|--------|----------|----------|
| **Security Agent** | 安全检查、输入验证、权限控制 | 编码/测试/部署 |
| **Performance Agent** | 性能基准、缓存策略、查询优化 | 设计/编码/测试 |
| **Infra Agent** | 部署配置、监控告警、资源限制 | 编码/部署/运维 |

**触发方式**:
- 按阶段自动触发
- 按关键词触发 (登录、查询、部署等)
- 手动指令触发

详见 [智能体注入工作流](./workflows/agent-injection-workflow.md)

---

## 快速开始

### 1. 执行完整工作流

```bash
# 从需求分析开始，执行完整的 SDLC
/sdlc-full "创建用户认证系统，支持 OAuth2 登录"
```

### 2. 分阶段执行

```bash
# 步骤 1: 需求分析
/requirements-analysis "创建用户认证系统"

# 步骤 2: 架构设计
/architecture-design

# 步骤 3: 详细设计
/detailed-design

# 步骤 4: 数据库迁移
/flyway-migration create --table=sys_user

# 步骤 5: 代码生成
/ruoyi-crud sys_user

# 步骤 6: 单元测试
/test-gen UserService

# 步骤 7: 代码审查
/code-review
```

### 3. 从特定阶段恢复

```bash
# 从架构设计阶段恢复
/sdlc-resume --from-stage=architecture-design
```

---

## 目录结构

```
SDLC-Framework/
├── README.md                      # 框架概览（本文件）
├── SDD-IMPROVEMENT-PLAN.md        # SDD 改进计划
├── framework-config.yaml          # 全局配置
│
├── 01-requirements-analysis/      # 阶段 1: 需求分析
├── 02-product-design/             # 阶段 2: 产品设计
├── 03-architecture-design/        # 阶段 3: 系统架构设计
├── 04-detailed-design/            # 阶段 4: 系统详细设计
├── 05-database-migration/         # 阶段 5: 数据库迁移
├── 06-code-development/           # 阶段 6: 代码开发
├── 07-unit-testing/               # 阶段 7: 单元测试
├── 08-integration-testing/        # 阶段 8: 集成测试
├── 09-system-testing/             # 阶段 9: 系统测试
├── 10-test-case-writing/          # 阶段 10: 测试用例编写
├── 11-system-acceptance/          # 阶段 11: 系统验收
├── 12-user-manual/                # 阶段 12: 用户手册
├── 13-operations-manual/          # 阶段 13: 运维手册
├── 14-deployment-instructions/    # 阶段 14: 部署说明
├── 15-incremental-upgrade/        # 阶段 15: 增量升级
│
├── subagents/                     # Subagent 角色定义
│   ├── product-manager-agent.md
│   ├── architect-agent.md
│   ├── backend-developer-agent.md
│   ├── frontend-developer-agent.md
│   ├── qa-engineer-agent.md
│   ├── devops-engineer-agent.md
│   ├── db-admin-agent.md
│   └── technical-writer-agent.md
│
├── agents/                        # 约束智能体（SDD 增强）
│   ├── security-agent.md          # 安全约束
│   ├── performance-agent.md       # 性能约束
│   └── infra-agent.md             # 基础设施约束
│
├── requirements/                  # 本地需求管理（SDD 增强）
│   ├── README.md                  # 使用说明
│   ├── TEMPLATE.md                # 需求模板
│   ├── backlog/                   # 待处理
│   ├── active/                    # 进行中
│   └── completed/                 # 已完成
│
├── workflows/                     # 工作流编排
│   ├── full-sdlc-workflow.md      # 完整 SDLC 工作流
│   ├── agile-sprint-workflow.md   # 敏捷 Sprint 工作流
│   ├── bug-fix-workflow.md        # Bug 修复工作流
│   ├── requirement-sync-workflow.md   # 需求同步工作流
│   └── agent-injection-workflow.md    # 智能体注入工作流
│
├── guides/                        # 框架指南
│   ├── getting-started.md         # 快速开始
│   ├── subagent-guide.md          # Subagent 指南
│   ├── skill-integration-guide.md # 技能集成指南
│   └── best-practices.md          # 最佳实践
│
├── guidance/                      # 顶层指导文档（SDD 增强）
│   ├── CONSTITUTION.md            # 项目宪法
│   ├── STEERING-DOCS.md           # 方向指导索引
│   ├── templates/                 # 规范模板
│   └── feedback/                  # 框架反馈记录
│       ├── FRAMEWORK-FEEDBACK.md  # 反馈总览
│       └── templates/             # 反思模板
│
└── config/                        # 配置文件
    ├── stage-dependencies.yaml    # 阶段依赖关系
    ├── skill-mapping.yaml         # 技能映射
    └── quality-gates.yaml         # 质量门禁
```

---

## 使用场景

### 场景 1: 新项目启动

适用于从零开始的新项目：

```bash
# 执行完整的 SDLC
/sdlc-full "电商平台订单管理系统"
```

### 场景 2: 功能新增

适用于为现有系统添加新功能：

```bash
# 只执行需要的阶段
/sdlc-stages --stages=requirements-analysis,detailed-design,code-development
```

### 场景 3: 敏捷开发

适用于 2 周的敏捷 Sprint：

```bash
# 使用敏捷 Sprint 工作流
/agile-sprint --sprint=23 --stories=5
```

### 场景 4: Bug 修复

适用于快速修复 Bug：

```bash
# 使用 Bug 修复工作流
/bug-fix --ticket=BUG-123
```

---

## 输出文档

使用框架后，你的项目将包含完整的文档体系：

```
your-project/
├── docs/
│   ├── requirements/              # 需求文档
│   │   ├── requirements-spec.md
│   │   ├── user-stories.md
│   │   └── acceptance-criteria.md
│   ├── design/                    # 设计文档
│   │   ├── wireframes.md
│   │   └── ui-flow.md
│   ├── architecture/              # 架构文档
│   │   ├── architecture.md
│   │   └── adr-records.md
│   ├── detailed-design/           # 详细设计
│   │   ├── api-specs.md
│   │   └── data-models.md
│   ├── testing/                   # 测试文档
│   │   ├── test-plan.md
│   │   └── test-cases.md
│   ├── user/                      # 用户文档
│   │   ├── user-manual.md
│   │   └── quick-start.md
│   ├── operations/                # 运维文档
│   │   ├── ops-manual.md
│   │   └── monitoring.md
│   ├── deployment/                # 部署文档
│   │   └── deployment-guide.md
│   └── upgrade/                   # 升级文档
│       └── upgrade-guide.md
└── src/                           # 源代码
```

---

## 配置和自定义

### 项目级配置

创建 `.sdlc/config.yaml` 自定义框架行为：

```yaml
framework_version: "1.0"

stages:
  - requirements-analysis
  - architecture-design
  - code-development

quality_gates:
  code_coverage: 0.9

custom_templates:
  requirements: .sdlc/templates/custom-requirements.md
```

### 自定义模板

1. 复制默认模板
2. 根据需求修改
3. 在配置中引用

详细说明请参考 [快速开始指南](./guides/getting-started.md)。

---

## 集成现有 Skills

框架集成了现有的 Skills：

- `ruoyi-crud` - CRUD 代码生成
- `code-review` - 代码审查
- `test-gen` - 单元测试生成
- `api-doc` - API 文档生成
- `sql-optimizer` - SQL 优化
- `flyway-migration` - Flyway 迁移管理

并添加了新的 Skills：

- `requirements-analysis` - 需求分析
- `architecture-design` - 架构设计
- `product-design` - 产品设计
- `detailed-design` - 详细设计
- 等等...

---

## 最佳实践

1. **按顺序执行**: 除非明确说明可以并行，否则按阶段顺序执行
2. **质量优先**: 不通过质量门禁不要进入下一阶段
3. **文档同步**: 保持文档与代码同步更新
4. **版本控制**: 所有文档和代码都应该版本控制
5. **定期审查**: 每个阶段完成后进行审查
6. **沟通协作**: 保持与干系人的定期沟通

详细的最佳实践请参考 [最佳实践指南](./guides/best-practices.md)。

---

## 文档

- [快速开始指南](./guides/getting-started.md) - 如何快速上手
- [Subagent 指南](./guides/subagent-guide.md) - 如何使用 Subagents
- [技能集成指南](./guides/skill-integration-guide.md) - 如何集成自定义 Skills
- [最佳实践](./guides/best-practices.md) - 框架使用的最佳实践
- [完整 SDLC 工作流](./workflows/full-sdlc-workflow.md) - 15 个阶段详解

---

## 优势

1. **完整覆盖**: 15 个阶段覆盖完整 SDLC
2. **角色分离**: 8 个 Subagent 模拟真实团队协作
3. **模板驱动**: 确保文档质量和一致性
4. **质量门禁**: 每个阶段都有质量检查点
5. **灵活组合**: 支持多种工作流场景
6. **可扩展**: 易于添加新阶段、新技能、新 Subagent
7. **生产级**: 可直接应用于实际项目

---

## 适用场景

- ✅ 新项目开发
- ✅ 功能模块开发
- ✅ 敏捷 Sprint
- ✅ Bug 修复
- ✅ 技术债务重构
- ✅ 系统升级迁移

---

## 注意事项

1. **模板质量**: 模板需要根据项目特点定制
2. **Subagent 上下文**: Subagent 需要足够的上下文信息
3. **阶段依赖**: 确保阶段依赖关系正确
4. **质量门禁**: 平衡自动化检查和人工审查
5. **文档维护**: 框架文档需要持续更新

---

## 版本

当前版本: **1.1.0** (SDD Enhanced)

### 更新日志

**v1.1.0** (2026-03-16)
- ✨ 新增本地需求管理（Markdown 格式）
- ✨ 新增角色约束智能体（Security/Performance/Infra）
- ✨ 新增智能体注入工作流
- ✨ 新增需求同步工作流
- 📝 完善 SDD 改进计划文档

**v1.0.0** (2026-03-01)
- 🎉 初始版本发布
- ✅ 15 个 SDLC 阶段
- ✅ 8 个 Subagent 角色
- ✅ 50+ 模板

---

## 许可证

MIT License - 详见 LICENSE 文件

---

## 贡献

欢迎贡献！请提交 Pull Request 或 Issue。

---

## 联系方式

如有问题或建议，请提交 Issue 到项目仓库。

---

**开始使用**: 阅读 [快速开始指南](./guides/getting-started.md) 🚀
