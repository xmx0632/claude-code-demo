#!/bin/bash

# 初始化数据库
#
# 使用方法:
#   ./scripts/init-db.sh

set -e

# 获取脚本所在目录
SCRIPT_DIR="$(cd "$(dirname "${BASH_SOURCE[0]}")" && pwd)"
PROJECT_DIR="$(dirname "$SCRIPT_DIR")"

# 颜色输出
GREEN='\033[0;32m'
YELLOW='\033[1;33m'
RED='\033[0;31m'
NC='\033[0m'

log_info() {
    echo -e "${GREEN}[INFO]${NC} $1"
}

log_warn() {
    echo -e "${YELLOW}[WARN]${NC} $1"
}

log_error() {
    echo -e "${RED}[ERROR]${NC} $1"
}

# 读取配置
read_config() {
    if [ -f "$PROJECT_DIR/flyway.local.conf" ]; then
        CONFIG_FILE="$PROJECT_DIR/flyway.local.conf"
    else
        CONFIG_FILE="$PROJECT_DIR/flyway.conf"
    fi

    # 解析配置文件获取数据库信息
    DB_URL=$(grep "^flyway.url=" "$CONFIG_FILE" | cut -d'=' -f2-)
    DB_USER=$(grep "^flyway.user=" "$CONFIG_FILE" | cut -d'=' -f2)
    DB_NAME=$(echo "$DB_URL" | grep -oP 'dbname=\K[^?]+' || echo "$DB_URL" | grep -oP '/\K[^?]+' | tail -n1)

    if [ -z "$DB_NAME" ]; then
        log_error "无法从配置文件解析数据库名称"
        exit 1
    fi
}

# 创建数据库
create_database() {
    log_info "创建数据库: $DB_NAME"

    # 提取主机和端口
    DB_HOST=$(echo "$DB_URL" | grep -oP '//([^:]+)' | cut -d'/' -f3 || echo "localhost")
    DB_PORT=$(echo "$DB_URL" | grep -oP ':\d+' | cut -d':' -f2 || echo "3306")

    log_info "主机: $DB_HOST"
    log_info "端口: $DB_PORT"

    # 创建数据库 SQL
    SQL="CREATE DATABASE IF NOT EXISTS \`$DB_NAME\` DEFAULT CHARSET utf8mb4 COLLATE utf8mb4_unicode_ci;"

    # 执行创建
    mysql -h"$DB_HOST" -P"$DB_PORT" -u"$DB_USER" -p -e "$SQL"

    if [ $? -eq 0 ]; then
        log_info "数据库创建成功！"
    else
        log_error "数据库创建失败！"
        exit 1
    fi
}

# 主流程
main() {
    log_info "初始化数据库"
    echo "=========================================="

    read_config
    create_database

    echo ""
    log_info "执行迁移: ./scripts/migrate.sh"
}

# 执行主流程
main
