<template>
  <Modal v-model:open="isOpen" :title="tag ? '编辑标签' : '创建标签'">
    <form @submit.prevent="handleSubmit" class="tag-form">
      <!-- 标签名称 -->
      <FormField label="标签名称" :error="nameError" required>
        <Input
          v-model="formData.name"
          placeholder="例如：工作、个人、重要..."
          :maxlength="20"
          :autofocus="!tag"
        />
        <template #hint>
          {{ formData.name.length }}/20
        </template>
      </FormField>

      <!-- 颜色选择 -->
      <FormField label="标签颜色">
        <div class="color-picker">
          <button
            v-for="color in presetColors"
            :key="color.value"
            type="button"
            class="color-option"
            :class="{ 'selected': formData.color === color.value }"
            :style="{ backgroundColor: color.value }"
            :aria-label="color.name"
            @click="formData.color = color.value"
          >
            <Check
              v-if="formData.color === color.value"
              :size="16"
              class="color-check"
            />
          </button>
        </div>

        <!-- 自定义颜色 -->
        <div class="custom-color">
          <label class="custom-color-label">自定义</label>
          <input
            v-model="formData.color"
            type="color"
            class="color-input"
          />
          <input
            v-model="formData.color"
            type="text"
            class="color-hex"
            placeholder="#FF6B6B"
            maxlength="7"
          />
        </div>
      </FormField>

      <!-- 预览 -->
      <FormField label="预览">
        <div class="tag-preview">
          <TagBadge :color="formData.color" :size="'lg'">
            {{ formData.name || '标签名称' }}
          </TagBadge>
        </div>
      </FormField>

      <!-- 操作按钮 -->
      <div class="form-actions">
        <Button
          type="button"
          variant="ghost"
          @click="handleCancel"
        >
          取消
        </Button>
        <Button
          type="submit"
          variant="primary"
          :disabled="!formData.name || !!nameError"
        >
          {{ tag ? '保存' : '创建' }}
        </Button>
      </div>
    </form>
  </Modal>
</template>

<script setup lang="ts">
import { ref, watch, computed } from 'vue';
import { Check } from 'lucide-vue-next';
import Modal from '@/components/ui/Modal.vue';
import Button from '@/components/ui/Button.vue';
import Input from '@/components/ui/Input.vue';
import FormField from '@/components/ui/FormField.vue';
import TagBadge from '@/components/ui/TagBadge.vue';
import type { Tag } from '@/types';

interface Props {
  open: boolean;
  tag?: Tag | null;
  existingNames?: string[];
}

const props = withDefaults(defineProps<Props>(), {
  existingNames: () => [],
});

const emit = defineEmits<{
  'update:open': [value: boolean];
  save: [data: { name: string; color: string }];
}>();

const isOpen = computed({
  get: () => props.open,
  set: (value) => emit('update:open', value),
});

const formData = ref({
  name: '',
  color: '#FF6B6B',
});

// 预设颜色
const presetColors = [
  { name: '红色', value: '#FF6B6B' },
  { name: '橙色', value: '#FFA07A' },
  { name: '黄色', value: '#F7DC6F' },
  { name: '绿色', value: '#4ECDC4' },
  { name: '青色', value: '#45B7D1' },
  { name: '蓝色', value: '#5C9DED' },
  { name: '紫色', value: '#BB8FCE' },
  { name: '粉色', value: '#FF9FF3' },
];

// 名称验证
const nameError = computed(() => {
  if (!formData.value.name) return '请输入标签名称';
  if (formData.value.name.length > 20) return '标签名称不能超过20个字符';

  const isDuplicate = props.existingNames.some(
    name => name === formData.value.name && name !== props.tag?.name
  );
  if (isDuplicate) return '标签名称已存在';

  return '';
});

// 监听 tag 变化
watch(() => props.tag, (tag) => {
  if (tag) {
    formData.value = {
      name: tag.name,
      color: tag.color,
    };
  } else {
    formData.value = {
      name: '',
      color: '#FF6B6B',
    };
  }
}, { immediate: true });

// 重置表单
watch(() => props.open, (open) => {
  if (!open && !props.tag) {
    formData.value = {
      name: '',
      color: '#FF6B6B',
    };
  }
});

const handleSubmit = () => {
  if (nameError.value) return;
  emit('save', { ...formData.value });
};

const handleCancel = () => {
  isOpen.value = false;
};
</script>

<style scoped>
.tag-form {
  @apply space-y-6;
}

.color-picker {
  @apply flex flex-wrap gap-2;
}

.color-option {
  @apply w-10 h-10 rounded-full;
  @apply flex items-center justify-center;
  @apply transition-all duration-fast;
  @apply border-2 border-transparent;
  @apply hover:scale-110 focus:outline-none focus:ring-2 focus:ring-offset-2 focus:ring-primary-500;
}

.color-option.selected {
  @apply border-slate-800 dark:border-slate-200;
}

.color-check {
  @apply text-white;
  @apply drop-shadow-sm;
}

.custom-color {
  @apply flex items-center gap-3 mt-3 p-3;
  @apply bg-slate-50 rounded-lg;
}

.custom-color-label {
  @apply text-sm text-slate-600;
}

.color-input {
  @apply w-10 h-10 rounded cursor-pointer;
  @apply border-0 p-0;
}

.color-hex {
  @apply flex-1 px-3 py-2;
  @apply border border-slate-200 rounded-lg;
  @apply text-sm font-mono uppercase;
  @apply focus:outline-none focus:ring-2 focus:ring-primary-500;
}

.tag-preview {
  @apply p-4 bg-slate-50 rounded-lg;
}

.form-actions {
  @apply flex items-center justify-end gap-3 pt-4;
  @apply border-t border-slate-100;
}
</style>
