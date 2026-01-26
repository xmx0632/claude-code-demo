# 附录

## A. 常见问题 FAQ

### 环境配置

**Q: Node.js 版本要求？**
A: 推荐 Node.js 18.x 或 20.x LTS 版本。

**Q: 如何查看当前配置？**
A: 运行 `claude settings` 查看配置。

**Q: 支持代理吗？**
A: 可以通过设置 `HTTP_PROXY` 环境变量配置代理。

### glm4.7 模型

**Q: 如何切换模型？**
A: 在 settings.json 中修改模型配置或使用环境变量。

**Q: API Key 泄露了怎么办？**
A: 立即在智谱 AI 控制台删除并重新生成。

### Happy 远程

**Q: 支持哪些平台？**
A: iOS 和 Android 均支持。

**Q: 需要公网 IP 吗？**
A: 不需要，Happy 通过中继服务器通信。

### Skills 开发

**Q: Skill 不生效？**
A: 检查 SKILL.md 文件位置和 YAML 格式。

**Q: 如何调试 Skill？**
A: 使用 `claude --debug` 查看详细日志。

**Q: 支持多文件 Skill 吗？**
A: 支持，将相关文件放在同一目录即可。

## B. 参考资源

### 官方文档

- [Claude Code 文档](https://code.claude.com/docs)
- [Happy 官网](https://happy.engineering/)
- [智谱 AI 文档](https://docs.bigmodel.cn/)
- [Ruoyi 官网](https://ruoyi.vip/)

### 相关工具

- [Node.js](https://nodejs.org/)
- [Spring Boot](https://spring.io/projects/spring-boot)
- [MyBatis-Plus](https://baomidou.com/)
- [IntelliJ IDEA](https://www.jetbrains.com/idea/)

### 社区资源

- [Happy GitHub](https://github.com/happy-coder/happy)
- [Claude Code Examples](https://github.com/anthropics/courses)

## C. 速查表

### 常用命令

| 命令 | 说明 |
|------|------|
| `claude` | 启动 Claude Code |
| `claude --help` | 查看帮助 |
| `claude settings` | 查看配置 |
| `happy` | 启动 Happy CLI |
| `/skill-name` | 调用 Skill |

### 配置文件

| 文件 | 位置 | 说明 |
|------|------|------|
| `settings.json` | `%USERPROFILE%\.claude\` | Claude Code 配置 |
| `SKILL.md` | `.claude/skills/skill-name/` | Skill 定义 |
| `.gitignore` | 项目根目录 | 忽略缓存文件 |

### 环境变量

| 变量 | 说明 |
|------|------|
| `ANTHROPIC_AUTH_TOKEN` | API Key |
| `ANTHROPIC_BASE_URL` | API 基础 URL |
| `API_TIMEOUT_MS` | 超时时间（毫秒） |

### 快捷键（手机端）

| 操作 | 说明 |
|------|------|
| 长按输入框 | 语音输入 |
| 下拉刷新 | 更新会话列表 |
| 左滑会话 | 结束会话 |
| 点击通知 | 查看详细输出 |
