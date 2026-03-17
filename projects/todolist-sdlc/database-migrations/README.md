# TodoList SDLC - 数据库迁移管理

本目录包含 TodoList SDLC 项目的数据库迁移脚本和管理工具。这是一个独立的模块，与后端业务代码解耦。

## 目录结构

```
database-migrations/
├── migrations/           # Flyway 迁移脚本
│   ├── V1__Init_schema.sql
│   ├── V2__create_tag_table.sql
│   ├── V3__create_todo_tag_table.sql
│   └── V4__init_data.sql
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

需要 Java 17 或更高版本：

```bash
# 检查 Java 版本
java -version

# 如果版本低于 17，设置 JAVA_HOME
export JAVA_HOME=/Library/Java/JavaVirtualMachines/jdk-17.jdk/Contents/Home
```

### Maven

使用 Maven 的 Flyway 插件进行迁移管理：

```bash
# macOS
brew install maven

# Linux
sudo apt install maven
```

### 数据库准备

确保 MySQL 服务运行并且数据库已创建：

```sql
CREATE DATABASE IF NOT EXISTS todolist CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci;
```

## 使用方法

### 1. 查看迁移状态

```bash
cd database-migrations
./scripts/info.sh
```

或直接使用 Maven：

```bash
cd database-migrations
mvn flyway:info
```

### 2. 执行迁移

```bash
cd database-migrations
./scripts/migrate.sh
```

或直接使用 Maven：

```bash
cd database-migrations
mvn flyway:migrate
```

### 3. 验证迁移脚本

```bash
cd database-migrations
./scripts/validate.sh
```

或直接使用 Maven：

```bash
cd database-migrations
mvn flyway:validate
```

### 4. 创建新的迁移脚本

在 `migrations/` 目录下创建新文件，遵循 Flyway 命名约定：

```
V5__add_user_preferences.sql
V6__create_notification_table.sql
```

命名规则：
- 以 `V` 开头（版本迁移）
- 版本号：1, 2, 3...（不要使用 1.0.0 格式）
- 双下划线 `__` 分隔符
- 描述性名称（使用下划线代替空格）

## 与后端集成

后端应用启动时会自动执行迁移。配置见 `backend/src/main/resources/application.yml`：

```yaml
spring:
  flyway:
    enabled: true
    locations: filesystem:database-migrations/migrations
    baseline-on-migrate: true
```

本模块的 `pom.xml` 可独立使用，与后端应用的 Flyway 配置互不影响。

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

# 清空数据库（危险操作！）
mvn flyway:clean

# 建立基线
mvn flyway:baseline
```

## 配置说明

数据库配置在 `pom.xml` 中：

```xml
<configuration>
    <url>jdbc:mysql://localhost:3306/todolist</url>
    <user>root</user>
    <password>root</password>
    <locations>filesystem:migrations</locations>
    <baselineOnMigrate>true</baselineOnMigrate>
    <encoding>UTF-8</encoding>
</configuration>
```

## 注意事项

1. **不要修改已执行的迁移脚本**：这会导致校验失败
2. **版本号必须递增**：新增脚本使用下一个版本号
3. **描述要清晰**：使用描述性的文件名
4. **提交前测试**：在开发环境充分测试后再部署

## 回滚策略

Flyway 不支持自动回滚。如需回滚：

1. 手动执行回滚 SQL
2. 删除 `flyway_schema_history` 中的对应记录（谨慎操作）

## 相关文档

- [Flyway 官方文档](https://flywaydb.org/documentation/)
- [Flyway Maven 插件文档](https://flywaydb.org/documentation/maven/)
- [Spring Boot Flyway 集成](https://docs.spring.io/spring-boot/docs/current/reference/html/howto.html#howto.data-initialization.migration-tool.flyway)
