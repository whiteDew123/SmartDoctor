import request from '@/utils/request'

// 获取医师级别列表（注册页下拉用）
export function getDoctorLevels() {
  return request({
    url: '/doctors/levels',
    method: 'get'
  })
}

// 获取诊治类型列表（注册页下拉用）
export function getTreatTypes() {
  return request({
    url: '/doctors/treat-types',
    method: 'get'
  })
}
