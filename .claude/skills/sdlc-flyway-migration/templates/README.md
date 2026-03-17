# Flyway 数据库迁移模板

本目录包含 Flyway 数据库迁移管理的通用模板文件，可快速复用到新项目。

## 模板文件说明

### 配置文件

| 文件 | 说明 |
|------|------|
| `pom.xml.template` | Maven 配置模板，支持 MySQL/H2 双数据库 |
| `.gitignore.template` | Git 忽略规则模板 |

### 通用脚本（scripts/）

| 脚本 | 功能 |
|------|------|
| `migrate.sh.template` | 执行数据库迁移 |
| `info.sh.template` | 查看迁移状态 |
| `validate.sh.template` | 验证迁移脚本 |
| `clean.sh.template` | 清理并重建数据库 |
| `repair.sh.template` | 修复 Schema History |

### H2 专用脚本（h2/）

| 脚本 | 功能 |
|------|------|
| `console.sh.template` | 启动独立 H2 Console |
| `migrate.sh.template` | H2 数据库迁移 |
| `info.sh.template` | H2 数据库状态 |
| `clean.sh.template` | 清理 H2 数据库 |

## 快速开始

### 1. 创建项目结构

```bash
# 在你的项目根目录下创建 database-migrations 目录
mkdir -p database-migrations/{migrations,scripts,h2/data}
```

### 2. 复制模板文件

```bash
# 复制配置文件
cp pom.xml.template database-migrations/pom.xml
cp .gitignore.template database-migrations/.gitignore

# 复制脚本
cp scripts/*.template database-migrations/scripts/
cd database-migrations/scripts
for f in *.template; do mv "$f" "${f%.template}"; done
chmod +x *.sh

# 复制 H2 脚本
cp h2/*.template database-migrations/h2/
cd ../h2
for f in *.template; do mv "$f" "${f%.template}"; done
chmod +x *.sh
```

### 3. 配置 pom.xml

编辑 `database-migrations/pom.xml`，替换以下占位符：

| 占位符 | 说明 | 示例 |
|--------|------|------|
| `{{GROUP_ID}}` | Maven Group ID | `com.example` |
| `{{PROJECT_NAME}}` | 项目名称 | `MyProject` |
| `{{MYSQL_JDBC_URL}}` | MySQL JDBC URL | `jdbc:mysql://localhost:3306/mydb?useUnicode=true&characterEncoding=UTF-8` |
| `{{MYSQL_USER}}` | MySQL 用户名 | `root` |
| `{{MYSQL_PASSWORD}}` | MySQL 密码 | `password` |
| `{{MYSQL_SCHEMA}}` | MySQL Schema 名 | `mydb` |
| `{{DB_NAME}}` | 数据库名称 | `mydb` |
| `{{H2_VERSION}}` | H2 版本 | `1.4.200` |

### 4. 配置 H2 脚本（如使用）

编辑 `database-migrations/h2/console.sh`，替换 `{{DB_NAME}}` 和 `{{H2_VERSION}}`。

## 使用方法

### Maven 命令

```bash
cd database-migrations

# MySQL（生产环境）
mvn flyway:info
mvn flyway:migrate
mvn flyway:validate

# H2（开发环境）
mvn flyway:info -Ph2
mvn flyway:migrate -Ph2
mvn flyway:validate -Ph2
```

### 辅助脚本

```bash
cd database-migrations

# MySQL（默认）
./scripts/info.sh
./scripts/migrate.sh
./scripts/clean.sh
./scripts/repair.sh

# H2（开发环境）
DB=h2 ./scripts/info.sh
DB=h2 ./scripts/migrate.sh
DB=h2 ./scripts/clean.sh
DB=h2 ./scripts/repair.sh
```

### H2 专用脚本

```bash
cd database-migrations/h2

./info.sh      # 查看状态
./migrate.sh   # 执行迁移
./clean.sh     # 清空数据库
./console.sh   # 启动独立 Console
```

## 迁移脚本命名规范

在 `database-migrations/migrations/` 目录下创建迁移脚本：

```
V1__Init_schema.sql
V2__create_user_table.sql
V3__add_email_index.sql
```

命名规则：
- 以 `V` 开头（版本迁移）
- 版本号：1, 2, 3...（不要使用 1.0.0 格式）
- 双下划线 `__` 分隔符
- 描述性名称（使用下划线代替空格）

## 多数据库兼容性

迁移脚本需要兼容 MySQL 和 H2，使用 MySQL 条件注释：

```sql
-- MySQL 特定：创建数据库（H2 会忽略）
/*! CREATE DATABASE IF NOT EXISTS mydb CHARACTER SET utf8mb4 */;
/*! USE mydb */;

-- 通用：创建表（兼容两种数据库）
CREATE TABLE IF NOT EXISTS t_user (
    id BIGINT PRIMARY KEY AUTO_INCREMENT,
    email VARCHAR(100) NOT NULL UNIQUE,
    ...
);
```

## Spring Boot 集成

### application.yml（生产环境）

```yaml
spring:
  datasource:
    url: jdbc:mysql://localhost:3306/mydb
    username: root
    password: password
  flyway:
    enabled: false  # 禁用自动迁移，需人工操作
```

### application-dev.yml（开发环境）

```yaml
spring:
  datasource:
    url: jdbc:h2:file:../database-migrations/h2/data/mydb;MODE=MySQL
    username: sa
    password:
  flyway:
    enabled: true  # 启用自动迁移
    locations: filesystem:../database-migrations/migrations
```

## 目录结构

```
database-migrations/
├── migrations/           # Flyway 迁移脚本
│   ├── V1__Init_schema.sql
│   └── V2__create_user_table.sql
├── scripts/             # 通用管理脚本
│   ├── migrate.sh
│   ├── info.sh
│   ├── validate.sh
│   ├── clean.sh
│   └── repair.sh
├── h2/                  # H2 专用目录
│   ├── data/            # 数据库文件（git 忽略）
│   ├── console.sh
│   ├── migrate.sh
│   ├── info.sh
│   └── clean.sh
├── pom.xml              # 独立的 Maven 配置
└── .gitignore           # 排除数据库文件
```

## 相关文档

- [Flyway 官方文档](https://flywaydb.org/documentation/)
- [Flyway Maven 插件](https://flywaydb.org/documentation/maven/)
- [H2 数据库 MySQL 兼容模式](https://www.h2database.com/html/features.html#compatibility)
