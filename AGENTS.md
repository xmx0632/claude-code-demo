# AGENTS.md
# Claude Code + SDLC Framework Agent 指南

## 项目定位

这是一个 **SDLC 框架演示项目**，展示 Claude Code 在企业 Java 开发中的应用。

**技术栈**: Spring Boot 3.2.0 + Java 17 + MyBatis-Plus + Flyway

**目的**: 为 Claude Code Agent 提供约束和上下文，确保代码质量和架构一致性。

---

## 核心约束

### 代码规范
- 使用 Java 17 特性（Record、Pattern Matching、Text Blocks）
- Lombok 注解减少样板代码
- RESTful API 设计规范
- 代码覆盖率 >= 80%

### 安全要求
- 禁止硬编码密钥
- 禁止 SQL 注入（使用参数化查询）
- 禁止 XSS 漏洞（输入验证+输出编码）
- 敏感数据必须加密

### 性能标准
- API 响应时间 < 2s
- 数据库查询 < 1s
- 单个方法不超过 50 行

---

## 架构边界

### 分层架构依赖流向

```
┌─────────────────────────────────────────────────────────┐
│                        UI Layer                         │
│                   (Controller/View)                     │
└────────────────────┬────────────────────────────────────┘
                     │
                     ▼
┌─────────────────────────────────────────────────────────┐
│                     Service Layer                       │
│              (Business Logic/Service)                   │
└────────────────────┬────────────────────────────────────┘
                     │
                     ▼
┌─────────────────────────────────────────────────────────┐
│                   Repository Layer                      │
│                  (Data Access/Mapper)                   │
└────────────────────┬────────────────────────────────────┘
                     │
                     ▼
┌─────────────────────────────────────────────────────────┐
│                     Domain Layer                        │
│                (Entity/DTO/Enum)                        │
└─────────────────────────────────────────────────────────┘
```

**禁止反向依赖！** UI 不能直接访问 Repository，Service 不能访问 UI。

### 模块依赖规则

```
Controller → Service → Repository → Domain
```

- Controller 只处理 HTTP 请求/响应
- Service 包含业务逻辑
- Repository 处理数据访问
- Domain 是纯数据模型

---

## 常见命令

### 开发命令
```bash
# 构建项目
mvn clean install

# 运行应用
mvn spring-boot:run

# 运行测试
mvn test

# 代码覆盖率
mvn jacoco:report
```

### SDLC 框架命令
```bash
# 完整 SDLC
/sdlc-full "功能描述"

# 分阶段执行
/requirements-analysis "需求描述"
/ruoyi-crud table_name
/test-gen ServiceName
/code-review
```

### Docker 命令
```bash
cd docker && docker-compose up -d
```

---

## Agent 行为准则

### 何时使用 Skills
| Skill | 使用时机 |
|-------|----------|
| `ruoyi-crud` | 需要为数据库表生成 CRUD 代码 |
| `code-review` | 代码提交前审查 |
| `test-gen` | Service 层方法编写后 |
| `api-doc` | Controller 完成后 |
| `flyway-migration` | 数据库结构变更时 |
| `sql-optimizer` | SQL 性能问题时 |

### 代码生成流程
```
1. 分析需求 → 理解业务逻辑
2. 查看架构 → 确认模块边界
3. 生成代码 → 使用 Skills
4. 编写测试 → test-gen
5. 代码审查 → code-review
6. 运行测试 → mvn test
```

### 质量检查点
- 单元测试覆盖率 >= 80%
- Lint 无错误
- 安全扫描无高危漏洞
- 代码审查通过

---

## 详细文档索引

| 文档 | 路径 | 触发时机 |
|------|------|----------|
| 架构规范 | `docs/architecture/` | 系统设计阶段 |
| 设计指南 | `docs/design/` | UI/UX 设计阶段 |
| 质量标准 | `SDLC-Framework/config/quality-gates.yaml` | 质量检查 |
| API 规范 | `docs/api/` | 接口开发 |
| 测试指南 | `docs/testing/` | 测试编写 |
| 部署指南 | `docs/deployment/` | 系统部署 |
| SDLC 框架 | `docs/sdlc/` | 流程开发 |

---

## 系统维护

### 熵管理

系统熵（混乱度）会随时间自然增加，需要定期维护：

```bash
# 检查系统熵
cd SDLC-Framework/scripts
./entropy-check.sh --mode=full

# 清理系统熵
./entropy-cleanup.sh --dry-run   # 预览
./entropy-cleanup.sh --execute   # 执行
```

**熵指数阈值**:
- 优秀: < 3 (绿色)
- 良好: < 5 (绿色)
- 警告: < 7 (黄色) - 建议清理
- 严重: >= 7 (红色) - 立即清理

### 可观测性

Agent 可以访问运行时数据：
- 日志查询: 通过 LogQL
- 指标查询: 通过 PromQL
- 追踪查询: 通过 Jaeger/Zipkin

配置文件: `SDLC-Framework/config/observability.yaml`

---

## 错误处理

### 常见错误与修复

| 错误 | 原因 | 修复方法 |
|------|------|----------|
| Layer violation | 违反依赖方向 | 按架构流向重构 |
| Low coverage | 缺少测试 | 使用 /test-gen |
| Security warning | 安全问题 | 查看 docs/security/ |
| Circular dependency | 循环依赖 | 提取公共接口 |

---

## 运行时反馈

Agent 可以查询：
- 日志: 通过日志文件查看运行状态
- 测试结果: `mvn test` 输出
- 覆盖率报告: `target/site/jacoco/index.html`

---

**最后更新**: 2026-03-15

**版本**: 1.0.0
