# Ruoyi 示例项目

这是一个基于 Ruoyi 框架的简化版管理系统示例，用于演示如何在实际项目中使用 Claude Code + Skills 提升开发效率。

## 项目特点

- **简化架构**: 只包含核心功能，易于理解
- **MyBatis-Plus**: 使用增强版 MyBatis，简化 CRUD 操作
- **RESTful API**: 标准的 REST 接口设计
- **Swagger**: 集成 Knife4j 接口文档
- **最佳实践**: 展示企业级代码规范

## 技术栈

- Spring Boot 3.2.0
- MyBatis-Plus 3.5.5
- MySQL 8.0
- Druid 1.2.20
- Lombok
- Hutool 5.8.24
- Knife4j 4.3.0

## 快速开始

### 1. 环境要求

- JDK 17+
- Maven 3.6+
- MySQL 8.0+

### 2. 数据库初始化

```bash
# 创建数据库
mysql -u root -p -e "CREATE DATABASE ruoyi_example DEFAULT CHARSET utf8mb4"

# 导入 SQL
mysql -u root -p ruoyi_example < docs/database-schema.sql
```

### 3. 修改配置

编辑 `src/main/resources/application.yml`，修改数据库连接信息：

```yaml
spring:
  datasource:
    url: jdbc:mysql://localhost:3306/ruoyi_example
    username: root
    password: your_password
```

### 4. 运行项目

```bash
# 使用 Maven
mvn spring-boot:run

# 或使用 IDE
# 运行 RuoyiExampleApplication
```

### 5. 访问应用

- 应用地址: http://localhost:8080
- 接口文档: http://localhost:8080/doc.html

## 项目结构

```
ruoyi-example/
├── pom.xml                          # Maven 配置
├── src/main/java/com/example/ruoyi/
│   ├── controller/                  # 控制器层
│   │   └── UserController.java
│   ├── service/                     # 服务层
│   │   ├── IUserService.java
│   │   └── impl/
│   │       └── UserServiceImpl.java
│   ├── mapper/                      # 数据访问层
│   │   └── UserMapper.java
│   └── domain/                      # 实体类
│       └── User.java
├── src/main/resources/
│   ├── application.yml              # 应用配置
│   └── mapper/
│       └── UserMapper.xml           # MyBatis XML
└── docs/
    ├── database-schema.sql          # 数据库结构
    └── api.md                       # API 文档
```

## Skills 演示场景

本项目配套 5 个实用 Skills，展示 Claude Code 在实际开发中的应用：

### 1. /ruoyi-crud - 快速生成 CRUD 代码

```bash
# 为新表生成完整的 CRUD 代码
/ruoyi-crud sys_notice
```

自动生成：Entity、Mapper、Service、Controller 四层代码

### 2. /code-review - 代码审查

```bash
# 审查代码质量
/code-review src/main/java/com/example/ruoyi/controller/UserController.java
```

检查：代码规范、潜在 Bug、性能问题、安全隐患

### 3. /api-doc - 生成 API 文档

```bash
# 自动生成接口文档
/api-doc --output=docs/api.md
```

解析 Controller，生成 Markdown 格式的 API 文档

### 4. /test-gen - 生成单元测试

```bash
# 为 Service 层生成测试
/test-gen UserService --package=com.example.ruoyi.service
```

生成 JUnit + Mockito 测试用例

### 5. /sql-optimizer - SQL 优化

```bash
# 优化 SQL 查询
/sql-optimizer UserMapper.xml
```

分析：索引问题、N+1 查询、全表扫描

## 核心代码说明

### 1. 实体类 (User.java)

使用 MyBatis-Plus 注解：
- `@TableName`: 指定表名
- `@TableId`: 主键策略
- `@TableField`: 字段映射
- `@TableLogic`: 逻辑删除

### 2. Mapper 接口 (UserMapper.java)

继承 `BaseMapper` 获得基础 CRUD 方法：
```java
public interface UserMapper extends BaseMapper<User> {
    // 自定义查询方法
    @Select("SELECT * FROM sys_user WHERE dept_id = #{deptId}")
    List<User> selectUsersByDeptId(@Param("deptId") Long deptId);
}
```

### 3. Service 层 (UserService.java)

继承 `IService` 获得增强服务方法：
```java
public interface IUserService extends IService<User> {
    // 自定义业务方法
    User selectUserByName(String userName);
}
```

### 4. Controller (UserController.java)

RESTful 风格接口：
```java
@GetMapping("/list")        # 查询列表
@GetMapping("/{userId}")    # 查询详情
@PostMapping                # 新增
@PutMapping                 # 修改
@DeleteMapping("/{ids}")    # 删除
```

## 测试数据

系统预置了测试数据：

**用户账号**:
- admin / admin123 (超级管理员)
- ry / admin123 (普通用户)

**部门结构**:
- XX科技
  - 深圳总公司
    - 研发部门
    - 市场部门
  - 长沙分公司

## 常见问题

### 1. 数据库连接失败

检查 `application.yml` 中的数据库配置是否正确。

### 2. 端口被占用

修改 `application.yml` 中的 `server.port`。

### 3. Swagger 无法访问

确保已添加 Knife4j 依赖，访问 http://localhost:8080/doc.html

## 扩展学习

- [MyBatis-Plus 官方文档](https://baomidou.com/)
- [Spring Boot 官方文档](https://spring.io/projects/spring-boot)
- [Ruoyi 框架文档](http://doc.ruoyi.vip/)

## 许可证

本项目仅用于学习和演示目的。
