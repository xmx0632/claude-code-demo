#!/bin/bash

# AutoDoc Skill 主脚本
# 文档转换工具：支持 Markdown 与 Word 双向转换

set -e

# 获取脚本所在目录
SCRIPT_DIR="$(cd "$(dirname "${BASH_SOURCE[0]}")" && pwd)"

# 加载配置
source "$SCRIPT_DIR/config.sh"

M2D_SCRIPT="$SCRIPT_DIR/m2d.sh"
D2M_SCRIPT="$SCRIPT_DIR/d2m.sh"
BATCH_M2D_SCRIPT="$SCRIPT_DIR/batch-m2d.sh"

# 显示使用帮助
show_help() {
    cat << EOF
AutoDoc - 自动文档转换工具

用法:
    /auto-doc <file>                          # 自动识别文件类型并转换
    /auto-doc m2d <file.md>                   # Markdown 转 Word
    /auto-doc d2m <file.docx>                 # Word 转 Markdown
    /auto-doc batch <目录|文件...>            # 批量转换 Markdown 为 Word
    /auto-doc help                           # 显示帮助信息

示例:
    # 自动识别并转换（推荐）
    /auto-doc design.md                      # 转为 Word
    /auto-doc requirements.docx              # 转为 Markdown

    # 手动指定转换类型
    /auto-doc m2d docs/architecture.md
    /auto-doc d2m output/requirements.docx

    # 使用自定义模板
    /auto-doc m2d docs/design.md --template=company.docx

    # 批量转换
    /auto-doc batch docs/                    # 转换目录下所有 MD
    /auto-doc batch docs/ --recursive        # 递归转换子目录
    /auto-doc batch *.md                     # 转换当前目录所有 MD
    /auto-doc batch file1.md file2.md        # 转换指定多个文件

工作目录:
    所有文档操作在项目目录下的固定工作目录进行
    位置: $AUTODOC_WORKSPACE

    目录结构:
    ├── input/       # 可选：存放输入文档
    ├── output/      # 转换后的文档输出到这里
    │   ├── design-20250316-153045/    # 每个文档独立目录（避免冲突）
    │   │   ├── design.md              # Markdown 文件
    │   │   ├── design.docx            # Word 文件
    │   │   └── media/                 # 该文档专属的图片目录
    │   └── prd-20250316-153120/
    │       ├── prd.md
    │       ├── prd.docx
    │       └── media/
    ├── template/    # 可选：存放 Word 模板
    └── temp/        # 临时文件

环境要求:
    - pandoc: brew install pandoc
    - mermaid-cli: npm install -g @mermaid-js/mermaid-cli
    - python3: 系统自带

验证安装:
    pandoc --version
    mmdc --version

更多信息: .claude/skills/auto-doc/skill.md
EOF
}

# 初始化工作目录
init_workspace() {
    mkdir -p "$AUTODOC_WORKSPACE"
    mkdir -p "$AUTODOC_OUTPUT_DIR"
    mkdir -p "$AUTODOC_TEMPLATE_DIR"
    mkdir -p "$AUTODOC_TEMP_DIR"
    mkdir -p "$AUTODOC_INPUT_DIR"

    echo "工作目录已初始化: $AUTODOC_WORKSPACE"
}

# 自动检测文件类型并转换
auto_convert() {
    if [ $# -eq 0 ]; then
        echo "错误: 请指定要转换的文件"
        echo ""
        show_help
        exit 1
    fi

    local file="$1"
    local ext="${file##*.}"
    local filename=$(basename "$file")
    local basedir=$(dirname "$file")

    # 初始化工作目录
    init_workspace

    case "$ext" in
        md|markdown)
            echo "检测到 Markdown 文件，转换为 Word..."
            bash "$M2D_SCRIPT" "$@"
            ;;
        docx|doc)
            echo "检测到 Word 文件，转换为 Markdown..."
            bash "$D2M_SCRIPT" "$@"
            ;;
        *)
            echo "错误: 不支持的文件类型 '.$ext'"
            echo ""
            echo "支持的文件类型:"
            echo "  - Markdown: .md, .markdown"
            echo "  - Word: .docx, .doc"
            echo ""
            echo "或者手动指定转换类型:"
            echo "  /auto-doc m2d file.md"
            echo "  /auto-doc d2m file.docx"
            exit 1
            ;;
    esac
}

# Markdown 转 Word
m2d() {
    if [ ! -f "$M2D_SCRIPT" ]; then
        echo "错误: 找不到转换脚本: $M2D_SCRIPT"
        exit 1
    fi

    # 初始化工作目录
    init_workspace

    # 检查是否提供了输入文件
    if [ $# -eq 0 ]; then
        echo "错误: 请指定输入的 Markdown 文件"
        echo ""
        show_help
        exit 1
    fi

    # 调用 m2d.sh 脚本
    bash "$M2D_SCRIPT" "$@"
}

# Word 转 Markdown
d2m() {
    if [ ! -f "$D2M_SCRIPT" ]; then
        echo "错误: 找不到转换脚本: $D2M_SCRIPT"
        exit 1
    fi

    # 初始化工作目录
    init_workspace

    # 检查是否提供了输入文件
    if [ $# -eq 0 ]; then
        echo "错误: 请指定输入的 Word 文件"
        echo ""
        show_help
        exit 1
    fi

    # 调用 d2m.sh 脚本
    bash "$D2M_SCRIPT" "$@"
}

# 批量 Markdown 转 Word
batch_m2d() {
    if [ ! -f "$BATCH_M2D_SCRIPT" ]; then
        echo "错误: 找不到批量转换脚本: $BATCH_M2D_SCRIPT"
        exit 1
    fi

    # 初始化工作目录
    init_workspace

    # 调用 batch-m2d.sh 脚本
    bash "$BATCH_M2D_SCRIPT" "$@"
}

# 主函数
main() {
    if [ $# -eq 0 ]; then
        show_help
        exit 0
    fi

    local command="$1"
    shift

    case "$command" in
        m2d)
            m2d "$@"
            ;;
        d2m)
            d2m "$@"
            ;;
        batch)
            batch_m2d "$@"
            ;;
        init)
            init_workspace
            ;;
        help|--help|-h)
            show_help
            ;;
        *)
            # 自动检测文件类型
            auto_convert "$command" "$@"
            ;;
    esac
}

main "$@"
