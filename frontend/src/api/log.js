import request from '@/utils/request'

export function getLogs(params) {
  return request({
    url: '/logs',
    method: 'get',
    params
  })
}

export function getLogDetail(id) {
  return request({
    url: `/logs/${id}`,
    method: 'get'
  })
}