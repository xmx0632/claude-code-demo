#!/bin/bash
# H2 Console 独立启动脚本
# 功能：启动 H2 TCP 服务器和 Web Console（无需 Spring Boot 应用）
# 用法：./console.sh

SCRIPT_DIR="$(cd "$(dirname "${BASH_SOURCE[0]}")" && pwd)"
PROJECT_DIR="$(dirname "$SCRIPT_DIR")"

cd "$PROJECT_DIR"

# 清理可能残留的锁文件
cleanup_lock() {
    echo "清理数据库锁文件..."
    rm -f h2/data/*.lock.db 2>/dev/null
}

# 退出时清理
trap cleanup_lock EXIT

echo "======================================"
echo "启动 H2 Console"
echo "======================================"
echo ""

# 检查 H2 jar
H2_JAR="$HOME/.m2/repository/com/h2database/h2/1.4.200/h2-1.4.200.jar"

if [ ! -f "$H2_JAR" ]; then
    echo "❌ 错误: 未找到 H2 jar 包"
    echo ""
    echo "请先运行以下命令下载依赖："
    echo "  cd $PROJECT_DIR"
    echo "  mvn flyway:migrate -Ph2"
    echo ""
    exit 1
fi

# 确保数据库文件存在
if [ ! -f "h2/data/ruoyi_example.mv.db" ]; then
    echo "⚠️  警告: H2 数据库文件不存在"
    echo ""
    read -p "是否现在初始化数据库？(y/N): " -n 1 -r
    echo
    if [[ $REPLY =~ ^[Yy]$ ]]; then
        echo ""
        echo "正在初始化数据库..."
        mvn flyway:clean -Ph2 && mvn flyway:migrate -Ph2
        if [ $? -ne 0 ]; then
            echo "❌ 数据库初始化失败"
            exit 1
        fi
    fi
    echo ""
fi

# 清理旧锁文件
cleanup_lock

DB_DIR="$(pwd)/h2/data"
DB_NAME="ruoyi_example"

echo "======================================"
echo "H2 Console 信息"
echo "======================================"
echo ""
echo "📍 Web Console:  http://localhost:8082"
echo "📍 TCP Server:   jdbc:h2:tcp://localhost:9092/$DB_NAME"
echo "📍 File URL:     jdbc:h2:file:${DB_DIR}/${DB_NAME}"
echo "👤 User Name:    sa"
echo "🔑 Password:     (留空)"
echo ""
echo "按 Ctrl+C 停止服务器"
echo ""
echo "提示：使用 TCP URL 可以避免文件锁定问题"
echo "======================================"
echo ""

# 先启动 H2 TCP 服务器（后台）
java -cp "$H2_JAR" org.h2.tools.Server \
    -tcp -tcpPort 9092 \
    -tcpAllowOthers \
    -baseDir "$DB_DIR" > /dev/null 2>&1 &
TCP_PID=$!

# 等待 TCP 服务器启动
sleep 2

# 启动 H2 Console（前台）
# 使用 TCP 连接而不是文件连接，避免锁定问题
java -cp "$H2_JAR" org.h2.tools.Console \
    -web \
    -webPort 8082 \
    -webAllowOthers \
    -browser \
    -driver "org.h2.Driver" \
    -url "jdbc:h2:tcp://localhost:9092/$DB_NAME" \
    -user "sa" \
    -password ""

# Console 关闭后也关闭 TCP 服务器
kill $TCP_PID 2>/dev/null
