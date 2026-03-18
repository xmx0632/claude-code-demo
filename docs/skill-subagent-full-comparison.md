# Skills + Subagents 完整对比分析

> 结合 gstack、SDLC-Framework Roles 和现有 Skills 的综合分析

---

## 一、三方定义总览

### 1.1 gstack 12 Skills

| Skill | 角色 | 认知模式 | 核心能力 |
|-------|------|----------|----------|
| `/plan-ceo-review` | CEO/创始人 | 产品战略思维 | 重新思考需求，找出 10 星产品 |
| `/plan-eng-review` | 工程经理 | 工程架构思维 | 锁定架构、数据流、边界情况 |
| `/plan-design-review` | 产品设计师 | 设计审计思维 | 80 项检查清单，AI Slop 检测 |
| `/review` | 偏执型工程师 | 挑剔审查思维 | 找出 CI 能过但生产会炸的 bug |
| `/ship` | 发布工程师 | 自动化思维 | 同步、测试、push、开 PR |
| `/browse` | QA 工程师 | 浏览器测试思维 | AI 能"看到"产品在浏览器里的样子 |
| `/qa` | QA + 修复工程师 | 闭环测试思维 | 测试 → 找 bug → 修复 → 验证 |
| `/qa-only` | QA 报告员 | 纯测试思维 | 只报告 bug，不修复 |
| `/qa-design-review` | 设计师 + 前端 | 设计 QA 思维 | 设计审计 + 前端修复 |
| `/setup-browser-cookies` | 会话管理器 | 会话管理思维 | 导入浏览器 cookies |
| `/retro` | 工程经理 | 复盘思维 | 团队复盘，贡献者分析 |
| `/document-release` | 技术文档工程师 | 文档思维 | 更新 README、文档 |

### 1.2 SDLC-Framework 4 Roles (Subagents)

| Role | 认知模式 | 核心职责 | 工具配置 |
|------|----------|----------|----------|
| **Product Manager** | 需求分析思维 | 需求收集、分析、定义 | Read, Write, Edit, Bash |
| **Architect** | 架构设计思维 | 架构设计、技术选型、ADR | Read, Write, Edit, Glob, Grep, Bash |
| **Backend Developer** | 实现思维 | API 开发、业务逻辑、测试 | Read, Write, Edit, Glob, Grep, Bash |
| **QA Engineer** | 质量保证思维 | 测试策略、用例设计、质量保证 | Read, Write, Edit, Grep, Bash |

### 1.3 现有 19 Skills

| Skill | 来源 | 认知模式 | 核心能力 |
|-------|------|----------|----------|
| `sdlc-requirements-analysis` | SDLC | 需求分析 | 需求收集、分析、规格说明书 |
| `sdlc-architecture-design` | SDLC | 架构设计 | 系统架构、技术选型 |
| `sdlc-detailed-design` | SDLC | 详细设计 | API 设计、数据模型、类图 |
| `sdlc-code-development` | SDLC | 代码实现 | 业务逻辑、API 接口 |
| `sdlc-code-review` | SDLC | 代码审查 | 规范检查、问题识别 |
| `sdlc-test-gen` | SDLC | 测试生成 | 单元测试生成 |
| `sdlc-testing` | SDLC | 测试执行 | 单元/集成/系统测试 |
| `sdlc-deployment` | SDLC | 部署 | 部署指南、配置文件 |
| `sdlc-documentation` | SDLC | 文档 | 用户手册、运维手册 |
| `sdlc-api-doc` | SDLC | API 文档 | Spring Boot API 文档生成 |
| `sdlc-mermaid-diagram` | SDLC | 图表 | Mermaid 图表生成 |
| `sdlc-flyway-migration` | SDLC | 数据库迁移 | Flyway 脚本管理 |
| `sdlc-sql-optimizer` | SDLC | SQL 优化 | MyBatis SQL 优化建议 |
| `sdlc-ruoyi-crud` | SDLC | CRUD 生成 | Ruoyi 完整 CRUD 代码 |
| `sdlc-ceo-review` | gstack | CEO 审视 | ✅ 新增 - 产品战略审视 |
| `sdlc-retro` | gstack | 工程复盘 | ✅ 新增 - 提交历史分析 |
| `sdlc-qa-browse` | gstack | 浏览器测试 | ✅ 新增 - Playwright 自动化 |
| `ui-ux-pro-max` | 外部 | UI/UX 设计 | 前端界面设计 |
| `doc-convert` | SDLC | 文档转换 | Markdown ↔ Word |

---

## 二、功能覆盖矩阵

### 2.1 按开发阶段分析

| 开发阶段 | gstack | SDLC Role | 现有 Skill | 覆盖状态 | 差距分析 |
|----------|--------|-----------|------------|----------|----------|
| **需求分析** | `/plan-ceo-review` | Product Manager | ✅ sdlc-requirements | ⚠️ 部分 | gstack 有 CEO 视角，我们偏文档化 |
| **产品战略** | `/plan-ceo-review` | ❌ | ✅ sdlc-ceo-review | ✅ 已补 | 新增完成 |
| **架构设计** | `/plan-eng-review` | Architect | ✅ sdlc-architecture | ✅ 完整 | 功能覆盖完整 |
| **详细设计** | - | Architect | ✅ sdlc-detailed | ✅ 完整 | 我们更完整 |
| **设计审计** | `/plan-design-review` | ❌ | ⚠️ ui-ux-pro-max | ⚠️ 部分 | 缺少 AI Slop 检测 |
| **代码开发** | - | Backend Developer | ✅ sdlc-code-development | ✅ 完整 | 功能覆盖完整 |
| **代码审查** | `/review` | ❌ | ✅ sdlc-code-review | ⚠️ 部分 | gstack 更"偏执" |
| **单元测试** | - | Backend Developer | ✅ sdlc-test-gen | ✅ 完整 | 功能覆盖完整 |
| **集成测试** | - | QA Engineer | ⚠️ sdlc-testing | ⚠️ 部分 | 合并在 testing 中 |
| **系统测试** | - | QA Engineer | ⚠️ sdlc-testing | ⚠️ 部分 | 合并在 testing 中 |
| **浏览器测试** | `/browse` | ❌ | ✅ sdlc-qa-browse | ✅ 已补 | 新增完成 |
| **QA 闭环** | `/qa` | QA Engineer | ⚠️ sdlc-testing | ⚠️ 部分 | 缺少"测+修"闭环 |
| **纯 QA 报告** | `/qa-only` | ❌ | ❌ | ❌ 缺失 | 无"只报告不修复"模式 |
| **设计 QA** | `/qa-design-review` | ❌ | ⚠️ ui-ux-pro-max | ⚠️ 部分 | 与 ui-ux 重叠 |
| **发布自动化** | `/ship` | ❌ | ⚠️ sdlc-deployment | ⚠️ 部分 | gstack 更自动化 |
| **文档更新** | `/document-release` | Backend Developer | ✅ sdlc-documentation | ✅ 完整 | 功能覆盖完整 |
| **工程复盘** | `/retro` | ❌ | ✅ sdlc-retro | ✅ 已补 | 新增完成 |
| **会话管理** | `/setup-browser-cookies` | ❌ | ❌ | ❌ 缺失 | 浏览器会话管理 |

### 2.2 按角色能力分析

| 角色能力 | gstack | SDLC Role | 现有 Skill | 整合建议 |
|----------|--------|-----------|------------|----------|
| **产品战略审视** | `/plan-ceo-review` | ❌ 无此角色 | ✅ sdlc-ceo-review | 已完成，保持独立 |
| **需求分析** | - | ✅ Product Manager | ✅ sdlc-requirements | 增强 PM 的战略思维 |
| **架构设计** | `/plan-eng-review` | ✅ Architect | ✅ sdlc-architecture | 整合 gstack 的工程审查 |
| **设计审计** | `/plan-design-review` | ⚠️ PM 部分 | ⚠️ ui-ux-pro-max | 新增 AI Slop 检测 |
| **代码实现** | - | ✅ Backend Developer | ✅ sdlc-code-development | 保持现状 |
| **代码审查** | `/review` | ❌ | ✅ sdlc-code-review | 合并"偏执审查"模式 |
| **测试策略** | - | ✅ QA Engineer | ⚠️ sdlc-testing | 拆分为多个测试 skill |
| **浏览器测试** | `/browse` | ❌ | ✅ sdlc-qa-browse | 已完成，保持独立 |
| **QA 闭环** | `/qa` | ⚠️ QA 部分 | ⚠️ sdlc-testing | 新增"测+修"模式 |
| **发布自动化** | `/ship` | ❌ | ⚠️ sdlc-deployment | 增强自动化流程 |
| **工程复盘** | `/retro` | ❌ | ✅ sdlc-retro | 已完成，保持独立 |

---

## 三、功能重复分析

### 3.1 无重复（独立功能）

| 功能 | gstack | 我们的实现 | 说明 |
|------|--------|------------|------|
| CEO 审视 | `/plan-ceo-review` | `sdlc-ceo-review` | ✅ 已新增 |
| 工程复盘 | `/retro` | `sdlc-retro` | ✅ 已新增 |
| 浏览器测试 | `/browse` | `sdlc-qa-browse` | ✅ 已新增 |
| CRUD 生成 | - | `sdlc-ruoyi-crud` | 我们特有 |
| SQL 优化 | - | `sdlc-sql-optimizer` | 我们特有 |
| Flyway 迁移 | - | `sdlc-flyway-migration` | 我们特有 |

### 3.2 部分重叠（需要整合）

| 功能 | gstack | SDLC | 重叠分析 | 整合建议 |
|------|--------|------|----------|----------|
| **架构审查** | `/plan-eng-review` | Architect + sdlc-architecture | gstack 偏工程审查，SDLC 偏架构设计 | 在 sdlc-architecture 中增加工程审查模式 |
| **代码审查** | `/review` | sdlc-code-review | gstack 更"偏执"，我们偏规范 | 合并 gstack 的偏执审查 checklist |
| **测试执行** | `/qa`, `/qa-only` | QA Engineer + sdlc-testing | gstack 有"测+修"闭环和"只报告"模式 | 拆分 sdlc-testing 为多个模式 |
| **发布流程** | `/ship` | sdlc-deployment | gstack 更自动化 | 增强 sdlc-deployment 的自动化能力 |
| **设计审计** | `/plan-design-review` | ui-ux-pro-max | 都有设计能力，但 gstack 有 AI Slop 检测 | 在 ui-ux-pro-max 中增加 AI Slop 检测 |

### 3.3 不需要复制

| gstack Skill | 原因 |
|--------------|------|
| `/qa-design-review` | 与 `ui-ux-pro-max` 功能重叠，不需要单独复制 |
| `/setup-browser-cookies` | 功能太小，可合并到 `sdlc-qa-browse` |

---

## 四、Roles 与 Skills 映射关系

### 4.1 当前映射

```
Product Manager (Role)
├── sdlc-requirements-analysis (Skill)
└── sdlc-ceo-review (Skill) ← 新增

Architect (Role)
├── sdlc-architecture-design (Skill)
├── sdlc-detailed-design (Skill)
├── sdlc-mermaid-diagram (Skill)
└── sdlc-api-doc (Skill)

Backend Developer (Role)
├── sdlc-code-development (Skill)
├── sdlc-ruoyi-crud (Skill)
├── sdlc-flyway-migration (Skill)
└── sdlc-sql-optimizer (Skill)

QA Engineer (Role)
├── sdlc-testing (Skill)
├── sdlc-test-gen (Skill)
├── sdlc-code-review (Skill) ← 共享
└── sdlc-qa-browse (Skill) ← 新增

无对应 Role
├── sdlc-retro (Skill) ← 新增
├── sdlc-deployment (Skill)
├── sdlc-documentation (Skill)
├── ui-ux-pro-max (Skill)
└── doc-convert (Skill)
```

### 4.2 建议新增的 Roles

基于 gstack 的角色分析，建议新增以下 Roles：

| 新 Role | 对应 gstack | 职责 | 关联 Skills |
|---------|-------------|------|-------------|
| **DevOps Engineer** | `/ship` | 发布自动化、CI/CD | sdlc-deployment |
| **Technical Writer** | `/document-release` | 文档编写、维护 | sdlc-documentation, doc-convert |
| **QA Lead** | `/qa`, `/qa-only` | 测试策略、QA 闭环 | sdlc-testing, sdlc-qa-browse |

---

## 五、整合优先级

### 5.1 P0 - 已完成

| 任务 | 状态 | 说明 |
|------|------|------|
| ✅ 新增 sdlc-ceo-review | 完成 | CEO 视角审视 |
| ✅ 新增 sdlc-retro | 完成 | 工程复盘 |
| ✅ 新增 sdlc-qa-browse | 完成 | 浏览器自动化测试 |

### 5.2 P1 - 需要整合

| 任务 | 工作量 | 说明 |
|------|--------|------|
| 增强 sdlc-code-review | 2h | 合并 gstack 的"偏执审查"模式 |
| 拆分 sdlc-testing | 3h | 拆分为"测+修"、"只报告"两种模式 |
| 增强 sdlc-deployment | 3h | 增加自动化发布流程（类似 `/ship`） |

### 5.3 P2 - 功能增强

| 任务 | 工作量 | 说明 |
|------|--------|------|
| 增强 ui-ux-pro-max | 2h | 添加 AI Slop 检测功能 |
| 新增 DevOps Engineer Role | 2h | 定义发布自动化角色 |
| 新增 Technical Writer Role | 1h | 定义文档编写角色 |
| 整合 setup-browser-cookies | 1h | 合并到 sdlc-qa-browse |

---

## 六、总结

### 6.1 当前状态

| 维度 | gstack | SDLC-Framework | 现有 Skills | 状态 |
|------|--------|----------------|-------------|------|
| **角色定义** | 12 个明确角色 | 4 个 Roles | 隐含在 skill 中 | ⚠️ 需要显式化 |
| **认知模式** | Explicit Gears | 阶段切换 | ❌ 无 | ⚠️ 需要添加 |
| **功能覆盖** | 12 skills | 15 阶段 | 19 skills | ✅ 较完整 |
| **自动化程度** | 高 | 中 | 中 | ⚠️ 需要增强 |
| **浏览器能力** | ✅ 内置 | ❌ | ✅ sdlc-qa-browse | ✅ 已补齐 |

### 6.2 关键差距

1. **认知模式缺失**: 我们的 skills 缺少"用什么脑子思考"的定义
2. **测试闭环不完整**: 缺少"测+修"和"只报告"两种模式
3. **发布自动化不足**: sdlc-deployment 偏文档，缺少自动化执行
4. **Roles 不完整**: 缺少 DevOps、Technical Writer 等角色

### 6.3 下一步行动

1. **立即**: 为现有 skills 添加认知模式定义
2. **本周**: 完成代码审查和测试 skill 的整合
3. **下周**: 增强 sdlc-deployment 的自动化能力
4. **持续**: 完善角色定义和映射关系
