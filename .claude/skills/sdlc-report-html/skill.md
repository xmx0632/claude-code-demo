---
name: sdlc-report-html
description: |
  将 Markdown 测试报告转换为单文件 HTML（base64 嵌入图片）。
  用于生成可分享的测试报告，适合发送到飞书、邮件等平台。
allowed-tools: ["Bash", "Read", "Write", "Glob"]
user-invocable: true
---

# /sdlc-report-html — Markdown 报告转 HTML

将 Markdown 测试报告（含截图）转换为样式化的单文件 HTML，所有图片以 base64 嵌入，无需额外附件。

## 功能特点

- ✅ Markdown 转 HTML（支持标题、列表、代码块、表格等）
- 🖼️ 图片自动 base64 嵌入（无需外部文件）
- 🎨 专业样式（响应式、暗色代码块、表格美化）
- 📦 单文件输出（方便分享）
- 📱 适配移动端

## 使用方法

### 基本用法

```bash
/sdlc-report-html .test-report/TEST-REPORT.md
```

### 指定输出文件

```bash
/sdlc-report-html .test-report/TEST-REPORT.md --output report.html
```

### 指定图片目录

```bash
/sdlc-report-html .test-report/TEST-REPORT.md --img-dir .test-report/screenshots
```

## 参数

| 参数 | 说明 | 默认值 |
|------|------|--------|
| `<markdown-file>` | Markdown 报告文件路径 | 必需 |
| `--output` | 输出 HTML 文件路径 | 同目录下的 .html 文件 |
| `--img-dir` | 图片目录（用于相对路径解析） | markdown 文件所在目录 |
| `--title` | HTML 页面标题 | "测试报告" |

## 工作流程

### 1. 读取 Markdown

解析指定的 Markdown 文件，提取：
- 标题层级
- 代码块
- 图片引用
- 表格
- 列表

### 2. 处理图片

对于每个图片引用 `![alt](path)`：
1. 解析图片路径（支持相对路径）
2. 读取图片二进制数据
3. 转换为 base64 编码
4. 生成 data URI：`data:image/png;base64,...`

### 3. 转换为 HTML

将 Markdown 元素转换为对应的 HTML：
- `# H1` → `<h1>`
- `**bold**` → `<strong>`
- `` `code` `` → `<code>`
- ` ``` ` → `<pre><code>`
- 表格 → `<table>`

### 4. 应用样式

注入专业 CSS 样式：
- 响应式布局
- 暗色代码块
- 表格美化
- 图片自适应
- 移动端适配

### 5. 输出单文件

生成完整的 HTML 文件：
```html
<!DOCTYPE html>
<html>
<head>
  <style>...</style>
</head>
<body>
  <div class="container">
    <!-- 内容 + base64 图片 -->
  </div>
</body>
</html>
```

## 输出示例

输入 Markdown:
```markdown
# 测试报告

## 登录测试

登录成功！![截图](screenshots/login.png)
```

输出 HTML:
```html
<!DOCTYPE html>
<html>
<head>
  <style>
    /* 专业样式 */
  </style>
</head>
<body>
  <div class="container">
    <h1>测试报告</h1>
    <h2>登录测试</h2>
    <p>登录成功！<img src="data:image/png;base64,iVBORw0KGgo..."></p>
  </div>
</body>
</html>
```

## 支持的 Markdown 语法

| 语法 | 转换结果 |
|------|----------|
| `# H1` ~ `###### H6` | `<h1>` ~ `<h6>` |
| `**bold**` | `<strong>` |
| `*italic*` | `<em>` |
| `` `code` `` | `<code>` |
| ` ``` ` | `<pre><code>` |
| `[link](url)` | `<a href="url">link</a>` |
| `![img](path)` | `<img src="data:...">` |
| `- item` | `<li>` |
| `\| table \|` | `<table>` |

## 样式特性

### 响应式
- 最大宽度 1200px
- 移动端自动适配

### 代码块
- 暗色背景 (#2d2d2d)
- 语法高亮色
- 横向滚动

### 表格
- 斑马条纹
- 圆角边框
- 悬停高亮

### 图片
- 最大宽度 100%
- 自动高度
- 圆角阴影

## 使用场景

### 1. QA 测试报告

```bash
# 先运行 QA 测试
/sdlc-qa-browse http://localhost:3000

# 转换报告为 HTML
/sdlc-report-html .test-report/TEST-REPORT.md
```

### 2. 发送到飞书

```bash
# 生成 HTML 后，使用飞书机器人发送
curl -X POST http://localhost:5003/api/send-message \
  -H "Content-Type: application/json" \
  -d '{"message": "测试报告已生成，请查看附件"}'
curl -X POST http://localhost:5003/api/send-file \
  -F "file=@.test-report/TEST-REPORT.html"
```

### 3. 邮件发送

单文件 HTML 可直接嵌入邮件正文，无需附件。

## 与其他 Skills 的配合

| Skill | 使用场景 |
|-------|----------|
| `/sdlc-qa-browse` | 执行测试，生成 markdown 报告 |
| `/sdlc-report-html` | 转换为 HTML（本 skill） |
| `/sdlc-qa-report` | 只报告模式的 QA 测试 |

## 技术实现

### Node.js 版本（推荐）

使用 ES Modules，支持：
- `fs` 模块读取文件
- `path` 模块处理路径
- 内置 Markdown 解析
- Base64 编码

### 依赖

无需外部依赖，纯 Node.js 内置模块。

## 限制

1. **图片格式**: 支持 PNG、JPG、JPEG、GIF
2. **文件大小**: 大图片会显著增加 HTML 文件大小
3. **Markdown 子集**: 支持常用语法，不支持所有 GFM 特性

## 故障排查

### 图片找不到

```
Error: Cannot find image './screenshots/test.png'
```

**解决**: 使用 `--img-dir` 参数指定图片目录，或使用绝对路径。

### HTML 文件过大

**原因**: 图片太多或分辨率太高。

**解决**:
1. 压缩原始图片
2. 减少截图数量
3. 使用图片缩略图

## 示例

完整工作流：

```bash
# 1. 运行 QA 测试
cd projects/todolist-sdlc
/sdlc-qa-browse http://localhost:5173

# 2. 转换报告
/sdlc-report-html .test-report/TEST-REPORT.md

# 3. 发送到飞书
curl -X POST http://localhost:5003/api/send-file \
  -F "file=@.test-report/TEST-REPORT.html" \
  -F "message=QA 测试报告"
```
