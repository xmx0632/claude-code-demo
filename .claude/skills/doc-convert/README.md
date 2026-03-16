# AutoDoc Skill - 自动文档转换

## 简介

AutoDoc 是一个完整的文档转换技能，支持 Markdown 与 Word 文档的双向转换，并能自动将 Mermaid 图表转换为 PNG 图片插入 Word 文档。

## 功能特性

- ✅ **Markdown 转 Word**: 将 Markdown 文档转换为格式规范的 Word 文档
- ✅ **Word 转 Markdown**: 将 Word 文档转换为 Markdown 格式，便于 AI 分析
- ✅ **Mermaid 图表支持**: 自动将 Mermaid 图表渲染为 PNG 图片并嵌入文档
- ✅ **自定义模板**: 支持使用自定义 Word 模板
- ✅ **独立运行**: 无需依赖外部 autoDoc 目录
- ✅ **独立目录**: 每个转换的文档都有独立的输出目录，避免文件冲突

## 设计亮点

### 独立目录结构

为了避免多次转换时的文件冲突，每个文档都会生成独立的时间戳目录：

```
output/
├── design-20250316-153045/   # 文档1（带时间戳）
│   ├── design.md
│   ├── design.docx
│   └── media/
└── prd-20250316-153120/      # 文档2（带时间戳）
    ├── prd.md
    ├── prd.docx
    └── media/
```

**优势：**
- ✅ 不同文档的图片不会互相覆盖
- ✅ 便于管理和归档
- ✅ 可以保留转换历史
- ✅ 清晰的组织结构

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

### 3. 使用技能（推荐方式）

**自动识别文件类型（推荐）:**
```bash
# 自动识别并转换（最简单！）
/auto-doc design.md          # Markdown → Word
/auto-doc requirements.docx  # Word → Markdown
```

**手动指定转换类型:**
```bash
# Markdown 转 Word
/auto-doc m2d docs/architecture.md

# Word 转 Markdown
/auto-doc d2m output/requirements.docx

# 使用自定义模板
/auto-doc m2d docs/design.md --template=company.docx
```

## 工作目录结构

```
.auto-doc-workspace/              # 固定工作目录（项目根目录下）
├── input/                        # 可选：存放待转换的文档
├── output/                       # 所有转换后的文档
│   ├── design-20250316-153045/   # 每个文档独立的目录（避免冲突）
│   │   ├── design.md             # Markdown 文件
│   │   ├── design.docx           # Word 文件
│   │   └── media/                # 该文档专属的图片目录
│   │       ├── image1.png
│   │       └── image2.png
│   └── prd-20250316-153120/      # 另一个文档
│       ├── prd.md
│       ├── prd.docx
│       └── media/
│           └── image1.png
├── template/                     # 可选：自定义 Word 模板
│   └── company.docx
└── temp/                         # 临时文件
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
