import { defineStore } from 'pinia'
import { ref } from 'vue'
import { todoApi } from '@/api/todo'
import { ElMessage } from 'element-plus'

export const useTodoStore = defineStore('todo', () => {
  const todoList = ref([])
  const total = ref(0)
  const loading = ref(false)
  const currentStatus = ref(null)
  const queryParams = ref({
    page: 1,
    size: 10,
    status: null,
    priority: null,
    categoryId: null,
    keyword: '',
    tagIds: []
  })
  const stats = ref({
    all: 0,
    pending: 0,
    inProgress: 0,
    completed: 0
  })

  // 获取任务列表
  async function fetchTodoList() {
    loading.value = true
    try {
      const res = await todoApi.getList({
        ...queryParams.value,
        status: currentStatus.value
      })
      todoList.value = res.data.records
      total.value = res.data.total
      await fetchStats()
    } catch (error) {
      ElMessage.error(error.message || '获取任务列表失败')
    } finally {
      loading.value = false
    }
  }

  // 获取统计数据
  async function fetchStats() {
    try {
      const res = await todoApi.getStats()
      stats.value = res.data || {
        all: 0,
        pending: 0,
        inProgress: 0,
        completed: 0
      }
    } catch (error) {
      console.error('获取统计数据失败:', error)
    }
  }

  // 创建任务
  async function createTodo(todoData) {
    try {
      await todoApi.create(todoData)
      ElMessage.success('创建成功')
      await fetchTodoList()
    } catch (error) {
      ElMessage.error(error.message || '创建失败')
      throw error
    }
  }

  // 更新任务
  async function updateTodo(id, todoData) {
    try {
      await todoApi.update(id, todoData)
      ElMessage.success('更新成功')
      await fetchTodoList()
    } catch (error) {
      ElMessage.error(error.message || '更新失败')
      throw error
    }
  }

  // 删除任务
  async function deleteTodo(id) {
    try {
      await todoApi.delete(id)
      ElMessage.success('删除成功')
      await fetchTodoList()
    } catch (error) {
      ElMessage.error(error.message || '删除失败')
      throw error
    }
  }

  // 完成任务
  async function completeTodo(id) {
    try {
      await todoApi.complete(id)
      ElMessage.success('操作成功')
      await fetchTodoList()
    } catch (error) {
      ElMessage.error(error.message || '操作失败')
      throw error
    }
  }

  // 取消完成
  async function uncompleteTodo(id) {
    try {
      await todoApi.uncomplete(id)
      ElMessage.success('操作成功')
      await fetchTodoList()
    } catch (error) {
      ElMessage.error(error.message || '操作失败')
      throw error
    }
  }

  // 切换状态筛选
  function setStatusFilter(status) {
    currentStatus.value = status
    queryParams.value.page = 1
    fetchTodoList()
  }

  // 分页
  function setPage(page) {
    queryParams.value.page = page
    fetchTodoList()
  }

  return {
    todoList,
    total,
    loading,
    currentStatus,
    queryParams,
    stats,
    fetchTodoList,
    fetchStats,
    createTodo,
    updateTodo,
    deleteTodo,
    completeTodo,
    uncompleteTodo,
    setStatusFilter,
    setPage
  }
})
