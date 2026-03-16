<template>
  <div class="tag-filter">
    <!-- 筛选面板 -->
    <div v-if="isOpen" class="filter-panel">
      <div class="filter-header">
        <div class="filter-title">
          <Filter :size="18" />
          <h3>标签筛选</h3>
        </div>
        <button
          v-if="selectedTags.length > 0"
          class="clear-btn"
          @click="handleClear"
        >
          清除
        </button>
      </div>

      <!-- 标签列表 -->
      <div class="filter-tags">
        <button
          v-for="tag in tags"
          :key="tag.id"
          class="filter-tag"
          :class="{ 'selected': isSelected(tag) }"
          :style="{
            '--tag-color': tag.color,
            '--tag-bg': isSelected(tag) ? tag.color + '20' : tag.color + '15'
          }"
          @click="toggleTag(tag)"
        >
          <span class="filter-tag-name">{{ tag.name }}</span>
          <span class="filter-tag-count">{{ tag.taskCount }}</span>
        </button>
      </div>

      <!-- 筛选状态 -->
      <div v-if="selectedTags.length > 0" class="filter-status">
        <span class="filter-status-text">
          已选择 <strong>{{ selectedTags.length }}</strong> 个标签，
          找到 <strong>{{ filteredCount }}</strong> 个任务
        </span>
      </div>
    </div>

    <!-- 折叠按钮 -->
    <button
      class="filter-toggle"
      :class="{ 'active': isOpen || selectedTags.length > 0 }"
      @click="isOpen = !isOpen"
    >
      <Filter :size="16" />
      <span v-if="selectedTags.length > 0" class="toggle-badge">
        {{ selectedTags.length }}
      </span>
    </button>
  </div>
</template>

<script setup lang="ts">
import { ref, computed } from 'vue';
import { Filter } from 'lucide-vue-next';
import type { Tag } from '@/types';

interface Props {
  tags: Tag[];
  modelValue?: Tag[];
  filteredCount?: number;
}

const props = withDefaults(defineProps<Props>(), {
  modelValue: () => [],
  filteredCount: 0,
});

const emit = defineEmits<{
  'update:modelValue': [tags: Tag[]];
}>();

const isOpen = ref(false);

// 选中的标签（使用 modelValue 或本地状态）
const selectedTags = computed({
  get: () => props.modelValue,
  set: (value) => emit('update:modelValue', value),
});

// 检查标签是否选中
const isSelected = (tag: Tag) => {
  return selectedTags.value.some(t => t.id === tag.id);
};

// 切换标签
const toggleTag = (tag: Tag) => {
  if (isSelected(tag)) {
    selectedTags.value = selectedTags.value.filter(t => t.id !== tag.id);
  } else {
    selectedTags.value = [...selectedTags.value, tag];
  }
};

// 清除筛选
const handleClear = () => {
  selectedTags.value = [];
};
</script>

<style scoped>
.tag-filter {
  @apply relative;
}

.filter-panel {
  @apply bg-white rounded-xl border border-slate-200;
  @apply shadow-sm;
  @apply overflow-hidden;
  @apply transition-all duration-base;
}

.filter-header {
  @apply flex items-center justify-between;
  @apply px-4 py-3;
  @apply border-b border-slate-100;
}

.filter-title {
  @apply flex items-center gap-2;
  @apply text-slate-700 font-medium;
}

.clear-btn {
  @apply px-3 py-1.5;
  @apply text-sm text-slate-500;
  @apply border border-slate-200 rounded-lg;
  @apply transition-colors duration-fast;
  @apply hover:bg-slate-50 hover:text-slate-700;
}

.filter-tags {
  @apply flex flex-wrap gap-2;
  @apply p-4;
}

.filter-tag {
  @apply inline-flex items-center gap-2;
  @apply px-3 py-2;
  @apply rounded-lg;
  @apply transition-all duration-fast;
  @apply cursor-pointer;
  @apply hover:scale-105;

  background-color: var(--tag-bg);
  color: var(--tag-color);
}

.filter-tag.selected {
  @apply ring-2;
  ring-color: var(--tag-color);
}

.filter-tag-name {
  @apply text-sm font-medium;
}

.filter-tag-count {
  @apply text-xs opacity-70;
}

.filter-status {
  @apply px-4 py-3;
  @apply bg-slate-50;
  @apply border-t border-slate-100;
}

.filter-status-text {
  @apply text-sm text-slate-600;
}

.filter-toggle {
  @apply fixed bottom-6 right-6;
  @apply w-14 h-14;
  @apply flex items-center justify-center;
  @apply bg-white text-slate-400;
  @apply rounded-full shadow-lg border border-slate-200;
  @apply transition-all duration-base;
  @apply hover:scale-110 hover:shadow-xl;
  @apply focus:outline-none focus:ring-2 focus:ring-primary-500;
  @apply z-50;
}

.filter-toggle.active {
  @apply bg-primary-500 text-white;
  @apply border-primary-500;
}

.toggle-badge {
  @apply absolute -top-1 -right-1;
  @apply w-5 h-5;
  @apply flex items-center justify-center;
  @apply bg-error-500 text-white;
  @apply text-xs font-bold;
  @apply rounded-full;
}

/* 移动端适配 */
@media (min-width: 768px) {
  .filter-toggle {
    @apply static;
    @apply w-10 h-10;
    @apply shadow-md;
  }

  .filter-panel {
    @apply shadow-md;
  }
}
</style>
