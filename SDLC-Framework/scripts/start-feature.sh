#!/bin/bash

###############################################################################
# 功能开发启动脚本 (Start Feature Script)
#
# 功能：
#   从 main 分支创建新的功能分支
#
# 用法：
#   ./start-feature.sh <feature-name>
#
# 示例：
#   ./start-feature.sh user-authentication
#   ./start-feature.sh payment-integration
#
# 说明：
#   - 脚本会确保在 main 分支上并拉取最新代码
#   - 然后创建 feature/<feature-name> 分支
#   - 自动切换到新创建的功能分支
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

# 检查参数
if [ -z "$1" ]; then
    print_error "缺少功能名称参数"
    echo ""
    echo "用法: $0 <feature-name>"
    echo ""
    echo "示例:"
    echo "  $0 user-authentication"
    echo "  $0 payment-integration"
    echo "  $0 order-management"
    exit 1
fi

FEATURE_NAME=$1
BRANCH_NAME="feature/${FEATURE_NAME}"

# 检查是否在 git 仓库中
if ! git rev-parse --git-dir > /dev/null 2>&1; then
    print_error "当前目录不是 git 仓库"
    exit 1
fi

# 检查是否有未提交的更改
if ! git diff-index --quiet HEAD --; then
    print_warning "检测到未提交的更改"
    echo ""
    git status --short
    echo ""
    read -p "是否继续？(y/N) " -n 1 -r
    echo
    if [[ ! $REPLY =~ ^[Yy]$ ]]; then
        print_info "操作已取消"
        exit 0
    fi
fi

# 检查分支是否已存在
if git show-ref --verify --quiet "refs/heads/${BRANCH_NAME}"; then
    print_error "分支 ${BRANCH_NAME} 已存在"
    echo ""
    echo "如需切换到现有分支，请使用:"
    echo "  git checkout ${BRANCH_NAME}"
    exit 1
fi

print_info "正在从 main 分支创建功能分支..."

# 保存当前分支
CURRENT_BRANCH=$(git branch --show-current)

# 切换到 main 分支
print_info "切换到 main 分支..."
if ! git checkout main 2>/dev/null; then
    print_error "无法切换到 main 分支"
    print_info "尝试从 origin/main 创建 main 分支..."
    git checkout -b main origin/main
fi

# 拉取最新代码
print_info "拉取 main 分支最新代码..."
git pull origin main

# 创建功能分支
print_info "创建功能分支: ${BRANCH_NAME}"
git checkout -b "$BRANCH_NAME"

# 显示成功信息
echo ""
print_success "功能分支创建成功！"
echo ""
echo -e "${GREEN}分支名称:${NC} ${BRANCH_NAME}"
echo -e "${GREEN}当前分支:${NC} $(git branch --show-current)"
echo ""
echo "下一步："
echo "  1. 开始开发功能"
echo "  2. 提交代码: git add . && git commit -m 'feat: 描述'"
echo "  3. 推送到远程: git push -u origin ${BRANCH_NAME}"
echo "  4. 完成功能后运行: ./scripts/finish-feature.sh"
echo ""
