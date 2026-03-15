#!/bin/bash

###############################################################################
# UI UX Pro Max Skill 自动安装脚本
# 适用于 Claude Code 和其他 AI 助手
###############################################################################

set -e  # 遇到错误立即退出

# 颜色定义
RED='\033[0;31m'
GREEN='\033[0;32m'
YELLOW='\033[1;33m'
BLUE='\033[0;34m'
NC='\033[0m' # No Color

# 打印带颜色的消息
print_info() {
    echo -e "${BLUE}ℹ️  $1${NC}"
}

print_success() {
    echo -e "${GREEN}✅ $1${NC}"
}

print_warning() {
    echo -e "${YELLOW}⚠️  $1${NC}"
}

print_error() {
    echo -e "${RED}❌ $1${NC}"
}

print_header() {
    echo -e "\n${BLUE}═══════════════════════════════════════════════════════════════${NC}"
    echo -e "${BLUE}  UI UX Pro Max Skill 安装向导${NC}"
    echo -e "${BLUE}═══════════════════════════════════════════════════════════════${NC}\n"
}

# 检查命令是否存在
command_exists() {
    command -v "$1" >/dev/null 2>&1
}

# 检查 Python
check_python() {
    print_info "检查 Python 3.x..."

    if command_exists python3; then
        PYTHON_VERSION=$(python3 --version 2>&1 | awk '{print $2}')
        print_success "Python $PYTHON_VERSION 已安装"
        return 0
    else
        print_warning "Python 3 未安装"
        echo ""
        echo "请安装 Python 3.x:"
        echo "  macOS:   brew install python3"
        echo "  Ubuntu:  sudo apt update && sudo apt install python3"
        echo "  Windows: winget install Python.Python.3.12"
        echo ""
        return 1
    fi
}

# 检查 Node.js
check_node() {
    print_info "检查 Node.js..."

    if command_exists node && command_exists npm; then
        NODE_VERSION=$(node --version)
        NPM_VERSION=$(npm --version)
        print_success "Node.js $NODE_VERSION, npm $NPM_VERSION 已安装"
        return 0
    else
        print_warning "Node.js 或 npm 未安装"
        echo ""
        echo "请安装 Node.js:"
        echo "  macOS:   brew install node"
        echo "  Ubuntu:  sudo apt install nodejs npm"
        echo "  Windows: winget install OpenJS.NodeJS"
        echo "  或访问: https://nodejs.org/"
        echo ""
        return 1
    fi
}

# 使用 Claude Marketplace 安装
install_via_marketplace() {
    print_header
    print_info "使用 Claude Marketplace 安装（推荐）"
    echo ""
    echo "请确保你正在使用 Claude Code"
    echo ""
    echo "在 Claude Code 中运行以下命令："
    echo ""
    echo -e "${GREEN}  /plugin marketplace add nextlevelbuilder/ui-ux-pro-max-skill${NC}"
    echo -e "${GREEN}  /plugin install ui-ux-pro-max@ui-ux-pro-max-skill${NC}"
    echo ""
    echo "安装完成后，skill 将自动激活"
    echo ""
    echo "📚 官方文档: https://ui-ux-pro-max-skill.nextlevelbuilder.io/"
    echo "🔗 GitHub: https://github.com/nextlevelbuilder/ui-ux-pro-max-skill"
}

# 使用 CLI 安装
install_via_cli() {
    print_header
    print_info "使用 CLI 工具安装（通用方法）"
    echo ""

    # 检查 Node.js
    if ! check_node; then
        print_error "需要 Node.js 才能使用 CLI 安装方式"
        exit 1
    fi

    # 检查 uipro-cli 是否已安装
    if command_exists uipro; then
        print_info "uipro-cli 已安装，检查更新..."
        print_info "运行: uipro update"
        uipro update 2>/dev/null || true
    else
        print_info "安装 uipro-cli..."
        npm install -g uipro-cli
        print_success "uipro-cli 安装完成"
    fi

    echo ""
    print_info "初始化 skill..."

    # 检测当前目录
    if [ -d ".claude" ] || [ -d ".cursor" ] || [ -d ".windsurf" ]; then
        print_success "检测到 AI 助手项目目录"

        # 尝试检测使用的是哪个 AI 助手
        AI_ASSISTANT="claude"
        if [ -d ".cursor" ]; then
            AI_ASSISTANT="cursor"
        elif [ -d ".windsurf" ]; then
            AI_ASSISTANT="windsurf"
        elif [ -d ".kiro" ]; then
            AI_ASSISTANT="kiro"
        fi

        print_info "检测到 AI 助手: $AI_ASSISTANT"
        echo ""
        print_info "运行: uipro init --ai $AI_ASSISTANT"
        uipro init --ai "$AI_ASSISTANT"
        print_success "Skill 安装完成！"
    else
        print_warning "未在 AI 助手项目目录中"
        echo ""
        echo "请选择你的 AI 助手:"
        echo "  1) Claude Code"
        echo "  2) Cursor"
        echo "  3) Windsurf"
        echo "  4) Antigravity"
        echo "  5) GitHub Copilot"
        echo "  6) Kiro"
        echo "  7) Codex CLI"
        echo "  8) Qoder"
        echo "  9) Roo Code"
        echo "  10) Gemini CLI"
        echo "  11) Trae"
        echo "  12) OpenCode"
        echo "  13) Continue"
        echo "  14) CodeBuddy"
        echo "  15) All (所有助手)"
        echo ""
        read -p "请输入选项 (1-15): " choice

        case $choice in
            1) AI="claude" ;;
            2) AI="cursor" ;;
            3) AI="windsurf" ;;
            4) AI="antigravity" ;;
            5) AI="copilot" ;;
            6) AI="kiro" ;;
            7) AI="codex" ;;
            8) AI="qoder" ;;
            9) AI="roocode" ;;
            10) AI="gemini" ;;
            11) AI="trae" ;;
            12) AI="opencode" ;;
            13) AI="continue" ;;
            14) AI="codebuddy" ;;
            15) AI="all" ;;
            *) print_error "无效选项"; exit 1 ;;
        esac

        echo ""
        print_info "运行: uipro init --ai $AI"
        uipro init --ai "$AI"
        print_success "Skill 安装完成！"
    fi

    echo ""
    echo "📚 查看文档: docs/UI-UX-PRO-MAX-INSTALL.md"
    echo "🌐 官方网站: https://ui-ux-pro-max-skill.nextlevelbuilder.io/"
}

# 显示使用说明
show_usage() {
    print_header
    echo "使用方法:"
    echo ""
    echo "  $0 [选项]"
    echo ""
    echo "选项:"
    echo "  -m, --marketplace    使用 Claude Marketplace 安装（推荐）"
    echo "  -c, --cli           使用 CLI 工具安装（通用）"
    echo "  -h, --help          显示此帮助信息"
    echo ""
    echo "示例:"
    echo "  $0 --marketplace    # 使用 Marketplace 安装"
    echo "  $0 --cli           # 使用 CLI 安装"
    echo ""
}

# 主函数
main() {
    # 解析参数
    case "${1:-}" in
        -h|--help|help)
            show_usage
            exit 0
            ;;
        -m|--marketplace|marketplace)
            install_via_marketplace
            exit 0
            ;;
        -c|--cli|cli)
            install_via_cli
            exit 0
            ;;
        *)
            # 无参数时，显示菜单让用户选择
            print_header
            echo "请选择安装方式:"
            echo ""
            echo "  1) Claude Marketplace（推荐，最简单）"
            echo "  2) CLI 工具（通用，支持所有 AI 助手）"
            echo "  3) 查看帮助"
            echo ""
            read -p "请输入选项 (1-3): " choice

            case $choice in
                1)
                    install_via_marketplace
                    ;;
                2)
                    install_via_cli
                    ;;
                3)
                    show_usage
                    ;;
                *)
                    print_error "无效选项"
                    exit 1
                    ;;
            esac
            ;;
    esac
}

# 运行主函数
main "$@"
