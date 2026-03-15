# UI UX Pro Max Skill 安装指南

> 为 Claude Code 添加专业的设计智能能力

## 项目简介

**UI UX Pro Max** 是一个强大的 AI Skill，为 Claude Code 提供专业的设计智能，包括：

- ✨ **67 种 UI 风格** - Glassmorphism、Neumorphism、Minimalism 等
- 🎨 **96 种配色方案** - 行业专用色彩（SaaS、E-commerce、Fintech 等）
- 🔤 **57 种字体搭配** - Google Fonts 集成
- 📊 **25 种图表类型** - 数据可视化建议
- 🎯 **100 条行业规则** - 智能设计系统生成
- 💻 **13 种技术栈** - React、Next.js、Vue、Flutter 等

**官方网站**: https://ui-ux-pro-max-skill.nextlevelbuilder.io/

**GitHub 仓库**: https://github.com/nextlevelbuilder/ui-ux-pro-max-skill

---

## 安装方法

### 方式一：Claude Marketplace（推荐）

在 Claude Code 中直接安装，最简单快捷：

```bash
# 1. 添加到 marketplace
/plugin marketplace add nextlevelbuilder/ui-ux-pro-max-skill

# 2. 安装 skill
/plugin install ui-ux-pro-max@ui-ux-pro-max-skill
```

**优点**:
- ✅ 最简单，两条命令搞定
- ✅ 自动更新
- ✅ 官方支持

### 方式二：CLI 安装（通用）

使用 CLI 工具，支持所有 AI 助手：

#### 1. 安装 CLI 工具

```bash
npm install -g uipro-cli
```

#### 2. 在项目中初始化

```bash
cd /path/to/your/project

# 根据你使用的 AI 助手选择对应命令
uipro init --ai claude       # Claude Code
uipro init --ai cursor       # Cursor
uipro init --ai windsurf     # Windsurf
uipro init --ai antigravity  # Antigravity
uipro init --ai copilot      # GitHub Copilot
uipro init --ai kiro         # Kiro
uipro init --ai codex        # Codex CLI
uipro init --ai qoder        # Qoder
uipro init --ai roocode      # Roo Code
uipro init --ai gemini       # Gemini CLI
uipro init --ai trae         # Trae
uipro init --ai opencode     # OpenCode
uipro init --ai continue     # Continue
uipro init --ai codebuddy    # CodeBuddy
uipro init --ai all          # 所有助手
```

#### 3. 其他 CLI 命令

```bash
uipro versions              # 查看可用版本
uipro update                # 更新到最新版本
uipro init --offline        # 跳过 GitHub 下载，使用捆绑资源
```

---

## 前置要求

### Python 3.x

Python 3.x 是运行搜索脚本所必需的：

```bash
# 检查 Python 是否已安装
python3 --version

# macOS
brew install python3

# Ubuntu/Debian
sudo apt update && sudo apt install python3

# Windows
winget install Python.Python.3.12
```

### Node.js / npm

如果使用 CLI 安装方式，需要 Node.js：

```bash
# 检查 Node.js 是否已安装
node --version
npm --version

# 安装 Node.js
# macOS
brew install node

# Ubuntu/Debian
sudo apt install nodejs npm

# Windows
winget install OpenJS.NodeJS

# 或访问 https://nodejs.org/
```

---

## 使用方法

### 自动激活模式（推荐）

**支持**: Claude Code, Windsurf, Antigravity, Codex CLI, Continue, Gemini CLI, OpenCode, Qoder, CodeBuddy

只需自然聊天，skill 会自动激活：

```
Build a landing page for my SaaS product
Create a dashboard for healthcare analytics
Design a portfolio website with dark mode
```

### 斜杠命令模式

**支持**: Cursor, Kiro, GitHub Copilot, Roo Code

使用斜杠命令激活：

```
/ui-ux-pro-max Build a landing page for my SaaS product
```

**注意**: Trae 用户需要先切换到 SOLO 模式

---

## 高级用法

### 直接访问设计系统生成器

```bash
# 生成 ASCII 格式的设计系统
python3 .claude/skills/ui-ux-pro-max/scripts/search.py "beauty spa wellness" --design-system -p "Serenity Spa"

# 生成 Markdown 格式
python3 .claude/skills/ui-ux-pro-max/scripts/search.py "fintech banking" --design-system -f markdown

# 特定领域搜索
python3 .claude/skills/ui-ux-pro-max/scripts/search.py "glassmorphism" --domain style
python3 .claude/skills/ui-ux-pro-max/scripts/search.py "elegant serif" --domain typography

# 技术栈特定指南
python3 .claude/skills/ui-ux-pro-max/scripts/search.py "form validation" --stack react
```

### 持久化设计系统

```bash
# 生成并保存到 design-system/MASTER.md
python3 .claude/skills/ui-ux-pro-max/scripts/search.py "SaaS dashboard" --design-system --persist -p "MyApp"

# 创建页面特定的覆盖文件
python3 .claude/skills/ui-ux-pro-max/scripts/search.py "SaaS dashboard" --design-system --persist -p "MyApp" --page "dashboard"
```

---

## 支持的技术栈

| 类别 | 技术栈 |
|------|--------|
| **Web** | HTML + Tailwind (默认) |
| **React** | React, Next.js, shadcn/ui |
| **Vue** | Vue, Nuxt.js, Nuxt UI |
| **其他** | Svelte, Astro |
| **iOS** | SwiftUI |
| **Android** | Jetpack Compose |
| **跨平台** | React Native, Flutter |

---

## 示例提示词

```
Build a landing page for my SaaS product
Create a dashboard for healthcare analytics
Design a portfolio website with dark mode
Make a mobile app UI for e-commerce
Build a fintech banking app with dark theme
```

---

## 工作原理

1. **你的请求** - 描述任何 UI/UX 任务
2. **设计系统生成** - AI 自动生成完整设计系统
3. **智能推荐** - 基于产品类型匹配最佳风格、色彩、字体
4. **代码生成** - 实现正确的颜色、字体、间距和最佳实践
5. **交付前检查** - 验证常见的 UI/UX 反模式

---

## 相关资源

- **GitHub**: https://github.com/nextlevelbuilder/ui-ux-pro-max-skill
- **官网**: https://ui-ux-pro-max-skill.nextlevelbuilder.io/
- **NPM**: https://www.npmjs.com/package/uipro-cli
- **License**: MIT License

---

## 常见问题

### Q: 如何选择安装方式？

**A**:
- 使用 Claude Code → 推荐 Marketplace 方式
- 使用其他 AI 助手 → 使用 CLI 方式
- 想要更多控制 → 使用 CLI 方式

### Q: skill 会自动激活吗？

**A**: 取决于你的 AI 助手：
- Claude Code: ✅ 自动激活
- Cursor/Kiro: ❌ 需要使用 `/ui-ux-pro-max` 命令
- 其他: 查看官方文档

### Q: 如何更新 skill？

**A**:
- Marketplace: 自动更新
- CLI: 运行 `uipro update`

### Q: 支持哪些技术栈？

**A**: 支持 13 种技术栈，包括 React、Next.js、Vue、Flutter、SwiftUI 等

### Q: 如何生成设计系统？

**A**: 在提示中自然描述你的产品，skill 会自动生成设计系统

---

## 更新日志

### v2.0 (最新)

- ✨ 智能设计系统生成器
- ✨ 100 条行业特定规则
- ✨ Master + Overrides 模式
- ✨ 持久化设计系统

---

## 开始使用

选择你喜欢的安装方式，立即开始使用 UI UX Pro Max！

```bash
# 方式一：Claude Marketplace
/plugin marketplace add nextlevelbuilder/ui-ux-pro-max-skill
/plugin install ui-ux-pro-max@ui-ux-pro-max-skill

# 方式二：CLI
npm install -g uipro-cli
uipro init --ai claude
```

**Happy Designing! 🎨**
