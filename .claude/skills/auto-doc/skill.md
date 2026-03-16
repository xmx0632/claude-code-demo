---
name: auto-doc
description: 自动文档转换工具，支持 Markdown 与 Word 文档双向转换，自动将 Mermaid 图表转换为 PNG 图片插入 Word 文档。
allowed-tools: ["Read", "Write", "Edit", "Bash"]
disable-model-invocation: false
user-invocable: true
context: fork
agent: general-purpose
---

# AutoDoc 自动文档转换技能

自动文档转换工具，支持 Markdown 与 Word 文档双向转换，自动将 Mermaid 图表转换为 PNG 图片插入 Word 文档。

## 技能概述

本技能集成了完整的文档转换功能，提供：

1. **Markdown 转 Word** (`m2d`): 将 Markdown 文档转换为格式规范的 Word 文档
2. **Word 转 Markdown** (`d2m`): 将 Word 文档转换为 Markdown 格式
3. **Mermaid 图表支持**: 自动将 Mermaid 图表渲染为 PNG 图片并嵌入 Word 文档

## 使用方法

### 1. Markdown 转 Word

#### 基本转换

```bash
/auto-doc m2d <input.md>
```

#### 使用模板转换

```bash
/auto-doc m2d <input.md> --template=<template.docx>
```

#### 示例

```bash
# 转换单个文件
/auto-doc m2d input_doc/example-doc.md

# 使用自定义模板
/auto-doc m2d input_doc/architecture.md --template=template/company-style.docx
```

**输出**: 生成 `.docx` 文件到 `output_doc/` 目录

### 2. Word 转 Markdown

```bash
/auto-doc d2m <input.docx>
```

#### 示例

```bash
# 转换 Word 文档为 Markdown
/auto-doc d2m output_doc/requirements.docx

# 转换后的 Markdown 可用于 AI 分析
```

**输出**: 生成 `.md` 文件到 `output_doc/` 目录，图片提取到 `output_doc/media/`

## 目录结构

```
.claude/skills/auto-doc/
├── input_doc/           # 输入文档目录
├── output_doc/          # 输出文档目录
│   └── media/           # 提取的媒体文件
├── template/            # Word 模板目录
│   └── template.docx    # 默认模板（可选）
├── temp/                # 临时文件目录
├── m2d.sh               # Markdown 转 Word 脚本
├── d2m.sh               # Word 转 Markdown 脚本
├── skill.sh             # 技能主入口脚本
└── skill.md             # 本说明文档
```

## 环境依赖

### 必需工具

1. **Pandoc** - 文档转换核心工具
   ```bash
   brew install pandoc  # macOS
   ```

2. **Mermaid CLI** - 图表渲染
   ```bash
   npm install -g @mermaid-js/mermaid-cli
   ```

3. **Python 3.x** - 脚本运行（系统自带）

### 验证安装

```bash
# 验证 pandoc
pandoc --version

# 验证 mermaid-cli
mmdc --version

# 验证 python
python3 --version
```

## Markdown 转 Word 特性

### Mermaid 图表自动转换

输入 Markdown:
```markdown
## 系统架构

\`\`\`mermaid
graph TB
    A[用户] --> B[API网关]
    B --> C[业务服务]
    C --> D[(数据库)]
\`\`\`
```

输出 Word: 自动渲染为 PNG 图片并插入文档

### 支持的 Markdown 特性

- ✅ 标题层级 (H1-H6)
- ✅ 文本格式 (加粗、斜体、删除线)
- ✅ 列表 (有序、无序)
- ✅ 表格
- ✅ 代码块
- ✅ 图片
- ✅ **Mermaid 图表** (自动转 PNG)
- ✅ 自动目录生成
- ✅ 页码编号

### 自定义模板

将自定义 Word 模板放在 `template/` 目录，转换时引用：

```bash
/auto-doc m2d input.md --template=template/custom.docx
```

## Word 转 Markdown 特性

### 提取内容

- ✅ 文档结构 (标题、段落)
- ✅ 文本格式
- ✅ 表格
- ✅ 图片 (提取到 media 目录)
- ✅ 列表

### 输出结构

```
output_doc/
├── document.md           # 转换后的 Markdown
└── media/               # 提取的图片资源
    ├── image1.png
    └── image2.png
```

## 工作流程

### Markdown 转 Word 流程

```
Markdown 文件
    ↓
检测 Mermaid 代码块
    ↓
[有] → 使用 mmdc 渲染为 PNG
    ↓
替换为图片引用
    ↓
Pandoc 转换
    ↓
Word 文档 (含图片)
```

### Word 转 Markdown 流程

```
Word 文档
    ↓
Pandoc 解析
    ↓
提取媒体文件
    ↓
生成 Markdown
    ↓
修复图片路径
    ↓
Markdown 文件 + 媒体目录
```

## 常见用法场景

### 1. 技术文档生成

```bash
# 将架构文档转为 Word 格式分享
/auto-doc m2d docs/architecture/design.md --template=template/tech-doc.docx
```

### 2. 需求文档处理

```bash
# 将产品提供的 Word 需求转为 Markdown，便于 AI 分析
/auto-doc d2m input_doc/PRD.docx

# 分析转换后的文档
# 然后可以使用其他 skill 进行分析
```

### 3. API 文档导出

```bash
# 将 Markdown API 文档转为 Word
/auto-doc m2d api-documentation.md
```

### 4. 设计文档分享

```bash
# 转换带 Mermaid 图的设计文档
/auto-doc m2d design/system-design.md
```

## 最佳实践

### Markdown 编写建议

1. **使用标准 Markdown 语法**
   ```markdown
   # 一级标题
   ## 二级标题
   - 列表项
   **加粗** *斜体*
   ```

2. **Mermaid 图表编写**
   ```markdown
   \`\`\`mermaid
   graph TB
       A[开始] --> B[处理]
       B --> C[结束]
   \`\`\`
   ```

3. **图片引用**
   ```markdown
   ![图片说明](path/to/image.png)
   ```

### Word 模板定制

在模板中定义：
- 标题样式
- 正文样式
- 代码块样式
- 表格样式
- 页眉页脚

### 批量处理

```bash
# 处理整个文档目录
for file in input_doc/*.md; do
    /auto-doc m2d "$file"
done
```

## 故障排除

### Mermaid 图表不渲染

**问题**: 图表没有转换为 PNG

**解决方案**:
```bash
# 检查 mmdc 是否安装
mmdc --version

# 重新安装
npm install -g @mermaid-js/mermaid-cli

# 验证渲染
mmdc -i test.mmd -o test.png
```

### 中文乱码

**问题**: 转换后中文显示异常

**解决方案**:
- 确保源 Markdown 文件是 UTF-8 编码
- 在模板中设置中文字体

### 图片路径错误

**问题**: 转换后图片无法显示

**解决方案**:
- 检查图片路径是否正确
- 使用相对路径
- 确保图片文件存在

## 与其他技能配合

### 文档生成流程

```bash
# 1. 使用 mermaid-diagram 生成图表
/auto-doc mermaid-diagram --type=architecture

# 2. 编写完整文档
# ... 在 Markdown 中使用生成的图表 ...

# 3. 转换为 Word 分发
/auto-doc m2d design-doc.md
```

### 需求分析流程

```bash
# 1. 转换 Word 需求文档
/auto-doc d2m requirements.docx

# 2. 使用需求分析技能
/sdlc-requirements-analysis requirements.md
```

### API 文档生成

```bash
# 1. 生成 API 文档
/sdlc-api-doc TodoController

# 2. 转换为 Word 格式
/auto-doc m2d api-documentation.md
```

## 注意事项

1. **文件编码**: 确保 Markdown 文件使用 UTF-8 编码
2. **路径问题**: 使用相对路径，避免绝对路径
3. **图表复杂度**: 过于复杂的 Mermaid 图表可能渲染失败
4. **模板兼容**: 确保模板与 Pandoc 版本兼容
5. **临时文件**: `temp/` 目录会在每次转换时清空

## 扩展功能

### 自定义过滤器

创建自定义 Pandoc 过滤器处理特殊需求。

### 样式定制

修改 Word 模板以匹配公司/项目样式规范。

### 批量处理脚本

创建脚本批量处理整个项目的文档。

## 相关资源

- [Pandoc 官方文档](https://pandoc.org/)
- [Mermaid 官方文档](https://mermaid.js.org/)
- [Mermaid CLI](https://github.com/mermaid-js/mermaid-cli)
- [Pandoc Markdown 指南](https://pandoc.org/MANUAL.html)
