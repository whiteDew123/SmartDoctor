import request from '@/utils/request'

/**
 * 分页查询医保政策列表
 * @param {Object} params - { pageNum, pageSize, title, cityId }
 */
export function getPolicyPage(params) {
  return request({
    url: '/medical-policy/page',
    method: 'get',
    params
  })
}

/**
 * 根据 ID 查询医保政策详情
 * @param {Number} id
 */
export function getPolicyDetail(id) {
  return request({
    url: `/medical-policy/${id}`,
    method: 'get'
  })
}

/**
 * 新增医保政策
 * @param {Object} data - { title, message, cityId }
 */
export function addPolicy(data) {
  return request({
    url: '/medical-policy',
    method: 'post',
    data
  })
}

/**
 * 修改医保政策
 * @param {Number} id
 * @param {Object} data - { title, message, cityId }
 */
export function updatePolicy(id, data) {
  return request({
    url: `/medical-policy/${id}`,
    method: 'put',
    data
  })
}

/**
 * 删除医保政策
 * @param {Number} id
 */
export function deletePolicy(id) {
  return request({
    url: `/medical-policy/${id}`,
    method: 'delete'
  })
}

/**
 * 查询所有城市选项（含城市名称，用于下拉选择）
 */
export function getCityOptions() {
  return request({
    url: '/medical-policy/cities',
    method: 'get'
  })
}
