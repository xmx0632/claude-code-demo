#!/bin/bash

###############################################################################
# 更新 Main 分支脚本 (Update Main Script)
#
# 功能：
#   切换到 main 分支并拉取最新代码
#
# 用法：
#   ./update-main.sh
#
# 说明：
#   - 保存当前分支状态
#   - 切换到 main 分支
#   - 拉取最新代码
#   - 提示返回原分支
#
# 场景：
#   - 在开始新功能前确保 main 是最新的
#   - 更新本地 main 分支以获取最新变更
###############################################################################

set -e  # 遇到错误立即退出

# 颜色定义
RED='\033[0;31m'
GREEN='\033[0;32m'
YELLOW='\033[1;33m'
BLUE='\033[0;34m'
NC='\033[0m' # No Color

# 打印带颜色的信息
print_info() {
    echo -e "${BLUE}ℹ${NC} $1"
}

print_success() {
    echo -e "${GREEN}✓${NC} $1"
}

print_warning() {
    echo -e "${YELLOW}⚠${NC} $1"
}

print_error() {
    echo -e "${RED}✗${NC} $1"
}

# 检查是否在 git 仓库中
if ! git rev-parse --git-dir > /dev/null 2>&1; then
    print_error "当前目录不是 git 仓库"
    exit 1
fi

# 保存当前分支
PREVIOUS_BRANCH=$(git branch --show-current)

# 检查当前分支
if [ "$PREVIOUS_BRANCH" = "main" ]; then
    print_info "已在 main 分支上"
    echo ""

    # 检查是否有未提交的更改
    if ! git diff-index --quiet HEAD --; then
        print_warning "检测到未提交的更改"
        echo ""
        git status --short
        echo ""
    fi

    # 直接拉取
    print_info "拉取 main 分支最新代码..."
    git pull origin main
    print_success "main 分支已更新到最新"
    echo ""

    exit 0
fi

# 检查是否有未提交的更改
if ! git diff-index --quiet HEAD --; then
    print_warning "检测到未提交的更改"
    echo ""
    git status --short
    echo ""
    print_warning "切换分支前建议先提交或暂存更改"
    echo ""
    read -p "是否继续？(y/N) " -n 1 -r
    echo
    if [[ ! $REPLY =~ ^[Yy]$ ]]; then
        print_info "操作已取消"
        echo ""
        echo "提示：如需暂存更改，可以使用:"
        echo "  git stash"
        echo "  [更新完成后恢复]"
        echo "  git stash pop"
        exit 0
    fi
fi

# 切换到 main 分支
print_info "从 ${PREVIOUS_BRANCH} 切换到 main 分支..."
if ! git checkout main 2>/dev/null; then
    print_error "无法切换到 main 分支"
    print_info "尝试从 origin/main 创建 main 分支..."
    git checkout -b main origin/main
fi

# 拉取最新代码
print_info "拉取 main 分支最新代码..."
git pull origin main

# 显示成功信息
echo ""
print_success "main 分支已更新到最新"
echo ""
echo -e "${GREEN}当前分支:${NC} $(git branch --show-current)"
echo ""
echo "下一步："
echo "  1. 返回原分支: git checkout ${PREVIOUS_BRANCH}"
echo "  2. 或创建新功能分支: ./scripts/start-feature.sh <feature-name>"
echo "  3. 或使用便利脚本: git checkout -"
echo ""
