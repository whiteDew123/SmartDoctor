import request from '@/utils/request'

export function getDrugPage(pageNum, pageSize) {
  return request({
    url: `/drugs/${pageNum}/${pageSize}`,
    method: 'get'
  })
}

export function addDrug(data) {
  return request({
    url: '/drugs',
    method: 'post',
    data
  })
}

export function updateDrug(drugId, data) {
  return request({
    url: `/drugs/${drugId}`,
    method: 'put',
    data
  })
}

export function deleteDrug(drugId) {
  return request({
    url: `/drugs/${drugId}`,
    method: 'delete'
  })
}
