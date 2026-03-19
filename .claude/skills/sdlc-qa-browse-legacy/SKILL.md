---
name: sdlc-qa-browse-legacy
description: 快速无头浏览器（兼容旧版 macOS），用于 QA 测试和站点验证。使用 Chromium CLI 进行截图，支持基本导航和状态管理。
allowed-tools: ["Bash", "Read", "AskUserQuestion"]
user-invocable: true
---

# browse: QA Testing & Dogfooding (Legacy)

兼容旧版 macOS 的 headless Chromium。使用 **Chromium CLI** 进行截图，状态持久化在 JSON 文件中。

## 架构说明

**macOS 11 兼容方案**:
- **Chromium CLI**: 用于截图（chromium-1019 兼容 macOS 11）
- **状态管理**: JSON 文件存储当前 URL 和历史记录
- **简化命令**: 不支持 Playwright 的 @e refs 等高级功能

## SETUP

**前置要求**: 首次使用需要安装 Bun（~30 秒）

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
[ -n "$_ROOT" ] && [ -x "$_ROOT/.claude/skills/sdlc-qa-browse-legacy/browse" ] && B="$_ROOT/.claude/skills/sdlc-qa-browse-legacy/browse"
if [ -x "$B" ]; then
  echo "READY: $B"
else
  echo "NEEDS_SETUP"
fi
```

If `NEEDS_SETUP`:
1. Tell user: "browse 工具需要首次编译（~30 秒）。是否继续？"
2. Run: `cd <SKILL_DIR> && ./build.sh`

## 核心命令

### Navigation

| 命令 | 说明 |
|------|------|
| `goto <url>` | 导航到 URL（仅更新状态） |
| `url` | 显示当前 URL |
| `status` | 显示当前状态 |
| `history` | 显示导航历史 |
| `clear` | 清除状态 |

### Screenshots

| 命令 | 说明 |
|------|------|
| `screenshot [path]` | 截取当前 URL 的截图 |
| `snap <url> [path]` | 导航并截图（一条命令） |
| `shot [path]` | screenshot 的别名 |

### Help

| 命令 | 说明 |
|------|------|
| `help` | 显示帮助信息 |

## Core QA Patterns

```bash
# 1. 快速截图（推荐）
$B snap https://example.com /tmp/screenshot.png

# 2. 分步操作
$B goto https://yourapp.com/login
$B screenshot /tmp/login-page.png

# 3. 批量截图
$B goto https://yourapp.com
for page in home about contact; do
  $B goto https://yourapp.com/$page
  $B screenshot /tmp/$page.png
done

# 4. 检查状态
$B status
$B history
```

## 使用示例

### 基本截图

```bash
# 截取 example.com
./browse snap https://example.com /tmp/example.png

# 使用默认路径（~/.gstack/screenshot-{timestamp}.png）
./browse screenshot

# 截取当前 URL
./browse goto https://example.com
./browse screenshot /tmp/example.png
```

### 批量测试

```bash
#!/bin/bash
# test-screenshots.sh

URLS=(
  "https://example.com"
  "https://example.com/about"
  "https://example.com/contact"
)

for url in "${URLS[@]}"; do
  filename=$(echo $url | sed 's|https://||g' | sed 's|/|-|g').png
  ./browse snap $url "/tmp/screenshots/$filename"
done
```

### 与 API 测试结合

```bash
#!/bin/bash
# ui-api-test.sh

API_URL="http://localhost:8080"
FRONTEND_URL="http://localhost:5173"

# 1. 通过 API 注册
curl -X POST $API_URL/api/auth/register \
  -H "Content-Type: application/json" \
  -d '{"email":"test@example.com","password":"Test123"}'

# 2. 截取登录页
./browse snap $FRONTEND_URL/login /tmp/login-page.png

# 3. 通过 API 登录
TOKEN=$(curl -X POST $API_URL/api/auth/login \
  -H "Content-Type: application/json" \
  -d '{"email":"test@example.com","password":"Test123"}' \
  | jq -r '.token')

# 4. 截取登录后的页面
./browse snap "$FRONTEND_URL/?token=$TOKEN" /tmp/dashboard.png
```

## 路径说明

- **截图默认路径**: `~/.gstack/screenshot-{timestamp}.png`
- **状态文件**: `~/.gstack/browse-hybrid.json`
- **支持相对路径**: `screenshot ./output.png` 会解析为绝对路径

## 限制说明

由于使用 Chromium CLI 而非 Playwright/CDP，以下功能**不支持**:

- ❌ `@e` refs（元素引用）
- ❌ `snapshot` 命令（页面结构分析）
- ❌ `click`、`fill` 等 UI 交互
- ❌ `text`、`html` 等页面内容读取
- ❌ `console`、`network` 等调试信息

### 替代方案

对于需要 UI 交互的场景，可以：

1. **使用 API 调用**: `curl` 进行操作，`browse` 进行截图
2. **使用 chromium-1019 CLI**: 直接调用 chromium 命令
3. **升级到 macOS 12+**: 使用完整版 Playwright

## 技术细节

### Chromium 版本

使用 Playwright 1.25.2 自带的 chromium-1019:
- 路径: `~/.cache/ms-playwright/chromium-1019/`
- 版本: Chromium 105.0.5195.19
- 兼容: macOS 11.x (Big Sur)

### 命令实现

```
browse → bun run src/browse-hybrid.ts
         ↓
    findChromium() → 查找 chromium 可执行文件
         ↓
    spawn(chromium, [--headless, --screenshot=...])
         ↓
    等待截图完成
```

### 状态文件格式

```json
{
  "currentUrl": "https://example.com",
  "history": ["https://example.com", "https://example.com/about"],
  "historyIndex": 1,
  "lastCommand": "goto"
}
```

## 故障排除

### Chromium 未找到

```
Error: Chromium not found
```

解决方法:
```bash
bunx playwright install chromium
```

### 截图超时

```
Screenshot timeout after 30000ms
```

解决方法:
- 检查 URL 是否可访问
- 检查网络连接
- 尝试使用更简单的 URL

### 截图为空白

可能原因:
- 页面使用 JavaScript 动态渲染
- 需要更长的加载时间
- 页面有反爬虫机制

解决方法:
- 增加 `--virtual-time-budget` 参数（需要修改源码）
- 使用 API 测试代替 UI 测试

## 测试报告

使用 `browse` 生成测试截图后，可以创建测试报告：

```bash
TIMESTAMP=$(date +"%Y-%m-%d-%H%M%S")
REPORT_DIR=".test-report/$TIMESTAMP"
mkdir -p "$REPORT_DIR"

# 运行测试
./browse snap https://example.com "$REPORT_DIR/01-homepage.png"
./browse snap https://example.com/about "$REPORT_DIR/02-about.png"

# 生成报告
cat > "$REPORT_DIR/TEST-REPORT.md" << EOF
# 测试报告

## 截图

- [01-homepage.png](01-homepage.png) - 首页
- [02-about.png](02-about.png) - 关于页面
EOF
```

## 相关资源

- **Playwright**: https://playwright.dev/
- **Chromium CLI**: https://www.chromium.org/developers/how-tos/run-chromium-with-flags
- **完整版 browse skill**: `sdlc-qa-browse` (需要 macOS 12+)
