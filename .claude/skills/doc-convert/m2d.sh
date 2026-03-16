#!/bin/bash

# Markdown 转 Word 脚本（支持 Mermaid 图表）
# 使用方法: ./m2d.sh [input.md] [--template=<template.docx>]

set -e

# 获取脚本所在目录
SCRIPT_DIR="$(cd "$(dirname "${BASH_SOURCE[0]}")" && pwd)"

# 加载配置
source "$SCRIPT_DIR/config.sh"

# --- 1. 解析参数 ---

INPUT_FILE=""
TEMPLATE_FILE=""

# 解析命令行参数
while [[ $# -gt 0 ]]; do
    case $1 in
        --template=*)
            TEMPLATE="${1#*=}"
            # 模板文件从工作目录的 template 目录查找
            TEMPLATE_FILE="$AUTODOC_TEMPLATE_DIR/$(basename "$TEMPLATE")"
            shift
            ;;
        *.md)
            INPUT_FILE="$1"
            shift
            ;;
        *)
            echo "未知参数: $1"
            echo "使用方法: $0 [input.md] [--template=<template.docx>]"
            exit 1
            ;;
    esac
done

# 如果没有指定输入文件，报错
if [ -z "$INPUT_FILE" ]; then
    echo "错误: 请指定输入的 Markdown 文件"
    echo "使用方法: $0 <input.md> [--template=<template.docx>]"
    exit 1
fi

# 检查输入文件是否存在
if [ ! -f "$INPUT_FILE" ]; then
    echo "错误: 输入文件不存在: $INPUT_FILE"
    exit 1
fi

# --- 2. 初始化工作目录 ---

mkdir -p "$AUTODOC_WORKSPACE"
mkdir -p "$AUTODOC_OUTPUT_DIR"
mkdir -p "$AUTODOC_TEMPLATE_DIR"
mkdir -p "$AUTODOC_TEMP_DIR"

# --- 3. 设置输出文件 ---

# 获取输入文件名（不含路径和扩展名）
INPUT_FILENAME=$(basename "$INPUT_FILE")
DOC_NAME="${INPUT_FILENAME%.*}"

# 生成唯一的时间戳，避免文件冲突
TIMESTAMP=$(date +"%Y%m%d-%H%M%S")
UNIQUE_DOC_NAME="${DOC_NAME}-${TIMESTAMP}"

# 为每个文档创建独立的输出目录
DOC_OUTPUT_DIR="$AUTODOC_OUTPUT_DIR/$UNIQUE_DOC_NAME"
OUTPUT_FILE="$DOC_OUTPUT_DIR/${DOC_NAME}.docx"
PROCESSED_MD_FILE="$AUTODOC_TEMP_DIR/processed-${TIMESTAMP}.md"

# 创建文档专属的输出目录
mkdir -p "$DOC_OUTPUT_DIR"

echo "=== Markdown 转 Word ==="
echo "工作目录: $AUTODOC_WORKSPACE"
echo "输入文件: $INPUT_FILE"
echo "输出目录: $DOC_OUTPUT_DIR"
echo "输出文件: $OUTPUT_FILE"
echo ""

# --- 4. 处理模板 ---

TEMPLATE_OPTION=""
if [ -n "$TEMPLATE_FILE" ] && [ -f "$TEMPLATE_FILE" ]; then
    TEMPLATE_OPTION="--reference-doc=$TEMPLATE_FILE"
    echo "使用模板: $TEMPLATE_FILE"
elif [ -f "$AUTODOC_TEMPLATE_DIR/template.docx" ]; then
    TEMPLATE_OPTION="--reference-doc=$AUTODOC_TEMPLATE_DIR/template.docx"
    echo "使用默认模板"
else
    echo "未找到模板文件，使用默认样式。"
fi

echo ""

# --- 5. 预处理 Mermaid 图表 ---

# 清空临时目录中旧的同名文件
rm -f "$PROCESSED_MD_FILE"

if ! grep -q '```mermaid' "$INPUT_FILE"; then
    echo "未检测到 Mermaid 图表，直接进行转换。"
    cp "$INPUT_FILE" "$PROCESSED_MD_FILE"
else
    echo "检测到 Mermaid 图表，开始预处理..."

    # 逐行读取输入文件
    i=0
    in_mermaid_block=false
    mermaid_content=""

    # 清空或创建处理后的 markdown 文件
    > "$PROCESSED_MD_FILE"

    while IFS= read -r line || [ -n "$line" ]; do
        if [[ "$line" =~ ^\`\`\`mermaid ]]; then
            in_mermaid_block=true
            mermaid_content=""
            continue
        fi

        if [[ "$line" =~ ^\`\`\` && "$in_mermaid_block" == true ]]; then
            in_mermaid_block=false
            ((i++))
            echo "处理图表 $i..."

            # 定义临时 mermaid 源文件和输出的 PNG 文件
            mermaid_src_file="$AUTODOC_TEMP_DIR/mermaid-${TIMESTAMP}-$i.mmd"
            png_file="$AUTODOC_TEMP_DIR/mermaid-${TIMESTAMP}-$i.png"

            # 将 mermaid 内容写入临时文件
            printf "%b" "$mermaid_content" > "$mermaid_src_file"

            # 使用 mmdc 渲染 PNG
            if mmdc -i "$mermaid_src_file" -o "$png_file" --backgroundColor white; then
                echo "图表 $i 渲染成功"
                # 将图片引用写入处理后的 markdown 文件
                echo "![Mermaid Diagram $i]($(basename "$png_file"))" >> "$PROCESSED_MD_FILE"
                echo "" >> "$PROCESSED_MD_FILE"
            else
                echo "警告: 图表 $i 渲染失败，将保留原始 Mermaid 代码。" >&2
                # 如果渲染失败，将原始代码块写回
                echo '```mermaid' >> "$PROCESSED_MD_FILE"
                echo -e "$mermaid_content" >> "$PROCESSED_MD_FILE"
                echo '```' >> "$PROCESSED_MD_FILE"
            fi
            continue
        fi

        if [ "$in_mermaid_block" == true ]; then
            mermaid_content+="$line\n"
        else
            echo "$line" >> "$PROCESSED_MD_FILE"
        fi
    done < "$INPUT_FILE"
    echo "Mermaid 图表处理完成。"
fi

echo ""

# --- 6. 执行 Pandoc 转换 ---

echo "正在转换..."

# Pandoc 转换（优化列表格式）
pandoc "$PROCESSED_MD_FILE" \
  --resource-path=.:"$AUTODOC_TEMP_DIR" \
  --table-of-contents \
  --toc-depth=6 \
  --number-sections \
  --from markdown+hard_line_breaks+smart \
  --to docx \
  $TEMPLATE_OPTION \
  -o "$OUTPUT_FILE"

echo ""
echo "=== 转换完成 ==="
echo "文档目录: $DOC_OUTPUT_DIR"
echo "输出文件: $OUTPUT_FILE"
echo ""
echo "提示: 文档保存在独立目录中，不会与其他文档冲突"

# 在 Mac 上自动打开生成的文档
if [[ "$OSTYPE" == "darwin"* ]]; then
    open "$OUTPUT_FILE"
fi

exit 0
