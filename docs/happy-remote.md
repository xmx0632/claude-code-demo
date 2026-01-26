# Happy 远程开发指南

## Happy 简介

Happy 是 Claude Code 的移动端客户端，让你可以在手机上远程控制 Claude Code 进行开发。

**核心特性**：
- 端到端加密通信
- 多会话同时管理
- 语音输入支持
- 实时推送通知

## 快速配置

### 安装 Happy CLI

```powershell
npm install -g happy-coder
```

### 启动 Happy

```powershell
happy
```

启动后会显示：
1. 中继服务器状态
2. 二维码（手机 App 扫码配对）
3. 本地 Claude Code 会话列表

### 手机 App 配对

**iOS**：
1. App Store 搜索 "Happy Coder"
2. 下载并安装
3. 扫描 CLI 显示的二维码配对

**Android**：
1. 访问 https://happy.engineering/android
2. 下载 APK 安装
3. 扫描二维码配对

## 远程使用

### 发送命令

在手机 App 中：
1. 选择活跃的 Claude Code 会话
2. 输入命令或问题
3. 发送（支持语音输入）

### 查看输出

- 实时显示 Claude 的响应
- 代码高亮显示
- 支持复制完整输出

### 推送通知

当以下情况发生时会收到通知：
- Claude 需要用户输入
- 代码生成完成
- 执行出错

## 工作流集成

### 与现有项目结合

```powershell
# 进入项目目录
cd your-project

# 启动 Happy
happy

# 启动 Claude Code
claude
```

现在你可以在手机上：
- 提交代码：`claude "提交当前更改，说明实现了用户管理功能"`
- 运行测试：`claude "运行单元测试并修复失败的用例"`
- 查看日志：`claude "检查最近的错误日志"`

### 多会话管理

Happy 支持同时管理多个 Claude Code 会话：
- 不同项目
- 不同分支
- 不同任务

### 语音操作

1. 点击麦克风图标
2. 说出命令
3. 自动识别并发送

**示例语音命令**：
- "帮我生成用户表的 CRUD 代码"
- "解释这段代码的作用"
- "检查代码中的潜在问题"

## 高级配置

### 自定义端口

```powershell
happy --port 3000
```

### 指定工作目录

```powershell
happy --path /path/to/project
```

### 环境变量

```powershell
set HAPPY_LOG_LEVEL=debug
happy
```

## 常见问题

**Q: 扫码后无响应**
- 检查网络连接
- 确认手机和电脑在同一网络

**Q: 会话列表为空**
- 确认 Claude Code 正在运行
- 重启 Happy CLI

**Q: 推送通知不显示**
- 检查手机通知权限
- 确认 Happy 后台运行权限

## 下一步

- [Skills 开发指南](./skills-guide.md)
- [最佳实践](./best-practices.md)
