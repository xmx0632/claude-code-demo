<template>
  <div class="tag-manager">
    <!-- 页面标题 -->
    <div class="page-header">
      <div>
        <h1 class="page-title">标签管理</h1>
        <p class="page-subtitle">管理您的任务标签，为任务添加灵活的分类方式</p>
      </div>
      <Button
        variant="primary"
        size="md"
        @click="openCreateModal"
      >
        <Plus :size="18" class="mr-2" />
        创建标签
      </Button>
    </div>

    <!-- 标签列表 -->
    <div v-if="tags.length > 0" class="tag-grid">
      <TagCard
        v-for="tag in tags"
        :key="tag.id"
        :tag="tag"
        @edit="openEditModal"
        @delete="confirmDelete"
      />
    </div>

    <!-- 空状态 -->
    <EmptyState
      v-else
      icon="Tag"
      title="还没有标签"
      description="创建您的第一个标签，开始更灵活地组织任务"
      action-text="创建标签"
      @action="openCreateModal"
    />

    <!-- 创建/编辑弹窗 -->
    <TagModal
      v-model:open="modalOpen"
      :tag="editingTag"
      :existing-names="tags.map(t => t.name)"
      @save="handleSave"
    />

    <!-- 删除确认弹窗 -->
    <ConfirmDialog
      v-model:open="deleteDialogOpen"
      :title="`删除标签「${deletingTag?.name}」`"
      :description="deletingTaskCount > 0
        ? `该标签正在被 ${deletingTaskCount} 个任务使用，删除后将自动移除这些任务上的标签。`
        : '确定要删除这个标签吗？'"
      confirm-text="删除"
      :variant="'danger'"
      @confirm="handleDelete"
    />
  </div>
</template>

<script setup lang="ts">
import { ref, computed } from 'vue';
import { Plus } from 'lucide-vue-next';
import Button from '@/components/ui/Button.vue';
import TagCard from '@/components/tags/TagCard.vue';
import TagModal from '@/components/tags/TagModal.vue';
import EmptyState from '@/components/ui/EmptyState.vue';
import ConfirmDialog from '@/components/ui/ConfirmDialog.vue';
import { useToast } from '@/composables/useToast';
import type { Tag } from '@/types';

const toast = useToast();

// 状态
const tags = ref<Tag[]>([
  { id: 1, name: '工作', color: '#FF6B6B', taskCount: 12, createdAt: '2026-03-15' },
  { id: 2, name: '个人', color: '#4ECDC4', taskCount: 8, createdAt: '2026-03-15' },
  { id: 3, name: '重要', color: '#45B7D1', taskCount: 5, createdAt: '2026-03-16' },
  { id: 4, name: '学习', color: '#F7DC6F', taskCount: 3, createdAt: '2026-03-16' },
]);

const modalOpen = ref(false);
const deleteDialogOpen = ref(false);
const editingTag = ref<Tag | null>(null);
const deletingTag = ref<Tag | null>(null);

// 计算属性
const deletingTaskCount = computed(() => deletingTag.value?.taskCount || 0);

// 方法
const openCreateModal = () => {
  editingTag.value = null;
  modalOpen.value = true;
};

const openEditModal = (tag: Tag) => {
  editingTag.value = tag;
  modalOpen.value = true;
};

const confirmDelete = (tag: Tag) => {
  deletingTag.value = tag;
  deleteDialogOpen.value = true;
};

const handleSave = async (data: { name: string; color: string }) => {
  try {
    if (editingTag.value) {
      // 编辑
      const index = tags.value.findIndex(t => t.id === editingTag.value!.id);
      if (index !== -1) {
        tags.value[index] = {
          ...tags.value[index],
          name: data.name,
          color: data.color,
        };
      }
      toast.success('标签已更新');
    } else {
      // 创建
      const newTag: Tag = {
        id: Date.now(),
        name: data.name,
        color: data.color,
        taskCount: 0,
        createdAt: new Date().toISOString().split('T')[0],
      };
      tags.value.unshift(newTag);
      toast.success('标签已创建');
    }
    modalOpen.value = false;
  } catch (error) {
    toast.error('操作失败，请重试');
  }
};

const handleDelete = () => {
  if (deletingTag.value) {
    tags.value = tags.value.filter(t => t.id !== deletingTag.value!.id);
    toast.success('标签已删除');
    deleteDialogOpen.value = false;
    deletingTag.value = null;
  }
};
</script>

<style scoped>
.tag-manager {
  @apply max-w-6xl mx-auto p-6;
}

.page-header {
  @apply flex items-start justify-between mb-8;
}

.page-title {
  @apply text-2xl font-semibold text-slate-900 mb-1;
}

.page-subtitle {
  @apply text-sm text-slate-500;
}

.tag-grid {
  @apply grid grid-cols-1 sm:grid-cols-2 lg:grid-cols-3 gap-4;
}

@media (min-width: 768px) {
  .tag-manager {
    @apply p-8;
  }
}
</style>
