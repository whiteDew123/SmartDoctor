import { defineStore } from 'pinia'
import { ref } from 'vue'
import { login } from '@/api/account'

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

  const logout = () => {
    token.value = ''
    userInfo.value = null
    localStorage.removeItem('token')
    localStorage.removeItem('userInfo')
  }

  const handleLogin = async (loginInfo) => {
    const username = loginInfo.uname.trim()
    return new Promise((resolve, reject) => {
      login({ uname: username, pwd: loginInfo.pwd }).then(res => {
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