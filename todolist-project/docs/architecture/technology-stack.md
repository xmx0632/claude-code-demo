# 技术栈文档 (Technology Stack)

> **项目**: TodoList 待办事项管理系统
> **版本**: 1.0
> **最后更新**: 2026-01-26

---

## 1. 技术栈概览

### 1.1 技术栈分层

```
┌─────────────────────────────────────────────────────────┐
│                    前端技术（可选）                      │
│              Vue.js / React / Angular                   │
├─────────────────────────────────────────────────────────┤
│                    后端技术                              │
│    Java 17 + Spring Boot 3.2.0 + MyBatis-Plus          │
├─────────────────────────────────────────────────────────┤
│                    数据存储                              │
│         MySQL 8.0+ + Flyway + H2（开发）                │
├─────────────────────────────────────────────────────────┤
│                    基础设施                              │
│     Docker + Nginx + Git + Maven + Jenkins              │
├─────────────────────────────────────────────────────────┤
│                    开发工具                              │
│      IntelliJ IDEA + Postman + Chrome DevTools          │
└─────────────────────────────────────────────────────────┘
```

### 1.2 技术选型原则

- **成熟稳定**: 选择成熟、社区活跃的技术
- **团队熟悉**: 优先选择团队熟悉的技术栈
- **性能优秀**: 满足性能要求（API <= 500ms）
- **可维护性**: 代码规范，文档完善，易于维护
- **可扩展性**: 支持功能扩展和性能扩展
- **成本考虑**: 优先使用开源技术，降低成本

---

## 2. 后端技术栈

### 2.1 核心框架

| 技术 | 版本 | 用途 | 官网 |
|------|------|------|------|
| **Java** | 17 (LTS) | 编程语言 | https://openjdk.org/projects/jdk/17/ |
| **Spring Boot** | 3.2.0 | 应用框架 | https://spring.io/projects/spring-boot |
| **Spring Security** | 6.x | 安全框架 | https://spring.io/projects/spring-security |
| **Spring Web** | 6.x | Web MVC | https://docs.spring.io/spring-framework/docs/current/reference/html/web.html |

#### Spring Boot 3.2.0

**选择理由**:
- 基于 Jakarta EE 9+，支持 Java 17
- 自动配置，简化开发
- 内嵌 Tomcat，无需外部应用服务器
- 生产级特性：健康检查、指标监控
- 社区活跃，文档完善，生态丰富

**核心特性**:
- 自动配置（Auto Configuration）
- 起步依赖（Starter Dependencies）
- 命令行界面（Spring Boot CLI）
- Actuator 监控端点

**依赖管理**:
```xml
<parent>
    <groupId>org.springframework.boot</groupId>
    <artifactId>spring-boot-starter-parent</artifactId>
    <version>3.2.0</version>
    <relativePath/>
</parent>
```

### 2.2 数据访问技术

| 技术 | 版本 | 用途 | 官网 |
|------|------|------|------|
| **MyBatis-Plus** | 3.5.5 | ORM 框架 | https://baomidou.com/ |
| **HikariCP** | 5.x | 数据库连接池 | https://github.com/brettwooldridge/HikariCP |
| **Flyway** | 9.x | 数据库迁移 | https://flywaydb.org/ |
| **MySQL Connector** | 8.0.x | MySQL 驱动 | https://dev.mysql.com/downloads/connector/j/ |

#### MyBatis-Plus 3.5.5

**选择理由**:
- 在 MyBatis 基础上增强，简化 CRUD
- 内置分页插件，性能优化
- 代码生成器，提高开发效率
- 支持 Lambda 表达式，类型安全
- 与参考项目 ruoyi-example 一致

**核心特性**:
- CRUD 接口: `BaseMapper<T>`
- 分页插件: `PaginationInnerInterceptor`
- 乐观锁插件: `OptimisticLockerInnerInterceptor`
- 逻辑删除: `@TableLogic`
- 代码生成器: `AutoGenerator`

**示例**:
```java
// Mapper 接口
public interface UserMapper extends BaseMapper<User> {
    // 继承 BaseMapper，自动拥有 CRUD 方法
}

// 使用示例
User user = userMapper.selectById(123);
List<User> users = userMapper.selectList(null);
userMapper.insert(user);
userMapper.updateById(user);
userMapper.deleteById(123);
```

### 2.3 安全技术

| 技术 | 版本 | 用途 | 官网 |
|------|------|------|------|
| **Spring Security** | 6.x | 认证授权框架 | https://spring.io/projects/spring-security |
| **JWT (JJWT)** | 0.12.x | Token 认证 | https://github.com/jwtk/jjwt |
| **BCrypt** | 内置 | 密码加密 | https://github.com/patrickfav/bcrypt |

#### JWT 认证

**选择理由**:
- 无状态认证，适合 RESTful API
- 跨域支持好，前后端分离友好
- Token 自包含，减少数据库查询
- 移动端友好

**JWT 结构**:
```json
// Header
{
  "alg": "HS256",
  "typ": "JWT"
}

// Payload
{
  "user_id": 123,
  "username": "testuser",
  "exp": 1706659200,
  "iat": 1706572800
}

// Signature
HMACSHA256(
  base64UrlEncode(header) + "." + base64UrlEncode(payload),
  secret
)
```

**使用示例**:
```java
// 生成 Token
String token = Jwts.builder()
    .subject(username)
    .claim("user_id", userId)
    .issuedAt(new Date())
    .expiration(Date.from(Instant.now().plus(24, ChronoUnit.HOURS)))
    .signWith(secretKey)
    .compact();

// 验证 Token
Claims claims = Jwts.parser()
    .verifyWith(secretKey)
    .build()
    .parseSignedClaims(token)
    .getPayload();
```

#### BCrypt 密码加密

**选择理由**:
- 单向哈希，不可逆
- 自动加盐，防止彩虹表攻击
- 计算复杂度可调，抵抗暴力破解
- Spring Security 内置支持

**示例**:
```java
// 加密密码
String encodedPassword = passwordEncoder.encode("rawPassword");

// 验证密码
boolean matches = passwordEncoder.matches("rawPassword", encodedPassword);
```

### 2.4 工具库

| 技术 | 版本 | 用途 | 官网 |
|------|------|------|------|
| **Lombok** | 1.18.x | 简化 Java 代码 | https://projectlombok.org/ |
| **MapStruct** | 1.5.x | 对象映射 | https://mapstruct.org/ |
| **Hutool** | 5.x | Java 工具库 | https://hutool.cn/ |
| **Apache Commons** | 3.x | 通用工具类 | https://commons.apache.org/ |

#### Lombok

**常用注解**:
```java
@Data                  // getter/setter/toString/equals/hashCode
@AllArgsConstructor     // 全参构造器
@NoArgsConstructor      // 无参构造器
@Builder              // 建造者模式
@Slf4j                // 日志对象
@Entity               // JPA 实体
@Table(name = "user")  // 表名
```

---

## 3. 数据库技术

### 3.1 关系型数据库

| 技术 | 版本 | 用途 | 官网 |
|------|------|------|------|
| **MySQL** | 8.0+ | 生产数据库 | https://www.mysql.com/ |
| **H2** | 2.x | 开发/测试数据库 | https://www.h2database.com/ |

#### MySQL 8.0+

**选择理由**:
- 开源免费，社区活跃
- 事务支持完善（ACID）
- 索引和查询优化成熟
- 支持全文索引（未来搜索）
- 云服务支持好（AWS RDS, 阿里云 RDS）

**核心特性**:
- InnoDB 存储引擎（支持事务和外键）
- JSON 字段类型（灵活存储）
- 窗口函数（复杂查询）
- CTE（公共表表达式）
- 性能 Schema（性能分析）

**配置优化**:
```sql
-- my.cnf
[mysqld]
# 连接数
max_connections = 200

-- 缓冲池大小（物理内存的 70-80%）
innodb_buffer_pool_size = 2G

-- 日志文件大小
innodb_log_file_size = 512M

-- 查询缓存（MySQL 8.0 已移除）
-- query_cache_size = 128M

-- 慢查询日志
slow_query_log = 1
long_query_time = 2
```

### 3.2 数据库迁移

#### Flyway 9.x

**选择理由**:
- 版本化数据库 schema
- 支持多种数据库
- 自动升级，易于团队协作
- SQL 迁移脚本，简单直观

**迁移脚本命名**:
```
db/migration/
├── V1__init_schema.sql
├── V2__create_user_table.sql
├── V3__create_todo_table.sql
├── V4__create_category_table.sql
└── V5__create_todo_category_table.sql
```

**配置**:
```yaml
spring:
  flyway:
    enabled: true
    locations: classpath:db/migration
    baseline-on-migrate: true
    validate-on-migrate: true
```

---

## 4. 开发工具

### 4.1 IDE 和编辑器

| 工具 | 版本 | 用途 | 下载 |
|------|------|------|------|
| **IntelliJ IDEA** | 2023.x | Java 开发 IDE | https://www.jetbrains.com/idea/ |
| **Visual Studio Code** | 最新 | 文本编辑器 | https://code.visualstudio.com/ |
| **Navicat** | 16.x | 数据库管理工具 | https://www.navicat.com/ |

#### IntelliJ IDEA 推荐插件

| 插件名称 | 用途 |
|----------|------|
| Lombok | 支持 Lombok 注解 |
| MyBatisX | MyBatis 增强插件 |
| Rainbow Brackets | 彩虹括号 |
| GitToolBox | Git 增强 |
| Alibaba Java Coding Guidelines | 代码规范检查 |
| SonarLint | 代码质量检查 |

### 4.2 API 测试工具

| 工具 | 版本 | 用途 | 下载 |
|------|------|------|------|
| **Postman** | 最新 | API 测试 | https://www.postman.com/ |
| **Apifox** | 最新 | API 设计和测试 | https://apifox.com/ |
| **curl** | 最新 | 命令行 HTTP 工具 | 系统自带 |

### 4.3 版本控制

| 工具 | 版本 | 用途 | 下载 |
|------|------|------|------|
| **Git** | 2.x | 版本控制 | https://git-scm.com/ |
| **GitHub** | - | 代码托管 | https://github.com/ |
| **GitLab** | - | 代码托管 + CI/CD | https://about.gitlab.com/ |

### 4.4 构建工具

| 工具 | 版本 | 用途 | 下载 |
|------|------|------|------|
| **Maven** | 3.9+ | 依赖管理和构建 | https://maven.apache.org/ |
| **Java** | 17+ | 运行环境 | https://openjdk.org/ |

#### Maven 配置

**`pom.xml` 关键配置**:
```xml
<properties>
    <java.version>17</java.version>
    <maven.compiler.source>17</maven.compiler.source>
    <maven.compiler.target>17</maven.compiler.target>
    <project.build.sourceEncoding>UTF-8</project.build.sourceEncoding>
</properties>

<dependencies>
    <!-- Spring Boot Starter -->
    <dependency>
        <groupId>org.springframework.boot</groupId>
        <artifactId>spring-boot-starter-web</artifactId>
    </dependency>

    <!-- MyBatis-Plus -->
    <dependency>
        <groupId>com.baomidou</groupId>
        <artifactId>mybatis-plus-boot-starter</artifactId>
        <version>3.5.5</version>
    </dependency>
</dependencies>
```

---

## 5. 基础设施

### 5.1 容器化技术

| 技术 | 版本 | 用途 | 下载 |
|------|------|------|------|
| **Docker** | 20.x | 容器化 | https://www.docker.com/ |
| **Docker Compose** | 2.x | 多容器编排 | https://docs.docker.com/compose/ |

#### Docker 镜像

```dockerfile
# 基础镜像
FROM openjdk:17-jdk-slim

# 工作目录
WORKDIR /app

# 复制 JAR 文件
COPY target/todolist-app-1.0.0.jar app.jar

# 暴露端口
EXPOSE 8080

# 启动命令
ENTRYPOINT ["java", "-jar", "app.jar"]
```

### 5.2 Web 服务器

| 技术 | 版本 | 用途 | 下载 |
|------|------|------|------|
| **Nginx** | 1.24.x | 反向代理 + 负载均衡 | https://nginx.org/ |

#### Nginx 作用

- 反向代理：转发请求到应用服务器
- 负载均衡：分发请求到多个应用实例
- SSL 终止：处理 HTTPS 加密
- 静态资源服务：缓存静态文件
- 请求限流：保护应用服务器

### 5.3 CI/CD 工具

| 工具 | 版本 | 用途 | 下载 |
|------|------|------|------|
| **Jenkins** | 2.x | 持续集成/部署 | https://www.jenkins.io/ |
| **GitLab CI** | 内置 | 持续集成/部署 | https://docs.gitlab.com/ee/ci/ |
| **GitHub Actions** | 内置 | 持续集成/部署 | https://github.com/features/actions |

---

## 6. 监控和日志

### 6.1 应用监控

| 工具 | 版本 | 用途 | 下载 |
|------|------|------|------|
| **Spring Boot Actuator** | 3.x | 应用监控端点 | 内置 |
| **Prometheus** | 2.x | 指标收集 | https://prometheus.io/ |
| **Grafana** | 10.x | 可视化仪表盘 | https://grafana.com/ |

### 6.2 日志管理

| 工具 | 版本 | 用途 | 下载 |
|------|------|------|------|
| **Logback** | 1.4.x | 日志框架 | 内置 |
| **ELK Stack** | 8.x | 日志收集和分析 | https://www.elastic.co/ |
| **Loki** | 2.x | 轻量级日志系统 | https://grafana.com/oss/loki/ |

---

## 7. 前端技术（可选）

### 7.1 前端框架

| 框架 | 版本 | 用途 | 官网 |
|------|------|------|------|
| **Vue.js** | 3.x | 渐进式框架 | https://vuejs.org/ |
| **React** | 18.x | UI 库 | https://react.dev/ |
| **Angular** | 17.x | 完整框架 | https://angular.io/ |

### 7.2 UI 组件库

| 库 | 框架 | 官网 |
|----|------|------|
| **Ant Design** | React | https://ant.design/ |
| **Element Plus** | Vue 3 | https://element-plus.org/ |
| **Material-UI** | React | https://mui.com/ |

---

## 8. 技术栈版本清单

### 8.1 后端技术

| 分类 | 技术 | 版本 |
|------|------|------|
| 编程语言 | Java | 17 (LTS) |
| 应用框架 | Spring Boot | 3.2.0 |
| 安全框架 | Spring Security | 6.x |
| ORM 框架 | MyBatis-Plus | 3.5.5 |
| 数据库驱动 | MySQL Connector | 8.0.x |
| 连接池 | HikariCP | 5.x |
| 数据库迁移 | Flyway | 9.x |
| JWT | JJWT | 0.12.x |
| 工具库 | Lombok | 1.18.x |
| 构建工具 | Maven | 3.9+ |

### 8.2 数据库技术

| 分类 | 技术 | 版本 |
|------|------|------|
| 生产数据库 | MySQL | 8.0+ |
| 开发数据库 | H2 | 2.x |
| 数据库管理 | Navicat | 16.x |

### 8.3 基础设施

| 分类 | 技术 | 版本 |
|------|------|------|
| 容器化 | Docker | 20.x |
| Web 服务器 | Nginx | 1.24.x |
| 版本控制 | Git | 2.x |
| CI/CD | Jenkins | 2.x |

---

## 9. 技术选型对比

### 9.1 ORM 框架对比

| 框架 | 优点 | 缺点 | 选择 |
|------|------|------|------|
| **MyBatis-Plus** | 简化 CRUD，代码生成，团队熟悉 | 魔法较多 | ✓ 选择 |
| Spring Data JPA | 更简洁， Convention over Configuration | 复杂查询不如 MyBatis | ✗ |
| 纯 MyBatis | 灵活，SQL 可控 | CRUD 代码量大 | ✗ |
| Hibernate | 功能强大 | 学习曲线陡峭 | ✗ |

### 9.2 认证方案对比

| 方案 | 优点 | 缺点 | 选择 |
|------|------|------|------|
| **JWT** | 无状态，跨域，移动端友好 | 无法主动撤销 | ✓ 选择 |
| Session + Cookie | 可控制，可撤销 | 服务器存储，扩展性差 | ✗ |
| OAuth 2.0 | 标准化，支持第三方 | 复杂，过度设计 | ✗ |

---

## 10. 技术栈演进规划

### 10.1 当前阶段（MVP）

**核心目标**: 快速构建可用的待办事项管理系统

**技术栈**:
- Spring Boot 3.2.0
- MySQL 8.0+
- MyBatis-Plus 3.5.5
- JWT 认证

### 10.2 未来扩展（可选）

**性能优化**:
- [ ] Redis 缓存（热点数据、Session）
- [ ] 数据库读写分离
- [ ] 分库分表（ShardingSphere）

**功能增强**:
- [ ] Elasticsearch 全文搜索
- [ ] RabbitMQ/Kafka 消息队列
- [ ] 定时任务（Quartz/Spring Scheduler）

**监控运维**:
- [ ] Prometheus + Grafana 监控
- [ ] ELK 日志分析
- [ ] SkyWalking 链路追踪

**微服务化**（长期）:
- [ ] Spring Cloud 微服务架构
- [ ] 服务注册与发现（Nacos/Eureka）
- [ ] 配置中心（Nacos Config）
- [ ] 服务网关（Spring Cloud Gateway）

---

## 11. 技术栈学习资源

### 11.1 官方文档

- Spring Boot: https://spring.io/projects/spring-boot
- Spring Security: https://docs.spring.io/spring-security/
- MyBatis-Plus: https://baomidou.com/
- MySQL: https://dev.mysql.com/doc/

### 11.2 推荐书籍

- 《Spring Boot 实战》
- 《深入理解 Spring Boot》
- 《MyBatis 从入门到精通》
- 《MySQL 必知必会》
- 《Java 并发编程实战》

### 11.3 在线课程

- Spring Boot 官方指南
- Bilibili Spring Boot 教程
- 极客时间 Spring Boot 专栏

---

## 12. 相关文档

- 系统架构设计: `docs/architecture/architecture.md`
- 架构决策记录: `docs/architecture/adr-records.md`
- 部署架构: `docs/architecture/deployment-architecture.md`
- 开发环境搭建: （待创建）
