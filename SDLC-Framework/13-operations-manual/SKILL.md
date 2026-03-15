# Operations Manual Skill

> 阶段 13: 运维手册

---

## 触发命令

```bash
/ops-manual
```

---

## 阶段目标

编写面向运维人员的系统运维文档。

---

## 输入

- 部署架构图
- 配置参数清单
- 监控指标

---

## 输出

| 产出物 | 文件 | 说明 |
|--------|------|------|
| 运维手册 | docs/operations/ops-manual.md | 运维操作指南 |
| 监控配置 | docs/operations/monitoring.md | 监控配置说明 |
| 故障处理 | docs/operations/troubleshooting.md | 故障排查手册 |

---

## 文档结构

```markdown
# 系统名称 运维手册

## 1. 系统概述
### 1.1 系统架构
### 1.2 技术栈
### 1.3 依赖服务

## 2. 部署配置
### 2.1 环境要求
### 2.2 配置参数
### 2.3 部署步骤

## 3. 日常运维
### 3.1 启停服务
### 3.2 日志管理
### 3.3 备份恢复

## 4. 监控告警
### 4.1 监控指标
### 4.2 告警规则
### 4.3 值班响应

## 5. 故障处理
### 5.1 常见问题
### 5.2 应急预案
### 5.3 联系方式
```

---

## 触发的 Guards

| Guard | 触发条件 |
|-------|----------|
| Infra Agent | 涉及部署配置 |
| Security Agent | 涉及安全配置 |

---

## 质量门禁

- [ ] 部署步骤已验证
- [ ] 监控配置完整
- [ ] 故障处理可执行
- [ ] 运维团队已确认

---

## 相关文件

- 模板目录: `13-operations-manual/templates/`
- 角色定义: `roles/backend-developer.md`, `roles/devops-engineer.md`
