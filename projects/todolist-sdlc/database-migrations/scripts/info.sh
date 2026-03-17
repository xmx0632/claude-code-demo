#!/bin/bash
# Flyway 信息查看脚本
# 功能：查看数据库迁移状态

SCRIPT_DIR="$(cd "$(dirname "${BASH_SOURCE[0]}")" && pwd)"
PROJECT_DIR="$(dirname "$SCRIPT_DIR")"

cd "$PROJECT_DIR"

echo "======================================"
echo "Flyway 迁移状态"
echo "======================================"
echo ""

# 检查 Flyway 是否安装
if ! command -v flyway &> /dev/null; then
    echo "错误: 未找到 Flyway 命令"
    echo ""
    echo "安装方法："
    echo "  macOS: brew install flyway"
    echo "  Linux: 下载 https://flywaydb.org/download"
    echo ""
    exit 1
fi

# 查看状态
flyway -configFiles=flyway.conf info

echo ""
echo "======================================"
