#!/bin/bash
# H2 Console 启动脚本
# 功能：启动 H2 Web Console

SCRIPT_DIR="$(cd "$(dirname "${BASH_SOURCE[0]}")" && pwd)"
PROJECT_DIR="$(dirname "$SCRIPT_DIR")"

cd "$PROJECT_DIR"

echo "======================================"
echo "启动 H2 Console"
echo "======================================"
echo ""

# 检查 H2 jar
H2_JAR="$HOME/.m2/repository/com/h2database/h2/1.4.200/h2-1.4.200.jar"

if [ ! -f "$H2_JAR" ]; then
    echo "错误: 未找到 H2 jar 包"
    echo "请先运行: mvn flyway:migrate -Ph2"
    exit 1
fi

# 检查数据库文件
if [ ! -f "h2/todolist.mv.db" ]; then
    echo "警告: H2 数据库文件不存在"
    echo "请先运行: mvn flyway:migrate -Ph2"
    echo ""
fi

echo "正在启动 H2 Console..."
echo ""
echo "连接信息："
echo "  JDBC URL: jdbc:h2:file:$(pwd)/h2/todolist"
echo "  User Name: sa"
echo "  Password: (留空)"
echo ""
echo "按 Ctrl+C 停止服务器"
echo ""

java -cp "$H2_JAR" org.h2.tools.Console \
    -web -webAllowOthers \
    -browser \
    -driver "org.h2.Driver" \
    -url "jdbc:h2:file:$(pwd)/h2/todolist" \
    -user "sa" \
    -password ""
