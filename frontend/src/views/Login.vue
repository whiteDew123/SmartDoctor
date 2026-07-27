<template>
  <div class="login-container">
    <div class="login-box">
      <h2 class="title">慧医数字医疗应用系统</h2>
      <el-form ref="formRef" :model="form" :rules="rules" class="login-form">
        <el-form-item prop="uname">
          <el-input v-model="form.uname" placeholder="请输入用户名" prefix-icon="User" />
        </el-form-item>
        <el-form-item prop="pwd">
          <el-input v-model="form.pwd" type="password" placeholder="请输入密码" prefix-icon="Lock" show-password />
        </el-form-item>
        <el-form-item>
          <el-button type="primary" class="login-btn" @click="handleLogin" :loading="loading">登录</el-button>
        </el-form-item>
      </el-form>
    </div>
  </div>
</template>

<script setup>
import { ref, reactive } from 'vue'
import { useRouter } from 'vue-router'
import { ElMessage } from 'element-plus'
import { login } from '@/api/account'
import { useUserStore } from '@/stores/user'

const router = useRouter()
const userStore = useUserStore()
const formRef = ref(null)
const loading = ref(false)

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

  loading.value = true
  try {
    const res = await login(form)
    userStore.setToken(res.data.id)
    userStore.setUserInfo(res.data)
    ElMessage.success('登录成功')
    router.push('/')
  } catch (error) {
    console.error(error)
  } finally {
    loading.value = false
  }
}
</script>

<style lang="scss" scoped>
.login-container {
  width: 100%;
  height: 100vh;
  display: flex;
  justify-content: center;
  align-items: center;
  background: linear-gradient(135deg, #667eea 0%, #764ba2 100%);

  .login-box {
    width: 400px;
    padding: 40px;
    background: #fff;
    border-radius: 10px;
    box-shadow: 0 2px 12px rgba(0, 0, 0, 0.1);

    .title {
      text-align: center;
      margin-bottom: 30px;
      color: #333;
    }

    .login-form {
      .login-btn {
        width: 100%;
      }
    }
  }
}
</style>