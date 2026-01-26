---
name: mermaid-diagram
description: 生成各种类型的 Mermaid 图表，包括系统架构图、流程图、时序图、状态图、ER 图和类图。在架构设计、流程分析、文档编写时使用。
allowed-tools: ["Read", "Write", "Edit"]
disable-model-invocation: false
user-invocable: true
context: fork
agent: general-purpose
---

# Mermaid Diagram Generation Skill

生成 Mermaid 图表: **$ARGUMENTS**

## 技能概述

本技能帮助你快速生成各种类型的 Mermaid 图表，用于系统架构设计、流程分析、数据建模等场景。

## 支持的图表类型

### 1. 系统架构图 (System Architecture)

**使用场景**: 展示系统整体架构、组件关系

```bash
/mermaid-diagram --type=architecture --title="电商系统架构"
```

**生成示例**:
```mermaid
graph TB
    subgraph "前端层"
        Web[Web 应用]
        Mobile[移动应用]
    end

    subgraph "网关层"
        Gateway[API 网关]
    end

    subgraph "服务层"
        UserService[用户服务]
        OrderService[订单服务]
        PaymentService[支付服务]
    end

    subgraph "数据层"
        MySQL[(MySQL)]
        Redis[(Redis)]
        MQ[消息队列]
    end

    Web --> Gateway
    Mobile --> Gateway
    Gateway --> UserService
    Gateway --> OrderService
    Gateway --> PaymentService
    UserService --> MySQL
    UserService --> Redis
    OrderService --> MySQL
    OrderService --> MQ
    PaymentService --> MySQL
```

### 2. 流程图 (Flowchart)

**使用场景**: 业务流程、用户流程、审批流程

```bash
/mermaid-diagram --type=flowchart --title="订单处理流程"
```

**生成示例**:
```mermaid
flowchart TD
    Start([开始]) --> Receive[接收订单]
    Receive --> Validate{验证订单}
    Validate -->|有效| Process[处理订单]
    Validate -->|无效| Reject[拒绝订单]

    Process --> Inventory{检查库存}
    Inventory -->|充足| Payment[创建支付]
    Inventory -->|不足| Backorder[缺货登记]

    Payment --> PayResult{支付结果}
    PayResult -->|成功| Ship[安排发货]
    PayResult -->|失败| Cancel[取消订单]

    Ship --> End([结束])
    Reject --> End
    Backorder --> End
    Cancel --> End
```

### 3. 时序图 (Sequence Diagram)

**使用场景**: API 交互、组件通信、业务流程时序

```bash
/mermaid-diagram --type=sequence --title="用户登录时序"
```

**生成示例**:
```mermaid
sequenceDiagram
    actor User as 用户
    participant Browser as 浏览器
    participant API as API 网关
    participant Service as 认证服务
    participant DB as 数据库

    User->>Browser: 输入用户名密码
    Browser->>API: POST /api/auth/login
    API->>Service: 验证请求
    Service->>DB: 查询用户信息
    DB-->>Service: 返回用户数据
    Service-->>API: 返回验证结果
    API-->>Browser: 返回 Token
    Browser-->>User: 跳转到首页
```

### 4. 状态图 (State Diagram)

**使用场景**: 订单状态、审批流程、对象生命周期

```bash
/mermaid-diagram --type=state --title="订单状态流转"
```

**生成示例**:
```mermaid
stateDiagram-v2
    [*] --> 待支付: 创建订单
    待支付 --> 已支付: 支付成功
    待支付 --> 已取消: 超时未支付

    已支付 --> 待发货: 商家接单
    待支付 --> 已退款: 申请退款

    待发货 --> 已发货: 物流揽收
    待发货 --> 已退款: 申请退款

    已发货 --> 已完成: 用户确认收货
    已发货 --> 退款中: 申请退款

    已完成 --> [*]
    已取消 --> [*]
    已退款 --> [*]
    退款中 --> 已退款
```

### 5. ER 图 (Entity Relationship)

**使用场景**: 数据模型设计、数据库关系

```bash
/mermaid-diagram --type=er --title="用户订单数据模型"
```

**生成示例**:
```mermaid
erDiagram
    USER ||--o{ ORDER : places
    USER {
        int id PK
        string username UK
        string email UK
        string password
        datetime created_at
    }

    ORDER ||--|{ ORDER_ITEM : contains
    ORDER {
        int id PK
        int user_id FK
        string order_no UK
        decimal total_amount
        string status
        datetime created_at
    }

    ORDER_ITEM {
        int id PK
        int order_id FK
        int product_id FK
        int quantity
        decimal price
    }

    PRODUCT ||--o{ ORDER_ITEM : ""
    PRODUCT {
        int id PK
        string name
        decimal price
        int stock
    }
```

### 6. 类图 (Class Diagram)

**使用场景**: 代码结构设计、类关系展示

```bash
/mermaid-diagram --type=class --title="用户管理类结构"
```

**生成示例**:
```mermaid
classDiagram
    class UserController {
        +IUserService userService
        +create(UserDTO dto) Result
        +update(UserDTO dto) Result
        +delete(Long id) Result
        +getById(Long id) UserVO
        +list(UserQuery query) List~UserVO~
    }

    class IUserService {
        <<interface>>
        +insert(UserDTO dto) Long
        +update(UserDTO dto) Boolean
        +deleteById(Long id) Boolean
        +getById(Long id) User
        +list(UserQuery query) List~User~
    }

    class UserServiceImpl {
        -UserMapper userMapper
        +insert(UserDTO dto) Long
        +update(UserDTO dto) Boolean
        +deleteById(Long id) Boolean
        +getById(Long id) User
        +list(UserQuery query) List~User~
    }

    class UserMapper {
        <<interface>>
        +selectById(Long id) User
        +insert(User user) int
        +updateById(User user) int
        +deleteById(Long id) int
    }

    class User {
        -Long id
        -String username
        -String email
        -String password
        +getUsername() String
    }

    class UserDTO {
        +String username
        +String email
        +String password
    }

    class UserVO {
        +Long id
        +String username
        +String email
    }

    UserController --> IUserService
    IUserService <|.. UserServiceImpl
    UserServiceImpl --> UserMapper
    UserMapper --> User
    User ..> UserDTO : 转换
    User ..> UserVO : 转换
```

### 7. 甘特图 (Gantt Chart)

**使用场景**: 项目计划、任务排期

```bash
/mermaid-diagram --type=gantt --title="开发计划"
```

**生成示例**:
```mermaid
gantt
    title 项目开发计划
    dateFormat  YYYY-MM-DD
    section 需求阶段
    需求分析      :a1, 2024-01-01, 7d
    原型设计      :a2, after a1, 5d

    section 开发阶段
    后端开发      :b1, after a2, 14d
    前端开发      :b2, after a2, 14d

    section 测试阶段
    集成测试      :c1, after b1, 7d
    用户验收      :c2, after c1, 5d
```

### 8. 饼图 (Pie Chart)

**使用场景**: 数据分布、占比展示

```bash
/mermaid-diagram --type=pie --title="用户分布"
```

**生成示例**:
```mermaid
pie title 用户类型分布
    "普通用户" : 70
    "VIP 用户" : 20
    "企业用户" : 10
```

## 图表配置

### 样式配置

Mermaid 支持通过配置类自定义样式：

```mermaid
%%{init: {'theme':'base', 'themeVariables': { 'primaryColor':'#ff0000'}}}%%
graph TD
    A[A] --> B[B]
```

### 主题选择

- **default**: 默认主题
- **forest**: 森林主题
- **dark**: 暗色主题
- **neutral**: 中性主题

### 方向设置

- **TB**: 从上到下 (top to bottom)
- **BT**: 从下到上 (bottom to top)
- **LR**: 从左到右 (left to right)
- **RL**: 从右到左 (right to left)

## 图表元素

### 节点样式

```mermaid
graph LR
    A[矩形节点]
    B(圆角矩形)
    C[(圆柱体/数据库)]
    D((圆形))
    E{菱形/判断}
    F[/平行四边形/输入输出]
```

### 连接线样式

```mermaid
graph LR
    A -->|实线箭头| B
    C -.->|虚线箭头| D
    E ==>|粗线箭头| F
```

### 子图

```mermaid
graph TB
    subgraph 子图1 [前端层]
        A[组件 A]
        B[组件 B]
    end

    subgraph 子图2 [后端层]
        C[服务 C]
        D[服务 D]
    end

    A --> C
    B --> D
```

## 图表导出

### PNG 图片

使用 Mermaid CLI 工具导出：

```bash
# 安装 Mermaid CLI
npm install -g @mermaid-js/mermaid-cli

# 导出为 PNG
mmdc -i input.mmd -o output.png
```

### SVG 图片

```bash
# 导出为 SVG
mmdc -i input.mmd -o output.svg
```

## 最佳实践

1. **简洁明了**: 避免过度复杂，突出重点
2. **层次清晰**: 使用子图组织复杂结构
3. **方向一致**: 统一使用 TB 或 LR
4. **颜色标注**: 使用样式区分不同类型
5. **标签清晰**: 节点和边的标签要简洁明确
6. **适度详细**: 图表不要包含过多细节

## 常见问题

### Q: 图表不显示？

A: 检查：
1. Mermaid 语法是否正确
2. 是否有未闭合的括号或引号
3. 特殊字符是否需要转义

### Q: 图表太大？

A: 解决方案：
1. 拆分成多个小图
2. 使用子图组织
3. 简化节点和连接

### Q: 图表重叠？

A: 解决方案：
1. 调整图表方向
2. 使用子图分隔
3. 增加图表尺寸

## 相关文档

- [Mermaid 官方文档](https://mermaid.js.org/)
- 架构设计: `docs/architecture/architecture.md`
- 产品设计: `docs/design/wireframes/`
