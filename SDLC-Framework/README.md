# SDLC Framework

> 简化高效的软件开发框架，基于文档驱动的角色协同机制

[![Framework Version](https://img.shields.io/badge/version-2.0.0-blue.svg)](https://github.com)
[![License](https://img.shields.io/badge/license-MIT-green.svg)](LICENSE)

---

## 概述

SDLC Framework v2.0 是一个简化的软件开发框架，通过**文档驱动的上下文传递**实现角色间高效协同。框架保留 10 个核心技能、3 个精简角色，支持三种场景的灵活切换。

**核心设计理念**：
- 文档是信息传递的载体
- 角色通过文档协作，减少上下文丢失
- 不同场景使用不同的文档组合
- 质量门禁确保交付质量

---

## 核心特性

### 📄 文档驱动协作

6 个核心文档构成信息传递链：

```
需求文档 (Requirements.md)
    │
    ▼
架构文档 (Architecture.md)
    │
    ├───▶ API规范 (API-Specs.md)
    │
    ├───▶ 数据模型 (Data-Models.md)
    │
    └───▶ 测试计划 (Test-Plan.md)
             │
             ▼
       部署文档 (Deployment.md)
```

### 🎯 三种场景

| 场景 | 触发条件 | 必需文档 | 预计时间 |
|------|---------|---------|---------|
| **新项目开发** | 目录为空 | 6 个核心文档 | 1-8 周 |
| **遗留项目维护** | 有 pom.xml/package.json | 变更需求、变更设计、测试清单 | 1 天-2 周 |
| **Bug 修复** | 意图含 "fix/bug/issue" | Bug分析报告、修复验证报告 | 1 小时-2 天 |

### 👥 三角色协同

| 角色 | 职责 | 输出文档 |
|------|------|---------|
| **Architect** | 需求分析、原型设计(可选)、架构设计、API/数据模型 | 需求、原型(可选)、架构、API规范、数据模型 |
| **Developer** | 代码实现、单元测试 | 代码、测试用例 |
| **QA** | 测试计划、测试执行、部署验证 | 测试计划、测试报告、部署文档 |

### 🔄 文档状态追踪

```
draft ──▶ in_review ──▶ approved
  │                    │
  │                    └──▶ blocked ──▶ draft
  │
  └────────────────────▶ deprecated
```

每个文档有独立的状态追踪，依赖关系自动处理阻塞。

### 📦 核心技能

保留的高价值技能：

| Skill | 用途 | 角色 |
|-------|------|------|
| `sdlc-requirements-analysis` | 需求分析 | Architect |
| `ui-ux-pro-max` | UI/UX 原型设计 | Architect (可选) |
| `sdlc-architecture-design` | 架构设计 | Architect |
| `sdlc-detailed-design` | 详细设计 | Architect |
| `sdlc-code-development` | 代码开发 | Developer |
| `sdlc-testing` | 测试 | QA |
| `sdlc-code-review` | 代码审查 | Architect |
| `sdlc-qa-browse` | QA 浏览器测试 | QA |
| `sdlc-qa-browse-legacy` | QA 浏览器测试 (旧版 macOS) | QA |
| `sdlc-flyway-migration` | 数据库迁移 | Developer |
| `sdlc-deployment` | 部署发布 | QA |

---

## 快速开始

### 场景 1: 新项目开发

```bash
# 步骤 1: 需求分析 (Architect)
/sdlc-requirements-analysis "用户认证系统，支持 OAuth2"

# 步骤 2: 原型设计 (可选) (Architect)
/ui-ux-pro-max design "用户登录页面"

# 步骤 3: 架构设计 (Architect)
/sdlc-architecture-design

# 步骤 4: 详细设计 (Architect)
/sdlc-detailed-design

# 步骤 5: 代码开发 (Developer)
/sdlc-code-development

# 步骤 6: 测试 (QA)
/sdlc-testing

# 步骤 7: 部署 (QA)
/sdlc-deployment
```

### 场景 2: 遗留项目维护

```bash
# 步骤 1: 变更需求 (Architect)
/sdlc-requirements-analysis "添加用户头像上传功能" --scope=change

# 步骤 2: 变更设计 (Architect)
/sdlc-detailed-design --update

# 步骤 3: 代码实现 (Developer)
/sdlc-code-development

# 步骤 4: 测试验证 (QA)
/sdlc-testing

# 步骤 5: 部署 (QA)
/sdlc-deployment
```

### 场景 3: Bug 修复

```bash
# 步骤 1: Bug 分析 (Architect)
# 创建 Bug-Analysis.md
/sdlc-requirements-analysis "修复登录超时问题" --scope=bug

# 步骤 2: 修复实现 (Developer)
/sdlc-code-development

# 步骤 3: 验证 (QA)
/sdlc-testing

# 步骤 4: 创建修复报告
# 创建 Fix-Verification.md
```

---

## 目录结构

```
SDLC-Framework/
├── README.md                      # 框架概览（本文件）
│
├── docs/                          # 核心文档定义
│   ├── core-docs.md               # 6 个核心文档定义
│   ├── status-tracking.md         # 文档状态追踪机制
│   └── templates/                 # 文档模板
│       ├── requirements-template.md
│       ├── architecture-template.md
│       ├── api-specs-template.md
│       ├── data-models-template.md
│       ├── test-plan-template.md
│       └── deployment-template.md
│
├── workflows/                     # 工作流定义
│   └── scenarios/                 # 三种场景
│       ├── new-project.md         # 新项目开发
│       ├── legacy-maintenance.md  # 遗留项目维护
│       └── bug-fix.md             # Bug 修复
│
├── roles/                         # 角色定义
│   ├── architect.md               # 架构师角色
│   ├── developer.md               # 开发者角色
│   └── qa.md                      # QA 角色
│
├── guards/                        # 质量约束
│   ├── README.md                  # 约束说明
│   ├── security.md                # 安全约束
│   ├── performance.md             # 性能约束
│   └── infra.md                   # 基础设施约束
│
├── guides/                        # 使用指南
│   └── getting-started.md         # 快速开始
│
├── scripts/                       # 工具脚本
│   ├── sdlc-status.sh             # 文档状态管理
│   └── sdlc-detect.sh             # 场景检测
│
└── config/                        # 配置文件
    └── framework.yaml             # 框架配置
```

---

## 文档状态追踪

### 状态文件

`.sdlc/docs-status.yaml` 记录所有文档状态：

```yaml
project:
  name: "用户认证系统"
  type: "new"
  created_at: "2026-03-19"
  scenario: "new-project"

documents:
  requirements.md:
    status: "approved"
    version: "v1.0"
    owner: "Architect"
    dependencies: []
    blocking: ["architecture.md"]

  architecture.md:
    status: "in_review"
    version: "v0.9"
    owner: "Architect"
    dependencies: ["requirements.md"]
    blocking: ["api-specs.md", "data-models.md"]
```

### 查看文档状态

```bash
# 查看所有文档状态
cat .sdlc/docs-status.yaml

# 生成状态看板
sdlc-docs dashboard
```

---

## 质量门禁

### 新项目场景

- [ ] 需求文档已批准
- [ ] 架构文档已审查
- [ ] API 规范已定义
- [ ] 测试用例已覆盖
- [ ] 部署方案已验证

### 遗留项目维护场景

- [ ] 变更影响已评估
- [ ] 回归测试已定义
- [ ] 向后兼容已验证
- [ ] 回滚方案已准备

### Bug 修复场景

- [ ] Bug 根因已确认
- [ ] 修复方案已评审
- [ ] 回归测试已通过
- [ ] 部署回滚方案已准备

---

## 技能映射

### Architect 负责的技能

| 阶段 | 技能 | 输出 |
|------|------|------|
| 需求分析 | `sdlc-requirements-analysis` | Requirements.md |
| 架构设计 | `sdlc-architecture-design` | Architecture.md |
| 详细设计 | `sdlc-detailed-design` | API-Specs.md, Data-Models.md |
| 代码审查 | `sdlc-code-review` | 审查意见 |

### Developer 负责的技能

| 阶段 | 技能 | 输出 |
|------|------|------|
| 代码开发 | `sdlc-code-development` | 源代码 |
| 数据库迁移 | `sdlc-flyway-migration` | 迁移脚本 |

### QA 负责的技能

| 阶段 | 技能 | 输出 |
|------|------|------|
| 测试 | `sdlc-testing` | 测试报告 |
| 浏览器测试 | `sdlc-qa-browse` | 测试报告 + 截图 |
| 部署 | `sdlc-deployment` | Deployment.md |

---

## 输出文档结构

使用框架后，项目将包含：

```
your-project/
├── .sdlc/
│   └── docs-status.yaml           # 文档状态追踪
│
├── docs/
│   ├── requirements.md             # 需求文档
│   ├── architecture.md             # 架构文档
│   ├── api-specs.md               # API 规范
│   ├── data-models.md             # 数据模型
│   ├── test-plan.md               # 测试计划
│   └── deployment.md              # 部署文档
│
└── src/                           # 源代码
```

---

## 最佳实践

1. **文档先行**: 任何代码实现前，先完成相应文档
2. **状态同步**: 文档状态变更时，更新 `.sdlc/docs-status.yaml`
3. **依赖管理**: 依赖文档未批准时，阻塞文档状态设为 `blocked`
4. **质量门禁**: 不通过质量门禁不进入下一阶段
5. **场景识别**: 开始前明确项目场景，选择合适的工作流

---

## 版本历史

### v2.0.0 (2026-03-19)

**重大简化**:
- ✨ 技能从 20 简化到 10
- ✨ 角色从 8 简化到 3
- ✨ 文档驱动协作机制
- ✨ 三种场景工作流
- ✨ 文档状态追踪系统

**保留技能**:
- sdlc-requirements-analysis
- sdlc-architecture-design
- sdlc-detailed-design
- sdlc-code-development
- sdlc-testing
- sdlc-code-review
- sdlc-qa-browse
- sdlc-qa-browse-legacy
- sdlc-flyway-migration
- sdlc-deployment

### v1.1.0 (2026-03-16)

- 本地需求管理
- 角色约束智能体
- 框架反馈循环

### v1.0.0 (2026-03-01)

- 初始版本发布

---

## 许可证

MIT License - 详见 LICENSE 文件

---

**开始使用**: 阅读 [场景文档](./workflows/scenarios/) 🚀
