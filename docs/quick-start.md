# 快速入门指南

## 环境准备

### 1.1 安装 Node.js

**下载地址**：https://nodejs.org/

**推荐版本**：Node.js 18.x 或 20.x LTS

**安装验证**：
```powershell
node -v
npm -v
```

### 1.2 安装 Claude Code CLI

```powershell
npm install -g @anthropic-ai/claude-code
```

**验证安装**：
```powershell
claude --version
```

### 1.3 获取智谱 AI API Key

1. 访问 https://open.bigmodel.cn/
2. 注册账号并登录
3. 进入「API Key」页面
4. 创建新的 API Key
5. 保存 Key（后续配置使用）

> 新用户通常有免费额度，可在控制台查看

## 配置 glm4.7 模型

### 方式一：自动化配置（推荐）

```powershell
npx @z_ai/coding-helper
```

按提示输入智谱 API Key 即可。

### 方式二：手动配置

**1. 创建配置目录**：
```powershell
mkdir $env:USERPROFILE\.claude
```

**2. 创建配置文件** `$env:USERPROFILE\.claude\settings.json`：

```json
{
  "env": {
    "ANTHROPIC_AUTH_TOKEN": "你的智谱API_Key",
    "ANTHROPIC_BASE_URL": "https://open.bigmodel.cn/api/paas/v4",
    "API_TIMEOUT_MS": "3000000",
    "CLAUDE_CODE_DISABLE_NONESSENTIAL_TRAFFIC": 1
  }
}
```

**3. 验证配置**：
```powershell
# 查看当前配置
claude settings

# 测试模型连接
claude "你好，请介绍一下你自己"
```

## Hello World 测试

### 创建测试项目

```powershell
# 创建项目目录
mkdir test-project
cd test-project

# 初始化项目
claude
```

### 基础命令

```
# 查看帮助
claude --help

# 直接提问
claude "解释这段代码的作用"

# 读取文件并提问
claude "分析 package.json 的依赖"

# 编辑文件
claude "在 README.md 中添加项目简介"
```

### 验证 glm4.7 连接

如果配置正确，Claude 会使用智谱的 glm4.7 模型进行响应。

**常见问题**：

| 问题 | 解决方案 |
|------|----------|
| `API_KEY not found` | 检查 settings.json 中的 API Key 是否正确 |
| `Connection timeout` | 检查网络连接和代理设置 |
| `Model not found` | 检查 BASE_URL 配置 |

## 下一步

配置完成后，继续阅读：
- [Happy 远程开发指南](./happy-remote.md)
- [Skills 开发指南](./skills-guide.md)
