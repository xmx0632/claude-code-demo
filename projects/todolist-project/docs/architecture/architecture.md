# 系统架构设计文档

> **项目**: TodoList 待办事项管理系统
> **版本**: 1.0
> **创建日期**: 2026-01-26
> **作者**: 系统架构师
> **状态**: 草稿 | 评审中 | 已批准

---

## 文档修订历史

| 版本 | 日期 | 作者 | 修订说明 | 审批人 |
|------|------|------|----------|--------|
| 1.0  | 2026-01-26 | 系统架构师 | 初始版本 | 待审批 |

---

## 1. 概述

### 1.1 系统目标

TodoList 待办事项管理系统是一个用于演示 SDLC（软件开发生命周期）框架的示例应用程序。系统采用经典的分层架构模式，基于 Spring Boot 3.2.0 构建，提供 RESTful API 服务，实现用户的待办事项管理功能。

**主要目标**:
- 教育目标: 演示完整的 SDLC 流程和最佳实践
- 技术目标: 构建清晰、可维护、可扩展的架构
- 质量目标: 高代码质量、完整的文档、充分的测试

### 1.2 架构原则

本系统架构遵循以下核心原则:

1. **简洁优先**: 架构简单明了，避免过度设计
2. **关注点分离**: 各层职责清晰，降低耦合
3. **可测试性**: 支持单元测试、集成测试和端到端测试
4. **可扩展性**: 预留扩展点，支持功能迭代
5. **标准化**: 遵循行业标准和最佳实践
6. **文档化**: 架构决策清晰记录，便于理解和维护

### 1.3 架构范围

**包含**:
- RESTful API 后端服务
- 用户认证和授权（JWT）
- 待办事项 CRUD 操作
- 分类/标签管理
- 数据持久化（MySQL + Flyway）
- API 文档（Swagger/OpenAPI）

**不包含**:
- 前端实现（仅作为 API 消费者）
- 实时通知功能
- 消息队列
- 缓存层（可后续添加）
- 文件存储服务

---

## 2. 架构视图

### 2.1 逻辑架构

#### 分层架构模式

系统采用经典的**分层架构（Layered Architecture）**模式，分为以下层次:

```
┌─────────────────────────────────────┐
│     Presentation Layer (表现层)      │  ← REST API Controllers
├─────────────────────────────────────┤
│      Business Layer (业务层)         │  ← Service Interfaces & Impl
├─────────────────────────────────────┤
│    Persistence Layer (持久层)        │  ← MyBatis-Plus Mappers
├─────────────────────────────────────┤
│      Domain Layer (领域层)           │  ← Entities, DTOs, Enums
└─────────────────────────────────────┘
```

**层次说明**:

1. **Presentation Layer (表现层)**
   - 职责: 处理 HTTP 请求和响应，参数验证，路由分发
   - 组件: REST Controllers, Request/Response DTOs, 异常处理器
   - 特点: 薄层，不包含业务逻辑

2. **Business Layer (业务层)**
   - 职责: 实现业务逻辑，事务管理，业务规则验证
   - 组件: Service 接口和实现类，业务规则引擎
   - 特点: 核心业务逻辑，可复用

3. **Persistence Layer (持久层)**
   - 职责: 数据访问，CRUD 操作，数据映射
   - 组件: MyBatis-Plus Mapper 接口，SQL 映射
   - 特点: 封装数据访问细节

4. **Domain Layer (领域层)**
   - 职责: 定义领域模型，业务概念
   - 组件: 实体类（Entity），数据传输对象（DTO），枚举
   - 特点: 跨层使用，贫血模型模式

#### 层间交互规则

| 调用方向 | 允许 | 说明 |
|----------|------|------|
| Presentation → Business | ✓ | Controller 调用 Service |
| Presentation → Domain | ✓ | 使用 DTO 传输数据 |
| Business → Persistence | ✓ | Service 调用 Mapper |
| Business → Domain | ✓ | 使用 Entity 和 DTO |
| Persistence → Domain | ✓ | Mapper 返回 Entity |
| Persistence → Business | ✗ | 禁止反向依赖 |
| Domain → 上层 | ✗ | 领域层不依赖上层 |

#### 模块划分

系统按业务领域划分为以下模块:

```
todolist-app
├── auth-module          # 认证授权模块
│   ├── controller       # 登录、注册、Token管理
│   ├── service          # 认证业务逻辑
│   └── mapper           # 用户数据访问
│
├── todo-module          # 待办事项模块
│   ├── controller       # 待办 CRUD 接口
│   ├── service          # 待办业务逻辑
│   └── mapper           # 待办数据访问
│
├── category-module      # 分类标签模块
│   ├── controller       # 分类 CRUD 接口
│   ├── service          # 分类业务逻辑
│   └── mapper           # 分类数据访问
│
└── common-module        # 通用模块
    ├── config           # 配置类
    ├── exception        # 全局异常处理
    ├── security         # 安全配置
    └── util             # 工具类
```

### 2.2 部署架构

#### 部署拓扑

系统采用单机部署架构，适合演示和小规模应用:

```
┌────────────────────────────────────────┐
│            Client Layer                │
│    (Browser / Mobile / Postman)        │
└──────────────┬─────────────────────────┘
               │ HTTPS
┌──────────────▼─────────────────────────┐
│         Web Server (Nginx)             │
│         (Optional - Reverse Proxy)     │
└──────────────┬─────────────────────────┘
               │ HTTP :8080
┌──────────────▼─────────────────────────┐
│      Spring Boot Application           │
│         (Embedded Tomcat)              │
│  ┌──────────────────────────────────┐  │
│  │  REST API Controllers            │  │
│  │  Business Services               │  │
│  │  Data Access Layer               │  │
│  └──────────────────────────────────┘  │
└──────────────┬─────────────────────────┘
               │ JDBC :3306
┌──────────────▼─────────────────────────┐
│      MySQL Database                    │
│    (Data Storage & Relations)          │
└────────────────────────────────────────┘
```

#### 环境划分

| 环境 | 用途 | 数据库 | 配置 |
|------|------|--------|------|
| Development | 开发环境 | H2 (内存) | application-dev.yml |
| Testing | 测试环境 | MySQL Docker | application-test.yml |
| Staging | 预生产环境 | MySQL 独立 | application-staging.yml |
| Production | 生产环境 | MySQL 集群 | application-prod.yml |

### 2.3 数据架构

#### 数据模型设计

**核心实体**:
1. **User (用户)**: 系统用户信息
2. **Todo (待办事项)**: 待办任务信息
3. **Category (分类)**: 分类/标签信息
4. **TodoCategory (待办-分类关联)**: 多对多关系

**数据关系**:
```
User (1) ────< (N) Todo
Todo (M) ────< (N) Category (通过 TodoCategory)
```

#### 数据流

**读流程**:
```
Client Request → Controller → Service → Mapper → Database
                     ↓
                DTO Transform
                     ↓
                Response JSON
```

**写流程**:
```
Client Request → Controller (Validation)
                     ↓
                DTO Transform
                     ↓
                Service (Business Logic)
                     ↓
                Mapper (Transaction)
                     ↓
                Database (Commit)
                     ↓
                Response (Updated Entity)
```

#### 数据一致性策略

- **事务管理**: 使用 Spring `@Transactional` 注解
- **隔离级别**: READ_COMMITTED（默认）
- **传播行为**: REQUIRED（默认）
- **乐观锁**: 使用 `version` 字段防止并发冲突
- **外键约束**: 数据库层保证引用完整性

### 2.4 安全架构

#### 认证机制

**JWT (JSON Web Token) 认证**:

```
┌─────────┐                  ┌─────────┐
│ Client  │                  │ Server  │
└────┬────┘                  └────┬────┘
     │                            │
     │  POST /api/auth/login      │
     │  {username, password}      │
     │──────────────────────────>│
     │                            │ Verify Credentials
     │                            │ Generate JWT
     │  {token, user}             │
     │<──────────────────────────│
     │                            │
     │  GET /api/todos            │
     │  Header: Authorization:   │
     │         Bearer <token>     │
     │──────────────────────────>│ Validate Token
     │                            │ Extract User Info
     │  {todos}                   │
     │<──────────────────────────│
```

**Token 结构**:
```json
{
  "header": {
    "alg": "HS256",
    "typ": "JWT"
  },
  "payload": {
    "user_id": 123,
    "username": "testuser",
    "exp": 1706659200,
    "iat": 1706572800
  }
}
```

#### 授权机制

**基于用户的数据隔离**:
- 每个用户只能访问自己的数据
- Service 层自动过滤 `user_id`
- Controller 层验证资源所有权

**权限检查点**:
1. **认证过滤器**: 验证 Token 有效性
2. **权限拦截器**: 检查用户权限
3. **Service 层**: 业务级权限验证
4. **数据层**: SQL WHERE 条件过滤

#### 安全防护措施

| 威胁类型 | 防护措施 | 实现方式 |
|----------|----------|----------|
| SQL 注入 | 参数化查询 | MyBatis-Plus 预编译 |
| XSS 攻击 | 输入验证 + 输出转义 | 前端框架 + 后端验证 |
| CSRF 攻击 | Token 验证 | SameSite Cookie |
| 暴力破解 | 登录失败限制 | Redis 计数器 |
| 密码泄露 | 密码加密存储 | BCrypt 哈希 |
| 敏感数据泄露 | 最小权限原则 | 数据隔离 |

---

## 3. 架构模式

### 3.1 选择模式: 分层架构 (Layered Architecture)

**选择理由**:

1. **团队熟悉**: 开发团队对分层架构有丰富经验
2. **简单直观**: 代码结构清晰，易于理解和维护
3. **适合 CRUD**: 待办事项系统主要是 CRUD 操作，分层架构足够
4. **测试友好**: 每层可独立测试，支持单元测试和集成测试
5. **参考资料**: ruoyi-example 项目采用相同模式，便于参考

**应用方式**:

- **Controller 层**: 只处理 HTTP 请求/响应，不包含业务逻辑
- **Service 层**: 实现业务逻辑，可复用，支持事务
- **Mapper 层**: 封装数据访问，使用 MyBatis-Plus 简化 CRUD
- **Domain 层**: 贫血模型，Entity 只包含数据，不包含行为

**架构特点**:

```
优点:
✓ 结构清晰，易于理解
✓ 关注点分离，降低耦合
✓ 可测试性强
✓ 便于团队协作

缺点:
✗ 层间传递可能带来性能开销
✗ 可能出现"贫血模型"问题
✗ 复杂业务逻辑可能分散
```

### 3.2 其他考虑的架构模式

#### MVC 模式 (Model-View-Controller)

- **评估**: 传统 Web 应用使用，但前后端分离后不再适用
- **决策**: 不采用，使用 RESTful API 替代

#### 六边形架构 (Hexagonal Architecture)

- **评估**: 领域驱动设计（DDD）项目适用，但本系统业务简单
- **决策**: 不采用，避免过度设计

#### 微服务架构 (Microservices Architecture)

- **评估**: 适合大规模分布式系统，但本系统是单体演示应用
- **决策**: 不采用，保持单体架构

---

## 4. 技术栈

### 4.1 后端技术

| 技术 | 版本 | 用途 | 选择理由 |
|------|------|------|----------|
| **Java** | 17 | 编程语言 | LTS 版本，性能优秀 |
| **Spring Boot** | 3.2.0 | 应用框架 | 简化配置，快速开发 |
| **Spring Security** | 6.x | 安全框架 | 成熟的安全解决方案 |
| **MyBatis-Plus** | 3.5.5 | ORM 框架 | 简化 CRUD，代码生成 |
| **JWT** | 0.12.x | Token 认证 | 无状态认证 |
| **Flyway** | 9.x | 数据库迁移 | 版本化数据库 schema |
| **Swagger/OpenAPI** | 2.x | API 文档 | 自动生成文档 |

### 4.2 数据库

| 技术 | 版本 | 用途 | 选择理由 |
|------|------|------|----------|
| **MySQL** | 8.0+ | 关系数据库 | 成熟稳定，事务支持好 |
| **H2** | 2.x | 内存数据库（开发） | 快速启动，测试友好 |

### 4.3 开发工具

| 工具 | 版本 | 用途 |
|------|------|------|
| **Maven** | 3.9+ | 依赖管理 |
| **Git** | 2.x | 版本控制 |
| **JUnit** | 5.x | 单元测试 |
| **Mockito** | 5.x | Mock 测试 |
| **Docker** | 20.x | 容器化部署 |

### 4.4 技术栈选择理由

**Spring Boot 3.2.0**:
- Spring Boot 3.x 基于 Jakarta EE 9+，支持 Java 17
- 性能提升，启动速度更快
- AOT 编译支持（GraalVM）
- 社区活跃，文档完善

**MyBatis-Plus**:
- 在 MyBatis 基础上增强，简化 CRUD
- 代码生成器，提高开发效率
- 内置分页插件，性能优化
- 团队熟悉，有 ruoyi-example 参考

**JWT 认证**:
- 无状态，适合 RESTful API
- 跨域支持好
- 移动端友好
- 性能优秀（无需 Session 存储）

**MySQL 数据库**:
- 开源免费，社区活跃
- 事务支持完善（ACID）
- 索引和查询优化成熟
- 支持全文索引（未来搜索功能）

**Flyway 迁移**:
- 版本化数据库 schema
- 支持多种数据库
- 自动升级，易于团队协作
- 回滚机制（可选）

---

## 5. 质量属性

### 5.1 性能

| 指标 | 目标值 | 优化策略 |
|------|--------|----------|
| API 响应时间 | <= 500ms (95分位) | 索引优化，查询优化 |
| 数据库查询时间 | <= 100ms (95分位) | 索引，分页查询 |
| 并发用户数 | >= 50 | 连接池优化 |
| 内存占用 | <= 512MB | JVM 调优 |

**性能优化措施**:
1. **数据库索引**: 在 `user_id`, `status`, `due_date` 字段建立索引
2. **分页查询**: 使用 MyBatis-Plus 分页插件
3. **懒加载**: 关联数据按需加载
4. **连接池**: HikariCP 高性能连接池
5. **缓存策略**: 预留 Redis 集成接口

### 5.2 可扩展性

**水平扩展**:
- 应用服务无状态，支持多实例部署
- 负载均衡器分发请求
- Session 存储在 JWT 中，无需共享

**垂直扩展**:
- 模块化设计，便于功能扩展
- 接口抽象，便于替换实现
- 配置化，支持动态调整

**扩展点**:
1. **缓存层**: 预留 Redis 集成接口
2. **搜索服务**: 预留 Elasticsearch 集成
3. **消息队列**: 预留 RabbitMQ/Kafka 集成
4. **文件存储**: 预留 OSS/S3 集成

### 5.3 可靠性

| 指标 | 目标值 | 保证措施 |
|------|--------|----------|
| 系统可用性 | >= 99% | 健康检查，自动重启 |
| 故障恢复时间 | < 2小时 | 监控告警，备份恢复 |
| 数据备份 | 每日 | 自动备份脚本 |

**可靠性措施**:
1. **异常处理**: 统一异常处理机制
2. **日志记录**: 完整的操作日志
3. **数据备份**: MySQL 定时备份
4. **健康检查**: Actuator 健康检查端点
5. **优雅停机**: Spring Boot 优雅停机支持

### 5.4 安全性

**认证和授权**:
- JWT Token 认证
- Token 过期机制（24小时）
- 登录失败限制（5次锁定30分钟）
- 密码复杂度验证

**数据安全**:
- 密码 BCrypt 加密存储
- SQL 参数化查询防注入
- API 输入验证
- HTTPS 传输加密

**审计和监控**:
- 操作日志记录
- 登录/登出日志
- 异常日志
- 安全事件告警

### 5.5 可维护性

**代码质量**:
- 遵循阿里巴巴 Java 开发规范
- 单元测试覆盖率 >= 80%
- 代码注释率 >= 30%（核心类）
- SonarQube 静态代码分析

**文档完整性**:
- API 文档（Swagger/OpenAPI）
- 架构设计文档
- 数据库设计文档
- 部署运维文档

**开发规范**:
- Git 分支管理策略
- Code Review 机制
- CI/CD 自动化流程
- 版本管理规范

---

## 6. 接口设计

### 6.1 RESTful API 规范

**URL 设计**:
```
/api/{version}/{resource}/{id}

示例:
GET    /api/v1/todos              # 获取待办列表
GET    /api/v1/todos/123          # 获取指定待办
POST   /api/v1/todos              # 创建待办
PUT    /api/v1/todos/123          # 更新待办
DELETE /api/v1/todos/123          # 删除待办
```

**HTTP 方法语义**:
| 方法 | 操作 | 幂等性 | 安全性 |
|------|------|--------|--------|
| GET | 查询 | ✓ | ✓ |
| POST | 创建 | ✗ | ✗ |
| PUT | 更新（全部） | ✓ | ✗ |
| PATCH | 更新（部分） | ✓ | ✗ |
| DELETE | 删除 | ✓ | ✗ |

**状态码规范**:
| 状态码 | 含义 | 使用场景 |
|--------|------|----------|
| 200 | OK | 查询成功 |
| 201 | Created | 创建成功 |
| 204 | No Content | 删除成功 |
| 400 | Bad Request | 参数错误 |
| 401 | Unauthorized | 未认证 |
| 403 | Forbidden | 无权限 |
| 404 | Not Found | 资源不存在 |
| 500 | Internal Server Error | 服务器错误 |

### 6.2 统一响应格式

**成功响应**:
```json
{
  "code": 200,
  "message": "success",
  "data": {
    "id": 123,
    "title": "完成项目文档"
  },
  "timestamp": "2026-01-26T12:00:00Z"
}
```

**错误响应**:
```json
{
  "code": 400,
  "message": "参数验证失败",
  "errors": [
    {
      "field": "title",
      "message": "标题不能为空"
    }
  ],
  "timestamp": "2026-01-26T12:00:00Z"
}
```

**分页响应**:
```json
{
  "code": 200,
  "message": "success",
  "data": {
    "records": [...],
    "total": 100,
    "page": 1,
    "size": 20,
    "pages": 5
  }
}
```

### 6.3 API 接口列表

详见 Swagger 文档: `http://localhost:8080/swagger-ui.html`

**核心接口**:
- 认证模块: `/api/v1/auth/**`
- 待办管理: `/api/v1/todos/**`
- 分类管理: `/api/v1/categories/**`
- 用户管理: `/api/v1/users/**`

---

## 7. 架构决策

### 关键决策列表

| ADR 编号 | 决策标题 | 状态 | 日期 |
|----------|----------|------|------|
| ADR-001 | 分层架构模式 | 已接受 | 2026-01-26 |
| ADR-002 | 技术栈选择 | 已接受 | 2026-01-26 |
| ADR-003 | 数据库设计方法 | 已接受 | 2026-01-26 |
| ADR-004 | 认证授权策略 | 已接受 | 2026-01-26 |
| ADR-005 | API 设计规范 | 已接受 | 2026-01-26 |

详细的架构决策记录请参考: `docs/architecture/adr-records.md`

---

## 8. 架构图

### 8.1 系统架构图

详见: `docs/architecture/diagrams.md`

包含图表:
- 系统架构图 (整体架构)
- 组件图 (组件关系)
- 部署架构图 (部署拓扑)
- 时序图 (交互流程)
- 状态图 (状态转换)
- ER 图 (数据模型)
- 类图 (类结构)

### 8.2 组件图

详见: `docs/architecture/component-diagram.md`

### 8.3 部署架构图

详见: `docs/architecture/deployment-architecture.md`

---

## 9. 相关文档

- 架构决策记录: `docs/architecture/adr-records.md`
- 架构图集: `docs/architecture/diagrams.md`
- 组件图: `docs/architecture/component-diagram.md`
- 部署架构: `docs/architecture/deployment-architecture.md`
- 技术栈: `docs/architecture/technology-stack.md`
- 需求规格: `docs/requirements/requirements-spec.md`
- 用户故事: `docs/requirements/user-stories.md`
- 设计系统: `docs/design/design-system.md`

---

## 10. 附录

### 10.1 术语表

| 术语 | 定义 |
|------|------|
| 分层架构 | 将系统按职责划分为多个层次的架构模式 |
| JWT | JSON Web Token，一种无状态认证机制 |
| RESTful API | 符合 REST 架构风格的 API 设计 |
| ORM | Object-Relational Mapping，对象关系映射 |
| DTO | Data Transfer Object，数据传输对象 |
| CRUD | Create, Read, Update, Delete，增删改查 |
| ADR | Architecture Decision Record，架构决策记录 |

### 10.2 参考资源

- Spring Boot 官方文档: https://spring.io/projects/spring-boot
- Spring Security 参考指南: https://docs.spring.io/spring-security/
- MyBatis-Plus 官方文档: https://baomidou.com/
- RESTful API 设计指南: https://restfulapi.net/
- 阿里巴巴 Java 开发规范
- ruoyi-example 项目代码

---

## 批准

| 角色 | 姓名 | 签名 | 日期 |
|------|------|------|------|
| 技术负责人 | | | |
| 架构师 | | | |
| 开发团队代表 | | | |
