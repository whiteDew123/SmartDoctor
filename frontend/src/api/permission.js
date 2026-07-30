import request from '@/utils/request'

/**
 * 根据角色名获取权限树
 * @param {string} roleName 角色名：'1'管理员 '2'医生 '3'患者
 */
export function getPermissions(roleName) {
  return request({
    url: '/permissions',
    method: 'get',
    params: { roleName }
  })
}
