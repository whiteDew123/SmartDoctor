import { createRouter, createWebHistory } from 'vue-router'
import { ElMessage } from 'element-plus'
import { useUserStore } from '@/store/user'
import { useAppStore } from '@/store/modules/app'

const STATIC_NAMES = [
  'Login', 'Layout', 'Home',
  'DrugCompany', 'SaleLocation', 'City', 'DrugList',
  'PolicyList', 'CompanyPolicy', 'Doctor', 'Material',
  'SecurityLog', 'NotFound'
]

/**
 * 基础静态路由：登录页 + Layout + 所有页面兜底。
 * 所有子页面都作为 Layout 的静态 children 存在，保证即使后端/权限接口异常也不会白屏。
 * 动态权限路由通过 addRoute('Layout', route) 同名覆盖静态路由。
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
      },
      {
        path: 'drug/company',
        name: 'DrugCompany',
        component: () => import('@/views/drug/company.vue'),
        meta: { title: '医药公司管理' }
      },
      {
        path: 'sale/location',
        name: 'SaleLocation',
        component: () => import('@/views/sale/location.vue'),
        meta: { title: '销售地点管理' }
      },
      {
        path: 'city',
        name: 'City',
        component: () => import('@/views/city/index.vue'),
        meta: { title: '城市信息管理' }
      },
      {
        path: 'drug/list',
        name: 'DrugList',
        component: () => import('@/views/drug/list.vue'),
        meta: { title: '药品信息管理', roles: ['1', '2'] }
      },
      {
        path: 'drug/index',
        redirect: '/drug/list'
      },
      {
        path: 'drug',
        redirect: '/drug/list'
      },
      {
        path: 'policy/list',
        name: 'PolicyList',
        component: () => import('@/views/policy/list.vue'),
        meta: { title: '医保政策管理' }
      },
      {
        path: 'company/policy',
        name: 'CompanyPolicy',
        component: () => import('@/views/company/policy.vue'),
        meta: { title: '医药公司政策管理' }
      },
      {
        path: 'doctor',
        name: 'Doctor',
        component: () => import('@/views/doctor/index.vue'),
        meta: { title: '医生信息管理' }
      },
      {
        path: 'material',
        name: 'Material',
        component: () => import('@/views/material/index.vue'),
        meta: { title: '必备材料管理', roles: ['1', '2'] }
      },
      {
        path: 'security-log',
        name: 'SecurityLog',
        component: () => import('@/views/system/securityLog.vue'),
        meta: { title: '安全日志', roles: ['1'] }
      }
    ]
  },
  {
    path: '/:pathMatch(.*)*',
    name: 'NotFound',
    component: () => import('@/views/NotFound.vue')
  }
]

/**
 * 获取所有静态子路由的完整路径（用于防止动态路由覆盖静态路由）
 */
export function getStaticChildPaths() {
  const paths = new Set()
  constantRoutes.forEach(route => {
    if (route.path === '/' && route.children) {
      route.children.forEach(child => {
        if (child.path && !child.redirect) {
          paths.add(`/${child.path}`)
        }
      })
    }
  })
  return paths
}

const router = createRouter({
  history: createWebHistory(),
  routes: constantRoutes
})

/**
 * 重置路由：只删除动态注入的路由，保留静态兜底路由。
 */
export function resetRouter() {
  router.getRoutes().forEach(route => {
    if (route.name && !STATIC_NAMES.includes(route.name)) {
      router.removeRoute(route.name)
    }
  })
}

/**
 * 路由守卫：
 * - 未登录访问非 /login → 强制跳登录
 * - 已登录访问 /login → 跳首页
 * - 已登录且动态路由未加载 → 调接口加载后重走守卫
 * - token 存在但取不到 utype → 强制退出并跳转登录
 */
router.beforeEach(async (to, from, next) => {
  const token = localStorage.getItem('token')

  if (!token) {
    if (to.path === '/login') return next()
    return next('/login')
  }

  if (to.path === '/login') return next('/')

  try {
    const userStore = useUserStore()
    const appStore = useAppStore()

    if (!appStore.routesLoaded) {
      const roleName = userStore.userInfo?.utype
      if (roleName) {
        try {
          await appStore.loadPermissionRoutes(String(roleName))
        } catch (e) {
          // 权限接口失败：不阻断导航，静态兜底路由已覆盖所有页面
          console.warn('[router guard] 动态路由加载失败，使用静态兜底路由：', e)
        }
        // 无论加载成功与否，都重新走一次守卫确保命中最终路由
        return next({ ...to, replace: true })
      }
      userStore.logout()
      return next('/login')
    }

    // 权限检查：如果目标路由有 meta.roles 且当前用户角色不在其中，拦截
    if (to.meta?.roles?.length) {
      const utype = String(userStore.userInfo?.utype || '')
      if (!to.meta.roles.includes(utype)) {
        ElMessage.warning('无权限访问')
        return next('/home')
      }
    }

    next()
  } catch (e) {
    console.error('[router guard] 守卫异常：', e)
    next()
  }
})

export default router
