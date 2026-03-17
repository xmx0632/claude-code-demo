#!/bin/bash
# Flyway 迁移脚本
# 功能：执行数据库迁移

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
mvn flyway:migrate

echo ""
echo "======================================"
echo "迁移完成"
echo "======================================"
