import { defineStore } from 'pinia'
import { ref } from 'vue'
import router, { resetRouter, getStaticChildPaths } from '@/router'
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
   * 即使后端接口异常，也会标记为已加载并依赖静态兜底路由，避免无限重定向。
   * 动态路由中若有与静态路由路径冲突的，跳过以保留静态路由的权限配置。
   * @param {string} roleName 角色名（'1'/'2'/'3'）
   */
  const loadPermissionRoutes = async (roleName) => {
    if (!roleName) return
    if (routesLoaded.value) return

    try {
      const res = await getPermissions(roleName)
      const permissions = res.data?.permissions || []
      const routerList = generateRoutes(permissions)
      const menuList = generateMenus(permissions)

      resetRouter()

      // 获取静态路由的路径集合，防止动态路由覆盖静态路由的 meta（如 roles 权限配置）
      const staticPaths = getStaticChildPaths()

      routerList.forEach(route => {
        try {
          // 检查路由路径是否与静态路由冲突，如果冲突则跳过
          let fullPath = route.path
          if (!fullPath.startsWith('/')) fullPath = '/' + fullPath
          
          if (staticPaths.has(fullPath)) {
            console.warn(`[loadPermissionRoutes] 跳过与静态路由冲突的动态路由: ${fullPath}`)
            return
          }
          
          router.addRoute('Layout', route)
        } catch (e) {
          console.warn('[loadPermissionRoutes] 添加路由失败:', route.name, e)
        }
      })

      dynamicRoutes.value = routerList
      menus.value = menuList
    } catch (e) {
      console.warn('[loadPermissionRoutes] 权限接口不可用，使用静态兜底路由:', e)
      dynamicRoutes.value = []
      menus.value = []
    } finally {
      routesLoaded.value = true
    }
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
