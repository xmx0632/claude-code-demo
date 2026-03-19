# SDLC Framework v2.0 - 快速开始指南

欢迎使用 SDLC Framework v2.0！简化高效的软件开发框架，基于文档驱动的角色协同机制。

---

## 框架概述

SDLC Framework v2.0 是一个简化的软件开发框架，核心特性：

- **3 个角色**: Architect、Developer、QA
- **3 种场景**: 新项目开发、遗留项目维护、Bug 修复
- **文档驱动**: 6 个核心文档作为信息传递载体
- **状态追踪**: 自动管理文档状态和依赖关系

---

## 快速开始

### 步骤 1: 检测项目场景

```bash
# 运行场景检测脚本
./SDLC-Framework/scripts/sdlc-detect.sh
```

输出示例：
```
=== SDLC 场景检测 ===

检测到场景: 新项目开发
建议文档: requirements.md, architecture.md, api-specs.md, data-models.md, test-plan.md, deployment.md
工作流: SDLC-Framework/workflows/scenarios/new-project.md
```

### 步骤 2: 初始化状态追踪

```bash
# 初始化文档状态文件
./SDLC-Framework/scripts/sdlc-status.sh init

# 按提示输入项目信息
```

### 步骤 3: 开始开发流程

根据检测到的场景，执行相应的工作流。

---

## 三种场景使用指南

### 场景 1: 新项目开发

适用于从零开始的新项目。

```bash
# 1. 需求分析 (Architect)
/sdlc-requirements-analysis "用户认证系统，支持 OAuth2 登录"

# 2. 原型设计 (可选) (Architect)
/ui-ux-pro-max design "用户登录页面"

# 3. 架构设计 (Architect)
/sdlc-architecture-design

# 4. 详细设计 (Architect)
/sdlc-detailed-design

# 5. 代码开发 (Developer)
/sdlc-code-development

# 6. 测试 (QA)
/sdlc-testing

# 7. 部署 (QA)
/sdlc-deployment
```

**输出文档**:
- `docs/requirements.md` - 需求文档
- `docs/architecture.md` - 架构文档
- `docs/api-specs.md` - API 规范
- `docs/data-models.md` - 数据模型
- `docs/test-plan.md` - 测试计划
- `docs/deployment.md` - 部署文档

### 场景 2: 遗留项目维护

适用于现有项目的功能维护和优化。

```bash
# 1. 变更需求 (Architect)
/sdlc-requirements-analysis "添加用户头像上传功能" --scope=change

# 2. 变更设计 (Architect)
/sdlc-detailed-design --update

# 3. 代码实现 (Developer)
/sdlc-code-development

# 4. 测试验证 (QA)
/sdlc-testing

# 5. 部署 (QA)
/sdlc-deployment
```

**输出文档**:
- `docs/change-request.md` - 变更需求
- `docs/change-design.md` - 变更设计
- `docs/test-checklist.md` - 测试清单

### 场景 3: Bug 修复

适用于线上问题快速修复。

```bash
# 1. Bug 分析 (Architect)
/sdlc-requirements-analysis "修复登录超时问题" --scope=bug

# 2. 修复实现 (Developer)
/sdlc-code-development

# 3. 验证测试 (QA)
/sdlc-testing

# 4. 生成修复报告
# 创建 docs/fix-verification.md
```

**输出文档**:
- `docs/bug-analysis.md` - Bug 分析报告
- `docs/fix-verification.md` - 修复验证报告

---

## 文档状态管理

### 查看文档状态

```bash
# 查看所有文档状态
./SDLC-Framework/scripts/sdlc-status.sh dashboard

# 查看特定文档状态
./SDLC-Framework/scripts/sdlc-status.sh status requirements.md
```

### 更新文档状态

```bash
# 批准文档
./SDLC-Framework/scripts/sdlc-status.sh approve requirements.md

# 设置文档状态
./SDLC-Framework/scripts/sdlc-status.sh set architecture.md approved

# 查看阻塞关系
./SDLC-Framework/scripts/sdlc-status.sh blocked-by api-specs.md
```

### 状态说明

| 状态 | 说明 | 可执行操作 |
|------|------|------------|
| `draft` | 草稿，编辑中 | 编辑 |
| `in_review` | 待审查 | 审查通过/驳回 |
| `approved` | 已批准 | 作为开发依据 |
| `blocked` | 被阻塞 | 等待依赖解除 |
| `deprecated` | 已废弃 | 不可使用 |

---

## 核心技能

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
| `sdlc-flyway-migration` | 数据库迁移 | Developer |
| `sdlc-deployment` | 部署发布 | QA |

---

## 角色职责

### Architect (架构师)

**职责**:
- 需求分析、原型设计(可选)
- 架构设计、技术选型
- API 规范、数据模型设计
- 代码审查

**输出文档**:
- requirements.md
- architecture.md
- api-specs.md
- data-models.md

### Developer (开发者)

**职责**:
- 代码实现
- 单元测试
- 数据库迁移

**输出**:
- 源代码
- 单元测试
- 迁移脚本

### QA (测试工程师)

**职责**:
- 测试计划
- 测试执行
- 部署验证

**输出文档**:
- test-plan.md
- 测试报告
- deployment.md

---

## 质量约束

框架包含三个质量约束智能体：

| 约束 | 关注领域 | 触发场景 |
|------|----------|---------|
| Security | 认证授权、输入验证、数据加密 | 涉及用户输入、API 接口 |
| Performance | API 响应时间、数据库查询、缓存 | 涉及数据库、大量数据处理 |
| Infra | 部署配置、监控告警、资源限制 | 涉及部署、容器化、CI/CD |

详见 `SDLC-Framework/guards/README.md`

---

## 项目结构

使用框架后，项目将包含：

```
your-project/
├── .sdlc/
│   └── docs-status.yaml        # 文档状态追踪
│
├── docs/                        # 项目文档
│   ├── requirements.md          # 需求文档
│   ├── architecture.md          # 架构文档
│   ├── api-specs.md            # API 规范
│   ├── data-models.md          # 数据模型
│   ├── test-plan.md            # 测试计划
│   └── deployment.md           # 部署文档
│
└── src/                         # 源代码
```

---

## 常见问题

### Q: 如何选择项目场景？

A: 运行 `./SDLC-Framework/scripts/sdlc-detect.sh` 自动检测。

### Q: 文档状态如何管理？

A: 使用 `./SDLC-Framework/scripts/sdlc-status.sh` 管理文档状态。

### Q: 必须完成所有 6 个文档吗？

A: 取决于场景：
- 新项目: 6 个文档
- 遗留项目: 3 个文档
- Bug 修复: 2 个文档

### Q: 原型设计是必须的吗？

A: 不是。原型设计是新项目场景中的可选步骤。

### Q: 如何自定义文档模板？

A: 复制 `SDLC-Framework/docs/templates/` 中的模板到项目目录并修改。

---

## 下一步

- 阅读 [README.md](../README.md) 了解框架概览
- 阅读 [场景文档](../workflows/scenarios/) 了解详细工作流
- 阅读 [角色定义](../roles/) 了解角色职责

---

**开始使用**: 运行 `./SDLC-Framework/scripts/sdlc-detect.sh` 检测场景 🚀
