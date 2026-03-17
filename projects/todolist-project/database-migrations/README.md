# TodoList Project - 数据库迁移管理

本目录包含 TodoList Project 项目的数据库迁移脚本和管理工具。这是一个独立的模块，与后端业务代码解耦，专注于 MySQL 数据库。

## 目录结构

```
database-migrations/
├── migrations/           # Flyway 迁移脚本
│   ├── V1__init_schema.sql
│   ├── V2__create_user_table.sql
│   ├── V3__create_todo_table.sql
│   ├── V4__create_category_table.sql
│   └── V5__create_todo_category_table.sql
├── scripts/             # 管理脚本
│   ├── migrate.sh       # 执行迁移
│   ├── info.sh          # 查看状态
│   └── validate.sh      # 验证脚本
├── pom.xml              # 独立的 Maven 配置
└── README.md            # 本文档
```

## 设计理念

将数据库迁移脚本独立管理有以下优势：

1. **解耦**：数据库变更与业务代码分离
2. **独立管理**：DBA 可以独立管理迁移脚本，无需接触业务代码
3. **版本控制**：独立的 git 历史记录
4. **可移植性**：可以在不同环境独立部署

## 前置要求

### Java 版本

需要 Java 8 或更高版本：

```bash
# 检查 Java 版本
java -version

# 如果版本低于 8，设置 JAVA_HOME
export JAVA_HOME=/Library/Java/JavaVirtualMachines/jdk1.8.jdk/Contents/Home
```

### Maven

使用 Maven 的 Flyway 插件进行迁移管理：

```bash
# macOS
brew install maven

# Linux
sudo apt install maven
```

### MySQL

确保 MySQL 服务正在运行：

```bash
# macOS
brew services start mysql

# Linux
sudo systemctl start mysql
```

## 使用方法

### Maven 命令

```bash
cd database-migrations

# 查看状态
mvn flyway:info

# 执行迁移
mvn flyway:migrate

# 验证脚本
mvn flyway:validate
```

### 辅助脚本

```bash
cd database-migrations

# 查看状态
./scripts/info.sh

# 执行迁移
./scripts/migrate.sh

# 验证脚本
./scripts/validate.sh
```

## 迁移脚本命名规范

### 版本格式

```
V<版本号>__<描述>.sql
```

**版本号规则**：
- 使用简单递增数字：1, 2, 3...
- 不要使用 1.0.0 格式（兼容性问题）
- 必须递增，不能跳过

**示例**：
- `V1__init_schema.sql` - 初始化数据库
- `V2__create_user_table.sql` - 创建用户表
- `V3__create_todo_table.sql` - 创建待办表

## 数据库配置

### MySQL 连接信息

```xml
<url>jdbc:mysql://localhost:3306</url>
<user>root</user>
<password>root123456</password>
<schemas>todolist</schemas>
```

### 修改配置

编辑 `pom.xml` 中的以下配置：

```xml
<configuration>
    <url>jdbc:mysql://your-host:3306</url>
    <user>your-username</user>
    <password>your-password</password>
    <schemas>your-schema</schemas>
</configuration>
```

## 与后端集成

### application.yml 配置

```yaml
spring:
  datasource:
    url: jdbc:mysql://localhost:3306/todolist
    username: root
    password: root123456

  flyway:
    enabled: true
    baseline-on-migrate: true
    locations: classpath:db/migration,filesystem:../database-migrations/migrations
    encoding: UTF-8
    validate-on-migrate: true
```

## 常用 Maven 命令

```bash
cd database-migrations

# 查看迁移状态
mvn flyway:info

# 执行迁移
mvn flyway:migrate

# 验证脚本
mvn flyway:validate

# 查看迁移历史
mvn flyway:history

# 建立基线
mvn flyway:baseline
```

## 注意事项

1. **不要修改已执行的迁移脚本**：这会导致校验失败
2. **版本号必须递增**：新增脚本使用下一个版本号
3. **描述要清晰**：使用描述性的文件名
4. **提交前测试**：在开发环境充分测试后再部署
5. **备份数据**：执行迁移前建议备份数据库

## 回滚策略

Flyway 不支持自动回滚。如需回滚：

1. 手动执行回滚 SQL
2. 删除 `flyway_schema_history` 中的对应记录（谨慎操作）

## 相关文档

- [Flyway 官方文档](https://flywaydb.org/documentation/)
- [Flyway Maven 插件文档](https://flywaydb.org/documentation/maven/)
- [Spring Boot Flyway 集成](https://docs.spring.io/spring-boot/docs/current/reference/html/howto.html#howto.data-initialization.migration-tool.flyway)
