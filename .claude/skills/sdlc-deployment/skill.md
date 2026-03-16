---
name: deployment
description: 部署阶段，生成部署指南、配置文件和升级说明。部署上线时使用。
allowed-tools: ["Read", "Write", "Edit", "Glob", "Grep", "Bash"]
user-invocable: true
---

# 部署助手

执行 SDLC 阶段 14-15：部署和升级，生成部署文档和配置。

## 阶段目标

生成部署指南、Docker 配置、升级说明等。

## 输出

| 产出物 | 文件路径 | 说明 |
|--------|----------|------|
| 部署指南 | `docs/deployment/deployment-guide.md` | 部署步骤 |
| Docker 配置 | `docker/Dockerfile` | 容器构建 |
| Docker Compose | `docker/docker-compose.yml` | 服务编排 |
| 升级指南 | `docs/deployment/upgrade-guide.md` | 版本升级 |
| CI/CD 配置 | `.github/workflows/` | 自动化流水线 |

## 执行步骤

### 1. 部署准备

```markdown
- 确认部署环境
- 准备配置文件
- 检查依赖服务
- 准备数据库迁移脚本
```

### 2. 容器化

```markdown
- 编写 Dockerfile
- 配置 docker-compose
- 设置环境变量
- 配置数据卷
```

### 3. CI/CD 配置

```markdown
- 配置构建流水线
- 配置测试流水线
- 配置部署流水线
- 配置通知
```

### 4. 部署文档

```markdown
- 编写部署步骤
- 记录配置说明
- 编写回滚流程
- 编写升级流程
```

## 使用方法

### 生成部署指南

```
/deployment --type guide
```

### 生成 Docker 配置

```
/deployment --type docker
```

### 生成 CI/CD 配置

```
/deployment --type cicd
```

### 生成所有部署文件

```
/deployment --all
```

## 配置模板

### Dockerfile

```dockerfile
FROM eclipse-temurin:17-jre-alpine

WORKDIR /app

COPY target/app.jar app.jar

ENV TZ=Asia/Shanghai
ENV JAVA_OPTS="-Xms256m -Xmx512m"

EXPOSE 8080

ENTRYPOINT ["sh", "-c", "java $JAVA_OPTS -jar app.jar"]
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
      - DB_HOST=mysql
    depends_on:
      - mysql
      - redis
    networks:
      - app-network

  mysql:
    image: mysql:8.0
    environment:
      - MYSQL_ROOT_PASSWORD=root123
      - MYSQL_DATABASE=app_db
    volumes:
      - mysql-data:/var/lib/mysql
    networks:
      - app-network

  redis:
    image: redis:7-alpine
    networks:
      - app-network

volumes:
  mysql-data:

networks:
  app-network:
```

### GitHub Actions

```yaml
name: CI/CD Pipeline

on:
  push:
    branches: [main]
  pull_request:
    branches: [main]

jobs:
  build:
    runs-on: ubuntu-latest
    steps:
      - uses: actions/checkout@v4

      - name: Set up JDK 17
        uses: actions/setup-java@v4
        with:
          java-version: '17'
          distribution: 'temurin'

      - name: Build with Maven
        run: mvn clean package -DskipTests

      - name: Run tests
        run: mvn test

      - name: Build Docker image
        run: docker build -t app:${{ github.sha }} .

      - name: Push to registry
        run: |
          docker tag app:${{ github.sha }} registry.example.com/app:latest
          docker push registry.example.com/app:latest
```

## 触发的 Guards

| Guard | 触发条件 |
|-------|----------|
| Infra Agent | 涉及部署配置 |
| Security Agent | 涉及密钥配置 |

## 质量门禁

- [ ] 部署指南已完成
- [ ] Docker 配置已测试
- [ ] CI/CD 流水线已配置
- [ ] 升级指南已编写
- [ ] 回滚流程已定义

## 部署检查清单

### 部署前

- [ ] 代码已合并到主分支
- [ ] 所有测试已通过
- [ ] 数据库迁移已准备
- [ ] 配置文件已更新
- [ ] 回滚方案已准备

### 部署中

- [ ] 服务健康检查通过
- [ ] 日志输出正常
- [ ] 监控指标正常
- [ ] 功能验证通过

### 部署后

- [ ] 用户验收通过
- [ ] 性能指标达标
- [ ] 文档已更新
- [ ] 发布说明已发送

## 相关 Skills

- `/flyway-migration` - 数据库迁移
- `/code-review` - 代码审查

## 完成标志

部署完成后，SDLC 流程全部结束。项目进入运维阶段。
