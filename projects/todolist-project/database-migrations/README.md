# TodoList Project - 数据库迁移管理

本目录包含 TodoList Project 项目的数据库迁移脚本和管理工具。这是一个独立的模块，与后端业务代码解耦，支持 MySQL 和 H2 数据库。

## 目录结构

```
database-migrations/
├── migrations/           # Flyway 迁移脚本（兼容 MySQL 和 H2）
│   ├── V1__init_schema.sql
│   ├── V2__create_user_table.sql
│   ├── V3__create_todo_table.sql
│   ├── V4__create_category_table.sql
│   └── V5__create_todo_category_table.sql
├── scripts/             # 管理脚本
│   ├── migrate.sh       # 执行迁移
│   ├── info.sh          # 查看状态
│   ├── validate.sh      # 验证脚本
│   ├── clean.sh         # 清理并重建
│   └── repair.sh        # 修复历史表
├── h2/                  # H2 专用目录
│   ├── data/            # 数据库文件（git 忽略）
│   ├── console.sh       # 独立 H2 Console
│   ├── migrate.sh       # H2 迁移
│   ├── info.sh          # H2 状态
│   └── clean.sh         # H2 清理
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

## 迁移策略

- **开发环境 (dev)**: 应用启动时自动执行 Flyway 迁移
- **其他环境 (prod/test)**: 禁用自动迁移，需人工使用脚本操作

这样可以避免生产环境的意外数据库操作。

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

## 数据库支持

本项目支持两种数据库：

### MySQL（生产环境）

- 数据库会在首次迁移时自动创建（V1 脚本包含 `CREATE DATABASE IF NOT EXISTS`）
- 默认 Profile，无需额外配置
- 连接信息：localhost:3306/todolist

### H2（开发环境）

- 文件数据库，无需安装
- 使用 `-Ph2` 激活
- 数据库文件持久化到 `h2/data/` 目录

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
./scripts/clean.sh      # 清理并重建
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
cd h2

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

### 独立 H2 Console

H2 Console 可以独立运行，无需启动 Spring Boot 应用：

```bash
cd h2
./console.sh
```

访问 http://localhost:8082，连接信息：
- **JDBC URL**: `jdbc:h2:tcp://localhost:9092/todolist`
- **User Name**: `sa`
- **Password**: (留空)

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
- `V4__create_category_table.sql` - 创建分类表

### 多数据库兼容性

迁移脚本需要兼容 MySQL 和 H2：

```sql
-- V1__init_schema.sql
-- 描述: TodoList 数据库初始化（兼容 MySQL 和 H2）

-- MySQL 特定：创建数据库（H2 会忽略）
/*! CREATE DATABASE IF NOT EXISTS todolist CHARACTER SET utf8mb4 */;
/*! USE todolist */;

-- 通用：创建表（兼容两种数据库）
CREATE TABLE IF NOT EXISTS t_user (
    id BIGINT PRIMARY KEY AUTO_INCREMENT,
    email VARCHAR(100) NOT NULL UNIQUE,
    ...
);
```

**关键点**：
- 使用 MySQL 条件注释 `/*! ... */` 包含 MySQL 特定语法
- H2 会将其视为普通注释并跳过
- H2 运行在 MySQL 模式（MODE=MySQL）兼容大部分语法

## 与后端集成

### 迁移策略

- **dev 模式**: 应用启动时自动执行 Flyway 迁移
- **prod 模式**: 禁用自动迁移，需人工操作

### dev 模式配置（application-dev.yml）

```yaml
spring:
  datasource:
    url: jdbc:h2:file:../database-migrations/h2/data/todolist;MODE=MySQL
  flyway:
    enabled: true  # 启用自动迁移
    locations: filesystem:../database-migrations/migrations
```

### 生产环境配置（application.yml）

```yaml
spring:
  datasource:
    url: jdbc:mysql://localhost:3306/todolist
  flyway:
    enabled: false  # 禁用自动迁移，需人工操作
```

### 生产环境迁移操作

生产环境的数据库迁移需要使用 `database-migrations` 目录中的脚本：

```bash
cd database-migrations

# 查看状态
./scripts/info.sh

# 执行迁移
./scripts/migrate.sh

# 验证
./scripts/validate.sh
```

这样可以确保生产环境的数据库变更由 DBA 完全控制。

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
    <url>jdbc:mysql://localhost:3306</url>
    <user>root</user>
    <password>root123456</password>
    <schemas>todolist</schemas>
    <locations>filesystem:migrations</locations>
</configuration>
```

### H2 配置

```xml
<configuration>
    <url>jdbc:h2:file:./h2/data/todolist;MODE=MySQL</url>
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
