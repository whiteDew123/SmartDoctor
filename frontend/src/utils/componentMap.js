/**
 * 前端组件映射表（Vite 的 import.meta.glob 不能用拼接字符串作为 key），
 * 后端 permission.component 字段 -> 对应 Vite 动态 import 函数
 *
 * 按数据库 permission 表的 component 字段（Home/PolicyList/Doctor 等）建立一一映射。
 * 若新增 component 类型，只需要在这里添加条目。
 */
const componentMap = {
  Home: () => import('@/views/home/index.vue'),
  Policy: () => import('@/views/policy/index.vue'),
  PolicyList: () => import('@/views/policy/list.vue'),
  PolicyDetail: () => import('@/views/policy/detail.vue'),
  Drug: () => import('@/views/drug/index.vue'),
  DrugList: () => import('@/views/drug/list.vue'),
  DrugCompany: () => import('@/views/drug/company.vue'),
  Doctor: () => import('@/views/doctor/index.vue'),
  Patient: () => import('@/views/patient/index.vue'),
  System: () => import('@/views/system/index.vue'),
  Account: () => import('@/views/system/account.vue'),
  Role: () => import('@/views/system/role.vue'),
  SaleLocation: () => import('@/views/sale/location.vue'),
  City: () => import('@/views/city/index.vue'),
  CompanyPolicy: () => import('@/views/company/policy.vue'),
  Material: () => import('@/views/material/index.vue'),
  SecurityLog: () => import('@/views/log/index.vue')
}

export function getComponent(name) {
  return componentMap[name] || (() => import('@/views/NotFound.vue'))
}
