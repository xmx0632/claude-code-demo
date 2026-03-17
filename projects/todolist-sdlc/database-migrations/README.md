# TodoList SDLC - 数据库迁移管理

本目录包含 TodoList SDLC 项目的数据库迁移脚本和管理工具。

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
├── flyway.conf          # Flyway 配置
└── README.md            # 本文档
```

## 前置要求

### 安装 Flyway

```bash
# macOS
brew install flyway

# Linux
# 下载并解压 Flyway from https://flywaydb.org/download
```

### 数据库准备

确保 MySQL 服务运行并且数据库已创建：

```sql
CREATE DATABASE IF NOT EXISTS todolist CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci;
```

## 使用方法

### 1. 执行迁移

```bash
cd database-migrations
./scripts/migrate.sh
```

### 2. 查看迁移状态

```bash
cd database-migrations
./scripts/info.sh
```

### 3. 验证迁移脚本

```bash
cd database-migrations
./scripts/validate.sh
```

### 4. 创建新的迁移脚本

按照 Flyway 命名约定创建新脚本：

```
V5__description.sql
V6__another_feature.sql
```

命名规则：
- 以 `V` 开头
- 版本号：1, 2, 3...（不要使用 1.0.0 格式）
- 双下划线 `__` 分隔符
- 描述性名称（使用下划线代替空格）

## Spring Boot 集成

应用启动时自动执行迁移。配置见 `backend/src/main/resources/application.yml`：

```yaml
spring:
  flyway:
    enabled: true
    locations: filesystem:database-migrations/migrations
    baseline-on-migrate: true
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
- [Spring Boot Flyway 集成](https://docs.spring.io/spring-boot/docs/current/reference/html/howto.html#howto.data-initialization.migration-tool.flyway)
