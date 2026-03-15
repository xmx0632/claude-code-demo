# SDLC 框架使用指南

## 框架概述

SDLC Framework 是一个生产级的软件开发生命周期框架，提供从需求分析到系统部署的完整开发流程。

```
需求分析 → 产品设计 → 架构设计 → 详细设计 → 数据库迁移
    ↓
代码开发 → 单元测试 → 集成测试 → 系统测试 → 测试用例
    ↓
系统验收 → 用户手册 → 运维手册 → 部署说明 → 增量升级
```

## 15 个阶段

| 阶段 | 名称 | 输出 | Skill |
|------|------|------|-------|
| 01 | 需求分析 | 需求规格说明书 | `requirements-analysis` |
| 02 | 产品设计 | 线框图、原型 | `product-design` |
| 03 | 系统架构设计 | 架构文档、ADR | `architecture-design` |
| 04 | 系统详细设计 | API 规范、数据模型 | `detailed-design` |
| 05 | 数据库迁移 | Flyway 脚本 | `flyway-migration` |
| 06 | 代码开发 | 源代码 | `ruoyi-crud` |
| 07 | 单元测试 | 测试用例 | `test-gen` |
| 08 | 集成测试 | 集成测试 | - |
| 09 | 系统测试 | 测试方案 | - |
| 10 | 测试用例编写 | 测试用例 | - |
| 11 | 系统验收 | 验收报告 | - |
| 12 | 用户手册 | 用户文档 | - |
| 13 | 运维手册 | 运维文档 | - |
| 14 | 部署说明 | 部署指南 | - |
| 15 | 增量升级 | 升级指南 | - |

## 8 个 Subagent 角色

| 角色 | 职责 | 使用场景 |
|------|------|----------|
| Product Manager | 需求收集、干系人管理 | 需求分析阶段 |
| Architect | 系统架构设计、技术选型 | 架构设计阶段 |
| Backend Developer | 后端代码实现 | 代码开发阶段 |
| Frontend Developer | UI 组件实现 | 前端开发阶段 |
| QA Engineer | 测试策略、质量保证 | 测试阶段 |
| DevOps Engineer | 部署、CI/CD | 部署阶段 |
| DB Administrator | 数据库设计、迁移 | 数据库设计阶段 |
| Technical Writer | 文档编写 | 文档阶段 |

## 质量门禁

每个阶段都有质量门禁，确保质量达标后才进入下一阶段。

```yaml
# 示例: 代码开发阶段质量门禁
quality_gates:
  code-development:
    - name: code_review_passed
      severity: critical
      criteria:
        critical_issues: 0
        major_issues: <= 3

    - name: unit_tests_written
      severity: high
      criteria:
        test_coverage: >= 0.6
```

## 工作流模式

### Full SDLC

```bash
# 执行完整的 15 个阶段
/sdlc-full "创建用户认证系统"
```

### Agile Sprint

```bash
# 2 周 Sprint 工作流
/agile-sprint --sprint=23 --stories=5
```

### Bug Fix

```bash
# Bug 修复工作流
/bug-fix --ticket=BUG-123
```

## 配置文件

### 阶段依赖配置

```yaml
# SDLC-Framework/config/stage-dependencies.yaml
stage_dependencies:
  requirements-analysis: []
  product-design:
    - requirements-analysis
  architecture-design:
    - product-design
  # ...
```

### Skill 映射配置

```yaml
# SDLC-Framework/config/skill-mapping.yaml
skill_mapping:
  requirements-analysis:
    skill: "requirements-analysis"
    agent: "product-manager"

  code-development:
    skill: "ruoyi-crud"
    agent: "backend-developer"
```

## 目录结构

```
SDLC-Framework/
├── 01-requirements-analysis/
│   ├── SKILL.md
│   └── templates/
├── 02-product-design/
├── ...
├── subagents/
│   ├── product-manager-agent.md
│   ├── architect-agent.md
│   └── ...
├── workflows/
│   ├── full-sdlc-workflow.md
│   └── ...
├── config/
│   ├── quality-gates.yaml
│   └── ...
└── guides/
    ├── getting-started.md
    └── ...
```

## 快速开始

```bash
# 1. 需求分析
/requirements-analysis "用户认证系统"

# 2. 架构设计
/architecture-design

# 3. 详细设计
/detailed-design

# 4. 数据库迁移
/flyway-migration create --table=sys_user

# 5. 代码生成
/ruoyi-crud sys_user

# 6. 单元测试
/test-gen UserService

# 7. 代码审查
/code-review
```

---

**最后更新**: 2026-03-15
