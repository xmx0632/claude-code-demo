<template>
  <span
    :class="classes"
    :style="style"
    v-bind="$attrs"
  >
    <span class="tag-content">
      <slot />
    </span>
    <button
      v-if="closable"
      type="button"
      class="tag-close"
      @click="$emit('close')"
    >
      <X :size="closeSize" />
    </button>
  </span>
</template>

<script setup lang="ts">
import { computed } from 'vue';
import { X } from 'lucide-vue-next';

interface Props {
  color?: string;
  size?: 'sm' | 'md' | 'lg';
  closable?: boolean;
}

const props = withDefaults(defineProps<Props>(), {
  color: '#6366F1',
  size: 'md',
  closable: false,
});

defineEmits<{
  close: [];
}>();

const classes = computed(() => {
  return [
    'tag-badge',
    `tag-${props.size}`,
  ];
});

const style = computed(() => ({
  '--tag-color': props.color,
}));

const closeSize = computed(() => {
  const sizes = { sm: 12, md: 14, lg: 16 };
  return sizes[props.size];
});
</script>

<style scoped>
.tag-badge {
  @apply inline-flex items-center gap-1.5;
  @apply font-medium;
  @apply transition-all duration-fast;
  background-color: var(--tag-color);
  color: white;
}

/* Size */
.tag-sm {
  @apply h-6 px-2 text-xs rounded-md;
}

.tag-md {
  @apply h-7 px-3 text-sm rounded-full;
}

.tag-lg {
  @apply h-8 px-4 text-base rounded-full;
}

.tag-content {
  @apply leading-none;
}

.tag-close {
  @apply flex items-center justify-center;
  @apply hover:bg-white/20;
  @apply rounded-full;
  @apply transition-colors duration-fast;
  @apply focus:outline-none focus:ring-2 focus:ring-white/50;
}

/* 尺寸对应的关闭按钮 */
.tag-sm .tag-close {
  @apply w-4 h-4 -mr-1;
}

.tag-md .tag-close {
  @apply w-5 h-5 -mr-1.5;
}

.tag-lg .tag-close {
  @apply w-6 h-6 -mr-2;
}
</style>
