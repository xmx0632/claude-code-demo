#!/bin/bash
# H2 数据库迁移脚本
# 功能：执行 Flyway 迁移到 H2 数据库

SCRIPT_DIR="$(cd "$(dirname "${BASH_SOURCE[0]}")" && pwd)"
PROJECT_DIR="$(dirname "$SCRIPT_DIR")"

cd "$PROJECT_DIR"

echo "======================================"
echo "H2 数据库迁移"
echo "======================================"
echo ""

# 检查 Maven
if ! command -v mvn &> /dev/null; then
    echo "错误: 未找到 Maven 命令"
    exit 1
fi

# 检查 Java 版本
JAVA_VERSION=$(java -version 2>&1 | head -1 | cut -d'"' -f2 | cut -d'.' -f1)
if [ "$JAVA_VERSION" -lt 17 ]; then
    echo "警告: 需要 Java 17 或更高版本"
    echo "当前 Java 版本: $JAVA_VERSION"
    echo ""
    echo "请设置 JAVA_HOME 到 Java 17+，例如："
    echo "  export JAVA_HOME=/Library/Java/JavaVirtualMachines/jdk-17.jdk/Contents/Home"
    echo ""
fi

# 执行迁移
echo "执行 Flyway 迁移到 H2..."
echo ""
mvn flyway:migrate -Ph2

echo ""
echo "======================================"
echo "迁移完成"
echo "======================================"
echo ""
echo "查看迁移状态: mvn flyway:info -Ph2"
echo "清理数据库: ./h2/clean.sh"
