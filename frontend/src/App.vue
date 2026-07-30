<template>
  <router-view />
</template>

<script setup>
import { onMounted } from 'vue'
import { useUserStore } from '@/store/user'
import { useAppStore } from '@/store/modules/app'

/**
 * 应用挂载时：如果已有登录态（用户直接刷新浏览器），重新加载动态路由树。
 * 避免刷新后侧边菜单和动态路由丢失。
 */
onMounted(async () => {
  const token = localStorage.getItem('token')
  if (!token) return
  const userStore = useUserStore()
  const appStore = useAppStore()
  if (!appStore.routesLoaded) {
    const utype = userStore.userInfo?.utype
    if (utype) {
      try {
        await appStore.loadPermissionRoutes(String(utype))
      } catch (e) {
        console.error('[App] 加载动态路由失败:', e)
      }
    }
  }
})
</script>

<style>
* {
  margin: 0;
  padding: 0;
  box-sizing: border-box;
}

html, body {
  width: 100%;
  min-height: 100vh;
  height: 100%;
}

#app {
  width: 100%;
  min-height: 100vh;
  height: 100%;
}
</style>