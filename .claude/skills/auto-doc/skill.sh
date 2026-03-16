#!/bin/bash

# AutoDoc Skill 主脚本
# 文档转换工具：支持 Markdown 与 Word 双向转换

set -e

# 获取脚本所在目录
SCRIPT_DIR="$(cd "$(dirname "${BASH_SOURCE[0]}")" && pwd)"
M2D_SCRIPT="$SCRIPT_DIR/m2d.sh"
D2M_SCRIPT="$SCRIPT_DIR/d2m.sh"

# 显示使用帮助
show_help() {
    cat << EOF
AutoDoc - 自动文档转换工具

用法:
    /auto-doc m2d <input.md> [--template=<template.docx>]    # Markdown 转 Word
    /auto-doc d2m <input.docx>                              # Word 转 Markdown
    /auto-doc help                                          # 显示帮助信息

示例:
    # 转换单个 Markdown 文件
    /auto-doc m2d input_doc/example-doc.md

    # 使用自定义模板转换
    /auto-doc m2d docs/design.md --template=template/company.docx

    # 转换 Word 文档为 Markdown
    /auto-doc d2m output_doc/requirements.docx

目录结构:
    .claude/skills/auto-doc/
    ├── input_doc/           # 输入文档目录
    ├── output_doc/          # 输出文档目录
    │   └── media/           # 提取的媒体文件
    ├── template/            # Word 模板目录
    │   └── template.docx    # 默认模板（可选）
    ├── temp/                # 临时文件目录
    ├── m2d.sh               # Markdown 转 Word 脚本
    ├── d2m.sh               # Word 转 Markdown 脚本
    └── skill.md             # 技能说明文档

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

# Markdown 转 Word
m2d() {
    if [ ! -f "$M2D_SCRIPT" ]; then
        echo "错误: 找不到转换脚本: $M2D_SCRIPT"
        exit 1
    fi

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
        help|--help|-h)
            show_help
            ;;
        *)
            echo "错误: 未知命令 '$command'"
            echo ""
            show_help
            exit 1
            ;;
    esac
}

main "$@"
