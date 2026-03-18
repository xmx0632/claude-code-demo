# 图书库存管理系统 - AI 教学文档

> 本文档记录了使用 AI (Claude Code) 开发图书库存管理系统的完整过程，包括需求分析、代码实现、问题排查等各个阶段。

---

## 项目概述

**项目名称**: 图书库存管理系统 (Library Inventory Management System)

**技术栈**:
- 后端: Spring Boot 3.2.0 + JPA + Flyway + SQLite
- 前端: Vue 3 + Element Plus + Vite
- 开发工具: Claude Code (AI 编程助手)

**开发时间**: 2026-02-25

**仓库地址**: https://github.com/xmx0632/claude-code-demo

---

## 开发阶段一：需求分析与项目设计

### 1.1 初始需求

**用户原始输入**:
```
做一个简单的图书库存管理系统，前端使用 Vue，后端使用 Spring Boot，数据库使用 SQLite，
数据库版本管理使用 Flyway，先写好需求设计文档。
```

### 1.2 AI 提示词设计

**提示词 1 - 需求分析**:
```
创建一个简单的图书库存管理系统。技术栈：
- 前端: Vue.js
- 后端: Spring Boot
- 数据库: SQLite
- 数据库版本管理: Flyway

功能需求：
1. 图书管理（增删改查）
2. 库存管理（入库、出库、预警）
3. 分类管理
4. 查询统计

请先设计需求文档，包括：
- 功能需求
- 数据模型设计
- API 接口设计
- 项目目录结构
```

### 1.3 AI 输出结果

**生成的文档**:
- `需求设计文档.md` - 包含功能需求、数据模型、API设计、项目结构
- `教学提示词文档.md` - 定义项目技术栈、开发步骤、教学重点

**关键决策**:
- 采用 RESTful API 设计
- 三层架构：Controller → Service → Repository
- 使用 Flyway 管理数据库版本

---

## 开发阶段二：数据库设计与迁移

### 2.1 Flyway 迁移脚本创建

**提示词 2 - 数据库表结构设计**:
```
创建 Flyway 数据库迁移脚本：

1. 表结构：
   - category (分类表)
   - book (图书表)
   - stock_log (库存日志表)

2. 字段要求：
   - 使用 SQLite 语法
   - 主键自增 (INTEGER PRIMARY KEY AUTOINCREMENT)
   - 时间戳自动记录
   - 外键关联

3. 创建索引优化查询

4. 插入测试数据（8个分类，14本图书）
```

### 2.2 AI 输出结果

**生成的 SQL 脚本**:
- `V1__init_schema.sql` - 表结构定义 + 索引
- `V2__insert_seed_data.sql` - 测试数据

**关键代码片段**:
```sql
CREATE TABLE IF NOT EXISTS book (
    id INTEGER PRIMARY KEY AUTOINCREMENT,
    title VARCHAR(200) NOT NULL,
    author VARCHAR(100) NOT NULL,
    isbn VARCHAR(20) UNIQUE,
    category_id INTEGER,
    stock_quantity INTEGER DEFAULT 0,
    min_stock INTEGER DEFAULT 5,
    created_at DATETIME DEFAULT CURRENT_TIMESTAMP,
    updated_at DATETIME DEFAULT CURRENT_TIMESTAMP,
    FOREIGN KEY (category_id) REFERENCES category(id)
);
```

---

## 开发阶段三：后端项目搭建

### 3.1 Maven 依赖配置

**提示词 3 - pom.xml 配置**:
```
创建 Spring Boot 3.2.0 项目的 pom.xml：

依赖要求：
- spring-boot-starter-web
- spring-boot-starter-data-jpa
- sqlite-jdbc (3.43.0.0)
- hibernate-community-dialects
- flyway-core
- lombok
- spring-boot-starter-validation

注意：flyway-sqlite 在国内镜像可能不可用，只使用 flyway-core
```

### 3.2 分层架构实现

**提示词 4 - 代码结构生成**:
```
创建 Spring Boot 分层架构代码：

1. Entity 层 (com.example.library.entity)
   - Book.java
   - Category.java
   - StockLog.java
   - 使用 Jakarta EE 9+ (jakarta.persistence.*)

2. Repository 层 (com.example.library.repository)
   - 继承 JpaRepository
   - 自定义查询方法

3. Service 层 (com.example.library.service)
   - 业务逻辑实现
   - 事务管理 (@Transactional)

4. Controller 层 (com.example.library.controller)
   - RESTful API 接口
   - 统一响应格式 (Result<T>)

5. DTO 层
   - Request: 请求数据传输对象
   - Response: 响应数据传输对象

6. 公共模块
   - Result: 统一响应封装
   - GlobalExceptionHandler: 全局异常处理
   - CorsConfig: 跨域配置
```

### 3.3 AI 输出结果

**生成文件** (共 24 个 Java 文件):
- Entity: 3 个
- Repository: 3 个
- Service: 3 个
- Controller: 3 个
- DTO Request: 3 个
- DTO Response: 3 个
- Config/Common: 3 个
- 启动类: 1 个

**关键代码示例**:

**统一响应封装**:
```java
@Data
public class Result<T> {
    private Integer code;
    private String message;
    private T data;

    public static <T> Result<T> success(T data) {
        return new Result<>(200, "success", data);
    }
}
```

**全局异常处理**:
```java
@RestControllerAdvice
public class GlobalExceptionHandler {
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public Result<Map<String, String>> handleValidationException(
        MethodArgumentNotValidException ex) {
        // 处理参数校验异常
    }
}
```

---

## 开发阶段四：前端项目搭建

### 4.1 Vue 3 项目初始化

**提示词 5 - 前端项目结构**:
```
创建 Vue 3 + Vite 前端项目：

package.json 依赖：
- vue (3.4.x)
- vue-router (4.x)
- pinia (2.x)
- element-plus (2.x)
- axios (1.6.x)
- @element-plus/icons-vue

目录结构：
- src/api/ - API 封装
- src/components/ - 公共组件
- src/views/ - 页面组件
- src/router/ - 路由配置
- src/stores/ - Pinia 状态管理
```

### 4.2 核心页面开发

**提示词 6 - 页面组件**:
```
创建以下页面组件：

1. Dashboard.vue - 仪表盘
   - 统计概览卡片
   - 低库存预警列表

2. BookList.vue - 图书列表
   - 搜索表单
   - 数据表格
   - 分页组件
   - 操作按钮（新增、编辑、删除、入库、出库）

3. BookForm.vue - 图书表单
   - 表单验证
   - 新增/编辑模式

4. CategoryList.vue - 分类管理
   - 分类列表
   - 新增/编辑对话框

5. Layout.vue - 布局组件
   - 侧边栏导航
   - 顶部栏
```

### 4.3 API 封装

**提示词 7 - API 接口封装**:
```
创建 API 封装模块：

1. api/index.js - axios 实例配置
   - 基础 URL 配置
   - 请求拦截器
   - 响应拦截器
   - 统一错误处理

2. api/book.js - 图书相关 API
3. api/category.js - 分类相关 API
4. api/stats.js - 统计相关 API
```

---

## 开发阶段五：问题排查与修复

### 5.1 问题 1 - Flyway SQLite 依赖缺失

**错误信息**:
```
'dependencies.dependency.version' for org.flywaydb:flyway-sqlite:jar is missing
```

**原因**: 阿里云镜像不包含 `flyway-sqlite`

**解决提示词**:
```
flyway-sqlite 依赖在阿里云镜像找不到，请修改 pom.xml：

移除 flyway-sqlite 依赖，只保留 flyway-core 即可支持基本的数据库迁移功能。
```

**解决方案**:
```xml
<!-- 只保留这个 -->
<dependency>
    <groupId>org.flywaydb</groupId>
    <artifactId>flyway-core</artifactId>
</dependency>
```

---

### 5.2 问题 2 - Java 版本不兼容

**错误信息**:
```
Fatal error compiling: 无效的标记: --release
```

**原因**: Maven 使用 Java 8，项目需要 Java 17

**解决提示词**:
```
Maven 编译失败，Java 版本不匹配。

检查当前 Java 版本，如果有 Java 17 可用，创建 ~/.mavenrc 文件指定使用 Java 17：

echo 'JAVA_HOME=/path/to/java17' > ~/.mavenrc

然后重新运行 mvn spring-boot:run
```

**解决方案**:
```bash
echo 'JAVA_HOME=/Library/Java/JavaVirtualMachines/temurin-17.jdk/Contents/Home' > ~/.mavenrc
```

---

### 5.3 问题 3 - Hibernate 类型验证失败

**错误信息**:
```
Schema-validation: wrong column type encountered in column [id] in table [book];
found [integer (Types#INTEGER)], but expecting [bigint (Types#BIGINT)]
```

**原因**: Flyway 创建的 `INTEGER` 类型与 Hibernate 的 `Long` 类型期望不匹配

**解决提示词**:
```
Hibernate schema 验证失败。SQLite 的 INTEGER PRIMARY KEY AUTOINCREMENT
与 JPA Long 类型不兼容。

修改 application.yml，设置 ddl-auto: none，让 Flyway 独占 schema 管理权：

spring:
  jpa:
    hibernate:
      ddl-auto: none
```

**解决方案**:
```yaml
spring:
  jpa:
    hibernate:
      ddl-auto: none  # 使用 Flyway 管理，禁用自动验证
```

---

### 5.4 问题 4 - Spring Boot 版本与依赖不兼容

**错误信息**:
```
Could not find artifact org.hibernate:hibernate-community-dialects:jar:5.6.15.Final
```

**原因**: Spring Boot 2.x 与 Hibernate 6.x 方言包版本不匹配

**解决提示词**:
```
依赖版本冲突。需要将 Spring Boot 升级到 3.x，并将所有 javax.* 包改为 jakarta.*

1. 修改 pom.xml，使用 Spring Boot 3.2.0
2. 修改所有 Entity 和 DTO 的导入语句：
   - javax.persistence.* → jakarta.persistence.*
   - javax.validation.* → jakarta.validation.*
```

---

### 5.5 问题 5 - 数据库目录不存在

**错误信息**:
```
Unable to obtain connection from database:
'/path/to/backend/data/library.db': '/path/to/backend/data' does not exist
```

**解决提示词**:
```
SQLite 需要数据库文件目录存在。请创建目录：

mkdir -p backend/data
```

---

### 5.6 问题 6 - 端口被占用

**错误信息**:
```
Web server failed to start. Port 8080 was already in use.
```

**解决提示词**:
```
端口 8080 被占用。请修改端口为 8088：

1. 修改 application.yml 中的 server.port 为 8088
2. 修改前端 vite.config.js 中的 proxy target 为 http://localhost:8088
```

---

### 5.7 问题 7 - 日期字段解析错误

**错误信息**:
```
Could not extract column [9] from JDBC ResultSet [Error parsing date]
```

**原因**: SQLite 的日期类型与 Hibernate 的 `LocalDate` 解析兼容性问题

**解决方案**: 避免查询 `publishDate` 字段，或将其改为 String 类型

---

## 开发阶段六：功能测试与完善

### 6.1 API 测试

**测试命令**:
```bash
# 测试统计 API
curl http://localhost:8088/api/stats/summary

# 测试分类列表
curl http://localhost:8088/api/categories

# 测试图书列表
curl "http://localhost:8088/api/books?page=1&size=10"
```

### 6.2 前后端联调

**配置要点**:
- 后端端口: 8088
- 前端端口: 5173
- 代理配置: `/api` → `http://localhost:8088`

---

## AI 提示词最佳实践

### 提示词设计原则

1. **明确性**: 清晰描述需求和技术栈
2. **结构化**: 使用列表、表格等结构化格式
3. **上下文**: 提供必要的背景信息
4. **迭代式**: 将大任务分解为小步骤

### 提示词模板

**模板 1 - 功能开发**:
```
请创建 [功能名称]，具体要求：

功能需求：
1. [需求1]
2. [需求2]

技术要求：
- 框架: [框架名称]
- 模式: [MVC/分层架构等]
- 特殊要求: [验证、日志等]

请包含：
- 完整的代码注释
- 错误处理
- 日志记录
```

**模板 2 - 问题排查**:
```
遇到以下错误：

[粘贴错误信息]

环境信息：
- OS: [操作系统]
- Java: [版本]
- Maven: [版本]

请分析原因并提供解决方案。
```

**模板 3 - 代码重构**:
```
请重构以下代码，[具体重构目标]：

[粘贴代码]

重构要求：
- 提取公共方法
- 优化代码结构
- 添加必要的注释
```

---

## 开发流程总结

### 成功的实践

1. **需求先行**: 先完成需求设计文档再编码
2. **分层架构**: 严格遵循 Controller → Service → Repository
3. **版本管理**: 使用 Flyway 管理数据库版本
4. **问题记录**: 及时记录问题和解决方案
5. **迭代开发**: 遇到问题立即修复，持续提交

### 经验教训

1. **依赖管理**: Spring Boot 父依赖管理大部分版本，第三方依赖需注意版本兼容性
2. **Java 版本**: Spring Boot 3.x 必须使用 Java 17+，注意配置 ~/.mavenrc
3. **数据库选择**: SQLite 适合轻量应用，但类型支持有限，生产环境建议使用 MySQL/PostgreSQL
4. **端口管理**: 开发环境注意端口冲突，使用非标准端口避免冲突

### Git 提交规范

```
<type>(<scope>): <subject>

<body>

<footer>

Generated with [Claude Code](https://claude.ai/code)
via [Happy](https://happy.engineering)

Co-Authored-By: Claude <noreply@anthropic.com>
Co-Authored-By: Happy <yesreply@happy.engineering>
```

**type 类型**:
- `feat` - 新功能
- `fix` - 修复 bug
- `docs` - 文档更新
- `style` - 代码格式调整
- `refactor` - 重构代码
- `test` - 测试相关
- `chore` - 构建/工具相关

---

## 快速参考

### 启动命令

```bash
# 后端
cd library-management-system/backend
mvn spring-boot:run

# 前端
cd library-management-system/frontend
npm install
npm run dev
```

### 访问地址

- 后端 API: http://localhost:8088
- 前端页面: http://localhost:5173

### 项目结构

```
library-management-system/
├── backend/                    # Spring Boot 后端
│   ├── src/main/java/
│   │   └── com/example/library/
│   ├── src/main/resources/
│   │   ├── application.yml
│   │   └── db/migration/        # Flyway 迁移脚本
│   └── pom.xml
│
├── frontend/                   # Vue 3 前端
│   ├── src/
│   │   ├── views/              # 页面组件
│   │   ├── components/         # 公共组件
│   │   ├── api/                # API 封装
│   │   ├── router/             # 路由配置
│   │   └── stores/             # 状态管理
│   └── package.json
│
└── docs/                       # 文档
    ├── 需求设计文档.md
    ├── 教学提示词文档.md
    ├── 问题记录文档.md
    └── 启动指南.md
```

---

## 附录：完整提示词列表

### 1. 项目初始化提示词
```
创建一个图书库存管理系统项目，技术栈：
- 前端: Vue 3 + Element Plus + Vite
- 后端: Spring Boot 3.2.0 + JPA + Flyway + SQLite

项目功能：
1. 图书管理（增删改查）
2. 库存管理（入库、出库、预警）
3. 分类管理
4. 统计概览

请先创建项目目录结构和需求设计文档。
```

### 2. 数据库设计提示词
```
创建 Flyway 数据库迁移脚本，包含：

表结构：
1. category - 分类表
   - id (主键)
   - name (名称)
   - description (描述)
   - created_at, updated_at

2. book - 图书表
   - id (主键)
   - title (书名)
   - author (作者)
   - isbn (ISBN)
   - category_id (分类ID，外键)
   - price (价格)
   - stock_quantity (库存数量)
   - min_stock (最小库存)
   - publish_date (出版日期)
   - created_at, updated_at

3. stock_log - 库存日志表
   - id (主键)
   - book_id (图书ID，外键)
   - type (类型: IN/OUT)
   - quantity (数量)
   - before_quantity, after_quantity (前后数量)
   - remark (备注)
   - created_at

插入测试数据：8个分类，14本图书
```

### 3. 后端代码生成提示词
```
生成 Spring Boot 3.2.0 分层架构代码：

1. Entity 层 - 实体类
   - Book.java
   - Category.java
   - StockLog.java
   - 使用 jakarta.persistence.*

2. Repository 层 - 数据访问
   - BookRepository.java
   - CategoryRepository.java
   - StockLogRepository.java

3. Service 层 - 业务逻辑
   - BookService.java
   - CategoryService.java

4. Controller 层 - API 接口
   - BookController.java
   - CategoryController.java
   - StatsController.java

5. 公共模块
   - Result.java - 统一响应封装
   - GlobalExceptionHandler.java - 全局异常处理
   - CorsConfig.java - 跨域配置

注意：
- 使用 @Transactional 管理事务
- 使用 @Valid 校验参数
- 异常处理要完善
- 代码要有注释
```

### 4. 前端代码生成提示词
```
创建 Vue 3 前端项目：

页面组件：
1. Dashboard.vue - 仪表盘
   - 统计卡片（图书总数、分类数、低库存预警）
   - 低库存图书列表

2. BookList.vue - 图书列表
   - 搜索表单
   - 数据表格
   - 分页
   - 操作按钮（新增、编辑、删除、入库、出库）

3. BookForm.vue - 图书表单
   - 表单验证
   - 新增/编辑模式

4. CategoryList.vue - 分类管理
   - 分类列表
   - 新增/编辑对话框

5. Layout.vue - 布局组件
   - 侧边栏导航
   - 顶部栏

API 封装：
- book.js
- category.js
- stats.js
```

### 5. 问题排查提示词
```
项目启动失败，错误信息：

[粘贴错误信息]

当前环境：
- Spring Boot 3.2.0
- Java 17
- Maven 3.6.3

请分析问题原因并提供解决方案。
```

---

*文档版本: 1.0*
*最后更新: 2026-02-25*
