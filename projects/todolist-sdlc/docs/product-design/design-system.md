# TodoList 任务标签功能 - 设计系统

| 版本 | 1.0 |
|------|-----|
| 日期 | 2026-03-16 |
| 设计风格 | 现代、简洁、高效 |

---

## 1. 设计原则

| 原则 | 描述 |
|------|------|
| **清晰** | 信息层级分明，操作意图明确 |
| **高效** | 减少点击次数，提供快捷操作 |
| **一致** | 组件样式统一，交互模式一致 |
| **反馈** | 每个操作都有即时视觉反馈 |

---

## 2. 色彩系统

### 2.1 品牌色

```css
/* Primary - 主要操作色 */
--primary-50: #EEF2FF;
--primary-100: #E0E7FF;
--primary-500: #6366F1;  /* Indigo-500 */
--primary-600: #4F46E5;
--primary-700: #4338CA;

/* Success - 成功状态 */
--success-500: #10B981;
--success-600: #059669;

/* Warning - 警告状态 */
--warning-500: #F59E0B;
--warning-600: #D97706;

/* Error - 错误状态 */
--error-500: #EF4444;
--error-600: #DC2626;
```

### 2.2 中性色

```css
/* Text */
--text-primary: #0F172A;    /* slate-900 */
--text-secondary: #475569;  /* slate-600 */
--text-tertiary: #94A3B8;   /* slate-400 */
--text-disabled: #CBD5E1;   /* slate-300 */

/* Background */
--bg-primary: #FFFFFF;
--bg-secondary: #F8FAFC;    /* slate-50 */
--bg-tertiary: #F1F5F9;     /* slate-100 */

/* Border */
--border-default: #E2E8F0;  /* slate-200 */
--border-muted: #F1F5F9;    /* slate-100 */
```

### 2.3 标签预设颜色

```css
/* Tag Colors */
--tag-red: #FF6B6B;
--tag-orange: #FFA07A;
--tag-amber: #F7DC6F;
--tag-green: #4ECDC4;
--tag-cyan: #45B7D1;
--tag-blue: #5C9DED;
--tag-purple: #BB8FCE;
--tag-pink: #FF9FF3;
```

---

## 3. 排版系统

### 3.1 字体家族

```css
/* Font Family */
--font-sans: 'Inter', -apple-system, BlinkMacSystemFont, 'Segoe UI', Roboto,
             'Helvetica Neue', Arial, sans-serif;
--font-mono: 'JetBrains Mono', 'SF Mono', Monaco, 'Cascadia Code', monospace;
```

### 3.2 字体大小

```css
/* Font Size */
--text-xs: 0.75rem;      /* 12px */
--text-sm: 0.875rem;     /* 14px */
--text-base: 1rem;       /* 16px */
--text-lg: 1.125rem;     /* 18px */
--text-xl: 1.25rem;      /* 20px */
--text-2xl: 1.5rem;      /* 24px */
--text-3xl: 1.875rem;    /* 30px */
```

### 3.3 字重

```css
/* Font Weight */
--font-normal: 400;
--font-medium: 500;
--font-semibold: 600;
--font-bold: 700;
```

---

## 4. 间距系统

```css
/* Spacing Scale (4px base unit) */
--spacing-1: 0.25rem;   /* 4px */
--spacing-2: 0.5rem;    /* 8px */
--spacing-3: 0.75rem;   /* 12px */
--spacing-4: 1rem;      /* 16px */
--spacing-5: 1.25rem;   /* 20px */
--spacing-6: 1.5rem;    /* 24px */
--spacing-8: 2rem;      /* 32px */
--spacing-10: 2.5rem;   /* 40px */
--spacing-12: 3rem;     /* 48px */
```

---

## 5. 圆角系统

```css
/* Border Radius */
--radius-sm: 0.25rem;   /* 4px */
--radius-md: 0.375rem;  /* 6px */
--radius-lg: 0.5rem;    /* 8px */
--radius-xl: 0.75rem;   /* 12px */
--radius-2xl: 1rem;     /* 16px */
--radius-full: 9999px;  /* Pill shape */
```

---

## 6. 阴影系统

```css
/* Box Shadow */
--shadow-sm: 0 1px 2px 0 rgb(0 0 0 / 0.05);
--shadow-md: 0 4px 6px -1px rgb(0 0 0 / 0.1), 0 2px 4px -2px rgb(0 0 0 / 0.1);
--shadow-lg: 0 10px 15px -3px rgb(0 0 0 / 0.1), 0 4px 6px -4px rgb(0 0 0 / 0.1);
--shadow-xl: 0 20px 25px -5px rgb(0 0 0 / 0.1), 0 8px 10px -6px rgb(0 0 0 / 0.1);
```

---

## 7. 动画系统

```css
/* Transitions */
--transition-fast: 150ms cubic-bezier(0.4, 0, 0.2, 1);
--transition-base: 200ms cubic-bezier(0.4, 0, 0.2, 1);
--transition-slow: 300ms cubic-bezier(0.4, 0, 0.2, 1);

/* Easing */
--ease-in: cubic-bezier(0.4, 0, 1, 1);
--ease-out: cubic-bezier(0, 0, 0.2, 1);
--ease-in-out: cubic-bezier(0.4, 0, 0.2, 1);
```

---

## 8. 组件规范

### 8.1 按钮组件

```vue
<!-- 尺寸 -->
<Button size="sm">  <!-- h-8 px-3 text-sm -->
<Button size="md">  <!-- h-10 px-4 text-base -->
<Button size="lg">  <!-- h-12 px-6 text-lg -->

<!-- 变体 -->
<Button variant="primary">   <!-- bg-primary-500 text-white -->
<Button variant="secondary"> <!-- bg-slate-100 text-slate-700 -->
<Button variant="ghost">     <!-- bg-transparent hover:bg-slate-100 -->
<Button variant="danger">    <!-- bg-error-500 text-white -->
```

### 8.2 标签组件

```vue
<!-- 尺寸 -->
<Tag size="sm">  <!-- h-6 px-2 text-xs -->
<Tag size="md">  <!-- h-7 px-3 text-sm -->
<Tag size="lg">  <!-- h-8 px-4 text-base -->

<!-- 变体 -->
<Tag :color="tag-red">工作</Tag>
<Tag :color="tag-green">个人</Tag>

<!-- 可关闭 -->
Tag closable @close="handleClose"
```

### 8.3 输入框组件

```vue
<Input
  placeholder="请输入标签名称"
  size="md"
  :maxlength="20"
  :error="errorMessage"
/>
```

---

## 9. 图标系统

使用 **Lucide Icons** (Vue 3)

```vue
<script setup>
import {
  Tag,
  Plus,
  X,
  Edit2,
  Trash2,
  Filter,
  Check
} from 'lucide-vue-next';
</script>
```

| 图标 | 用途 |
|------|------|
| `Tag` | 标签管理入口 |
| `Plus` | 添加标签/创建标签 |
| `X` | 关闭/移除 |
| `Edit2` | 编辑标签 |
| `Trash2` | 删除标签 |
| `Filter` | 筛选 |
| `Check` | 选中状态 |

---

## 10. 响应式断点

```css
/* Breakpoints */
--breakpoint-sm: 640px;
--breakpoint-md: 768px;
--breakpoint-lg: 1024px;
--breakpoint-xl: 1280px;
--breakpoint-2xl: 1536px;
```

---

## 11. 无障碍规范

### 11.1 颜色对比度

- 正文文本：至少 4.5:1
- 大号文本：至少 3:1
- 图标/图形：至少 3:1

### 11.2 键盘导航

- Tab: 焦点移动
- Enter/Space: 激活按钮
- Escape: 关闭弹窗/下拉
- 方向键: 在列表中导航

### 11.3 ARIA 属性

```vue
<button
  :aria-label="label"
  :aria-pressed="pressed"
  :aria-expanded="expanded"
>
```

---

## 12. 设计交付物

| 交付物 | 文件位置 |
|--------|----------|
| 设计系统文档 | `docs/product-design/design-system.md` |
| 标签管理页面 | `docs/product-design/components/TagManager.vue` |
| 任务标签组件 | `docs/product-design/components/TaskTags.vue` |
| 标签筛选器组件 | `docs/product-design/components/TagFilter.vue` |

---

## 13. Tailwind 配置

```javascript
// tailwind.config.js
export default {
  theme: {
    extend: {
      colors: {
        primary: {
          50: '#EEF2FF',
          100: '#E0E7FF',
          500: '#6366F1',
          600: '#4F46E5',
          700: '#4338CA',
        },
        tag: {
          red: '#FF6B6B',
          orange: '#FFA07A',
          amber: '#F7DC6F',
          green: '#4ECDC4',
          cyan: '#45B7D1',
          blue: '#5C9DED',
          purple: '#BB8FCE',
          pink: '#FF9FF3',
        }
      },
      fontFamily: {
        sans: ['Inter', 'sans-serif'],
        mono: ['JetBrains Mono', 'monospace'],
      },
      transitionDuration: {
        fast: '150ms',
        base: '200ms',
        slow: '300ms',
      },
    }
  }
}
```
