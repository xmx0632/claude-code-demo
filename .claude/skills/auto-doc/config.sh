# AutoDoc 配置文件
# 定义工作目录和路径

# 获取项目根目录（skill 目录的父目录的父目录）
PROJECT_ROOT="$(cd "$(dirname "${BASH_SOURCE[0]}")/../../.." && pwd)"

# 工作目录（项目根目录下的固定目录）
AUTODOC_WORKSPACE="$PROJECT_ROOT/.auto-doc-workspace"

# 工作目录结构
AUTODOC_INPUT_DIR="$AUTODOC_WORKSPACE/input"
AUTODOC_OUTPUT_DIR="$AUTODOC_WORKSPACE/output"
AUTODOC_TEMPLATE_DIR="$AUTODOC_WORKSPACE/template"
AUTODOC_TEMP_DIR="$AUTODOC_WORKSPACE/temp"
