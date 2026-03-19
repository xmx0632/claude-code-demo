---
name: sdlc-qa-puppeteer
description: 基于 Puppeteer 13.7.0 的 QA 测试工具，兼容 macOS 11。支持页面导航、元素交互、截图、JavaScript 执行等功能。
allowed-tools: ["Bash", "Read", "Write", "Edit", "Glob", "Grep", "AskUserQuestion"]
user-invocable: true
---

# browse: QA Testing & Dogfooding (Puppeteer)

基于 Puppeteer 13.7.0 的 headless 浏览器，**兼容 macOS 11**。支持完整的 UI 交互测试。

## ✅ macOS 11 兼容性

**组合方案**:
- **Puppeteer 13.7.0** - 旧版本，支持外部 Chromium
- **Chromium 1019** - Playwright 提供的兼容版本

## SETUP

**前置要求**: 首次使用需要安装 Bun 和依赖（~2 分钟）

```bash
# 检查 bun 是否已安装
if ! command -v bun &> /dev/null; then
  echo "正在安装 Bun..."
  curl -fsSL https://bun.sh/install | bash -s "bun-v1.0.2"
  export PATH="$HOME/.bun/bin:$PATH"
fi

# 检查 browse 工具状态
_ROOT=$(git rev-parse --show-toplevel 2>/dev/null)
B=""
[ -n "$_ROOT" ] && [ -x "$_ROOT/.claude/skills/sdlc-qa-puppeteer/browse" ] && B="$_ROOT/.claude/skills/sdlc-qa-puppeteer/browse"
if [ -x "$B" ]; then
  echo "READY: $B"
else
  echo "NEEDS_SETUP"
fi
```

If `NEEDS_SETUP`:
1. Tell user: "browse 工具需要首次设置（~2 分钟）。是否继续？"
2. Run: `cd <SKILL_DIR> && ./setup.sh`

## 核心命令

### Navigation

| 命令 | 说明 |
|------|------|
| `goto <url>` | 导航到 URL |
| `back` | 后退 |
| `forward` | 前进 |
| `reload` | 刷新页面 |
| `url` | 显示当前 URL |

### Reading

| 命令 | 说明 |
|------|------|
| `text` | 获取页面文本 |
| `html [selector]` | 获取 HTML |
| `title` | 获取页面标题 |

### Interaction

| 命令 | 说明 |
|------|------|
| `click <selector>` | 点击元素 |
| `fill <selector> <value>` | 填写输入框 |
| `type <text>` | 输入文本到焦点元素 |
| `select <selector> <value>` | 下拉选择 |
| `hover <selector>` | 悬停 |
| `press <key>` | 按键 (Enter, Tab, Escape, 等) |

### Inspection

| 命令 | 说明 |
|------|------|
| `screenshot [path]` | 截图 |
| `pdf [path]` | 保存为 PDF |
| `js <expression>` | 执行 JavaScript |
| `css <selector> <property>` | 获取计算样式 |
| `attrs <selector>` | 获取元素属性 |

### State

| 命令 | 说明 |
|------|------|
| `status` | 显示浏览器状态 |
| `stop` | 停止浏览器 |
| `cookies` | 显示 cookies |

## Core QA Patterns

```bash
# 1. 验证页面加载
$B goto https://yourapp.com
$B title                         # 页面标题
$B text                          # 内容加载?

# 2. 测试用户流程
$B goto https://app.com/login
$B fill "#email" "user@test.com"
$B fill "#password" "password"
$B click "button[type='submit']"

# 3. 验证操作结果
$B goto https://app.com/dashboard
$B screenshot /tmp/dashboard.png
$B js "document.querySelector('.welcome')?.textContent"

# 4. 表单测试
$B fill "input[name='username']" "testuser"
$B fill "input[name='email']" "test@example.com"
$B select "select[name='country']" "China"
$B click "button[type='submit']"

# 5. JavaScript 验证
$B js "document.title"
$B js "document.querySelectorAll('.error').length"
$B js "window.location.href"
```

## 技术架构

```
browse (Shell wrapper)
    ↓
browse-puppeteer.ts (TypeScript)
    ↓
Puppeteer 13.7.0
    ↓
Chromium 1019 (来自 Playwright)
```

### 版本兼容性

| 组件 | 版本 | 说明 |
|------|------|------|
| Puppeteer | 13.7.0 | 支持外部 Chromium 路径 |
| Chromium | 1019 (105.0.5195.19) | Playwright 提供，兼容 macOS 11 |
| Bun | 1.0.2 | JavaScript 运行时 |

## 使用示例

### 基本导航

```bash
# 访问页面
./browse goto https://example.com

# 获取页面标题
./browse title

# 获取页面文本
./browse text

# 截图
./browse screenshot /tmp/example.png
```

### 表单填写

```bash
# 填写登录表单
./browse goto https://example.com/login
./browse fill "#email" "test@example.com"
./browse fill "#password" "password123"
./browse click "button[type='submit']"

# 验证登录成功
./browse text | grep "Welcome"
```

### JavaScript 执行

```bash
# 执行 JavaScript
./browse js "document.title"
./browse js "window.location.href"
./browse js "document.querySelectorAll('a').length"

# 修改页面
./browse js "document.body.style.backgroundColor = 'red'"
./browse screenshot /tmp/red-bg.png
```

### TodoList 测试示例

```bash
# 测试 TodoList 应用
./browse goto http://localhost:5173
./browse screenshot /tmp/homepage.png

# 填写注册表单
./browse goto http://localhost:5173/register
./browse fill "input[type='email']" "test@example.com"
./browse fill "input[placeholder*='密码']" "Pass123"
./browse fill "input[placeholder*='确认']" "Pass123"
./browse fill "input[placeholder*='昵称']" "测试用户"
./browse screenshot /tmp/filled-form.png

# 提交表单
./browse click "button[type='submit']"
sleep 2
./browse screenshot /tmp/after-submit.png
```

## 与 sdlc-qa-browse 的对比

| 功能 | sdlc-qa-browse (Playwright) | sdlc-qa-puppeteer (Puppeteer 13) |
|------|----------------------------|------------------------------|
| macOS 11 兼容 | ❌ 需要 macOS 12+ | ✅ **兼容** |
| 截图 | ✅ | ✅ |
| 元素点击 | ✅ | ✅ |
| 表单填写 | ✅ | ✅ |
| @e refs | ✅ | ❌ (使用 CSS 选择器) |
| snapshot | ✅ | ❌ |
| 网络监听 | ✅ | ❌ |
| 控制台日志 | ✅ | ❌ |
| 多标签页 | ✅ | ❌ |
| JavaScript | ✅ | ✅ |

## 选择器说明

推荐使用稳定的 CSS 选择器：

```bash
# 推荐 - 使用 CSS 选择器
./browse click "button[type='submit']"
./browse fill "input[name='email']" "test@example.com"
./browse click ".btn-primary"
./browse click "#submit-button"

# 文本匹配 - 通过 JavaScript
./browse js "Array.from(document.querySelectorAll('button')).find(b => b.textContent === 'Submit').click()"
```

## 限制说明

由于使用 Puppeteer 13.7.0（而非最新版本），以下功能不支持：

1. **不支持 @e refs**: 使用 CSS 选择器或 JavaScript 代替
2. **不支持 snapshot**: 使用 `text`、`html`、`js` 代替
3. **无网络/控制台监听**: 需要这些功能请升级系统

## 故障排除

### Chromium 路径问题

```
Error: Executable not found
```

解决方法:
```bash
# 安装 Playwright Chromium
bunx playwright install chromium

# 验证路径
ls /Users/xmx0632/Library/Caches/ms-playwright/chromium-1019/chrome-mac/Chromium.app
```

### 元素未找到

```
Error: No node found for selector: .xyz
```

解决方法:
- 检查选择器是否正确
- 等待页面加载完成
- 使用 `js` 命令验证元素存在

## 相关资源

- **Puppeteer 13.7.0**: https://github.com/puppeteer/puppeteer/blob/v13.7.0
- **CSS Selectors**: https://developer.mozilla.org/en-US/docs/Web/CSS/CSS_Selectors
