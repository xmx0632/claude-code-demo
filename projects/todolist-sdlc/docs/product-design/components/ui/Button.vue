<template>
  <button
    :type="type"
    :class="classes"
    :disabled="disabled || loading"
    v-bind="$attrs"
  >
    <Spinner v-if="loading" :size="iconSize" class="mr-2" />
    <component
      :is="icon"
      v-if="icon && !loading"
      :size="iconSize"
      class="mr-2"
    />
    <slot />
  </button>
</template>

<script setup lang="ts">
import { computed } from 'vue';
import Spinner from './Spinner.vue';

interface Props {
  type?: 'button' | 'submit' | 'reset';
  variant?: 'primary' | 'secondary' | 'ghost' | 'danger';
  size?: 'sm' | 'md' | 'lg';
  icon?: any;
  disabled?: boolean;
  loading?: boolean;
}

const props = withDefaults(defineProps<Props>(), {
  type: 'button',
  variant: 'primary',
  size: 'md',
  disabled: false,
  loading: false,
});

const iconSize = computed(() => {
  const sizes = { sm: 16, md: 18, lg: 20 };
  return sizes[props.size];
});

const classes = computed(() => {
  return [
    'btn',
    `btn-${props.variant}`,
    `btn-${props.size}`,
    { 'btn-disabled': props.disabled || props.loading },
  ];
});
</script>

<style scoped>
.btn {
  @apply inline-flex items-center justify-center;
  @apply font-medium;
  @apply transition-all duration-fast;
  @apply focus:outline-none focus:ring-2 focus:ring-offset-2;
  @apply disabled:opacity-50 disabled:cursor-not-allowed;
}

/* Size */
.btn-sm {
  @apply h-8 px-3 text-sm rounded-lg;
  @apply focus:ring-offset-1;
}

.btn-md {
  @apply h-10 px-4 text-base rounded-lg;
}

.btn-lg {
  @apply h-12 px-6 text-lg rounded-xl;
}

/* Variant - Primary */
.btn-primary {
  @apply bg-primary-500 text-white;
  @apply hover:bg-primary-600 active:bg-primary-700;
  @apply focus:ring-primary-500;
}

/* Variant - Secondary */
.btn-secondary {
  @apply bg-slate-100 text-slate-700;
  @apply hover:bg-slate-200 active:bg-slate-300;
  @apply focus:ring-slate-500;
}

/* Variant - Ghost */
.btn-ghost {
  @apply bg-transparent text-slate-700;
  @apply hover:bg-slate-100 active:bg-slate-200;
  @apply focus:ring-slate-500;
}

/* Variant - Danger */
.btn-danger {
  @apply bg-error-500 text-white;
  @apply hover:bg-error-600 active:bg-error-700;
  @apply focus:ring-error-500;
}
</style>
