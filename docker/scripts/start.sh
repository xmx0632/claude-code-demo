#!/bin/bash

# Docker 服务启动脚本
#
# 使用方法:
#   ./scripts/start.sh

set -e

# 获取脚本所在目录
SCRIPT_DIR="$(cd "$(dirname "${BASH_SOURCE[0]}")" && pwd)"
PROJECT_DIR="$(dirname "$SCRIPT_DIR")"

# 颜色输出
GREEN='\033[0;32m'
YELLOW='\033[1;33m'
RED='\033[0;31m'
NC='\033[0m' # No Color

log_info() {
    echo -e "${GREEN}[INFO]${NC} $1"
}

log_warn() {
    echo -e "${YELLOW}[WARN]${NC} $1"
}

log_error() {
    echo -e "${RED}[ERROR]${NC} $1"
}

# 检查环境变量文件
check_env_file() {
    if [ ! -f "$PROJECT_DIR/.env" ]; then
        log_warn ".env 文件不存在"
        echo "是否从 .env.example 创建 .env 文件？[y/N]"
        read -r response
        if [[ "$response" =~ ^[Yy]$ ]]; then
            cp "$PROJECT_DIR/.env.example" "$PROJECT_DIR/.env"
            log_info "已创建 .env 文件，请修改其中的配置"
            log_warn "请特别注意修改密码配置！"
            echo ""
            log_info "按回车继续..."
            read -r
        else
            log_error "取消启动"
            exit 1
        fi
    fi
}

# 检查 Docker 是否运行
check_docker() {
    if ! docker info > /dev/null 2>&1; then
        log_error "Docker 未运行，请先启动 Docker"
        exit 1
    fi
}

# 检查 docker-compose
check_docker_compose() {
    if ! command -v docker-compose &> /dev/null; then
        # 尝试使用 docker compose (V2)
        if docker compose version &> /dev/null; then
            DOCKER_COMPOSE="docker compose"
        else
            log_error "docker-compose 未安装"
            echo "请访问 https://docs.docker.com/compose/install/ 安装"
            exit 1
        fi
    else
        DOCKER_COMPOSE="docker-compose"
    fi
    echo "使用: $DOCKER_COMPOSE"
}

# 启动服务
start_services() {
    cd "$PROJECT_DIR/docker"

    log_info "启动 Docker 服务..."
    echo "=========================================="

    $DOCKER_COMPOSE up -d

    echo ""
    log_info "服务启动完成！"
    echo ""
    echo "=========================================="
    log_info "服务状态:"
    $DOCKER_COMPOSE ps
    echo ""
    log_info "查看日志: ./scripts/logs.sh [服务名]"
    log_info "停止服务: ./scripts/stop.sh"
}

# 主流程
main() {
    log_info "Docker 服务启动工具"
    echo "=========================================="

    check_docker
    check_docker_compose
    check_env_file
    start_services

    echo ""
    log_info "访问地址:"
    echo "  应用: http://localhost:${APP_PORT:-8080}"
    echo "  API 文档: http://localhost:${APP_PORT:-8080}/doc.html"
}

# 执行主流程
main
