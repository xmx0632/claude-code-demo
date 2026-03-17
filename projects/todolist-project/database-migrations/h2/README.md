# H2 开发数据库

H2 数据库管理目录，提供本地开发环境的数据持久化。

## 目录结构

```
h2/
├── data/                    # 数据库文件目录（本地生成，git 忽略）
│   ├── todolist.mv.db      # H2 数据库主文件
│   └── todolist.trace.db   # H2 追踪文件（调试时生成）
├── clean.sh                # 清理数据库脚本
├── console.sh              # 启动 H2 Console
├── info.sh                 # 查看数据库状态
├── migrate.sh              # 执行迁移脚本
└── README.md               # 本文档
```

## 快速开始

### 初始化数据库

```bash
cd h2
./migrate.sh
```

### 查看状态

```bash
./info.sh
```

### 清理数据库

```bash
./clean.sh
```

### 启动 H2 Console（独立运行）

H2 Console 可以独立运行，无需启动 Spring Boot 应用：

```bash
./console.sh
```

H2 Console 将自动启动，并在浏览器中打开。

**独立模式的连接信息：**
```
JDBC URL: jdbc:h2:tcp://localhost:9092/todolist
User Name: sa
Password: (留空)
```

**注意：** 独立模式使用 TCP 服务器，避免文件锁定问题。

### 启动 H2 Console（通过 Spring Boot）

如果 Spring Boot 应用正在运行，也可以使用内置的 H2 Console：

```bash
# 启动应用（dev 模式）
cd ../..
mvn spring-boot:run -Dspring-boot.run.profiles=dev
```

然后访问: http://localhost:8081/h2-console

**应用模式的连接信息：**
```
JDBC URL: jdbc:h2:file:../database-migrations/h2/data/todolist
User Name: sa
Password: (留空)
```

## Maven 命令

也可以直接使用 Maven（从项目根目录）：

```bash
cd database-migrations

# 迁移
mvn flyway:migrate -Ph2

# 查看状态
mvn flyway:info -Ph2

# 验证
mvn flyway:validate -Ph2

# 清空
mvn flyway:clean -Ph2
```

## H2 Console 连接信息

```
JDBC URL: jdbc:h2:file:./database-migrations/h2/data/todolist
User Name: sa
Password: (留空)
```

## 数据库文件说明

### data/todolist.mv.db
- H2 数据库主文件
- 包含所有表结构和数据
- 持久化到本地磁盘

### data/todolist.trace.db
- H2 追踪文件（仅调试时生成）
- 记录 SQL 执行日志
- 可以安全删除

## 开发工作流

### 典型开发流程

```bash
# 1. 清理旧数据
cd h2 && ./clean.sh

# 2. 执行迁移
./migrate.sh

# 3. 启动应用测试
cd ../..
mvn spring-boot:run -Dspring-boot.run.profiles=dev

# 4. 查看数据
# 使用 H2 Console 或直接查看 data/ 目录
```

### 调试模式

在 `application-dev.yml` 中启用 SQL 日志：

```yaml
logging:
  level:
    com.todolist: debug
    org.hibernate.SQL: DEBUG
```

## 常见问题

### 数据库锁定

```bash
# 删除锁文件
rm -f h2/data/*.lock.db
```

### 迁移失败

```bash
# 完全重置
cd h2 && ./clean.sh && ./migrate.sh
```

### 查看表结构

使用 H2 Console 或执行：

```sql
SHOW TABLES;
DESCRIBE t_user;
```

## 注意事项

1. **本地文件**：`data/` 目录在 `.gitignore` 中，不会提交到 git
2. **数据隔离**：每个开发者拥有独立的 H2 数据库
3. **定期清理**：开发完成后建议运行 `./clean.sh` 清理数据
4. **兼容性**：H2 运行在 MySQL 模式，与生产环境 MySQL 语法兼容

## 相关链接

- [H2 数据库文档](https://www.h2database.com/html/main.html)
- [Flyway 文档](https://flywaydb.org/documentation/)
- [Spring Boot H2 配置](https://docs.spring.io/spring-boot/docs/current/reference/html/howto.html#howto.data-initialization.using-database-tools)
