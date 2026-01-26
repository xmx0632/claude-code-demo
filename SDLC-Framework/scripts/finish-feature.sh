#!/bin/bash

###############################################################################
# 功能开发完成脚本 (Finish Feature Script)
#
# 功能：
#   完成功能开发，推送代码并创建 Pull Request
#
# 用法：
#   ./finish-feature.sh
#
# 说明：
#   - 检查当前分支是否为功能分支
#   - 推送代码到远程仓库
#   - 使用 GitHub CLI 创建 Pull Request
#   - 如未安装 gh CLI，会提示手动创建 PR
#
# 依赖：
#   - GitHub CLI (gh): https://cli.github.com/
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

# 获取当前分支
CURRENT_BRANCH=$(git branch --show-current)

# 检查是否在功能分支上
if [[ ! $CURRENT_BRANCH =~ ^feature/ ]]; then
    print_error "当前不在功能分支上"
    echo ""
    echo -e "${YELLOW}当前分支:${NC} $CURRENT_BRANCH"
    echo ""
    echo "此脚本只能在功能分支上运行（feature/*）"
    exit 1
fi

# 检查是否有未提交的更改
if ! git diff-index --quiet HEAD --; then
    print_warning "检测到未提交的更改"
    echo ""
    git status --short
    echo ""
    read -p "是否先提交更改？(y/N) " -n 1 -r
    echo
    if [[ $REPLY =~ ^[Yy]$ ]]; then
        print_info "请先手动提交更改，然后重新运行此脚本"
        exit 0
    else
        print_info "操作已取消"
        exit 0
    fi
fi

# 检查是否已推送到远程
if git ls-remote --heads origin "$CURRENT_BRANCH" | grep -q "$CURRENT_BRANCH"; then
    print_info "分支已存在于远程仓库"
    HAS_REMOTE=true
else
    print_warning "分支尚未推送到远程仓库"
    HAS_REMOTE=false
fi

# 推送到远程（如果需要）
if [ "$HAS_REMOTE" = false ]; then
    print_info "推送分支到远程仓库..."
    git push -u origin "$CURRENT_BRANCH"
    print_success "推送成功"
else
    # 检查是否需要推送新的提交
    LOCAL_COMMIT=$(git rev-parse HEAD)
    REMOTE_COMMIT=$(git rev-parse origin/"$CURRENT_BRANCH")

    if [ "$LOCAL_COMMIT" != "$REMOTE_COMMIT" ]; then
        print_info "本地有新的提交，推送到远程..."
        git push origin "$CURRENT_BRANCH"
        print_success "推送成功"
    else
        print_success "代码已是最新"
    fi
fi

# 检查是否安装了 GitHub CLI
if ! command -v gh &> /dev/null; then
    print_warning "未检测到 GitHub CLI (gh)"
    echo ""
    echo "请手动创建 Pull Request："
    echo ""
    echo "  1. 访问: https://github.com/$(git config --get remote.origin.url | sed 's/.*:\(.*\)\/\(.*\).git/\1\/\2/')/pull/new/${CURRENT_BRANCH}"
    echo "  2. 选择 base: main"
    echo "  3. 选择 compare: ${CURRENT_BRANCH}"
    echo "  4. 填写 PR 标题和描述"
    echo "  5. 提交 PR"
    echo ""
    echo "安装 GitHub CLI 以便下次自动创建 PR："
    echo "  macOS: brew install gh"
    echo "  Linux: https://cli.github.com/"
    echo ""
    exit 0
fi

# 检查是否已登录 GitHub
if ! gh auth status &> /dev/null; then
    print_error "未登录 GitHub"
    echo ""
    echo "请先登录: gh auth login"
    exit 1
fi

# 从分支名提取功能描述
FEATURE_DESC=$(echo "$CURRENT_BRANCH" | sed 's/feature\///' | sed 's/-/ /g')

# 构建 PR 标题
PR_TITLE="feat: $(echo "$FEATURE_DESC" | sed 's/\b\w/\u&/g')"

# 构建 PR 描述
PR_DESC="## 变更说明
实现 ${FEATURE_DESC} 功能

## 变更类型
- [x] 新功能

## 测试情况
- [ ] 单元测试通过
- [ ] 集成测试通过
- [ ] 手动测试通过

## 检查清单
- [ ] 代码符合团队规范
- [ ] 已添加必要的注释
- [ ] 已更新相关文档

## 截图/演示
<!-- 如果是 UI 变更，添加截图或 GIF -->"

# 创建 PR
print_info "创建 Pull Request..."
echo ""

if gh pr create \
  --title "$PR_TITLE" \
  --body "$PR_DESC" \
  --base main \
  --head "$CURRENT_BRANCH"; then
    echo ""
    print_success "Pull Request 创建成功！"
    echo ""
    echo "下一步："
    echo "  1. 等待代码审查"
    echo "  2. 根据反馈修改代码"
    echo "  3. 审查通过后合并 PR"
    echo "  4. 合并后删除功能分支: git checkout main && git branch -d $CURRENT_BRANCH"
    echo ""
else
    echo ""
    print_error "创建 Pull Request 失败"
    echo ""
    echo "可能原因："
    echo "  - PR 已经存在"
    echo "  - 网络连接问题"
    echo ""
    echo "请手动检查或创建 PR"
fi
