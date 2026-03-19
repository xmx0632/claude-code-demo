---
name: sdlc-qa-browse
description: 快速无头浏览器，用于 QA 测试和站点验证。导航任意 URL，与元素交互，验证页面状态，对比操作前后差异，截取带注释的截图，检查响应式布局，测试表单和上传。
allowed-tools: ["Bash", "Read", "AskUserQuestion"]
user-invocable: true
---

# browse: QA Testing & Dogfooding

Persistent headless Chromium. First call auto-starts (~3s), then ~100ms per command. State persists between calls (cookies, tabs, login sessions).

## SETUP

**前置要求**: 首次使用需要编译 browse 工具（~30 秒）

```bash
# 1. 先检查 browse 工具是否已存在且可执行
_ROOT=$(git rev-parse --show-toplevel 2>/dev/null)
B=""
[ -n "$_ROOT" ] && [ -x "$_ROOT/.claude/skills/sdlc-qa-browse/dist/browse" ] && B="$_ROOT/.claude/skills/sdlc-qa-browse/dist/browse"

if [ -x "$B" ]; then
  echo "READY: $B"
else
  # 2. browse 不存在，检查并安装 Bun（编译需要）
  if ! command -v bun &> /dev/null; then
    echo "正在安装 Bun（用于编译 browse）..."
    curl -fsSL https://bun.sh/install | bash
    export PATH="$HOME/.bun/bin:$PATH"
  fi

  # 3. 编译 browse 工具
  echo "NEEDS_SETUP"
fi
```

If `NEEDS_SETUP`:
1. Tell user: "browse 工具需要首次编译（~30 秒）。是否继续？"
2. Run: `cd <SKILL_DIR> && ./build.sh`

## Core QA Patterns

```bash
# 1. 验证页面加载
$B goto https://yourapp.com
$B text                          # 内容加载?
$B console                       # JS 错误?
$B network                       # 失败的请求?
$B is visible ".main-content"    # 关键元素存在?

# 2. 测试用户流程
$B goto https://app.com/login
$B snapshot -i                   # 查看所有可交互元素
$B fill @e3 "user@test.com"      # 使用 @e refs
$B fill @e4 "password"
$B click @e5
$B snapshot -D                   # diff: 变化对比

# 3. 验证操作结果
$B snapshot                      # baseline
$B click @e3
$B snapshot -D                   # unified diff

# 4. 截图证据
$B screenshot /tmp/bug.png
$B snapshot -i -a -o /tmp/annotated.png

# 5. 断言元素状态
$B is visible ".modal"
$B is enabled "#submit-btn"
$B is disabled "#submit-btn"
$B is checked "#agree-checkbox"
$B is editable "#name-field"
$B is focused "#search-input"

# 6. 响应式测试
$B responsive /tmp/layout        # mobile + tablet + desktop

# 7. 文件上传测试
$B upload "#file-input" /path/to/file.pdf

# 8. 对话框测试
$B dialog-accept "yes"
$B click "#delete-button"
$B dialog
```

## Snapshot (核心工具)

```
-i        --interactive           只显示可交互元素 (@e refs)
-c        --compact               紧凑模式
-d <N>    --depth                 限制树深度
-s <sel>  --selector              限定 CSS 选择器范围
-D        --diff                  与上一次 snapshot 对比
-a        --annotate              生成带标注的截图
-o <path> --output                标注截图输出路径
-C        --cursor-interactive    显示 cursor:pointer 等元素 (@c refs)
```

**Ref 使用**: `@e1, @e2...` (interactive), `@c1, @c2...` (cursor)

> ⚠️ **重要**: `@e` refs 会随页面变化而改变，**推荐使用稳定的 CSS 选择器**

```bash
# 推荐: 使用 CSS 选择器
$B fill "input[placeholder*='邮箱']" "test@example.com"
$B click "button:has-text('登录')"

# 不推荐: 使用 @e refs（可能变化）
$B snapshot -i > /dev/null  # 必须先刷新
$B fill @e1 "test@example.com"  # @e1 可能指向不同元素
```

## 注意事项

### 1. 命令使用

| 错误写法 | 正确写法 |
|---------|---------|
| `gstack browse goto <url>` | `browse goto <url>` |
| `gstack browse fill <sel> <val>` | `browse fill <sel> <val>` |

使用 `$B` 变量或 `browse` 别名，不是 `gstack browse`。

### 2. 截图路径限制

browse 命令的 screenshot 功能有路径限制，只能保存到：
- `/tmp` 目录
- skill 目录 (`/path/to/.claude/skills/sdlc-qa-browse/`)

**解决方案**: 先保存到 `/tmp`，再复制到目标目录

```bash
screenshot() {
  local name=$1
  local tmp_path="/tmp/screenshots/$name.png"
  local final_path="$TEST_DIR/$name.png"
  $B screenshot "$tmp_path"
  cp "$tmp_path" "$final_path"
}
```

### 3. 元素选择器稳定性

**CSS 选择器优先级**:
1. 文本匹配: `button:has-text('登录')`
2. 属性选择: `input[placeholder*='邮箱']`
3. 类选择器: `.el-button--primary`
4. ID 选择器: `#username`

**避免使用**:
- `@e` refs (会变化)
- 过于通用的选择器: `input`, `button`

### 4. 页面跳转等待

```bash
# 导航后等待页面加载
$B goto $FRONTEND_URL/login
sleep 2  # 简单等待
# 或
$B wait ".login-form" 5  # 等待特定元素
```

## 命令速查表

### Navigation
| 命令 | 说明 |
|------|------|
| `goto <url>` | 导航到 URL |
| `back` / `forward` | 历史前进后退 |
| `reload` | 重新加载 |

### Reading
| 命令 | 说明 |
|------|------|
| `text` | 页面纯文本 |
| `html [sel]` | innerHTML |
| `links` | 所有链接 |
| `forms` | 表单字段 JSON |

### Interaction
| 命令 | 说明 |
|------|------|
| `click <sel>` | 点击 |
| `fill <sel> <val>` | 填写输入 |
| `select <sel> <val>` | 下拉选择 |
| `type <text>` | 输入到焦点元素 |
| `hover <sel>` | 悬停 |
| `press <key>` | 按键 (Enter/Tab/Esc/ArrowUp 等) |
| `upload <sel> <file>` | 上传文件 |
| `wait <sel>` | 等待元素 (15s timeout) |

### Inspection
| 命令 | 说明 |
|------|------|
| `is <prop> <sel>` | 状态检查 (visible/enabled/disabled/checked/editable/focused) |
| `js <expr>` | 执行 JS 表达式 |
| `eval <file>` | 执行 JS 文件 |
| `css <sel> <prop>` | 计算样式 |
| `attrs <sel>` | 元素属性 JSON |
| `console [--errors]` | 控制台消息 |
| `network [--clear]` | 网络请求 |
| `perf` | 页面加载时序 |

### Visual
| 命令 | 说明 |
|------|------|
| `screenshot [sel] [path]` | 截图 |
| `responsive [prefix]` | 响应式截图 (3个尺寸) |
| `pdf [path]` | 保存为 PDF |
| `diff <url1> <url2>` | 文本对比 |

### Tabs
| 命令 | 说明 |
|------|------|
| `newtab [url]` | 新标签页 |
| `tabs` | 列出标签页 |
| `tab <id>` | 切换标签页 |
| `closetab [id]` | 关闭标签页 |

### Server
| 命令 | 说明 |
|------|------|
| `status` | 健康检查 |
| `stop` / `restart` | 停止/重启服务器 |

## 截图展示

使用 `$B screenshot` 或 `$B snapshot -a -o` 后，**务必用 Read 工具读取 PNG 文件**，否则用户看不到截图。

## 测试报告

QA 测试完成后应生成结构化报告，包含：
- `TEST-PLAN.md` - 测试用例计划（先制定）⭐
- `TEST-REPORT.md` - 测试报告
- `test-script.sh` - 测试复现脚本 ⭐
- `test-output.log` - 测试执行日志
- `screenshot-*.png` - 测试截图

详见: `.claude/skills/sdlc-qa-browse/REPORT-TEMPLATE.md`

**核心原则**:
1. **browse 命令优先**: 当 browse 可执行时，直接使用，不进行编译
2. **测试用例驱动**: 按照测试用例计划执行测试
3. **脚本可复现**: 测试脚本能够独立复现测试场景
4. **路径明确**: 所有文件路径使用绝对路径
5. **使用 gstack browse**: 不使用 MCP Playwright

**报告结构** (按时间戳分目录):
```
.test-report/
├── 2026-03-19-170034/          # 时间戳目录 (YYYY-MM-DD-HHMMSS)
│   ├── TEST-PLAN.md              # 测试用例计划（先制定）⭐
│   ├── TEST-REPORT.md             # 测试报告
│   ├── test-script.sh             # 测试复现脚本 ⭐
│   ├── test-output.log            # 测试执行日志
│   ├── screenshot-1.png           # 测试截图
│   └── ...
├── 2026-03-19-170145/
│   └── ...
└── LATEST -> 2026-03-19-170145  # 符号链接指向最新测试
```

### 测试流程

**第一步: 制定测试用例计划**

在开始测试前，先制定测试用例计划 `TEST-PLAN.md`：

```markdown
# TodoList SDLC - 注册登录功能测试计划

## 测试范围
- 用户注册功能
- 用户登录功能
- 表单验证
- 错误处理

## 测试用例

### TC001: 用户注册 - 正常流程
**优先级**: P0
**前置条件**: 访问注册页面

**测试步骤**:
1. 导航到 http://localhost:5173/register
2. 填写邮箱: test@example.com
3. 填写密码: Pass123
4. 填写确认密码: Pass123
5. 填写昵称: 测试用户
6. 点击注册按钮

**预期结果**: 注册成功，跳转到主页面或登录页
```

**第二步: 按测试计划执行测试**

```bash
# 创建测试目录
TIMESTAMP=$(date +"%Y-%m-%d-%H%M%S")
REPORT_DIR=".test-report/$TIMESTAMP"
mkdir -p "$REPORT_DIR"
cd "$REPORT_DIR"

# 执行测试（记录到日志）
{
  echo "=== 开始测试 ==="
  echo "时间: $(date)"

  # TC001: 用户注册
  echo ""
  echo "[TC001] 用户注册测试"
  gstack browse goto http://localhost:5173/register
  gstack browse fill "input[type='email']" "test@example.com"
  gstack browse fill "input[placeholder*='密码']" "Pass123"
  gstack browse fill "input[placeholder*='确认']" "Pass123"
  gstack browse fill "input[placeholder*='昵称']" "测试用户"
  gstack browse screenshot "tc001-before-submit.png"
  gstack browse click "button[type='submit']"
  sleep 2
  gstack browse screenshot "tc001-result.png"

} 2>&1 | tee test-output.log
```

**第三步: 生成测试报告**

基于测试日志生成 `TEST-REPORT.md`

### 快速复现问题

```bash
# 方法1: 运行测试脚本
cd .test-report/{TIMESTAMP}
./test-script.sh

# 方法2: 查看最新测试
cd .test-report/LATEST && ./test-script.sh
```
