# Docker 服务配置

本目录包含 ruoyi-example 应用及其依赖基础设施服务的 Docker Compose 配置。

## 服务概览

本 Docker Compose 配置包含以下服务：

| 服务名 | 容器名 | 镜像 | 端口 | 说明 |
|--------|--------|------|------|------|
| mysql | ruoyi-mysql | mysql:8.0 | 3306 | MySQL 数据库 |
| redis | ruoyi-redis | redis:7-alpine | 6379 | Redis 缓存 |
| ruoyi-app | ruoyi-app | 自定义构建 | 8080 | Spring Boot 应用 |

## 快速开始

### 前置要求

- Docker 20.10+
- Docker Compose 2.0+ 或 docker-compose 1.29+

### 启动步骤

1. **进入 docker 目录**
   ```bash
   cd docker
   ```

2. **创建环境变量文件**
   ```bash
   cp .env.example .env
   ```

3. **修改 .env 文件**（重要）
   ```bash
   vi .env
   ```
   请务必修改以下密码配置：
   - `MYSQL_ROOT_PASSWORD` - MySQL root 密码
   - `MYSQL_PASSWORD` - 应用数据库用户密码

4. **启动所有服务**
   ```bash
   ./scripts/start.sh
   ```
   或使用 docker-compose：
   ```bash
   docker-compose up -d
   ```

5. **查看服务状态**
   ```bash
   docker-compose ps
   ```

6. **查看服务日志**
   ```bash
   # 查看所有服务日志
   ./scripts/logs.sh

   # 查看特定服务日志
   ./scripts/logs.sh mysql
   ./scripts/logs.sh redis
   ./scripts/logs.sh ruoyi-app
   ```

## 访问服务

服务启动后，可通过以下地址访问：

| 服务 | URL | 说明 |
|------|-----|------|
| 应用 | http://localhost:8080 | 主应用 |
| API 文档 | http://localhost:8080/doc.html | Knife4j API 文档 |
| 健康检查 | http://localhost:8080/actuator/health | Actuator 健康端点 |
| MySQL | localhost:3306 | 数据库连接 |
| Redis | localhost:6379 | Redis 连接 |

## 辅助脚本

### start.sh

启动所有服务。

```bash
./scripts/start.sh
```

功能：
- 检查 Docker 运行状态
- 检查 docker-compose 安装
- 检查 .env 文件（不存在则提示创建）
- 启动所有服务
- 显示服务状态

### stop.sh

停止所有服务。

```bash
./scripts/stop.sh
```

功能：
- 停止所有容器
- 保留数据卷

### logs.sh

查看服务日志。

```bash
# 查看所有服务日志
./scripts/logs.sh

# 查看特定服务日志
./scripts/logs.sh mysql
./scripts/logs.sh redis
./scripts/logs.sh ruoyi-app
```

按 `Ctrl+C` 退出日志查看。

### clean.sh

清理所有容器、网络和数据卷。

```bash
./scripts/clean.sh
```

**警告**：此操作将：
- 停止并删除所有容器
- 删除所有网络
- 删除所有数据卷（数据将永久丢失）

执行前会要求确认。

## 目录结构

```
docker/
├── README.md                   # 本文档
├── docker-compose.yml          # Docker Compose 配置
├── .env.example                # 环境变量模板
├── services/                   # 服务配置目录
│   ├── mysql/                  # MySQL 配置
│   │   ├── my.cnf              # MySQL 配置文件
│   │   └── init.sql            # 初始化 SQL
│   └── redis/                  # Redis 配置
│       └── redis.conf          # Redis 配置文件
├── app/                        # 应用容器配置
│   ├── Dockerfile              # 应用 Dockerfile
│   └── .dockerignore           # Docker 忽略文件
└── scripts/                    # 辅助脚本
    ├── start.sh                # 启动脚本
    ├── stop.sh                 # 停止脚本
    ├── logs.sh                 # 日志脚本
    └── clean.sh                # 清理脚本
```

## 环境变量

在 `.env` 文件中配置以下环境变量：

```env
# 应用配置
APP_PORT=8080                  # 应用端口
APP_NAME=ruoyi-example         # 应用名称

# MySQL 配置
MYSQL_ROOT_PASSWORD=root_password_change_me  # root 密码（必须修改）
MYSQL_DATABASE=ruoyi_example                # 数据库名称
MYSQL_USER=ruoyi                            # 应用用户名
MYSQL_PASSWORD=ruoyi_password_change_me     # 应用密码（必须修改）
MYSQL_PORT=3306                             # MySQL 端口

# Redis 配置
REDIS_PORT=6379                 # Redis 端口

# 时区配置
TZ=Asia/Shanghai               # 时区
```

## 数据持久化

本配置使用 Docker 数据卷实现数据持久化：

| 数据卷 | 用途 | 宿主机位置 |
|--------|------|------------|
| mysql-data | MySQL 数据 | Docker 管理的卷 |
| redis-data | Redis 数据 | Docker 管理的卷 |

查看数据卷：
```bash
docker volume ls | grep claude-code-demo
```

备份数据：
```bash
# 备份 MySQL 数据
docker run --rm \
  -v claude-code-demo_mysql-data:/data \
  -v $(pwd)/backup:/backup \
  alpine tar czf /backup/mysql-backup.tar.gz -C /data .

# 备份 Redis 数据
docker run --rm \
  -v claude-code-demo_redis-data:/data \
  -v $(pwd)/backup:/backup \
  alpine tar czf /backup/redis-backup.tar.gz -C /data .
```

## 数据库迁移

首次启动后，需要执行数据库迁移脚本：

### 方式 1：在容器内执行（推荐）

```bash
# 进入应用容器
docker-compose exec ruoyi-app sh

# 在容器内执行迁移
cd /app/database-migrations
./scripts/migrate.sh

# 退出容器
exit
```

### 方式 2：从主机执行

1. 确保容器已启动：
   ```bash
   docker-compose ps
   ```

2. 修改 `../database-migrations/flyway.conf`：
   ```properties
   flyway.url=jdbc:mysql://localhost:3306/ruoyi_example
   flyway.user=ruoyi
   flyway.password=ruoyi_password_change_me  # 修改为实际密码
   ```

3. 执行迁移：
   ```bash
   cd ../database-migrations
   ./scripts/migrate.sh
   ```

## 服务健康检查

所有服务都配置了健康检查：

### MySQL
```bash
# 检查 MySQL 健康状态
docker-compose exec mysql mysqladmin ping -h localhost

# 查看 MySQL 健康检查日志
docker inspect ruoyi-mysql | grep -A 10 Health
```

### Redis
```bash
# 连接 Redis
docker-compose exec redis redis-cli ping
# 应返回: PONG
```

### 应用
```bash
# 检查应用健康状态
curl http://localhost:8080/actuator/health

# 查看容器健康状态
docker ps | grep ruoyi-app
```

## 故障排查

### 端口冲突

如果 3306、6379 或 8080 端口已被占用，修改 `.env` 文件：

```env
MYSQL_PORT=3307
REDIS_PORT=6380
APP_PORT=8081
```

### 服务无法启动

1. **查看服务日志**
   ```bash
   ./scripts/logs.sh [服务名]
   ```

2. **检查容器状态**
   ```bash
   docker-compose ps
   ```

3. **重启服务**
   ```bash
   docker-compose restart [服务名]
   ```

### 数据库连接失败

1. **检查 MySQL 是否就绪**
   ```bash
   docker-compose logs mysql
   ```

2. **验证连接**
   ```bash
   docker-compose exec mysql mysql -u ruoyi -p
   ```

3. **检查网络**
   ```bash
   docker network ls | grep docker
   docker network inspect docker_default
   ```

### 完全重置

如果需要完全重置所有服务：

```bash
# 停止并删除所有容器、网络、数据卷
./scripts/clean.sh

# 重新启动
./scripts/start.sh

# 重新执行数据库迁移
docker-compose exec ruoyi-app sh -c "cd /app/database-migrations && ./scripts/migrate.sh"
```

## 生产环境部署

在生产环境使用时，请注意：

1. **修改所有默认密码**
   - `.env` 文件中的所有密码都必须修改为强密码

2. **启用 MySQL SSL**
   - 修改 `my.cnf` 启用 SSL 连接

3. **配置 Redis 密码**
   - 修改 `redis.conf` 设置 `requirepass`

4. **资源限制**
   - 在 `docker-compose.yml` 中添加 CPU/内存限制

5. **日志管理**
   - 配置日志驱动和轮转策略

6. **备份策略**
   - 定期备份 MySQL 和 Redis 数据卷

7. **监控**
   - 使用 Prometheus + Grafana 监控服务状态

## 高级用法

### 单独启动某个服务

```bash
# 只启动 MySQL 和 Redis
docker-compose up -d mysql redis

# 只启动应用（需要 MySQL 和 Redis 已启动）
docker-compose up -d ruoyi-app
```

### 重新构建应用镜像

```bash
# 重新构建并启动
docker-compose up -d --build ruoyi-app

# 只构建不启动
docker-compose build ruoyi-app
```

### 扩展服务

```bash
# 扩展应用实例（需要配置负载均衡）
docker-compose up -d --scale ruoyi-app=3
```

### 查看资源使用

```bash
# 查看容器资源使用情况
docker stats

# 查看特定服务
docker stats ruoyi-app ruoyi-mysql ruoyi-redis
```

## 相关文档

- [项目主 README](../README.md)
- [应用文档](../ruoyi-example/README.md)
- [数据库迁移文档](../database-migrations/README.md)

## 技术支持

如遇问题，请：
1. 查看服务日志：`./scripts/logs.sh`
2. 检查容器状态：`docker-compose ps`
3. 参考故障排查章节
