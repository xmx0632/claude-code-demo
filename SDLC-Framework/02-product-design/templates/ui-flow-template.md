# 用户流程图 (User Flow)

> **项目**: [项目名称]
> **版本**: 1.0
> **最后更新**: [日期]

---

## 流程概览

本文档描述产品的主要用户流程，包括用户从进入系统到完成目标的完整路径。

---

## 用户角色

| 角色 | 描述 | 主要目标 |
|------|------|----------|
| 管理员 | 系统管理员 | 管理用户、配置系统 |
| 操作员 | 业务操作人员 | 处理日常业务 |
| 普通用户 | 终端用户 | 使用系统功能 |

---

## 用户流程图

### 主要流程 1: [流程名称]

```mermaid
flowchart TD
    Start([开始]) --> Entry[进入系统]
    Entry --> CheckAuth{已登录?}

    CheckAuth -->|否| Login[登录页面]
    Login --> LoginValidate{登录验证}
    LoginValidate -->|成功| Dashboard[仪表板]
    LoginValidate -->|失败| Login

    CheckAuth -->|是| Dashboard

    Dashboard --> Action{用户操作}
    Action -->|查看数据| DataView[数据查看页面]
    Action -->|创建记录| CreateRecord[创建记录页面]
    Action -->|导出数据| Export[导出数据]

    DataView --> Filter{需要筛选?}
    Filter -->|是| FilterApply[应用筛选条件]
    Filter -->|否| BackDashboard
    FilterApply --> DataView

    CreateRecord --> FillForm[填写表单]
    FillForm --> Validate{验证通过?}
    Validate -->|是| Submit{提交成功?}
    Validate -->|否| FillForm

    Submit -->|是| Success[成功提示]
    Submit -->|否| Error[错误提示]

    Success --> Dashboard
    Error --> FillForm

    Export --> Download[下载文件]
    Download --> Dashboard

    BackDashboard --> Dashboard
    Dashboard --> End([结束])
```

**流程说明**:
1. 用户进入系统，检查登录状态
2. 未登录则跳转到登录页面
3. 登录成功后进入仪表板
4. 根据用户操作导航到不同功能页面
5. 完成操作后返回仪表板或退出

---

### 主要流程 2: [流程名称]

```mermaid
flowchart TD
    Start([开始]) --> PageA[页面 A]
    PageA --> Decision1{决策点 1}

    Decision1 -->|选项 1| PageB[页面 B]
    Decision1 -->|选项 2| PageC[页面 C]

    PageB --> Process1[处理流程 1]
    PageC --> Process2[处理流程 2]

    Process1 --> Decision2{决策点 2}
    Process2 --> Decision2

    Decision2 -->|成功| Success[成功页面]
    Decision2 -->|失败| Retry[重试]

    Retry --> PageA
    Success --> End([结束])
```

---

### 子流程: [子流程名称]

```mermaid
flowchart LR
    subgraph "子流程: [子流程名称]"
        A[步骤 1] --> B[步骤 2]
        B --> C{判断}
        C -->|是| D[步骤 3a]
        C -->|否| E[步骤 3b]
        D --> F[步骤 4]
        E --> F
    end
```

---

## 页面流程详情

### 页面: [页面名称]

**入口**:
- 来源: [从哪个页面进入]
- 条件: [进入条件]

**出口**:
- 动作 1 → [目标页面]
- 动作 2 → [目标页面]
- 返回 → [返回页面]

**关键交互**:
1. [交互 1]
2. [交互 2]

**页面元素**:
- [元素 1]: [说明]
- [元素 2]: [说明]

---

## 异常流程

### 异常 1: [异常名称]

**触发条件**: [什么情况触发]

**处理流程**:
```mermaid
flowchart TD
    Start([开始]) --> Error[异常发生]
    Error --> Handle{处理方式}
    Handle -->|自动| AutoHandle[自动处理]
    Handle -->|手动| ManualHandle[手动处理]

    AutoHandle --> Validate{验证成功?}
    Validate -->|是| Success([成功])
    Validate -->|否| Fallback[降级处理]

    ManualHandle --> UserAction{用户操作}
    UserAction -->|重试| Retry[重新尝试]
    UserAction -->|取消| Cancel([取消])
    UserAction -->|跳过| Skip([跳过])

    Retry --> Start
    Fallback --> Success
    Skip --> End([结束])
    Cancel --> End
    Success --> End
```

---

## 用户流程最佳实践

### 设计原则

1. **清晰性**: 流程应该清晰、直观
2. **简洁性**: 减少不必要的步骤
3. **一致性**: 相似操作使用相似流程
4. **反馈性**: 每个操作都有明确反馈
5. **容错性**: 允许用户撤销和修正

### 流程优化建议

- **减少点击次数**: 合并相关步骤
- **提供快捷方式**: 为高频用户提供快速路径
- **智能预填充**: 根据上下文预填信息
- **错误预防**: 在错误发生前进行提示
- **渐进式引导**: 新用户显示引导提示

---

## 流程验证

### 验证清单

- [ ] 流程覆盖所有用户场景
- [ ] 每个决策点都有明确的分支
- [ ] 异常流程都有处理方案
- [ ] 流程符合用户习惯
- [ ] 流程支持用户目标达成

### 测试用例

| 用例 ID | 场景描述 | 步骤 | 预期结果 |
|---------|----------|------|----------|
| UF-001 | 正常登录 | 1. 打开登录页<br>2. 输入用户名密码<br>3. 点击登录 | 跳转到仪表板 |
| UF-002 | 登录失败 | 1. 打开登录页<br>2. 输入错误密码<br>3. 点击登录 | 显示错误提示 |

---

## 相关文档

- 线框图: `docs/design/wireframes/`
- 原型规格: `docs/design/prototype-spec.md`
- 需求规格: `docs/requirements/requirements-spec.md`
