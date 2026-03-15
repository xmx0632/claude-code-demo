# 指导文档索引

本目录包含 SDLC Framework 的顶层指导文档。

---

## 文档层级

```
CONSTITUTION.md          # 第一层：项目宪法（必读）
    │
    ├── STEERING-DOCS.md     # 第二层：领域指导（按需）
    │       │
    │       ├── steering/    # 第三层：具体规范
    │       │   ├── api-design.md
    │       │   ├── auth.md
    │       │   └── ...
    │       │
    │       └── templates/   # 规范模板
    │           └── STEERING-TEMPLATE.md
    │
    └── feedback/            # 框架反馈记录
        └── FRAMEWORK-FEEDBACK.md
```

---

## 使用方式

### 1. 新项目启动时

```bash
# 1. 阅读 Constitution，了解基本约束
# 2. 根据项目类型，创建相关 Steering Docs
# 3. 在 CLAUDE.md 中引用这些文档
```

### 2. Claude Code 执行任务时

```markdown
AI 应该：
1. 读取 CONSTITUTION.md 了解项目约束
2. 根据任务类型读取相关 steering/*.md
3. 遵循规范生成代码/文档
4. 检查是否符合检查清单
```

### 3. 发现 Bug 时

```markdown
1. 在 guidance/feedback/ 记录框架反思
2. 分析是"规范到实现的缺口"还是"意图到规范的缺口"
3. 更新相关指导文档
```

---

## 快速链接

- [项目宪法](./CONSTITUTION.md) - 所有项目必须遵守
- [方向指导索引](./STEERING-DOCS.md) - 按领域查找规范
- [框架反馈记录](./feedback/FRAMEWORK-FEEDBACK.md) - 从 Bug 中学习
- [创建新规范](./templates/STEERING-TEMPLATE.md) - 使用模板

---

## 维护指南

### 何时更新 Constitution

- 项目技术栈重大变更
- 新增通用安全/质量要求
- 团队共识的架构决策

### 何时创建 Steering Doc

- 新技术领域引入（如 AI、区块链）
- 反复出现的同一类 Bug
- 团队需要统一的实现方式

### 何时记录框架反馈

- 每个 Bug 修复后
- 发现规范有缺口时
- 实现与规范不一致时
