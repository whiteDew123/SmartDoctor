import request from '@/utils/request'

export function getCompanyPolicyPage(pageNum, pageSize, params = {}) {
  return request({
    url: '/companyPolicy/list',
    method: 'get',
    params: {
      pageNum,
      pageSize,
      ...params
    }
  })
}

export function getCompanyPolicyById(id) {
  return request({
    url: `/companyPolicy/${id}`,
    method: 'get'
  })
}

export function addCompanyPolicy(data) {
  return request({
    url: '/companyPolicy/add',
    method: 'post',
    data
  })
}

export function updateCompanyPolicy(data) {
  return request({
    url: '/companyPolicy/update',
    method: 'put',
    data
  })
}

export function deleteCompanyPolicy(id) {
  return request({
    url: `/companyPolicy/${id}`,
    method: 'delete'
  })
}

export function getCompanyList(pageNum = 1, pageSize = 1000) {
  return request({
    url: '/companies/page',
    method: 'get',
    params: { pageNum, pageSize }
  })
}