# Architecture Design Skill

> 阶段 3: 系统架构设计

---

## 触发命令

```bash
/architecture-design
```

---

## 阶段目标

设计系统整体架构，确定技术选型和关键决策。

---

## 输入

- 需求规格说明书 (阶段 1 产出)
- 产品原型 (阶段 2 产出)
- 项目宪法 (guidance/CONSTITUTION.md)

---

## 输出

| 产出物 | 文件 | 模板 |
|--------|------|------|
| 架构设计文档 | docs/architecture/architecture.md | architecture-template.md |
| 架构决策记录 | docs/architecture/adr/ | adr-template.md |
| 技术选型报告 | docs/architecture/tech-stack.md | tech-stack-template.md |
| 系统部署图 | docs/architecture/deployment.md | deployment-template.md |

---

## 执行步骤

### 1. 分析需求

- 阅读需求规格说明书
- 识别功能性需求和非功能性需求
- 确定约束条件

### 2. 设计架构

- 选择架构风格 (微服务/单体/事件驱动)
- 划分系统模块
- 定义模块间接口

### 3. 技术选型

- 选择技术栈
- 评估第三方组件
- 记录选型理由

### 4. 架构评审

- 检查架构合理性
- 验证满足需求
- 记录架构决策

---

## 触发的 Guards

| Guard | 触发条件 |
|-------|----------|
| Performance Agent | 涉及性能需求 |
| Infra Agent | 涉及部署架构 |

---

## 质量门禁

- [ ] 架构文档已评审
- [ ] 技术选型有记录
- [ ] ADR 已创建
- [ ] 性能需求可满足
- [ ] 安全需求已考虑

---

## 相关文件

- 模板目录: `03-architecture-design/templates/`
- 角色定义: `roles/architect.md`
- 工作流: `workflows/full-sdlc-workflow.md`
