#!/bin/bash
# Flyway 验证脚本
# 功能：验证迁移脚本

SCRIPT_DIR="$(cd "$(dirname "${BASH_SOURCE[0]}")" && pwd)"
PROJECT_DIR="$(dirname "$SCRIPT_DIR")"

cd "$PROJECT_DIR"

echo "======================================"
echo "Flyway 验证迁移脚本"
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

# 验证
flyway -configFiles=flyway.conf validate

echo ""
echo "======================================"
