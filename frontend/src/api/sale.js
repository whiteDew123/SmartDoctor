import request from '@/utils/request'

/**
 * 分页查询销售地点
 * @param {Object} params - { pageNum, pageSize, saleName, salePhone }
 */
export function getSaleList(params) {
  return request({
    url: '/sale/page',
    method: 'get',
    params
  })
}

/**
 * 新增销售地点
 * @param {Object} data - { saleName, salePhone }
 */
export function addSale(data) {
  return request({
    url: '/sale',
    method: 'post',
    data
  })
}

/**
 * 修改销售地点
 * @param {Number} id - 销售地点ID
 * @param {Object} data - { saleName, salePhone }
 */
export function updateSale(id, data) {
  return request({
    url: `/sale/${id}`,
    method: 'put',
    data
  })
}

/**
 * 删除销售地点
 * @param {Number} id - 销售地点ID
 */
export function deleteSale(id) {
  return request({
    url: `/sale/${id}`,
    method: 'delete'
  })
}
