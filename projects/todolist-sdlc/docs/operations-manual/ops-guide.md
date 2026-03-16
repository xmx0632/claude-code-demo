# TodoList 运维手册

**版本**: v1.0.0
**更新日期**: 2026-03-16

---

## 1. 系统架构

```
┌─────────────┐     ┌─────────────┐     ┌─────────────┐
│   Nginx     │────▶│  Spring     │────▶│   MySQL     │
│   (前端)    │     │  Boot       │     │   8.0       │
└─────────────┘     └─────────────┘     └─────────────┘
                          │
                          ▼
                    ┌─────────────┐
                    │   Redis     │
                    │   7.x       │
                    └─────────────┘
```

---

## 2. 服务配置

### 2.1 后端服务

| 配置项 | 开发环境 | 生产环境 |
|--------|----------|----------|
| 端口 | 8080 | 8080 |
| 数据库 | localhost:3306 | mysql.prod:3306 |
| Redis | localhost:6379 | redis.prod:6379 |
| 日志级别 | DEBUG | INFO |

### 2.2 前端服务

| 配置项 | 开发环境 | 生产环境 |
|--------|----------|----------|
| 端口 | 5173 | 80 |
| API 地址 | localhost:8080 | api.example.com |

---

## 3. 部署检查清单

### 3.1 部署前

- [ ] 数据库备份完成
- [ ] 配置文件检查
- [ ] 环境变量设置
- [ ] SSL 证书有效

### 3.2 部署后

- [ ] 服务健康检查通过
- [ ] API 接口测试通过
- [ ] 前端页面访问正常
- [ ] 日志无错误信息

---

## 4. 监控配置

### 4.1 健康检查端点

```bash
# 后端健康检查
curl http://localhost:8080/actuator/health

# 预期响应
{"status":"UP"}
```

### 4.2 监控指标

| 指标 | 告警阈值 | 处理方式 |
|------|----------|----------|
| CPU 使用率 | > 80% | 扩容或优化 |
| 内存使用率 | > 85% | 检查内存泄漏 |
| 响应时间 | > 500ms | 性能分析 |
| 错误率 | > 1% | 检查日志 |

---

## 5. 日志管理

### 5.1 日志位置

```
/var/log/todolist/
├── application.log    # 应用日志
├── access.log         # 访问日志
└── error.log          # 错误日志
```

### 5.2 日志轮转配置

```bash
# /etc/logrotate.d/todolist
/var/log/todolist/*.log {
    daily
    rotate 30
    compress
    missingok
    notifempty
}
```

### 5.3 常用日志查询

```bash
# 查看错误日志
tail -f /var/log/todolist/error.log

# 搜索特定用户操作
grep "userId=123" /var/log/todolist/application.log

# 统计今日请求量
grep "$(date +%Y-%m-%d)" /var/log/todolist/access.log | wc -l
```

---

## 6. 数据库维护

### 6.1 备份策略

```bash
# 每日全量备份
0 2 * * * mysqldump -u root -p todolist > /backup/todolist_$(date +\%Y\%m\%d).sql

# 保留 30 天
find /backup -name "todolist_*.sql" -mtime +30 -delete
```

### 6.2 数据恢复

```bash
# 恢复数据库
mysql -u root -p todolist < /backup/todolist_20260316.sql
```

---

## 7. 故障处理

### 7.1 服务无法启动

```bash
# 检查端口占用
lsof -i:8080

# 检查日志
tail -100 /var/log/todolist/error.log

# 检查数据库连接
mysql -h localhost -u root -p -e "SELECT 1"
```

### 7.2 数据库连接失败

```bash
# 检查 MySQL 状态
systemctl status mysql

# 检查连接数
mysql -e "SHOW PROCESSLIST"

# 检查最大连接数
mysql -e "SHOW VARIABLES LIKE 'max_connections'"
```

### 7.3 内存溢出

```bash
# 查看内存使用
free -m

# 查看 Java 进程内存
jstat -gc <pid>

# 生成堆转储
jmap -dump:format=b,file=heap.hprof <pid>
```

---

## 8. 安全加固

### 8.1 定期检查

- [ ] 更新依赖版本
- [ ] 检查安全漏洞
- [ ] 审计用户权限
- [ ] 检查日志异常

### 8.2 安全配置

```yaml
# 禁用不必要端点
management:
  endpoints:
    web:
      exposure:
        include: health,info

# 启用 HTTPS
server:
  ssl:
    enabled: true
```

---

## 9. 联系方式

| 角色 | 联系人 | 电话 |
|------|--------|------|
| 运维负责人 | - | - |
| DBA | - | - |
| 开发负责人 | - | - |
