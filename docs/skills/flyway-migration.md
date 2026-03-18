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

### 迁移脚本位置

默认迁移脚本位置：
```
database-migrations/
├── migrations/
│   ├── V1__Init_schema.sql
│   ├── V2__create_table.sql
│   └── V3__add_index.sql
├── scripts/
│   ├── migrate.sh    # 执行迁移
│   ├── info.sh        # 查看状态
│   └── validate.sh    # 验证脚本
└── flyway.conf       # Flyway 配置
```

### Flyway 配置文件

```properties
flyway.url=jdbc:mysql://localhost:3306/database_name
flyway.user=root
flyway.password=password
flyway.schemas=database_schema
flyway.locations=filesystem:migrations
flyway.baselineOnMigrate=true
flyway.encoding=UTF-8
flyway.validateOnMigrate=true
```

## 🔧 常用命令

### 执行迁移

```bash
# 查看当前状态
./scripts/info.sh

# 执行所有待执行的迁移
./scripts/migrate.sh

# 验证迁移脚本
./scripts/validate.sh
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
**A**: 使用 Flyway 的配置文件或环境变量

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
