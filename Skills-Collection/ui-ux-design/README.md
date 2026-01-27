# UI/UX Design Skill

> 为 SDLC Framework 提供专业的 UI/UX 设计系统生成能力

## 概述

本技能为 SDLC Framework 的**产品设计阶段**（02-product-design）提供专业的设计系统生成能力，基于 UI UX Pro Max 设计智能系统，为产品提供行业特定的 UI/UX 设计建议。

## 核心功能

### 1. 设计风格推荐

- **67 种 UI 风格**：Minimalism、Glassmorphism、Neumorphism、Brutalism 等
- **行业适配**：根据产品类型自动推荐最合适的设计风格
- **性能考虑**：提供性能等级和无障碍等级信息

### 2. 色彩方案生成

- **96 种行业专用调色板**：覆盖 SaaS、E-commerce、Healthcare、Fintech 等行业
- **完整色彩系统**：主色、辅助色、CTA、背景、文本、边框
- **对比度检查**：确保 WCAG AA/AAA 标准

### 3. 字体搭配建议

- **57 种字体组合**：标题字体 + 正文字体的完美搭配
- **Google Fonts 集成**：提供完整的字体引入代码
- **情感基调匹配**：根据品牌定位推荐字体

### 4. 落地页模式

- **14 种转化优化的页面结构**：Hero-Centric、Conversion-Optimized 等
- **CTA 位置策略**：优化转化率的行为召唤设计
- **社会证明元素**：增强信任感的设计元素

### 5. UX 最佳实践

- **交互效果**：动画时长、过渡效果、Hover 状态
- **响应式设计**：断点设置、布局适配
- **无障碍设计**：对比度、键盘导航、屏幕阅读器支持

## 安装

本技能已集成到 SDLC Framework 的 `Skills-Collection/` 目录中。

### 方式一：使用 Claude Marketplace（推荐）

在 Claude Code 中使用以下命令直接安装：

```bash
/plugin marketplace add nextlevelbuilder/ui-ux-pro-max-skill
/plugin install ui-ux-pro-max@ui-ux-pro-max-skill
```

### 方式二：使用 CLI（适用于所有 AI 助手）

#### 1. 全局安装 CLI 工具

```bash
npm install -g uipro-cli
```

#### 2. 在项目中初始化

进入你的项目目录并运行：

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

### 前置要求

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

### 目录结构

```
Skills-Collection/ui-ux-design/
├── skill.md                           # 技能定义文件
├── README.md                          # 本文件
├── data/                              # 设计数据库
│   ├── ui-styles.csv                 # UI 风格数据（67种）
│   ├── color-palettes.csv            # 色彩方案数据（96种）
│   └── typography-pairs.csv          # 字体搭配数据（57种）
└── templates/                         # 设计系统模板
    └── design-system-template.md     # 设计系统文档模板
```

## 使用方法

### 基础用法

在产品设计阶段，使用以下命令生成设计系统：

```bash
/ui-ux-design "为我的 SaaS 仪表板设计界面"
```

### 指定产品类型

```bash
/ui-ux-design "电商产品详情页" --type=e-commerce
```

### 指定风格偏好

```bash
/ui-ux-design "健康管理 App" --style=soft-ui
```

### 完整参数

```bash
/ui-ux-design "企业级数据分析平台" --type=saas --style=minimalism --theme=dark
```

### 持久化设计系统

```bash
# 生成并保存设计系统
/ui-ux-design "我的 SaaS 产品" --persist --name=MyApp

# 创建页面特定的覆盖文件
/ui-ux-design "仪表板页面" --persist --name=MyApp --page=dashboard
```

## 参数说明

| 参数 | 说明 | 可选值 |
|------|------|--------|
| --type | 产品类型 | saas, e-commerce, healthcare, fintech, education, gaming 等 |
| --style | 设计风格 | minimalism, glassmorphism, neumorphism, brutalism 等 |
| --theme | 主题模式 | light, dark, auto |
| --stack | 技术栈 | react, vue, html-tailwind, flutter 等 |
| --persist | 持久化到文件 | 保存到 design-system/ 目录 |
| --name | 产品名称 | 用于持久化文件命名 |
| --page | 页面名称 | 用于创建页面特定覆盖 |

## 输出内容

### 设计系统文档

生成完整的 `design-system.md` 文档，包含：

1. **设计概述** - 产品信息、设计原则
2. **设计风格** - 风格定义、视觉特征
3. **色彩系统** - 完整的色彩规范
4. **字体系统** - 字体家族、字号、字重
5. **间距系统** - 基础间距、布局间距
6. **组件规范** - 按钮、输入框、卡片、导航
7. **交互效果** - 动画、过渡、Hover 状态
8. **响应式设计** - 断点设置、适配规则
9. **无障碍设计** - 对比度、键盘导航
10. **反模式** - 应避免的设计元素
11. **技术栈配置** - Tailwind、CSS Variables

### 代码示例

提供以下代码示例：

- **CSS Variables**: 完整的设计令牌
- **Tailwind 配置**: 集成 Tailwind CSS
- **组件代码**: React/Vue 组件示例
- **Google Fonts**: 字体引入代码

## 行业支持

本技能支持以下行业的设计系统生成：

### SaaS 产品
- 风格：Minimalism, Glassmorphism, Soft UI Evolution
- 色彩：专业蓝色系 + 可信辅助色
- 字体：现代 Sans-serif (Inter, Space Grotesk)

### E-commerce
- 风格：Vibrant & Block-based, 3D & Hyperrealism
- 色彩：活力橙/红 + 高对比 CTA
- 字体：友好圆润 (Poppins, Nunito)

### Healthcare
- 风格：Neumorphism, Soft UI Evolution, Organic Biophilic
- 色彩：平静绿/蓝 + 暖色辅助
- 字体：易读 Serif/Mono 组合 (Cormorant Garamond / Montserrat)

### Fintech
- 风格：Minimalism, Glassmorphism, Swiss Style
- 色彩：可信蓝/绿 + 专业辅助
- 字体：专业 Sans-serif (Inter, Roboto)

### 更多行业

- Education（教育）
- Gaming（游戏）
- Wellness（健康）
- Food & Beverage（餐饮）
- Travel（旅行）
- Real Estate（房地产）
- Media & Entertainment（媒体娱乐）
- Nonprofit（非营利）
- Legal（法律）
- Consulting（咨询）
- Technology（科技）
- Banking（银行）
- Insurance（保险）
- Crypto（加密货币）
- Logistics（物流）
- Automotive（汽车）
- Sports（体育）
- Beauty & Cosmetics（美妆）
- Fashion（时尚）
- Music & Audio（音乐音频）
- Art & Design（艺术设计）
- Photography（摄影）
- Pet Services（宠物服务）
- Home & Garden（家居园艺）

## 集成到 SDLC Framework

### 在产品设计阶段使用

本技能设计用于在 SDLC Framework 的**产品设计阶段**（02-product-design）使用：

```bash
# 步骤1: 需求分析
/requirements-analysis "创建用户认证系统"

# 步骤2: 生成设计系统
/ui-ux-design "用户认证界面" --type=saas

# 步骤3: 创建线框图
/product-design "用户认证界面"

# 步骤4: 架构设计
/architecture-design
```

### 与其他技能配合

- `/product-design` - 使用设计系统创建线框图和用户流程
- `/mermaid-diagram` - 创建流程图和架构图
- `/code-review` - 审查代码实现是否符合设计系统

## 设计数据库

### UI 风格数据 (ui-styles.csv)

包含 67 种 UI 风格的详细信息：
- 风格 ID、名称、类别
- 描述、适用场景
- 性能等级、无障碍等级
- 关键词标签

### 色彩方案数据 (color-palettes.csv)

包含 96 种行业专用配色方案：
- 调色板 ID、名称、产品类型
- 主色、辅助色、CTA、背景、文本、边框
- 描述、情感基调

### 字体搭配数据 (typography-pairs.csv)

包含 57 种字体组合：
- 字体对 ID、标题字体、正文字体
- 情感基调、适用场景
- Google Fonts URL
- 描述

## 设计系统持久化

### Master + Overrides 模式

设计系统支持层级式持久化：

```
design-system/
├── MASTER.md           # 全局单一数据源
└── pages/
    ├── dashboard.md    # 页面特定覆盖
    ├── profile.md      # 页面特定覆盖
    └── settings.md     # 页面特定覆盖
```

### 层级检索规则

1. 构建特定页面时，首先检查页面文件
2. 如果页面文件存在，其规则**覆盖** Master 文件
3. 如果不存在，仅使用 Master 文件

### 示例

```bash
# 生成全局设计系统
/ui-ux-design "MyApp" --persist --name=MyApp

# 生成仪表板页面覆盖
/ui-ux-design "仪表板" --persist --name=MyApp --page=dashboard

# 生成设置页面覆盖
/ui-ux-design "设置" --persist --name=MyApp --page=settings
```

## 质量检查

设计系统生成完成后，自动进行质量检查：

- [ ] 设计风格与产品类型匹配
- [ ] 色彩方案符合行业特征
- [ ] 字体搭配具有良好的可读性
- [ ] 落地页模式支持转化目标
- [ ] 交互效果符合性能要求
- [ ] 无障碍设计符合 WCAG AA 标准
- [ ] 响应式设计覆盖所有断点
- [ ] 反模式已明确标识

## 技术栈支持

本技能支持以下技术栈的设计系统生成：

### Web (HTML)
- HTML + Tailwind (默认)

### React 生态
- React
- Next.js
- shadcn/ui

### Vue 生态
- Vue
- Nuxt.js
- Nuxt UI

### 其他 Web
- Svelte
- Astro

### iOS
- SwiftUI

### Android
- Jetpack Compose

### 跨平台
- React Native
- Flutter

## 最佳实践

### 1. 明确产品类型

提供准确的产品类型信息，以获得最匹配的设计建议：

```bash
/ui-ux-design "我的产品" --type=saas
```

### 2. 指定品牌定位

描述品牌的情感定位，帮助选择合适的风格和色彩：

```bash
/ui-ux-design "友好的教育产品" --style=playful
```

### 3. 考虑技术限制

根据技术栈选择可实现的设计方案：

```bash
/ui-ux-design "我的产品" --stack=html-tailwind
```

### 4. 持久化设计系统

对于长期项目，建议持久化设计系统：

```bash
/ui-ux-design "MyApp" --persist --name=MyApp
```

### 5. 创建页面覆盖

为不同页面创建特定的设计覆盖：

```bash
/ui-ux-design "仪表板" --persist --name=MyApp --page=dashboard
```

## 常见问题

### Q: 如何选择合适的设计风格？

A: 本技能会根据产品类型自动推荐最合适的风格。你也可以使用 `--style` 参数指定偏好。

### Q: 色彩方案可以自定义吗？

A: 本技能提供行业标准的色彩方案。生成后，你可以在设计系统文档中修改颜色值。

### Q: 支持深色模式吗？

A: 是的，使用 `--theme=dark` 参数可以生成深色模式的设计系统。

### Q: 如何确保无障碍设计？

A: 本技能生成的所有设计系统都符合 WCAG AA 标准，包含对比度检查和无障碍指南。

### Q: 可以用于移动应用吗？

A: 可以。使用 `--stack=react-native` 或 `--stack=flutter` 参数生成移动应用的设计系统。

## 示例

### 示例 1: SaaS 产品

```bash
/ui-ux-design "B2B SaaS 数据分析平台"
```

**输出**:
- 风格：Minimalism & Swiss Style
- 色彩：专业蓝 + 成功绿
- 字体：Inter / Space Grotesk
- 落地页：Feature-Rich Showcase

### 示例 2: 健康美容

```bash
/ui-ux-design "高端美容 SPA 预订系统"
```

**输出**:
- 风格：Soft UI Evolution
- 色彩：Soft Pink + Sage Green + Gold
- 字体：Cormorant Garamond / Montserrat
- 落地页：Hero-Centric + Social Proof

## 更新日志

### v1.0.0 (2024-01-27)

- 初始版本
- 支持 67 种 UI 风格
- 支持 96 种色彩方案
- 支持 57 种字体搭配
- 支持设计系统持久化
- 支持多个技术栈

## 贡献

欢迎贡献！你可以：

1. 添加新的 UI 风格
2. 添加新的色彩方案
3. 添加新的字体搭配
4. 改进设计模板
5. 完善文档

## 许可证

MIT License

## 相关资源

- **GitHub 仓库**: [https://github.com/nextlevelbuilder/ui-ux-pro-max-skill](https://github.com/nextlevelbuilder/ui-ux-pro-max-skill)
- **官方文档**: [https://ui-ux-pro-max-skill.nextlevelbuilder.io/](https://ui-ux-pro-max-skill.nextlevelbuilder.io/)
- **NPM 包**: [uipro-cli](https://www.npmjs.com/package/uipro-cli)

## 致谢

本技能基于 [UI UX Pro Max](https://github.com/nextlevelbuilder/ui-ux-pro-max-skill) 项目，感谢原作者 [nextlevelbuilder](https://github.com/nextlevelbuilder) 的贡献。

---

**开始使用**: `/ui-ux-design "你的产品描述"`
