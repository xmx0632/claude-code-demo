#!/bin/bash
# Flyway 验证脚本
# 功能：验证迁移脚本
# 用法：./validate.sh

SCRIPT_DIR="$(cd "$(dirname "${BASH_SOURCE[0]}")" && pwd)"
PROJECT_DIR="$(dirname "$SCRIPT_DIR")"

cd "$PROJECT_DIR"

echo "======================================"
echo "Flyway 验证迁移脚本"
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

# 验证
echo "数据库: MySQL"
echo ""
mvn flyway:validate

echo ""
echo "======================================"
