# Incremental Upgrade Skill

> 阶段 15: 增量升级

---

## 触发命令

```bash
/upgrade-guide --version=<version>
```

---

## 阶段目标

编写版本升级指南，确保升级过程平滑、可回滚。

---

## 输入

- 版本变更记录
- 数据库变更脚本
- 配置变更说明

---

## 输出

| 产出物 | 文件 | 说明 |
|--------|------|------|
| 升级指南 | docs/upgrade/v{version}-upgrade.md | 升级操作步骤 |
| 变更日志 | CHANGELOG.md | 版本变更记录 |
| 迁移脚本 | db/migration/ | 数据迁移脚本 |

---

## 升级流程

### 1. 升级前准备

```bash
# 1. 备份数据库
pg_dump -h localhost -U postgres db > backup_$(date +%Y%m%d).sql

# 2. 记录当前版本
git tag -l | tail -1

# 3. 通知相关人员
# - 运维团队
# - 业务方
# - 用户 (如有停机)
```

### 2. 执行升级

```bash
# 1. 拉取新版本代码
git fetch origin
git checkout v{version}

# 2. 执行数据库迁移
flyway migrate

# 3. 更新配置
# - 更新环境变量
# - 更新配置文件

# 4. 重启服务
kubectl rollout restart deployment/app
```

### 3. 升级验证

```bash
# 1. 健康检查
curl https://api.example.com/health

# 2. 功能验证
npm run test:smoke

# 3. 监控观察
# - 错误率
# - 响应时间
# - 资源使用
```

### 4. 回滚方案

```bash
# 如需回滚
kubectl rollout undo deployment/app

# 回滚数据库
flyway undo -target={previous_version}
```

---

## 升级文档模板

```markdown
# v1.1.0 升级指南

## 升级内容
- 新增功能 A
- 修复问题 B
- 优化性能 C

## 兼容性
- 数据库: 需要迁移
- 配置: 新增 XXX 环境变量
- API: 废弃 /api/v1/old

## 升级步骤
1. 备份数据库
2. 执行迁移脚本
3. 更新配置
4. 重启服务
5. 验证功能

## 回滚方案
1. 回滚代码版本
2. 回滚数据库
3. 恢复配置

## 预计时间
- 停机时间: 约 10 分钟
- 总时间: 约 30 分钟
```

---

## 触发的 Guards

| Guard | 触发条件 |
|-------|----------|
| Infra Agent | 部署变更 |
| Security Agent | 安全相关升级 |
| Performance Agent | 性能相关升级 |

---

## 质量门禁

- [ ] 升级步骤已验证
- [ ] 回滚方案已测试
- [ ] 数据已备份
- [ ] 相关方已通知
- [ ] 监控已就绪

---

## 相关文件

- 模板目录: `15-incremental-upgrade/templates/`
- 角色定义: `roles/devops-engineer.md`, `roles/db-admin.md`
