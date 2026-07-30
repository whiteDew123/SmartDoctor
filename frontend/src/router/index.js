import { createRouter, createWebHistory } from 'vue-router'
import { useUserStore } from '@/store/user'
import { useAppStore } from '@/store/modules/app'

/**
 * 基础静态路由：登录页 + Layout（含首页兜底）。
 * Layout 始终存在，保证刷新或直接访问 / 时不出现 "No match"。
 * 动态权限路由通过 addRoute('Layout', route) 注入为子路由。
 */
const constantRoutes = [
  {
    path: '/login',
    name: 'Login',
    component: () => import('@/views/Login.vue'),
    meta: { title: '登录' }
  },
  {
    path: '/',
    name: 'Layout',
    component: () => import('@/layout/index.vue'),
    redirect: '/home',
    children: [
      {
        path: 'home',
        name: 'Home',
        component: () => import('@/views/home/index.vue'),
        meta: { title: '首页' }
      }
    ]
  }
]

const router = createRouter({
  history: createWebHistory(),
  routes: constantRoutes
})

/**
 * 重置路由：退出登录或需要清空动态路由时调用。
 * Vue Router 4 不暴露 matcher，需遍历 getRoutes() 逐个 removeRoute。
 */
export function resetRouter() {
  router.getRoutes().forEach(route => {
    if (route.name && !['Login', 'Layout', 'Home'].includes(route.name)) {
      router.removeRoute(route.name)
    }
  })
}

/**
 * 路由守卫：
 * - 未登录访问非 /login → 强制跳登录
 * - 已登录访问 /login → 跳首页
 * - 已登录且动态路由未加载 → 调接口加载后重走守卫
 * - token 存在但取不到 utype（用户信息损坏/旧 token）→ 强制退出并跳转登录
 */
router.beforeEach(async (to, from, next) => {
  const token = localStorage.getItem('token')

  // 未登录
  if (!token) {
    if (to.path === '/login') return next()
    return next('/login')
  }

  // 已登录访问登录页 → 去首页
  if (to.path === '/login') return next('/')

  try {
    const userStore = useUserStore()
    const appStore = useAppStore()

    if (!appStore.routesLoaded) {
      const roleName = userStore.userInfo?.utype
      if (roleName) {
        await appStore.loadPermissionRoutes(String(roleName))
        // 动态路由已注入，用完整 location 对象重走一次守卫，确保新路由被正确匹配
        return next({ ...to, replace: true })
      }
      // 有 token 但取不到角色信息：localStorage 数据可能已损坏，强制重新登录
      userStore.logout()
      return next('/login')
    }

    next()
  } catch (e) {
    console.error('[router guard] 加载动态路由失败：', e)
    // 加载异常时仍放行，至少能看到 Layout + 兜底首页，避免白屏
    next()
  }
})

export default router
