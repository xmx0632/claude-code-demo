#!/bin/bash
# Flyway 信息查看脚本
# 功能：查看数据库迁移状态
# 用法：
#   ./info.sh            # 默认 MySQL
#   DB=h2 ./info.sh      # 使用 H2

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

# 根据环境变量选择数据库
DB_TYPE=${DB:-mysql}

if [ "$DB_TYPE" = "h2" ]; then
    echo "数据库: H2 (开发环境)"
    PROFILE="-Ph2"
    echo ""
else
    echo "数据库: MySQL (生产环境)"
    PROFILE=""
    echo ""
fi

# 查看状态
mvn flyway:info $PROFILE

echo ""
echo "======================================"
echo ""
echo "提示："
echo "  H2 开发环境: DB=h2 ./info.sh"
echo "  MySQL 生产环境: ./info.sh"
