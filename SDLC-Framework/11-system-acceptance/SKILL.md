# System Acceptance Skill

> 阶段 11: 系统验收

---

## 触发命令

```bash
/acceptance-test
```

---

## 阶段目标

执行用户验收测试 (UAT)，确保系统满足业务需求。

---

## 输入

- 完整系统
- 测试用例 (阶段 10 产出)
- 验收标准 (阶段 1 产出)

---

## 输出

| 产出物 | 文件 | 说明 |
|--------|------|------|
| 验收报告 | docs/acceptance/report.md | 验收结果 |
| 问题清单 | docs/acceptance/issues.md | 待解决问题 |
| 签字确认 | docs/acceptance/sign-off.md | 验收确认 |

---

## 验收流程

### 1. 准备验收环境

- 部署到验收环境
- 准备演示数据
- 通知干系人

### 2. 执行验收

- 演示核心功能
- 执行验收测试
- 记录问题

### 3. 问题处理

- 分类问题优先级
- 分配责任人
- 跟踪解决进度

### 4. 验收确认

- 所有 P0/P1 问题已解决
- 干系人签字确认
- 准备上线

---

## 质量门禁

- [ ] 所有验收用例通过
- [ ] P0/P1 问题已关闭
- [ ] 干系人已签字
- [ ] 上线计划已确认

---

## 相关文件

- 模板目录: `11-system-acceptance/templates/`
- 角色定义: `roles/product-manager.md`, `roles/qa-engineer.md`
