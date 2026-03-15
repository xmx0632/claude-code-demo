# 部署指南

## 环境要求

### 服务器要求

| 组件 | 最低配置 | 推荐配置 |
|------|----------|----------|
| CPU | 2 核 | 4 核+ |
| 内存 | 4GB | 8GB+ |
| 磁盘 | 50GB | 100GB+ |
| OS | Linux | CentOS 7+ / Ubuntu 20.04+ |

### 软件要求

| 软件 | 版本 |
|------|------|
| JDK | 17 |
| Maven | 3.9+ |
| MySQL | 8.0+ |
| Redis | 7.0+ |
| Nginx | 1.20+ |

## 构建部署

### Maven 构建

```bash
# 清理并打包
mvn clean package -DskipTests

# 跳过测试打包（仅开发环境）
mvn clean package -DskipTests

# 完整构建
mvn clean package
```

### 生成的文件

```
target/
├── xxx-executable.jar      # 可执行 JAR
├── xxx.jar.original        # 原始 JAR
└── classes/                # 编译后的类
```

## 配置文件

### application-prod.yml

```yaml
server:
  port: 8080
  servlet:
    context-path: /api

spring:
  datasource:
    url: jdbc:mysql://${DB_HOST}:3306/${DB_NAME}?useSSL=true
    username: ${DB_USER}
    password: ${DB_PASSWORD}
  redis:
    host: ${REDIS_HOST}
    port: 6379
    password: ${REDIS_PASSWORD}

logging:
  level:
    root: INFO
    com.example: WARN
  file:
    name: /var/log/app/application.log
```

### 环境变量

```bash
# .env 文件
DB_HOST=localhost
DB_NAME=ruoyi
DB_USER=ruoyi
DB_PASSWORD=your_password

REDIS_HOST=localhost
REDIS_PASSWORD=your_redis_password

JWT_SECRET=your_jwt_secret_key
JWT_EXPIRATION=7200
```

## Docker 部署

### Dockerfile

```dockerfile
FROM openjdk:17-jdk-slim

WORKDIR /app

COPY target/xxx-executable.jar app.jar

EXPOSE 8080

ENTRYPOINT ["java", "-jar", "app.jar"]
```

### docker-compose.yml

```yaml
version: '3.8'

services:
  app:
    build: .
    ports:
      - "8080:8080"
    environment:
      - SPRING_PROFILES_ACTIVE=prod
    depends_on:
      - mysql
      - redis

  mysql:
    image: mysql:8.0
    environment:
      MYSQL_ROOT_PASSWORD: root123
      MYSQL_DATABASE: ruoyi
    volumes:
      - mysql-data:/var/lib/mysql

  redis:
    image: redis:7
    command: redis-server --requirepass redis123

volumes:
  mysql-data:
```

### 启动服务

```bash
# 构建并启动
docker-compose up -d

# 查看日志
docker-compose logs -f app

# 停止服务
docker-compose down
```

## Nginx 配置

```nginx
server {
    listen 80;
    server_name your-domain.com;

    location /api/ {
        proxy_pass http://localhost:8080/;
        proxy_set_header Host $host;
        proxy_set_header X-Real-IP $remote_addr;
        proxy_set_header X-Forwarded-For $proxy_add_x_forwarded_for;
    }

    location / {
        root /var/www/html;
        index index.html;
    }
}
```

## 健康检查

### Actuator 配置

```yaml
management:
  endpoints:
    web:
      exposure:
        include: health,info,metrics
  endpoint:
    health:
      show-details: always
```

### 健康检查端点

```
GET /actuator/health
```

## 回滚策略

```bash
# 1. 停止服务
systemctl stop app

# 2. 备份当前版本
cp /opt/app/app.jar /opt/app/app.jar.backup

# 3. 恢复上一版本
cp /opt/app/versions/app-v1.0.0.jar /opt/app/app.jar

# 4. 启动服务
systemctl start app

# 5. 验证
curl http://localhost:8080/actuator/health
```

## 监控告警

### Prometheus 配置

```yaml
management:
  endpoints:
    web:
      exposure:
        include: prometheus
  metrics:
    export:
      prometheus:
        enabled: true
```

### 告警规则

```yaml
# 应用响应时间 > 2s
- alert: HighResponseTime
  expr: http_server_requests_seconds_max > 2

# 错误率 > 5%
- alert: HighErrorRate
  expr: rate(http_server_requests_seconds_count{status=~"5.."}[5m]) > 0.05
```

---

**最后更新**: 2026-03-15
