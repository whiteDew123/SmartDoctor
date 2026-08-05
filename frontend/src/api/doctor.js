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

/**
 * 分页查询医师列表
 * @param {Object} params - { pn, size, keyword }
 */
export function getDoctorPage(params) {
  return request({
    url: '/doctors',
    method: 'get',
    params
  })
}

/**
 * 新增医师
 * @param {Object} data - { name, age, sex, hospital, levelId, phoneNumber, typeId, pwd }
 */
export function addDoctor(data) {
  return request({
    url: '/doctors',
    method: 'post',
    data
  })
}

/**
 * 修改医师
 * @param {Number} id
 * @param {Object} data - { name, age, sex, hospital, levelId, phoneNumber, typeId, accountId }
 */
export function updateDoctor(id, data) {
  return request({
    url: `/doctors/${id}`,
    method: 'put',
    data
  })
}

/**
 * 删除医师
 * @param {Number} id
 */
export function deleteDoctor(id) {
  return request({
    url: `/doctors/${id}`,
    method: 'delete'
  })
}

/**
 * 重置密码
 * @param {Number} id
 * @param {String} pwd - 新密码（Query参数）
 */
export function resetPassword(id, pwd) {
  return request({
    url: `/doctors/${id}/reset-password`,
    method: 'put',
    params: { pwd }
  })
}