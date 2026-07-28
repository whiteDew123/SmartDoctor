<template>
  <div class="login-container" :style="{ backgroundImage: `url(${loginBg})` }">
    <div class="login-wrapper">
      <div class="login-header">
        <img :src="logo" class="logo" alt="logo" />
      </div>
      <div class="login-box">
        <h2 class="title">欢迎登录</h2>
        <el-form ref="formRef" :model="form" :rules="rules" class="login-form">
          <el-form-item prop="uname">
            <el-input v-model="form.uname" placeholder="用户名">
              <template #prefix>
                <img :src="usernameIcon" class="input-icon" />
              </template>
            </el-input>
          </el-form-item>
          <el-form-item prop="pwd">
            <el-input v-model="form.pwd" type="password" placeholder="密码" show-password>
              <template #prefix>
                <img :src="passwordIcon" class="input-icon" />
              </template>
            </el-input>
          </el-form-item>
          <el-form-item>
            <div class="login-btn" @click="handleLogin">
              <img :src="loginBtn" alt="登录" />
            </div>
          </el-form-item>
        </el-form>
      </div>
    </div>
  </div>
</template>

<script setup>
import { ref, reactive } from 'vue'
import { useRouter } from 'vue-router'
import { ElMessage } from 'element-plus'
import { useUserStore } from '@/store/user'
import loginBg from '@/assets/images/LoginBackground.jpg'
import logo from '@/assets/images/logo.png'
import usernameIcon from '@/assets/images/username.png'
import passwordIcon from '@/assets/images/password.png'
import loginBtn from '@/assets/images/LoginButton.png'

const router = useRouter()
const userStore = useUserStore()
const formRef = ref(null)

const form = reactive({
  uname: '',
  pwd: ''
})

const rules = {
  uname: [{ required: true, message: '请输入用户名', trigger: 'blur' }],
  pwd: [{ required: true, message: '请输入密码', trigger: 'blur' }]
}

const handleLogin = async () => {
  const valid = await formRef.value.validate().catch(() => false)
  if (!valid) return

  try {
    await userStore.handleLogin(form)
    ElMessage.success('登录成功')
    router.push('/')
  } catch (error) {
    console.error(error)
  }
}
</script>

<style lang="scss" scoped>
.login-container {
  width: 100%;
  min-height: 100vh;
  height: auto;
  background-repeat: no-repeat;
  background-position: center center;
  background-size: cover;
  display: flex;
  justify-content: center;
  align-items: center;
  position: relative;
}

.login-wrapper {
  display: flex;
  flex-direction: column;
  align-items: center;
  position: relative;
  z-index: 10;
}

.login-header {
  position: relative;
  z-index: 20;
  margin-bottom: -50px;

  .logo {
    width: 100px;
    height: 100px;
    border-radius: 50%;
    border: 4px solid #fff;
    box-shadow: 0 4px 12px rgba(0, 0, 0, 0.15);
  }
}

.login-box {
  width: 420px;
  padding: 60px 50px 40px;
  background: #fff;
  border-radius: 12px;
  box-shadow: 0 8px 32px rgba(0, 0, 0, 0.15);

  .title {
    text-align: center;
    margin: 0 0 30px 0;
    color: #333;
    font-size: 28px;
    font-weight: 600;
  }

  .login-form {
    .el-form-item {
      margin-bottom: 24px;
    }

    .input-icon {
      width: 20px;
      height: 20px;
      margin-right: 8px;
    }

    :deep(.el-input__wrapper) {
      border-radius: 25px;
      padding: 8px 20px;
      box-shadow: 0 0 0 2px #00d9bf;

      &.is-focus {
        box-shadow: 0 0 0 2px #0099cc;
      }

      &:hover {
        box-shadow: 0 0 0 2px #0099cc;
      }
    }

    :deep(.el-input__inner) {
      font-size: 16px;
      height: 28px;
    }

    .login-btn {
      width: 100%;
      text-align: center;
      cursor: pointer;
      margin-top: 10px;

      img {
        width: 100%;
        height: 48px;
        object-fit: contain;
      }

      &:hover {
        opacity: 0.9;
      }

      &:active {
        opacity: 0.8;
      }
    }
  }
}
</style>
