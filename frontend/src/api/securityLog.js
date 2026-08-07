import request from '@/utils/request'

const BASE = '/security-log'

/** 分页查询安全日志 */
export function getSecurityLogs(params) {
  return request({
    url: BASE,
    method: 'get',
    params
  })
}

/** 删除单条日志 */
export function deleteSecurityLog(id) {
  return request({
    url: BASE + '/' + id,
    method: 'delete'
  })
}

/** 批量删除日志 */
export function batchDeleteSecurityLog(ids) {
  return request({
    url: BASE + '/batch-delete',
    method: 'post',
    data: ids
  })
}

/** 清理指定日期之前的日志 */
export function cleanSecurityLog(beforeDate) {
  return request({
    url: BASE + '/clean',
    method: 'post',
    data: { beforeDate }
  })
}
