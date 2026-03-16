#!/bin/bash

# Word 转 Markdown 脚本
# 使用方法: ./d2m.sh <input.docx>

set -e

# 获取脚本所在目录的绝对路径
SCRIPT_DIR="$(cd "$(dirname "${BASH_SOURCE[0]}")" && pwd)"

# 检查是否提供了输入文件
if [ "$#" -ne 1 ]; then
    echo "使用方法: $0 <input.docx>"
    exit 1
fi

INPUT_FILE="$1"

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

# 创建输出目录
OUTPUT_DIR="$SCRIPT_DIR/output_doc"
mkdir -p "$OUTPUT_DIR"

# 获取输入文件名（不含路径和扩展名）
INPUT_FILENAME=$(basename "$INPUT_FILE")
OUTPUT_FILE="$OUTPUT_DIR/${INPUT_FILENAME%.*}.md"
MEDIA_DIR="$OUTPUT_DIR/media"

# 创建媒体目录
mkdir -p "$MEDIA_DIR"

echo "=== Word 转 Markdown ==="
echo "输入文件: $INPUT_FILE"
echo "输出文件: $OUTPUT_FILE"
echo ""

# 使用 pandoc 转换文档
pandoc "$INPUT_FILE" \
    --wrap=preserve \
    --extract-media="$MEDIA_DIR" \
    -f docx \
    -t markdown \
    --standalone \
    -o "$OUTPUT_FILE"

# 修复图片路径，确保指向正确的 media 目录
perl -i -pe 's#!\[(.*?)\]\([^)]*?media/([^)]+)\)#![\1](./media/\2)#g' "$OUTPUT_FILE"

echo ""
echo "转换完成！"
echo "输出文件: $OUTPUT_FILE"
echo "媒体文件已保存到: $MEDIA_DIR/"

# 在 Mac 上自动打开生成的 Markdown 文件
if [[ "$OSTYPE" == "darwin"* ]]; then
    open "$OUTPUT_FILE"
fi

exit 0
