#!/bin/bash

# Flyway 数据库迁移脚本
#
# 使用方法:
#   ./scripts/migrate.sh [环境]
#
# 参数:
#   环境 - dev|test|prod (可选，默认为 dev)

set -e

# 获取脚本所在目录
SCRIPT_DIR="$(cd "$(dirname "${BASH_SOURCE[0]}")" && pwd)"
PROJECT_DIR="$(dirname "$SCRIPT_DIR")"

# 颜色输出
RED='\033[0;31m'
GREEN='\033[0;32m'
YELLOW='\033[1;33m'
NC='\033[0m' # No Color

# 日志函数
log_info() {
    echo -e "${GREEN}[INFO]${NC} $1"
}

log_warn() {
    echo -e "${YELLOW}[WARN]${NC} $1"
}

log_error() {
    echo -e "${RED}[ERROR]${NC} $1"
}

# 检查 Flyway 是否安装
check_flyway() {
    if ! command -v flyway &> /dev/null; then
        log_error "Flyway 未安装"
        echo "请访问 https://flywaydb.org/download 下载安装"
        echo "或使用 Maven: mvn flyway:migrate"
        exit 1
    fi
}

# 检查配置文件
check_config() {
    if [ ! -f "$PROJECT_DIR/flyway.conf" ] && [ ! -f "$PROJECT_DIR/flyway.local.conf" ]; then
        log_error "配置文件不存在"
        echo "请复制 flyway.conf 为 flyway.local.conf 并修改数据库连接信息"
        exit 1
    fi
}

# 执行迁移
run_migration() {
    log_info "开始执行数据库迁移..."

    cd "$PROJECT_DIR"

    # 使用 flyway.local.conf (如果存在)，否则使用 flyway.conf
    if [ -f "flyway.local.conf" ]; then
        CONFIG_FILE="flyway.local.conf"
    else
        CONFIG_FILE="flyway.conf"
    fi

    log_info "使用配置文件: $CONFIG_FILE"

    # 执行迁移
    flyway -configFiles="$CONFIG_FILE" migrate

    if [ $? -eq 0 ]; then
        log_info "数据库迁移完成！"
    else
        log_error "数据库迁移失败！"
        exit 1
    fi
}

# 主流程
main() {
    log_info "Flyway 数据库迁移工具"
    echo "=========================================="

    check_flyway
    check_config
    run_migration

    echo ""
    log_info "查看迁移状态: ./scripts/info.sh"
    log_info "验证迁移脚本: ./scripts/validate.sh"
}

# 执行主流程
main
