#!/bin/bash

# Docker 服务清理脚本
#
# 使用方法:
#   ./scripts/clean.sh

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

log_warn() {
    echo -e "${YELLOW}[WARN]${NC} $1"
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

# 清理服务
clean_services() {
    cd "$PROJECT_DIR/docker"

    log_warn "警告：此操作将删除所有容器、网络和数据卷"
    echo "数据将被永久删除，是否继续？[y/N]"
    read -r response

    if [[ ! "$response" =~ ^[Yy]$ ]]; then
        log_info "已取消"
        exit 0
    fi

    log_info "停止并删除容器..."
    $DOCKER_COMPOSE down -v

    log_info "删除未使用的数据卷..."
    docker volume prune -f

    echo ""
    log_info "清理完成！"
}

# 显示使用状态
show_status() {
    cd "$PROJECT_DIR/docker"

    echo ""
    log_info "当前容器状态:"
    docker ps -a --filter "name=ruoyi-" --format "table {{.Names}}\t{{.Status}}\t{{.Ports}}"

    echo ""
    log_info "当前数据卷:"
    docker volume ls --filter "name=claude-code-demo" --format "table {{.Name}}\t{{.Driver}}"

    echo ""
    log_info "当前网络:"
    docker network ls --filter "name=docker" --format "table {{.Name}}\t{{.Driver}}"
}

main() {
    log_info "Docker 服务清理工具"
    echo "=========================================="

    check_docker_compose
    show_status
    clean_services

    log_info "如需重新启动，请运行: ./scripts/start.sh"
}

main
