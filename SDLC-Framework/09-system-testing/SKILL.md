# System Testing Skill

> 阶段 9: 系统测试

---

## 触发命令

```bash
/system-test
```

---

## 阶段目标

执行端到端系统测试，验证系统整体功能和非功能需求。

---

## 输入

- 完整系统 (阶段 6-8 产出)
- 测试计划 (阶段 1 需求)
- 性能基准

---

## 输出

| 产出物 | 目录 | 说明 |
|--------|------|------|
| 系统测试报告 | reports/system/ | 测试结果汇总 |
| 性能测试报告 | reports/performance/ | 性能基准数据 |
| 安全测试报告 | reports/security/ | 安全扫描结果 |

---

## 测试类型

### 1. 功能测试

- 端到端业务流程
- 用户场景覆盖
- 边界条件验证

### 2. 性能测试

- 负载测试
- 压力测试
- 并发测试

### 3. 安全测试

- 漏洞扫描
- 渗透测试
- 权限验证

### 4. 兼容性测试

- 浏览器兼容
- 设备兼容
- 操作系统兼容

---

## 执行步骤

```bash
# 1. 准备测试环境
npm run test:system:setup

# 2. 执行功能测试
npm run test:e2e

# 3. 执行性能测试
npm run test:performance

# 4. 执行安全扫描
npm run test:security

# 5. 生成报告
npm run report:generate
```

---

## 触发的 Guards

| Guard | 触发条件 |
|-------|----------|
| Security Agent | 安全测试阶段 |
| Performance Agent | 性能测试阶段 |
| Infra Agent | 部署环境测试 |

---

## 质量门禁

- [ ] 所有功能测试通过
- [ ] 性能满足基准
- [ ] 无高危安全漏洞
- [ ] 兼容性测试通过
- [ ] 测试报告已生成

---

## 相关文件

- 模板目录: `09-system-testing/templates/`
- 角色定义: `roles/qa-engineer.md`
- 工作流: `workflows/full-sdlc-workflow.md`
