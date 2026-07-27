<template>
  <el-container class="layout-container">
    <el-aside width="220px" class="aside">
      <div class="logo">
        <span>慧医系统</span>
      </div>
      <el-menu
        :default-active="$route.path"
        router
        background-color="#304156"
        text-color="#bfcbd9"
        active-text-color="#409EFF"
      >
        <el-menu-item index="/home">
          <el-icon><HomeFilled /></el-icon>
          <span>首页</span>
        </el-menu-item>
        <el-menu-item index="/account">
          <el-icon><User /></el-icon>
          <span>用户管理</span>
        </el-menu-item>
        <el-menu-item index="/doctor">
          <el-icon><FirstAidKit /></el-icon>
          <span>医生管理</span>
        </el-menu-item>
        <el-menu-item index="/drug">
          <el-icon><MedicineBox /></el-icon>
          <span>药品管理</span>
        </el-menu-item>
      </el-menu>
    </el-aside>
    <el-container>
      <el-header class="header">
        <div class="header-right">
          <el-dropdown @command="handleCommand">
            <span class="user-info">
              <el-icon><UserFilled /></el-icon>
              {{ userStore.userInfo?.realname || '用户' }}
              <el-icon><ArrowDown /></el-icon>
            </span>
            <template #dropdown>
              <el-dropdown-menu>
                <el-dropdown-item command="logout">退出登录</el-dropdown-item>
              </el-dropdown-menu>
            </template>
          </el-dropdown>
        </div>
      </el-header>
      <el-main class="main">
        <router-view />
      </el-main>
    </el-container>
  </el-container>
</template>

<script setup>
import { useRouter } from 'vue-router'
import { useUserStore } from '@/stores/user'

const router = useRouter()
const userStore = useUserStore()

const handleCommand = (command) => {
  if (command === 'logout') {
    userStore.logout()
    router.push('/login')
  }
}
</script>

<style lang="scss" scoped>
.layout-container {
  height: 100vh;

  .aside {
    background: #304156;

    .logo {
      height: 60px;
      display: flex;
      align-items: center;
      justify-content: center;
      color: #fff;
      font-size: 20px;
      font-weight: bold;
      border-bottom: 1px solid #3a4a5b;
    }

    .el-menu {
      border-right: none;
    }
  }

  .header {
    background: #fff;
    border-bottom: 1px solid #e6e6e6;
    display: flex;
    align-items: center;
    justify-content: flex-end;
    padding-right: 20px;

    .header-right {
      .user-info {
        display: flex;
        align-items: center;
        gap: 5px;
        cursor: pointer;
        color: #333;
      }
    }
  }

  .main {
    background: #f0f2f5;
    padding: 20px;
  }
}
</style>