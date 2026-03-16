# AutoDoc Skill - 自动文档转换

## 简介

AutoDoc 是一个完整的文档转换技能，支持 Markdown 与 Word 文档的双向转换，并能自动将 Mermaid 图表转换为 PNG 图片插入 Word 文档。

## 功能特性

- ✅ **Markdown 转 Word**: 将 Markdown 文档转换为格式规范的 Word 文档
- ✅ **Word 转 Markdown**: 将 Word 文档转换为 Markdown 格式，便于 AI 分析
- ✅ **Mermaid 图表支持**: 自动将 Mermaid 图表渲染为 PNG 图片并嵌入文档
- ✅ **自定义模板**: 支持使用自定义 Word 模板
- ✅ **独立运行**: 无需依赖外部 autoDoc 目录

## 快速开始

### 1. 环境准备

```bash
# 安装 Pandoc
brew install pandoc

# 安装 Mermaid CLI
npm install -g @mermaid-js/mermaid-cli
```

### 2. 验证安装

```bash
pandoc --version
mmdc --version
```

### 3. 使用技能

```bash
# Markdown 转 Word
/auto-doc m2d input_doc/example-doc.md

# Word 转 Markdown
/auto-doc d2m output_doc/document.docx

# 使用自定义模板
/auto-doc m2d docs/design.md --template=template/company.docx
```

## 目录结构

```
.claude/skills/auto-doc/
├── input_doc/           # 输入文档目录
│   └── example-doc.md   # 示例文档
├── output_doc/          # 输出文档目录
│   └── media/           # 提取的媒体文件
├── template/            # Word 模板目录
│   └── template.docx    # 默认模板（可选）
├── temp/                # 临时文件目录
├── m2d.sh               # Markdown 转 Word 脚本
├── d2m.sh               # Word 转 Markdown 脚本
├── skill.sh             # 技能主入口脚本
├── skill.md             # 技能说明文档
└── README.md            # 本文件
```

## 使用示例

### Markdown 转 Word

```bash
# 基本转换
/auto-doc m2d input_doc/example-doc.md

# 使用模板转换
/auto-doc m2d docs/architecture.md --template=template/tech-doc.docx
```

### Word 转 Markdown

```bash
# 转换 Word 文档
/auto-doc d2m input_doc/requirements.docx
```

## 支持的 Markdown 特性

- 标题层级 (H1-H6)
- 文本格式 (加粗、斜体、删除线)
- 列表 (有序、无序)
- 表格
- 代码块
- 图片
- **Mermaid 图表** (自动转 PNG)
- 引用
- 分隔线

## Mermaid 图表支持

支持的 Mermaid 图表类型：

- 流程图 (graph)
- 时序图 (sequenceDiagram)
- 状态图 (stateDiagram)
- ER 图 (erDiagram)
- 类图 (classDiagram)
- 甘特图 (gantt)
- 饼图 (pie)
- 等...

### Mermaid 示例

```markdown
\`\`\`mermaid
graph TB
    A[开始] --> B[处理]
    B --> C[结束]
\`\`\`
```

## 常见问题

### 1. Mermaid 图表不显示

**解决方案**: 确保 mmdc 已正确安装
```bash
npm install -g @mermaid-js/mermaid-cli
mmdc --version
```

### 2. 中文乱码

**解决方案**: 确保 Markdown 文件是 UTF-8 编码

### 3. 图片路径错误

**解决方案**: 使用相对路径引用图片

## 与其他技能配合

### 文档生成流程

```bash
# 1. 生成 Mermaid 图表
/sdlc-mermaid-diagram --type=architecture

# 2. 转换为 Word
/auto-doc m2d design.md
```

### 需求分析流程

```bash
# 1. 转换 Word 需求
/auto-doc d2m PRD.docx

# 2. 分析需求
/sdlc-requirements-analysis PRD.md
```

## 技术实现

- **Pandoc**: 文档转换核心
- **Mermaid CLI**: 图表渲染
- **Bash Script**: 自动化脚本

## 许可证

MIT License

## 贡献

欢迎提交 Issue 和 Pull Request。
