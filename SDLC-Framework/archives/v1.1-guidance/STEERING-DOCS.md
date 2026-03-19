# 方向指导文档 (Steering Docs)

> 针对特定技术领域或关注点的详细指导，与 Constitution 配合使用。

**最后更新**: 2026-03-15

---

## 使用说明

Steering Docs 是针对特定领域的详细指南，补充 Constitution 中声明的高层原则。

### 文档结构

每个 Steering Doc 应该包含：

1. **适用范围** - 这个指南适用于哪些场景
2. **具体规则** - 详细的约束和规范
3. **示例代码** - 展示正确实现方式
4. **检查清单** - 验证是否符合指南

### 创建新的 Steering Doc

```bash
# 复制模板
cp guidance/templates/STEERING-TEMPLATE.md guidance/steering-XXX.md

# 编辑内容
# 在 guidance/index.md 中添加索引
```

---

## 当前 Steering Docs

### 技术类

| 文档 | 适用范围 | 最后更新 |
|------|----------|----------|
| [API 设计规范](./steering/api-design.md) | 所有 API 开发 | 待创建 |
| [数据库设计规范](./steering/database.md) | 数据模型设计 | 待创建 |
| [前端开发规范](./steering/frontend.md) | UI/UX 开发 | 待创建 |
| [测试策略](./steering/testing.md) | 所有测试相关 | 待创建 |

### 安全类

| 文档 | 适用范围 | 最后更新 |
|------|----------|----------|
| [认证授权规范](./steering/auth.md) | 用户认证和授权 | 待创建 |
| [数据安全规范](./steering/data-security.md) | 敏感数据处理 | 待创建 |

### 运维类

| 文档 | 适用范围 | 最后更新 |
|------|----------|----------|
| [部署流程](./steering/deployment.md) | 生产环境部署 | 待创建 |
| [监控告警](./steering/monitoring.md) | 系统监控 | 待创建 |

---

## 智能体使用

### Claude Code 中的使用

```markdown
<!-- AI 在执行任务时应该 -->

1. 读取 Constitution 了解基本约束
2. 根据任务类型读取相关 Steering Docs
3. 遵循其中的规范生成代码/文档
```

### 角色约束智能体

可以基于 Steering Docs 创建角色智能体：

| 智能体类型 | 对应 Steering Docs |
|------------|-------------------|
| Security Agent | auth.md, data-security.md |
| Performance Agent | api-design.md, database.md |
| Infrastructure Agent | deployment.md, monitoring.md |

---

## 更新记录

| 日期 | 更新内容 | 更新人 |
|------|----------|--------|
| 2026-03-15 | 初始创建 | Claude |
