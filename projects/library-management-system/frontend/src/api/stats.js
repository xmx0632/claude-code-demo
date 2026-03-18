import request from './index'

/**
 * 获取统计概览
 */
export function getSummary() {
  return request({
    url: '/stats/summary',
    method: 'get'
  })
}

/**
 * 获取低库存图书
 */
export function getLowStockBooks() {
  return request({
    url: '/stats/low-stock',
    method: 'get'
  })
}
