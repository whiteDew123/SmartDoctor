import request from '@/utils/request'

/**
 * 分页查询医药公司
 * @param {Object} params - { pageNum, pageSize, companyName, companyPhone }
 */
export function getCompanyPage(params) {
  return request({
    url: '/companies/page',
    method: 'get',
    params
  })
}

/**
 * 查询医药公司详情
 * @param {Number} companyId
 */
export function getCompanyDetail(companyId) {
  return request({
    url: `/companies/${companyId}`,
    method: 'get'
  })
}

/**
 * 新增医药公司
 * @param {Object} data - { companyName, companyPhone }
 */
export function addCompany(data) {
  return request({
    url: '/companies',
    method: 'post',
    data
  })
}

/**
 * 修改医药公司
 * @param {Number} companyId
 * @param {Object} data - { companyName, companyPhone }
 */
export function updateCompany(companyId, data) {
  return request({
    url: `/companies/${companyId}`,
    method: 'put',
    data
  })
}

/**
 * 删除医药公司
 * @param {Number} companyId
 */
export function deleteCompany(companyId) {
  return request({
    url: `/companies/${companyId}`,
    method: 'delete'
  })
}