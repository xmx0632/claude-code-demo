---
name: sdlc-qa-browse-legacy
description: 快速无头浏览器（兼容旧版 macOS），用于 QA 测试和站点验证。导航任意 URL，与元素交互，验证页面状态，对比操作前后差异，截取带注释的截图，检查响应式布局，测试表单和上传。
allowed-tools: ["Bash", "Read", "AskUserQuestion"]
user-invocable: true
---
<!-- AUTO-GENERATED from SKILL.md.tmpl — do not edit directly -->
<!-- Regenerate: bun run gen:skill-docs -->

## Preamble (run first)

```bash
_UPD=$(~/.claude/skills/gstack/bin/gstack-update-check 2>/dev/null || .claude/skills/gstack/bin/gstack-update-check 2>/dev/null || true)
[ -n "$_UPD" ] && echo "$_UPD" || true
mkdir -p ~/.gstack/sessions
touch ~/.gstack/sessions/"$PPID"
_SESSIONS=$(find ~/.gstack/sessions -mmin -120 -type f 2>/dev/null | wc -l | tr -d ' ')
find ~/.gstack/sessions -mmin +120 -type f -delete 2>/dev/null || true
_CONTRIB=$(~/.claude/skills/gstack/bin/gstack-config get gstack_contributor 2>/dev/null || true)
_BRANCH=$(git branch --show-current 2>/dev/null || echo "unknown")
echo "BRANCH: $_BRANCH"
```

If output shows `UPGRADE_AVAILABLE <old> <new>`: read `~/.claude/skills/gstack/gstack-upgrade/SKILL.md` and follow the "Inline upgrade flow" (auto-upgrade if configured, otherwise AskUserQuestion with 4 options, write snooze state if declined). If `JUST_UPGRADED <from> <to>`: tell user "Running gstack v{to} (just updated!)" and continue.

## AskUserQuestion Format

**ALWAYS follow this structure for every AskUserQuestion call:**
1. **Re-ground:** State the project, the current branch (use the `_BRANCH` value printed by the preamble — NOT any branch from conversation history or gitStatus), and the current plan/task. (1-2 sentences)
2. **Simplify:** Explain the problem in plain English a smart 16-year-old could follow. No raw function names, no internal jargon, no implementation details. Use concrete examples and analogies. Say what it DOES, not what it's called.
3. **Recommend:** `RECOMMENDATION: Choose [X] because [one-line reason]`
4. **Options:** Lettered options: `A) ... B) ... C) ...`

Assume the user hasn't looked at this window in 20 minutes and doesn't have the code open. If you'd need to read the source to understand your own explanation, it's too complex.

Per-skill instructions may add additional formatting rules on top of this baseline.

## Contributor Mode

If `_CONTRIB` is `true`: you are in **contributor mode**. You're a gstack user who also helps make it better.

**At the end of each major workflow step** (not after every single command), reflect on the gstack tooling you used. Rate your experience 0 to 10. If it wasn't a 10, think about why. If there is an obvious, actionable bug OR an insightful, interesting thing that could have been done better by gstack code or skill markdown — file a field report. Maybe our contributor will help make us better!

**Calibration — this is the bar:** For example, `$B js "await fetch(...)"` used to fail with `SyntaxError: await is only valid in async functions` because gstack didn't wrap expressions in async context. Small, but the input was reasonable and gstack should have handled it — that's the kind of thing worth filing. Things less consequential than this, ignore.

**NOT worth filing:** user's app bugs, network errors to user's URL, auth failures on user's site, user's own JS logic bugs.

**To file:** write `~/.gstack/contributor-logs/{slug}.md` with **all sections below** (do not truncate — include every section through the Date/Version footer):

```
# {Title}

Hey gstack team — ran into this while using /{skill-name}:

**What I was trying to do:** {what the user/agent was attempting}
**What happened instead:** {what actually happened}
**My rating:** {0-10} — {one sentence on why it wasn't a 10}

## Steps to reproduce
1. {step}

## Raw output
```
{paste the actual error or unexpected output here}
```

## What would make this a 10
{one sentence: what gstack should have done differently}

**Date:** {YYYY-MM-DD} | **Version:** {gstack version} | **Skill:** /{skill}
```

Slug: lowercase, hyphens, max 60 chars (e.g. `browse-js-no-await`). Skip if file already exists. Max 3 reports per session. File inline and continue — don't stop the workflow. Tell user: "Filed gstack field report: {title}"

# browse: QA Testing & Dogfooding

Persistent headless Chromium. First call auto-starts (~3s), then ~100ms per command.
State persists between calls (cookies, tabs, login sessions).

## SETUP (run this check BEFORE any browse command)

**前置要求**: 首次使用需要安装 Bun（~30 秒）

```bash
# 检查 bun 是否已安装
if ! command -v bun &> /dev/null; then
  echo "正在安装 Bun..."
  curl -fsSL https://bun.sh/install | bash
  export PATH="$HOME/.bun/bin:$PATH"
fi
```

然后检查 browse 工具状态：

```bash
_ROOT=$(git rev-parse --show-toplevel 2>/dev/null)
B=""
[ -n "$_ROOT" ] && [ -x "$_ROOT/.claude/skills/sdlc-qa-browse-legacy/dist/browse" ] && B="$_ROOT/.claude/skills/sdlc-qa-browse-legacy/dist/browse"
if [ -x "$B" ]; then
  echo "READY: $B"
else
  echo "NEEDS_SETUP"
fi
```

If `NEEDS_SETUP`:
1. Tell the user: "browse 工具需要首次编译（~30 秒）。是否继续？" Then STOP and wait.
2. Run: `cd <SKILL_DIR> && ./build.sh`

**build.sh 会执行**：
- 安装 npm 依赖
- 编译 browse 二进制文件
- 安装 Playwright Chromium 浏览器

## Core QA Patterns

### 1. Verify a page loads correctly
```bash
$B goto https://yourapp.com
$B text                          # content loads?
$B console                       # JS errors?
$B network                       # failed requests?
$B is visible ".main-content"    # key elements present?
```

### 2. Test a user flow
```bash
$B goto https://app.com/login
$B snapshot -i                   # see all interactive elements
$B fill @e3 "user@test.com"
$B fill @e4 "password"
$B click @e5                     # submit
$B snapshot -D                   # diff: what changed after submit?
$B is visible ".dashboard"       # success state present?
```

### 3. Verify an action worked
```bash
$B snapshot                      # baseline
$B click @e3                     # do something
$B snapshot -D                   # unified diff shows exactly what changed
```

### 4. Visual evidence for bug reports
```bash
$B snapshot -i -a -o /tmp/annotated.png   # labeled screenshot
$B screenshot /tmp/bug.png                # plain screenshot
$B console                                # error log
```

### 5. Find all clickable elements (including non-ARIA)
```bash
$B snapshot -C                   # finds divs with cursor:pointer, onclick, tabindex
$B click @c1                     # interact with them
```

### 6. Assert element states
```bash
$B is visible ".modal"
$B is enabled "#submit-btn"
$B is disabled "#submit-btn"
$B is checked "#agree-checkbox"
$B is editable "#name-field"
$B is focused "#search-input"
$B js "document.body.textContent.includes('Success')"
```

### 7. Test responsive layouts
```bash
$B responsive /tmp/layout        # mobile + tablet + desktop screenshots
$B viewport 375x812              # or set specific viewport
$B screenshot /tmp/mobile.png
```

### 8. Test file uploads
```bash
$B upload "#file-input" /path/to/file.pdf
$B is visible ".upload-success"
```

### 9. Test dialogs
```bash
$B dialog-accept "yes"           # set up handler
$B click "#delete-button"        # trigger dialog
$B dialog                        # see what appeared
$B snapshot -D                   # verify deletion happened
```

### 10. Compare environments
```bash
$B diff https://staging.app.com https://prod.app.com
```

### 11. Show screenshots to the user
After `$B screenshot`, `$B snapshot -a -o`, or `$B responsive`, always use the Read tool on the output PNG(s) so the user can see them. Without this, screenshots are invisible.

## Snapshot Flags

The snapshot is your primary tool for understanding and interacting with pages.

```
-i        --interactive           Interactive elements only (buttons, links, inputs) with @e refs
-c        --compact               Compact (no empty structural nodes)
-d <N>    --depth                 Limit tree depth (0 = root only, default: unlimited)
-s <sel>  --selector              Scope to CSS selector
-D        --diff                  Unified diff against previous snapshot (first call stores baseline)
-a        --annotate              Annotated screenshot with red overlay boxes and ref labels
-o <path> --output                Output path for annotated screenshot (default: /tmp/browse-annotated.png)
-C        --cursor-interactive    Cursor-interactive elements (@c refs — divs with pointer, onclick)
```

All flags can be combined freely. `-o` only applies when `-a` is also used.
Example: `$B snapshot -i -a -C -o /tmp/annotated.png`

**Ref numbering:** @e refs are assigned sequentially (@e1, @e2, ...) in tree order.
@c refs from `-C` are numbered separately (@c1, @c2, ...).

After snapshot, use @refs as selectors in any command:
```bash
$B click @e3       $B fill @e4 "value"     $B hover @e1
$B html @e2        $B css @e5 "color"      $B attrs @e6
$B click @c1       # cursor-interactive ref (from -C)
```

**Output format:** indented accessibility tree with @ref IDs, one element per line.
```
  @e1 [heading] "Welcome" [level=1]
  @e2 [textbox] "Email"
  @e3 [button] "Submit"
```

Refs are invalidated on navigation — run `snapshot` again after `goto`.

## Full Command List

### Navigation
| Command | Description |
|---------|-------------|
| `back` | History back |
| `forward` | History forward |
| `goto <url>` | Navigate to URL |
| `reload` | Reload page |
| `url` | Print current URL |

### Reading
| Command | Description |
|---------|-------------|
| `accessibility` | Full ARIA tree |
| `forms` | Form fields as JSON |
| `html [selector]` | innerHTML of selector (throws if not found), or full page HTML if no selector given |
| `links` | All links as "text → href" |
| `text` | Cleaned page text |

### Interaction
| Command | Description |
|---------|-------------|
| `click <sel>` | Click element |
| `cookie <name>=<value>` | Set cookie on current page domain |
| `cookie-import <json>` | Import cookies from JSON file |
| `cookie-import-browser [browser] [--domain d]` | Import cookies from Comet, Chrome, Arc, Brave, or Edge (opens picker, or use --domain for direct import) |
| `dialog-accept [text]` | Auto-accept next alert/confirm/prompt. Optional text is sent as the prompt response |
| `dialog-dismiss` | Auto-dismiss next dialog |
| `fill <sel> <val>` | Fill input |
| `header <name>:<value>` | Set custom request header (colon-separated, sensitive values auto-redacted) |
| `hover <sel>` | Hover element |
| `press <key>` | Press key — Enter, Tab, Escape, ArrowUp/Down/Left/Right, Backspace, Delete, Home, End, PageUp, PageDown, or modifiers like Shift+Enter |
| `scroll [sel]` | Scroll element into view, or scroll to page bottom if no selector |
| `select <sel> <val>` | Select dropdown option by value, label, or visible text |
| `type <text>` | Type into focused element |
| `upload <sel> <file> [file2...]` | Upload file(s) |
| `useragent <string>` | Set user agent |
| `viewport <WxH>` | Set viewport size |
| `wait <sel|--networkidle|--load>` | Wait for element, network idle, or page load (timeout: 15s) |

### Inspection
| Command | Description |
|---------|-------------|
| `attrs <sel|@ref>` | Element attributes as JSON |
| `console [--clear|--errors]` | Console messages (--errors filters to error/warning) |
| `cookies` | All cookies as JSON |
| `css <sel> <prop>` | Computed CSS value |
| `dialog [--clear]` | Dialog messages |
| `eval <file>` | Run JavaScript from file and return result as string (path must be under /tmp or cwd) |
| `is <prop> <sel>` | State check (visible/hidden/enabled/disabled/checked/editable/focused) |
| `js <expr>` | Run JavaScript expression and return result as string |
| `network [--clear]` | Network requests |
| `perf` | Page load timings |
| `storage [set k v]` | Read all localStorage + sessionStorage as JSON, or set <key> <value> to write localStorage |

### Visual
| Command | Description |
|---------|-------------|
| `diff <url1> <url2>` | Text diff between pages |
| `pdf [path]` | Save as PDF |
| `responsive [prefix]` | Screenshots at mobile (375x812), tablet (768x1024), desktop (1280x720). Saves as {prefix}-mobile.png etc. |
| `screenshot [--viewport] [--clip x,y,w,h] [selector|@ref] [path]` | Save screenshot (supports element crop via CSS/@ref, --clip region, --viewport) |

### Snapshot
| Command | Description |
|---------|-------------|
| `snapshot [flags]` | Accessibility tree with @e refs for element selection. Flags: -i interactive only, -c compact, -d N depth limit, -s sel scope, -D diff vs previous, -a annotated screenshot, -o path output, -C cursor-interactive @c refs |

### Meta
| Command | Description |
|---------|-------------|
| `chain` | Run commands from JSON stdin. Format: [["cmd","arg1",...],...] |

### Tabs
| Command | Description |
|---------|-------------|
| `closetab [id]` | Close tab |
| `newtab [url]` | Open new tab |
| `tab <id>` | Switch to tab |
| `tabs` | List open tabs |

### Server
| Command | Description |
|---------|-------------|
| `restart` | Restart server |
| `status` | Health check |
| `stop` | Shutdown server |

## 测试报告模板约定

使用 gstack browse 进行 QA 测试后，应生成结构化的测试报告并保存到 `.test-report/` 目录。

### 报告结构

```
.test-report/
├── TEST-REPORT.md          # 测试报告（包含截图）
├── screenshot-1.png         # 测试截图
├── screenshot-2.png
└── ...
```

### 报告模板

```markdown
# {项目名称} QA 测试报告

## 测试概述

**测试日期**: {YYYY-MM-DD}
**测试工具**: gstack browse (headless Chromium)
**测试环境**: {开发/测试/生产}
**测试人员**: {测试人员名称}

## 服务状态

| 服务 | 状态 | 端口 |
|------|------|------|
| 前端 | ✅ 运行中 | {端口} |
| 后端 | ✅ 运行中 | {端口} |
| 数据库 | ✅ 运行中 | {端口/类型} |

## 测试用例执行情况

### {测试用例标题} ✅/❌

**测试步骤**:
1. {步骤1}
2. {步骤2}
3. {步骤3}

**测试结果**: ✅ 通过 / ❌ 失败
- API 响应: {响应信息}
- 页面跳转: {跳转信息}
- 数据持久化: {持久化状态}

**截图说明**:
<img src="{screenshot-filename}.png" width="600">

### {更多测试用例...}

## 测试覆盖率

| 模块 | 测试用例数 | 通过 | 失败 | 覆盖率 |
|------|-----------|------|------|--------|
| {模块1} | {n} | {n} | {n} | {xx}% |
| {模块2} | {n} | {n} | {n} | {xx}% |
| **总计** | **{n}** | **{n}** | **{n}** | **{xx}%** |

## API 测试结果

### 成功的 API 调用

| 方法 | 端点 | 状态码 | 响应时间 |
|------|------|--------|----------|
| {METHOD} | `{endpoint}` | {200} | {~100ms} |

### 失败的 API 调用

| 方法 | 端点 | 状态码 | 错误信息 |
|------|------|--------|----------|
| {METHOD} | `{endpoint}` | {500} | {error} |

## 发现的问题

### {问题标题}

**严重程度**: 高/中/低
**复现步骤**:
1. {步骤}
2. {步骤}

**预期结果**: {预期}
**实际结果**: {实际}

### 注意事项

1. **技术要点1**: {说明}
2. **技术要点2**: {说明}

## 结论

**测试结果**: ✅ 全部通过 / ⚠️ 部分通过 / ❌ 存在阻塞问题

{总结性描述}

## 测试环境配置

- **前端**: {技术栈}
- **后端**: {技术栈}
- **数据库**: {数据库类型}
- **ORM**: {ORM框架}
- **API 文档**: {文档地址}

## 下一步建议

1. {建议1}
2. {建议2}
3. {建议3}

---

**测试工具**: gstack browse v{version}
**报告生成时间**: {YYYY-MM-DD HH:mm:ss UTC}
```

### 截图规范

1. **固定宽度**: 所有截图使用 `width="600"` 保持统一
   ```html
   <img src="screenshot.png" width="600">
   ```

2. **命名规范**: 使用描述性文件名
   - `{feature}-page.png` - 功能页面截图
   - `{action}-before.png` - 操作前截图
   - `{action}-after.png` - 操作后截图
   - `{error}-error.png` - 错误截图

3. **截图类型**:
   - 全页面截图: 使用 `$B screenshot`
   - 标注截图: 使用 `$B snapshot -i -a -o`
   - 响应式截图: 使用 `$B responsive`

### 测试流程建议

1. **启动服务**: 确保前后端服务运行
2. **初始化**: 运行 preamble 检查 browse 工具状态
3. **执行测试**: 按测试用例逐个执行
4. **保存截图**: 每个关键步骤保存截图到 `/tmp/`
5. **复制文件**: 将所有截图复制到 `.test-report/` 目录
6. **生成报告**: 使用模板生成 TEST-REPORT.md

### 示例测试脚本

```bash
# 1. 创建测试报告目录
mkdir -p .test-report

# 2. 初始化 browse
_ROOT=$(git rev-parse --show-toplevel 2>/dev/null)
B="$_ROOT/.claude/skills/sdlc-qa-browse-legacy/dist/browse"

# 3. 执行测试
$B goto http://localhost:3000
$B screenshot -o /tmp/login-page.png
# ... 更多测试步骤

# 4. 复制截图
cp /tmp/*.png .test-report/

# 5. 生成报告
# 根据模板生成 TEST-REPORT.md
```
