# 测试报告模板

使用 **browse** 命令进行 QA 测试后，应生成结构化的测试报告并保存到被测试项目的 `.test-report/` 目录。

> **重要**:
> 1. 使用 `browse` 命令，不是 `gstack browse`。browse 命令位于 `.claude/skills/sdlc-qa-browse-legacy/dist/browse`
> 2. 测试报告应保存在**被测试项目**的 `.test-report/` 目录下，而非仓库根目录

## 核心原则

1. **browse 命令优先**: 当 browse 可执行时，直接使用，不进行编译
2. **测试用例驱动**: 按照测试用例计划执行测试
3. **脚本可复现**: 测试脚本能够独立复现测试场景
4. **路径明确**: 测试报告位于被测试项目目录下
5. **先进入项目目录**: 执行测试前必须先 `cd` 到被测试项目目录

## 报告结构

```
projects/todolist-sdlc/          # 被测试项目目录
└── .test-report/                # 项目级测试报告目录
    ├── 2026-03-19-170034/       # 时间戳目录 (YYYY-MM-DD-HHMMSS)
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

## 测试流程

### 第一步: 进入被测试项目目录

```bash
# 示例：测试 todolist-sdlc 项目
cd projects/todolist-sdlc
```

## 测试流程

### 第一步: 制定测试用例计划

在开始测试前，先制定测试用例计划：

```bash
# 创建测试目录
TIMESTAMP=$(date +"%Y-%m-%d-%H%M%S")
REPORT_DIR=".test-report/$TIMESTAMP"
mkdir -p "$REPORT_DIR"
cd "$REPORT_DIR"

# 创建测试用例计划
cat > TEST-PLAN.md << 'EOF'
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

### TC002: 用户登录 - 正常流程
**优先级**: P0
**测试数据**: admin/admin123

**测试步骤**:
1. 导航到 http://localhost:5173/login
2. 填写邮箱: admin
3. 填写密码: admin123
4. 点击登录按钮

**预期结果**: 登录成功，跳转到主页面

### TC003: 表单验证 - 密码不一致
**优先级**: P1

**测试步骤**:
1. 访问注册页面
2. 填写邮箱: test@example.com
3. 填写密码: Pass123
4. 填写确认密码: Different123
5. 点击注册按钮

**预期结果**: 显示"密码不一致"错误提示
EOF
```

### 第二步: 按测试计划执行测试

```bash
# 使用 browse 执行测试
# 注意: browse 需要在对应项目目录下执行

# 导航到测试目录
cd "$REPORT_DIR"

# 执行测试（记录到日志）
{
  echo "=== 开始测试 ==="
  echo "时间: $(date)"

  # TC001: 用户注册
  echo ""
  echo "[TC001] 用户注册测试"
  browse goto http://localhost:5173/register
  browse fill "input[type='email']" "test@example.com"
  browse fill "input[placeholder*='密码']" "Pass123"
  browse fill "input[placeholder*='确认']" "Pass123"
  browse fill "input[placeholder*='昵称']" "测试用户"
  browse screenshot "tc001-before-submit.png"
  browse click "button[type='submit']"
  sleep 2
  browse screenshot "tc001-result.png"

} 2>&1 | tee test-output.log
```

### 第三步: 生成测试报告

```bash
# 基于测试日志生成报告
cat > TEST-REPORT.md << 'EOF'
# TodoList SDLC - 注册登录功能测试报告

## 测试概述

**测试日期**: {YYYY-MM-DD HH:MM:SS}
**测试工具**: browse (sdlc-qa-browse-legacy)
**测试环境**: 开发环境

## 测试用例执行情况

### TC001: 用户注册 - 正常流程 ✅/❌

**测试步骤**:
1. 导航到注册页面
2. 填写注册表单
3. 提交注册

**测试结果**: {通过/失败}

**截图说明**:
- 注册前: <img src="tc001-before-submit.png" width="400">
- 注册后: <img src="tc001-result.png" width="400">

**执行日志**:
\`\`\`
{相关日志片段}
\`\`\`

### TC002: 用户登录 - 正常流程 ✅/❌

...

## 发现的问题

### 问题1: {问题标题}

**严重程度**: 高/中/低

**复现步骤**:
\`\`\`bash
cd .test-report/{TIMESTAMP}
./test-script.sh
\`\`\`

**预期结果**: {预期}
**实际结果**: {实际}

## 结论

**测试结果**: ✅ 全部通过 / ⚠️ 部分通过 / ❌ 存在阻塞问题
EOF
```

## 测试脚本规范

### 脚本模板

```bash
#!/bin/bash
# TodoList SDLC - 注册登录功能测试脚本
# 生成时间: {YYYY-MM-DD HH:MM:SS}

set -e  # 遇到错误时退出

# 配置
FRONTEND_URL="http://localhost:5173"
BACKEND_URL="http://localhost:8080"
TEST_DIR="$(cd "$(dirname "$0")" && pwd)"
LOG_FILE="$TEST_DIR/test-output.log"

# 日志函数
log() {
  echo "[$(date +"%H:%M:%S")] $1" | tee -a "$LOG_FILE"
}

# 截图函数
screenshot() {
  local name=$1
  browse screenshot "$TEST_DIR/$name.png"
  log "截图保存: $name.png"
}

log "=== TodoList SDLC - 注册登录功能测试 ==="
log "前端: $FRONTEND_URL"
log "后端: $BACKEND_URL"
log ""

# TC001: 用户注册
log "[TC001] 开始用户注册测试"
browse goto $FRONTEND_URL/register
screenshot "tc001-register-page"

log "填写注册表单..."
browse fill "input[type='email']" "test@example.com"
browse fill "input[placeholder*='密码']" "Pass123"
browse fill "input[placeholder*='确认']" "Pass123"
browse fill "input[placeholder*='昵称']" "测试用户"
screenshot "tc001-form-filled"

log "提交注册..."
browse click "button[type='submit']"
sleep 3
screenshot "tc001-result"

log "[TC001] 测试完成"
```

### 脚本命名规范

| 场景 | 脚本名格式 | 示例 |
|------|-----------|------|
| 功能测试 | `{feature}-test.sh` | `login-test.sh` |
| Bug 复现 | `bug-{id}-reproduce.sh` | `bug-001-reproduce.sh` |
| 回归测试 | `regression-{module}.sh` | `regression-auth.sh` |
| 冒烟测试 | `smoke-test.sh` | `smoke-test.sh` |

## browse 常用命令

### 导航和交互
```bash
browse goto <url>                    # 导航到URL
browse fill "<selector>" "<value>"   # 填写表单
browse click "<selector>"             # 点击元素
browse snapshot -i                    # 查看可交互元素
browse text                          # 获取页面文本
browse screenshot [path]              # 截图
browse console                       # 查看控制台消息
browse network                       # 查看网络请求
```

### 元素定位
```bash
# 通过属性定位
browse fill "input[type='email']" "test@example.com"
browse fill "input[placeholder*='密码']" "Pass123"
browse click "button[type='submit']"

# 通过 class 定位
browse click ".login-button"
browse fill ".username-input" "admin"
```

## 测试脚本完整示例

```bash
#!/bin/bash
# TodoList SDLC - 用户认证功能完整测试
# 生成时间: 2026-03-19 17:00:00

set -e

FRONTEND_URL="http://localhost:5173"
TEST_DIR="$(cd "$(dirname "$0")" && pwd)"
LOG_FILE="$TEST_DIR/test-output.log"

log() {
  echo "[$(date +"%H:%M:%S")] $1" | tee -a "$LOG_FILE"
}

screenshot() {
  browse screenshot "$TEST_DIR/$1.png"
  log "截图: $1.png"
}

log "=== TodoList SDLC - 用户认证功能测试 ==="
log "前端: $FRONTEND_URL"

# 前置检查
log "[前置] 检查服务状态"
curl -s $FRONTEND_URL > /dev/null && log "✅ 前端服务正常" || { log "❌ 前端服务异常"; exit 1; }

# TC001: 访问应用首页
log "[TC001] 访问应用首页"
browse goto $FRONTEND_URL
screenshot "tc001-homepage"

# TC002: 查看登录页面
log "[TC002] 查看登录页面"
browse goto $FRONTEND_URL/login
screenshot "tc002-login-page"

# TC003: 用户登录测试
log "[TC003] 用户登录测试"
browse fill "input[type='email']" "admin"
browse fill "input[type='password']" "admin123"
screenshot "tc003-login-form"
browse click "button[type='submit']"
sleep 3
screenshot "tc003-login-result"
browse text | head -10 | tee -a "$LOG_FILE"

log "=== 测试完成 ==="
```

## 快捷命令

```bash
# 1. 进入被测试项目目录
cd projects/todolist-sdlc

# 2. 创建新的测试目录
TIMESTAMP=$(date +"%Y-%m-%d-%H%M%S")
mkdir -p ".test-report/$TIMESTAMP"
cd ".test-report/$TIMESTAMP"

# 3. 制定测试计划
# 编辑 TEST-PLAN.md

# 4. 按计划执行测试
./test-script.sh

# 5. 生成测试报告
# 基于 test-output.log 生成 TEST-REPORT.md

# 6. 查看最新测试
cd ../LATEST
```

## 测试报告模板

```markdown
# {项目名称} QA 测试报告

## 测试概述

**测试日期**: {YYYY-MM-DD HH:MM:SS}
**测试工具**: browse (sdlc-qa-browse-legacy)
**测试环境**: {开发/测试/生产}
**测试人员**: {测试人员名称}

## 快速复现

⭐ **一键复现测试**:
```bash
cd .test-report/{TIMESTAMP}
./test-script.sh
```

## 服务状态

| 服务 | 状态 | URL |
|------|------|-----|
| 前端 | ✅ 运行中 | http://localhost:5173 |
| 后端 | ✅ 运行中 | http://localhost:8080 |

## 测试用例执行情况

### TC001: {用例标题} ✅/❌

**测试步骤**:
1. {步骤1}
2. {步骤2}

**测试结果**: {通过/失败}

**截图**:
- 操作前: <img src="{screenshot}.png" width="400">

**执行日志**:
\`\`\`
15:30:01 [TC001] 开始测试
15:30:02 [INFO] 导航到注册页面
15:30:05 [INFO] 填写表单完成
15:30:06 [INFO] 提交注册
\`\`\`

## 发现的问题

### {问题标题}

**严重程度**: 高/中/低

**复现脚本**: `test-script.sh` (第 XX 行)

**复现步骤**:
1. {步骤}
2. {步骤}

**预期结果**: {预期}
**实际结果**: {实际}

## 结论

**测试结果**: ✅ 全部通过 / ⚠️ 部分通过 / ❌ 存在阻塞问题

{总结性描述}

---

## 常见问题和注意事项

### 问题1: 截图路径限制

**错误现象**:
```bash
Path must be within: /tmp, /Users/xxx/.claude/skills/sdlc-qa-browse-legacy
```

**原因**: browse 命令的 screenshot 功能有路径限制，只能保存到 `/tmp` 或 skill 目录。

**解决方案**:
```bash
# 先保存到 /tmp，再复制到测试目录
SCREENSHOT_DIR="/tmp/test-screenshots"
mkdir -p "$SCREENSHOT_DIR"

screenshot() {
  local name=$1
  local tmp_path="$SCREENSHOT_DIR/$name.png"
  local final_path="$TEST_DIR/$name.png"
  browse screenshot "$tmp_path"
  cp "$tmp_path" "$final_path"
  log "截图保存: $name.png"
}
```

---

### 问题2: 元素引用 @e 失效

**错误现象**:
```bash
Ref @e1 not found. Run 'snapshot' to get fresh refs.
```

**原因**: `@e` refs 会随页面变化而改变

**解决方案**:
```bash
# 推荐: 使用稳定的 CSS 选择器
browse fill "input[placeholder*='邮箱']" "test@example.com"
browse click "button:has-text('登录')"

# 不推荐: 使用 @e refs（可能变化）
browse snapshot -i > /dev/null
browse fill @e1 "test@example.com"
```

---

### 问题3: 页面跳转后的元素等待

**错误现象**:
```bash
Element not found after navigation
```

**解决方案**:
```bash
# 方法1: 使用 sleep 等待
browse click "button:has-text('登录')"
sleep 3  # 等待页面加载
browse text  # 验证页面内容

# 方法2: 等待特定元素出现
browse wait ".user-info"  # 等待用户信息元素出现
```

---

## 测试脚本最佳实践

### 1. 配置部分

```bash
#!/bin/bash
# 使用绝对路径
FRONTEND_URL="http://localhost:5173"
BACKEND_URL="http://localhost:8080"
TEST_DIR="/absolute/path/to/.test-report/2026-03-19-172657"
LOG_FILE="$TEST_DIR/test-output.log"
BROWSE="/absolute/path/to/.claude/skills/sdlc-qa-browse-legacy/dist/browse"

# 截图临时目录（避免路径限制）
SCREENSHOT_DIR="/tmp/test-screenshots"
mkdir -p "$SCREENSHOT_DIR"
```

### 2. 日志和截图函数

```bash
log() {
  echo "[$(date +"%H:%M:%S")] $1" | tee -a "$LOG_FILE"
}

screenshot() {
  local name=$1
  local tmp_path="$SCREENSHOT_DIR/$name.png"
  local final_path="$TEST_DIR/$name.png"
  browse screenshot "$tmp_path"
  cp "$tmp_path" "$final_path"
  log "截图保存: $name.png"
}
```

### 3. 元素交互模式

```bash
# 推荐: 使用稳定的 CSS 选择器
browse fill "input[placeholder*='邮箱']" "test@example.com"
browse fill "input[placeholder*='密码']" "Test123"
browse click "button:has-text('登录')"

# 不推荐: 使用 @e refs（可能变化）
browse snapshot -i > /dev/null
browse fill @e1 "test@example.com"
```

---

## 调试技巧

### 查看页面结构

```bash
# 查看可交互元素（带 @e refs）
browse snapshot -i

# 查看完整页面结构
browse snapshot

# 查看紧凑结构
browse snapshot -c

# 查看特定选择器的元素
browse snapshot -s "form"
```

### 获取页面信息

```bash
# 获取页面文本
browse text

# 获取特定元素的 HTML
browse html ".form"

# 获取元素属性
browse attrs "input[type='email']"

# 获取计算样式
browse css "button" "background-color"
```

### 检查网络和控制台

```bash
# 查看网络请求
browse network

# 查看控制台错误
browse console --errors

# 查看页面性能
browse perf
```

---

## 选择器速查表

| 场景 | 选择器示例 | 说明 |
|------|-----------|------|
| 文本包含 | `input[placeholder*='密码']` | placeholder 包含"密码" |
| 文本匹配 | `button:has-text('登录')` | 按钮文本为"登录" |
| 属性等于 | `input[type='email']` | type 属性为 email |
| 类选择器 | `.el-button--primary` | class 包含 el-button--primary |
| ID 选择器 | `#username` | id 为 username |
| 组合选择器 | `form.el-form input[name='email']` | 表单内的邮箱输入框 |
| 层级选择器 | `.login-form > .el-input` | 直接子元素 |
