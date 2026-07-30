import request from '@/utils/request'

export function getMaterialPage(pageNum, pageSize) {
  return request({
    url: `/materials/${pageNum}/${pageSize}`,
    method: 'get'
  })
}

export function searchMaterial(pageNum, pageSize, keyword) {
  return request({
    url: `/materials/search/${pageNum}/${pageSize}`,
    method: 'get',
    params: { keyword }
  })
}

export function addMaterial(data) {
  return request({
    url: '/materials/add',
    method: 'post',
    data
  })
}

export function updateMaterial(data) {
  return request({
    url: '/materials/update',
    method: 'put',
    data
  })
}

export function deleteMaterial(id) {
  return request({
    url: `/materials/delete/${id}`,
    method: 'delete'
  })
}
