import request from '@/utils/request'

// 登录
export function login(data) {
  return request({
    url: '/account/login',
    method: 'post',
    data
  })
}

// 注册
export function register(data) {
  return request({
    url: '/account/register',
    method: 'post',
    data
  })
}

// 获取用户信息
export function getAccount(id) {
  return request({
    url: `/account/${id}`,
    method: 'get'
  })
}