#!/bin/bash
# TodoList 前端服务停止脚本

set -e

# 颜色输出
RED='\033[0;31m'
GREEN='\033[0;32m'
YELLOW='\033[1;33m'
NC='\033[0m'

echo -e "${YELLOW}正在停止 TodoList 前端服务...${NC}"

# 查找 Vite 进程并停止
PIDS=$(ps aux | grep -E "vite|node.*todolist-frontend" | grep -v grep | awk '{print $2}')

if [ -z "$PIDS" ]; then
    echo -e "${YELLOW}没有找到运行中的前端服务${NC}"
    exit 0
fi

echo -e "${GREEN}找到进程: $PIDS${NC}"
for PID in $PIDS; do
    kill -9 $PID 2>/dev/null || true
    echo -e "${GREEN}已停止 PID: $PID${NC}"
done

echo -e "${GREEN}前端服务已停止${NC}"
