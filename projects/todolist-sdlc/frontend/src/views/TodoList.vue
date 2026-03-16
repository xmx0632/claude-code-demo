<template>
  <div class="todo-container">
    <!-- 状态标签页 -->
    <div class="status-tabs">
      <el-radio-group v-model="activeStatus" @change="handleStatusChange">
        <el-radio-button :value="null">全部 ({{ stats.all }})</el-radio-button>
        <el-radio-button :value="0">待办 ({{ stats.pending }})</el-radio-button>
        <el-radio-button :value="1">进行中 ({{ stats.inProgress }})</el-radio-button>
        <el-radio-button :value="2">已完成 ({{ stats.completed }})</el-radio-button>
      </el-radio-group>
    </div>

    <!-- 工具栏 -->
    <div class="toolbar">
      <el-input
        v-model="searchKeyword"
        placeholder="搜索任务..."
        prefix-icon="Search"
        clearable
        class="search-input"
        @clear="handleSearch"
        @keyup.enter="handleSearch"
      />
      <el-button type="primary" icon="Plus" @click="showAddDialog">
        新建任务
      </el-button>
    </div>

    <!-- 任务列表 -->
    <div class="todo-list" v-loading="todoStore.loading">
      <el-empty v-if="todoStore.todoList.length === 0" description="暂无任务" />
      <div
        v-for="todo in todoStore.todoList"
        :key="todo.id"
        class="todo-item"
        :class="{ completed: todo.status === 2 }"
      >
        <div class="todo-checkbox">
          <el-checkbox
            :model-value="todo.status === 2"
            @change="handleToggleComplete(todo)"
          />
        </div>
        <div class="todo-content">
          <div class="todo-title">{{ todo.title }}</div>
          <div class="todo-meta">
            <el-tag
              v-if="todo.priority !== undefined"
              :type="getPriorityType(todo.priority)"
              size="small"
            >
              {{ getPriorityText(todo.priority) }}
            </el-tag>
            <el-tag
              v-if="todo.categoryName"
              :color="todo.categoryColor"
              size="small"
              effect="light"
            >
              {{ todo.categoryName }}
            </el-tag>
            <span v-if="todo.dueDate" class="due-date">
              截止: {{ formatDate(todo.dueDate) }}
            </span>
          </div>
        </div>
        <div class="todo-actions">
          <el-button link type="primary" @click="handleEdit(todo)">
            <el-icon><Edit /></el-icon>
          </el-button>
          <el-popconfirm title="确定删除?" @confirm="handleDelete(todo.id)">
            <template #reference>
              <el-button link type="danger">
                <el-icon><Delete /></el-icon>
              </el-button>
            </template>
          </el-popconfirm>
        </div>
      </div>
    </div>

    <!-- 分页 -->
    <div class="pagination" v-if="todoStore.total > 0">
      <el-pagination
        v-model:current-page="currentPage"
        :page-size="10"
        :total="todoStore.total"
        layout="prev, pager, next"
        @current-change="handlePageChange"
      />
    </div>

    <!-- 新建/编辑对话框 -->
    <el-dialog
      v-model="dialogVisible"
      :title="isEdit ? '编辑任务' : '新建任务'"
      width="500px"
    >
      <el-form ref="formRef" :model="formData" :rules="formRules" label-width="80px">
        <el-form-item label="标题" prop="title">
          <el-input v-model="formData.title" placeholder="请输入任务标题" />
        </el-form-item>
        <el-form-item label="描述">
          <el-input
            v-model="formData.description"
            type="textarea"
            :rows="3"
            placeholder="请输入任务描述"
          />
        </el-form-item>
        <el-form-item label="优先级">
          <el-select v-model="formData.priority" placeholder="请选择优先级">
            <el-option label="低" :value="0" />
            <el-option label="中" :value="1" />
            <el-option label="高" :value="2" />
          </el-select>
        </el-form-item>
        <el-form-item label="截止日期">
          <el-date-picker
            v-model="formData.dueDate"
            type="date"
            placeholder="选择截止日期"
            value-format="YYYY-MM-DD"
          />
        </el-form-item>
      </el-form>
      <template #footer>
        <el-button @click="dialogVisible = false">取消</el-button>
        <el-button type="primary" :loading="submitting" @click="handleSubmit">
          确定
        </el-button>
      </template>
    </el-dialog>
  </div>
</template>

<script setup>
import { ref, reactive, computed, onMounted } from 'vue'
import { useTodoStore } from '@/stores/todo'
import { formatDate } from '@/utils/format'

const todoStore = useTodoStore()

const activeStatus = ref(null)
const searchKeyword = ref('')
const currentPage = ref(1)
const dialogVisible = ref(false)
const isEdit = ref(false)
const submitting = ref(false)
const editId = ref(null)
const formRef = ref()

const formData = reactive({
  title: '',
  description: '',
  priority: 1,
  categoryId: null,
  dueDate: ''
})

const formRules = {
  title: [{ required: true, message: '请输入标题', trigger: 'blur' }]
}

// 统计数据
const stats = computed(() => {
  const list = todoStore.todoList
  return {
    all: todoStore.total,
    pending: list.filter(t => t.status === 0).length,
    inProgress: list.filter(t => t.status === 1).length,
    completed: list.filter(t => t.status === 2).length
  }
})

onMounted(() => {
  todoStore.fetchTodoList()
})

function handleStatusChange() {
  todoStore.setStatusFilter(activeStatus.value)
}

function handleSearch() {
  todoStore.queryParams.keyword = searchKeyword.value
  todoStore.fetchTodoList()
}

function handlePageChange(page) {
  todoStore.setPage(page)
}

function showAddDialog() {
  isEdit.value = false
  editId.value = null
  Object.assign(formData, {
    title: '',
    description: '',
    priority: 1,
    categoryId: null,
    dueDate: ''
  })
  dialogVisible.value = true
}

function handleEdit(todo) {
  isEdit.value = true
  editId.value = todo.id
  Object.assign(formData, {
    title: todo.title,
    description: todo.description,
    priority: todo.priority,
    categoryId: todo.categoryId,
    dueDate: todo.dueDate
  })
  dialogVisible.value = true
}

async function handleSubmit() {
  await formRef.value.validate()
  submitting.value = true
  try {
    if (isEdit.value) {
      await todoStore.updateTodo(editId.value, formData)
    } else {
      await todoStore.createTodo(formData)
    }
    dialogVisible.value = false
  } finally {
    submitting.value = false
  }
}

async function handleToggleComplete(todo) {
  if (todo.status === 2) {
    await todoStore.uncompleteTodo(todo.id)
  } else {
    await todoStore.completeTodo(todo.id)
  }
}

async function handleDelete(id) {
  await todoStore.deleteTodo(id)
}

function getPriorityType(priority) {
  const types = { 0: 'info', 1: 'warning', 2: 'danger' }
  return types[priority] || 'info'
}

function getPriorityText(priority) {
  const texts = { 0: '低', 1: '中', 2: '高' }
  return texts[priority] || '中'
}
</script>

<style scoped>
.todo-container {
  background: #fff;
  border-radius: 8px;
  padding: 24px;
}

.status-tabs {
  margin-bottom: 20px;
}

.toolbar {
  display: flex;
  gap: 16px;
  margin-bottom: 20px;
}

.search-input {
  width: 300px;
}

.todo-list {
  min-height: 200px;
}

.todo-item {
  display: flex;
  align-items: center;
  padding: 16px;
  border-bottom: 1px solid #eee;
  transition: background 0.2s;
}

.todo-item:hover {
  background: #f9f9f9;
}

.todo-item.completed .todo-title {
  text-decoration: line-through;
  color: #999;
}

.todo-checkbox {
  margin-right: 12px;
}

.todo-content {
  flex: 1;
}

.todo-title {
  font-size: 16px;
  margin-bottom: 8px;
}

.todo-meta {
  display: flex;
  align-items: center;
  gap: 8px;
}

.due-date {
  color: #999;
  font-size: 12px;
}

.todo-actions {
  display: flex;
  gap: 8px;
}

.pagination {
  margin-top: 20px;
  display: flex;
  justify-content: center;
}
</style>
