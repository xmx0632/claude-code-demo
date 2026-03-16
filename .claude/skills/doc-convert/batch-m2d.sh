#!/bin/bash

# 批量 Markdown 转 Word 脚本
# 使用方法: ./batch-m2d.sh <目录|文件列表> [--template=<template.docx>]

set -e

# 获取脚本所在目录
SCRIPT_DIR="$(cd "$(dirname "${BASH_SOURCE[0]}")" && pwd)"

# 加载配置
source "$SCRIPT_DIR/config.sh"

M2D_SCRIPT="$SCRIPT_DIR/m2d.sh"

# 颜色输出
GREEN='\033[0;32m'
BLUE='\033[0;34m'
YELLOW='\033[1;33m'
RED='\033[0;31m'
NC='\033[0m' # No Color

# 显示使用帮助
show_help() {
    cat << EOF
批量 Markdown 转 Word 工具

用法:
    $0 <目录>                    # 转换指定目录下所有 .md 文件
    $0 <文件1.md> <文件2.md>     # 转换指定的多个文件
    $0 --pattern="**/*.md"       # 使用通配符模式查找文件

选项:
    --template=<template.docx>   # 使用指定 Word 模板
    --recursive                  # 递归查找子目录中的文件
    --dry-run                    # 仅显示将要转换的文件，不执行转换

示例:
    # 转换当前目录所有 Markdown 文件
    $0 .

    # 转换指定目录
    $0 docs/

    # 递归转换所有子目录
    $0 docs/ --recursive

    # 使用自定义模板
    $0 docs/ --template=company.docx

    # 预览将要转换的文件
    $0 docs/ --dry-run

工作目录: $AUTODOC_WORKSPACE
EOF
}

# 解析参数
TARGET=""
TEMPLATE=""
RECURSIVE=false
DRY_RUN=false

while [[ $# -gt 0 ]]; do
    case $1 in
        --template=*)
            TEMPLATE="$1"
            shift
            ;;
        --recursive)
            RECURSIVE=true
            shift
            ;;
        --dry-run)
            DRY_RUN=true
            shift
            ;;
        --help|-h)
            show_help
            exit 0
            ;;
        *)
            TARGET="$TARGET $1"
            shift
            ;;
    esac
done

# 去除前导空格
TARGET=$(echo "$TARGET" | xargs)

if [ -z "$TARGET" ]; then
    echo -e "${RED}错误: 请指定目录或文件${NC}"
    echo ""
    show_help
    exit 1
fi

# 初始化工作目录
mkdir -p "$AUTODOC_WORKSPACE"
mkdir -p "$AUTODOC_OUTPUT_DIR"
mkdir -p "$AUTODOC_TEMPLATE_DIR"
mkdir -p "$AUTODOC_TEMP_DIR"

# 查找所有 Markdown 文件
find_md_files() {
    local target="$1"
    local files=()

    if [ -f "$target" ]; then
        # 单个文件
        if [[ "$target" =~ \.md$ ]]; then
            files+=("$target")
        fi
    elif [ -d "$target" ]; then
        # 目录
        if [ "$RECURSIVE" = true ]; then
            # 递归查找
            while IFS= read -r -d '' file; do
                files+=("$file")
            done < <(find "$target" -type f -name "*.md" -print0)
        else
            # 仅当前目录
            while IFS= read -r -d '' file; do
                files+=("$file")
            done < <(find "$target" -maxdepth 1 -type f -name "*.md" -print0)
        fi
    fi

    echo "${files[@]}"
}

# 收集所有文件
ALL_FILES=()
for item in $TARGET; do
    if [ -e "$item" ]; then
        files=$(find_md_files "$item")
        for file in $files; do
            ALL_FILES+=("$file")
        done
    else
        echo -e "${YELLOW}警告: 路径不存在，跳过: $item${NC}"
    fi
done

# 去重
IFS=$'\n' SORTED_FILES=($(sort -u <<<"${ALL_FILES[*]}"))
unset IFS

# 检查是否有文件
if [ ${#SORTED_FILES[@]} -eq 0 ]; then
    echo -e "${RED}错误: 未找到任何 Markdown 文件${NC}"
    echo ""
    echo "提示:"
    echo "  - 确保指定的路径正确"
    echo "  - 使用 --recursive 参数递归查找子目录"
    exit 1
fi

# 显示将要转换的文件
echo ""
echo -e "${BLUE}=== 批量 Markdown 转 Word ===${NC}"
echo -e "找到 ${#SORTED_FILES[@]} 个 Markdown 文件"
echo ""

for i in "${!SORTED_FILES[@]}"; do
    printf "  [%2d] %s\n" $((i+1)) "${SORTED_FILES[$i]}"
done

echo ""

if [ "$DRY_RUN" = true ]; then
    echo -e "${YELLOW}--dry-run 模式，不执行实际转换${NC}"
    exit 0
fi

# 构建模板参数
TEMPLATE_ARG=""
if [ -n "$TEMPLATE" ]; then
    TEMPLATE_ARG="--template=$TEMPLATE"
fi

# 执行批量转换
SUCCESS_COUNT=0
FAIL_COUNT=0
FAILED_FILES=()

echo -e "${BLUE}开始批量转换...${NC}"
echo ""

TOTAL=${#SORTED_FILES[@]}
for i in "${!SORTED_FILES[@]}"; do
    file="${SORTED_FILES[$i]}"
    progress=$((i + 1))

    echo -e "${GREEN}[$progress/$TOTAL]${NC} 转换: $file"

    if bash "$M2D_SCRIPT" "$file" $TEMPLATE_ARG > /dev/null 2>&1; then
        echo -e "       ${GREEN}✓ 转换成功${NC}"
        ((SUCCESS_COUNT++))
    else
        echo -e "       ${RED}✗ 转换失败${NC}"
        ((FAIL_COUNT++))
        FAILED_FILES+=("$file")
    fi
    echo ""
done

# 显示转换结果
echo -e "${BLUE}=== 转换完成 ===${NC}"
echo ""
echo -e "总计:     $TOTAL 个文件"
echo -e "${GREEN}成功:     $SUCCESS_COUNT 个${NC}"
if [ $FAIL_COUNT -gt 0 ]; then
    echo -e "${RED}失败:     $FAIL_COUNT 个${NC}"
    echo ""
    echo -e "${RED}失败的文件:${NC}"
    for file in "${FAILED_FILES[@]}"; do
        echo "  - $file"
    done
fi

echo ""
echo "输出目录: $AUTODOC_OUTPUT_DIR"

# 如果有失败的文件，返回错误码
if [ $FAIL_COUNT -gt 0 ]; then
    exit 1
fi

exit 0
