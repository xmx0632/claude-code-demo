# Detailed Design Skill

> 阶段 4: 系统详细设计

---

## 触发命令

```bash
/detailed-design
```

---

## 阶段目标

设计 API 接口、数据模型、类结构等详细设计。

---

## 输入

- 架构设计文档 (阶段 3 产出)
- 需求规格说明书 (阶段 1 产出)

---

## 输出

| 产出物 | 文件 | 模板 |
|--------|------|------|
| API 设计文档 | docs/design/api-spec.md | api-spec-template.md |
| 数据模型设计 | docs/design/data-model.md | data-model-template.md |
| 类图设计 | docs/design/class-diagram.md | class-diagram-template.md |
| 序列图 | docs/design/sequence-diagram.md | sequence-template.md |

---

## 执行步骤

### 1. API 设计

- 定义 API 端点
- 设计请求/响应格式
- 定义错误码

### 2. 数据模型设计

- 设计数据库表结构
- 定义实体关系
- 设计索引策略

### 3. 详细设计

- 设计类结构
- 定义接口契约
- 绘制序列图

### 4. 设计评审

- 检查设计合理性
- 验证满足需求
- 评审通过后签字

---

## 触发的 Guards

| Guard | 触发条件 |
|-------|----------|
| Security Agent | 涉及 API 设计 |
| Performance Agent | 涉及数据模型 |

---

## 质量门禁

- [ ] API 文档已完成
- [ ] 数据模型已评审
- [ ] 接口契约已定义
- [ ] 安全考虑已记录
- [ ] 性能考虑已记录

---

## 相关文件

- 模板目录: `04-detailed-design/templates/`
- 角色定义: `roles/architect.md`, `roles/backend-developer.md`
- 工作流: `workflows/full-sdlc-workflow.md`
