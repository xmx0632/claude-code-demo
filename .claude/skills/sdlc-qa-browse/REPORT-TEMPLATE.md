# 测试报告模板

使用 browse 进行 QA 测试后，应生成结构化的测试报告并保存到 `.test-report/` 目录。

## 报告结构

```
.test-report/
├── 2026-03-19-143022/          # 时间戳目录 (YYYY-MM-DD-HHMMSS)
│   ├── TEST-REPORT.md          # 测试报告（包含截图）
│   ├── test-script.sh          # 测试复现脚本 ⭐ 新增
│   ├── screenshot-1.png         # 测试截图
│   ├── screenshot-2.png
│   └── ...
├── 2026-03-19-150845/
│   ├── TEST-REPORT.md
│   ├── test-script.sh
│   └── ...
└── LATEST -> 2026-03-19-150845  # 符号链接指向最新测试
```

## 初始化测试目录

```bash
# 创建时间戳目录
TIMESTAMP=$(date +"%Y-%m-%d-%H%M%S")
REPORT_DIR=".test-report/$TIMESTAMP"
mkdir -p "$REPORT_DIR"

# 创建 LATEST 符号链接
rm -f .test-report/LATEST
ln -s "$TIMESTAMP" .test-report/LATEST

# 初始化测试脚本文件
cat > "$REPORT_DIR/test-script.sh" << 'SCRIPT_EOF'
#!/bin/bash
# QA 测试复现脚本
# 生成时间: $(date +"%Y-%m-%d %H:%M:%S")

# 设置 browse 工具路径
_ROOT=$(git rev-parse --show-toplevel 2>/dev/null)
B="$_ROOT/.claude/skills/sdlc-qa-browse/dist/browse"

# 检查 browse 工具
if [ ! -x "$B" ]; then
  echo "错误: browse 工具未就绪，请先运行测试初始化"
  exit 1
fi

echo "=== 开始复现测试 ==="
echo "测试目录: $REPORT_DIR"
echo ""

SCRIPT_EOF

chmod +x "$REPORT_DIR/test-script.sh"

# 初始化报告文件
cat > "$REPORT_DIR/TEST-REPORT.md" << 'REPORT_EOF'
# {项目名称} QA 测试报告

## 测试概述

**测试日期**: $(date +"%Y-%m-%d %H:%M:%S")
**测试工具**: browse (headless Chromium)
**测试环境**: {开发/测试/生产}
**测试人员**: {测试人员名称}

## 服务状态

| 服务 | 状态 | 端口 |
|------|------|------|
| 前端 | ✅ 运行中 | {端口} |
| 后端 | ✅ 运行中 | {端口} |
| 数据库 | ✅ 运行中 | {端口/类型} |

## 测试用例执行情况

REPORT_EOF

echo "测试目录: $REPORT_DIR"
echo "测试脚本: $REPORT_DIR/test-script.sh"
```

## 测试脚本规范

### 脚本格式

```bash
#!/bin/bash
# QA 测试复现脚本
# 用例: {测试用例名称}
# 生成时间: {YYYY-MM-DD HH:MM:SS}

_ROOT=$(git rev-parse --show-toplevel 2>/dev/null)
B="$_ROOT/.claude/skills/sdlc-qa-browse/dist/browse"

echo "=== {测试用例名称} ==="
echo "开始时间: $(date +"%Y-%m-%d %H:%M:%S")"
echo ""

# 测试前准备
echo "[准备] 导航到测试页面..."
$B goto http://localhost:3000

# 测试步骤1
echo "[步骤1] 输入用户名..."
$B fill "#username" "test@example.com"

# 测试步骤2
echo "[步骤2] 输入密码..."
$B fill "#password" "password123"

# 测试步骤3
echo "[步骤3] 点击登录按钮..."
$B click "#login-button"

# 验证结果
echo "[验证] 检查登录成功..."
$B wait --text "欢迎" 2>/dev/null || echo "⚠️  超时: 未找到预期文本"

# 截图证据
echo "[截图] 保存测试结果..."
$B screenshot ".test-report/{TIMESTAMP}/login-result.png"

echo ""
echo "=== 测试完成 ==="
echo "结束时间: $(date +"%Y-%m-%d %H:%M:%S")"
```

### 脚本命名

| 场景 | 脚本名格式 | 示例 |
|------|-----------|------|
| 功能测试 | `{feature}-test.sh` | `login-test.sh` |
| Bug 复现 | `bug-{id}-reproduce.sh` | `bug-001-reproduce.sh` |
| 回归测试 | `regression-{module}.sh` | `regression-auth.sh` |
| 冒烟测试 | `smoke-test.sh` | `smoke-test.sh` |

## 报告模板

```markdown
# {项目名称} QA 测试报告

## 测试概述

**测试日期**: {YYYY-MM-DD HH:MM:SS}
**测试工具**: browse (headless Chromium)
**测试环境**: {开发/测试/生产}
**测试人员**: {测试人员名称}

## 快速复现

⭐ **一键复现测试**: 运行脚本即可复现完整测试流程
```bash
cd .test-report/{TIMESTAMP}
./test-script.sh
```

## 服务状态

| 服务 | 状态 | 端口 |
|------|------|------|
| 前端 | ✅ 运行中 | {端口} |
| 后端 | ✅ 运行中 | {端口} |
| 数据库 | ✅ 运行中 | {端口/类型} |

## 测试用例执行情况

### {测试用例标题} ✅/❌

**测试步骤**:
1. {步骤1}
2. {步骤2}
3. {步骤3}

**测试结果**: ✅ 通过 / ❌ 失败
- API 响应: {响应信息}
- 页面跳转: {跳转信息}
- 数据持久化: {持久化状态}

**截图说明**:
<img src="{screenshot-filename}.png" width="600">

## 测试覆盖率

| 模块 | 测试用例数 | 通过 | 失败 | 覆盖率 |
|------|-----------|------|------|--------|
| {模块1} | {n} | {n} | {n} | {xx}% |
| **总计** | **{n}** | **{n}** | **{n}** | **{xx}%** |

## 发现的问题

### {问题标题}

**严重程度**: 高/中/低
**复现脚本**: `bug-{id}-reproduce.sh`

**复现步骤**:
1. {步骤}
2. {步骤}

**预期结果**: {预期}
**实际结果**: {实际}

## 结论

**测试结果**: ✅ 全部通过 / ⚠️ 部分通过 / ❌ 存在阻塞问题

{总结性描述}

## 测试环境配置

- **前端**: {技术栈}
- **后端**: {技术栈}
- **数据库**: {数据库类型}
- **API 文档**: {文档地址}

---

**测试工具**: browse v{version}
**报告生成时间**: {YYYY-MM-DD HH:mm:ss UTC}
```

## 完整测试流程

```bash
# 1. 创建时间戳测试目录
TIMESTAMP=$(date +"%Y-%m-%d-%H%M%S")
REPORT_DIR=".test-report/$TIMESTAMP"
mkdir -p "$REPORT_DIR"

# 2. 创建 LATEST 符号链接
rm -f .test-report/LATEST
ln -s "$TIMESTAMP" .test-report/LATEST

# 3. 初始化测试脚本
cat > "$REPORT_DIR/test-script.sh" << 'EOF'
#!/bin/bash
ROOT=$(git rev-parse --show-toplevel 2>/dev/null)
B="$ROOT/.claude/skills/sdlc-qa-browse/dist/browse"

echo "=== 登录功能测试 ==="
$B goto http://localhost:3000
$B fill "#username" "test@example.com"
$B fill "#password" "password123"
$B click "#login-button"
$B wait --text "欢迎" 5 || echo "登录超时"
$B screenshot "$REPORT_DIR/login-result.png"
EOF
chmod +x "$REPORT_DIR/test-script.sh"

# 4. 初始化报告
cat > "$REPORT_DIR/TEST-REPORT.md" << 'EOF'
# 登录功能测试报告

## 快速复现
\`\`\`bash
./test-script.sh
\`\`\`

## 测试结果
EOF

# 5. 执行测试
cd "$REPORT_DIR"
./test-script.sh > test-output.log 2>&1

# 6. 将测试输出追加到报告
echo "" >> TEST-REPORT.md
echo "## 测试执行日志" >> TEST-REPORT.md
echo "\`\`\`" >> TEST-REPORT.md
cat test-output.log >> TEST-REPORT.md
echo "\`\`\`" >> TEST-REPORT.md

# 7. 查看报告
echo "报告已生成: $REPORT_DIR/TEST-REPORT.md"
echo "测试脚本: $REPORT_DIR/test-script.sh"
```

## 测试输出日志

在测试脚本中保存执行日志：

```bash
# 在 test-script.sh 中添加日志记录
LOG_FILE="$REPORT_DIR/test-output.log"

# 记录每一步
{
  echo "$(date +"%H:%M:%S") [INFO] 开始测试"
  echo "$(date +"%H:%M:%S") [INFO] 导航到登录页面"
  $B goto http://localhost:3000

  echo "$(date +"%H:%M:%S") [INFO] 填写用户名"
  $B fill "#username" "test@example.com"

  echo "$(date +"%H:%M:%S") [INFO] 点击登录"
  $B click "#login-button"

  echo "$(date +"%H:%M:%S") [INFO] 验证结果"
  $B wait --text "欢迎" && echo "✅ 登录成功" || echo "❌ 登录失败"

  echo "$(date +"%H:%M:%S") [INFO] 测试完成"
} | tee -a "$LOG_FILE"
```

## 快捷命令

```bash
# 查看所有测试历史
ls -la .test-report/

# 查看最新测试报告
cat .test-report/LATEST/TEST-REPORT.md

# 运行最新测试脚本
cd .test-report/LATEST && ./test-script.sh

# 清理旧测试（保留最近 5 次）
ls -t .test-report/ | tail -n +6 | xargs -I {} rm -rf ".test-report/{}"
```

## 测试脚本最佳实践

1. **注释清晰**: 每个步骤都有注释说明
2. **错误处理**: 使用 `|| echo` 捕获命令失败
3. **时间戳**: 关键操作记录时间戳
4. **状态输出**: 显示当前执行状态
5. **独立可执行**: 脚本可直接运行复现
