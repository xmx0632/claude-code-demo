#!/bin/bash
# Flyway 迁移脚本
# 功能：执行数据库迁移
# 用法：
#   ./migrate.sh         # 默认 MySQL
#   DB=h2 ./migrate.sh   # 使用 H2

SCRIPT_DIR="$(cd "$(dirname "${BASH_SOURCE[0]}")" && pwd)"
PROJECT_DIR="$(dirname "$SCRIPT_DIR")"

cd "$PROJECT_DIR"

echo "======================================"
echo "Flyway 数据库迁移"
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

# 执行迁移
mvn flyway:migrate $PROFILE

echo ""
echo "======================================"
echo "迁移完成"
echo "======================================"
echo ""
echo "提示："
echo "  H2 开发环境: DB=h2 ./migrate.sh"
echo "  MySQL 生产环境: ./migrate.sh"
