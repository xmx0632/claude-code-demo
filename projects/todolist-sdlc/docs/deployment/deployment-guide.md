# 系统部署说明

**项目**: TodoList 应用
**版本**: v1.0.0
**日期**: 2026-03-16

---

## 1. 部署架构

```
┌─────────────────────────────────────────────────────────┐
│                      负载均衡器                          │
│                     (Nginx/ALB)                         │
└─────────────────────────────────────────────────────────┘
                           │
           ┌───────────────┼───────────────┐
           ▼               ▼               ▼
    ┌─────────────┐ ┌─────────────┐ ┌─────────────┐
    │  前端容器   │ │  后端容器1  │ │  后端容器2  │
    │  (Nginx)    │ │  (Spring)   │ │  (Spring)   │
    └─────────────┘ └─────────────┘ └─────────────┘
                           │               │
           ┌───────────────┼───────────────┘
           ▼               ▼
    ┌─────────────┐ ┌─────────────┐
    │   MySQL     │ │   Redis     │
    │   主从      │ │   集群      │
    └─────────────┘ └─────────────┘
```

---

## 2. 环境要求

### 2.1 服务器配置

| 组件 | CPU | 内存 | 磁盘 | 数量 |
|------|-----|------|------|------|
| 应用服务器 | 2核 | 4GB | 50GB | 2+ |
| 数据库服务器 | 4核 | 8GB | 200GB SSD | 2 |
| Redis | 2核 | 4GB | 20GB | 3 |

### 2.2 软件要求

| 软件 | 版本 |
|------|------|
| Docker | 24.0+ |
| Docker Compose | 2.20+ |
| Nginx | 1.24+ |
| MySQL | 8.0+ |
| Redis | 7.0+ |
| JDK | 17 |

---

## 3. Docker 部署

### 3.1 构建镜像

```bash
# 后端
cd backend
docker build -t todolist-backend:v1.0.0 .

# 前端
cd frontend
docker build -t todolist-frontend:v1.0.0 .
```

### 3.2 Docker Compose 配置

```yaml
# docker-compose.yml
version: '3.8'

services:
  frontend:
    image: todolist-frontend:v1.0.0
    ports:
      - "80:80"
    depends_on:
      - backend
    networks:
      - todolist-network

  backend:
    image: todolist-backend:v1.0.0
    ports:
      - "8080:8080"
    environment:
      - SPRING_PROFILES_ACTIVE=prod
      - MYSQL_HOST=mysql
      - REDIS_HOST=redis
    depends_on:
      - mysql
      - redis
    networks:
      - todolist-network

  mysql:
    image: mysql:8.0
    environment:
      - MYSQL_ROOT_PASSWORD=${MYSQL_ROOT_PASSWORD}
      - MYSQL_DATABASE=todolist
    volumes:
      - mysql-data:/var/lib/mysql
    networks:
      - todolist-network

  redis:
    image: redis:7-alpine
    volumes:
      - redis-data:/data
    networks:
      - todolist-network

volumes:
  mysql-data:
  redis-data:

networks:
  todolist-network:
```

### 3.3 启动服务

```bash
# 启动所有服务
docker-compose up -d

# 查看服务状态
docker-compose ps

# 查看日志
docker-compose logs -f backend
```

---

## 4. 传统部署

### 4.1 后端部署

```bash
# 1. 构建 JAR
./mvnw clean package -DskipTests

# 2. 上传到服务器
scp target/todolist-1.0.0.jar user@server:/opt/todolist/

# 3. 启动服务
java -jar -Xms512m -Xmx1024m \
  -Dspring.profiles.active=prod \
  /opt/todolist/todolist-1.0.0.jar
```

### 4.2 前端部署

```bash
# 1. 构建
npm run build

# 2. 上传到 Nginx 目录
scp -r dist/* user@server:/var/www/todolist/

# 3. Nginx 配置
# /etc/nginx/sites-available/todolist
server {
    listen 80;
    server_name todolist.example.com;

    location / {
        root /var/www/todolist;
        try_files $uri $uri/ /index.html;
    }

    location /api {
        proxy_pass http://localhost:8080;
        proxy_set_header Host $host;
        proxy_set_header X-Real-IP $remote_addr;
    }
}
```

---

## 5. 数据库初始化

```bash
# 1. 创建数据库
mysql -u root -p -e "CREATE DATABASE todolist DEFAULT CHARACTER SET utf8mb4"

# 2. 执行初始化脚本
mysql -u root -p todolist < scripts/schema.sql

# 3. 创建应用用户
mysql -u root -p -e "CREATE USER 'todolist'@'%' IDENTIFIED BY 'password'"
mysql -u root -p -e "GRANT ALL ON todolist.* TO 'todolist'@'%'"
```

---

## 6. 健康检查

```bash
# 后端健康检查
curl http://localhost:8080/actuator/health

# 前端检查
curl http://localhost:80

# 数据库连接检查
mysql -h localhost -u todolist -p -e "SELECT 1"

# Redis 连接检查
redis-cli ping
```

---

## 7. 回滚方案

### 7.1 应用回滚

```bash
# Docker 回滚
docker-compose down
docker tag todolist-backend:v1.0.0 todolist-backend:previous
docker-compose up -d

# JAR 回滚
systemctl stop todolist
cp /opt/todolist/todolist-previous.jar /opt/todolist/todolist.jar
systemctl start todolist
```

### 7.2 数据库回滚

```bash
# 恢复备份
mysql -u root -p todolist < /backup/todolist_$(date +%Y%m%d).sql
```

---

## 8. 部署检查清单

- [ ] 环境变量配置完成
- [ ] 数据库连接正常
- [ ] Redis 连接正常
- [ ] 健康检查通过
- [ ] 日志输出正常
- [ ] 监控告警配置
- [ ] 备份策略确认
