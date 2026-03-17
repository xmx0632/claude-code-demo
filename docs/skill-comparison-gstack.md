# Skill 对比分析：gstack vs SDLC-Framework vs 现有 Skills

> 基于 Garry Tan 的 gstack 项目与现有 skills 的深度对比

---

## 一、gstack 核心理念

### 1.1 设计哲学

> "我不想让 AI 工具停留在模糊的模式，我要的是**明确的齿轮（Explicit Gears）**。"
> — Garry Tan

**核心思想：**
- 不是"AI 帮你写代码"，而是"AI 帮你开公司"
- 将通用 AI 拆分成 12 个专业角色
- 每个角色有明确的职责、判断标准和交付要求

### 1.2 gstack 12 个 Skills

| Skill | 角色 | 核心职责 |
|-------|------|----------|
| `/plan-ceo-review` | CEO/创始人 | 重新思考需求，找出 10 星产品 |
| `/plan-eng-review` | 工程经理 | 锁定架构、数据流、边界情况、测试 |
| `/plan-design-review` | 高级产品设计师 | 80 项检查清单，AI Slop 检测，字母评级 |
| `/review` | 偏执型 Staff Engineer | 找出 CI 能过但生产会炸的 bug |
| `/ship` | 发布工程师 | 同步、测试、push、开 PR，一条命令搞定 |
| `/browse` | QA 工程师 | 给 AI 装上眼睛，能看到浏览器里的产品 |
| `/qa` | QA + 修复工程师 | 测试、找 bug、修复、验证 |
| `/qa-only` | QA 报告员 | 只报告 bug，不修复 |
| `/qa-design-review` | 设计师 + 前端工程师 | 设计审计 + 修复 |
| `/setup-browser-cookies` | 会话管理器 | 导入浏览器 cookies |
| `/retro` | 工程经理 | 团队复盘，贡献者分析 |
| `/document-release` | 技术文档工程师 | 更新 README、文档 |

---

## 二、三方对比矩阵

### 2.1 功能覆盖对比

| 功能领域 | gstack | SDLC-Framework | 现有 Skills | 差距分析 |
|----------|--------|----------------|-------------|----------|
| **需求分析** | `/plan-ceo-review` | 01-requirements | ✅ sdlc-requirements | 我们偏文档化，gstack 偏产品思维 |
| **产品设计** | - | 02-product-design | ⚠️ ui-ux-pro-max | 缺少独立的"产品审视"模式 |
| **架构设计** | `/plan-eng-review` | 03-architecture | ✅ sdlc-architecture | 类似 |
| **详细设计** | - | 04-detailed-design | ✅ sdlc-detailed | 我们更完整 |
| **代码开发** | - | 06-code-development | ✅ sdlc-code-development | 类似 |
| **代码审查** | `/review` | - | ✅ sdlc-code-review | gstack 更"偏执" |
| **发布管理** | `/ship` | 14-deployment | ✅ sdlc-deployment | gstack 更自动化 |
| **单元测试** | - | 07-unit-testing | ✅ sdlc-test-gen | 我们有 |
| **集成测试** | - | 08-integration-testing | ⚠️ 合并在 testing | 可拆分 |
| **系统测试** | - | 09-system-testing | ⚠️ 合并在 testing | 可拆分 |
| **QA 测试** | `/qa`, `/browse` | - | ❌ 缺失 | **重大差距** |
| **设计审计** | `/plan-design-review` | - | ⚠️ ui-ux-pro-max | gstack 有 AI Slop 检测 |
| **工程复盘** | `/retro` | - | ❌ 缺失 | **需要补充** |
| **文档更新** | `/document-release` | 12-user-manual | ✅ sdlc-documentation | 类似 |

### 2.2 设计模式对比

| 维度 | gstack | SDLC-Framework | 现有 Skills |
|------|--------|----------------|-------------|
| **角色定义** | ✅ 12 个明确角色 | ✅ 8 个 Subagent | ⚠️ 隐含在描述中 |
| **认知模式切换** | ✅ Explicit Gears | ⚠️ 阶段切换 | ❌ 无明确切换 |
| **输入/输出** | ⚠️ 隐含在描述 | ✅ 表格化定义 | ⚠️ 部分有 |
| **质量门禁** | ⚠️ 隐含 | ✅ Checklist | ⚠️ 部分有 |
| **浏览器集成** | ✅ 内置 Chromium | ❌ 无 | ❌ 无 |
| **外部服务集成** | ✅ Greptile | ❌ 无 | ❌ 无 |
| **阶段依赖** | ⚠️ 隐含 | ✅ 15 阶段流程 | ❌ 无 |

---

## 三、gstack 的创新点

### 3.1 认知模式切换

gstack 最大的创新是**让用户告诉 AI 用什么"脑子"思考**：

```
/plan-ceo-review   → "用创始人思维思考"
/review            → "用偏执型工程师思维审查"
/ship              → "用发布机器思维执行"
```

**我们可以学习：**
- 为每个 skill 定义明确的"认知模式"
- 在 skill 描述中加入角色视角

### 3.2 浏览器集成 (`/browse`, `/qa`)

gstack 内置了持久化的 Chromium 浏览器：
- 首次启动 3 秒，后续 100-200ms
- Cookie 和登录态保持
- AI 可以"看到"产品在浏览器里的样子

**这是重大差距**，我们完全缺失这种能力。

### 3.3 设计审计 + AI Slop 检测

`/plan-design-review` 的核心功能：
- 80 项检查清单
- 10 个类别打分（A-F）
- **AI Slop Score**：检测 AI 生成痕迹
- 推断设计系统，生成 DESIGN.md

### 3.4 自动化工作流

`/ship` 命令自动化整个发布流程：
```
sync main → run tests → resolve reviews → push → open PR
```

`/qa` 命令的完整流程：
```
分析 diff → 识别受影响页面 → 测试 → 找 bug → 修复 → 验证 → 提交
```

---

## 四、Roles/Subagents 对比分析

### 4.1 gstack vs SDLC-Framework 角色对比

| gstack Skill | 角色 | SDLC-Framework Role | 职责对比 |
|--------------|------|---------------------|----------|
| `/plan-ceo-review` | CEO/创始人 | ❌ 无对应 | gstack 有独立的产品战略审视角色 |
| `/plan-eng-review` | 工程经理 | ✅ Architect | 类似，但 SDLC 更偏技术架构 |
| `/plan-design-review` | 产品设计师 | ⚠️ Product Manager | SDLC PM 偏需求，非设计审计 |
| `/review` | 偏执型工程师 | ⚠️ QA Engineer | SDLC QA 偏测试，非代码审查 |
| `/ship` | 发布工程师 | ❌ 无对应 | SDLC 有部署阶段，但无自动化角色 |
| `/browse` | QA 工程师 | ⚠️ QA Engineer | SDLC QA 缺少浏览器能力 |
| `/qa` | QA + 修复 | ⚠️ QA Engineer | gstack 更强调"测+修"闭环 |
| `/qa-only` | QA 报告员 | ⚠️ QA Engineer | gstack 有"只报告不修复"模式 |
| `/qa-design-review` | 设计师+前端 | ❌ 无对应 | 缺少设计+QA 结合的角色 |
| `/setup-browser-cookies` | 会话管理器 | ❌ 无对应 | 浏览器会话管理 |
| `/retro` | 工程经理 | ❌ 无对应 | 缺少工程复盘角色 |
| `/document-release` | 技术文档工程师 | ⚠️ Backend Developer | SDLC 有文档阶段，但非独立角色 |

### 4.2 SDLC-Framework Roles 定义

| Role | 核心职责 | 工具配置 |
|------|----------|----------|
| **Product Manager** | 需求收集、分析、定义 | Read, Write, Edit, Bash |
| **Architect** | 架构设计、技术选型、ADR | Read, Write, Edit, Glob, Grep, Bash |
| **Backend Developer** | API 开发、业务逻辑、测试 | Read, Write, Edit, Glob, Grep, Bash |
| **QA Engineer** | 测试策略、用例设计、质量保证 | Read, Write, Edit, Grep, Bash |

### 4.3 功能重复分析

#### ✅ 无重复（可直接复制）
| gstack Skill | 说明 |
|--------------|------|
| `/plan-ceo-review` | CEO 视角审视，SDLC 无此角色 |
| `/retro` | 工程复盘，SDLC 无此功能 |
| `/browse` | 浏览器测试，SDLC QA 无此能力 |
| `/setup-browser-cookies` | 会话管理，完全缺失 |

#### ⚠️ 部分重叠（需要整合）
| gstack Skill | SDLC 对应 | 整合建议 |
|--------------|-----------|----------|
| `/plan-eng-review` | Architect | 增强 Architect 的工程审查能力 |
| `/plan-design-review` | Product Manager | 新增设计审计子功能 |
| `/review` | sdlc-code-review skill | 合并偏执审查 checklist |
| `/qa`, `/qa-only` | QA Engineer + sdlc-testing | 增强自动化测试流程 |
| `/ship` | sdlc-deployment skill | 增加自动化发布流程 |
| `/document-release` | sdlc-documentation skill | 类似，可合并 |

#### ❌ 不需要复制
| gstack Skill | 原因 |
|--------------|------|
| `/qa-design-review` | 与 ui-ux-pro-max 功能重叠 |

---

## 五、改进计划（补充）

### 5.1 高优先级 - 学习 gstack 创新

| 序号 | 改进项 | 说明 | 工作量 | 状态 |
|------|--------|------|--------|------|
| G1 | 添加认知模式定义 | 在每个 skill 中定义"用什么脑子思考" | 2h | 待开始 |
| G2 | 新增 `/sdlc-ceo-review` | 创始人视角的需求审视 | 3h | ✅ 已完成 |
| G3 | 新增 `/sdlc-qa-browse` | 浏览器自动化测试（需 Playwright） | 4h | ✅ 已完成 |
| G4 | 新增 `/sdlc-retro` | 工程复盘 skill | 2h | 待开始 |
| G5 | 增强 `/sdlc-design-review` | 添加 AI Slop 检测 | 3h | 待开始 |

### 5.2 中优先级 - 工作流自动化

| 序号 | 改进项 | 说明 | 工作量 |
|------|--------|------|--------|
| G6 | 增强 `/sdlc-ship` | 自动化发布流程 | 3h |
| G7 | 增强 `/sdlc-qa` | Diff-aware 测试 + 自动修复 | 4h |
| G8 | 添加外部服务集成 | 支持 Greptile/CodeRabbit 等 | 4h |

### 5.3 低优先级 - 体验优化

| 序号 | 改进项 | 说明 | 工作量 |
|------|--------|------|--------|
| G9 | 添加多会话支持 | 类似 gstack 的 Conductor | 需要平台支持 |
| G10 | 添加历史追踪 | Retro 快照、QA 报告归档 | 2h |

---

## 六、建议新增的 Skills

### 6.1 `/sdlc-ceo-review` - 创始人视角审视

```markdown
---
name: sdlc-ceo-review
description: 以创始人/CEO视角重新审视需求，找出10星产品
---

# CEO 审视模式

## 核心问题

不是问"你要做什么功能"，而是问：
**这个需求背后，那个 10 星产品长什么样？**

## 审视维度

1. **用户价值**：这个需求解决的真实问题是什么？
2. **产品差异化**：为什么用户会选择我们而不是竞品？
3. **增长潜力**：这个功能能带来用户增长吗？
4. **护城河**：这个功能能建立壁垒吗？
5. **时机**：现在是做这个的正确时机吗？

## 输出

- 产品定位建议
- 功能优先级排序
- 潜在风险提示
- 下一步行动建议
```

### 6.2 `/sdlc-retro` - 工程复盘

```markdown
---
name: sdlc-retro
description: 分析代码提交历史，生成工程复盘报告
---

# 工程复盘模式

## 分析维度

1. **提交统计**：commits、LOC、PR 数量
2. **工作模式**：高峰时段、连续工作天数
3. **代码质量**：测试比例、修复比例
4. **热点文件**：最常修改的文件
5. **团队贡献**：各贡献者分析

## 输出

- 本周/本周期工作总结
- Top 3 成果
- Top 3 改进点
- 下周期建议
```

### 6.3 `/sdlc-qa-browse` - 浏览器自动化测试 ✅ 已完成

```markdown
---
name: sdlc-qa-browse
description: 使用 Playwright 进行浏览器自动化测试
allowed-tools: ["Read", "Write", "Edit", "Bash", "mcp__plugin_playwright_*"]
---

# 浏览器自动化测试

## 功能

- 启动浏览器访问应用
- 自动填写表单、点击按钮
- 截图验证
- 检测控制台错误
- 验��页面跳转

## 使用方法

/qa-browse http://localhost:3000 --flow=signup

## 测试流程

1. 分析 git diff，识别受影响页面
2. 启动浏览器，访问目标页面
3. 执行测试操作
4. 截图验证
5. 生成测试报告
```

---

## 七、实施优先级总表

| 优先级 | 来源 | 改进项 | 工作量 |
|--------|------|--------|--------|
| **P0** | gstack | G1: 添加认知模式定义 | 2h |
| **P0** | gstack | G2: 新增 `/sdlc-ceo-review` | 3h |
| **P0** | SDLC | 1-4: 结构标准化 | 5h |
| **P1** | gstack | G3: 新增 `/sdlc-qa-browse` | 4h | ✅ 已完成 |
| **P1** | gstack | G4: 新增 `/sdlc-retro` | 2h |
| **P1** | SDLC | 5-7: 功能补充 | 6h |
| **P2** | gstack | G5: 增强 AI Slop 检测 | 3h |
| **P2** | gstack | G6-G7: 工作流自动化 | 7h |
| **P2** | SDLC | 9-11: 增强完善 | 10h |

**总计工作量：约 42 小时**

---

## 八、总结

### gstack 的核心启示

1. **角色分工** - 把通用 AI 拆成专业角色
2. **认知切换** - 让用户告诉 AI 用什么"脑子"
3. **浏览器集成** - 给 AI 装上眼睛
4. **自动化闭环** - 测试 → 修复 → 验证 → 提交

### 我们的差距

| 差距类型 | 说明 | 优先级 | 状态 |
|----------|------|--------|------|
| **浏览器测试** | ✅ 已完成 | P0 | sdlc-qa-browse |
| **CEO 视角** | 缺少产品思维审视 | P0 | 待开发 |
| **认知模式** | skill 描述缺少角色定义 | P0 |
| **工程复盘** | 缺少 retro 功能 | P1 |
| **AI Slop 检测** | 设计审计缺少此项 | P2 |

### 建议行动

1. **立即**：为现有 skills 添加认知模式定义
2. **本周**：新增 `/sdlc-ceo-review` 和 `/sdlc-retro`
3. **下周**：研究 Playwright 集成，新增 `/sdlc-qa-browse`
4. **持续**：完善结构标准化和功能补充
