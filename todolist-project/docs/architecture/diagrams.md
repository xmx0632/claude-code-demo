# 架构图集 (Architecture Diagrams)

> **项目**: TodoList 待办事项管理系统
> **版本**: 1.0
> **最后更新**: 2026-01-26

---

## 图表目录

| 图表类型 | 描述 | Mermaid 类型 |
|----------|------|--------------|
| 系统架构图 | 整体系统架构和层次关系 | graph |
| 组件图 | 核心组件及其关系 | graph |
| 部署架构图 | 部署拓扑和环境 | graph |
| 用户认证时序图 | 登录认证流程 | sequenceDiagram |
| 创建待办时序图 | 创建待办事项流程 | sequenceDiagram |
| 更新待办时序图 | 更新待办事项流程 | sequenceDiagram |
| 状态图 | 待办状态转换 | stateDiagram-v2 |
| ER 图 | 数据库实体关系 | erDiagram |
| 类图 | 核心类结构 | classDiagram |

---

## 1. 系统架构图

### 整体系统架构

```mermaid
graph TB
    subgraph "Client Layer 客户端层"
        Web[Web 浏览器]
        Mobile[移动应用]
        API_Client[API 客户端<br/>Postman/Apifox]
    end

    subgraph "Presentation Layer 表现层"
        AuthController[认证控制器<br/>AuthController]
        TodoController[待办控制器<br/>TodoController]
        CategoryController[分类控制器<br/>CategoryController]
        UserController[用户控制器<br/>UserController]
    end

    subgraph "Business Layer 业务层"
        AuthService[认证服务<br/>AuthService]
        TodoService[待办服务<br/>TodoService]
        CategoryService[分类服务<br/>CategoryService]
        UserService[用户服务<br/>UserService]
    end

    subgraph "Persistence Layer 持久层"
        AuthMapper[认证Mapper<br/>AuthMapper]
        TodoMapper[待办Mapper<br/>TodoMapper]
        CategoryMapper[分类Mapper<br/>CategoryMapper]
        UserMapper[用户Mapper<br/>UserMapper]
    end

    subgraph "Domain Layer 领域层"
        User[用户实体<br/>User]
        Todo[待办实体<br/>Todo]
        Category[分类实体<br/>Category]
        TodoCategory[关联实体<br/>TodoCategory]
    end

    subgraph "Infrastructure 基础设施"
        JWT[JWT 工具<br/>JwtUtil]
        Security[安全配置<br/>SecurityConfig]
        Flyway[数据库迁移<br/>Flyway]
        MySQL[(MySQL 数据库)]
    end

    Web --> AuthController
    Web --> TodoController
    Web --> CategoryController
    Web --> UserController

    Mobile --> AuthController
    Mobile --> TodoController
    API_Client --> AuthController
    API_Client --> TodoController

    AuthController --> AuthService
    TodoController --> TodoService
    CategoryController --> CategoryService
    UserController --> UserService

    AuthService --> AuthMapper
    TodoService --> TodoMapper
    CategoryService --> CategoryMapper
    UserService --> UserMapper

    AuthMapper --> User
    TodoMapper --> Todo
    TodoMapper --> Category
    CategoryMapper --> Category

    AuthController --> JWT
    AuthController --> Security
    UserMapper --> MySQL
    TodoMapper --> MySQL
    CategoryMapper --> MySQL

    User -.-> Flyway
    Todo -.-> Flyway
    Category -.-> Flyway
    Flyway -.-> MySQL
```

---

## 2. 组件图

### 核心组件关系

```mermaid
graph LR
    subgraph "认证模块 Auth Module"
        AC[AuthController]
        AS[AuthService]
        AM[UserMapper]
    end

    subgraph "待办模块 Todo Module"
        TC[TodoController]
        TS[TodoService]
        TM[TodoMapper]
    end

    subgraph "分类模块 Category Module"
        CC[CategoryController]
        CS[CategoryService]
        CM[CategoryMapper]
    end

    subgraph "通用模块 Common Module"
        EH[ExceptionHandler<br/>全局异常处理]
        JWT[JwtUtil<br/>JWT 工具]
        Security[SecurityConfig<br/>安全配置]
    end

    subgraph "领域模型 Domain"
        User[User<br/>用户实体]
        Todo[Todo<br/>待办实体]
        Category[Category<br/>分类实体]
    end

    AC --> AS
    AC --> JWT
    AC --> Security

    TC --> TS
    TC --> Security

    CC --> CS
    CC --> Security

    AS --> AM
    TS --> TM
    CS --> CM

    AS --> User
    TS --> Todo
    TS --> Category
    CS --> Category

    AM --> User
    TM --> Todo
    CM --> Category

    AC -.-> EH
    TC -.-> EH
    CC -.-> EH
```

---

## 3. 部署架构图

### 部署拓扑

```mermaid
graph TB
    subgraph "客户端层 Client Layer"
        Browser[Web 浏览器<br/>Chrome/Firefox/Safari]
        Mobile[移动应用<br/>iOS/Android]
        API_Tool[API 工具<br/>Postman/Apifox]
    end

    subgraph "网络层 Network Layer"
        Internet[互联网<br/>HTTPS]
    end

    subgraph "应用服务器层 Application Server"
        LB[负载均衡器<br/>Nginx - 可选]

        subgraph "Spring Boot 应用"
            App1[应用实例 1<br/>:8080]
            App2[应用实例 2<br/>:8081<br/>可扩展]
        end
    end

    subgraph "数据层 Data Layer"
        MySQL[(MySQL 数据库<br/>:3306)]
        Flyway[Flyway 迁移]
    end

    subgraph "监控层 Monitoring Layer"
        Actuator[Actuator<br/>健康检查]
        Logs[应用日志]
    end

    Browser --> Internet
    Mobile --> Internet
    API_Tool --> Internet

    Internet --> LB
    Internet -.-> App1

    LB --> App1
    LB -.-> App2

    App1 --> MySQL
    App2 --> MySQL

    Flyway --> MySQL

    App1 --> Actuator
    App1 --> Logs
    App2 --> Actuator
    App2 --> Logs
```

### 环境划分

```mermaid
graph LR
    subgraph "开发环境 Development"
        Dev[Spring Boot<br/>Embedded Tomcat<br/>:8080]
        Dev_DB[(H2 内存数据库)]
    end

    subgraph "测试环境 Testing"
        Test[Spring Boot<br/>Embedded Tomcat<br/>:8080]
        Test_DB[(MySQL Docker<br/>:3306)]
    end

    subgraph "预生产环境 Staging"
        Staging[Spring Boot<br/>External Tomcat<br/>:8080]
        Staging_DB[(MySQL 独立<br/>:3306)]
    end

    subgraph "生产环境 Production"
        Prod[Nginx LB<br/>→ Spring Boot 集群]
        Prod_DB[(MySQL 主从<br/>:3306)]
    end

    Dev --> Dev_DB
    Test --> Test_DB
    Staging --> Staging_DB
    Prod --> Prod_DB
```

---

## 4. 时序图

### 4.1 用户认证时序图

```mermaid
sequenceDiagram
    autonumber
    actor User as 用户
    participant Frontend as 前端
    participant API as REST API
    participant Controller as AuthController
    participant Service as AuthService
    participant Mapper as UserMapper
    participant DB as 数据库
    participant JWT as JWT 工具

    User->>Frontend: 输入用户名和密码
    Frontend->>API: POST /api/v1/auth/login<br/>{username, password}
    API->>Controller: 接收请求

    Controller->>Controller: 验证参数<br/>@Valid
    Controller->>Service: login(username, password)

    Service->>Mapper: findByUsername(username)
    Mapper->>DB: SELECT * FROM sys_user<br/>WHERE username = ?
    DB-->>Mapper: User 实体
    Mapper-->>Service: User 实体

    alt 用户不存在
        Service-->>Controller: 抛出异常<br/>用户名或密码错误
        Controller-->>API: 401 Unauthorized
        API-->>Frontend: {code: 401, message: "用户名或密码错误"}
        Frontend-->>User: 显示错误提示
    else 用户存在
        Service->>Service: 验证密码<br/>BCrypt.checkpw()
        alt 密码错误
            Service->>Service: 记录失败次数
            Service-->>Controller: 抛出异常<br/>用户名或密码错误
            Controller-->>API: 401 Unauthorized
            API-->>Frontend: {code: 401, message: "用户名或密码错误"}
            Frontend-->>User: 显示错误提示
        else 密码正确
            Service->>Service: 检查账户锁定状态
            alt 账户已锁定
                Service-->>Controller: 抛出异常<br/>账户已锁定
                Controller-->>API: 403 Forbidden
                API-->>Frontend: {code: 403, message: "账户已锁定"}
                Frontend-->>User: 显示锁定信息
            else 账户正常
                Service->>Service: 清除失败次数
                Service->>JWT: 生成 JWT Token<br/>generateToken(user)
                JWT-->>Service: JWT Token
                Service-->>Controller: 返回 Token 和用户信息
                Controller-->>API: 200 OK<br/>{token, user}
                API-->>Frontend: {code: 200, data: {token, user}}
                Frontend->>Frontend: 存储 Token<br/>localStorage
                Frontend-->>User: 跳转到待办列表页
            end
        end
    end
```

### 4.2 创建待办事项时序图

```mermaid
sequenceDiagram
    autonumber
    actor User as 用户
    participant Frontend as 前端
    participant API as REST API
    participant AuthFilter as 认证过滤器
    participant Controller as TodoController
    participant Service as TodoService
    participant Mapper as TodoMapper
    participant DB as 数据库

    User->>Frontend: 填写待办表单<br/>标题、描述、优先级等
    Frontend->>Frontend: 前端验证<br/>@Valid
    Frontend->>API: POST /api/v1/todos<br/>Header: Authorization: Bearer <token><br/>Body: {title, description, ...}

    API->>AuthFilter: JWT 认证过滤器
    AuthFilter->>AuthFilter: 验证 Token 有效性
    alt Token 无效
        AuthFilter-->>API: 401 Unauthorized
        API-->>Frontend: {code: 401, message: "未授权"}
        Frontend-->>User: 跳转到登录页
    else Token 有效
        AuthFilter->>AuthFilter: 提取用户信息<br/>(user_id, username)
        AuthFilter->>API: 放行请求<br/>设置 SecurityContext
        API->>Controller: 接收请求

        Controller->>Controller: 验证参数<br/>@Valid
        Controller->>Service: createTodo(todoDTO, userId)

        Service->>Service: 业务规则验证
        Service->>Service: DTO 转 Entity
        Service->>Service: 设置默认值<br/>status=PENDING, user_id
        Service->>Mapper: insert(todoEntity)

        Mapper->>DB: INSERT INTO todo<br/>(title, description, ...)<br/>VALUES (?, ?, ...)
        DB-->>Mapper: 返回生成的主键 ID
        Mapper-->>Service: Todo 实体（带 ID）

        Service->>Service: Entity 转 DTO
        Service-->>Controller: 返回 TodoDTO
        Controller-->>API: 201 Created<br/>{code: 201, data: todoDTO}
        API-->>Frontend: {code: 201, data: {...}}
        Frontend-->>User: 显示成功提示<br/>跳转到待办列表页
    end
```

### 4.3 更新待办事项时序图

```mermaid
sequenceDiagram
    autonumber
    actor User as 用户
    participant Frontend as 前端
    participant API as REST API
    participant AuthFilter as 认证过滤器
    participant Controller as TodoController
    participant Service as TodoService
    participant Mapper as TodoMapper
    participant DB as 数据库

    User->>Frontend: 点击编辑待办<br/>修改表单字段
    Frontend->>API: PUT /api/v1/todos/123<br/>Header: Authorization: Bearer <token><br/>Body: {title, description, ...}

    API->>AuthFilter: JWT 认证过滤器
    AuthFilter->>AuthFilter: 验证 Token
    AuthFilter->>API: 放行请求<br/>设置 SecurityContext
    API->>Controller: 接收请求

    Controller->>Controller: 验证参数<br/>@Valid
    Controller->>Service: updateTodo(id, todoDTO, userId)

    Service->>Mapper: selectById(id)
    Mapper->>DB: SELECT * FROM todo<br/>WHERE id = ?
    DB-->>Mapper: Todo 实体
    Mapper-->>Service: Todo 实体

    alt 待办不存在
        Service-->>Controller: 抛出异常<br/>待办不存在
        Controller-->>API: 404 Not Found
        API-->>Frontend: {code: 404, message: "待办不存在"}
        Frontend-->>User: 显示错误提示
    else 待办存在
        Service->>Service: 验证权限<br/>todo.userId == userId
        alt 无权限
            Service-->>Controller: 抛出异常<br/>无权访问
            Controller-->>API: 403 Forbidden
            API-->>Frontend: {code: 403, message: "无权访问"}
            Frontend-->>User: 显示错误提示
        else 有权限
            Service->>Service: 更新字段<br/>copyNonNullFields()
            Service->>Service: 设置更新时间<br/>updateTime = now()
            Service->>Mapper: updateById(todoEntity)

            Mapper->>DB: UPDATE todo<br/>SET title=?, description=?, ...<br/>WHERE id = ?
            DB-->>Mapper: 影响行数
            Mapper-->>Service: 更新成功

            Service->>Mapper: selectById(id)
            Mapper->>DB: SELECT * FROM todo<br/>WHERE id = ?
            DB-->>Mapper: 更新后的 Todo 实体
            Mapper-->>Service: Todo 实体

            Service->>Service: Entity 转 DTO
            Service-->>Controller: 返回 TodoDTO
            Controller-->>API: 200 OK<br/>{code: 200, data: todoDTO}
            API-->>Frontend: {code: 200, data: {...}}
            Frontend-->>User: 显示成功提示<br/>更新列表显示
        end
    end
```

---

## 5. 状态图

### 待办状态转换

```mermaid
stateDiagram-v2
    [*] --> 待处理: 创建待办

    待处理 --> 已完成: 标记为完成
    已完成 --> 待处理: 标记为未完成

    待处理 --> 已过期: 超过截止日期
    已过期 --> 待处理: 修改截止日期
    已过期 --> 已完成: 标记为完成

    待处理 --> 已删除: 删除待办
    已完成 --> 已删除: 删除待办
    已过期 --> 已删除: 删除待办

    已删除 --> [*]

    note right of 待处理
        默认初始状态
        status = PENDING
    end note

    note right of 已完成
        任务已完成
        completedAt 设置时间戳
    end note

    note right of 已过期
        截止日期 < 当前时间
        系统自动标记
    end note

    note right of 已删除
        软删除
        deleted = true
        deletedAt 设置时间戳
    end note
```

---

## 6. ER 图

### 数据库实体关系

```mermaid
erDiagram
    USER ||--o{ TODO : "创建 (1:N)"
    TODO ||--o{ TODO_CATEGORY : "关联 (M:N)"
    CATEGORY ||--o{ TODO_CATEGORY : "被关联 (M:N)"
    USER ||--o{ CATEGORY : "拥有 (1:N)"

    USER {
        bigint id PK "用户ID"
        string username UK "用户名"
        string password "密码 (BCrypt)"
        datetime created_at "创建时间"
        datetime updated_at "更新时间"
        boolean deleted "删除标记"
    }

    TODO {
        bigint id PK "待办ID"
        bigint user_id FK "用户ID"
        string title "标题"
        text description "描述"
        string status "状态: PENDING/COMPLETED"
        string priority "优先级: HIGH/MEDIUM/LOW"
        date due_date "截止日期"
        datetime completed_at "完成时间"
        datetime created_at "创建时间"
        datetime updated_at "更新时间"
        boolean deleted "删除标记"
        bigint version "版本号 (乐观锁)"
    }

    CATEGORY {
        bigint id PK "分类ID"
        bigint user_id FK "用户ID"
        string name "分类名称"
        string color "颜色标识"
        datetime created_at "创建时间"
        datetime updated_at "更新时间"
        boolean deleted "删除标记"
    }

    TODO_CATEGORY {
        bigint todo_id FK "待办ID"
        bigint category_id FK "分类ID"
        datetime created_at "创建时间"
    }
```

---

## 7. 类图

### 核心类结构

```mermaid
classDiagram
    class TodoController {
        +ITodoService todoService
        +create(TodoDTO dto)^
        +getById(Long id)^
        +list(TodoQuery query)^
        +update(Long id, TodoDTO dto)^
        +delete(Long id)^
        +complete(Long id)^
        +incomplete(Long id)^
    }

    class ITodoService {
        <<interface>>
        +insert(TodoDTO dto)^
        +updateById(TodoDTO dto)^
        +deleteById(Long id)^
        +getById(Long id)^
        +list(TodoQuery query)^
        +complete(Long id)^
        +incomplete(Long id)^
    }

    class TodoServiceImpl {
        -TodoMapper todoMapper
        -CategoryMapper categoryMapper
        +insert(TodoDTO dto)^
        +updateById(TodoDTO dto)^
        +deleteById(Long id)^
        +getById(Long id)^
        +list(TodoQuery query)^
        +complete(Long id)^
        +incomplete(Long id)^
        -validateOwnership(Long todoId, Long userId)^
        -entityToDTO(Todo entity)^
        -dtoToEntity(TodoDTO dto)^
    }

    class TodoMapper {
        <<interface>>
        +selectById(Long id)^
        +selectList(QueryWrapper query)^
        +insert(Todo entity)^
        +updateById(Todo entity)^
        +deleteById(Long id)^
    }

    class Todo {
        -Long id
        -Long userId
        -String title
        -String description
        -String status
        -String priority
        -LocalDate dueDate
        -LocalDateTime completedAt
        -LocalDateTime createdAt
        -LocalDateTime updatedAt
        -Boolean deleted
        -Long version
    }

    class TodoDTO {
        -Long id
        -String title
        -String description
        -String status
        -String priority
        -LocalDate dueDate
        -List~Long~ categoryIds
    }

    class TodoQuery {
        -String status
        -String priority
        -LocalDate dueDateFrom
        -LocalDate dueDateTo
        -String keyword
        -List~Long~ categoryIds
    }

    class Category {
        -Long id
        -Long userId
        -String name
        -String color
    }

    TodoController --> ITodoService
    ITodoService <|.. TodoServiceImpl
    TodoServiceImpl --> TodoMapper
    TodoMapper --> Todo

    TodoServiceImpl ..> TodoDTO : 使用
    TodoServiceImpl ..> TodoQuery : 使用
    TodoServiceImpl ..> Category : 使用

    TodoDTO --|> Todo
    TodoQuery --|> Todo
```

---

## 图表使用说明

### 如何查看图表

1. **GitHub/GitLab**: 直接在 Markdown 文件中渲染
2. **VS Code**: 安装 "Markdown Preview Mermaid Support" 插件
3. **在线工具**: 访问 https://mermaid.live/ 粘贴代码查看
4. **文档工具**: MkDocs, Hugo, Docusaurus 等支持 Mermaid

### 图表维护规范

- 保持图表简洁，避免过度复杂
- 使用中文标签，提高可读性
- 保持图表与代码同步更新
- 图表变更后更新文档修订日期
- 复杂图表添加注释说明

### 图表命名规范

- 系统架构图: `system-architecture`
- 组件图: `component-diagram`
- 部署架构图: `deployment-architecture`
- 时序图: `sequence-diagram-{功能}`
- 状态图: `state-diagram-{实体}`
- ER 图: `er-diagram`
- 类图: `class-diagram-{模块}`

---

## 相关文档

- 系统架构设计: `docs/architecture/architecture.md`
- 组件图文档: `docs/architecture/component-diagram.md`
- 部署架构文档: `docs/architecture/deployment-architecture.md`
- 数据库设计: （待创建）
- API 设计: （待创建）
