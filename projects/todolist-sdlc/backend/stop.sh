#!/bin/bash
# TodoList 后端服务停止脚本

set -e

# 颜色输出
RED='\033[0;31m'
GREEN='\033[0;32m'
YELLOW='\033[1;33m'
NC='\033[0m'

PORT=8080

echo -e "${YELLOW}正在停止 TodoList 后端服务...${NC}"

# 查找并停止进程
PIDS=$(jps -l | grep -E "todolist|TodoListApplication" | awk '{print $1}')

if [ -z "$PIDS" ]; then
    echo -e "${YELLOW}没有找到运行中的服务${NC}"
    exit 0
fi

echo -e "${GREEN}找到进程: $PIDS${NC}"
for PID in $PIDS; do
    kill -9 $PID
    echo -e "${GREEN}已停止 PID: $PID${NC}"
done

echo -e "${GREEN}服务已停止${NC}"
