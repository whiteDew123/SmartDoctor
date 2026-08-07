<template>
  <el-container class="layout-container" :class="{ 'logging-out': loggingOut }">
    <!-- ===== 左侧边栏 ===== -->
    <el-aside :width="asideWidth + 'px'" class="aside">
      <div class="aside-logo">
        <img :src="homeLogo" alt="logo" class="logo-img" />
        <span class="logo-text">慧医数字医疗</span>
      </div>
      <el-menu
        :default-active="activeMenu"
        router
        class="side-menu"
        background-color="transparent"
        text-color="#fff"
        active-text-color="#fff"
      >
        <template v-for="menu in menus" :key="menu.id">
          <el-menu-item :index="menu.path">
            <el-icon :size="18"><component :is="getMenuIcon(menu)" /></el-icon>
            <template #title>{{ menu.title }}</template>
          </el-menu-item>
        </template>
      </el-menu>
    </el-aside>

    <!-- ===== 右侧主区域 ===== -->
    <el-container class="main-container">
      <el-header class="header" height="60px">
        <div class="header-left">
          <img :src="homeLogo" alt="logo" class="header-logo" />
          <span class="header-title">慧医数字医疗应用系统</span>
        </div>
        <div class="header-right">
          <span class="welcome-text">
            欢迎来到慧医数字医疗应用系统，当前用户：
            <span class="user-name">{{ userName }}</span>
          </span>
          <el-button
            link
            class="theme-toggle"
            :title="isDark ? '切换浅色模式' : '切换深色模式'"
            @click="toggleTheme"
          >
            <el-icon :size="18"><component :is="isDark ? 'Sunny' : 'Moon'" /></el-icon>
          </el-button>
          <el-button
            link
            type="primary"
            class="logout-btn"
            @click="handleLogout"
          >
            退出登录
          </el-button>
        </div>
      </el-header>

      <el-main class="main">
        <router-view v-slot="{ Component, route }">
          <transition name="fade" mode="out-in">
            <component :is="Component" :key="route.fullPath" />
          </transition>
        </router-view>
      </el-main>
    </el-container>
    
    <!-- AI助手 -->
    <AIAssistant v-model="showAIAssistant" />
    
    <!-- AI助手悬浮按钮 -->
    <AIFloatButton @click="showAIAssistant = true" />
  </el-container>
</template>

<script setup>
import { computed, ref, onMounted } from 'vue'
import { useRoute, useRouter } from 'vue-router'
import { ElMessageBox } from 'element-plus'
import { useUserStore } from '@/store/user'
import { useAppStore } from '@/store/modules/app'
import homeLogo from '@/assets/images/HomeLogo.png'
import AIAssistant from '@/components/AIAssistant.vue'
import AIFloatButton from '@/components/AIFloatButton.vue'

const route = useRoute()
const router = useRouter()
const userStore = useUserStore()
const appStore = useAppStore()

const asideWidth = ref(240)
const showAIAssistant = ref(false)
const loggingOut = ref(false)
const activeMenu = computed(() => route.path)
const userName = computed(() => userStore.userInfo?.realname || '未登录')

const isDark = ref(false)

const toggleTheme = () => {
  isDark.value = !isDark.value
  const html = document.documentElement
  if (isDark.value) {
    html.classList.add('dark')
  } else {
    html.classList.remove('dark')
  }
  localStorage.setItem('theme', isDark.value ? 'dark' : 'light')
}

const fallbackMenus = [
  { id: 1, path: '/home', title: '首页' },
  { id: 2, path: '/drug/company', title: '医药公司管理' },
  { id: 3, path: '/sale/location', title: '销售地点管理' },
  { id: 4, path: '/city', title: '城市信息管理' },
  { id: 5, path: '/drug/list', title: '药品信息管理' },
  { id: 6, path: '/policy/list', title: '医保政策管理' },
  { id: 7, path: '/company/policy', title: '医药公司政策管理' },
  { id: 8, path: '/doctor', title: '医生信息管理' },
  { id: 9, path: '/material', title: '必备材料管理' }
]

const menus = computed(() => fallbackMenus)

const iconMap = {
  '/home': 'HomeFilled',
  '/drug/company': 'OfficeBuilding',
  '/sale/location': 'Location',
  '/city': 'Place',
  '/drug/list': 'Operation',
  '/policy/list': 'Tickets',
  '/company/policy': 'DataLine',
  '/doctor': 'FirstAidKit',
  '/material': 'Document'
}

const getMenuIcon = (menu) => iconMap[menu.path] || 'Menu'

const handleLogout = async () => {
  try {
    await ElMessageBox.confirm('确定要退出登录吗？', '温馨提示', {
      confirmButtonText: '确定',
      cancelButtonText: '取消',
      type: 'warning'
    })
  } catch (e) {
    return
  }
  loggingOut.value = true
  setTimeout(() => {
    appStore.clearPermissionRoutes()
    userStore.logout()
    router.push('/login')
  }, 500)
}

onMounted(() => {
  const savedTheme = localStorage.getItem('theme')
  if (savedTheme === 'dark') {
    isDark.value = true
    document.documentElement.classList.add('dark')
  }

  if (!appStore.routesLoaded && userStore.userInfo?.utype) {
    appStore.loadPermissionRoutes(String(userStore.userInfo.utype))
  }
})
</script>

<style lang="scss" scoped>
.layout-container {
  height: 100vh;
  width: 100%;
  background: #f2f4f7;
  transition: opacity 0.5s ease;

  &.logging-out {
    opacity: 0;
    pointer-events: none;
  }
}

.aside {
  background: linear-gradient(180deg, rgba(47, 183, 155, 0.9) 0%, rgba(34, 166, 136, 0.9) 100%);
  color: #fff;
  display: flex;
  flex-direction: column;
  overflow: hidden;
  border-right: 1px solid rgba(255, 255, 255, 0.12);
  box-shadow: 4px 0 20px rgba(0, 0, 0, 0.08);

  .aside-logo {
    height: 60px;
    padding: 0 18px;
    display: flex;
    align-items: center;
    gap: 10px;
    background: rgba(0, 0, 0, 0.06);
    border-bottom: 1px solid rgba(255, 255, 255, 0.12);

    .logo-img {
      width: 40px;
      height: 40px;
      border-radius: 50%;
      object-fit: cover;
      border: 2px solid rgba(255, 255, 255, 0.7);
      flex-shrink: 0;
    }
    .logo-text {
      font-size: 17px;
      font-weight: 700;
      color: #fff;
      letter-spacing: 1px;
      white-space: nowrap;
    }
  }

  .side-menu {
    flex: 1;
    border-right: none;
    padding-top: 10px;
    overflow-y: auto;

    &::-webkit-scrollbar { width: 4px; }
    &::-webkit-scrollbar-thumb { background: rgba(255,255,255,0.25); border-radius: 2px; }

    :deep(.el-menu-item) {
      height: 48px;
      line-height: 48px;
      margin: 4px 10px;
      border-radius: 8px;
      color: rgba(255, 255, 255, 0.9);

      &:hover {
        background: rgba(255, 255, 255, 0.14);
        color: #fff;
      }
    }
    :deep(.el-menu-item.is-active) {
      background: rgba(255, 255, 255, 0.22) !important;
      color: #fff !important;
      font-weight: 600;
      box-shadow: inset 3px 0 0 #fff;
    }
    :deep(.el-menu .el-icon) {
      margin-right: 10px;
    }
  }
}

.main-container {
  min-width: 0;
}

.header {
  position: sticky;
  top: 0;
  z-index: 100;
  background: rgba(255, 255, 255, 0.55);
  backdrop-filter: blur(12px);
  -webkit-backdrop-filter: blur(12px);
  border-bottom: 1px solid rgba(229, 231, 235, 0.4);
  display: flex;
  align-items: center;
  justify-content: space-between;
  padding: 0 22px;
  box-shadow: 0 1px 0 rgba(0, 0, 0, 0.03);

  .header-left {
    display: flex;
    align-items: center;
    gap: 12px;

    .header-logo {
      width: 42px;
      height: 42px;
      border-radius: 50%;
      object-fit: cover;
      border: 2px solid #2FB79B;
    }
    .header-title {
      font-size: 22px;
      font-weight: 700;
      color: #2FB79B;
      letter-spacing: 2px;
    }
  }

  .header-right {
    display: flex;
    align-items: center;
    gap: 18px;

    .welcome-text {
      font-size: 14px;
      color: #4b5563;

      .user-name {
        color: #2FB79B;
        font-weight: 600;
      }
    }

    .logout-btn {
      font-size: 14px;
      padding: 6px 10px;
      border-radius: 6px;
      color: #2FB79B !important;

      &:hover {
        background: #ecfdf5;
      }
    }
  }
}

.main {
  padding: 0;
  margin-top: -60px;
  padding-top: 60px;
  background: #f2f4f7;
  overflow: auto;
}

.fade-enter-active,
.fade-leave-active {
  transition: opacity 0.2s ease;
}
.fade-enter-from,
.fade-leave-to {
  opacity: 0;
}
</style>

<style lang="scss">
/* ===== 深色模式（全局） ===== */
html.dark .layout-container {
  background: #141414;
}

html.dark .header {
  background: rgba(29, 29, 29, 0.55);
  backdrop-filter: blur(12px);
  -webkit-backdrop-filter: blur(12px);
  border-bottom-color: rgba(51, 51, 51, 0.4);
  box-shadow: 0 1px 0 rgba(255, 255, 255, 0.03);
}

html.dark .header .header-title {
  color: #52c4a2;
}

html.dark .header .welcome-text {
  color: #bbb;
}

html.dark .header .welcome-text .user-name {
  color: #52c4a2;
}

html.dark .header .logout-btn {
  color: #52c4a2 !important;
}

html.dark .header .logout-btn:hover {
  background: #1a3a30;
}

html.dark .header .theme-toggle {
  color: #bbb;
}

html.dark .header .theme-toggle:hover {
  color: #52c4a2;
}

html.dark .main {
  background: #141414;
}

html.dark .aside {
  background: linear-gradient(180deg, rgba(26, 58, 48, 0.9) 0%, rgba(18, 48, 37, 0.9) 100%);
  border-right-color: rgba(255, 255, 255, 0.06);
}
</style>