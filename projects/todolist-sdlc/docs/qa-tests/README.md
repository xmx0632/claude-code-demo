# QA 测试示例

本目录包含使用 gstack browse skill 进行自动化测试的示例。

## 文件说明

| 文件 | 说明 |
|------|------|
| `browse-skill-qa-report.md` | 完整测试报告（Markdown） |
| `browse-qa-test-final.png` | 测试截图 |

## 测试工具

使用 gstack browse 二进制进行浏览器自动化测试：

```bash
B="/Volumes/macext/code/demo/claude-code-demo/.claude/skills/sdlc-qa-browse/dist/browse"

# 基本操作
$B goto http://localhost:5173
$B snapshot -i              # 获取交互元素
$B fill @e1 "value"         # 填充表单
$B click @e3                # 点击按钮
$B screenshot /tmp/test.png # 截图
$B console --errors         # 检查错误
```

## 测试覆盖

- ✅ 用户注册
- ✅ 用户登录
- ✅ 创建任务
- ✅ 创建标签
- ✅ 响应式布局

## 相关链接

- [gstack GitHub](https://github.com/garrytan/gstack)
- [SDLC QA Browse Skill](../../../.claude/skills/sdlc-qa-browse/SKILL.md)
