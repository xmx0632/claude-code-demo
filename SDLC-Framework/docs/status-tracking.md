# 文档状态追踪机制

本文档定义 SDLC Framework 的文档状态追踪机制，确保文档质量和上下文传递的完整性。

## 状态文件位置

```
.sdlc/docs-status.yaml
```

## 状态定义

| 状态 | 说明 | 可执行操作 |
|------|------|------------|
| `draft` | 草稿，编辑中 | 编辑 |
| `in_review` | 待审查 | 审查通过/驳回 |
| `approved` | 已批准 | 作为开发依据 |
| `deprecated` | 已废弃 | 不可使用 |
| `blocked` | 被阻塞 | 等待依赖解除 |

## 状态文件结构

```yaml
project:
  name: "{项目名称}"
  type: "new"  # new | legacy
  created_at: "YYYY-MM-DD"
  scenario: "new-project"  # new-project | legacy-maintenance | bug-fix

documents:
  requirements.md:
    status: "draft"  # draft | in_review | approved | deprecated | blocked
    version: "v1.0"
    owner: "Architect"
    created_at: "YYYY-MM-DD"
    updated_at: "YYYY-MM-DD"
    dependencies: []  # 依赖的文档
    reviewers: []  # 审查者
    blocking: []  # 阻塞的文档

  architecture.md:
    status: "blocked"
    version: "v0.9"
    owner: "Architect"
    dependencies: ["requirements.md"]
    reviewers:
      - role: "Developer"
        status: "pending"
    blocking:
      - "api-specs.md"
```

## 状态转换规则

```
draft ──▶ in_review ──▶ approved
  │                    │
  │                    └──▶ blocked ──▶ draft
  │
  └────────────────────▶ deprecated
```

## 查看文档状态

```bash
# 查看所有文档状态
cat .sdlc/docs-status.yaml

# 查看特定文档状态
sdlc-docs status requirements.md

# 查看阻塞关系
sdlc-docs blocked-by api-specs.md
```

## 状态可视化脚本

```bash
# 生成文档状态看板
sdlc-docs dashboard

# 输出示例:
┌─────────────┬─────────────┬─────────────┐
│   草稿      │   审查中    │   已批准    │
├─────────────┼─────────────┼─────────────┤
│ api-specs   │ architecture │ requirements │
│             │             │              │
└─────────────┴─────────────┴─────────────┘
```
