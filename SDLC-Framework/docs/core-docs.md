# 核心文档集合定义

本文档定义 SDLC Framework 的 6 个核心文档，作为角色协同和信息传递的载体。

## 文档依赖链

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

## 文档清单

### 1. 需求文档 (Requirements.md)
- **负责角色**: Architect
- **输出给**: Developer, QA
- **触发条件**: 项目启动、功能新增
- **必需章节**: 需求概述、功能列表、验收标准、干系人

### 2. 架构文档 (Architecture.md)
- **负责角色**: Architect
- **输出给**: Developer, QA, DevOps
- **触发条件**: 需求文档批准后
- **必需章节**: 架构概述、组件设计、技术栈、关键决策

### 3. API规范 (API-Specs.md)
- **负责角色**: Architect + Developer
- **输出给**: Frontend, QA
- **触发条件**: 架构文档批准后
- **必需章节**: 接口列表、请求响应示例、错误码规范

### 4. 数据模型 (Data-Models.md)
- **负责角色**: Architect
- **输出给**: Developer, QA
- **触发条件**: 架构文档批准后
- **必需章节**: ER图、表结构、索引设计

### 5. 测试计划 (Test-Plan.md)
- **负责角色**: QA
- **输出给**: Developer, Stakeholder
- **触发条件**: API 规范完成后
- **必需章节**: 测试范围、测试用例、测试环境

### 6. 部署文档 (Deployment.md)
- **负责角色**: DevOps (QA 兼任)
- **输出给**: Operations, Developer
- **触发条件**: 测试通过后
- **必需章节**: 环境要求、部署步骤、回滚计划

## 分场景文档要求

### 新项目 (6 个文档)
全部核心文档。

### 遗留项目维护 (3 个文档)
1. 变更需求 (Change-Request.md)
2. 变更设计 (Change-Design.md)
3. 测试清单 (Test-Checklist.md)

### Bug 修复 (2 个文档)
1. Bug 分析报告 (Bug-Analysis.md)
2. 修复验证报告 (Fix-Verification.md)

## 文档状态

| 状态 | 说明 |
|------|------|
| `draft` | 草稿，编辑中 |
| `in_review` | 待审查 |
| `approved` | 已批准，可作为依据 |
| `deprecated` | 已废弃，不可使用 |
| `blocked` | 被阻塞，等待依赖 |
