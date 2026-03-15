# Infra Agent - 基础设施约束智能体

> 自动注入部署、监控、资源限制要求

---

## 触发条件

- 涉及新服务/模块部署
- 涉及容器化配置
- 涉及云资源配置
- 涉及监控告警
- 涉及 CI/CD 流程

---

## 部署规范

### 容器化要求

```dockerfile
# ✅ 正确：多阶段构建，安全基础镜像
FROM node:20-alpine AS builder
WORKDIR /app
COPY package*.json ./
RUN npm ci --only=production

FROM node:20-alpine AS runtime
WORKDIR /app
COPY --from=builder /app/node_modules ./node_modules
COPY dist ./dist

# 非 root 用户
RUN addgroup -g 1001 -S appgroup && \
    adduser -u 1001 -S appuser -G appgroup
USER appuser

EXPOSE 3000
CMD ["node", "dist/main.js"]

# ❌ 错误：使用 latest 标签，root 用户
FROM node:latest
COPY . .
RUN npm install
CMD ["npm", "start"]
```

### 资源限制

```yaml
# Kubernetes 资源配置
resources:
  requests:
    cpu: "100m"
    memory: "128Mi"
  limits:
    cpu: "500m"
    memory: "512Mi"

# 健康检查
livenessProbe:
  httpGet:
    path: /health
    port: 3000
  initialDelaySeconds: 30
  periodSeconds: 10

readinessProbe:
  httpGet:
    path: /ready
    port: 3000
  initialDelaySeconds: 5
  periodSeconds: 5
```

### 环境变量管理

```yaml
# ✅ 正确：敏感信息用 Secret
env:
  - name: NODE_ENV
    value: "production"
  - name: DATABASE_URL
    valueFrom:
      secretKeyRef:
        name: app-secrets
        key: database-url

# ❌ 错误：明文敏感信息
env:
  - name: DATABASE_URL
    value: "postgresql://user:password@host:5432/db"
```

---

## 监控要求

### 必须暴露的指标

| 指标类型 | 指标名称 | 说明 |
|----------|----------|------|
| 健康检查 | `/health` | 服务存活状态 |
| 就绪检查 | `/ready` | 服务就绪状态 |
| 指标暴露 | `/metrics` | Prometheus 格式 |

### 关键监控指标

```typescript
// ✅ 正确：暴露关键指标
import client from 'prom-client';

// 请求计数
const httpRequestCounter = new client.Counter({
  name: 'http_requests_total',
  help: 'Total HTTP requests',
  labelNames: ['method', 'path', 'status']
});

// 请求耗时
const httpRequestDuration = new client.Histogram({
  name: 'http_request_duration_seconds',
  help: 'HTTP request duration',
  labelNames: ['method', 'path'],
  buckets: [0.01, 0.05, 0.1, 0.5, 1, 5]
});

// 在中间件中记录
app.use((req, res, next) => {
  const start = Date.now();
  res.on('finish', () => {
    httpRequestCounter.inc({ method: req.method, path: req.path, status: res.statusCode });
    httpRequestDuration.observe(
      { method: req.method, path: req.path },
      (Date.now() - start) / 1000
    );
  });
  next();
});
```

### 告警规则

```yaml
# Prometheus 告警规则示例
groups:
  - name: app-alerts
    rules:
      - alert: HighErrorRate
        expr: rate(http_requests_total{status=~"5.."}[5m]) > 0.1
        for: 5m
        labels:
          severity: critical
        annotations:
          summary: "高错误率告警"

      - alert: HighLatency
        expr: histogram_quantile(0.95, rate(http_request_duration_seconds_bucket[5m])) > 1
        for: 5m
        labels:
          severity: warning
        annotations:
          summary: "高延迟告警"
```

---

## CI/CD 规范

### 构建流程

```yaml
# .github/workflows/deploy.yml
name: Deploy

on:
  push:
    branches: [main]

jobs:
  build:
    runs-on: ubuntu-latest
    steps:
      - uses: actions/checkout@v4

      - name: Build
        run: npm ci && npm run build

      - name: Test
        run: npm test

      - name: Security Scan
        run: npm audit --audit-level=high

      - name: Build Image
        run: docker build -t app:${{ github.sha }} .

      - name: Push to Registry
        run: docker push registry/app:${{ github.sha }}
```

### 部署检查清单

- [ ] 单元测试通过
- [ ] 代码审查通过
- [ ] 安全扫描无高危漏洞
- [ ] 镜像构建成功
- [ ] 镜像已推送到仓库
- [ ] 部署到预发布环境
- [ ] 烟雾测试通过
- [ ] 部署到生产环境

---

## 日志规范

### 结构化日志

```typescript
// ✅ 正确：结构化日志
import pino from 'pino';

const logger = pino({
  level: process.env.LOG_LEVEL || 'info',
  formatters: {
    level: (label) => ({ level: label })
  }
});

logger.info({
  action: 'user_login',
  user_id: user.id,
  ip: req.ip,
  user_agent: req.headers['user-agent']
}, 'User logged in');

// ❌ 错误：非结构化日志
console.log(`User ${user.email} logged in from ${req.ip}`);
```

### 日志级别

| 级别 | 使用场景 |
|------|----------|
| error | 错误，需要立即处理 |
| warn | 警告，可能有问题 |
| info | 关键业务事件 |
| debug | 调试信息（生产环境禁用） |

---

## 成本优化

### 资源规划

| 服务类型 | CPU 请求 | 内存请求 | 实例数 |
|----------|----------|----------|--------|
| API 服务 | 100m | 128Mi | 2-10 |
| 后台任务 | 200m | 256Mi | 1-5 |
| 定时任务 | 100m | 128Mi | 1 |

### 自动伸缩

```yaml
# HPA 配置
apiVersion: autoscaling/v2
kind: HorizontalPodAutoscaler
metadata:
  name: app-hpa
spec:
  scaleTargetRef:
    apiVersion: apps/v1
    kind: Deployment
    name: app
  minReplicas: 2
  maxReplicas: 10
  metrics:
    - type: Resource
      resource:
        name: cpu
        target:
          type: Utilization
          averageUtilization: 70
```

---

## 使用方式

### 在 SDLC 阶段中触发

```markdown
<!-- Claude Code 执行部署阶段时 -->

当前阶段: 部署上线
服务: user-service

🤖 Infra Agent 自动注入:

部署要求:
- [ ] Dockerfile 多阶段构建
- [ ] 非 root 用户运行
- [ ] 资源限制配置 (CPU: 100-500m, Memory: 128-512Mi)
- [ ] 健康检查端点 (/health, /ready)
- [ ] Prometheus 指标暴露 (/metrics)
- [ ] 结构化日志输出

CI/CD 检查:
- [ ] 单元测试
- [ ] 安全扫描
- [ ] 镜像扫描
```

---

## 输出模板

```markdown
## 🏗️ Infra Agent 报告

**检查范围**: [服务/模块]

### 部署配置检查

| 检查项 | 状态 | 说明 |
|--------|------|------|
| Dockerfile | ✅ | 多阶段构建，非 root |
| 资源限制 | ⚠️ | 未配置 limits |
| 健康检查 | ✅ | 已配置 /health |
| 监控指标 | ❌ | 缺少 /metrics |

### 建议修复

1. **[高优先级]** 添加资源限制
   ```yaml
   resources:
     limits:
       cpu: "500m"
       memory: "512Mi"
   ```

2. **[中优先级]** 添加 Prometheus 指标

### CI/CD 状态

| 阶段 | 状态 |
|------|------|
| 构建 | ✅ 通过 |
| 测试 | ✅ 通过 |
| 安全扫描 | ⚠️ 1 个中危 |
| 部署 | 🔄 进行中 |
```
