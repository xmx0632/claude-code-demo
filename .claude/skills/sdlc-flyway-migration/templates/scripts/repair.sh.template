#!/bin/bash
# Flyway 修复脚本
# 功能：修复 Flyway Schema History 表
# 适用场景：
#   - 迁移历史表损坏
#   - 校验和错误
#   - 需要重新对齐迁移状态
# 用法：
#   ./repair.sh          # 默认 MySQL
#   DB=h2 ./repair.sh    # 使用 H2

SCRIPT_DIR="$(cd "$(dirname "${BASH_SOURCE[0]}")" && pwd)"
PROJECT_DIR="$(dirname "$SCRIPT_DIR")"

cd "$PROJECT_DIR"

echo "======================================"
echo "Flyway Schema History 修复"
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
else
    echo "数据库: MySQL (生产环境)"
    PROFILE=""
fi

echo ""
echo "Flyway Repair 会："
echo "  - 重新计算所有迁移的校验和"
echo "  - 修复损坏的迁移历史记录"
echo "  - 移除缺失的迁移记录"
echo ""
echo "⚠️  注意：这不会修改已应用的迁移，只修复历史表"
echo ""

read -p "确认执行修复？(y/N): " -n 1 -r
echo
echo ""

if [[ ! $REPLY =~ ^[Yy]$ ]]; then
    echo "已取消"
    exit 0
fi

# 执行修复
mvn flyway:repair $PROFILE

if [ $? -eq 0 ]; then
    echo ""
    echo "======================================"
    echo "✓ 修复完成"
    echo "======================================"
    echo ""
    echo "建议执行验证："
    if [ "$DB_TYPE" = "h2" ]; then
        echo "  DB=h2 ./scripts/validate.sh"
        echo "  DB=h2 ./scripts/info.sh"
    else
        echo "  ./scripts/validate.sh"
        echo "  ./scripts/info.sh"
    fi
else
    echo ""
    echo "======================================"
    echo "❌ 修复失败"
    echo "======================================"
    echo ""
    echo "可能的原因："
    echo "  - 数据库连接失败"
    echo "  - 权限不足"
    echo "  - 历史表严重损坏"
    echo ""
    echo "如果历史表严重损坏，请考虑："
    echo "  1. 手动删除 flyway_schema_history 表"
    echo "  2. 使用 ./scripts/clean.sh 重建数据库"
    exit 1
fi
