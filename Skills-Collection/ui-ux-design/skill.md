---
name: ui-ux-design
description: 生成专业的 UI/UX 设计系统，包括设计风格、色彩方案、字体搭配和最佳实践建议。在产品设计阶段使用，为产品界面提供专业的设计指导。
allowed-tools: ["Read", "Write", "Edit", "Glob", "Grep", "Bash"]
---

# UI/UX 设计系统生成器

为产品 **$ARGUMENTS** 生成专业的设计系统。

## 技能概述

本技能基于 UI UX Pro Max 设计智能系统，为你的产品提供专业的设计建议，包括：
- **设计风格推荐** - 67 种 UI 风格（Glassmorphism、Neumorphism、Minimalism 等）
- **色彩方案** - 96 种行业专用调色板（SaaS、E-commerce、Healthcare、Fintech 等）
- **字体搭配** - 57 种字体组合及 Google Fonts 集成
- **落地页模式** - 14 种转化优化的页面结构
- **UX 指南** - 动画、无障碍、性能最佳实践

## 使用场景

- **新产品设计** - 为新产品建立完整的设计系统
- **界面重构** - 改进现有界面的视觉设计
- **品牌升级** - 更新产品的视觉风格和色彩体系
- **落地页优化** - 提升落地页的转化率

## 工作流程

### 步骤 1: 分析产品需求

**输入信息**:
- 产品类型（SaaS、E-commerce、Healthcare、Fintech 等）
- 目标用户群体
- 品牌定位（专业、友好、创新、可靠等）
- 竞品参考

**分析维度**:
1. 行业特征分析
2. 用户心理画像
3. 品牌情感定位
4. 使用场景梳理

### 步骤 2: 生成设计系统

根据产品需求，智能生成包含以下内容的设计系统：

#### 2.1 设计风格推荐

**推荐原则**:
- 匹配行业特征
- 符合品牌调性
- 考虑技术可行性
- 优先选择无障碍友好的风格

**输出示例**:
```
推荐风格: Soft UI Evolution
- 关键词: 柔和阴影、微妙深度、平静感、高级感、有机形状
- 适用场景: 健康美容、生活方式品牌、高端服务
- 性能表现: 优秀 | 无障碍: WCAG AA
```

#### 2.2 色彩方案

**色彩系统包含**:
- Primary Color - 主色调
- Secondary Color - 辅助色
- CTA Color - 行为召唤色
- Background Color - 背景色
- Text Color - 文本色
- Border Color - 边框色

**输出示例**:
```
色彩方案: Serenity Spa
- Primary:    #E8B4B8 (Soft Pink)
- Secondary:  #A8D5BA (Sage Green)
- CTA:        #D4AF37 (Gold)
- Background: #FFF5F5 (Warm White)
- Text:       #2D3436 (Charcoal)

说明: 平静色调搭配金色点缀，营造奢华感
```

#### 2.3 字体搭配

**字体组合包含**:
- 标题字体
- 正文字体
- 字号规范
- 行高规范
- Google Fonts 引用

**输出示例**:
```
字体搭配: Cormorant Garamond / Montserrat
- 标题字体: Cormorant Garamond (优雅、精致)
- 正文字体: Montserrat (现代、清晰)
- 情感基调: 优雅、平静、高级
- 适用场景: 奢侈品牌、健康美容、编辑设计

Google Fonts:
https://fonts.google.com/share?selection-family=Cormorant+Garamond:wght@400;600;700&family=Montserrat:wght@400;500;600
```

#### 2.4 落地页模式

**页面结构包含**:
- Hero 区域设计
- 特性展示方式
- 社会证明元素
- CTA 位置策略
- 内容组织方式

**输出示例**:
```
落地页模式: Hero-Centric + Social Proof
- 转化策略: 情感驱动 + 信任元素
- CTA 位置: 首屏上方 + 推荐后重复
- 页面区块:
  1. Hero - 主视觉区域
  2. Features - 特性展示
  3. Testimonials - 用户证言
  4. Booking - 预订 CTA
  5. Contact - 联系方式
```

#### 2.5 关键效果

**交互效果包含**:
- 阴影样式
- 过渡动画时长
- Hover 状态
- 加载状态

**输出示例**:
```
关键效果:
- 柔和阴影 (box-shadow: 0 4px 20px rgba(0,0,0,0.08))
- 平滑过渡 (transition: all 200-300ms ease)
- 温和 Hover 状态
- 优雅的加载动画
```

#### 2.6 反模式（需避免）

**列出应避免的设计元素**:
- 与行业不符的风格
- 无障碍不友好的设计
- 性能问题的元素
- 过时的设计趋势

**输出示例**:
```
避免（反模式）:
- 鲜艳的霓虹色
- 刺眼的动画效果
- 深色模式（不适合健康美容）
- AI 紫色/粉色渐变（过于常见）
- 过度复杂的交互
```

### 步骤 3: 交付前检查清单

**设计系统质量检查**:
- [ ] 不使用 emoji 作为图标（使用 SVG: Heroicons/Lucide）
- [ ] 所有可点击元素设置 cursor-pointer
- [ ] Hover 状态使用平滑过渡（150-300ms）
- [ ] 浅色模式下文本对比度至少 4.5:1
- [ ] 键盘导航的 Focus 状态可见
- [ ] 遵循 prefers-reduced-motion
- [ ] 响应式设计: 375px, 768px, 1024px, 1440px

### 步骤 4: 输出设计系统文档

**输出位置**: `docs/design/design-system.md`

**文档结构**:
```markdown
# [产品名称] 设计系统

## 1. 设计概述
- 产品类型
- 目标用户
- 品牌定位

## 2. 设计风格
- 风格名称
- 风格描述
- 适用场景
- 性能考虑

## 3. 色彩系统
- 色彩方案
- 色彩使用指南
- 无障碍考虑

## 4. 字体系统
- 字体搭配
- 字号规范
- 行高规范
- 引用代码

## 5. 组件规范
- 按钮样式
- 表单元素
- 卡片设计
- 导航组件

## 6. 间距系统
- 基础间距单位
- 布局间距
- 组件内间距

## 7. 交互效果
- 动画时长
- 过渡效果
- Hover 状态
- 加载状态

## 8. 响应式设计
- 断点设置
- 布局适配
- 字体缩放

## 9. 无障碍设计
- 对比度要求
- 键盘导航
- 屏幕阅读器支持

## 10. 反模式
- 应避免的设计
- 原因说明
- 替代方案

## 11. 代码示例
- CSS 变量
- Tailwind 配置
- 组件示例
```

## 行业专用设计规则

### SaaS 产品

**风格优先**: Minimalism, Glassmorphism, Soft UI Evolution

**色彩基调**: 专业蓝色系 + 可信辅助色

**字体选择**: 现代 Sans-serif (Inter, Space Grotesk)

**关键元素**:
- 清晰的数据可视化
- 简洁的导航
- 高效的表单设计

**避免**: 复杂动画、过于鲜艳的颜色

### E-commerce

**风格优先**: Vibrant & Block-based, 3D & Hyperrealism

**色彩基调**: 活力橙/红 + 高对比 CTA

**字体选择**: 友好圆润 (Poppins, Nunito)

**关键元素**:
- 突出的产品图片
- 明确的 CTA 按钮
- 社会证明元素

**避免**: 过于复杂的导航、隐蔽的购买按钮

### Healthcare

**风格优先**: Neumorphism, Soft UI Evolution, Organic Biophilic

**色彩基调**: 平静绿/蓝 + 暖色辅助

**字体选择**: 易读 Serif/Mono 组合 (Cormorant Garamond / Montserrat)

**关键元素**:
- 清晰的层级
- 温暖的色调
- 易读的排版

**避免**: 刺眼的颜色、快速动画、深色主题

### Fintech

**风格优先**: Minimalism, Glassmorphism, Swiss Style

**色彩基调**: 可信蓝/绿 + 专业辅助

**字体选择**: 专业 Sans-serif (Inter, Roboto)

**关键元素**:
- 数据安全感的视觉
- 清晰的数据展示
- 简洁的操作流程

**避免**: 娱乐化元素、过于鲜艳的颜色、AI 紫粉渐变

## 使用方法

### 基础用法

```
/ui-ux-design "为我的 SaaS 仪表板设计界面"
```

### 指定产品类型

```
/ui-ux-design "电商产品详情页，时尚品牌" --type=e-commerce
```

### 指定风格偏好

```
/ui-ux-design "健康管理 App，想要平静优雅的风格" --style=soft-ui
```

### 完整参数

```
/ui-ux-design "企业级数据分析平台" --type=saas --style=minimalism --theme=dark
```

## 参数说明

| 参数 | 说明 | 可选值 |
|------|------|--------|
| --type | 产品类型 | saas, e-commerce, healthcare, fintech, education, gaming 等 |
| --style | 设计风格 | minimalism, glassmorphism, neumorphism, brutalism 等 |
| --theme | 主题模式 | light, dark, auto |
| --stack | 技术栈 | react, vue, html-tailwind, flutter 等 |

## 技术栈集成

### HTML + Tailwind (默认)

生成 Tailwind 配置和 CSS 变量。

```javascript
// tailwind.config.js
module.exports = {
  theme: {
    extend: {
      colors: {
        primary: '#E8B4B8',
        secondary: '#A8D5BA',
        cta: '#D4AF37',
      },
      fontFamily: {
        heading: ['Cormorant Garamond', 'serif'],
        body: ['Montserrat', 'sans-serif'],
      },
    }
  }
}
```

### React

生成 React 组件示例和 Hooks。

```jsx
// Button component with design system
const Button = ({ variant, children }) => {
  return (
    <button className={getButtonStyles(variant)}>
      {children}
    </button>
  );
};
```

### Vue

生成 Vue 3 组件示例和 Composition API 用法。

### Flutter

生成 Flutter Widget 和主题配置。

## 设计系统持久化

**Master + Overrides 模式**:

设计系统可以持久化到文件中，支持跨会话的层级检索：

```bash
# 生成并保存到设计系统文件
/ui-ux-design "我的 SaaS 产品" --persist --name=MyApp

# 创建页面特定的覆盖文件
/ui-ux-design "仪表板页面" --persist --name=MyApp --page=dashboard
```

这会创建 `design-system/` 文件夹结构：

```
design-system/
├── MASTER.md           # 全局单一数据源（色彩、字体、间距、组件）
└── pages/
    └── dashboard.md    # 页面特定覆盖（仅包含与 Master 的差异）
```

**层级检索工作原理**:

1. 构建特定页面（如 "Checkout"）时，首先检查 `design-system/pages/checkout.md`
2. 如果页面文件存在，其规则**覆盖** Master 文件
3. 如果不存在，仅使用 `design-system/MASTER.md`

## 质量检查

完成设计系统生成后，确保：

- [ ] 设计风格与产品类型匹配
- [ ] 色彩方案符合行业特征
- [ ] 字体搭配具有良好的可读性
- [ ] 落地页模式支持转化目标
- [ ] 交互效果符合性能要求
- [ ] 无障碍设计符合 WCAG AA 标准
- [ ] 响应式设计覆盖所有断点
- [ ] 反模式已明确标识

## 下一步

设计系统生成完成后：

1. **创建线框图** - 使用设计系统创建产品线框图
2. **开发原型** - 基于设计系统开发可交互原型
3. **用户测试** - 验证设计系统的有效性
4. **开发实现** - 将设计系统交付给开发团队

```bash
# 进入产品设计阶段
/product-design "用户管理功能界面"
```

## 示例

### 示例 1: SaaS 产品

**输入**:
```
/ui-ux-design "B2B SaaS 数据分析平台"
```

**输出**:
```
推荐设计系统:

风格: Minimalism & Swiss Style
- 简洁、专业、数据导向
- 适合企业应用和仪表板

色彩:
- Primary: #1890ff (专业蓝)
- Secondary: #52c41a (成功绿)
- CTA: #1890ff
- Background: #F0F2F5
- Text: #262626

字体: Inter / Space Grotesk
- 现代、清晰、易读
- 适合数据密集界面

落地页: Feature-Rich Showcase
- 突出功能特性
- 数据可视化
- 清晰的层级
```

### 示例 2: 健康美容

**输入**:
```
/ui-ux-design "高端美容 SPA 预订系统"
```

**输出**:
```
推荐设计系统:

风格: Soft UI Evolution
- 柔和、平静、高级感
- 适合健康美容品牌

色彩:
- Primary: #E8B4B8 (Soft Pink)
- Secondary: #A8D5BA (Sage Green)
- CTA: #D4AF37 (Gold)
- Background: #FFF5F5 (Warm White)
- Text: #2D3436 (Charcoal)

字体: Cormorant Garamond / Montserrat
- 优雅、精致、高级
- 适合奢侈品牌

落地页: Hero-Centric + Social Proof
- 情感驱动
- 用户证言
- 预订 CTA
```

## 设计资源

本技能集成了以下设计资源：

- **67 种 UI 风格** - 覆盖主流设计趋势
- **96 种色彩方案** - 行业专用配色
- **57 种字体搭配** - Google Fonts 集成
- **14 种落地页模式** - 转化优化
- **99 条 UX 指南** - 最佳实践

## 注意事项

1. **行业适配**: 设计系统会根据产品类型自动调整
2. **技术可行性**: 考虑技术栈的样式实现能力
3. **性能优先**: 避免过度设计影响性能
4. **无障碍设计**: 确保 WCAG AA 标准
5. **品牌一致性**: 设计系统应与品牌保持一致

## 相关技能

- `/product-design` - 创建产品线框图和用户流程
- `/mermaid-diagram` - 创建流程图和架构图
- `/code-review` - 审查代码实现是否符合设计系统
