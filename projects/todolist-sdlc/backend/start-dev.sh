#!/bin/bash
# TodoList 后端服务启动脚本 (Dev 环境)
# 使用方法: ./start-dev.sh

set -e

# 颜色输出
RED='\033[0;31m'
GREEN='\033[0;32m'
YELLOW='\033[1;33m'
NC='\033[0m' # No Color

# 项目信息
PROJECT_NAME="todolist-sdlc"
PORT=8080

# 获取脚本所在目录
SCRIPT_DIR="$(cd "$(dirname "${BASH_SOURCE[0]}")" && pwd)"
cd "$SCRIPT_DIR"

echo -e "${GREEN}======================================${NC}"
echo -e "${GREEN}  TodoList 后端服务 (Dev 环境)${NC}"
echo -e "${GREEN}======================================${NC}"

# 自动检测 Java 17
JAVA_HOME=$(/usr/libexec/java_home -v 17 2>/dev/null) || true
if [ -z "$JAVA_HOME" ]; then
    echo -e "${YELLOW}警告: 未找到 Java 17，使用系统默认 Java${NC}"
    unset JAVA_HOME
else
    echo -e "${GREEN}使用 Java: $JAVA_HOME${NC}"
fi

# 检查 Java 版本
if [ -n "$JAVA_HOME" ]; then
    JAVA_VERSION=$("$JAVA_HOME/bin/java" -version 2>&1 | head -1 | cut -d'"' -f2 | cut -d'.' -f1)
    if [ "$JAVA_VERSION" -ne 17 ] && [ "$JAVA_VERSION" -lt 17 ]; then
        echo -e "${RED}错误: 需要 Java 17 或更高版本${NC}"
        echo -e "${YELLOW}当前版本: $JAVA_VERSION${NC}"
        exit 1
    fi
fi

# 检查端口是否被占用
if lsof -Pi :$PORT -sTCP:LISTEN -t >/dev/null 2>&1; then
    echo -e "${YELLOW}端口 $PORT 已被占用，正在尝试关闭...${NC}"
    PID=$(lsof -ti :$PORT)
    kill -9 $PID 2>/dev/null || true
    sleep 2
fi

echo -e "${GREEN}启动中...${NC}"
echo -e "  - 端口: $PORT"
echo -e "  - 环境: dev"
echo -e "  - H2 Console: http://localhost:$PORT/h2-console"
echo -e "  - API 文档: http://localhost:$PORT/doc.html"
echo ""

# 启动服务
if [ -n "$JAVA_HOME" ]; then
    export JAVA_HOME
    mvn spring-boot:run -Dspring-boot.run.profiles=dev
else
    mvn spring-boot:run -Dspring-boot.run.profiles=dev
fi
