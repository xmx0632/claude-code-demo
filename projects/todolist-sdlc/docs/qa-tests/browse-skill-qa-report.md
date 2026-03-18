# 🔍 Browse Skill 自动化测试报告

**测试日期**: 2026-03-18
**测试工具**: gstack browse 二进制 (CLI)
**测试项目**: todolist-sdlc
**测试 URL**: http://localhost:5173

---

## 一、测试环境

| 项目 | 信息 |
|------|------|
| browse 二进制 | `/Volumes/macext/code/demo/claude-code-demo/.claude/skills/sdlc-qa-browse/dist/browse` |
| 前端 | Vue.js 3.4 + Vite 5.1 + Element Plus 2.6 |
| 后端 | Spring Boot 3.2.3 + H2 数据库 |

---

## 二、测试命令记录

### 2.1 页面导航

```bash
# 访问登录页
$B goto http://localhost:5173
# 输出: Navigated to http://localhost:5173 (200)

# 获取页面交互元素
$B snapshot -i
# 输出:
# @e1 [textbox] "邮箱"
# @e2 [textbox] "密码"
# @e3 [button] "登录"
# @e4 [link] "立即注册"
```

### 2.2 注册流程

```bash
# 点击注册链接
$B click @e4
# 输出: Clicked @e4 → now at http://localhost:5173/register

# 填写注册表单
$B fill @e1 "browse-test@example.com"
$B fill "#el-id-1603-11" "Test123456"  # 使用 ID 选择器避免歧义
$B fill @e3 "Test123456"
$B fill @e4 "Browse测试"

# 提交注册
$B click @e5
# 输出: Clicked @e5 → now at http://localhost:5173/login
```

### 2.3 登录流程

```bash
# 填写登录表单
$B fill @e1 "browse-test@example.com"
$B fill @e2 "Test123456"

# 提交登录
$B click @e3
# 输出: Clicked @e3 → now at http://localhost:5173/todos
```

### 2.4 创建任务

```bash
# 点击新建任务
$B click @e10

# 填写任务信息
$B fill @e12 "Browse自动化测试任务"
$B fill @e13 "使用 gstack browse 二进制进行自动化测试"

# 提交任务
$B click @e18
# 输出: Clicked @e18 → now at http://localhost:5173/todos
```

### 2.5 验证与检查

```bash
# 截图保存
$B screenshot /tmp/browse-qa-test-final.png

# 检查控制台错误
$B console --errors

# 检查网络请求
$B network
```

---

## 三、测试结果

| 步骤 | 操作 | 结果 | 耗时 |
|------|------|------|------|
| 1 | 访问登录页 | ✅ 成功 | ~100ms |
| 2 | 跳转注册页 | ✅ 成功 | ~50ms |
| 3 | 填写注册表单 | ✅ 成功 | ~50ms |
| 4 | 提交注册 | ✅ 成功 | ~200ms |
| 5 | 填写登录表单 | ✅ 成功 | ~50ms |
| 6 | 提交登录 | ✅ 成功 | ~200ms |
| 7 | 打开新建任务对话框 | ✅ 成功 | ~50ms |
| 8 | 填写任务信息 | ✅ 成功 | ~50ms |
| 9 | 提交任务 | ✅ 成功 | ~200ms |
| 10 | 截图保存 | ✅ 成功 | ~100ms |

**总耗时**: ~1秒（不含页面加载）

---

## 四、控制台警告

```
[warning] ElementPlusError: [el-radio] [API] label act as value is about to be deprecated
```

**建议**: 将 `el-radio` 的 `label` 属性改为 `value`

---

## 五、browse vs Playwright MCP 对比

| 维度 | gstack browse | Playwright MCP |
|------|---------------|----------------|
| 额度限制 | ✅ 无限制（本地） | ❌ 有 MCP 额度限制 |
| 响应速度 | ✅ ~100ms/命令 | ⚠️ 有网络延迟 |
| 状态持久 | ✅ cookies/登录态保持 | ⚠️ 需要额外处理 |
| 使用方式 | ✅ CLI 直观 | ⚠️ MCP 工具调用 |
| 适用场景 | 本地开发、CI/CD | 需要远程访问 |

**结论**: 优先使用 gstack browse 二进制进行浏览器自动化测试

---

## 六、截图

![测试结果](https://maas-log-prod.cn-wlcb.ufileos.com/anthropic/d48bab78-0252-47b1-af29-93edaf36e080/browse-qa-test-final.png)

---

*报告生成: Claude Code + gstack browse skill*
