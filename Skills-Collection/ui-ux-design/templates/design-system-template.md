# [产品名称] 设计系统

> **版本**: 1.0
> **最后更新**: [日期]
> **设计团队**: [团队信息]

---

## 1. 设计概述

### 1.1 产品信息

| 项目 | 描述 |
|------|------|
| 产品名称 | [产品名称] |
| 产品类型 | [SaaS / E-commerce / Healthcare / Fintech 等] |
| 目标用户 | [主要用户群体] |
| 品牌定位 | [专业 / 友好 / 创新 / 可靠 等] |
| 设计风格 | [风格名称] |

### 1.2 设计原则

1. **[原则1]** - [描述]
2. **[原则2]** - [描述]
3. **[原则3]** - [描述]

---

## 2. 设计风格

### 2.1 风格定义

**风格名称**: [风格名称]

**风格描述**:
- [风格特征1]
- [风格特征2]
- [风格特征3]

**适用场景**: [适用场景描述]

**性能表现**: [性能等级 - Excellent/Good/Fair]

**无障碍等级**: [WCAG AA/AAA]

### 2.2 关键词

`[关键词1]`, `[关键词2]`, `[关键词3]`, `[关键词4]`

### 2.3 视觉特征

- **布局**: [布局特征]
- **形状**: [形状特征]
- **效果**: [效果特征]
- **动画**: [动画特征]

---

## 3. 色彩系统

### 3.1 主色调

```css
--color-primary: [主色值]; /* [主色名称] */
--color-primary-light: [浅色值]; /* 浅色变体 */
--color-primary-dark: [深色值]; /* 深色变体 */
```

**使用场景**: [主色使用场景]

### 3.2 辅助色

```css
--color-secondary: [辅助色值]; /* [辅助色名称] */
--color-secondary-light: [浅色值];
--color-secondary-dark: [深色值];
```

**使用场景**: [辅助色使用场景]

### 3.3 行为召唤色 (CTA)

```css
--color-cta: [CTA色值]; /* [CTA色名称] */
--color-cta-hover: [悬停色值];
--color-cta-active: [激活色值];
```

**使用场景**: [CTA使用场景]

### 3.4 功能色

```css
--color-success: [成功色值]; /* 成功/确认 */
--color-warning: [警告色值]; /* 警告/注意 */
--color-error: [错误色值]; /* 错误/危险 */
--color-info: [信息色值]; /* 信息/提示 */
```

### 3.5 中性色

```css
--color-bg-primary: [主背景色]; /* 主背景 */
--color-bg-secondary: [次背景色]; /* 次背景 */
--color-text-primary: [主文本色]; /* 主文本 */
--color-text-secondary: [次文本色]; /* 次文本 */
--color-text-tertiary: [三级文本色]; /* 三级文本 */
--color-border: [边框色]; /* 边框 */
--color-divider: [分割线色]; /* 分割线 */
```

### 3.6 色彩对比度

| 前景色 | 背景色 | 对比度 | WCAG等级 |
|--------|--------|--------|----------|
| 主文本 | 主背景 | [对比度值] | [AA/AAA] |
| CTA | 背景 | [对比度值] | [AA/AAA] |

---

## 4. 字体系统

### 4.1 字体家族

```css
/* 标题字体 */
--font-heading: '[标题字体]', [备用字体1], [备用字体2], serif;

/* 正文字体 */
--font-body: '[正文字体]', [备用字体1], [备用字体2], sans-serif;

/* 代码字体 */
--font-code: '[代码字体]', [备用字体], monospace;
```

### 4.2 字号规范

```css
/* 标题字号 */
--font-size-h1: [字号]; /* [行高] */
--font-size-h2: [字号]; /* [行高] */
--font-size-h3: [字号]; /* [行高] */
--font-size-h4: [字号]; /* [行高] */

/* 正文字号 */
--font-size-body-large: [字号]; /* [行高] */
--font-size-body: [字号]; /* [行高] */
--font-size-body-small: [字号]; /* [行高] */

/* 辅助字号 */
--font-size-caption: [字号]; /* [行高] */
--font-size-overline: [字号]; /* [行高] */
```

### 4.3 字重规范

```css
--font-weight-light: [字重值]; /* 细体 */
--font-weight-regular: [字重值]; /* 常规 */
--font-weight-medium: [字重值]; /* 中等 */
--font-weight-semibold: [字重值]; /* 半粗 */
--font-weight-bold: [字重值]; /* 粗体 */
```

### 4.4 Google Fonts 引入

```html
<link rel="preconnect" href="https://fonts.googleapis.com">
<link rel="preconnect" href="https://fonts.gstatic.com" crossorigin>
<link href="[Google Fonts URL]" rel="stylesheet">
```

**URL**: `[Google Fonts URL]`

### 4.5 字体情感基调

**描述**: [字体情感描述]

**适用场景**: [字体适用场景]

---

## 5. 间距系统

### 5.1 基础间距单位

```css
--space-xs: [间距值];   /* 极小间距 */
--space-sm: [间距值];   /* 小间距 */
--space-md: [间距值];   /* 中等间距 */
--space-lg: [间距值];   /* 大间距 */
--space-xl: [间距值];   /* 超大间距 */
--space-2xl: [间距值];  /* 特大间距 */
```

### 5.2 布局间距

```css
--layout-padding-sm: [间距值];   /* 小屏内边距 */
--layout-padding-md: [间距值];   /* 中屏内边距 */
--layout-padding-lg: [间距值];   /* 大屏内边距 */
--layout-padding-xl: [间距值];   /* 超大屏内边距 */
```

### 5.3 组件间距

```css
--component-gap-sm: [间距值];   /* 小间距 */
--component-gap-md: [间距值];   /* 中等间距 */
--component-gap-lg: [间距值];   /* 大间距 */
```

---

## 6. 组件规范

### 6.1 按钮 (Button)

#### 主要按钮 (Primary Button)

```css
.btn-primary {
  background-color: var(--color-cta);
  color: var(--color-bg-primary);
  border: none;
  border-radius: [圆角值];
  padding: [内边距];
  font-size: var(--font-size-body);
  font-weight: var(--font-weight-semibold);
  transition: all [过渡时长] ease;
}

.btn-primary:hover {
  background-color: var(--color-cta-hover);
  transform: [变换效果];
}

.btn-primary:active {
  background-color: var(--color-cta-active);
}
```

**使用场景**: 主要操作、表单提交

#### 次要按钮 (Secondary Button)

```css
.btn-secondary {
  background-color: transparent;
  color: var(--color-primary);
  border: [边框宽度] solid var(--color-primary);
  border-radius: [圆角值];
  padding: [内边距];
  /* ... */
}
```

**使用场景**: 次要操作、取消操作

### 6.2 输入框 (Input)

```css
.input {
  background-color: var(--color-bg-primary);
  border: [边框宽度] solid var(--color-border);
  border-radius: [圆角值];
  padding: [内边距];
  font-size: var(--font-size-body);
  color: var(--color-text-primary);
  transition: border-color [过渡时长] ease;
}

.input:focus {
  outline: none;
  border-color: var(--color-primary);
  box-shadow: 0 0 0 [焦点环宽度] var(--color-primary-light);
}

.input::placeholder {
  color: var(--color-text-tertiary);
}
```

### 6.3 卡片 (Card)

```css
.card {
  background-color: var(--color-bg-primary);
  border: [边框宽度] solid var(--color-border);
  border-radius: [圆角值];
  padding: [内边距];
  box-shadow: [阴影值];
}

.card:hover {
  box-shadow: [悬停阴影值];
  transform: [变换效果];
}
```

### 6.4 导航 (Navigation)

```css
.nav {
  background-color: var(--color-bg-primary);
  border-bottom: [边框宽度] solid var(--color-border);
  padding: [内边距];
}

.nav-item {
  color: var(--color-text-secondary);
  padding: [内边距];
  transition: color [过渡时长] ease;
}

.nav-item:hover,
.nav-item.active {
  color: var(--color-primary);
}
```

---

## 7. 阴影系统

```css
--shadow-xs: [阴影值];   /* 极小阴影 */
--shadow-sm: [阴影值];   /* 小阴影 */
--shadow-md: [阴影值];   /* 中等阴影 */
--shadow-lg: [阴影值];   /* 大阴影 */
--shadow-xl: [阴影值];   /* 超大阴影 */
```

---

## 8. 圆角系统

```css
--radius-sm: [圆角值];   /* 小圆角 */
--radius-md: [圆角值];   /* 中等圆角 */
--radius-lg: [圆角值];   /* 大圆角 */
--radius-xl: [圆角值];   /* 超大圆角 */
--radius-full: [圆角值]; /* 完全圆角 */
```

---

## 9. 交互效果

### 9.1 过渡动画

```css
--transition-fast: [时长] ease;   /* 快速过渡 */
--transition-base: [时长] ease;   /* 基础过渡 */
--transition-slow: [时长] ease;   /* 慢速过渡 */
```

### 9.2 Hover 状态

- **按钮**: [Hover效果描述]
- **链接**: [Hover效果描述]
- **卡片**: [Hover效果描述]
- **导航**: [Hover效果描述]

### 9.3 加载状态

```css
.spinner {
  border: [边框宽度] solid var(--color-border);
  border-top-color: var(--color-primary);
  border-radius: var(--radius-full);
  width: [尺寸];
  height: [尺寸];
  animation: spin [动画时长] linear infinite;
}

@keyframes spin {
  to { transform: rotate(360deg); }
}
```

### 9.4 动画时长

- **快速**: [时长] - 微交互、状态变化
- **基础**: [时长] - 一般过渡
- **慢速**: [时长] - 页面切换、复杂动画

---

## 10. 响应式设计

### 10.1 断点设置

```css
--breakpoint-xs: [宽度值];  /* 超小屏 (手机竖) */
--breakpoint-sm: [宽度值];  /* 小屏 (手机横) */
--breakpoint-md: [宽度值];  /* 中屏 (平板) */
--breakpoint-lg: [宽度值];  /* 大屏 (桌面) */
--breakpoint-xl: [宽度值];  /* 超大屏 (大桌面) */
```

### 10.2 响应式规则

- **Mobile First**: 从小屏开始，向上扩展
- **断点范围**: [断点范围描述]
- **字体缩放**: [字体缩放规则]
- **布局适配**: [布局适配规则]

### 10.3 断点使用示例

```css
/* 小屏及以下 */
@media (max-width: 767px) {
  /* 小屏样式 */
}

/* 中屏及以上 */
@media (min-width: 768px) {
  /* 中屏样式 */
}

/* 大屏及以上 */
@media (min-width: 1024px) {
  /* 大屏样式 */
}
```

---

## 11. 无障碍设计

### 11.1 对比度要求

- **WCAG AA**: 正文对比度 ≥ 4.5:1，大文本 ≥ 3:1
- **WCAG AAA**: 正文对比度 ≥ 7:1，大文本 ≥ 4.5:1

**当前对比度**:
- 主文本 vs 主背景: [对比度值] ([WCAG等级])
- CTA vs 背景: [对比度值] ([WCAG等级])

### 11.2 键盘导航

```css
/* Focus 可见 */
:focus-visible {
  outline: [焦点样式];
  outline-offset: [焦点偏移];
}
```

### 11.3 屏幕阅读器支持

```css
/* 屏幕阅读器专用 */
.sr-only {
  position: absolute;
  width: 1px;
  height: 1px;
  padding: 0;
  margin: -1px;
  overflow: hidden;
  clip: rect(0, 0, 0, 0);
  white-space: nowrap;
  border: 0;
}
```

### 11.4 减少动画

```css
@media (prefers-reduced-motion: reduce) {
  *,
  *::before,
  *::after {
    animation-duration: 0.01ms !important;
    animation-iteration-count: 1 !important;
    transition-duration: 0.01ms !important;
  }
}
```

---

## 12. 反模式（避免）

### 12.1 应避免的设计

1. **[反模式1]**
   - **原因**: [原因说明]
   - **替代方案**: [替代方案]

2. **[反模式2]**
   - **原因**: [原因说明]
   - **替代方案**: [替代方案]

### 12.2 行业特定反模式

- **[行业]**: 应避免 [特定反模式]
- **原因**: [原因说明]

---

## 13. 技术栈配置

### 13.1 Tailwind CSS 配置

```javascript
// tailwind.config.js
module.exports = {
  theme: {
    extend: {
      colors: {
        primary: {
          DEFAULT: 'var(--color-primary)',
          light: 'var(--color-primary-light)',
          dark: 'var(--color-primary-dark)',
        },
        secondary: {
          DEFAULT: 'var(--color-secondary)',
          light: 'var(--color-secondary-light)',
          dark: 'var(--color-secondary-dark)',
        },
        cta: {
          DEFAULT: 'var(--color-cta)',
          hover: 'var(--color-cta-hover)',
          active: 'var(--color-cta-active)',
        },
      },
      fontFamily: {
        heading: ['var(--font-heading)'],
        body: ['var(--font-body)'],
        code: ['var(--font-code)'],
      },
      spacing: {
        xs: 'var(--space-xs)',
        sm: 'var(--space-sm)',
        md: 'var(--space-md)',
        lg: 'var(--space-lg)',
        xl: 'var(--space-xl)',
        '2xl': 'var(--space-2xl)',
      },
    }
  }
}
```

### 13.2 CSS Variables

```css
/* :root, :root[data-theme="light"] */
:root {
  /* 色彩 */
  --color-primary: [主色值];
  --color-secondary: [辅助色值];
  --color-cta: [CTA色值];

  /* 字体 */
  --font-heading: [标题字体];
  --font-body: [正文字体];

  /* 间距 */
  --space-sm: [小间距值];
  --space-md: [中等间距值];
  --space-lg: [大间距值];

  /* 过渡 */
  --transition-base: [过渡时长] ease;
}

/* :root[data-theme="dark"] */
:root[data-theme="dark"] {
  /* 深色模式变量 */
}
```

---

## 14. 设计资源

### 14.1 图标库

- **推荐**: [Heroicons / Lucide / Material Icons]
- **原因**: [选择原因]
- **引入方式**: [引入代码]

### 14.2 图片资源

- **占位图**: [占位图服务]
- **图库**: [免费图库推荐]
- **规格**: [图片规格要求]

### 14.3 设计工具

- **Figma**: [Figma文件链接]
- **Sketch**: [Sketch文件链接]
- **Adobe XD**: [XD文件链接]

---

## 15. 使用指南

### 15.1 快速开始

1. **引入字体**: 复制 Google Fonts 代码到 `<head>`
2. **设置CSS变量**: 复制 CSS Variables 到样式文件
3. **使用组件**: 参考组件规范实现UI

### 15.2 最佳实践

1. **保持一致性**: 严格遵循设计系统规范
2. **语义化命名**: 使用语义化的类名和变量
3. **响应式优先**: 考虑所有断点的表现
4. **无障碍友好**: 确保键盘导航和屏幕阅读器支持
5. **性能优化**: 避免过度使用动画和效果

### 15.3 常见问题

**Q: [问题1]**
A: [答案1]

**Q: [问题2]**
A: [答案2]

---

## 16. 版本历史

| 版本 | 日期 | 变更说明 | 作者 |
|------|------|----------|------|
| 1.0 | [日期] | 初始版本 | [作者] |

---

## 17. 附录

### 17.1 术语表

| 术语 | 定义 |
|------|------|
| [术语1] | [定义1] |
| [术语2] | [定义2] |

### 17.2 参考资源

- [参考资源1]
- [参考资源2]
- [参考资源3]

---

**文档结束**
