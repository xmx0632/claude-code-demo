# sdlc-qa-browse 使用指南

无头浏览器 QA 测试工具，用于 Web 应用测试和站点验证。

## 🚀 快速开始

```bash
# 1. 首次使用需安装 Bun（~30 秒）
curl -fsSL https://bun.sh/install | bash

# 2. 检查 browse 工具状态
_ROOT=$(git rev-parse --show-toplevel 2>/dev/null)
B="$_ROOT/.claude/skills/sdlc-qa-browse/dist/browse"
[ -x "$B" ] && echo "READY" || echo "NEEDS_BUILD"

# 3. 如需编译
cd .claude/skills/sdlc-qa-browse && ./build.sh
```

## 📋 核心功能

| 功能 | 说明 |
|------|------|
| **页面导航** | 访问任意 URL，检查页面加载状态 |
| **元素交互** | 点击、填写表单、悬停、键盘输入 |
| **状态验证** | 检查元素可见性、启用状态、文本内容 |
| **截图** | 全页面截图、带标注截图、响应式截图 |
| **差异对比** | 操作前后页面状态差异对比 |
| **表单测试** | 填写、上传、对话框测试 |
| **网络监控** | 检查网络请求、响应时间、失败请求 |

## 🎯 常用命令

### 导航与基础操作

```bash
# 访问页面
$B goto https://example.com

# 获取页面 URL
$B url

# 刷新页面
$B reload

# 后退/前进
$B back
$B forward
```

### 页面检查

```bash
# 获取页面文本
$B text

# 检查元素可见性
$B is visible ".main-content"

# 检查元素状态
$B is enabled "#submit-btn"
$B is checked "#agree-checkbox"

# 获取交互元素快照
$B snapshot -i
```

### 元素交互

```bash
# 点击元素（使用快照中的 @e 引用）
$B click @e3

# 填写表单
$B fill @e1 "user@example.com"

# 输入文本
$B type "Hello World"

# 悬停
$B hover @e2

# 按键
$B press Enter
$B press Tab
```

### 截图功能

```bash
# 全页面截图
$B screenshot /tmp/page.png

# 带标注的截图
$B snapshot -i -a -o /tmp/annotated.png

# 响应式截图（移动端 + 平板 + 桌面）
$B responsive /tmp/layout
```

### 调试信息

```bash
# 控制台消息
$B console

# 网络请求
$B network

# 页面 HTML
$B html
```

## 📝 使用示例

### 示例 1：测试登录流程

```bash
# 1. 访问登录页
$B goto https://example.com/login

# 2. 获取交互元素
$B snapshot -i

# 3. 填写表单
$B fill @e1 "user@example.com"
$B fill @e2 "password123"

# 4. 提交
$B click @e3

# 5. 验证跳转
$B url
$B is visible ".dashboard"
```

### 示例 2：对比操作前后状态

```bash
# 1. 基线快照
$B snapshot

# 2. 执行操作
$B click @e5

# 3. 差异对比
$B snapshot -D
```

### 示例 3：测试响应式布局

```bash
# 1. 生成响应式截图
$B responsive /tmp/responsive

# 输出：
#   responsive-mobile.png (375x812)
#   responsive-tablet.png (768x1024)
#   responsive-desktop.png (1280x720)
```

## ⚙️ 配置说明

### SETUP 检查脚本

```bash
_ROOT=$(git rev-parse --show-toplevel 2>/dev/null)
B=""
[ -n "$_ROOT" ] && [ -x "$_ROOT/.claude/skills/sdlc-qa-browse/dist/browse" ] && B="$_ROOT/.claude/skills/sdlc-qa-browse/dist/browse"
if [ -x "$B" ]; then
  echo "READY: $B"
else
  echo "NEEDS_SETUP"
fi
```

### 编译 browse 工具

```bash
cd .claude/skills/sdlc-qa-browse
./build.sh
```

## 🐛 常见问题

### Q: 提示 "NEEDS_SETUP"
**A**: 需要编译 browse 工具，运行 `./build.sh`

### Q: 如何获取元素引用？
**A**: 使用 `$B snapshot -i` 获取交互元素列表，每个元素有 `@e1`, `@e2` 等引用

### Q: 支持哪些浏览器？
**A**: 使用 Playwright Chromium，支持 Chrome/Edge 最新特性

### Q: 如何处理弹窗？
**A**: 使用 `$B dialog-accept "yes"` 设置自动接受，然后触发弹窗

### Q: 截图保存在哪里？
**A**: 默认 `/tmp/` 目录，可使用 `-o` 参数指定路径

## 📚 完整参考

详细命令和配置请参考：
[SKILL.md](../../.claude/skills/sdlc-qa-browse/SKILL.md)

## 🔗 相关 Skills

- [sdlc-qa-report](../skills/index.md#sdlc-qa-report) - 生成结构化 QA 测试报告
- [sdlc-testing](../skills/index.md#sdlc-testing) - 测试阶段执行
