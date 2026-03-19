#!/bin/bash
# SDLC 场景检测脚本

set -e

# 颜色定义
GREEN='\033[0;32m'
YELLOW='\033[1;33m'
BLUE='\033[0;34m'
NC='\033[0m'

# 检测项目场景
detect_scenario() {
    local current_dir=$(pwd)

    echo -e "${BLUE}=== SDLC 场景检测 ===${NC}\n"

    # 检查是否为新项目
    if is_new_project "$current_dir"; then
        echo -e "${GREEN}检测到场景: 新项目开发${NC}"
        echo "建议文档: requirements.md, architecture.md, api-specs.md, data-models.md, test-plan.md, deployment.md"
        echo "工作流: SDLC-Framework/workflows/scenarios/new-project.md"
        return 0
    fi

    # 检查是否为遗留项目
    if is_legacy_project "$current_dir"; then
        echo -e "${GREEN}检测到场景: 遗留项目维护${NC}"
        echo "建议文档: change-request.md, change-design.md, test-checklist.md"
        echo "工作流: SDLC-Framework/workflows/scenarios/legacy-maintenance.md"
        return 0
    fi

    # 默认为 Bug 修复场景（需要用户意图确认）
    echo -e "${YELLOW}检测到场景: Bug 修复 (需要确认)${NC}"
    echo "建议文档: bug-analysis.md, fix-verification.md"
    echo "工作流: SDLC-Framework/workflows/scenarios/bug-fix.md"
    echo ""
    echo "提示: Bug 修复场景由用户意图触发（包含 fix/bug/issue/修复 等关键词）"
}

# 检查是否为新项目
is_new_project() {
    local dir="$1"

    # 检查目录是否为空或仅包含初始化文件
    local file_count=$(find "$dir" -maxdepth 1 -type f | wc -l)
    if [ "$file_count" -eq 0 ]; then
        return 0
    fi

    # 检查是否存在 pom.xml 或 package.json
    if [ ! -f "$dir/pom.xml" ] && [ ! -f "$dir/package.json" ]; then
        return 0
    fi

    return 1
}

# 检查是否为遗留项目
is_legacy_project() {
    local dir="$1"

    # 检查是否存在 pom.xml 或 package.json
    if [ -f "$dir/pom.xml" ] || [ -f "$dir/package.json" ]; then
        # 检查是否存在源代码目录
        if [ -d "$dir/src" ] || [ -d "$dir/app" ] || [ -d "$dir/lib" ]; then
            return 0
        fi
    fi

    return 1
}

# 显示项目信息
show_project_info() {
    local dir="$1"

    echo -e "${BLUE}=== 项目信息 ===${NC}\n"

    # 项目类型
    if [ -f "$dir/pom.xml" ]; then
        echo "项目类型: Maven 项目"
        local group_id=$(grep "groupId" "$dir/pom.xml" | head -1 | sed 's/.*<groupId>\(.*\)<\/groupId>.*/\1/')
        local artifact_id=$(grep "artifactId" "$dir/pom.xml" | head -1 | sed 's/.*<artifactId>\(.*\)<\/artifactId>.*/\1/')
        local version=$(grep "version" "$dir/pom.xml" | head -1 | sed 's/.*<version>\(.*\)<\/version>.*/\1/')
        echo "Group ID: $group_id"
        echo "Artifact ID: $artifact_id"
        echo "Version: $version"
    elif [ -f "$dir/package.json" ]; then
        echo "项目类型: Node.js 项目"
        local name=$(grep '"name"' "$dir/package.json" | head -1 | sed 's/.*"\(.*\)".*\(.*\).*/\2/')
        local version=$(grep '"version"' "$dir/package.json" | head -1 | sed 's/.*"\(.*\)".*\(.*\).*/\2/')
        echo "Name: $name"
        echo "Version: $version"
    else
        echo "项目类型: 未知"
    fi

    echo ""

    # 源代码目录
    echo "源代码目录:"
    for src_dir in "src" "app" "lib"; do
        if [ -d "$dir/$src_dir" ]; then
            echo "  - $src_dir/"
        fi
    done

    echo ""

    # 检测到的框架
    echo "检测到的框架:"
    if [ -f "$dir/pom.xml" ]; then
        if grep -q "spring-boot" "$dir/pom.xml"; then
            echo "  - Spring Boot"
        fi
        if grep -q "mybatis" "$dir/pom.xml"; then
            echo "  - MyBatis"
        fi
    fi
    if [ -f "$dir/package.json" ]; then
        if grep -q "vue" "$dir/package.json"; then
            echo "  - Vue.js"
        fi
        if grep -q "react" "$dir/package.json"; then
            echo "  - React"
        fi
    fi
}

# 主函数
main() {
    local dir="${1:-.}"

    detect_scenario "$dir"
    echo ""
    show_project_info "$dir"
}

main "$@"
