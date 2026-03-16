#!/bin/bash

# Markdown 转 Word 脚本（支持 Mermaid 图表）
# 使用方法: ./m2d.sh [input.md] [--template=<template.docx>]

set -e

# 获取脚本所在目录的绝对路径
SCRIPT_DIR="$(cd "$(dirname "${BASH_SOURCE[0]}")" && pwd)"

# --- 1. 解析参数 ---

INPUT_FILE=""
TEMPLATE_FILE=""

# 解析命令行参数
while [[ $# -gt 0 ]]; do
    case $1 in
        --template=*)
            TEMPLATE="${1#*=}"
            TEMPLATE_FILE="$SCRIPT_DIR/template/$(basename "$TEMPLATE")"
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

# 如果没有指定输入文件，使用默认文件
if [ -z "$INPUT_FILE" ]; then
    INPUT_FILE="$SCRIPT_DIR/input_doc/example-doc.md"
    echo "未指定输入文件，使用默认文件: $INPUT_FILE"
fi

# 检查输入文件是否存在
if [ ! -f "$INPUT_FILE" ]; then
    # 如果是相对路径，尝试从脚本目录查找
    if [ -f "$SCRIPT_DIR/$INPUT_FILE" ]; then
        INPUT_FILE="$SCRIPT_DIR/$INPUT_FILE"
    else
        echo "错误: 输入文件不存在: $INPUT_FILE"
        exit 1
    fi
fi

# --- 2. 设置输出目录和文件 ---

OUTPUT_DIR="$SCRIPT_DIR/output_doc"
OUTPUT_FILE="$OUTPUT_DIR/$(basename "${INPUT_FILE%.*}").docx"

# 临时目录
TEMP_DIR="$SCRIPT_DIR/temp"
PROCESSED_MD_FILE="$TEMP_DIR/processed.md"

# --- 3. 准备环境 ---

mkdir -p "$OUTPUT_DIR"
# 彻底重建临时目录，确保环境干净
rm -rf "$TEMP_DIR"
mkdir -p "$TEMP_DIR"

echo "=== Markdown 转 Word ==="
echo "输入文件: $INPUT_FILE"
echo "输出文件: $OUTPUT_FILE"
echo "临时目录: $TEMP_DIR"
echo ""

# --- 4. 处理模板 ---

TEMPLATE_OPTION=""
if [ -n "$TEMPLATE_FILE" ] && [ -f "$TEMPLATE_FILE" ]; then
    TEMPLATE_OPTION="--reference-doc=$TEMPLATE_FILE"
    echo "使用模板: $TEMPLATE_FILE"
elif [ -f "$SCRIPT_DIR/template/template.docx" ]; then
    TEMPLATE_OPTION="--reference-doc=$SCRIPT_DIR/template/template.docx"
    echo "使用默认模板: $SCRIPT_DIR/template/template.docx"
else
    echo "未找到模板文件，使用默认样式。"
fi

echo ""

# --- 5. 预处理 Mermaid 图表 ---

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
            mermaid_src_file="$TEMP_DIR/mermaid_$i.mmd"
            png_file="$TEMP_DIR/mermaid_$i.png"

            # 将 mermaid 内容写入临时文件
            printf "%b" "$mermaid_content" > "$mermaid_src_file"

            # 使用 mmdc 渲染 PNG
            if mmdc -i "$mermaid_src_file" -o "$png_file" --backgroundColor white; then
                echo "图表 $i 渲染成功 -> $png_file"
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

echo "正在转换: $PROCESSED_MD_FILE -> $OUTPUT_FILE"

pandoc "$PROCESSED_MD_FILE" \
  --resource-path=.:"$TEMP_DIR" \
  --table-of-contents \
  --toc-depth=6 \
  --number-sections \
  $TEMPLATE_OPTION \
  -o "$OUTPUT_FILE"

echo ""
echo "=== 转换完成 ==="
echo "输出文件: $OUTPUT_FILE"
echo "提示: 临时文件保存在 '$TEMP_DIR' 目录下，可供调试。"

# 在 Mac 上自动打开生成的文档
if [[ "$OSTYPE" == "darwin"* ]]; then
    open "$OUTPUT_FILE"
fi

exit 0
