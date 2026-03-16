import { defineStore } from 'pinia'
import { ref } from 'vue'
import { tagApi } from '@/api/tag'

export const useTagStore = defineStore('tag', () => {
  const tagList = ref([])
  const loading = ref(false)

  // 获取标签列表
  async function fetchTagList() {
    loading.value = true
    try {
      const res = await tagApi.getAll()
      tagList.value = res.data || []
    } catch (error) {
      console.error('获取标签列表失败:', error)
    } finally {
      loading.value = false
    }
  }

  // 创建标签
  async function createTag(data) {
    const res = await tagApi.create(data)
    await fetchTagList()
    return res
  }

  // 更新标签
  async function updateTag(id, data) {
    const res = await tagApi.update(id, data)
    await fetchTagList()
    return res
  }

  // 删除标签
  async function deleteTag(id) {
    const res = await tagApi.delete(id)
    await fetchTagList()
    return res
  }

  return {
    tagList,
    loading,
    fetchTagList,
    createTag,
    updateTag,
    deleteTag
  }
})
