# 任务标签功能 - UI 设计交付文档

| 版本 | 1.0 |
|------|-----|
| 日期 | 2026-03-16 |
| 设计工具 | Vue 3 + Tailwind CSS |

---

## 1. 交付概览

### 1.1 设计范围

本次设计交付包含 TodoList 项目任务标签功能的完整 UI 设计，涵盖：

| 模块 | 组件 | 文件 |
|------|------|------|
| 标签管理 | TagManager.vue | 标签管理主页面 |
| 标签管理 | TagCard.vue | 标签卡片组件 |
| 标签管理 | TagModal.vue | 创建/编辑弹窗 |
| 任务标签 | TaskTags.vue | 任务标签添加/移除 |
| 标签筛选 | TagFilter.vue | 标签筛选器 |

### 1.2 设计交付物

```
docs/product-design/
├── README.md                 # 本文档
├── design-system.md          # 设计系统规范
└── components/
    ├── TagManager.vue        # 标签管理页面
    ├── TagCard.vue           # 标签卡片
    ├── TagModal.vue          # 创建/编辑弹窗
    ├── TaskTags.vue          # 任务标签组件
    ├── TagFilter.vue         # 标签筛选器
    └── ui/
        ├── Button.vue        # 按钮组件
        ├── TagBadge.vue      # 标签徽章
        ├── Input.vue         # 输入框（待实现）
        ├── Modal.vue         # 弹窗（待实现）
        └── ...               # 其他基础组件
```

---

## 2. 设计规范

### 2.1 色彩

参见 `design-system.md` 第 2 节

关键颜色：
- 主色：Indigo-500 (#6366F1)
- 成功：Emerald-500 (#10B981)
- 错误：Red-500 (#EF4444)
- 标签：8 种预设颜色

### 2.2 排版

- 字体：Inter
- 大小：12px ~ 30px
- 字重：400/500/600/700

### 2.3 间距

基于 4px 栅格系统：
- 基础单位：4px
- 常用间距：8px / 16px / 24px / 32px

### 2.4 圆角

- 小：4px
- 中：8px
- 大：12px
- 胶囊：9999px

---

## 3. 组件说明

### 3.1 TagManager（标签管理页面）

**功能**：标签的 CRUD 操作管理界面

**特性**：
- 标签卡片网格布局
- 响应式设计（1/2/3 列）
- 空状态提示
- 创建/编辑/删除操作

**关键交互**：
- 点击"创建标签"打开创建弹窗
- 点击标签卡片上的编辑/删除图标
- 删除前确认关联任务数

```vue
<TagManager />
```

### 3.2 TagCard（标签卡片）

**功能**：展示单个标签信息

**Props**：
```typescript
interface Props {
  tag: Tag;  // 标签数据
}
```

**Events**：
```typescript
emits: {
  edit: [tag: Tag];
  delete: [tag: Tag];
}
```

### 3.3 TagModal（创建/编辑弹窗）

**功能**：标签创建和编辑表单

**Props**：
```typescript
interface Props {
  open: boolean;
  tag?: Tag | null;
  existingNames?: string[];
}
```

**特性**：
- 名称输入（最多 20 字符）
- 颜色选择器（8 预设 + 自定义）
- 实时预览
- 表单验证

### 3.4 TaskTags（任务标签组件）

**功能**：为任务添加/移除标签

**Props**：
```typescript
interface Props {
  tags: Tag[];           // 已添加的标签
  availableTags: Tag[];  // 可选标签列表
  size?: 'sm' | 'md' | 'lg';
}
```

**Events**：
```typescript
emits: {
  add: [tag: Tag];
  remove: [tag: Tag];
}
```

**特性**：
- Popover 下拉选择
- 标签搜索
- 快速创建新标签
- 动画过渡

### 3.5 TagFilter（标签筛选器）

**功能**：按标签筛选任务列表

**Props**：
```typescript
interface Props {
  tags: Tag[];
  modelValue?: Tag[];
  filteredCount?: number;
}
```

**特性**：
- 多标签选择（AND 逻辑）
- 筛选状态显示
- 移动端浮动按钮
- 快速清除

---

## 4. 使用示例

### 4.1 标签管理页面

```vue
<script setup lang="ts">
import TagManager from '@/components/tags/TagManager.vue';
</script>

<template>
  <TagManager />
</template>
```

### 4.2 任务详情中的标签

```vue
<script setup lang="ts">
import { ref } from 'vue';
import TaskTags from '@/components/tags/TaskTags.vue';

const taskTags = ref<Tag[]>([]);
const allTags = ref<Tag[]>([
  { id: 1, name: '工作', color: '#FF6B6B', taskCount: 12 },
  { id: 2, name: '个人', color: '#4ECDC4', taskCount: 8 },
]);

const handleAddTag = (tag: Tag) => {
  taskTags.value.push(tag);
};

const handleRemoveTag = (tag: Tag) => {
  taskTags.value = taskTags.value.filter(t => t.id !== tag.id);
};
</script>

<template>
  <TaskTags
    v-model="taskTags"
    :available-tags="allTags"
    @add="handleAddTag"
    @remove="handleRemoveTag"
  />
</template>
```

### 4.3 任务列表筛选

```vue
<script setup lang="ts">
import { ref } from 'vue';
import TagFilter from '@/components/tags/TagFilter.vue';

const selectedTags = ref<Tag[]>([]);
const allTags = ref<Tag[]>([/* ... */]);

const filteredTaskCount = ref(5);
</script>

<template>
  <div>
    <TagFilter
      v-model="selectedTags"
      :tags="allTags"
      :filtered-count="filteredTaskCount"
    />
    <!-- 任务列表... -->
  </div>
</template>
```

---

## 5. 响应式设计

### 5.1 断点

```css
--breakpoint-sm: 640px;
--breakpoint-md: 768px;
--breakpoint-lg: 1024px;
--breakpoint-xl: 1280px;
```

### 5.2 标签管理页面

| 屏幕尺寸 | 列数 |
|----------|------|
| < 640px | 1 列 |
| 640px - 1024px | 2 列 |
| > 1024px | 3 列 |

### 5.3 标签筛选器

- 桌面端：展开在侧边栏
- 移动端：浮动按钮在右下角

---

## 6. 状态设计

### 6.1 空状态

| 场景 | 描述 | 图示 |
|------|------|------|
| 无标签 | "还没有标签，创建您的第一个标签" | Tag 图标 |
| 筛选无结果 | "没有找到符合条件的任务" | Filter 图标 |
| 搜索无结果 | "未找到匹配的标签" | Search 图标 |

### 6.2 加载状态

- 按钮加载：Spinner 动画
- 列表加载：骨架屏
- 弹窗加载：遮罩 + Spinner

### 6.3 错误状态

- 表单错误：红色边框 + 错误提示
- 网络错误：Toast 提示
- 删除确认：警告色弹窗

---

## 7. 动画规范

### 7.1 过渡时长

```css
--transition-fast: 150ms;
--transition-base: 200ms;
--transition-slow: 300ms;
```

### 7.2 常用动画

| 交互 | 动画 |
|------|------|
| Hover | 颜色/透明度 150ms |
| Focus | Ring 扩散 200ms |
| Modal | 淡入 + 缩放 200ms |
| 标签添加 | 缩放 + 淡入 150ms |
| 标签移除 | 缩小 + 淡出 150ms |

### 7.3 Easing

默认使用 `cubic-bezier(0.4, 0, 0.2, 1)`

---

## 8. 无障碍设计

### 8.1 键盘导航

- Tab：焦点移动
- Enter/Space：激活按钮
- Escape：关闭弹窗
- 方向键：列表导航

### 8.2 ARIA 属性

```vue
<!-- 按钮 -->
<button :aria-label="'编辑标签'" />

<!-- 弹窗 -->
<div role="dialog" :aria-label="'创建标签'" />

<!-- 标签 -->
<span role="badge">{{ tag.name }}</span>
```

### 8.3 颜色对比度

所有文本与背景对比度 ≥ 4.5:1

---

## 9. 浏览器兼容性

| 浏览器 | 最低版本 |
|--------|----------|
| Chrome | 90+ |
| Safari | 14+ |
| Firefox | 88+ |
| Edge | 90+ |

---

## 10. 待实现组件

以下组件为设计规范中的基础组件，待实现：

- [ ] Input.vue
- [ ] Modal.vue
- [ ] Popover.vue
- [ ] Spinner.vue
- [ ] IconButton.vue
- [ ] EmptyState.vue
- [ ] ConfirmDialog.vue
- [ ] FormField.vue
- [ ] TagQuickCreate.vue

---

## 11. 下一步

1. **开发对接**
   - 将设计组件集成到项目
   - 连接后端 API
   - 实现状态管理

2. **测试验证**
   - 单元测试
   - 视觉回归测试
   - 无障碍测试

3. **文档完善**
   - Storybook 组件文档
   - 使用示例
   - 最佳实践

---

## 12. 设计资源

| 资源 | 链接 |
|------|------|
| 设计系统 | `docs/product-design/design-system.md` |
| Tailwind CSS | https://tailwindcss.com |
| Lucide Icons | https://lucide.dev |
| Inter 字体 | https://fonts.google.com/specimen/Inter |

---

## 13. 变更记录

| 版本 | 日期 | 变更内容 | 作者 |
|------|------|----------|------|
| 1.0 | 2026-03-16 | 初始版本 | Claude Code |
