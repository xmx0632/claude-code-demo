#!/bin/bash

# Docker 服务日志查看脚本
#
# 使用方法:
#   ./scripts/logs.sh [服务名]

set -e

SCRIPT_DIR="$(cd "$(dirname "${BASH_SOURCE[0]}")" && pwd)"
PROJECT_DIR="$(dirname "$SCRIPT_DIR")"

GREEN='\033[0;32m'
YELLOW='\033[1;33m'
RED='\033[0;31m'
NC='\033[0m'

log_info() {
    echo -e "${GREEN}[INFO]${NC} $1"
}

log_error() {
    echo -e "${RED}[ERROR]${NC} $1"
}

# 检查 docker-compose
check_docker_compose() {
    if ! command -v docker-compose &> /dev/null; then
        if docker compose version &> /dev/null; then
            DOCKER_COMPOSE="docker compose"
        else
            log_error "docker-compose 未安装"
            exit 1
        fi
    else
        DOCKER_COMPOSE="docker-compose"
    fi
}

# 查看日志
show_logs() {
    cd "$PROJECT_DIR/docker"

    if [ -z "$1" ]; then
        log_info "显示所有服务日志（按 Ctrl+C 退出）..."
        $DOCKER_COMPOSE logs -f
    else
        log_info "显示 $1 服务日志（按 Ctrl+C 退出）..."
        $DOCKER_COMPOSE logs -f "$1"
    fi
}

main() {
    if [ "$1" = "-h" ] || [ "$1" = "--help" ]; then
        echo "使用方法: $0 [服务名]"
        echo ""
        echo "服务名:"
        echo "  ruoyi-app  - 应用服务"
        echo "  mysql      - MySQL 数据库"
        echo "  redis      - Redis 缓存"
        echo ""
        echo "不指定服务名则显示所有服务日志"
        exit 0
    fi

    log_info "Docker 服务日志查看工具"
    echo "=========================================="

    check_docker_compose
    show_logs "$@"
}

main "$@"
