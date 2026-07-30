import request from '@/utils/request'

export function login(data) {
  return request({
    url: '/login',
    method: 'post',
    data
  })
}

export function register(data) {
  return request({
    url: '/account/register',
    method: 'post',
    data
  })
}

export function forgotPassword(data) {
  return request({
    url: '/account/forgot-password',
    method: 'post',
    data
  })
}
