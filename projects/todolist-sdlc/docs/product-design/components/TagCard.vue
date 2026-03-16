<template>
  <div
    class="tag-card"
    :style="{ borderLeftColor: tag.color }"
  >
    <div class="tag-content">
      <div class="tag-header">
        <TagBadge :color="tag.color" :size="'lg'" />
        <span class="tag-name">{{ tag.name }}</span>
      </div>
      <div class="tag-meta">
        <span class="tag-count">{{ tag.taskCount }} 个任务</span>
        <span class="tag-date">{{ formatDate(tag.createdAt) }}</span>
      </div>
    </div>

    <div class="tag-actions">
      <IconButton
        :icon="Edit2"
        :size="'sm'"
        :variant="'ghost'"
        :aria-label="'编辑标签'"
        @click="$emit('edit', tag)"
      />
      <IconButton
        :icon="Trash2"
        :size="'sm'"
        :variant="'ghost'"
        :aria-label="'删除标签'"
        class="text-error-500 hover:text-error-600 hover:bg-error-50"
        @click="$emit('delete', tag)"
      />
    </div>
  </div>
</template>

<script setup lang="ts">
import { Edit2, Trash2 } from 'lucide-vue-next';
import IconButton from '@/components/ui/IconButton.vue';
import TagBadge from '@/components/ui/TagBadge.vue';
import type { Tag } from '@/types';

defineProps<{
  tag: Tag;
}>();

defineEmits<{
  edit: [tag: Tag];
  delete: [tag: Tag];
}>();

const formatDate = (date: string) => {
  const d = new Date(date);
  const now = new Date();
  const diff = now.getTime() - d.getTime();
  const days = Math.floor(diff / (1000 * 60 * 60 * 24));

  if (days === 0) return '今天';
  if (days === 1) return '昨天';
  if (days < 7) return `${days} 天前`;
  return d.toLocaleDateString('zh-CN', { month: 'short', day: 'numeric' });
};
</script>

<style scoped>
.tag-card {
  @apply bg-white rounded-xl border border-slate-200 p-4;
  @apply transition-all duration-fast;
  border-left-width: 4px;
}

.tag-card:hover {
  @apply shadow-md;
}

.tag-content {
  @apply mb-3;
}

.tag-header {
  @apply flex items-center gap-2 mb-2;
}

.tag-name {
  @apply font-medium text-slate-900 text-base;
}

.tag-meta {
  @apply flex items-center justify-between text-sm;
}

.tag-count {
  @apply text-slate-500;
}

.tag-date {
  @apply text-slate-400 text-xs;
}

.tag-actions {
  @apply flex items-center gap-1 pt-3 border-t border-slate-100;
}
</style>
