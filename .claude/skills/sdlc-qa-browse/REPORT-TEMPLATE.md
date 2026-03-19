# 测试报告模板

使用 gstack browse 进行 QA 测试后，应生成结构化的测试报告并保存到 `.test-report/` 目录。

## 报告结构

```
.test-report/
├── 2026-03-19-143022/          # 时间戳目录 (YYYY-MM-DD-HHMMSS)
│   ├── TEST-REPORT.md          # 测试报告（包含截图）
│   ├── screenshot-1.png         # 测试截图
│   ├── screenshot-2.png
│   └── ...
├── 2026-03-19-150845/
│   ├── TEST-REPORT.md
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

# 初始化报告文件
cat > "$REPORT_DIR/TEST-REPORT.md" << 'REPORT_EOF'
# {项目名称} QA 测试报告

## 测试概述

**测试日期**: $(date +"%Y-%m-%d %H:%M:%S")
**测试工具**: gstack browse (headless Chromium)
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
```

## 报告模板

```markdown
# {项目名称} QA 测试报告

## 测试概述

**测试日期**: {YYYY-MM-DD HH:MM:SS}
**测试工具**: gstack browse (headless Chromium)
**测试环境**: {开发/测试/生产}
**测试人员**: {测试人员名称}

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

**测试工具**: gstack browse v{version}
**报告生成时间**: {YYYY-MM-DD HH:mm:ss UTC}
```

## 截图规范

1. **固定宽度**: `<img src="screenshot.png" width="600">`
2. **命名规范**:
   - `{feature}-page.png` - 功能页面
   - `{action}-before.png` - 操作前
   - `{action}-after.png` - 操作后
   - `{error}-error.png` - 错误截图

3. **截图类型**:
   - 全页面: `$B screenshot`
   - 标注截图: `$B snapshot -i -a -o`
   - 响应式: `$B responsive`

## 完整测试流程

```bash
# 1. 创建时间戳测试目录
TIMESTAMP=$(date +"%Y-%m-%d-%H%M%S")
REPORT_DIR=".test-report/$TIMESTAMP"
mkdir -p "$REPORT_DIR"

# 2. 创建 LATEST 符号链接
rm -f .test-report/LATEST
ln -s "$TIMESTAMP" .test-report/LATEST

# 3. 初始化报告
cat > "$REPORT_DIR/TEST-REPORT.md" << 'EOF'
# {项目名称} QA 测试报告

## 测试概述

**测试日期**: $(date +"%Y-%m-%d %H:%M:%S")
**测试工具**: gstack browse

## 服务状态

| 服务 | 状态 | 端口 |
|------|------|------|
| 前端 | ✅ | 5173 |
| 后端 | ✅ | 8080 |

EOF

# 4. 初始化 browse
_ROOT=$(git rev-parse --show-toplevel 2>/dev/null)
B="$_ROOT/.claude/skills/sdlc-qa-browse/dist/browse"

# 5. 执行测试并保存截图到时间戳目录
$B goto http://localhost:3000
$B screenshot "$REPORT_DIR/login-page.png"

# 6. 继续测试，添加到报告...
# echo "### 登录测试 ✅" >> "$REPORT_DIR/TEST-REPORT.md"
# echo '<img src="login-page.png" width="600">' >> "$REPORT_DIR/TEST-REPORT.md"

# 7. 查看报告
echo "报告已生成: $REPORT_DIR/TEST-REPORT.md"
echo "最新报告: .test-report/LATEST/TEST-REPORT.md"
```

## 快捷命令

```bash
# 查看所有测试历史
ls -la .test-report/

# 查看最新测试报告
cat .test-report/LATEST/TEST-REPORT.md

# 清理旧测试（保留最近 5 次）
ls -t .test-report/ | tail -n +6 | xargs -I {} rm -rf ".test-report/{}"
```
