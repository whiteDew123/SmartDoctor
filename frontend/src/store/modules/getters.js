import { defineStore } from 'pinia'
import { computed } from 'vue'
import { useAppStore } from './app'
import { useUserStore } from '@/store/user'
import { useDrugStore } from './drug'
import { useCompanyPolicyStore } from './companyPolicy'
import { useCityStore } from './city'

/**
 * 集中式 getters 入口。
 * 其他模块的派生状态统一从这里获取，避免组件散落访问 store 私有字段。
 */
export const useGettersStore = defineStore('getters', () => {
  const appStore = useAppStore()
  const userStore = useUserStore()
  const drugStore = useDrugStore()
  const companyPolicyStore = useCompanyPolicyStore()
  const cityStore = useCityStore()

  const routes = computed(() => appStore.dynamicRoutes)
  const menus = computed(() => appStore.menus)
  const routesLoaded = computed(() => appStore.routesLoaded)

  const token = computed(() => userStore.token)
  const userInfo = computed(() => userStore.userInfo)
  const userName = computed(() => userStore.userInfo?.realname || '未登录')
  const userRole = computed(() => userStore.userInfo?.utype || '')

  /**
   * 返回当前用户对应的角色名（用于展示、传后端 roleName）
   */
  const roleName = computed(() => userStore.userInfo?.utype || '')

  const drugList = computed(() => drugStore.drugList)
  const drugTotal = computed(() => drugStore.total)
  const saleList = computed(() => drugStore.saleList)

  const policyList = computed(() => companyPolicyStore.policyList)
  const policyTotal = computed(() => companyPolicyStore.total)
  const companyList = computed(() => companyPolicyStore.companyList)

  const cityList = computed(() => cityStore.cityList)
  const cityTotal = computed(() => cityStore.total)

  return {
    routes,
    menus,
    routesLoaded,
    token,
    userInfo,
    userName,
    userRole,
    roleName,
    drugList,
    drugTotal,
    saleList,
    policyList,
    policyTotal,
    companyList,
    cityList,
    cityTotal
  }
})