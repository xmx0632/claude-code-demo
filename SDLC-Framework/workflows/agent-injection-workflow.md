# 智能体注入工作流

> 定义角色约束智能体如何在 SDLC 阶段中自动触发

---

## 智能体概览

| 智能体 | 关注领域 | 主要触发阶段 |
|--------|----------|--------------|
| Security Agent | 安全、认证、数据保护 | 编码、测试、部署 |
| Performance Agent | 性能、缓存、查询优化 | 设计、编码、测试 |
| Infra Agent | 部署、监控、资源 | 编码、部署、运维 |

---

## 触发规则

### 按阶段触发

```
需求分析 → 无智能体触发
    ↓
架构设计 → Performance Agent (架构性能评估)
    ↓
详细设计 → Security Agent (安全设计评审)
         → Performance Agent (性能设计)
    ↓
编码实现 → Security Agent (代码安全检查)
         → Performance Agent (代码性能检查)
         → Infra Agent (容器化配置)
    ↓
测试验证 → Security Agent (安全测试)
         → Performance Agent (性能测试)
    ↓
部署上线 → Infra Agent (部署配置检查)
         → Security Agent (安全配置)
    ↓
运维监控 → Infra Agent (监控告警配置)
```

### 按关键词触发

| 关键词 | 触发智能体 |
|--------|------------|
| 登录、认证、密码、权限 | Security Agent |
| 查询、列表、缓存、性能 | Performance Agent |
| 部署、容器、监控、日志 | Infra Agent |
| API、接口、请求 | Security + Performance |
| 数据库、SQL | Performance + Security |

---

## 注入流程

### 1. 阶段开始时

```markdown
<!-- Claude Code 检测到阶段和相关智能体 -->

## 开始阶段: 编码实现

📋 任务: 实现用户登录 API

🤖 自动激活智能体:
- 🔒 Security Agent (检测到: 登录、认证)
- ⚡ Performance Agent (检测到: API)
- 🏗️ Infra Agent (默认激活)

智能体约束已加载，将在代码生成时应用。
```

### 2. 代码生成时

```markdown
<!-- Claude 生成代码时自动应用约束 -->

## 生成代码: 用户登录 API

🔒 Security Agent 检查:
- ✅ 输入验证: 使用 zod schema
- ✅ 密码处理: bcrypt 加密
- ✅ Token: JWT 配置
- ✅ 速率限制: 登录失败锁定

⚡ Performance Agent 检查:
- ✅ 数据库查询: 使用索引 (email)
- ✅ 缓存策略: 用户信息缓存 5 分钟

🏗️ Infra Agent 检查:
- ✅ 健康检查: /health 端点
- ✅ 监控指标: 登录成功/失败计数
```

### 3. 阶段结束时

```markdown
## 阶段完成: 编码实现

🤖 智能体汇总报告:

### 🔒 Security Agent
| 检查项 | 状态 | 备注 |
|--------|------|------|
| 输入验证 | ✅ | zod schema |
| 密码加密 | ✅ | bcrypt |
| 权限检查 | ✅ | RBAC |
| 日志脱敏 | ⚠️ | 建议移除邮箱日志 |

### ⚡ Performance Agent
| 检查项 | 状态 | 备注 |
|--------|------|------|
| 查询优化 | ✅ | 索引已添加 |
| N+1 问题 | ✅ | 无 |
| 缓存策略 | ✅ | Redis 5min TTL |

### 🏗️ Infra Agent
| 检查项 | 状态 | 备注 |
|--------|------|------|
| Dockerfile | ✅ | 多阶段构建 |
| 资源限制 | ⚠️ | 需配置 limits |
| 健康检查 | ✅ | 已配置 |

⚠️ 发现 2 个待处理项，建议修复后再进入下一阶段。
```

---

## 手动触发

### Claude Code 指令

```markdown
# 对指定文件执行安全审查
对 src/auth/ 执行 Security Agent 审查

# 对指定模块执行性能分析
对 API 模块执行 Performance Agent 分析

# 检查部署配置
执行 Infra Agent 部署检查

# 运行所有智能体
执行完整智能体审查
```

### 针对特定需求

```markdown
# 处理需求时指定智能体
处理需求 REQ-001，重点关注安全性

# 跳过某些智能体
处理需求 REQ-002，跳过性能检查
```

---

## 质量门禁集成

### 阻断条件

```yaml
quality_gates:
  security:
    blocker:
      - hardcoded_secrets
      - sql_injection
      - missing_auth_check
    action: block_merge

  performance:
    blocker:
      - n_plus_one_query
      - missing_pagination
    action: warn  # 警告但不阻止

  infra:
    blocker:
      - missing_health_check
      - root_user_container
    action: block_merge
```

### 与 CI/CD 集成

```yaml
# .github/workflows/agent-check.yml
name: Agent Checks

on: [pull_request]

jobs:
  security-check:
    runs-on: ubuntu-latest
    steps:
      - uses: actions/checkout@v4
      - name: Security Agent Scan
        # 运行安全扫描

  performance-check:
    runs-on: ubuntu-latest
    steps:
      - uses: actions/checkout@v4
      - name: Performance Agent Scan
        # 运行性能分析

  infra-check:
    runs-on: ubuntu-latest
    steps:
      - uses: actions/checkout@v4
      - name: Infra Agent Scan
        # 检查部署配置
```

---

## 智能体配置文件

每个智能体配置文件包含：

```markdown
# [Agent Name]

## 触发条件
- 条件 1
- 条件 2

## 检查清单
- [ ] 检查项 1
- [ ] 检查项 2

## 代码模式
### ✅ 正确模式
```typescript
// 示例代码
```

### ❌ 错误模式
```typescript
// 反例代码
```

## 输出模板
<!-- 报告格式 -->
```

---

## 扩展智能体

### 创建新智能体

1. 在 `guards/` 目录创建配置文件
2. 定义触发条件
3. 编写检查清单
4. 提供代码模式示例
5. 更新本工作流文档

### 智能体模板

```markdown
# [Name] Agent - [描述]

> [一句话说明]

## 触发条件

- 条件 1
- 条件 2

## 检查清单

### [类别 1]
- [ ] 检查项

## 代码模式

### ✅ 正确模式
### ❌ 错误模式

## 使用方式

## 输出模板
```
