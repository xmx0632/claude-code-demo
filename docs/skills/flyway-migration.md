# sdlc-flyway-migration 使用指南

Flyway 数据库迁移管理工具，用于数据库版本控制、迁移脚本创建和验证。

## 🚀 快速开始

```bash
# 1. 创建新的迁移脚本
/sdlc-flyway-migration create --table=sys_user --type=add_column

# 2. 验证迁移脚本
/sdlc-flyway-migration validate V2__add_column.sql

# 3. 生成回滚脚本
/sdlc-flyway-migration rollback V2__add_column.sql
```

## 📋 核心功能

| 功能 | 说明 |
|------|------|
| **迁移脚本创建** | 自动生成符合命名规范的 SQL 脚本 |
| **脚本验证** | 检查 SQL 语法和潜在问题 |
| **回滚脚本生成** | 生成对应的回滚 SQL |
| **迁移执行** | 按顺序执行迁移脚本 |
| **状态检查** | 查看当前数据库版本状态 |

## 🎯 命名规范

迁移脚本按以下格式命名：

```
V{version}__{description}.sql

例如：
V1__Init_schema.sql
V2__create_user_table.sql
V3__add_email_index.sql
```

**规则**：
- 版本号：从 V1 开始递增
- 描述：简短描述，使用下划线连接
- 后缀：必须为 `.sql`

## 📝 使用示例

### 示例 1：创建表迁移

```bash
/sdlc-flyway-migration create --table=sys_user --type=create_table
```

**生成内容**：
```sql
-- V2__create_table_sys_user.sql
CREATE TABLE sys_user (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    user_name VARCHAR(50) NOT NULL,
    email VARCHAR(100) NOT NULL,
    created_at DATETIME DEFAULT CURRENT_TIMESTAMP,
    updated_at DATETIME DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
    deleted TINYINT DEFAULT 0
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;
```

### 示例 2：添加字段迁移

```bash
/sdlc-flyway-migration create --table=sys_user --type=add_column --column=last_login_time
```

### 示例 3：验证迁移

```bash
/sdlc-flyway-migration validate V2__create_table_sys_user.sql
```

**检查项**：
- SQL 语法正确性
- 表名和字段名规范
- 索引命名规范
- 外键约束完整性

### 示例 4：生成回滚脚本

```bash
/sdlc-flyway-migration rollback V3__add_email_index.sql
```

**生成回滚**：
```sql
-- 回滚 V3__add_email_index.sql
ALTER TABLE sys_user DROP INDEX idx_email;
```

## ⚙️ 配置说明

### 迁移组件结构

```
database-migrations/
├── migrations/              # Flyway 迁移脚本目录
│   ├── V1__Init_schema.sql
│   ├── V2__create_table.sql
│   └── V3__add_index.sql
├── pom.xml                  # Maven 配置（数据库连接）
└── scripts/                 # 辅助脚本
    ├── migrate.sh           # 执行迁移
    └── info.sh              # 查看状态
```

### pom.xml 配置

数据库连接通过 Maven `profiles` 配置，支持不同环境：

**MySQL Profile（生产环境，默认）**：
```xml
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
                    <url>jdbc:mysql://localhost:3306/database_name</url>
                    <user>root</user>
                    <password>password</password>
                    <schemas>database_schema</schemas>
                    <locations>filesystem:migrations</locations>
                    <encoding>UTF-8</encoding>
                    <baselineOnMigrate>true</baselineOnMigrate>
                    <validateOnMigrate>true</validateOnMigrate>
                </configuration>
            </plugin>
        </plugins>
    </build>
</profile>
```

**H2 Profile（开发环境）**：
```xml
<profile>
    <id>h2</id>
    <build>
        <plugins>
            <plugin>
                <groupId>org.flywaydb</groupId>
                <artifactId>flyway-maven-plugin</artifactId>
                <configuration>
                    <url>jdbc:h2:file:./h2/data/todolist;MODE=MySQL</url>
                    <user>sa</user>
                    <password></password>
                    <locations>filesystem:migrations</locations>
                </configuration>
            </plugin>
        </plugins>
    </build>
</profile>
```

**配置说明**：
- `url`: 数据库连接 URL
- `user` / `password`: 数据库用户名和密码
- `schemas`: 目标数据库 schema 名称
- `locations`: 迁移脚本位置
- `baselineOnMigrate`: 对现有数据库初始化
- `validateOnMigrate`: 迁移前验证 SQL

## 🔧 常用命令

### 执行迁移

```bash
# 进入数据库迁移组件目录
cd database-migrations

# 执行迁移（使用默认 profile，如 MySQL）
mvn flyway:migrate

# 查看迁移状态
mvn flyway:info

# 验证 SQL 脚本
mvn flyway:validate

# 清理（开发环境，重置数据库）
mvn flyway:clean flyway:migrate
```

### 不同环境切换

```bash
# 使用 MySQL profile（默认）
mvn flyway:migrate

# 使用 H2 profile（开发环境）
mvn flyway:migrate -Ph2
```

### SQL 类型和迁移类型

| SQL 类型 | 说明 |
|----------|------|
| `create_table` | 创建新表 |
| `add_column` | 添加字段 |
| `add_index` | 添加索引 |
| `add_foreign_key` | 添加外键 |
| `modify_column` | 修改字段 |
| `drop_column` | 删除字段 |
| `drop_table` | 删除表 |
| `init_data` | 初始化数据 |

## 🐛 常见问题

### Q: 迁移脚本执行失败怎么办？
**A**:
1. 检查数据库连接
2. 验证 SQL 语法
3. 查看 Flyway 历史表，定位失败的脚本
4. 修复后重新执行

### Q: 如何回滚已执行的迁移？
**A**: Flyway 不支持自动回滚，需要手动执行回滚 SQL

### Q: 多人协作时如何处理版本冲突？
**A**:
1. 版本号协调使用，避免冲突
2. 使用 Git 管理迁移脚本
3. 定期同步 `flyway_schema_history` 表

### Q: 如何在不同环境使用不同配置？
**A**: 使用 Maven profiles 切换不同环境的数据库配置：
- `mvn flyway:migrate` - 使用默认的 MySQL profile（生产环境）
- `mvn flyway:migrate -Ph2` - 使用 H2 profile（开发环境）

## 📚 最佳实践

1. **版本号递增**: 始终使用下一个可用的版本号
2. **原子性**: 每个迁移脚本应该可以独立执行和回滚
3. **向前兼容**: 新迁移不应破坏现有数据
4. **测试先行**: 在开发环境验证后再用于生产
5. **备份重要**: 执行迁移前备份数据库

## 📚 完整参考

详细配置和高级用法请参考：
[SKILL.md](../../.claude/skills/sdlc-flyway-migration/SKILL.md)

## 🔗 相关 Skills

- [sdlc-architecture-design](../skills/index.md#sdlc-architecture-design) - 系统架构设计
- [sdlc-detailed-design](../skills/index.md#sdlc-detailed-design) - 详细设计阶段
- [sdlc-deployment](../skills/index.md#sdlc-deployment) - 部署指南
