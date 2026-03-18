import request from './index'

/**
 * 获取所有分类
 */
export function getCategories() {
  return request({
    url: '/categories',
    method: 'get'
  })
}

/**
 * 获取分类详情
 */
export function getCategory(id) {
  return request({
    url: `/categories/${id}`,
    method: 'get'
  })
}

/**
 * 新增分类
 */
export function createCategory(data) {
  return request({
    url: '/categories',
    method: 'post',
    data
  })
}

/**
 * 更新分类
 */
export function updateCategory(id, data) {
  return request({
    url: `/categories/${id}`,
    method: 'put',
    data
  })
}

/**
 * 删除分类
 */
export function deleteCategory(id) {
  return request({
    url: `/categories/${id}`,
    method: 'delete'
  })
}
