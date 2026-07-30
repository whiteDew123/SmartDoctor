import { defineStore } from 'pinia'
import { ref } from 'vue'
import router, { resetRouter } from '@/router'
import { getPermissions } from '@/api/permission'
import { generateRoutes, generateMenus } from '@/utils/route'

/**
 * 应用级状态：动态路由、菜单树、加载状态。
 * - 路由树：登录后通过 loadPermissionRoutes 拉取并动态 addRoute('Layout', route)
 * - 菜单树：侧边栏直接渲染 menus
 */
export const useAppStore = defineStore('app', () => {
  const routesLoaded = ref(false)
  const dynamicRoutes = ref([])
  const menus = ref([])

  /**
   * 加载权限路由并注入 router。
   * 若已加载则直接返回。
   * @param {string} roleName 角色名（'1'/'2'/'3'）
   */
  const loadPermissionRoutes = async (roleName) => {
    if (!roleName) return
    if (routesLoaded.value) return

    const res = await getPermissions(roleName)
    const permissions = res.data?.permissions || []
    const routerList = generateRoutes(permissions)
    const menuList = generateMenus(permissions)

    // 先清理旧的动态路由（保留 Login / Layout / Home 三个静态兜底路由）
    resetRouter()

    // 将后端返回的路由逐个挂载为 Layout 的子路由
    // Vue Router 4 支持同名覆盖，若后端也返回 Home，会覆盖静态 Home，组件相同无感知
    routerList.forEach(route => {
      try {
        router.addRoute('Layout', route)
      } catch (e) {
        console.warn('[loadPermissionRoutes] 添加路由失败:', route.name, e)
      }
    })

    dynamicRoutes.value = routerList
    menus.value = menuList
    routesLoaded.value = true
  }

  const clearPermissionRoutes = () => {
    routesLoaded.value = false
    dynamicRoutes.value = []
    menus.value = []
    resetRouter()
  }

  return {
    routesLoaded,
    dynamicRoutes,
    menus,
    loadPermissionRoutes,
    clearPermissionRoutes
  }
})
