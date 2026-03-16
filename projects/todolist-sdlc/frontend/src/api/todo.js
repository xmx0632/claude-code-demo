import request from '@/utils/request'

export const todoApi = {
  // 获取任务列表
  getList(params) {
    return request.get('/todos', { params })
  },

  // 获取任务详情
  getDetail(id) {
    return request.get(`/todos/${id}`)
  },

  // 创建任务
  create(data) {
    return request.post('/todos', data)
  },

  // 更新任务
  update(id, data) {
    return request.put(`/todos/${id}`, data)
  },

  // 删除任务
  delete(id) {
    return request.delete(`/todos/${id}`)
  },

  // 完成任务
  complete(id) {
    return request.put(`/todos/${id}/complete`)
  },

  // 取消完成
  uncomplete(id) {
    return request.put(`/todos/${id}/uncomplete`)
  }
}
