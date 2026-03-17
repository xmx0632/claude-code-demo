# TodoList SDLC - 数据库迁移管理

本目录包含 TodoList SDLC 项目的数据库迁移脚本和管理工具。这是一个独立的模块，与后端业务代码解耦，支持 MySQL 和 H2 数据库。

## 目录结构

```
database-migrations/
├── migrations/           # Flyway 迁移脚本（兼容 MySQL 和 H2）
│   ├── V1__Init_schema.sql
│   ├── V2__create_tag_table.sql
│   ├── V3__create_todo_tag_table.sql
│   └── V4__init_data.sql
├── scripts/             # 管理脚本
│   ├── migrate.sh       # 执行迁移
│   ├── info.sh          # 查看状态
│   └── validate.sh      # 验证脚本
├── pom.xml              # 独立的 Maven 配置（支持多数据库 Profile）
└── README.md            # 本文档
```

## 设计理念

将数据库迁移脚本独立管理有以下优势：

1. **解耦**：数据库变更与业务代码分离
2. **独立管理**：DBA 可以独立管理迁移脚本，无需接触业务代码
3. **版本控制**：独立的 git 历史记录
4. **可移植性**：可以在不同环境独立部署
5. **多数据库支持**：支持 MySQL（生产）和 H2（开发）

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

## 数据库支持

本项目支持两种数据库：

### MySQL（生产环境）

- 数据库会在首次迁移时自动创建（V1 脚本包含 `CREATE DATABASE IF NOT EXISTS`）
- 默认 Profile，无需额外配置

### H2（开发环境）

- 内存数据库，无需安装
- 使用 `-Ph2` 激活

## 使用方法

### 生产环境（MySQL）

```bash
cd database-migrations

# 查看状态
mvn flyway:info

# 执行迁移
mvn flyway:migrate

# 验证脚本
mvn flyway:validate
```

### 开发环境（H2）

**方式一：使用 Maven（推荐）**

```bash
cd database-migrations

# 使用 H2 Profile
mvn flyway:info -Ph2
mvn flyway:migrate -Ph2
mvn flyway:validate -Ph2
```

**方式二：使用辅助脚本**

```bash
cd database-migrations

# MySQL（默认）
./scripts/info.sh
./scripts/migrate.sh
./scripts/validate.sh
./scripts/clean.sh      # 清理并重建数据库
./scripts/repair.sh     # 修复 Schema History

# H2（开发环境）
DB=h2 ./scripts/info.sh
DB=h2 ./scripts/migrate.sh
DB=h2 ./scripts/validate.sh
DB=h2 ./scripts/clean.sh
DB=h2 ./scripts/repair.sh
```

**方式三：使用 H2 专用脚本**

```bash
cd database-migrations/h2

# 迁移数据库
./migrate.sh

# 查看状态
./info.sh

# 清空数据库
./clean.sh

# 启动 H2 Console
./console.sh
```

H2 数据库文件持久化到 `h2/` 目录，方便测试和调试。

### 使用脚本（默认 MySQL）

```bash
cd database-migrations

# 查看状态
./scripts/info.sh

# 执行迁移
./scripts/migrate.sh

# 验证脚本
./scripts/validate.sh
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

后端应用启动时会自动执行迁移。

### 生产环境（application.yml）

```yaml
spring:
  datasource:
    url: jdbc:mysql://localhost:3306/todolist
  flyway:
    enabled: true
    locations: filesystem:database-migrations/migrations
    baseline-on-migrate: true
```

### 开发环境（application-dev.yml）

```yaml
spring:
  datasource:
    url: jdbc:h2:mem:todolist;DB_CLOSE_DELAY=-1;MODE=MySQL
  flyway:
    enabled: true
    locations: filesystem:../database-migrations/migrations
    baseline-on-migrate: true
```

## Maven Profile 配置

### MySQL Profile（默认）

```xml
<profile>
    <id>mysql</id>
    <activation>
        <activeByDefault>true</activeByDefault>
    </activation>
    <!-- MySQL 配置 -->
</profile>
```

### H2 Profile

```xml
<profile>
    <id>h2</id>
    <!-- H2 配置 -->
</profile>
```

## 常用 Maven 命令

```bash
cd database-migrations

# MySQL（默认）
mvn flyway:info
mvn flyway:migrate
mvn flyway:validate

# H2（使用 Profile）
mvn flyway:info -Ph2
mvn flyway:migrate -Ph2
mvn flyway:validate -Ph2

# 其他命令
mvn flyway:history          # 查看迁移历史
mvn flyway:clean            # 清空数据库（危险！）
mvn flyway:baseline         # 建立基线
```

## 配置说明

### MySQL 配置

```xml
<configuration>
    <url>jdbc:mysql://127.0.0.1:13306</url>
    <user>root</user>
    <password>root@P@SSw0Rd</password>
    <schemas>todolist</schemas>
    <locations>filesystem:migrations</locations>
</configuration>
```

### H2 配置

```xml
<configuration>
    <url>jdbc:h2:mem:todolist;DB_CLOSE_DELAY=-1;MODE=MySQL</url>
    <user>sa</user>
    <password></password>
    <locations>filesystem:migrations</locations>
</configuration>
```

## 注意事项

1. **不要修改已执行的迁移脚本**：这会导致校验失败
2. **版本号必须递增**：新增脚本使用下一个版本号
3. **描述要清晰**：使用描述性的文件名
4. **提交前测试**：在开发环境充分测试后再部署
5. **H2 兼容性**：迁移脚本已针对 H2 的 MySQL 模式进行优化

## 回滚策略

Flyway 不支持自动回滚。如需回滚：

1. 手动执行回滚 SQL
2. 删除 `flyway_schema_history` 中的对应记录（谨慎操作）

## 相关文档

- [Flyway 官方文档](https://flywaydb.org/documentation/)
- [Flyway Maven 插件文档](https://flywaydb.org/documentation/maven/)
- [Spring Boot Flyway 集成](https://docs.spring.io/spring-boot/docs/current/reference/html/howto.html#howto.data-initialization.migration-tool.flyway)
- [H2 数据库 MySQL 兼容模式](https://www.h2database.com/html/features.html#compatibility)
