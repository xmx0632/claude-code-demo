# 架构规范

## 分层架构

```
┌─────────────────────────────────────────────────────────┐
│                    Presentation Layer                    │
│                   (Controller / View)                    │
│  职责: 处理 HTTP 请求/响应，不包含业务逻辑                 │
└────────────────────┬────────────────────────────────────┘
                     │ 只能调用 Service
                     ▼
┌─────────────────────────────────────────────────────────┐
│                      Service Layer                       │
│                (Business Logic / Service)                │
│  职责: 业务逻辑处理，事务管理                              │
└────────────────────┬────────────────────────────────────┘
                     │ 只能调用 Repository 和其他 Service
                     ▼
┌─────────────────────────────────────────────────────────┐
│                   Repository Layer                      │
│                  (Data Access / Mapper)                  │
│  职责: 数据访问，SQL 执行                                  │
└────────────────────┬────────────────────────────────────┘
                     │ 只能调用 Domain
                     ▼
┌─────────────────────────────────────────────────────────┐
│                      Domain Layer                        │
│              (Entity / DTO / Enum / VO)                  │
│  职责: 数据模型，无业务逻辑                                 │
└─────────────────────────────────────────────────────────┘
```

## 依赖规则

### 允许的依赖

| 源层 | 目标层 | 说明 |
|------|--------|------|
| Controller | Service | 请求转发 |
| Controller | Domain | 数据传输 |
| Service | Service | 服务协作 |
| Service | Repository | 数据访问 |
| Service | Domain | 数据处理 |
| Repository | Domain | 数据映射 |

### 禁止的依赖

| 违规 | 示例 | 后果 |
|------|------|------|
| UI → Repository | Controller 直接调用 Mapper | 跳过业务逻辑 |
| UI → Domain Entity | Controller 直接返回 Entity | 数据泄露 |
| Repository → Service | Mapper 调用 Service | 循环依赖 |
| Service → Controller | Service 调用 Controller | 架构混乱 |

## 模块划分

```
ruoyi-example/
├── controller/          # 控制器层
├── service/            # 服务层
│   ├── IXXXService.java
│   └── impl/           # 实现类
├── mapper/             # 数据访问层
├── domain/             # 领域模型层
│   ├── entity/         # 实体
│   ├── dto/            # 数据传输对象
│   └── vo/             # 视图对象
└── common/             # 通用组件
    ├── config/         # 配置
    ├── constant/       # 常量
    ├── exception/      # 异常
    └── utils/          # 工具
```

## ADR (架构决策记录)

重要的架构决策应记录在 ADR 中：

```
docs/architecture/adr/
├── 001-use-spring-boot.md
├── 002-orm-selection-mybatis-plus.md
├── 003-api-versioning.md
└── ...
```

### ADR 模板

```markdown
# ADR-XXX: 决策标题

## 状态
提议 / 已接受 / 已弃用 / 已替代

## 上下文
描述驱动此决策的问题或情况。

## 决策
描述我们做出的决定。

## 后果
描述应用此决策的结果，包括正面和负面影响。
```

## 技术选型

| 组件 | 选择 | 理由 |
|------|------|------|
| Web 框架 | Spring Boot 3.2.0 | 成熟稳定，生态丰富 |
| ORM | MyBatis-Plus 3.5.5 | 灵活的 SQL 控制 |
| 数据库 | MySQL 8.0 | 广泛使用，可靠性高 |
| 缓存 | Redis 7 | 高性能，数据结构丰富 |
| API 文档 | Knife4j 4.3.0 | 基于 Swagger，增强 UI |
| 构建工具 | Maven 3.9+ | 标准化构建 |
| 测试框架 | JUnit 5 + Mock | 标准测试栈 |

## 扩展性考虑

### 横向扩展
- 无状态服务设计
- Session 使用 Redis 存储
- 负载均衡支持

### 纵向扩展
- 数据库读写分离
- 缓存策略优化
- 异步处理机制

---

**最后更新**: 2026-03-15
