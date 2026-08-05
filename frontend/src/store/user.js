import { defineStore } from 'pinia'
import { ref } from 'vue'
import { login } from '@/api/account'
import { useAppStore } from './modules/app'

export const useUserStore = defineStore('user', () => {
  const token = ref(localStorage.getItem('token') || '')
  const userInfoStr = localStorage.getItem('userInfo')
  const userInfo = ref(userInfoStr && userInfoStr !== 'undefined' ? JSON.parse(userInfoStr) : null)

  const setToken = (newToken) => {
    token.value = newToken
    localStorage.setItem('token', newToken)
  }

  const setUserInfo = (info) => {
    userInfo.value = info
    localStorage.setItem('userInfo', JSON.stringify(info))
  }

  /**
   * 退出登录：同时清空动态路由/菜单状态，保证下次登录重新加载。
   * 注意：getActivePinia 需在已挂载 pinia 的上下文中调用，此处用 try/catch 兜底。
   */
  const logout = () => {
    token.value = ''
    userInfo.value = null
    localStorage.removeItem('token')
    localStorage.removeItem('userInfo')
    try {
      const appStore = useAppStore()
      appStore.clearPermissionRoutes()
    } catch (e) {
      // 无 Pinia 上下文时忽略
    }
  }

  const handleLogin = async (loginInfo) => {
    const username = loginInfo.uname.trim()
    return new Promise((resolve, reject) => {
      login({
        uname: username,
        pwd: loginInfo.pwd,
        role: loginInfo.role
      }).then(res => {
        setToken(res.data.token)
        setUserInfo(res.data.account)
        resolve(res)
      }).catch(() => {
        reject()
      })
    })
  }

  return {
    token,
    userInfo,
    setToken,
    setUserInfo,
    logout,
    handleLogin
  }
})