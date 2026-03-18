# TodoList SDLC 待办事项

> 最后更新: 2026-03-18

---

## 待修复问题

### P3 - 低优先级

| 序号 | 问题 | 位置 | 状态 | 发现日期 |
|------|------|------|------|----------|
| 1 | `favicon.ico` 404 错误 | `frontend/public/` | 待修复 | 2026-03-18 |
| 2 | ElementPlus API 弃用警告 | `frontend/src/` | 待修复 | 2026-03-18 |

#### 问题详情

**1. favicon.ico 缺失**

- **现象**: 控制台报错 `Failed to load resource: 404 (Not Found) @ http://localhost:5173/favicon.ico`
- **影响**: 浏览器标签页无图标
- **修复方案**: 在 `frontend/public/` 目录添加 `favicon.ico` 文件

**2. ElementPlus API 弃用警告**

- **现象**: 控制台警告 `[el-radio] [API] label act as value is about to be deprecated in version 3.0.0`
- **影响**: 未来 ElementPlus 3.0 升级时可能破坏
- **修复方案**: 将 `el-radio` 组件的 `label` 属性改为 `value`

---

## 功能增强

| 序号 | 功能 | 优先级 | 状态 |
|------|------|--------|------|
| - | 暂无 | - | - |

---

## 已完成

| 序号 | 功能 | 完成日期 |
|------|------|----------|
| 1 | QA 自动化测试 | 2026-03-18 |

---

## 参考资料

- [ElementPlus Radio 组件文档](https://element-plus.org/en-US/component/radio.html)
