# 部署文档模板

## 文档元数据

```yaml
document:
  name: "deployment.md"
  version: "v1.0"
  status: "draft"
  created_at: "YYYY-MM-DD"
  updated_at: "YYYY-MM-DD"
  owner: "QA"
  scenario: "new-project"

dependencies:
  - "requirements.md"
  - "architecture.md"
  - "test-plan.md"
blocking: []

reviewers: []
```

---

# 部署文档

## 1. 部署概述

### 1.1 部署目标

- 确保应用稳定上线
- 最小化停机时间
- 支持快速回滚
- 保证部署可追溯

### 1.2 部署架构

```
┌─────────────────────────────────────────────────────────┐
│                       负载均衡                           │
│                    (Nginx/SLB)                          │
└─────────────────────────────────────────────────────────┘
                          │
         ┌────────────────┼────────────────┐
         ▼                ▼                ▼
    ┌─────────┐      ┌─────────┐      ┌─────────┐
    │  App-1  │      │  App-2  │      │  App-3  │
    │ 8081    │      │ 8082    │      │ 8083    │
    └─────────┘      └─────────┘      └─────────┘
         │                │                │
         └────────────────┼────────────────┘
                          ▼
                   ┌─────────────┐
                   │   MySQL     │
                   │  Master     │
                   └─────────────┘
                          │
                   ┌─────────────┐
                   │   MySQL     │
                   │   Slave     │
                   └─────────────┘
                          │
                   ┌─────────────┐
                   │   Redis     │
                   │  Sentinel   │
                   └─────────────┘
```

## 2. 环境要求

### 2.1 硬件要求

| 环境 | CPU | 内存 | 磁盘 | 数量 |
|------|-----|------|------|------|
| 开发 | 2核 | 4GB | 50GB | 1 |
| 测试 | 4核 | 8GB | 100GB | 2 |
| 生产 | 8核 | 16GB | 200GB | 3+ |

### 2.2 软件依赖

| 软件 | 版本要求 | 用途 |
|------|---------|------|
| Java | 17+ | 应用运行环境 |
| Maven | 3.8+ | 构建工具 |
| MySQL | 8.0+ | 数据库 |
| Redis | 7.0+ | 缓存 |
| Nginx | 1.20+ | 反向代理 |
| Docker | 20.10+ | 容器化 (可选) |

### 2.3 网络端口

| 端口 | 协议 | 说明 |
|------|------|------|
| 8080 | HTTP | 应用端口 |
| 3306 | TCP | MySQL端口 |
| 6379 | TCP | Redis端口 |
| 80/443 | HTTP/HTTPS | 对外服务端口 |

## 3. 部署准备

### 3.1 配置文件

**application-prod.yml**

```yaml
server:
  port: 8080

spring:
  datasource:
    url: jdbc:mysql://${DB_HOST}:${DB_PORT}/${DB_NAME}
    username: ${DB_USER}
    password: ${DB_PASSWORD}
    hikari:
      maximum-pool-size: 20
      minimum-idle: 5

  redis:
    host: ${REDIS_HOST}
    port: ${REDIS_PORT}
    password: ${REDIS_PASSWORD}

logging:
  level:
    root: INFO
    com.example: DEBUG
  file:
    name: /var/log/app/application.log
```

### 3.2 环境变量

```bash
# .env.production
DB_HOST=mysql.prod.internal
DB_PORT=3306
DB_NAME=myapp_prod
DB_USER=app_user
DB_PASSWORD=encrypted_password

REDIS_HOST=redis.prod.internal
REDIS_PORT=6379
REDIS_PASSWORD=encrypted_password

JWT_SECRET=encrypted_secret
ENCRYPT_KEY=encrypted_key
```

### 3.3 数据库初始化

```bash
# 1. 创建数据库
mysql -u root -p << EOF
CREATE DATABASE IF NOT EXISTS myapp_prod
DEFAULT CHARACTER SET utf8mb4
DEFAULT COLLATE utf8mb4_unicode_ci;

CREATE USER 'app_user'@'%' IDENTIFIED BY 'password';
GRANT ALL PRIVILEGES ON myapp_prod.* TO 'app_user'@'%';
FLUSH PRIVILEGES;
EOF

# 2. 执行Flyway迁移
mvn flyway:migrate -Dflyway.configFiles=flyway-prod.conf
```

## 4. 部署步骤

### 4.1 构建应用

```bash
# 1. 拉取代码
git clone https://github.com/example/myapp.git
cd myapp
git checkout v1.0.0

# 2. 编译打包
mvn clean package -DskipTests -Pprod

# 3. 验证构建
ls -lh target/myapp-1.0.0.jar
```

### 4.2 部署应用

```bash
# 1. 停止旧版本 (滚动更新时跳过)
./scripts/stop.sh

# 2. 备份旧版本
cp myapp-current.jar myapp-backup-$(date +%Y%m%d%H%M%S).jar

# 3. 部署新版本
cp target/myapp-1.0.0.jar /opt/app/myapp-current.jar

# 4. 启动新版本
./scripts/start.sh

# 5. 健康检查
./scripts/health-check.sh
```

### 4.3 滚动更新

```bash
#!/bin/bash
# rolling-update.sh

SERVERS=("app-01" "app-02" "app-03")
NEW_VERSION="myapp-1.0.0.jar"

for server in "${SERVERS[@]}"; do
  echo "Deploying to $server..."

  # 1. 从负载均衡摘除
  kubectl drain $server --ignore-daemonsets

  # 2. 部署新版本
  scp $NEW_VERSION $server:/opt/app/myapp-current.jar
  ssh $server "./scripts/restart.sh"

  # 3. 健康检查
  ssh $server "./scripts/health-check.sh"

  # 4. 恢复流量
  kubectl uncordon $server

  echo "$server deployed successfully"
  sleep 30  # 等待稳定
done
```

## 5. 部署验证

### 5.1 健康检查

```bash
#!/bin/bash
# health-check.sh

HEALTH_URL="http://localhost:8080/actuator/health"
TIMEOUT=30
RETRY=3

for i in $(seq 1 $RETRY); do
  response=$(curl -s -o /dev/null -w "%{http_code}" $HEALTH_URL)

  if [ "$response" = "200" ]; then
    echo "Health check passed"
    exit 0
  fi

  echo "Health check failed, retrying... ($i/$RETRY)"
  sleep 5
done

echo "Health check failed after $RETRY attempts"
exit 1
```

### 5.2 冒烟测试

```bash
#!/bin/bash
# smoke-test.sh

BASE_URL="http://localhost:8080"

# 测试登录
echo "Testing login..."
token=$(curl -s -X POST "$BASE_URL/api/v1/auth/login" \
  -H "Content-Type: application/json" \
  -d '{"username":"admin","password":"admin123"}' \
  | jq -r '.data.token')

if [ -z "$token" ]; then
  echo "Login test failed"
  exit 1
fi

# 测试API
echo "Testing API..."
status=$(curl -s -o /dev/null -w "%{http_code}" \
  "$BASE_URL/api/v1/users" \
  -H "Authorization: Bearer $token")

if [ "$status" = "200" ]; then
  echo "Smoke test passed"
  exit 0
else
  echo "Smoke test failed"
  exit 1
fi
```

### 5.3 监控验证

| 指标 | 检查项 | 阈值 |
|------|--------|------|
| 应用 | 健康状态 | UP |
| 应用 | 错误率 | < 0.1% |
| 应用 | 响应时间 | P99 < 500ms |
| 数据库 | 连接数 | < 80% |
| Redis | 内存使用 | < 80% |

## 6. 回滚计划

### 6.1 回滚触发条件

- [ ] 健康检查失败
- [ ] 错误率 > 5%
- [ ] 响应时间 P99 > 2000ms
- [ ] 关键功能不可用
- [ ] 数据异常

### 6.2 回滚步骤

```bash
#!/bin/bash
# rollback.sh

echo "Starting rollback..."

# 1. 停止当前版本
./scripts/stop.sh

# 2. 恢复备份版本
LATEST_BACKUP=$(ls -t myapp-backup-*.jar | head -1)
cp $LATEST_BACKUP myapp-current.jar

# 3. 启动备份版本
./scripts/start.sh

# 4. 验证回滚
./scripts/health-check.sh
./scripts/smoke-test.sh

echo "Rollback completed: $LATEST_BACKUP"

# 5. 数据库回滚 (如需要)
mvn flyway:repair -Dflyway.configFiles=flyway-prod.conf
```

### 6.3 数据库回滚

```bash
# 回滚到指定版本
mvn flyway:undo \
  -Dflyway.configFiles=flyway-prod.conf \
  -Dflyway.target=1.0

# 查看回滚历史
mvn flyway:info -Dflyway.configFiles=flyway-prod.conf
```

## 7. 运维手册

### 7.1 日常运维

**查看日志**

```bash
# 实时查看应用日志
tail -f /var/log/app/application.log

# 查看错误日志
grep ERROR /var/log/app/application.log | tail -100

# 查看特定时间段的日志
sed -n '/2026-03-19 10:00/,/2026-03-19 11:00/p' application.log
```

**服务管理**

```bash
# 启动服务
./scripts/start.sh

# 停止服务
./scripts/stop.sh

# 重启服务
./scripts/restart.sh

# 查看服务状态
./scripts/status.sh
```

### 7.2 故障排查

**问题1: 应用启动失败**

```bash
# 1. 检查Java版本
java -version

# 2. 检查端口占用
netstat -tuln | grep 8080

# 3. 查看启动日志
tail -100 /var/log/app/startup.log

# 4. 检查数据库连接
mysql -h $DB_HOST -u $DB_USER -p$DB_PASSWORD -e "SELECT 1"
```

**问题2: 接口响应慢**

```bash
# 1. 查看应用线程状态
curl http://localhost:8080/actuator/threads

# 2. 查看数据库慢查询
mysql -e "SHOW PROCESSLIST;" | grep "Query time"

# 3. 查看Redis状态
redis-cli INFO stats
```

**问题3: 内存溢出**

```bash
# 1. 查看堆内存使用
jmap -heap $(pgrep -f myapp)

# 2. 导出堆转储
jmap -dump:format=b,file=heap.hprof $(pgrep -f myapp)

# 3. 分析GC日志
grep "GC" /var/log/app/gc.log | tail -100
```

### 7.3 监控告警

**监控指标**

| 类型 | 指标 | 告警阈值 | 处理措施 |
|------|------|---------|---------|
| 应用 | CPU使用率 | > 80% | 扩容 |
| 应用 | 堆内存使用 | > 85% | 分析内存 |
| 应用 | 线程数 | > 500 | 检查线程池 |
| 应用 | GC时间 | > 1s | 优化GC |
| 数据库 | 慢查询 | > 100 | 优化SQL |
| 数据库 | 连接数 | > 80% | 扩容连接池 |

**告警通知**

```yaml
# alerting.yml
alerts:
  - name: HighErrorRate
    condition: error_rate > 5%
    duration: 5m
    actions:
      - type: email
        to: ops@example.com
      - type: webhook
        url: https://hooks.example.com/alert
      - type: dingtalk
        webhook: https://oapi.dingtalk.com/robot/send
```

## 8. 备份与恢复

### 8.1 备份策略

| 类型 | 频率 | 保留期 | 存储位置 |
|------|------|--------|---------|
| 数据库全量 | 每天 | 30天 | 对象存储 |
| 数据库增量 | 每小时 | 7天 | 对象存储 |
| 应用日志 | 每天 | 90天 | 对象存储 |
| 配置文件 | 每次变更 | 永久 | Git仓库 |

### 8.2 备份脚本

```bash
#!/bin/bash
# backup.sh

BACKUP_DIR="/backup/$(date +%Y%m%d)"
mkdir -p $BACKUP_DIR

# 1. 备份数据库
mysqldump -h $DB_HOST -u $DB_USER -p$DB_PASSWORD \
  --single-transaction \
  --routines \
  --triggers \
  myapp_prod | gzip > $BACKUP_DIR/database.sql.gz

# 2. 备份Redis
redis-cli --rdb $BACKUP_DIR/redis.rdb

# 3. 备份配置文件
tar -czf $BACKUP_DIR/config.tar.gz /opt/app/config/

# 4. 上传到对象存储
aws s3 sync $BACKUP_DIR s3://myapp-backup/$(date +%Y%m%d)/

echo "Backup completed: $BACKUP_DIR"
```

### 8.3 恢复流程

```bash
#!/bin/bash
# restore.sh

BACKUP_DATE=$1  # 格式: YYYYMMDD

if [ -z "$BACKUP_DATE" ]; then
  echo "Usage: restore.sh <YYYYMMDD>"
  exit 1
fi

# 1. 从对象存储下载
aws s3 sync s3://myapp-backup/$BACKUP_DATE/ /tmp/restore/

# 2. 停止应用
./scripts/stop.sh

# 3. 恢复数据库
gunzip < /tmp/restore/database.sql.gz | \
  mysql -h $DB_HOST -u $DB_USER -p$DB_PASSWORD myapp_prod

# 4. 恢复Redis
redis-cli --rdb /tmp/restore/redis.rdb

# 5. 启动应用
./scripts/start.sh

echo "Restore completed"
```

## 9. 附录

### 9.1 端口映射

| 服务 | 内部端口 | 外部端口 |
|------|---------|---------|
| 应用 | 8080 | 80/443 |
| MySQL | 3306 | - |
| Redis | 6379 | - |

### 9.2 目录结构

```
/opt/app/
├── myapp-current.jar       # 当前运行版本
├── myapp-backup/           # 历史版本备份
├── config/                 # 配置文件
├── scripts/                # 运维脚本
├── logs/                   # 日志文件
└── temp/                   # 临时文件
```

### 9.3 相关链接

- 架构文档: [链接]
- API文档: [链接]
- 监控大盘: [链接]
- 告警规则: [链接]
