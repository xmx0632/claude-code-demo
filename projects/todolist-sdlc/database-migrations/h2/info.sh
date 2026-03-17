#!/bin/bash
# H2 数据库信息查看脚本
# 功能：查看 H2 数据库文件状态

SCRIPT_DIR="$(cd "$(dirname "${BASH_SOURCE[0]}")" && pwd)"
PROJECT_DIR="$(dirname "$SCRIPT_DIR")"

cd "$PROJECT_DIR"

echo "======================================"
echo "H2 数据库状态"
echo "======================================"
echo ""

# 检查 H2 目录
if [ ! -d "h2" ]; then
    echo "H2 目录不存在"
    echo "请先运行: mvn flyway:migrate -Ph2"
    exit 1
fi

# 显示数据库文件
echo "数据库文件："
ls -lh h2/*.db 2>/dev/null || echo "无数据库文件"
echo ""

# 显示文件大小
if [ -f "h2/todolist.mv.db" ]; then
    SIZE=$(du -sh h2/todolist.mv.db | cut -f1)
    echo "数据库大小: $SIZE"
    echo "创建时间: $(stat -f '%Sm' -t '%Y-%m-%d %H:%M:%S' h2/todolist.mv.db)"
    echo "修改时间: $(stat -f '%Mm' -t '%Y-%m-%d %H:%M:%S' h2/todolist.mv.db)"
fi

echo ""
echo "======================================"
echo ""
echo "使用 Maven 查看迁移状态："
echo "  mvn flyway:info -Ph2"
