import request from './index'

/**
 * 获取图书列表
 */
export function getBooks(params) {
  return request({
    url: '/books',
    method: 'get',
    params
  })
}

/**
 * 搜索图书
 */
export function searchBooks(params) {
  return request({
    url: '/books/search',
    method: 'get',
    params
  })
}

/**
 * 获取图书详情
 */
export function getBook(id) {
  return request({
    url: `/books/${id}`,
    method: 'get'
  })
}

/**
 * 新增图书
 */
export function createBook(data) {
  return request({
    url: '/books',
    method: 'post',
    data
  })
}

/**
 * 更新图书
 */
export function updateBook(id, data) {
  return request({
    url: `/books/${id}`,
    method: 'put',
    data
  })
}

/**
 * 删除图书
 */
export function deleteBook(id) {
  return request({
    url: `/books/${id}`,
    method: 'delete'
  })
}

/**
 * 入库
 */
export function stockIn(id, data) {
  return request({
    url: `/books/${id}/stock-in`,
    method: 'post',
    data
  })
}

/**
 * 出库
 */
export function stockOut(id, data) {
  return request({
    url: `/books/${id}/stock-out`,
    method: 'post',
    data
  })
}
