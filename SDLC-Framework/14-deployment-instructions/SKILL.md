# Deployment Instructions Skill

> 阶段 14: 部署说明

---

## 触发命令

```bash
/deploy-guide
```

---

## 阶段目标

编写系统部署文档，确保部署过程可重复、可追溯。

---

## 输入

- 部署架构图
- 配置参数清单
- 运维要求

---

## 输出

| 产出物 | 文件 | 说明 |
|--------|------|------|
| 部署指南 | docs/deployment/deployment-guide.md | 部署操作手册 |
| 环境配置 | docs/deployment/env-config.md | 环境变量说明 |
| 回滚方案 | docs/deployment/rollback.md | 回滚操作步骤 |

---

## 部署流程

### 1. 预发布检查

```bash
# 检查代码质量
npm run lint
npm run test

# 检查安全漏洞
npm audit

# 检查依赖版本
npm outdated
```

### 2. 构建部署包

```bash
# 构建生产版本
npm run build

# 构建 Docker 镜像
docker build -t app:v1.0.0 .

# 推送到镜像仓库
docker push registry/app:v1.0.0
```

### 3. 部署到环境

```bash
# 部署到测试环境
kubectl apply -f k8s/test/

# 部署到生产环境
kubectl apply -f k8s/prod/
```

### 4. 部署验证

```bash
# 健康检查
curl https://api.example.com/health

# 冒烟测试
npm run test:smoke
```

---

## 触发的 Guards

| Guard | 触发条件 |
|-------|----------|
| Infra Agent | 部署配置 |
| Security Agent | 安全配置 |
| Performance Agent | 资源配置 |

---

## 质量门禁

- [ ] 部署步骤已验证
- [ ] 回滚方案已测试
- [ ] 监控已配置
- [ ] 文档已评审

---

## 相关文件

- 模板目录: `14-deployment-instructions/templates/`
- 角色定义: `roles/devops-engineer.md`
