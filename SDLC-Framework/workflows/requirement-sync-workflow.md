# 需求同步工作流

> 从本地 Markdown 需求文件启动 SDLC 流程

---

## 目录结构

```
SDLC-Framework/
└── requirements/
    ├── TEMPLATE.md          # 需求模板
    ├── README.md            # 使用说明
    ├── backlog/             # 待处理需求
    │   └── REQ-001-XXX.md
    ├── active/              # 进行中需求（软链接或移动）
    └── completed/           # 已完成需求
        └── REQ-000-XXX.md
```

---

## 使用方式

### 1. 创建新需求

```bash
# 复制模板
cp requirements/TEMPLATE.md requirements/backlog/REQ-002-功能名称.md

# 编辑需求文件
# 填写需求描述、验收标准等
```

### 2. 启动 SDLC 流程

```markdown
<!-- 在 Claude Code 中执行 -->

我来处理需求 REQ-001。

首先读取需求文件：
```

**Claude 应执行**:
1. 读取 `requirements/backlog/REQ-001-XXX.md`
2. 提取需求信息
3. 初始化 SDLC 进度追踪表
4. 将需求移动到 `active/` 目录

### 3. 阶段执行与状态同步

每个 SDLC 阶段完成时：

```markdown
<!-- 更新需求文件中的进度表 -->

| 阶段 | 状态 | 开始日期 | 完成日期 | 产出物 |
|------|------|----------|----------|--------|
| 需求分析 | ✅ 完成 | 2026-03-15 | 2026-03-15 | REQ-001.md |
| 架构设计 | ✅ 完成 | 2026-03-15 | 2026-03-15 | architecture.md |
```

### 4. 完成需求

所有阶段完成后：

1. 更新需求状态为 `done`
2. 移动文件到 `completed/` 目录
3. 记录最终产出物

---

## 命令格式

### 读取需求

```bash
# 查看所有待处理需求
ls requirements/backlog/

# 查看进行中需求
ls requirements/active/
```

### Claude Code 指令

```
# 处理特定需求
处理需求 REQ-001

# 查看需求状态
需求 REQ-001 当前状态

# 完成某个阶段
完成需求 REQ-001 的架构设计阶段
```

---

## 状态流转

```
draft → backlog → active → completed
  ↓        ↓        ↓
 取消    暂停    阻塞
```

| 状态 | 说明 | 目录 |
|------|------|------|
| draft | 草稿，未完善 | backlog/ |
| backlog | 待处理 | backlog/ |
| active | 进行中 | active/ |
| completed | 已完成 | completed/ |
| blocked | 阻塞中 | active/ (标记) |
| cancelled | 已取消 | backlog/ (标记) |

---

## 与 SDLC 阶段映射

| SDLC 阶段 | 需求状态更新 | 产出物 |
|-----------|--------------|--------|
| 需求分析 | 分析中 → 完成 | 更新需求文件 |
| 架构设计 | 设计中 → 完成 | architecture.md |
| 详细设计 | 设计中 → 完成 | design/*.md |
| 编码实现 | 编码中 → 完成 | src/* |
| 测试验证 | 测试中 → 完成 | tests/*, 报告 |
| 部署上线 | 部署中 → 完成 | 部署记录 |

---

## 自动化检查点

Claude 在执行 SDLC 流程时应：

1. **开始阶段前**
   - 检查需求文件是否存在
   - 验证上一阶段是否完成
   - 确认依赖需求状态

2. **阶段执行中**
   - 实时更新进度表
   - 记录关键决策

3. **阶段完成后**
   - 更新状态为 ✅ 完成
   - 填写完成日期和产出物
   - 检查是否可以进入下一阶段
