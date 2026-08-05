import request from '@/utils/request'

export function getCityList() {
  return request({
    url: '/city/list',
    method: 'get'
  })
}

export function getCityById(cityId) {
  return request({
    url: `/city/${cityId}`,
    method: 'get'
  })
}

export function addCity(data) {
  return request({
    url: '/city/add',
    method: 'post',
    data
  })
}

export function deleteCity(cityId) {
  return request({
    url: `/city/${cityId}`,
    method: 'delete'
  })
}