# Ruoyi Example - 数据库迁移管理

本目录包含 Ruoyi Example 项目的数据库迁移脚本和管理工具。这是一个独立的模块，与后端业务代码解耦，支持 MySQL 和 H2 数据库。

## 目录结构

```
database-migrations/
├── migrations/           # Flyway 迁移脚本（兼容 MySQL 和 H2）
│   ├── V1__init_schema.sql
│   ├── V2__insert_init_data.sql
│   ├── V3__add_user_avatar_field.sql
│   ├── V4__add_remark_field_to_tables.sql
│   ├── V5__create_oper_log_table.sql
│   ├── V6__add_indexes_for_performance.sql
│   └── V7__create_dictionary_tables.sql
├── scripts/             # 管理脚本（支持 MySQL/H2）
│   ├── migrate.sh       # 执行迁移
│   ├── info.sh          # 查看状态
│   ├── validate.sh      # 验证脚本
│   ├── clean.sh         # 清理并重建
│   ├── repair.sh        # 修复历史表
│   ├── init-db.sh       # 初始化数据库（保留）
│   └── rollback.sh      # 回滚脚本生成（保留）
├── h2/                  # H2 专用目录
│   ├── data/            # 数据库文件（git 忽略）
│   ├── console.sh       # 独立 H2 Console
│   ├── migrate.sh       # H2 迁移
│   ├── info.sh          # H2 状态
│   └── clean.sh         # H2 清理
├── docs/                # 文档
│   ├── conventions.md   # 命名规范
│   └── changelog.md     # 变更日志
├── pom.xml              # 独立的 Maven 配置（支持多数据库 Profile）
├── flyway.conf          # Flyway 配置模板（保留）
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

- **开发环境 (dev)**: 可选择使用 H2 数据库进行快速开发测试
- **其他环境 (prod/test)**: 使用 MySQL 数据库

## 前置要求

### Java 版本

需要 Java 8 或更高版本：

```bash
# 检查 Java 版本
java -version
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

- 数据库需要在首次迁移前创建
- 默认 Profile，无需额外配置
- 连接信息：127.0.0.1:13306/ruoyi_example

### H2（开发环境）

- 文件数据库，无需安装
- 使用 `-Ph2` 激活
- 数据库文件持久化到 `h2/` 目录

## 使用方法

### Maven 命令（推荐）

**MySQL（生产环境）**
```bash
cd database-migrations

# 查看状态
mvn flyway:info

# 执行迁移
mvn flyway:migrate

# 验证
mvn flyway:validate
```

**H2（开发环境）**
```bash
cd database-migrations

# 使用 H2 Profile
mvn flyway:info -Ph2
mvn flyway:migrate -Ph2
mvn flyway:validate -Ph2
```

### 辅助脚本

**通用脚本（scripts/）**
```bash
cd database-migrations

# MySQL（默认）
./scripts/info.sh
./scripts/migrate.sh
./scripts/validate.sh
./scripts/clean.sh      # 清理并重建
./scripts/repair.sh     # 修复历史表

# H2（开发环境）
DB=h2 ./scripts/info.sh
DB=h2 ./scripts/migrate.sh
DB=h2 ./scripts/validate.sh
DB=h2 ./scripts/clean.sh
DB=h2 ./scripts/repair.sh
```

**H2 专用脚本（h2/）**
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

### 独立 H2 Console

H2 Console 可以独立运行，无需启动 Spring Boot 应用：

```bash
cd h2
./console.sh
```

访问 http://localhost:8082，连接信息：
- **JDBC URL**: `jdbc:h2:tcp://localhost:9092/ruoyi_example`
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
- `V2__insert_init_data.sql` - 插入初始数据
- `V3__add_user_avatar_field.sql` - 添加用户头像字段

### 多数据库兼容性

迁移脚本需要兼容 MySQL 和 H2：

```sql
-- V1__init_schema.sql
-- 描述: 数据库初始化（兼容 MySQL 和 H2）

-- MySQL 特定：创建数据库（H2 会忽略）
/*! CREATE DATABASE IF NOT EXISTS ruoyi_example CHARACTER SET utf8mb4 */;
/*! USE ruoyi_example */;

-- 通用：创建表（兼容两种数据库）
CREATE TABLE IF NOT EXISTS sys_user (
    id BIGINT PRIMARY KEY AUTO_INCREMENT,
    user_name VARCHAR(30) NOT NULL,
    ...
);
```

**关键点**：
- 使用 MySQL 条件注释 `/*! ... */` 包含 MySQL 特定语法
- H2 会将其视为普通注释并跳过
- H2 运行在 MySQL 模式（MODE=MySQL）兼容大部分语法

## Maven 配置

### standalone pom.xml

在 `database-migrations/` 目录创建独立的 pom.xml：

```xml
<properties>
    <flyway.version>7.15.0</flyway.version>
    <h2.version>1.4.200</h2.version>
</properties>

<profiles>
    <!-- MySQL Profile -->
    <profile>
        <id>mysql</id>
        <activation><activeByDefault>true</activeByDefault></activation>
        <!-- MySQL 配置 -->
    </profile>

    <!-- H2 Profile -->
    <profile>
        <id>h2</id>
        <!-- H2 配置 -->
    </profile>
</profiles>
```

## 与后端集成

### application.yml 配置

```yaml
spring:
  datasource:
    url: jdbc:mysql://localhost:3306/ruoyi_example
    username: root
    password: root123456

  flyway:
    enabled: true
    baseline-on-migrate: true
    locations: classpath:db/migration,filesystem:../database-migrations/migrations
```

## 编写迁移脚本

### 增量变更原则

**✅ 正确：增量变更**
```sql
-- V3__add_user_status.sql
ALTER TABLE sys_user ADD COLUMN status VARCHAR(10) DEFAULT '0';
```

**❌ 错误：完整重建**
```sql
-- 不要在已有表上使用 DROP TABLE + CREATE TABLE
DROP TABLE IF EXISTS sys_user;
CREATE TABLE sys_user (...);
```

### 迁移脚本模板

```sql
-- ========================================
-- V<版本>__<描述>
-- ========================================
-- 作者: <作者名>
-- 日期: <创建日期>
-- 描述: <详细描述此迁移的内容>
-- ========================================

-- MySQL 特定：选择数据库（H2 会忽略）
/*! USE ruoyi_example */;

-- 你的 SQL 语句

-- 示例：添加字段
ALTER TABLE table_name
ADD COLUMN column_name VARCHAR(255) DEFAULT '';

-- 示例：创建索引
CREATE INDEX idx_column ON table_name(column_name);
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

## 注意事项

1. **不要修改已执行的迁移脚本**：这会导致校验失败
2. **版本号必须递增**：新增脚本使用下一个版本号
3. **描述要清晰**：使用描述性的文件名
4. **提交前测试**：在开发环境充分测试后再部署
5. **H2 兼容性**：迁移脚本已针对 H2 的 MySQL 模式进行优化

## 回滚策略

Flyway 不支持自动回滚。如需回滚：

1. 记录每次迁移的回滚 SQL
2. 在测试环境验证回滚脚本
3. 生产环境回滚前先备份数据库
4. 按版本倒序执行回滚

## 相关文档

- [Flyway 官方文档](https://flywaydb.org/documentation/)
- [Flyway Maven 插件文档](https://flywaydb.org/documentation/maven/)
- [Spring Boot Flyway 集成](https://docs.spring.io/spring-boot/docs/current/reference/html/howto.html#howto.data-initialization.migration-tool.flyway)
- [H2 数据库 MySQL 兼容模式](https://www.h2database.com/html/features.html#compatibility)
