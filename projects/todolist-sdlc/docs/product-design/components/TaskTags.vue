<template>
  <div class="task-tags">
    <!-- 已添加的标签 -->
    <div v-if="tags.length > 0" class="tags-list">
      <TransitionGroup name="tag">
        <TagBadge
          v-for="tag in tags"
          :key="tag.id"
          :color="tag.color"
          :size="size"
          closable
          @close="handleRemove(tag)"
        >
          {{ tag.name }}
        </TagBadge>
      </TransitionGroup>
    </div>

    <!-- 添加标签按钮 -->
    <Popover v-model:open="popoverOpen" :placement="'bottom-start'">
      <template #trigger>
        <button
          class="add-tag-btn"
          :class="{ 'has-tags': tags.length > 0 }"
        >
          <Plus :size="14" />
          <span>添加标签</span>
        </button>
      </template>

      <template #content>
        <div class="tag-popover">
          <!-- 搜索框 -->
          <div v-if="availableTags.length > 5" class="tag-search">
            <Search :size="16" class="search-icon" />
            <input
              v-model="searchQuery"
              type="text"
              placeholder="搜索标签..."
              class="search-input"
            />
          </div>

          <!-- 标签列表 -->
          <div class="tag-list">
            <div
              v-for="tag in filteredTags"
              :key="tag.id"
              class="tag-option"
              :class="{ 'selected': isTagSelected(tag) }"
              @click="handleAdd(tag)"
            >
              <div
                class="tag-dot"
                :style="{ backgroundColor: tag.color }"
              />
              <span class="tag-name">{{ tag.name }}</span>
              <Check
                v-if="isTagSelected(tag)"
                :size="16"
                class="tag-check"
              />
            </div>

            <!-- 空状态 -->
            <div v-if="filteredTags.length === 0" class="empty-state">
              {{ searchQuery ? '未找到匹配的标签' : '没有可用标签' }}
            </div>
          </div>

          <!-- 创建新标签 -->
          <div class="tag-create">
            <button
              class="create-btn"
              @click="openCreateModal"
            >
              <Plus :size="14" />
              创建新标签
            </button>
          </div>
        </div>
      </template>
    </Popover>

    <!-- 创建标签弹窗 -->
    <TagQuickCreate
      v-model:open="createModalOpen"
      @created="handleCreated"
    />
  </div>
</template>

<script setup lang="ts">
import { ref, computed } from 'vue';
import { Plus, Search, Check } from 'lucide-vue-next';
import TagBadge from '@/components/ui/TagBadge.vue';
import Popover from '@/components/ui/Popover.vue';
import TagQuickCreate from '@/components/tags/TagQuickCreate.vue';
import type { Tag, TagSize } from '@/types';

interface Props {
  tags: Tag[];
  availableTags: Tag[];
  size?: TagSize;
}

const props = withDefaults(defineProps<Props>(), {
  size: 'md',
});

const emit = defineEmits<{
  add: [tag: Tag];
  remove: [tag: Tag];
}>();

const popoverOpen = ref(false);
const createModalOpen = ref(false);
const searchQuery = ref('');

// 过滤标签
const filteredTags = computed(() => {
  let tags = props.availableTags;

  // 排除已选择的标签
  tags = tags.filter(t => !props.tags.some(selected => selected.id === t.id));

  // 搜索过滤
  if (searchQuery.value) {
    const query = searchQuery.value.toLowerCase();
    tags = tags.filter(t => t.name.toLowerCase().includes(query));
  }

  return tags;
});

// 检查标签是否已选择
const isTagSelected = (tag: Tag) => {
  return props.tags.some(t => t.id === tag.id);
};

// 添加标签
const handleAdd = (tag: Tag) => {
  emit('add', tag);
  popoverOpen.value = false;
  searchQuery.value = '';
};

// 移除标签
const handleRemove = (tag: Tag) => {
  emit('remove', tag);
};

// 打开创建弹窗
const openCreateModal = () => {
  createModalOpen.value = true;
};

// 创建成功回调
const handleCreated = (tag: Tag) => {
  emit('add', tag);
  popoverOpen.value = false;
};
</script>

<style scoped>
.task-tags {
  @apply flex flex-wrap items-center gap-2;
}

.tags-list {
  @apply flex flex-wrap items-center gap-2;
}

/* 标签动画 */
.tag-enter-active,
.tag-leave-active {
  @apply transition-all duration-fast;
}

.tag-enter-from {
  @apply opacity-0 scale-75;
}

.tag-leave-to {
  @apply opacity-0 scale-75;
}

.add-tag-btn {
  @apply inline-flex items-center gap-1.5;
  @apply px-3 py-1.5;
  @apply text-sm text-slate-500;
  @apply border border-dashed border-slate-300 rounded-full;
  @apply transition-all duration-fast;
  @apply hover:border-primary-500 hover:text-primary-500 hover:bg-primary-50;
}

.add-tag-btn.has-tags {
  @apply py-1 px-2;
  @apply text-xs;
}

/* Popover 内容 */
.tag-popover {
  @apply w-64;
}

.tag-search {
  @apply relative mb-3;
}

.search-icon {
  @apply absolute left-3 top-1/2 -translate-y-1/2;
  @apply text-slate-400;
}

.search-input {
  @apply w-full pl-9 pr-3 py-2;
  @apply border border-slate-200 rounded-lg;
  @apply text-sm;
  @apply focus:outline-none focus:ring-2 focus:ring-primary-500;
}

.tag-list {
  @apply max-h-48 overflow-y-auto;
  @apply space-y-1;
}

.tag-option {
  @apply flex items-center gap-2;
  @apply px-3 py-2;
  @apply rounded-lg cursor-pointer;
  @apply transition-colors duration-fast;
}

.tag-option:hover:not(.selected) {
  @apply bg-slate-50;
}

.tag-option.selected {
  @apply bg-primary-50;
}

.tag-dot {
  @apply w-3 h-3 rounded-full;
  @apply flex-shrink-0;
}

.tag-name {
  @apply flex-1 text-sm text-slate-700;
}

.tag-check {
  @apply text-primary-500;
  @apply flex-shrink-0;
}

.empty-state {
  @apply py-4 text-center;
  @apply text-sm text-slate-400;
}

.tag-create {
  @apply pt-2 mt-2;
  @apply border-t border-slate-100;
}

.create-btn {
  @apply w-full flex items-center justify-center gap-2;
  @apply py-2;
  @apply text-sm text-primary-600;
  @apply transition-colors duration-fast;
  @apply hover:bg-primary-50 rounded-lg;
}
</style>
