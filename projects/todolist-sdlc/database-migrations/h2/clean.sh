#!/bin/bash
# H2 数据库清理脚本
# 功能：清空 H2 数据库文件

SCRIPT_DIR="$(cd "$(dirname "${BASH_SOURCE[0]}")" && pwd)"
PROJECT_DIR="$(dirname "$SCRIPT_DIR")"

cd "$PROJECT_DIR"

echo "======================================"
echo "清理 H2 数据库"
echo "======================================"
echo ""

# 检查 H2 目录是否存在
if [ ! -d "h2" ]; then
    echo "H2 目录不存在，无需清理"
    exit 0
fi

echo "将删除以下文件："
ls -lh h2/*.db 2>/dev/null || echo "无 .db 文件"
echo ""

read -p "确认删除？(y/N): " -n 1 -r
echo
if [[ $REPLY =~ ^[Yy]$ ]]; then
    rm -f h2/*.db
    rm -f h2/*.lock.db
    echo "✓ H2 数据库已清理"
else
    echo "已取消"
fi

echo ""
echo "======================================"
