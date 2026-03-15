# SDLC Framework SDD 改进计划

**创建日期**: 2026-03-15
**分支**: feature/sdd-improvements
**参考文章**: [规范驱动开发（SDD）](https://www.infoq.com/articles/enterprise-spec-driven-development/)

---

## 背景

对比 SDD（规范驱动开发）方法论与当前 SDLC Framework，发现以下可借鉴的先进理念：

1. **规范作为对话媒介** - 而非静态文档
2. **MCP 服务器集成** - 连接现有需求管理工具
3. **角色特定约束智能体** - 自动注入领域知识
4. **框架反馈循环** - 从 Bug 中学习，强化框架
5. **顶层指导文档** - Constitution/Steering Docs

---

## 改进项清单

### 1. MCP 服务器集成 - 连接需求管理工具

**现状**: 框架独立运行，与 Jira/Linear/Azure DevOps 等工具脱节

**改进方案**:

```
发现阶段 → 从 Jira/Linear 拉取需求 → 自动生成 PRD 模板
    ↓
设计阶段 → 架构师与 AI 确定方案 → 进度回写到需求工具
    ↓
任务阶段 → 在代码仓库中细化 → 完成状态同步
```

**实现步骤**:
1. 创建 MCP Server 连接 Jira API
2. 在 `/sdlc-stages` 中添加 `--from-jira=PROJ-123` 参数
3. 阶段完成时自动回写状态

**新增命令**:
```bash
/sdlc-stages --from-jira=PROJ-123 --stages=requirements-analysis,detailed-design
/bug-fix --from-jira=BUG-456
```

**优先级**: 高
**工作量**: 3-5 天

---

### 2. 角色特定约束智能体

**现状**: 8 种角色定义，但约束分散在各阶段模板中

**改进方案**: 创建角色约束智能体，自动注入领域知识

| 智能体 | 自动注入的约束 |
|--------|----------------|
| `infra-agent` | 部署约束、资源限制、监控要求 |
| `security-agent` | 安全检查、合规要求、敏感数据处理 |
| `performance-agent` | 性能基准、缓存策略、数据库优化 |
| `cost-agent` | 成本估算、资源优化建议 |

**实现方式**:
1. 创建 `agents/` 目录存放各角色智能体配置
2. 在阶段执行时自动调用相关智能体
3. 将约束作为质量门禁的一部分

**文件结构**:
```
SDLC-Framework/
├── agents/
│   ├── infra-agent.md
│   ├── security-agent.md
│   ├── performance-agent.md
│   └── cost-agent.md
└── workflows/
    └── agent-injection-workflow.md
```

**优先级**: 中
**工作量**: 5-7 天

---

### 3. 框架反馈循环

**现状**: Bug 修复后缺乏对框架的反思

**改进方案**: 建立反馈机制，每个 Bug 都用来改进框架

**反馈分类**:

| 缺口类型 | 原因 | 框架改进 |
|----------|------|----------|
| 规范→实现缺口 | 规范清晰但实现跑偏 | 强化验证门禁 |
| 意图→规范缺口 | 需求遗漏细节 | 优化需求引导模板 |
| 角色协作缺口 | 角色间信息丢失 | 添加角色交接检查点 |

**实现方式**:
1. 在 `/bug-fix` 流程结束时添加"框架反思"步骤
2. 创建 `feedback/` 目录记录改进建议
3. 定期评审反馈，更新框架

**新增文件**:
```
SDLC-Framework/
├── feedback/
│   ├── TEMPLATE.md
│   └── records/
│       └── 2026-03-XX_某问题反思.md
```

**优先级**: 高
**工作量**: 2-3 天

---

### 4. 顶层指导文档

**现状**: 缺少跨阶段的统一约束文档

**改进方案**: 添加两层顶层文档

**Constitution（宪法文档）**:
- 项目核心原则
- 不可违反的约束
- 技术栈选择理由
- 架构决策记录

**Steering Docs（导向文档）**:
- 当前迭代重点
- 优先级规则
- 风险区域
- 临时约束

**文件结构**:
```
SDLC-Framework/
├── constitution/
│   ├── core-principles.md
│   ├── tech-stack-decisions.md
│   └── architecture-decisions/
│       └── ADR-001-XXX.md
└── steering/
    ├── current-focus.md
    ├── priorities.md
    └── risk-areas.md
```

**使用方式**:
```bash
/sdlc-stages --stages=design  # 自动读取 constitution/ 和 steering/
```

**优先级**: 中
**工作量**: 2-3 天

---

### 5. 规范动态化

**现状**: 模板是静态的，填完就归档

**改进方案**: 模板作为"活文档"，支持增量更新

**实现方式**:
1. 每个阶段的模板不是一次性产出
2. 变更时先更新相关规范，再改代码
3. 规范版本与代码版本关联

**命令改进**:
```bash
# 当前：执行阶段生成文档
/sdlc-stages --stages=requirements-analysis

# 改进：增量更新
/sdlc-stages --update-spec=requirements-analysis --change="添加XX功能"
```

**优先级**: 低（当前框架已部分支持）
**工作量**: 3-4 天

---

## 实施路线

### Phase 1: 基础强化（1 周）
- [ ] 创建 Constitution/Steering Docs 结构
- [ ] 实现框架反馈循环
- [ ] 更新 `/bug-fix` 流程

### Phase 2: 工具集成（1 周）
- [ ] 开发 Jira MCP Server
- [ ] 集成到 `/sdlc-stages` 命令
- [ ] 测试需求同步

### Phase 3: 智能体增强（1 周）
- [ ] 创建角色约束智能体配置
- [ ] 实现自动注入机制
- [ ] 添加到质量门禁

### Phase 4: 文档完善（3 天）
- [ ] 更新 README
- [ ] 编写使用示例
- [ ] 合并到 main 分支

---

## 验收标准

1. **MCP 集成**: 能从 Jira 拉取需求并回写进度
2. **反馈循环**: 每个 Bug 修复后有框架反思记录
3. **顶层文档**: Constitution/Steering Docs 在阶段执行时被引用
4. **角色智能体**: 至少 2 个智能体（security/performance）生效

---

## 相关文件

- 对比分析: `/Volumes/macext/code/demo/虚拟的我/想法/2026-03-15_TPDD_SDD_SDLC对比分析.md`
- SDD 笔记: `/Volumes/macext/code/demo/虚拟的我/想法/2026-03-15_规范驱动开发SDD.md`
- 当前框架: `/Volumes/macext/code/demo/claude-code-demo/SDLC-Framework/README.md`
