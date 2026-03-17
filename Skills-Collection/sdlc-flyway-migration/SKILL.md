---
name: flyway-migration
description: 管理 Flyway 数据库迁移脚本。数据库版本控制时使用。
allowed-tools: ["Read", "Write", "Edit", "Glob", "Bash"]
---

# Flyway 数据库迁移助手

自动化管理 Flyway 数据库迁移脚本的生成、验证和执行。

## 功能特性

- 自动生成迁移脚本
- 验证 SQL 语法正确性
- 检查迁移命名规范
- 独立的数据库迁移管理模块
- 支持多数据库（MySQL、H2）
- 完整的辅助脚本工具集
- **通用模板文件**：可快速复用到新项目

## 模板文件

本技能包含可直接复用的模板文件，存放在 `templates/` 目录下：

### 快速复用到新项目

```bash
# 1. 在项目中创建目录结构
mkdir -p database-migrations/{migrations,scripts,h2/data}

# 2. 复制模板文件
cp templates/pom.xml.template database-migrations/pom.xml
cp templates/.gitignore.template database-migrations/.gitignore
cp templates/scripts/*.template database-migrations/scripts/
cp templates/h2/*.template database-migrations/h2/

# 3. 去掉 .template 后缀并设置权限
cd database-migrations/scripts
for f in *.template; do mv "$f" "${f%.template}"; done
chmod +x *.sh

cd ../h2
for f in *.template; do mv "$f" "${f%.template}"; done
chmod +x *.sh
```

### 模板清单

**配置文件：**
- `pom.xml.template` - Maven 配置（支持 MySQL/H2 双数据库 Profile）
- `.gitignore.template` - Git 忽略规则

**通用脚本（scripts/）：**
- `migrate.sh.template` - 执行数据库迁移
- `info.sh.template` - 查看迁移状态
- `validate.sh.template` - 验证迁移脚本
- `clean.sh.template` - 清理并重建数据库
- `repair.sh.template` - 修复 Schema History

**H2 专用脚本（h2/）：**
- `console.sh.template` - 启动独立 H2 Console
- `migrate.sh.template` - H2 数据库迁移
- `info.sh.template` - H2 数据库状态
- `clean.sh.template` - 清理 H2 数据库

### 模板占位符

复制模板后需要替换以下占位符：

| 占位符 | 说明 | 示例 |
|--------|------|------|
| `{{GROUP_ID}}` | Maven Group ID | `com.example` |
| `{{PROJECT_NAME}}` | 项目名称 | `MyProject` |
| `{{MYSQL_JDBC_URL}}` | MySQL JDBC URL | `jdbc:mysql://localhost:3306/mydb...` |
| `{{MYSQL_USER}}` | MySQL 用户名 | `root` |
| `{{MYSQL_PASSWORD}}` | MySQL 密码 | `password` |
| `{{MYSQL_SCHEMA}}` | MySQL Schema 名 | `mydb` |
| `{{DB_NAME}}` | 数据库名称 | `mydb` |
| `{{H2_VERSION}}` | H2 版本 | `1.4.200` |

**详细使用说明请参考 `templates/README.md`**

## 项目结构

推荐使用独立的 `database-migrations` 目录组织迁移脚本：

```
database-migrations/
├── migrations/           # Flyway 迁移脚本
│   ├── V1__Init_schema.sql
│   ├── V2__create_tag_table.sql
│   ├── V3__create_todo_tag_table.sql
│   └── V4__init_data.sql
├── scripts/             # 通用管理脚本（支持 MySQL/H2）
│   ├── migrate.sh       # 执行迁移
│   ├── info.sh          # 查看状态
│   ├── validate.sh      # 验证脚本
│   ├── clean.sh         # 清理并重建
│   └── repair.sh        # 修复历史表
├── h2/                  # H2 专用目录
│   ├── data/            # 数据库文件（git 忽略）
│   │   └── todolist.mv.db
│   ├── console.sh       # 独立 H2 Console
│   ├── migrate.sh       # H2 迁移
│   ├── info.sh          # H2 状态
│   └── clean.sh         # H2 清理
├── pom.xml              # 独立的 Maven 配置
│   └── README.md        # 详细文档
└── .gitignore           # 排除数据库文件
```

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

# 清空重建
mvn flyway:clean && mvn flyway:migrate
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
./scripts/clean.sh      # 清理并重建
./scripts/repair.sh     # 修复历史表

# H2（开发环境）
DB=h2 ./scripts/info.sh
DB=h2 ./scripts/migrate.sh
DB=h2 ./scripts/clean.sh
DB=h2 ./scripts/repair.sh
```

**H2 专用脚本（h2/）**
```bash
cd h2

./info.sh      # 查看状态
./migrate.sh   # 执行迁移
./clean.sh     # 清空数据库
./console.sh   # 启动独立 Console
```

### 独立 H2 Console

H2 Console 可以独立运行，无需 Spring Boot 应用：

```bash
cd h2
./console.sh
```

访问 http://localhost:8082，连接信息：
- **JDBC URL**: `jdbc:h2:tcp://localhost:9092/todolist`
- **User Name**: `sa`
- **Password**: (留空)

## 迁移命名规范

### 版本格式

```
V<版本号>__<描述>.sql
```

**版本号规则**：
- 使用简单递增数字：1, 2, 3...
- 不要使用 1.0.0 格式（兼容性问题）
- 必须递增，不能跳过

**示例**：
- `V1__Init_schema.sql` - 初始化数据库
- `V2__create_tag_table.sql` - 创建标签表
- `V3__create_todo_tag_table.sql` - 创建关联表
- `V4__init_data.sql` - 初始化数据

### 多数据库兼容性

迁移脚本需要兼容 MySQL 和 H2：

```sql
-- V1__Init_schema.sql
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

## Maven 配置

### standalone pom.xml

在 `database-migrations/` 目录创建独立的 pom.xml：

```xml
<properties>
    <flyway.version>7.15.0</flyway.version>
    <h2.version>1.4.200</h2.version>
</properties>

<dependencies>
    <dependency>
        <groupId>org.flywaydb</groupId>
        <artifactId>flyway-core</artifactId>
        <version>${flyway.version}</version>
    </dependency>
    <dependency>
        <groupId>mysql</groupId>
        <artifactId>mysql-connector-java</artifactId>
        <version>8.0.20</version>
    </dependency>
</dependencies>

<build>
    <profiles>
        <!-- MySQL Profile -->
        <profile>
            <id>mysql</id>
            <activation>
                <activeByDefault>true</activeByDefault>
            </activation>
            <build>
                <plugins>
                    <plugin>
                        <groupId>org.flywaydb</groupId>
                        <artifactId>flyway-maven-plugin</artifactId>
                        <configuration>
                            <url>jdbc:mysql://127.0.0.1:13306?...</url>
                            <user>root</user>
                            <password>root@P@SSw0Rd</password>
                            <schemas>todolist</schemas>
                            <locations>filesystem:migrations</locations>
                        </configuration>
                    </plugin>
                </plugins>
            </build>
        </profile>

        <!-- H2 Profile -->
        <profile>
            <id>h2</id>
            <build>
                <plugins>
                    <plugin>
                        <groupId>org.flywaydb</groupId>
                        <artifactId>flyway-maven-plugin</artifactId>
                        <configuration>
                            <url>jdbc:h2:file:./h2/data/todolist;MODE=MySQL</url>
                            <locations>filesystem:migrations</locations>
                        </configuration>
                    </plugin>
                </plugins>
            </build>
        </profile>
    </profiles>
</build>
```

## Spring Boot 集成

### Profile-based 迁移策略

**推荐策略**：
- **dev 模式**：应用启动时自动执行 Flyway 迁移
- **prod 模式**：禁用自动迁移，需人工使用脚本操作

### application.yml（生产环境）

```yaml
spring:
  datasource:
    driver-class-name: com.mysql.cj.jdbc.Driver
    url: jdbc:mysql://127.0.0.1:13306/todolist?...
    username: root
    password: root@P@SSw0Rd

  flyway:
    enabled: false  # 生产环境禁用自动迁移
    locations: classpath:db/migration,filesystem:database-migrations/migrations
```

### application-dev.yml（开发环境）

```yaml
spring:
  datasource:
    driver-class-name: org.h2.Driver
    url: jdbc:h2:file:../database-migrations/h2/data/todolist;MODE=MySQL
    username: sa
    password:

  flyway:
    enabled: true  # 开发环境启用自动迁移
    locations: filesystem:../database-migrations/migrations
```

### backend/pom.xml

确保 H2 版本与 Flyway 一致：

```xml
<properties>
    <h2.version>1.4.200</h2.version>
</properties>

<dependencies>
    <dependency>
        <groupId>com.h2database</groupId>
        <artifactId>h2</artifactId>
        <version>${h2.version}</version>
        <scope>runtime</scope>
    </dependency>
</dependencies>
```

## 迁移模板

### 创建表模板

```sql
-- V<version>__create_<table_name>.sql
-- 描述: 创建<表描述>
-- 作者: <作者>
-- 日期: YYYY-MM-DD

CREATE TABLE IF NOT EXISTS <table_name> (
    id BIGINT PRIMARY KEY AUTO_INCREMENT COMMENT '<主键>',
    <field1> <type> <constraints> COMMENT '<字段1>',
    <field2> <type> <constraints> COMMENT '<字段2>',
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间'
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='<表说明>';
```

### 添加字段模板

```sql
-- V<version>__add_<field_name>_to_<table_name>.sql
-- 描述: 添加<字段说明>
-- 作者: <作者>
-- 日期: YYYY-MM-DD

ALTER TABLE <table_name>
ADD COLUMN <column_name> <data_type> <constraints> COMMENT '<字段说明>';

-- 如果需要更新现有数据
-- UPDATE <table_name> SET <column_name> = <default_value> WHERE <condition>;
```

### 创建索引模板

```sql
-- V<version>__create_index_on_<table_name>.sql
-- 描述: 创建<索引说明>
-- 作者: <作者>
-- 日期: YYYY-MM-DD

CREATE INDEX idx_<table_name>_<column>
ON <table_name>(<column_name>);

-- 复合索引
CREATE INDEX idx_<table_name>_<column1>_<column2>
ON <table_name>(<column1>, <column2>);
```

### 数据迁移模板

```sql
-- V<version>__migrate_<entity>_data.sql
-- 描述: <迁移说明>
-- 作者: <作者>
-- 日期: YYYY-MM-DD

-- 从旧表迁移数据
INSERT INTO <new_table> (id, name, created_at)
SELECT id, name, created_at
FROM <old_table>;

-- 数据转换
UPDATE <table_name>
SET <field> = <new_value>
WHERE <condition>;
```

## SQL 编写规范

### 增量变更原则

**✅ 正确：增量变更**
```sql
-- V2__add_user_status.sql
ALTER TABLE sys_user ADD COLUMN status VARCHAR(10) DEFAULT '0';
```

**❌ 错误：完整重建**
```sql
-- 不要在已有表上使用 DROP TABLE + CREATE TABLE
DROP TABLE IF EXISTS sys_user;
CREATE TABLE sys_user (...);
```

### 事务处理

每个迁移脚本默认在一个事务中执行，多个语句自动一起提交或回滚。

### 外键和索引

```sql
-- 先添加字段
ALTER TABLE sys_user ADD COLUMN dept_id BIGINT;

-- 再添加外键
ALTER TABLE sys_user_role
ADD CONSTRAINT fk_user_dept
FOREIGN KEY (user_id) REFERENCES sys_user(id);
```

## 验证检查清单

在执行迁移前，确保：

- [ ] 版本号正确递增（1, 2, 3...）
- [ ] SQL 语法正确
- [ ] 使用增量变更（ALTER，不是 DROP+CREATE）
- [ ] 外键依赖关系正确
- [ ] 索引命名符合规范
- [ ] 添加必要的 COMMENT
- [ ] 测试环境已验证
- [ ] 准备了回滚方案

## 回滚策略

Flyway 不自动支持回滚，需要手动编写回滚脚本：

### 建议的回滚流程

1. 记录每次迁移的回滚 SQL
2. 在测试环境验证回滚脚本
3. 生产环境回滚前先备份数据库
4. 按版本倒序执行回滚

### 回滚示例

```sql
-- 回滚 V2__add_user_status.sql
ALTER TABLE sys_user DROP COLUMN status;

-- 回滚 V3__create_index.sql
DROP INDEX idx_username ON sys_user;
```

## 辅助脚本实现

### migrate.sh

```bash
#!/bin/bash
# 根据 DB 环境变量选择数据库
DB_TYPE=${DB:-mysql}
PROFILE=""
[ "$DB_TYPE" = "h2" ] && PROFILE="-Ph2"
mvn flyway:migrate $PROFILE
```

### info.sh

```bash
#!/bin/bash
DB_TYPE=${DB:-mysql}
PROFILE=""
[ "$DB_TYPE" = "h2" ] && PROFILE="-Ph2"
mvn flyway:info $PROFILE
```

### clean.sh

```bash
#!/bin/bash
# 清空并重建数据库
DB_TYPE=${DB:-mysql}
PROFILE=""
[ "$DB_TYPE" = "h2" ] && PROFILE="-Ph2"
mvn flyway:clean $PROFILE && mvn flyway:migrate $PROFILE
```

## 工作流程

### 日常开发流程

1. **创建迁移脚本**
   ```bash
   cd database-migrations/migrations
   # 新建 V5__xxx.sql
   ```

2. **验证脚本**
   ```bash
   ./scripts/validate.sh  # 或 DB=h2 ./scripts/validate.sh
   ```

3. **测试迁移**
   ```bash
   # H2 测试
   DB=h2 ./scripts/clean.sh
   DB=h2 ./scripts/migrate.sh
   ```

4. **生产迁移**
   ```bash
   ./scripts/migrate.sh
   ```

### 生产环境部署流程

1. **代码审查**
   - 检查 SQL 语法
   - 验证增量变更
   - 确认回滚方案

2. **预发布验证**
   ```bash
   # 在测试环境验证
   ./scripts/info.sh
   ./scripts/validate.sh
   ```

3. **执行迁移**
   ```bash
   # 备份数据库
   # 执行迁移
   ./scripts/migrate.sh
   # 验证结果
   ```

## 最佳实践

1. **小步快跑**：每个迁移只做一件事
2. **向前兼容**：新字段设置默认值
3. **数据备份**：执行前备份数据库
4. **测试验证**：先在测试环境执行
5. **代码审查**：迁移脚本需要 Code Review
6. **文档记录**：重要变更写文档说明
7. **避免数据丢失**：不使用 DROP 或 TRUNCATE
8. **考虑性能**：大批量数据分批处理
9. **版本控制**：迁移脚本纳入 Git 管理
10. **环境隔离**：开发/测试/生产使用相同脚本

## 相关文档

- Flyway 官方文档: https://flywaydb.org/documentation/
- Flyway Maven 插件: https://flywaydb.org/documentation/maven/
- MySQL 迁移最佳实践: https://flywaydb.org/documentation/database/mysql
