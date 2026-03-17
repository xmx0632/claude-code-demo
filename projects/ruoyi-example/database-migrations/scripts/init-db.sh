#!/bin/bash

# 初始化数据库
#
# 使用方法:
#   ./scripts/init-db.sh
#   DB=h2 ./scripts/init-db.sh  # 使用 H2 数据库

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

# 创建 H2 数据库
create_h2_database() {
    log_info "H2 数据库将自动创建，无需手动初始化"
    log_info "运行迁移: ./h2/migrate.sh"
}

# 创建 MySQL 数据库
create_mysql_database() {
    # 从 pom.xml 读取配置
    POM_FILE="$PROJECT_DIR/pom.xml"

    if [ ! -f "$POM_FILE" ]; then
        log_error "未找到 pom.xml 配置文件"
        exit 1
    fi

    # 提取配置 (使用 awk 处理 XML)
    DB_URL=$(awk -F'[<>]' '/<url>/ {gsub(/&amp;/, "&"); print $3; exit}' "$POM_FILE")
    DB_USER=$(awk -F'[<>]' '/<user>/ {print $3; exit}' "$POM_FILE")
    DB_PASSWORD=$(awk -F'[<>]' '/<password>/ {print $3; exit}' "$POM_FILE")
    DB_SCHEMA=$(awk -F'[<>]' '/<schemas>/ {print $3; exit}' "$POM_FILE")

    if [ -z "$DB_SCHEMA" ]; then
        log_error "无法从 pom.xml 解析数据库 schema"
        exit 1
    fi

    # 解析主机和端口
    # 移除 jdbc:mysql: 前缀
    DB_URL_CLEAN="${DB_URL#jdbc:mysql:}"
    # 提取主机 (格式: //host:port 或 //host)
    DB_HOST=$(echo "$DB_URL_CLEAN" | sed 's|^//\([^:/]*\).*|\1|')
    # 提取端口
    DB_PORT=$(echo "$DB_URL_CLEAN" | sed 's|^//[^:]*:\([0-9]*\).*|\1|')

    # 如果没有端口，使用默认值
    if [ -z "$DB_PORT" ] || [ "$DB_PORT" = "$DB_URL_CLEAN" ]; then
        DB_PORT="3306"
    fi

    log_info "创建数据库: $DB_SCHEMA"
    log_info "主机: $DB_HOST"
    log_info "端口: $DB_PORT"

    # 创建数据库 SQL
    SQL="CREATE DATABASE IF NOT EXISTS \`$DB_SCHEMA\` DEFAULT CHARSET utf8mb4 COLLATE utf8mb4_unicode_ci;"

    # 执行创建
    if [ -n "$DB_PASSWORD" ]; then
        mysql -h"$DB_HOST" -P"$DB_PORT" -u"$DB_USER" -p"$DB_PASSWORD" -e "$SQL"
    else
        mysql -h"$DB_HOST" -P"$DB_PORT" -u"$DB_USER" -p -e "$SQL"
    fi

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

    if [ "$DB" = "h2" ]; then
        create_h2_database
    else
        create_mysql_database
    fi

    echo ""
    if [ "$DB" = "h2" ]; then
        log_info "执行迁移: ./h2/migrate.sh"
    else
        log_info "执行迁移: ./scripts/migrate.sh"
    fi
}

# 执行主流程
main
