#!/bin/bash
# Flyway 数据库清理和重建脚本
# 功能：清空数据库并重新执行迁移
# 用法：
#   ./clean.sh           # 默认 MySQL
#   DB=h2 ./clean.sh     # 使用 H2

SCRIPT_DIR="$(cd "$(dirname "${BASH_SOURCE[0]}")" && pwd)"
PROJECT_DIR="$(dirname "$SCRIPT_DIR")"

cd "$PROJECT_DIR"

echo "======================================"
echo "数据库清理和重建"
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
    DB_NAME="H2"
else
    echo "数据库: MySQL (生产环境)"
    PROFILE=""
    DB_NAME="MySQL"
    echo ""
    echo "⚠️  警告：即将清空 MySQL 数据库的所有数据！"
    echo ""
fi

read -p "确认清理 ${DB_NAME} 数据库并重新迁移？(y/N): " -n 1 -r
echo
echo ""

if [[ ! $REPLY =~ ^[Yy]$ ]]; then
    echo "已取消"
    exit 0
fi

echo "开始清理..."
echo ""

# 清空数据库
mvn flyway:clean $PROFILE

if [ $? -ne 0 ]; then
    echo "❌ 清理失败"
    exit 1
fi

echo ""
echo "✓ 清理完成"
echo ""
echo "开始迁移..."
echo ""

# 重新迁移
mvn flyway:migrate $PROFILE

if [ $? -ne 0 ]; then
    echo "❌ 迁移失败"
    exit 1
fi

echo ""
echo "======================================"
echo "✓ 数据库重建完成"
echo "======================================"
echo ""
echo "提示："
echo "  H2 开发环境: DB=h2 ./clean.sh"
echo "  MySQL 生产环境: ./clean.sh"
