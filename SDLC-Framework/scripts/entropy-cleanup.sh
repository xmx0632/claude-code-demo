#!/bin/bash
# Entropy Cleanup Script
# 清理系统中的熵：归档旧文档、删除过时规则、清理临时文件
#
# 使用方法:
#   ./entropy-cleanup.sh --dry-run     # 预览清理操作
#   ./entropy-cleanup.sh --execute     # 执行清理
#   ./entropy-cleanup.sh --archive     # 归档而非删除

set -e

# 配置
PROJECT_ROOT="$(cd "$(dirname "${BASH_SOURCE[0]}")/../.." && pwd)"
ARCHIVE_DIR="$PROJECT_ROOT/.archive"
AUDIT_DIR="$PROJECT_ROOT/.audit"
LOG_FILE="$AUDIT_DIR/entropy-cleanup.log"
DRY_RUN="${DRY_RUN:-true}"

# 颜色定义
RED='\033[0;31m'
GREEN='\033[0;32m'
YELLOW='\033[1;33m'
BLUE='\033[0;34m'
NC='\033[0m'

# 创建目录
mkdir -p "$ARCHIVE_DIR"
mkdir -p "$AUDIT_DIR"

# 日志函数
log() {
    echo "[$(date '+%Y-%m-%d %H:%M:%S')] $*" | tee -a "$LOG_FILE"
}

# ========== 1. 归档旧文档 ==========
archive_old_documents() {
    log "归档旧文档..."

    local three_months_ago=$(date -v-3m +%s 2>/dev/null || date -d "3 months ago" +%s 2>/dev/null)
    local archived_count=0

    find "$PROJECT_ROOT/docs" -name "*.md" -not -path "*/node_modules/*" -not -path "*/.archive/*" 2>/dev/null | while read -r file; do
        local last_access=$(stat -f %a "$file" 2>/dev/null || stat -c %X "$file" 2>/dev/null)

        if [ "$last_access" -lt "$three_months_ago" ]; then
            local relative_path="${file#$PROJECT_ROOT/docs/}"
            local archive_path="$ARCHIVE_DIR/docs/$relative_path"
            local archive_dir=$(dirname "$archive_path")

            if [ "$DRY_RUN" = true ]; then
                echo -e "${BLUE}[DRY RUN]${NC} 将归档: $relative_path"
            else
                mkdir -p "$archive_dir"
                cp "$file" "$archive_path"
                rm "$file"
                log "已归档: $relative_path → $archive_path"
            fi
            archived_count=$((archived_count + 1))
        fi
    done

    log "归档完成，共 $archived_count 个文件"
}

# ========== 2. 删除过时规则 ==========
remove_obsolete_rules() {
    log "删除过时规则..."

    local removed_count=0

    # 查找包含 deprecated 或 obsolete 的配置文件
    find "$PROJECT_ROOT/SDLC-Framework/config" -name "*deprecated*" -o -name "*obsolete*" 2>/dev/null | while read -r file; do
        local filename=$(basename "$file")

        if [ "$DRY_RUN" = true ]; then
            echo -e "${BLUE}[DRY RUN]${NC} 将删除: $filename"
        else
            # 先备份到归档目录
            cp "$file" "$ARCHIVE_DIR/config/"
            rm "$file"
            log "已删除: $filename (已备份到归档目录)"
        fi
        removed_count=$((removed_count + 1))
    done

    log "清理完成，共 $removed_count 个文件"
}

# ========== 3. 清理临时文件 ==========
cleanup_temp_files() {
    log "清理临时文件..."

    local temp_patterns=(
        "*.tmp"
        "*~"
        ".DS_Store"
        "Thumbs.db"
        "*.swp"
        "*.swo"
        ".audit/tmp_*.json"
        ".audit/tmp_*.md"
    )

    local cleaned_count=0

    for pattern in "${temp_patterns[@]}"; do
        find "$PROJECT_ROOT" -name "$pattern" -not -path "*/node_modules/*" -not -path "*/.git/*" 2>/dev/null | while read -r file; do
            if [ "$DRY_RUN" = true ]; then
                echo -e "${BLUE}[DRY RUN]${NC} 将删除: $file"
            else
                rm "$file"
                cleaned_count=$((cleaned_count + 1))
            fi
        done
    done

    log "临时文件清理完成"
}

# ========== 4. 清理未使用的文档 ==========
cleanup_unused_docs() {
    log "查找未使用的文档..."

    # 查找 6 个月未访问的文档
    local six_months_ago=$(date -v-6m +%s 2>/dev/null || date -d "6 months ago" +%s 2>/dev/null)

    find "$PROJECT_ROOT/docs" -name "*.md" -not -path "*/node_modules/*" -not -path "*/.archive/*" 2>/dev/null | while read -r file; do
        local last_access=$(stat -f %a "$file" 2>/dev/null || stat -c %X "$file" 2>/dev/null)

        if [ "$last_access" -lt "$six_months_ago" ]; then
            local relative_path="${file#$PROJECT_ROOT/}"

            echo -e "${YELLOW}未使用文档:${NC} $relative_path"
            echo "  最后访问: $(date -r "$file" '+%Y-%m-%d %H:%M:%S' 2>/dev/null)"
        fi
    done
}

# ========== 5. 合并重复文档 ==========
merge_duplicate_docs() {
    log "检查重复文档..."

    # 简化的重复检测（基于文件名相似度）
    find "$PROJECT_ROOT/docs" -name "*.md" -not -path "*/node_modules/*" 2>/dev/null | while read -r file; do
        local basename=$(basename "$file" .md)

        # 查找同名文件
        local duplicates=$(find "$PROJECT_ROOT/docs" -name "${basename}.md" -not -path "$file" 2>/dev/null | wc -l | tr -d ' ')

        if [ "$duplicates" -gt 0 ]; then
            echo -e "${YELLOW}发现重复:${NC} $basename.md"
            find "$PROJECT_ROOT/docs" -name "${basename}.md" -not -path "*/node_modules/*" 2>/dev/null
        fi
    done
}

# ========== 6. 生成清理报告 ==========
generate_cleanup_report() {
    local report_file="$AUDIT_DIR/entropy-cleanup-report.md"

    cat > "$report_file" << EOF
# 熵清理报告

生成时间: $(date -u +%Y-%m-%dT%H:%M:%SZ)
清理模式: $([ "$DRY_RUN" = true ] && echo "预览" || echo "执行")

## 清理操作

### 1. 归档旧文档
- 归档超过 3 个月未访问的文档
- 目标目录: \`$ARCHIVE_DIR/docs/\`

### 2. 删除过时规则
- 删除标记为 deprecated/obsolete 的配置文件
- 备份到归档目录

### 3. 清理临时文件
- 删除编辑器临时文件
- 清理审计临时文件

### 4. 检查未使用文档
- 识别 6 个月未访问的文档
- 建议人工审查

### 5. 检测重复文档
- 识别同名文档
- 建议合并

## 清理统计

$(if [ "$DRY_RUN" = false ]; then
    echo "- 实际执行清理操作"
    echo "- 详细日志: \`$LOG_FILE\`"
else
    echo "- 这是预览模式，未实际执行"
    echo "- 使用 \`--execute\` 参数执行实际清理"
fi)

## 建议

1. 定期运行熵清理（建议每月一次）
2. 在清理前创建备份
3. 仔细审查未使用和重复的文档
4. 更新相关文档引用

---

下次检查: $(date -v+1m '+%Y-%m-%d' 2>/dev/null || date -d "1 month" '+%Y-%m-%d')
EOF

    echo ""
    echo "清理报告已生成: $report_file"
}

# ========== 主函数 ==========
main() {
    log "开始熵清理 (模式: $([ "$DRY_RUN" = true ] && echo "预览" || echo "执行"))"

    archive_old_documents
    remove_obsolete_rules
    cleanup_temp_files
    cleanup_unused_docs
    merge_duplicate_docs
    generate_cleanup_report

    log "熵清理完成"

    if [ "$DRY_RUN" = true ]; then
        echo ""
        echo -e "${YELLOW}这是预览模式，未实际执行清理${NC}"
        echo "使用 --execute 参数执行实际清理"
    else
        echo ""
        echo -e "${GREEN}清理已执行${NC}"
        echo "查看日志: $LOG_FILE"
    fi
}

# 解析参数
while [[ $# -gt 0 ]]; do
    case $1 in
        --dry-run)
            DRY_RUN=true
            shift
            ;;
        --execute)
            DRY_RUN=false
            shift
            ;;
        --archive)
            # 归档模式（不删除，只归档）
            ARCHIVE_MODE=true
            shift
            ;;
        --help|-h)
            echo "用法: $0 [选项]"
            echo ""
            echo "选项:"
            echo "  --dry-run    预览清理操作（默认）"
            echo "  --execute    执行实际清理"
            echo "  --archive    归档模式（不删除）"
            echo "  --help       显示帮助"
            exit 0
            ;;
        *)
            echo "未知参数: $1"
            echo "使用 --help 查看帮助"
            exit 1
            ;;
    esac
done

# 运行主程序
main
