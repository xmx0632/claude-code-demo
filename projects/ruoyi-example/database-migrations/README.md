# Database Migrations

独立的 Flyway 数据库迁移组件，用于管理和版本化数据库变更。

## 特性

- ✅ **可移植**: 可在任何项目中使用
- ✅ **独立管理**: 数据库变更与应用代码分离
- ✅ **版本控制**: 完整的迁移历史记录
- ✅ **灵活集成**: 支持多种集成方式

## 快速开始

### 1. 安装 Flyway

```bash
# macOS
brew install flyway

# Linux
wget https://repo1.maven.org/maven2/org/flywaydb/flyway-commandline/9.22.3/flyway-commandline-9.22.3-linux-x64.tar.gz
tar -xzf flyway-commandline-9.22.3-linux-x64.tar.gz

# Windows
# 从 https://flywaydb.org/download 下载
```

### 2. 配置数据库连接

```bash
# 复制配置文件
cp flyway.conf flyway.local.conf

# 编辑 flyway.local.conf，修改数据库连接信息
vi flyway.local.conf
```

配置示例：

```properties
flyway.url=jdbc:mysql://localhost:3306/your_database
flyway.user=root
flyway.password=your_password
```

### 3. 初始化数据库

```bash
# 创建数据库（可选）
./scripts/init-db.sh

# 执行迁移
./scripts/migrate.sh
```

## 目录结构

```
database-migrations/
├── README.md                    # 本文件
├── flyway.conf                  # Flyway 配置模板
├── migrations/                  # 迁移脚本目录
│   ├── V1__init_schema.sql
│   ├── V2__insert_init_data.sql
│   └── ...
├── scripts/                     # 辅助脚本
│   ├── migrate.sh              # 执行迁移
│   ├── info.sh                 # 查看状态
│   ├── validate.sh             # 验证脚本
│   ├── rollback.sh             # 生成回滚
│   └── init-db.sh              # 初始化数据库
└── docs/                        # 文档
    ├── conventions.md          # 命名规范
    └── changelog.md            # 变更日志
```

## 常用命令

### 执行迁移

```bash
./scripts/migrate.sh
```

### 查看迁移状态

```bash
./scripts/info.sh
```

输出示例：

```
Schema version: 7
Pending migrations: none
```

### 验证迁移脚本

```bash
./scripts/validate.sh
```

### 生成回滚脚本

```bash
./scripts/rollback.sh V8
```

## 集成到项目

### 方式 1: 命令行（推荐）

在项目根目录添加此组件为 submodule：

```bash
git submodule add https://github.com/your-org/database-migrations.git
cd database-migrations
./scripts/migrate.sh
```

### 方式 2: Maven 插件

在 `pom.xml` 中配置：

```xml
<plugin>
    <groupId>org.flywaydb</groupId>
    <artifactId>flyway-maven-plugin</artifactId>
    <version>9.22.3</version>
    <configuration>
        <configFiles>../database-migrations/flyway.conf</configFiles>
        <locations>filesystem:../database-migrations/migrations</locations>
    </configuration>
</plugin>
```

执行：

```bash
mvn flyway:migrate
```

### 方式 3: Spring Boot 集成

在 `application.yml` 中配置：

```yaml
spring:
  flyway:
    enabled: true
    locations: filesystem:../database-migrations/migrations
    baseline-on-migrate: true
```

## 迁移脚本命名规范

Flyway 迁移文件命名格式：

```
V<版本号>__<描述>.sql
```

示例：

- `V1__init_schema.sql` - 初始化数据库结构
- `V2__insert_init_data.sql` - 插入初始数据
- `V3__add_user_status.sql` - 添加用户状态字段
- `V3.1__create_index.sql` - 创建索引（小版本）

详细规范请参考 [docs/conventions.md](./docs/conventions.md)。

## 编写迁移脚本

### 增量变更原则

**✅ 正确**：使用 ALTER TABLE 添加新字段

```sql
ALTER TABLE sys_user
ADD COLUMN status VARCHAR(10) DEFAULT '0';
```

**❌ 错误**：使用 DROP + CREATE

```sql
DROP TABLE IF EXISTS sys_user;
CREATE TABLE sys_user (...);  -- 会丢失现有数据！
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

USE your_database;

-- 你的 SQL 语句

-- 示例：添加字段
ALTER TABLE table_name
ADD COLUMN column_name VARCHAR(255) DEFAULT '';

-- 示例：创建索引
CREATE INDEX idx_column ON table_name(column_name);
```

## 版本历史

查看完整变更日志：[docs/changelog.md](./docs/changelog.md)

## 常见问题

### Q: 迁移失败怎么办？

A: 检查 SQL 语法，修复后重新执行。如果需要修复 Flyway 状态：

```bash
flyway repair
```

### Q: 如何跳过某个迁移？

A: 不建议跳过。如必须，手动修改 `flyway_schema_history` 表。

### Q: 已有数据库如何接入？

A: 启用 `baselineOnMigrate=true`，Flyway 会自动为已有数据库建立基线。

### Q: 如何回滚？

A: Flyway Community 不支持自动回滚。使用 `./scripts/rollback.sh` 生成回滚脚本参考。

## 最佳实践

1. **小步快跑**：每个迁移只做一件事
2. **向前兼容**：新字段设置默认值
3. **先测试**：在测试环境验证后再应用到生产
4. **备份数据**：执行迁移前备份数据库
5. **代码审查**：迁移脚本需要团队审查
6. **记录变更**：在 changelog.md 中记录重要变更

## 相关资源

- [Flyway 官方文档](https://flywaydb.org/documentation/)
- [MySQL 最佳实践](https://flywaydb.org/documentation/database/mysql)
- [迁移脚本编写规范](./docs/conventions.md)

## 许可证

MIT
