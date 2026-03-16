import request from '@/utils/request'

export const tagApi = {
  // 获取所有标签
  getAll() {
    return request.get('/tags/all')
  },

  // 分页查询标签
  getPage(query) {
    return request.get('/tags', { params: query })
  },

  // 获取标签详情
  getDetail(id) {
    return request.get(`/tags/${id}`)
  },

  // 创建标签
  create(data) {
    return request.post('/tags', data)
  },

  // 更新标签
  update(id, data) {
    return request.put(`/tags/${id}`, data)
  },

  // 删除标签
  delete(id) {
    return request.delete(`/tags/${id}`)
  }
}
