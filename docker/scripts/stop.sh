#!/bin/bash

# Docker 服务停止脚本
#
# 使用方法:
#   ./scripts/stop.sh

set -e

SCRIPT_DIR="$(cd "$(dirname "${BASH_SOURCE[0]}")" && pwd)"
PROJECT_DIR="$(dirname "$SCRIPT_DIR")"

GREEN='\033[0;32m'
YELLOW='\033[1;33m'
NC='\033[0m'

log_info() {
    echo -e "${GREEN}[INFO]${NC} $1"
}

log_warn() {
    echo -e "${YELLOW}[WARN]${NC} $1"
}

# 检查 docker-compose
check_docker_compose() {
    if ! command -v docker-compose &> /dev/null; then
        if docker compose version &> /dev/null; then
            DOCKER_COMPOSE="docker compose"
        else
            echo "错误: docker-compose 未安装"
            exit 1
        fi
    else
        DOCKER_COMPOSE="docker-compose"
    fi
}

# 停止服务
stop_services() {
    cd "$PROJECT_DIR/docker"

    log_info "停止 Docker 服务..."
    $DOCKER_COMPOSE down

    echo ""
    log_info "服务已停止"
}

main() {
    log_info "Docker 服务停止工具"
    echo "=========================================="

    check_docker_compose
    stop_services
}

main
