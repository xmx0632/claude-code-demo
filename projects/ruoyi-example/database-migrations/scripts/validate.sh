#!/bin/bash

# 验证迁移脚本
#
# 使用方法:
#   ./scripts/validate.sh

set -e

# 获取脚本所在目录
SCRIPT_DIR="$(cd "$(dirname "${BASH_SOURCE[0]}")" && pwd)"
PROJECT_DIR="$(dirname "$SCRIPT_DIR")"

# 颜色输出
GREEN='\033[0;32m'
RED='\033[0;31m'
NC='\033[0m'

log_info() {
    echo -e "${GREEN}[INFO]${NC} $1"
}

log_error() {
    echo -e "${RED}[ERROR]${NC} $1"
}

# 检查 Flyway
if ! command -v flyway &> /dev/null; then
    log_error "Flyway 未安装"
    echo "请访问 https://flywaydb.org/download 下载安装"
    exit 1
fi

cd "$PROJECT_DIR"

# 确定配置文件
if [ -f "flyway.local.conf" ]; then
    CONFIG_FILE="flyway.local.conf"
else
    CONFIG_FILE="flyway.conf"
fi

log_info "验证迁移脚本"
echo "=========================================="

flyway -configFiles="$CONFIG_FILE" validate

if [ $? -eq 0 ]; then
    log_info "验证通过！"
else
    log_error "验证失败！"
    exit 1
fi
