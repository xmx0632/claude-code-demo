# 设计系统 (Design System)

> **项目**: TodoList 待办事项管理系统
> **版本**: 1.0
> **最后更新**: 2026-01-26

---

## 设计系统概述

本文档定义 TodoList 应用的设计系统，包括视觉设计、交互模式、组件规范和设计原则，确保产品的一致性和用户体验。

---

## 设计原则

### 核心原则

1. **简洁优先** (Simplicity First)
   - 界面简洁明了，避免不必要的装饰
   - 功能直观易懂，减少学习成本
   - 内容优先于形式

2. **一致性** (Consistency)
   - 视觉元素风格统一
   - 交互模式保持一致
   - 术语和表述规范统一

3. **可用性** (Usability)
   - 符合用户心智模型
   - 提供明确的反馈和引导
   - 容错性和可恢复性

4. **响应式** (Responsiveness)
   - 适配多种设备尺寸
   - 优化触摸交互
   - 性能优先

5. **可访问性** (Accessibility)
   - 支持键盘导航
   - 适当的颜色对比度
   - 屏幕阅读器友好

---

## 颜色系统

### 主色调

| 颜色名称 | HEX 值 | RGB 值 | 使用场景 | 情感联想 |
|----------|--------|--------|----------|----------|
| 主蓝色 | #1890FF | rgb(24, 144, 255) | 主要按钮、链接、激活状态 | 专业、可信赖 |
| 浅蓝色 | #40A9FF | rgb(64, 169, 255) | 悬停状态 | 友好、轻快 |
| 深蓝色 | #0050B3 | rgb(0, 80, 179) | 按下状态 | 稳重、可靠 |

### 功能色

| 颜色名称 | HEX 值 | 使用场景 |
|----------|--------|----------|
| 成功绿 | #52C41A | 成功提示、完成状态、确认操作 |
| 警告橙 | #FAAD14 | 警告提示、中优先级 |
| 错误红 | #F5222D | 错误提示、删除操作、高优先级 |
| 信息蓝 | #1890FF | 信息提示、链接 |
| 帮助灰 | #8C8C8C | 帮助文本、禁用状态 |

### 中性色

| 颜色名称 | HEX 值 | 使用场景 |
|----------|--------|----------|
| 标题黑 | #262626 | 一级标题 |
| 正文黑 | #333333 | 正文、二级标题 |
| 辅助灰 | #666666 | 说明文字、三级标题 |
| 禁用灰 | #BFBFBF | 禁用文本 |
| 边框灰 | #D9D9D9 | 边框、分割线 |
| 背景灰 | #F5F5F5 | 页面背景 |
| 卡片白 | #FFFFFF | 卡片背景、模块背景 |

### 背景色

| 颜色名称 | HEX 值 | 使用场景 |
|----------|--------|----------|
| 页面背景 | #F5F5F5 | 全局页面背景 |
| 组件背景 | #FFFFFF | 卡片、模态框、下拉菜单 |
| 悬停背景 | #F5F5F5 | 列表项、表格行悬停 |
| 激活背景 | #E6F7FF | 选中项、激活状态 |
| 输入框背景 | #FFFFFF | 输入框、下拉选择器 |
| 禁用背景 | #F5F5F5 | 禁用组件 |

### 标签色

| 颜色名称 | HEX 值 | 背景色 | 使用场景 |
|----------|--------|--------|----------|
| 蓝色标签 | #1890FF | #E6F7FF | 工作分类 |
| 绿色标签 | #52C41A | #F6FFED | 个人分类 |
| 橙色标签 | #FAAD14 | #FFF7E6 | 学习分类 |
| 红色标签 | #F5222D | #FFF1F0 | 高优先级 |
| 灰色标签 | #8C8C8C | #FAFAFA | 低优先级 |

---

## 字体系统

### 字体家族

**主字体**:
```css
font-family: -apple-system, BlinkMacSystemFont, "Segoe UI", "PingFang SC",
             "Hiragino Sans GB", "Microsoft YaHei", Arial, sans-serif;
```

**代码字体**:
```css
font-family: "SFMono-Regular", Consolas, "Liberation Mono", Menlo, Courier,
             monospace;
```

### 字体层级

| 级别 | 字号 | 字重 | 行高 | 使用场景 |
|------|------|------|------|----------|
| H1 | 24px | Bold (600) | 1.3 | 页面标题 |
| H2 | 20px | Bold (600) | 1.4 | 模块标题 |
| H3 | 18px | Medium (500) | 1.5 | 子标题 |
| H4 | 16px | Medium (500) | 1.5 | 卡片标题 |
| Body Large | 15px | Normal (400) | 1.6 | 重要正文 |
| Body | 14px | Normal (400) | 1.6 | 正文 |
| Body Small | 13px | Normal (400)  | 1.5 | 辅助文字 |
| Caption | 12px | Normal (400) | 1.5 | 说明文字、提示 |

### 字重规范

| 字重名称 | 数值 | 使用场景 |
|----------|------|----------|
| Regular | 400 | 正文、说明文字 |
| Medium | 500 | 子标题、按钮文字、标签 |
| Semibold | 600 | 标题、重要文字 |
| Bold | 700 | 强调文字、页面标题 |

---

## 间距系统

### 基础间距单位

基础单位: **4px**

| 名称 | 值 | 使用场景 |
|------|-----|----------|
| XXS | 4px | 紧凑元素间距 |
| XS | 8px | 小间距、图标与文字 |
| SM | 12px | 中小间距、表单字段 |
| MD | 16px | 中等间距、卡片内边距 |
| LG | 20px | 大间距、模块间距 |
| XL | 24px | 超大间距、页面区块间距 |
| XXL | 32px | 页面级间距 |

### 组件间距

| 组件 | 内边距 | 外边距 |
|------|--------|--------|
| 按钮 | 8px 16px | 8px |
| 输入框 | 10px 12px | 15px (垂直) |
| 卡片 | 20px | 15px |
| 模态框 | 24px | - |
| 工具栏 | 10px 15px | 15px (垂直) |

---

## 组件规范

### 按钮 (Button)

#### 尺寸

| 尺寸 | 高度 | 水平内边距 | 字号 | 图标尺寸 |
|------|------|-----------|------|----------|
| Small | 28px | 8px 16px | 13px | 14px |
| Medium | 32px | 8px 16px | 14px | 16px |
| Large | 40px | 12px 24px | 16px | 18px |

#### 类型

| 类型 | 背景色 | 文字色 | 边框 | 使用场景 |
|------|--------|--------|------|----------|
| Primary | #1890FF | #FFFFFF | 无 | 主要操作 |
| Default | #FFFFFF | #333333 | 1px solid #D9D9D9 | 次要操作 |
| Danger | #F5222D | #FFFFFF | 无 | 危险操作 |
| Success | #52C41A | #FFFFFF | 无 | 成功操作 |
| Link | 透明 | #1890FF | 无 | 链接按钮 |

#### 状态

- **Normal**: 默认状态
- **Hover**: 背景色加深 10%
- **Active**: 背景色加深 20%
- **Disabled**: 透明度 40%，禁用光标
- **Loading**: 显示加载动画，禁用点击

---

### 输入框 (Input)

#### 尺寸

| 尺寸 | 高度 | 水平内边距 | 字号 |
|------|------|-----------|------|
| Small | 28px | 8px 12px | 13px |
| Medium | 32px | 10px 12px | 14px |
| Large | 40px | 12px 16px | 16px |

#### 状态

| 状态 | 边框色 | 背景色 | 说明 |
|------|--------|--------|------|
| Normal | #D9D9D9 | #FFFFFF | 默认状态 |
| Focus | #1890FF | #FFFFFF | 聚焦状态 |
| Error | #F5222D | #FFFFFF | 验证失败 |
| Disabled | #D9D9D9 | #F5F5F5 | 禁用状态 |

#### 前缀/后缀

- 前缀图标: 距离输入框 8px
- 后缀图标: 距离输入框 8px
- 图标颜色: #8C8C8C

---

### 卡片 (Card)

#### 基础样式

```css
background: #FFFFFF;
border: 1px solid #E8E8E8;
border-radius: 4px;
padding: 20px;
box-shadow: 0 2px 4px rgba(0, 0, 0, 0.05);
```

#### 状态

- **Normal**: 默认阴影
- **Hover**: 阴影加深 `0 4px 8px rgba(0, 0, 0, 0.1)`
- **Active**: 边框色变为主题蓝

---

### 标签 (Tag)

#### 尺寸

| 尺寸 | 高度 | 水平内边距 | 字号 | 圆角 |
|------|------|-----------|------|------|
| Small | 20px | 4px 8px | 12px | 2px |
| Medium | 24px | 5px 10px | 13px | 3px |
| Large | 28px | 6px 12px | 14px | 4px |

#### 颜色变体

| 类型 | 背景色 | 文字色 | 边框色 |
|------|--------|--------|--------|
| Default | #FAFAFA | #666666 | #D9D9D9 |
| Blue | #E6F7FF | #1890FF | #91D5FF |
| Green | #F6FFED | #52C41A | #B7EB8F |
| Orange | #FFF7E6 | #FAAD14 | #FFD591 |
| Red | #FFF1F0 | #F5222D | #FFA39E |

---

### 表格 (Table)

#### 行高

| 类型 | 行高 | 内边距 |
|------|------|--------|
| Compact | 40px | 8px 12px |
| Normal | 56px | 12px 16px |
| Spacious | 72px | 16px 20px |

#### 表头

```css
background: #FAFAFA;
border-bottom: 1px solid #E8E8E8;
font-weight: 500;
color: #333333;
```

#### 行状态

- **Normal**: 白色背景
- **Hover**: #F5F5F5 背景
- **Selected**: #E6F7FF 背景
- **Odd**: 可选斑马纹 #FAFAFA

---

### 模态框 (Modal)

#### 尺寸

| 尺寸 | 宽度 | 最大宽度 |
|------|------|---------|
| Small | 90% | 400px |
| Medium | 90% | 600px |
| Large | 90% | 800px |
| Full | 100% | 100% |

#### 结构

```
┌────────────────────────────────┐
│ 标题 (24px 内边距)             │
├────────────────────────────────┤
│                                │
│ 内容 (24px 内边距)              │
│                                │
├────────────────────────────────┤
│ 按钮 (右对齐, 16px 内边距)     │
└────────────────────────────────┘
```

#### 遮罩

```css
background: rgba(0, 0, 0, 0.45);
backdrop-filter: blur(2px);
```

---

### 消息提示 (Toast)

#### 类型

| 类型 | 图标 | 背景色 | 文字色 | 自动关闭 |
|------|------|--------|--------|----------|
| Success | ✓ | #F6FFED | #52C41A | 3秒 |
| Info | ℹ | #E6F7FF | #1890FF | 3秒 |
| Warning | ⚠ | #FFFBE6 | #FAAD14 | 5秒 |
| Error | ✕ | #FFF1F0 | #F5222D | 不关闭 |

#### 位置

- Top Right (默认): 右上角
- Top Left: 左上角
- Top Center: 顶部居中
- Bottom Right: 右下角

---

### 分页 (Pagination)

#### 尺寸

| 尺寸 | 按钮高度 | 内边距 | 字号 |
|------|----------|--------|------|
| Small | 28px | 4px 10px | 13px |
| Medium | 32px | 5px 12px | 14px |
| Large | 40px | 6px 16px | 16px |

#### 按钮状态

- **Normal**: 白色背景，灰色边框
- **Active**: 主题蓝背景，白色文字
- **Disabled**: 灰色背景，禁用光标

---

## 图标系统

### 图标尺寸

| 尺寸 | 值 | 使用场景 |
|------|-----|----------|
| XS | 12px | 小图标、列表图标 |
| SM | 16px | 按钮图标、输入框图标 |
| MD | 20px | 表格操作图标 |
| LG | 24px | 卡片图标、导航图标 |
| XL | 32px | 页面主图标 |
| XXL | 48px | 空状态图标 |

### 图标规范

- 使用线条图标（Outlined）而非填充图标（Filled）
- 线条粗细: 1.5px - 2px
- 圆角: 2px（适度圆角）
- 颜色: 继承文字颜色或使用语义色

### 推荐图标库

- [Ant Design Icons](https://ant.design/components/icon/)
- [Heroicons](https://heroicons.com/)
- [Feather Icons](https://feathericons.com/)

---

## 动画效果

### 过渡时长

| 类型 | 时长 | 缓动函数 | 使用场景 |
|------|------|----------|----------|
| Fast | 150ms | ease-out | 简单过渡 |
| Normal | 250ms | ease-in-out | 标准过渡 |
| Slow | 350ms | ease-in-out | 复杂过渡 |

### 常用动画

#### 淡入淡出

```css
.fade-enter { opacity: 0; }
.fade-enter-active { opacity: 1; transition: opacity 250ms; }
.fade-exit { opacity: 1; }
.fade-exit-active { opacity: 0; transition: opacity 250ms; }
```

#### 滑动

```css
.slide-up-enter { transform: translateY(20px); opacity: 0; }
.slide-up-enter-active {
  transform: translateY(0);
  opacity: 1;
  transition: all 250ms ease-out;
}
```

#### 缩放

```css
.scale-enter { transform: scale(0.95); opacity: 0; }
.scale-enter-active {
  transform: scale(1);
  opacity: 1;
  transition: all 250ms cubic-bezier(0.34, 1.56, 0.64, 1);
}
```

---

## 响应式设计

### 断点

| 断点名称 | 屏幕宽度 | 设备类型 | 布局调整 |
|----------|----------|----------|----------|
| XS | < 576px | 手机（小） | 单栏布局 |
| SM | 576px - 768px | 手机（大） | 单栏布局 |
| MD | 768px - 992px | 平板（小） | 两栏布局 |
| LG | 992px - 1200px | 平板（大） | 两栏布局 |
| XL | 1200px - 1600px | 桌面 | 标准布局 |
| XXL | > 1600px | 大屏桌面 | 宽屏布局 |

### 移动端适配

#### 导航

- 桌面: 水平导航栏
- 平板: 水平导航栏（紧凑）
- 手机: 汉堡菜单 + 抽屉式侧边栏

#### 表格

- 桌面: 完整表格
- 平板: 完整表格（可横向滚动）
- 手机: 卡片式列表

#### 模态框

- 桌面: 居中固定宽度
- 平板: 居中 90% 宽度
- 手机: 全屏，从底部滑入

---

## 可访问性

### 颜色对比度

- **正常文字**: 最小 4.5:1
- **大号文字 (18px+)**: 最小 3:1
- **图标和图形**: 最小 3:1

### 键盘导航

- Tab 键: 按顺序聚焦可交互元素
- Shift + Tab: 反向聚焦
- Enter/Space: 激活聚焦元素
- ESC: 关闭模态框、下拉菜单

### 焦点指示

```css
:focus-visible {
  outline: 2px solid #1890FF;
  outline-offset: 2px;
}
```

### 屏幕阅读器支持

- 图标按钮: 添加 `aria-label`
- 表单字段: 关联 `label` 或 `aria-label`
- 错误提示: 使用 `aria-live` 区域
- 加载状态: `aria-busy="true"`

---

## 设计资源

### 设计工具

- **Figma**: UI 设计和原型
- **Sketch**: UI 设计
- **Adobe XD**: UI 设计和原型

### 代码框架

- **CSS Framework**: Tailwind CSS, Bootstrap
- **Component Library**: Ant Design, Material-UI
- **Icon Library**: Ant Design Icons, Heroicons

### 参考资源

- [Ant Design 设计规范](https://ant.design/docs/spec/introduce)
- [Material Design 设计规范](https://material.io/design)
- [WCAG 2.1 可访问性标准](https://www.w3.org/WAI/WCAG21/quickref/)

---

## 相关文档

- 线框图: `docs/design/wireframes/`
- 用户流程: `docs/design/user-flow.md`
- 原型规格: `docs/design/prototype-spec.md`
- 需求规格: `docs/requirements/requirements-spec.md`
