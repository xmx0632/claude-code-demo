# H2 开发数据库

此目录用于存储 H2 开发数据库的持久化文件。

## 目录结构

```
h2/
├── todolist.mv.db    # H2 数据库主文件
└── todolist.trace.db # H2 追踪文件（调试时生成）
```

## 使用方法

### 1. 使用 Maven 管理 H2 数据库

```bash
cd database-migrations

# 初始化/迁移 H2 数据库
mvn flyway:migrate -Ph2

# 查看迁移状态
mvn flyway:info -Ph2

# 验证脚本
mvn flyway:validate -Ph2

# 清空数据库
mvn flyway:clean -Ph2
```

### 2. 使用 H2 Console

启动后端应用后，访问 H2 Console：

```
URL: http://localhost:8080/h2-console
```

连接信息：
- **JDBC URL**: `jdbc:h2:file:./database-migrations/h2/todolist`
- **User Name**: `sa`
- **Password**: (留空)

### 3. 直接使用 H2 CLI

```bash
# 进入 H2 目录
cd database-migrations/h2

# 使用 java -jar 运行 H2（需要 H2 jar 包）
java -cp ~/.m2/repository/com/h2database/h2/1.4.200/h2-1.4.200.jar org.h2.tools.Console
```

## 数据库文件说明

### todolist.mv.db
- H2 数据库主文件
- 包含所有表结构和数据
- 持久化到本地磁盘

### todolist.trace.db
- H2 追踪文件（仅调试时生成）
- 记录 SQL 执行日志
- 可以安全删除

## 清理数据库

### 完全重置

```bash
cd database-migrations

# 方法 1: 使用 Flyway clean
mvn flyway:clean -Ph2

# 方法 2: 手动删除文件
rm -rf h2/*.db

# 方法 3: 重新迁移
mvn flyway:clean -Ph2 && mvn flyway:migrate -Ph2
```

### 仅重置数据（保留表结构）

使用 H2 Console 或 Flyway 迁移到版本 0 再迁移回来。

## 调试技巧

### 启用 SQL 日志

在 `application-dev.yml` 中添加：

```yaml
logging:
  level:
    org.hibernate.SQL: DEBUG
    org.hibernate.type.descriptor.sql.BasicBinder: TRACE
```

### 查看 Flyway 历史表

```sql
SELECT * FROM flyway_schema_history ORDER BY installed_rank;
```

### 导出数据

```bash
# 使用 H2 Script 工具
java -cp ~/.m2/repository/com/h2database/h2/1.4.200/h2-1.4.200.jar \
     org.h2.tools.Script -url jdbc:h2:file:./h2/todolist -user sa \
     -script backup.sql
```

## 注意事项

1. **不要提交到 Git**：`h2/` 目录已在 `.gitignore` 中
2. **数据隔离**：每个开发者拥有独立的 H2 数据库
3. **定期清理**：开发完成后建议清理数据库文件
4. **兼容性**：H2 运行在 MySQL 模式，语法与 MySQL 基本兼容

## 故障排查

### 数据库锁定

如果遇到数据库锁定错误：

```bash
# 检查是否有进程占用
lsof | grep todolist.mv.db

# 强制删除锁文件（谨慎使用）
rm -f h2/todolist.lock.db
```

### 迁移失败

```bash
# 查看详细错误
mvn flyway:migrate -Ph2 -X

# 重新开始
mvn flyway:clean -Ph2
mvn flyway:migrate -Ph2
```

## 相关链接

- [H2 数据库文档](https://www.h2database.com/html/main.html)
- [H2 MySQL 兼容模式](https://www.h2database.com/html/features.html#compatibility)
- [Flyway 文档](https://flywaydb.org/documentation/)
