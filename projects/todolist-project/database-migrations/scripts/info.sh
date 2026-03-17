#!/bin/bash
# Flyway 信息查看脚本
# 功能：查看数据库迁移状态
# 用法：./info.sh

SCRIPT_DIR="$(cd "$(dirname "${BASH_SOURCE[0]}")" && pwd)"
PROJECT_DIR="$(dirname "$SCRIPT_DIR")"

cd "$PROJECT_DIR"

echo "======================================"
echo "Flyway 迁移状态"
echo "======================================"
echo ""

# 检查 Maven 是否安装
if ! command -v mvn &> /dev/null; then
    echo "错误: 未找到 Maven 命令"
    echo ""
    echo "安装方法："
    echo "  macOS: brew install maven"
    echo "  Linux: sudo apt install maven"
    echo ""
    exit 1
fi

# 查看状态
echo "数据库: MySQL"
echo ""
mvn flyway:info

echo ""
echo "======================================"
