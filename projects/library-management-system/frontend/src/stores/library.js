import { defineStore } from 'pinia'
import { ref } from 'vue'
import { getCategories } from '@/api/category'

export const useLibraryStore = defineStore('library', () => {
  // 分类列表
  const categories = ref([])

  // 加载分类列表
  const loadCategories = async () => {
    try {
      categories.value = await getCategories()
    } catch (error) {
      console.error('加载分类失败:', error)
    }
  }

  // 根据ID获取分类名称
  const getCategoryName = (id) => {
    const category = categories.value.find(c => c.id === id)
    return category ? category.name : '未分类'
  }

  return {
    categories,
    loadCategories,
    getCategoryName
  }
})
