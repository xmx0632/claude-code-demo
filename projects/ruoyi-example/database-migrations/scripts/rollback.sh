#!/bin/bash

# 生成回滚脚本
#
# 注意: Flyway Community Edition 不支持自动回滚
# 此脚本生成手动回滚 SQL 供参考
#
# 使用方法:
#   ./scripts/rollback.sh <版本号>

set -e

# 获取脚本所在目录
SCRIPT_DIR="$(cd "$(dirname "${BASH_SOURCE[0]}")" && pwd)"
PROJECT_DIR="$(dirname "$SCRIPT_DIR")"

# 颜色输出
GREEN='\033[0;32m'
YELLOW='\033[1;33m'
RED='\033[0;31m'
NC='\033[0m'

log_info() {
    echo -e "${GREEN}[INFO]${NC} $1"
}

log_warn() {
    echo -e "${YELLOW}[WARN]${NC} $1"
}

# 检查参数
if [ -z "$1" ]; then
    echo "使用方法: $0 <版本号>"
    echo "示例: $0 V8"
    exit 1
fi

VERSION="$1"
MIGRATION_DIR="$PROJECT_DIR/migrations"

log_info "生成 $VERSION 的回滚脚本"
echo "=========================================="

# 查找对应的迁移文件
MIGRATION_FILE=$(find "$MIGRATION_DIR" -name "${VERSION}__*.sql" | head -n 1)

if [ -z "$MIGRATION_FILE" ]; then
    echo "错误: 找不到版本 $VERSION 的迁移文件"
    exit 1
fi

log_info "迁移文件: $MIGRATION_FILE"

# 生成回滚文件名
ROLLBACK_FILE="${MIGRATION_FILE%.sql}.rollback.sql"

log_warn "注意: 请根据以下模板手动编写回滚 SQL"
echo ""
echo "=========================================="
cat << 'EOF'
-- 回滚 SQL 模板
-- 请根据实际迁移内容编写对应的回滚语句

-- 示例 1: 回滚添加的字段
-- ALTER TABLE table_name DROP COLUMN column_name;

-- 示例 2: 回滚创建的表
-- DROP TABLE IF EXISTS table_name;

-- 示例 3: 回滚插入的数据
-- DELETE FROM table_name WHERE condition;

-- 示例 4: 回滚索引
-- DROP INDEX index_name ON table_name;

-- 示例 5: 回滚外键
-- ALTER TABLE table_name DROP FOREIGN KEY fk_name;
EOF
echo "=========================================="
echo ""
log_info "建议将回滚脚本保存到: $ROLLBACK_FILE"
echo ""
log_warn "执行回滚前请先备份数据库！"
