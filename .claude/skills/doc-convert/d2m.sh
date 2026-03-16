#!/bin/bash

# Word 转 Markdown 脚本
# 使用方法: ./d2m.sh <input.docx>

set -e

# 获取脚本所在目录
SCRIPT_DIR="$(cd "$(dirname "${BASH_SOURCE[0]}")" && pwd)"

# 加载配置
source "$SCRIPT_DIR/config.sh"

# 检查是否提供了输入文件
if [ "$#" -ne 1 ]; then
    echo "使用方法: $0 <input.docx>"
    exit 1
fi

INPUT_FILE="$1"

# 检查输入文件是否存在
if [ ! -f "$INPUT_FILE" ]; then
    echo "错误: 输入文件不存在: $INPUT_FILE"
    exit 1
fi

# --- 初始化工作目录 ---

mkdir -p "$AUTODOC_WORKSPACE"
mkdir -p "$AUTODOC_OUTPUT_DIR"
mkdir -p "$AUTODOC_TEMP_DIR"

# 获取输入文件名（不含路径和扩展名）
INPUT_FILENAME=$(basename "$INPUT_FILE")
DOC_NAME="${INPUT_FILENAME%.*}"

# 生成唯一的时间戳，避免文件冲突
TIMESTAMP=$(date +"%Y%m%d-%H%M%S")
UNIQUE_DOC_NAME="${DOC_NAME}-${TIMESTAMP}"

# 为每个文档创建独立的输出目录和媒体目录
DOC_OUTPUT_DIR="$AUTODOC_OUTPUT_DIR/$UNIQUE_DOC_NAME"
MEDIA_DIR="$DOC_OUTPUT_DIR/media"
OUTPUT_FILE="$DOC_OUTPUT_DIR/${DOC_NAME}.md"

# 创建文档专属的输出目录
mkdir -p "$MEDIA_DIR"

echo "=== Word 转 Markdown ==="
echo "工作目录: $AUTODOC_WORKSPACE"
echo "输入文件: $INPUT_FILE"
echo "输出目录: $DOC_OUTPUT_DIR"
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

# 修复嵌套的 media 目录问题
if [ -d "$MEDIA_DIR/media" ]; then
    mv "$MEDIA_DIR/media/"* "$MEDIA_DIR/" 2>/dev/null
    rmdir "$MEDIA_DIR/media" 2>/dev/null
    # 重新修复路径
    perl -i -pe 's#!\[(.*?)\]\(./media/media/([^)]+)\)#![\1](./media/\2)#g' "$OUTPUT_FILE"
fi

echo ""
echo "转换完成！"
echo "文档目录: $DOC_OUTPUT_DIR"
echo "Markdown 文件: $OUTPUT_FILE"
echo "媒体文件: $MEDIA_DIR/"
echo ""
echo "提示: 文档保存在独立目录中，不会与其他文档冲突"

# 在 Mac 上自动打开生成的 Markdown 文件
if [[ "$OSTYPE" == "darwin"* ]]; then
    open "$OUTPUT_FILE"
fi

exit 0
