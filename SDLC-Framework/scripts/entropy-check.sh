#!/bin/bash
# Entropy Check Script
# 检测系统熵：文档漂移、规则冲突、过时内容
#
# 使用方法:
#   ./entropy-check.sh                    # 完整检查
#   ./entropy-check.sh --check=api-doc    # 特定检查
#   ./entropy-check.sh --mode=quick       # 快速检查

set -e

# 配置
PROJECT_ROOT="$(cd "$(dirname "${BASH_SOURCE[0]}")/../.." && pwd)"
OUTPUT_DIR="$PROJECT_ROOT/.audit"
ENTROPY_REPORT="$OUTPUT_DIR/entropy-report.json"
CHECK_MODE="${CHECK_MODE:-full}"  # quick | full

# 颜色定义
RED='\033[0;31m'
GREEN='\033[0;32m'
YELLOW='\033[1;33m'
BLUE='\033[0;34m'
NC='\033[0m'

# 创建输出目录
mkdir -p "$OUTPUT_DIR"

# ========== 报告结构 ==========
report='{
  "timestamp": "TIMESTAMP_PLACEHOLDER",
  "project_root": "PROJECT_ROOT_PLACEHOLDER",
  "check_mode": "MODE_PLACEHOLDER",
  "entropy_index": {
    "documentation_drift": 0,
    "rule_conflicts": 0,
    "obsolete_content": 0,
    "overall": 0
  },
  "issues": []
}'

# ========== 1. 文档漂移检测 ==========
check_documentation_drift() {
    local drift_count=0
    local issues=""

    echo -e "${BLUE}检查文档漂移...${NC}"

    # 检查 API 文档同步
    local java_files=$(find "$PROJECT_ROOT" -name "*.java" -not -path "*/target/*" | wc -l | tr -d ' ')
    local api_doc_files=$(find "$PROJECT_ROOT/docs/api" -name "*.md" 2>/dev/null | wc -l | tr -d ' ')

    if [ "$java_files" -gt 0 ] && [ "$api_doc_files" -eq 0 ]; then
        drift_count=$((drift_count + 1))
        issues="${issues}{\"type\":\"api_doc_missing\",\"severity\":\"critical\",\"message\":\"存在 Java 文件但缺少 API 文档\"},"
    fi

    # 检查架构文档一致性
    local架构文档="$PROJECT_ROOT/docs/architecture/architecture.md"
    local架构规则="$PROJECT_ROOT/SDLC-Framework/config/architecture-rules.yaml"

    if [ -f "$架构文档" ] && [ -f "$架构规则" ]; then
        local doc_age=$(( $(date +%s) - $(stat -f %m "$架构文档" 2>/dev/null || stat -c %Y "$架构文档" 2>/dev/null) ))
        local rule_age=$(( $(date +%s) - $(stat -f %m "$架构规则" 2>/dev/null || stat -c %Y "$架构规则" 2>/dev/null) ))

        local diff=$((doc_age - rule_age))
        if [ $diff -lt 0 ]; then diff=$((-diff)); fi

        if [ $diff -gt 604800 ]; then  # 7 天
            drift_count=$((drift_count + 1))
            issues="${issues}{\"type\":\"architecture_doc_drift\",\"severity\":\"warning\",\"message\":\"架构文档与规则更新时间差异超过 7 天\"},"
        fi
    fi

    # 检查质量门禁一致性
    local质量配置="$PROJECT_ROOT/SDLC-Framework/config/quality-gates.yaml"
    local质量文档="$PROJECT_ROOT/docs/quality/quality.md"

    if [ -f "$质量配置" ] && [ -f "$质量文档" ]; then
        if ! grep -q "code_coverage" "$质量文档" 2>/dev/null; then
            drift_count=$((drift_count + 1))
            issues="${issues}{\"type\":\"quality_doc_incomplete\",\"severity\":\"warning\",\"message\":\"质量文档未包含覆盖率要求\"},"
        fi
    fi

    # 更新报告
    local current_drift=$(echo "$report" | grep -o '"documentation_drift":[0-9]*' | grep -o '[0-9]*')
    report=$(echo "$report" | sed "s/\"documentation_drift\":$current_drift/\"documentation_drift\":$drift_count/")

    echo "$issues" >> "$OUTPUT_DIR/tmp_issues.json"
}

# ========== 2. 规则冲突检测 ==========
check_rule_conflicts() {
    local conflict_count=0
    local issues=""

    echo -e "${BLUE}检查规则冲突...${NC}"

    # 检查架构规则和质量门禁的冲突
    local架构规则="$PROJECT_ROOT/SDLC-Framework/config/architecture-rules.yaml"
    local质量门禁="$PROJECT_ROOT/SDLC-Framework/config/quality-gates.yaml"

    if [ -f "$架构规则" ] && [ -f "$质量门禁" ]; then
        # 检查复杂度规则是否一致
        local架构复杂度=$(grep -o "max_cyclomatic_complexity: [0-9]*" "$架构规则" 2>/dev/null | grep -o '[0-9]*' || echo "10")
        local质量复杂度=$(grep -o "max_complexity: [0-9]*" "$质量门禁" 2>/dev/null | grep -o '[0-9]*' || echo "10")

        if [ "$架构复杂度" != "$质量复杂度" ]; then
            conflict_count=$((conflict_count + 1))
            issues="${issues}{\"type\":\"complexity_rule_mismatch\",\"severity\":\"warning\",\"message\":\"架构规则和质量门禁中的复杂度限制不一致\"},"
        fi
    fi

    # 检查循环依赖（在上下文配置中）
    local上下文配置="$PROJECT_ROOT/SDLC-Framework/config/context-discovery.yaml"

    if [ -f "$上下文配置" ]; then
        if grep -q "architecture-design.*:" "$上下文配置" && grep -q "detailed-design.*architecture-design" "$上下文配置"; then
            # 简化的循环依赖检测
            if grep -A 20 "detailed-design:" "$上下文配置" | grep -q "architecture"; then
                if grep -A 20 "architecture-design:" "$上下文配置" | grep -q "detailed-design"; then
                    conflict_count=$((conflict_count + 1))
                    issues="${issues}{\"type\":\"circular_dependency_in_config\",\"severity\":\"error\",\"message\":\"上下文配置中存在循环依赖\"},"
                fi
            fi
        fi
    fi

    # 更新报告
    local current_conflicts=$(echo "$report" | grep -o '"rule_conflicts":[0-9]*' | grep -o '[0-9]*')
    report=$(echo "$report" | sed "s/\"rule_conflicts\":$current_conflicts/\"rule_conflicts\":$conflict_count/")

    echo "$issues" >> "$OUTPUT_DIR/tmp_issues.json"
}

# ========== 3. 过时内容检测 ==========
check_obsolete_content() {
    local obsolete_count=0
    local issues=""

    echo -e "${BLUE}检查过时内容...${NC}"

    # 检查 3 个月未访问的文档
    local three_months_ago=$(date -v-3m +%s 2>/dev/null || date -d "3 months ago" +%s 2>/dev/null)

    find "$PROJECT_ROOT/docs" -name "*.md" -not -path "*/node_modules/*" 2>/dev/null | while read -r file; do
        local last_access=$(stat -f %a "$file" 2>/dev/null || stat -c %X "$file" 2>/dev/null)
        if [ "$last_access" -lt "$three_months_ago" ]; then
            local relative_path="${file#$PROJECT_ROOT/}"
            obsolete_count=$((obsolete_count + 1))
            issues="${issues}{\"type\":\"unused_document\",\"severity\":\"info\",\"file\":\"$relative_path\",\"message\":\"文档 3 个月未访问\"},"
        fi
    done

    # 检查废弃的规则
    find "$PROJECT_ROOT/SDLC-Framework/config" -name "*deprecated*" -o -name "*obsolete*" 2>/dev/null | while read -r file; do
        local relative_path="${file#$PROJECT_ROOT/}"
        obsolete_count=$((obsolete_count + 1))
        issues="${issues}{\"type\":\"deprecated_rule_file\",\"severity\":\"warning\",\"file\":\"$relative_path\",\"message\":\"存在废弃的规则文件\"},"
    done

    # 检查重复的内容（简单检查）
    local docs=$(find "$PROJECT_ROOT/docs" -name "*.md" 2>/dev/null)
    local doc_count=$(echo "$docs" | wc -l | tr -d ' ')

    if [ "$doc_count" -gt 50 ]; then
        issues="${issues}{\"type\":\"too_many_documents\",\"severity\":\"info\",\"count\":$doc_count,\"message\":\"文档数量过多，考虑合并或归档\"},"
    fi

    # 更新报告
    local current_obsolete=$(echo "$report" | grep -o '"obsolete_content":[0-9]*' | grep -o '[0-9]*')
    report=$(echo "$report" | sed "s/\"obsolete_content\":$current_obsolete/\"obsolete_content\":$obsolete_count/")

    echo "$issues" >> "$OUTPUT_DIR/tmp_issues.json"
}

# ========== 4. 质量门禁验证 ==========
check_quality_gates() {
    echo -e "${BLUE}验证质量门禁...${NC}"

    local质量门禁="$PROJECT_ROOT/SDLC-Framework/config/quality-gates.yaml"

    if [ -f "$质量门禁" ]; then
        # 检查是否有覆盖率要求
        if ! grep -q "test_coverage" "$质量门禁" 2>/dev/null; then
            echo "{\"type\":\"missing_coverage_requirement\",\"severity\":\"warning\",\"message\":\"质量门禁未定义测试覆盖率要求\"}," >> "$OUTPUT_DIR/tmp_issues.json"
        fi

        # 检查是否有安全要求
        if ! grep -q "security" "$质量门禁" 2>/dev/null; then
            echo "{\"type\":\"missing_security_gates\",\"severity\":\"warning\",\"message\":\"质量门禁未包含安全检查\"}," >> "$OUTPUT_DIR/tmp_issues.json"
        fi
    fi
}

# ========== 计算总体熵指数 ==========
calculate_entropy_index() {
    local doc_drift=$(echo "$report" | grep -o '"documentation_drift":[0-9]*' | grep -o '[0-9]*')
    local rule_conflicts=$(echo "$report" | grep -o '"rule_conflicts":[0-9]*' | grep -o '[0-9]*')
    local obsolete=$(echo "$report" | grep -o '"obsolete_content":[0-9]*' | grep -o '[0-9]*')

    # 熵指数计算 (0-10，越高越差)
    local drift_score=$(echo "scale=1; $doc_drift * 0.5" | bc 2>/dev/null || echo "$doc_drift")
    local conflict_score=$(echo "scale=1; $rule_conflicts * 2" | bc 2>/dev/null || echo "$rule_conflicts")
    local obsolete_score=$(echo "scale=1; $obsolete * 0.3" | bc 2>/dev/null || echo "$obsolete")

    local overall=$(echo "$drift_score + $conflict_score + $obsolete_score" | bc 2>/dev/null || echo "0")

    report=$(echo "$report" | sed "s/\"overall\":0/\"overall\":$overall/")
}

# ========== 生成报告 ==========
generate_report() {
    # 合并所有问题
    local all_issues=""
    if [ -f "$OUTPUT_DIR/tmp_issues.json" ]; then
        all_issues=$(cat "$OUTPUT_DIR/tmp_issues.json" | tr -d '\n' | sed 's/,$//')
        rm "$OUTPUT_DIR/tmp_issues.json"
    fi

    # 构建最终报告
    local timestamp=$(date -u +%Y-%m-%dT%H:%M:%SZ)
    report=$(echo "$report" | sed "s/TIMESTAMP_PLACEHOLDER/$timestamp/")
    report=$(echo "$report" | sed "s|PROJECT_ROOT_PLACEHOLDER|$PROJECT_ROOT|")
    report=$(echo "$report" | sed "s/MODE_PLACEHOLDER/$CHECK_MODE/")
    report=$(echo "$report" | sed "s/\"issues\":\[\]/\"issues\":[$all_issues]/")

    # 输出 JSON 报告
    echo "$report" | tee "$ENTROPY_REPORT"

    # 输出人类可读摘要
    echo ""
    echo "=========================================="
    echo "           熵检查报告"
    echo "=========================================="
    echo "检查时间: $timestamp"
    echo "检查模式: $CHECK_MODE"
    echo ""

    # 提取指标
    local doc_drift=$(echo "$report" | grep -o '"documentation_drift":[0-9.]*' | cut -d: -f2)
    local rule_conflicts=$(echo "$report" | grep -o '"rule_conflicts":[0-9.]*' | cut -d: -f2)
    local obsolete=$(echo "$report" | grep -o '"obsolete_content":[0-9.]*' | cut -d: -f2)
    local overall=$(echo "$report" | grep -o '"overall":[0-9.]*' | cut -d: -f2)

    echo "熵指数:"
    echo "  文档漂移: $doc_drift"
    echo "  规则冲突: $rule_conflicts"
    echo "  过时内容: $obsolete"
    echo "  总体熵指数: $overall / 10"
    echo ""

    # 状态判定
    if (( $(echo "$overall < 3" | bc -l 2>/dev/null || echo "0") )); then
        echo -e "${GREEN}状态: 优秀 ✅${NC}"
    elif (( $(echo "$overall < 5" | bc -l 2>/dev/null || echo "0") )); then
        echo -e "${GREEN}状态: 良好 ✅${NC}"
    elif (( $(echo "$overall < 7" | bc -l 2>/dev/null || echo "0") )); then
        echo -e "${YELLOW}状态: 警告 ⚠️${NC}"
    else
        echo -e "${RED}状态: 严重 🔴${NC}"
    fi

    echo ""
    echo "详细报告: $ENTROPY_REPORT"
    echo "=========================================="
}

# ========== 主函数 ==========
main() {
    case "$CHECK_MODE" in
        quick)
            echo "执行快速熵检查..."
            check_documentation_drift
            check_rule_conflicts
            ;;
        full)
            echo "执行完整熵检查..."
            check_documentation_drift
            check_rule_conflicts
            check_obsolete_content
            check_quality_gates
            ;;
        *)
            echo "未知模式: $CHECK_MODE"
            exit 1
            ;;
    esac

    calculate_entropy_index
    generate_report
}

# 解析参数
while [[ $# -gt 0 ]]; do
    case $1 in
        --check=*)
            CHECK_TYPE="${1#*=}"
            case "$CHECK_TYPE" in
                documentation-drift)
                    CHECK_MODE="custom"
                    check_documentation_drift
                    calculate_entropy_index
                    generate_report
                    exit 0
                    ;;
                rule-conflicts)
                    CHECK_MODE="custom"
                    check_rule_conflicts
                    calculate_entropy_index
                    generate_report
                    exit 0
                    ;;
                obsolete-content)
                    CHECK_MODE="custom"
                    check_obsolete_content
                    calculate_entropy_index
                    generate_report
                    exit 0
                    ;;
            esac
            ;;
        --mode=*)
            CHECK_MODE="${1#*=}"
            ;;
        *)
            echo "未知参数: $1"
            exit 1
            ;;
    esac
    shift
done

# 运行主程序
main
