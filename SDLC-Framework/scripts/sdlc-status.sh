#!/bin/bash
# SDLC 文档状态管理脚本

set -e

# 配置
STATUS_FILE=".sdlc/docs-status.yaml"
FRAMEWORK_CONFIG="SDLC-Framework/config/framework.yaml"

# 颜色定义
RED='\033[0;31m'
GREEN='\033[0;32m'
YELLOW='\033[1;33m'
BLUE='\033[0;34m'
NC='\033[0m' # No Color

# 帮助信息
show_help() {
    cat << EOF
SDLC 文档状态管理工具

用法:
    sdlc-status init                    初始化状态文件
    sdlc-status status [doc]            查看文档状态
    sdlc-status set <doc> <status>      设置文档状态
    sdlc-status approve <doc>           批准文档
    sdlc-status block <doc>             阻塞文档
    sdlc-status blocked-by <doc>        查看阻塞关系
    sdlc-status dashboard               生成状态看板

状态值:
    draft       - 草稿，编辑中
    in_review   - 待审查
    approved    - 已批准，可作为依据
    deprecated  - 已废弃，不可使用
    blocked     - 被阻塞，等待依赖

示例:
    sdlc-status init
    sdlc-status status requirements.md
    sdlc-status set architecture.md approved
    sdlc-status approve api-specs.md
    sdlc-status blocked-by api-specs.md
    sdlc-status dashboard
EOF
}

# 检查状态文件是否存在
check_status_file() {
    if [ ! -f "$STATUS_FILE" ]; then
        echo -e "${RED}错误: 状态文件不存在，请先运行 'sdlc-status init'${NC}"
        exit 1
    fi
}

# 初始化状态文件
init_status() {
    mkdir -p .sdlc

    if [ -f "$STATUS_FILE" ]; then
        echo -e "${YELLOW}警告: 状态文件已存在${NC}"
        read -p "是否覆盖? (y/N): " -n 1 -r
        echo
        if [[ ! $REPLY =~ ^[Yy]$ ]]; then
            exit 0
        fi
    fi

    # 读取项目信息
    read -p "项目名称: " project_name
    read -p "项目类型 (new/legacy): " project_type

    cat > "$STATUS_FILE" << EOF
project:
  name: "$project_name"
  type: "$project_type"
  created_at: "$(date -u +"%Y-%m-%dT%H:%M:%SZ")"
  scenario: "detecting"

documents: {}
EOF

    echo -e "${GREEN}✓ 状态文件已创建: $STATUS_FILE${NC}"
}

# 查看文档状态
show_status() {
    check_status_file

    local doc="$1"

    if [ -z "$doc" ]; then
        # 显示所有文档状态
        echo -e "${BLUE}=== 文档状态 ===${NC}"
        echo ""
        grep -A 5 "documents:" "$STATUS_FILE" | tail -n +2 || echo "暂无文档"
    else
        # 显示特定文档状态
        local status=$(grep -A 10 "$doc:" "$STATUS_FILE" | grep "status:" | awk '{print $2}')
        local version=$(grep -A 10 "$doc:" "$STATUS_FILE" | grep "version:" | awk '{print $2}')
        local owner=$(grep -A 10 "$doc:" "$STATUS_FILE" | grep "owner:" | awk '{print $2}')

        if [ -z "$status" ]; then
            echo -e "${RED}文档 '$doc' 不存在${NC}"
            exit 1
        fi

        echo "文档: $doc"
        echo "状态: $status"
        echo "版本: $version"
        echo "负责人: $owner"
    fi
}

# 设置文档状态
set_status() {
    check_status_file

    local doc="$1"
    local status="$2"

    if [ -z "$doc" ] || [ -z "$status" ]; then
        echo -e "${RED}用法: sdlc-status set <doc> <status>${NC}"
        exit 1
    fi

    # 验证状态值
    case "$status" in
        draft|in_review|approved|deprecated|blocked)
            ;;
        *)
            echo -e "${RED}错误: 无效的状态值 '$status'${NC}"
            exit 1
            ;;
    esac

    # 使用 yq 更新状态（如果没有 yq，使用 sed）
    if command -v yq &> /dev/null; then
        yq eval ".documents.\"$doc\".status = \"$status\"" -i "$STATUS_FILE"
        yq eval ".documents.\"$doc\".updated_at = \"$(date -u +"%Y-%m-%dT%H:%M:%SZ")\"" -i "$STATUS_FILE"
    else
        # 简单的 sed 替换（假设文档已存在）
        sed -i.bak "s/\\($doc:.*status: \)\"[^\"]*\"/\\1\"$status\"/" "$STATUS_FILE"
        rm -f "${STATUS_FILE}.bak"
    fi

    echo -e "${GREEN}✓ 文档 '$doc' 状态已更新为: $status${NC}"
}

# 批准文档
approve_doc() {
    set_status "$1" "approved"
    echo -e "${GREEN}✓ 文档 '$1' 已批准${NC}"
}

# 阻塞文档
block_doc() {
    set_status "$1" "blocked"
    echo -e "${YELLOW}⚠ 文档 '$1' 已被阻塞${NC}"
}

# 查看阻塞关系
show_blocked_by() {
    check_status_file

    local doc="$1"

    if [ -z "$doc" ]; then
        echo -e "${RED}用法: sdlc-status blocked-by <doc>${NC}"
        exit 1
    fi

    echo -e "${BLUE}=== $doc 的阻塞关系 ===${NC}\n"

    # 显示依赖的文档
    local deps=$(grep -A 10 "$doc:" "$STATUS_FILE" | grep "dependencies:" -A 10 | grep "- " | sed 's/.*- //' || echo "")

    if [ -n "$deps" ]; then
        echo "依赖的文档:"
        for dep in $deps; do
            local dep_status=$(grep -A 10 "$dep:" "$STATUS_FILE" | grep "status:" | awk '{print $2}')
            local icon="✓"
            if [ "$dep_status" != "approved" ]; then
                icon="✗"
            fi
            echo "  $icon $dep ($dep_status)"
        done
    else
        echo "无依赖"
    fi

    echo ""

    # 显示被此文档阻塞的文档
    echo "被此文档阻塞的文档:"
    local blocking=$(grep -B 5 "$doc" "$STATUS_FILE" | grep "dependencies:" -B 5 | grep "^[a-z]" | grep -v "dependencies:" || echo "")
    if [ -n "$blocking" ]; then
        for blocked in $blocking; do
            if [ "$blocked" != "$doc" ]; then
                local blocked_status=$(grep -A 10 "$blocked:" "$STATUS_FILE" | grep "status:" | awk '{print $2}')
                echo "  • $blocked ($blocked_status)"
            fi
        done
    else
        echo "无"
    fi
}

# 生成状态看板
show_dashboard() {
    check_status_file

    echo -e "${BLUE}=== SDLC 文档状态看板 ===${NC}\n"

    # 项目信息
    local project_name=$(grep "name:" "$STATUS_FILE" | head -1 | awk '{print $2}')
    local project_type=$(grep "type:" "$STATUS_FILE" | head -1 | awk '{print $2}')
    local scenario=$(grep "scenario:" "$STATUS_FILE" | awk '{print $2}')

    echo "项目: $project_name"
    echo "类型: $project_type"
    echo "场景: $scenario"
    echo ""

    # 统计各状态的文档数量
    local draft=0 in_review=0 approved=0 deprecated=0 blocked=0

    while IFS= read -r line; do
        if [[ $line =~ status: ]]; then
            local status=$(echo "$line" | awk '{print $2}')
            case "$status" in
                draft) ((draft++)) ;;
                in_review) ((in_review++)) ;;
                approved) ((approved++)) ;;
                deprecated) ((deprecated++)) ;;
                blocked) ((blocked++)) ;;
            esac
        fi
    done < "$STATUS_FILE"

    # 看板表格
    printf "%-15s %-15s %-15s %-15s %-15s\n" "草稿" "审查中" "已批准" "已废弃" "被阻塞"
    printf "%-15s %-15s %-15s %-15s %-15s\n" "----" "------" "------" "------" "------"
    printf "%-15s %-15s %-15s %-15s %-15s\n" "$draft" "$in_review" "$approved" "$deprecated" "$blocked"

    echo ""

    # 详细列表
    echo "详细状态:"
    grep "^[a-z]" "$STATUS_FILE" | grep -v "project\|documents\|dependencies\|blocking\|reviewers" | while read -r line; do
        if [[ $line =~ ^[a-z] ]]; then
            local doc=$(echo "$line" | cut -d: -f1)
            local status=$(grep -A 5 "$doc:" "$STATUS_FILE" | grep "status:" | awk '{print $2}')
            local owner=$(grep -A 5 "$doc:" "$STATUS_FILE" | grep "owner:" | awk '{print $2}')

            local icon="○"
            case "$status" in
                draft) icon="○" ;;
                in_review) icon="◐" ;;
                approved) icon="●" ;;
                deprecated) icon="✗" ;;
                blocked) icon="⊘" ;;
            esac

            printf "  $icon %-30s %-10s %s\n" "$doc" "$status" "$owner"
        fi
    done
}

# 主函数
main() {
    local command="$1"
    shift || true

    case "$command" in
        init)
            init_status
            ;;
        status)
            show_status "$@"
            ;;
        set)
            set_status "$@"
            ;;
        approve)
            approve_doc "$@"
            ;;
        block)
            block_doc "$@"
            ;;
        blocked-by)
            show_blocked_by "$@"
            ;;
        dashboard)
            show_dashboard
            ;;
        *)
            show_help
            ;;
    esac
}

main "$@"
