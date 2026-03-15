#!/bin/bash
# Agent-Linter
# 输出格式专为 AI Agent 设计的 Linter 工具
#
# 输出格式: JSON
# 包含: 错误类型、位置、描述、修复建议、是否可自动修复

set -e

# 颜色定义（终端显示用）
RED='\033[0;31m'
GREEN='\033[0;32m'
YELLOW='\033[1;33m'
NC='\033[0m' # No Color

# 项目根目录
PROJECT_ROOT="$(cd "$(dirname "${BASH_SOURCE[0]}")/../.." && pwd)"
cd "$PROJECT_ROOT"

# 配置文件
ARCHITECTURE_RULES="$PROJECT_ROOT/SDLC-Framework/config/architecture-rules.yaml"

# 输出模式：json（默认）或 human
OUTPUT_MODE="${OUTPUT_MODE:-json}"

# 开始输出 JSON 数组
echo "["

first_error=true

# ========== 检查 1: 分层架构违规 ==========
check_layer_violation() {
    local file="$1"
    local content="$2"

    # 检查 Controller 是否直接访问 Repository/Mapper
    if echo "$file" | grep -q "Controller.java"; then
        # 检查是否导入 Mapper
        if echo "$content" | grep -q "import.*Mapper;"; then
            if [ "$OUTPUT_MODE" = "json" ]; then
                [ "$first_error" = false ] && echo ","
                first_error=false

                cat << EOF
{
  "check": "layer_violation",
  "severity": "critical",
  "file": "$file",
  "line": "$(grep -n "import.*Mapper;" "$file" | cut -d: -f1 | head -1)",
  "message": "Controller 直接导入 Mapper，违反分层架构",
  "explanation": "Controller 只能调用 Service，不能直接访问 Repository/Mapper 层",
  "fix_suggestion": "删除 Mapper 导入，通过 Service 层访问数据",
  "correct_pattern": "Controller → Service → Repository/Mapper",
  "auto_fix_available": false,
  "manual_steps": [
    "1. 删除 Controller 中的 Mapper 导入",
    "2. 创建或使用对应的 Service",
    "3. 在 Service 中注入 Mapper",
    "4. Controller 调用 Service 方法"
  ]
}
EOF
            fi
        fi
    fi

    # 检查 Service 是否直接访问 Controller
    if echo "$file" | grep -q "Service"; then
        if echo "$content" | grep -q "import.*Controller;"; then
            if [ "$OUTPUT_MODE" = "json" ]; then
                [ "$first_error" = false ] && echo ","
                first_error=false

                cat << EOF
{
  "check": "layer_violation",
  "severity": "critical",
  "file": "$file",
  "line": "$(grep -n "import.*Controller;" "$file" | cut -d: -f1 | head -1)",
  "message": "Service 导入 Controller，违反分层架构",
  "explanation": "Service 层不应该依赖 Controller 层，这会造成循环依赖",
  "fix_suggestion": "将共享逻辑提取到独立的类或工具类中",
  "correct_pattern": "单向依赖：Controller → Service → Repository",
  "auto_fix_available": false
}
EOF
            fi
        fi
    fi
}

# ========== 检查 2: 循环依赖 ==========
check_circular_dependency() {
    local file="$1"

    # 简化的循环依赖检查（实际项目中需要更复杂的分析）
    if grep -q "import.*\.service\." "$file" && grep -q "import.*\.controller\." "$file"; then
        if [ "$OUTPUT_MODE" = "json" ]; then
            [ "$first_error" = false ] && echo ","
            first_error=false

            cat << EOF
{
  "check": "circular_dependency",
  "severity": "critical",
  "file": "$file",
  "message": "可能存在循环依赖：同时导入 service 和 controller",
  "explanation": "循环依赖会导致代码难以维护和测试",
  "fix_suggestion": "重新设计模块依赖关系，提取公共接口",
  "auto_fix_available": false,
  "refactoring_required": true
}
EOF
        fi
    fi
}

# ========== 检查 3: 包结构违规 ==========
check_package_structure() {
    local file="$1"

    # 检查是否在正确的包中
    if echo "$file" | grep -q "src/main/java"; then
        local package=$(grep "^package " "$file" | sed 's/package //; s/;//')
        local expected_dir=$(echo "$package" | tr '.' '/')
        local actual_dir=$(dirname "$(echo "$file" | sed 's|src/main/java/||')")

        if [ "$expected_dir" != "$actual_dir" ]; then
            if [ "$OUTPUT_MODE" = "json" ]; then
                [ "$first_error" = false ] && echo ","
                first_error=false

                cat << EOF
{
  "check": "package_mismatch",
  "severity": "error",
  "file": "$file",
  "message": "包声明与文件路径不匹配",
  "explanation": "Package 声明: $package, 但文件在: $actual_dir",
  "fix_suggestion": "将文件移动到正确的目录或修改 package 声明",
  "auto_fix_available": true,
  "auto_fix_command": "mv '$file' 'src/main/java/$expected_dir/'"
}
EOF
            fi
        fi
    fi
}

# ========== 检查 4: 安全问题 ==========
check_security_issues() {
    local file="$1"
    local content="$2"

    # 检查硬编码密钥
    if echo "$content" | grep -iE "(password|secret|key)\s*=\s*\"[^\"]+\"" | grep -v "//\|#" > /dev/null; then
        if [ "$OUTPUT_MODE" = "json" ]; then
            [ "$first_error" = false ] && echo ","
            first_error=false

            cat << EOF
{
  "check": "hardcoded_secret",
  "severity": "critical",
  "file": "$file",
  "message": "检测到可能的硬编码密钥",
  "explanation": "硬编码密钥是严重的安全风险",
  "fix_suggestion": "使用环境变量或配置中心管理敏感信息",
  "auto_fix_available": false,
  "security_impact": "high",
  "reference": "docs/security/security.md"
}
EOF
        fi
    fi

    # 检查 SQL 注入风险
    if echo "$content" | grep -E '\$\{.*\}' | grep -v "//\|#" > /dev/null; then
        if [ "$OUTPUT_MODE" = "json" ]; then
            [ "$first_error" = false ] && echo ","
            first_error=false

            cat << EOF
{
  "check": "sql_injection_risk",
  "severity": "critical",
  "file": "$file",
  "message": "检测到可能的 SQL 注入风险",
  "explanation": "使用字符串拼接构建 SQL 可能导致注入攻击",
  "fix_suggestion": "使用 MyBatis 的 #{} 参数化查询",
  "auto_fix_available": false,
  "security_impact": "critical",
  "reference": "docs/security/security.md"
}
EOF
        fi
    fi
}

# ========== 检查 5: 代码复杂度 ==========
check_complexity() {
    local file="$1"
    local content="$2"

    # 简单的行数检查
    local lines=$(echo "$content" | wc -l)
    if [ "$lines" -gt 500 ]; then
        if [ "$OUTPUT_MODE" = "json" ]; then
            [ "$first_error" = false ] && echo ","
            first_error=false

            cat << EOF
{
  "check": "file_too_long",
  "severity": "warning",
  "file": "$file",
  "message": "文件过长 ($lines 行)",
  "explanation": "超过 500 行的文件难以维护",
  "fix_suggestion": "考虑拆分为多个类或提取内部类",
  "auto_fix_available": false,
  "refactoring_required": true
}
EOF
        fi
    fi
}

# ========== 主检查流程 ==========
main() {
    # 查找所有 Java 文件
    find "$PROJECT_ROOT" -name "*.java" -not -path "*/node_modules/*" -not -path "*/target/*" | while read -r file; do
        if [ -f "$file" ]; then
            content=$(cat "$file")

            # 执行所有检查
            check_layer_violation "$file" "$content"
            check_circular_dependency "$file"
            check_package_structure "$file"
            check_security_issues "$file" "$content"
            check_complexity "$file" "$content"
        fi
    done

    # 关闭 JSON 数组
    echo "]"
}

# 运行检查
main

# 如果是 JSON 模式，额外输出摘要
if [ "$OUTPUT_MODE" = "json" ]; then
    echo ""
    echo "{" | cat - <(
        cat << 'EOF'
  "summary": {
    "tool": "agent-linter",
    "version": "1.0.0",
    "project_root": "PROJECT_ROOT_PLACEHOLDER",
    "timestamp": "TIMESTAMP_PLACEHOLDER",
    "documentation": "docs/architecture/architecture.md",
    "architecture_rules": "SDLC-Framework/config/architecture-rules.yaml"
  }
}
EOF
    ) | sed "s|PROJECT_ROOT_PLACEHOLDER|$PROJECT_ROOT|" | sed "s|TIMESTAMP_PLACEHOLDER|$(date -u +%Y-%m-%dT%H:%M:%SZ)|"
fi
