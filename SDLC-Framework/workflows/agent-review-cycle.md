# Agent-to-Agent 审查循环

## 概述

Agent-to-Agent 审查循环是一种自动化质量保证机制，通过多个 Agent 协作来确保代码质量。

```
┌─────────────────────────────────────────────────────────────┐
│                    Agent Review Cycle                       │
├─────────────────────────────────────────────────────────────┤
│                                                               │
│  ┌──────────────┐      ┌──────────────┐      ┌────────────┐ │
│  │     Code     │─────→│  Architecture │─────→│   Quality  │ │
│  │   Developer  │      │    Auditor   │      │   Gates    │ │
│  │    Agent     │      │     Agent    │      │            │ │
│  └──────────────┘      └──────────────┘      └────────────┘ │
│         │                      │                     │       │
│         └──────────────────────┴─────────────────────┘       │
│                                │                             │
│                           通过/失败                           │
│                                                               │
└─────────────────────────────────────────────────────────────┘
```

---

## 工作流阶段

### 阶段 1: 代码生成

**Agent**: Code Developer Agent

**职责**:
- 根据需求生成代码
- 遵循架构规范
- 编写基础测试

**输入**:
- 需求文档
- 架构设计
- API 规范

**输出**:
- 源代码文件
- 测试文件
- 变更摘要

---

### 阶段 2: 架构审计

**Agent**: Architecture Auditor Agent

**职责**:
- 运行 Linter 检查
- 验证架构规则
- 检测依赖违规
- 评估代码复杂度

**输入**:
- 生成的代码
- 架构规则 (`config/architecture-rules.yaml`)

**输出**:
- 审计报告 (JSON)
- 违规列表
- 修复建议

**检查项**:
- [x] 分层架构合规
- [x] 无循环依赖
- [x] 模块边界正确
- [x] 安全规则通过
- [x] 复杂度在限值内

---

### 阶段 3: 反馈与修复

**条件**: 审计发现违规

**流程**:

1. **审计 Agent** 将违规信息反馈给 **Developer Agent**
2. **Developer Agent** 根据建议修复代码
3. **审计 Agent** 重新检查修复后的代码
4. 循环直到所有违规修复或达到最大重试次数

```
┌──────────────┐      发现违规
│  Developer   │◄────────────┐
│    Agent     │              │
└──────┬───────┘              │
       │ 生成代码              │
       ▼                       │
┌──────────────┐              │
│   Auditor    │──────────────┘
│    Agent     │ 反馈违规
└──────────────┘
       │
   有违规?
       │
      Yes ──→ 返回 Developer
       │
      No
       ▼
┌──────────────┐
│ Quality Gates│
│    通过      │
└──────────────┘
```

---

### 阶段 4: 质量门禁

**Agent**: Quality Gate Agent

**职责**:
- 汇总所有检查结果
- 验证质量门禁
- 决定通过/失败

**门禁标准**:

| 检查项 | 严重 | 错误 | 警告 |
|--------|------|------|------|
| 架构违规 | 0 | 0 | ≤ 5 |
| 安全问题 | 0 | 0 | - |
| 代码复杂度 | - | - | ≤ 10 |
| 测试覆盖率 | ≥ 80% | - | - |

**决策逻辑**:

```yaml
if critical_issues == 0 and error_issues == 0:
    if warning_issues <= 5:
        status = "PASS"
    else:
        status = "REVIEW"
else:
    status = "FAIL"
```

---

## 配置文件

### 审查循环配置

```yaml
# SDLC-Framework/config/agent-review-cycle.yaml
agent_review_cycle:
  enabled: true

  # 参与的 Agent
  agents:
    - name: code_developer
      agent: backend-developer-agent
      phase: generate

    - name: architecture_auditor
      agent: architecture-auditor-agent
      phase: audit

    - name: quality_gate
      agent: quality-gate-agent
      phase: validate

  # 最大重试次数
  max_retries: 3

  # 失败后操作
  on_failure:
    - notify_developer
    - create_issue
    - block_merge

  # Linter 配置
  linter:
    script: "SDLC-Framework/scripts/agent-linter.sh"
    output_format: "json"
    fail_on: ["critical", "error"]

  # 质量门禁
  quality_gates:
    config: "SDLC-Framework/config/quality-gates.yaml"
    strict_mode: false
```

---

## 执行示例

### 场景 1: 无违规，直接通过

```bash
# 1. Developer 生成代码
Code Developer: "生成 UserService 和 UserMapper"

# 2. Auditor 审计
Architecture Auditor: 运行 Linter...
Architecture Auditor: ✓ 无违规发现

# 3. Quality Gate 验证
Quality Gate: ✓ 所有门禁通过

# 结果: PASS
```

### 场景 2: 有违规，修复后通过

```bash
# 1. Developer 生成代码
Code Developer: "生成 UserController 直接调用 UserMapper"

# 2. Auditor 审计
Architecture Auditor: ✗ 发现严重违规
Architecture Auditor: - Controller 不能直接访问 Mapper
Architecture Auditor: - 建议创建 UserService

# 3. Developer 修复
Code Developer: "创建 UserService，修改 UserController"

# 4. Auditor 重新审计
Architecture Auditor: ✓ 违规已修复

# 5. Quality Gate 验证
Quality Gate: ✓ 所有门禁通过

# 结果: PASS (第 2 次尝试)
```

### 场景 3: 无法修复，失败

```bash
# 1-3. 同场景 2，但修复后仍有问题

# 4. Auditor 重新审计
Architecture Auditor: ✗ 仍有违规

# 5. 达到最大重试次数
Architecture Auditor: 达到最大重试次数 (3)

# 6. Quality Gate 决定
Quality Gate: ✗ 审查失败
Quality Gate: - 需要人工介入

# 结果: FAIL
```

---

## 集成到 SDLC

### 在代码开发阶段

```yaml
# SDLC 工作流集成
stage: code-development

steps:
  1. 生成代码 (Code Developer)
  2. 架构审计 (Architecture Auditor)
  3. 反馈修复 (循环直到通过或放弃)
  4. 质量门禁 (Quality Gate)
  5. 进入下一阶段
```

### 在 CI/CD 中

```yaml
# .github/workflows/sdlc-check.yml
name: SDLC Architecture Check

on: [pull_request]

jobs:
  architecture-audit:
    runs-on: ubuntu-latest
    steps:
      - uses: actions/checkout@v3

      - name: Run Agent Linter
        run: |
          cd SDLC-Framework/scripts
          ./agent-linter.sh > audit-result.json

      - name: Check Quality Gates
        run: |
          python scripts/check-quality-gates.py audit-result.json
```

---

## 监控和报告

### 审查指标

| 指标 | 说明 | 目标 |
|------|------|------|
| 审查通过率 | 一次性通过的比例 | > 80% |
| 平均审查轮数 | 每次变更的平均审查次数 | < 2 |
| 常见违规类型 | 最常出现的违规 | 减少 |
| 修复时间 | 从违规到修复的时间 | < 5 分钟 |

### 报告格式

```json
{
  "review_cycle_summary": {
    "timestamp": "2026-03-16T10:30:00Z",
    "total_reviews": 42,
    "pass_rate": 0.85,
    "avg_rounds": 1.8,
    "common_violations": [
      "layer_violation: 5",
      "complexity: 3"
    ]
  }
}
```

---

## 最佳实践

1. **快速反馈** - Linter 应在 5 秒内完成
2. **明确建议** - 修复建议应具体可操作
3. **限制重试** - 避免无限循环
4. **人工介入** - 复杂问题需要人工判断
5. **持续改进** - 根据违规数据优化规则

---

**最后更新**: 2026-03-16
**版本**: 1.0.0
