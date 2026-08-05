<template>
  <div class="login-container" :style="{ backgroundImage: `url(${loginBg})` }">
    <div class="login-wrapper">
      <div class="login-header">
        <img :src="logo" class="logo" alt="logo" />
      </div>
      <div class="login-box" :class="{ 'is-register': mode === 'register' }">
        <!-- 登录表单 -->
        <template v-if="mode === 'login'">
          <h2 class="title">欢迎登录</h2>
          <el-form ref="loginFormRef" :model="loginForm" :rules="loginRules" class="login-form">
            <el-form-item class="role-item">
              <el-radio-group v-model="loginForm.role" class="role-group">
                <el-radio-button label="1">管理员</el-radio-button>
                <el-radio-button label="2">医生</el-radio-button>
                <el-radio-button label="3">患者</el-radio-button>
              </el-radio-group>
            </el-form-item>
            <el-form-item prop="uname">
              <el-input v-model="loginForm.uname" placeholder="用户名">
                <template #prefix>
                  <img :src="usernameIcon" class="input-icon" />
                </template>
              </el-input>
            </el-form-item>
            <el-form-item prop="pwd">
              <el-input v-model="loginForm.pwd" type="password" placeholder="密码" show-password>
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
            <div class="switch-tip">
              <el-link type="primary" :underline="false" @click="switchMode('forgot')">忘记密码？</el-link>
              <span style="margin: 0 8px;">|</span>
              <el-link type="primary" :underline="false" @click="switchMode('register')">去注册</el-link>
            </div>
          </el-form>
        </template>

        <!-- 忘记密码表单 -->
        <template v-else-if="mode === 'forgot'">
          <h2 class="title">重置密码</h2>
          <el-form ref="forgotFormRef" :model="forgotForm" :rules="forgotRules" class="login-form">
            <el-form-item prop="uname">
              <el-input v-model="forgotForm.uname" placeholder="用户名">
                <template #prefix>
                  <img :src="usernameIcon" class="input-icon" />
                </template>
              </el-input>
            </el-form-item>
            <el-form-item prop="phonenumber">
              <el-input v-model="forgotForm.phonenumber" placeholder="手机号">
                <template #prefix>
                  <img :src="usernameIcon" class="input-icon" />
                </template>
              </el-input>
            </el-form-item>
            <el-form-item prop="newPwd">
              <el-input v-model="forgotForm.newPwd" type="password" placeholder="新密码" show-password>
                <template #prefix>
                  <img :src="passwordIcon" class="input-icon" />
                </template>
              </el-input>
            </el-form-item>
            <el-form-item prop="confirmNewPwd">
              <el-input v-model="forgotForm.confirmNewPwd" type="password" placeholder="确认新密码" show-password>
                <template #prefix>
                  <img :src="passwordIcon" class="input-icon" />
                </template>
              </el-input>
            </el-form-item>
            <el-form-item>
              <el-button type="primary" class="register-btn" :loading="forgotLoading" @click="handleForgot">重 置</el-button>
            </el-form-item>
            <div class="switch-tip">
              想起密码了？<el-link type="primary" :underline="false" @click="switchMode('login')">去登录</el-link>
            </div>
          </el-form>
        </template>

        <!-- 注册表单 -->
        <template v-else>
          <h2 class="title">用户注册</h2>
          <el-form ref="registerFormRef" :model="registerForm" :rules="registerRules" class="login-form" label-position="top">
            <el-form-item class="role-item">
              <el-radio-group v-model="registerForm.utype" class="role-group">
                <el-radio-button label="2">医生</el-radio-button>
                <el-radio-button label="3">患者</el-radio-button>
              </el-radio-group>
            </el-form-item>
            <el-form-item prop="realname">
              <el-input v-model="registerForm.realname" placeholder="真实姓名">
                <template #prefix>
                  <img :src="usernameIcon" class="input-icon" />
                </template>
              </el-input>
            </el-form-item>
            <el-form-item prop="uname">
              <el-input v-model="registerForm.uname" placeholder="登录用户名">
                <template #prefix>
                  <img :src="usernameIcon" class="input-icon" />
                </template>
              </el-input>
            </el-form-item>
            <el-form-item prop="pwd">
              <el-input v-model="registerForm.pwd" type="password" placeholder="密码" show-password>
                <template #prefix>
                  <img :src="passwordIcon" class="input-icon" />
                </template>
              </el-input>
            </el-form-item>
            <el-form-item prop="confirmPwd">
              <el-input v-model="registerForm.confirmPwd" type="password" placeholder="确认密码" show-password>
                <template #prefix>
                  <img :src="passwordIcon" class="input-icon" />
                </template>
              </el-input>
            </el-form-item>
            <el-form-item prop="phonenumber">
              <el-input v-model="registerForm.phonenumber" placeholder="手机号">
                <template #prefix>
                  <img :src="usernameIcon" class="input-icon" />
                </template>
              </el-input>
            </el-form-item>

            <!-- 医生额外字段 -->
            <template v-if="registerForm.utype === '2'">
              <div class="doctor-extra-title">医生信息</div>
              <el-form-item prop="hospital">
                <el-input v-model="registerForm.hospital" placeholder="所属医院" />
              </el-form-item>
              <el-form-item prop="levelId">
                <el-select v-model="registerForm.levelId" placeholder="医师级别" class="full-width">
                  <el-option v-for="item in doctorLevels" :key="item.id" :label="item.name" :value="item.id" />
                </el-select>
              </el-form-item>
              <el-form-item prop="typeId">
                <el-select v-model="registerForm.typeId" placeholder="诊治类型" class="full-width">
                  <el-option v-for="item in treatTypes" :key="item.id" :label="item.name" :value="item.id" />
                </el-select>
              </el-form-item>
              <div class="inline-row">
                <el-form-item prop="age" class="inline-item">
                  <el-input v-model.number="registerForm.age" placeholder="年龄" />
                </el-form-item>
                <el-form-item prop="sex" class="inline-item">
                  <el-select v-model="registerForm.sex" placeholder="性别" class="full-width">
                    <el-option label="男" :value="1" />
                    <el-option label="女" :value="2" />
                  </el-select>
                </el-form-item>
              </div>
            </template>

            <el-form-item>
              <el-button type="primary" class="register-btn" :loading="registerLoading" @click="handleRegister">注 册</el-button>
            </el-form-item>
            <div class="switch-tip">
              已有账号？<el-link type="primary" :underline="false" @click="switchMode('login')">去登录</el-link>
            </div>
          </el-form>
        </template>
      </div>
    </div>
  </div>
</template>

<script setup>
import { ref, reactive } from 'vue'
import { useRouter } from 'vue-router'
import { ElMessage } from 'element-plus'
import { useUserStore } from '@/store/user'
import { register, forgotPassword } from '@/api/account'
import { getDoctorLevels, getTreatTypes } from '@/api/doctor'
import loginBg from '@/assets/images/LoginBackground.jpg'
import logo from '@/assets/images/logo.png'
import usernameIcon from '@/assets/images/username.png'
import passwordIcon from '@/assets/images/password.png'
import loginBtn from '@/assets/images/LoginButton.png'

const router = useRouter()
const userStore = useUserStore()
const loginFormRef = ref(null)
const registerFormRef = ref(null)
const forgotFormRef = ref(null)

// 当前展示：login / register / forgot
const mode = ref('login')

const loginForm = reactive({
  uname: '',
  pwd: '',
  role: '1'
})

const loginRules = {
  uname: [{ required: true, message: '请输入用户名', trigger: 'blur' }],
  pwd: [{ required: true, message: '请输入密码', trigger: 'blur' }]
}

const registerForm = reactive({
  utype: '2',
  realname: '',
  uname: '',
  pwd: '',
  confirmPwd: '',
  phonenumber: '',
  age: null,
  sex: null,
  hospital: '',
  levelId: null,
  typeId: null
})

const validateConfirmPwd = (rule, value, callback) => {
  if (value === '') {
    callback(new Error('请再次输入密码'))
  } else if (value !== registerForm.pwd) {
    callback(new Error('两次输入密码不一致'))
  } else {
    callback()
  }
}

const validatePhone = (rule, value, callback) => {
  if (value === '') {
    callback(new Error('请输入手机号'))
  } else if (!/^1[3-9]\d{9}$/.test(value)) {
    callback(new Error('手机号格式不正确'))
  } else {
    callback()
  }
}

const registerRules = {
  realname: [{ required: true, message: '请输入真实姓名', trigger: 'blur' }],
  uname: [{ required: true, message: '请输入登录用户名', trigger: 'blur' }],
  pwd: [
    { required: true, message: '请输入密码', trigger: 'blur' },
    { min: 6, message: '密码至少6位', trigger: 'blur' }
  ],
  confirmPwd: [{ required: true, validator: validateConfirmPwd, trigger: 'blur' }],
  phonenumber: [{ required: true, validator: validatePhone, trigger: 'blur' }],
  hospital: [{ required: true, message: '请输入所属医院', trigger: 'blur' }],
  levelId: [{ required: true, message: '请选择医师级别', trigger: 'change' }],
  typeId: [{ required: true, message: '请选择诊治类型', trigger: 'change' }],
  age: [{ required: true, message: '请输入年龄', trigger: 'blur' }],
  sex: [{ required: true, message: '请选择性别', trigger: 'change' }]
}

const forgotForm = reactive({
  uname: '',
  phonenumber: '',
  newPwd: '',
  confirmNewPwd: ''
})

const validateConfirmNewPwd = (rule, value, callback) => {
  if (value === '') {
    callback(new Error('请再次输入新密码'))
  } else if (value !== forgotForm.newPwd) {
    callback(new Error('两次输入密码不一致'))
  } else {
    callback()
  }
}

const forgotRules = {
  uname: [{ required: true, message: '请输入用户名', trigger: 'blur' }],
  phonenumber: [{ required: true, validator: validatePhone, trigger: 'blur' }],
  newPwd: [
    { required: true, message: '请输入新密码', trigger: 'blur' },
    { min: 6, message: '密码至少6位', trigger: 'blur' }
  ],
  confirmNewPwd: [{ required: true, validator: validateConfirmNewPwd, trigger: 'blur' }]
}

// 医生级别 / 诊治类型下拉数据
const doctorLevels = ref([])
const treatTypes = ref([])
const registerLoading = ref(false)
const forgotLoading = ref(false)

const loadDoctorOptions = async () => {
  try {
    const [levelRes, typeRes] = await Promise.all([getDoctorLevels(), getTreatTypes()])
    doctorLevels.value = levelRes.data || []
    treatTypes.value = typeRes.data || []
  } catch (e) {
    console.warn('[register] 加载医生级别/诊治类型失败：', e)
  }
}

const switchMode = (target) => {
  mode.value = target
  if (target === 'register' && doctorLevels.value.length === 0) {
    loadDoctorOptions()
  }
}

const handleLogin = async () => {
  const valid = await loginFormRef.value.validate().catch(() => false)
  if (!valid) return

  try {
    const res = await userStore.handleLogin(loginForm)
    const account = res?.data?.account
    const token = res?.data?.token
    if (!account || !token) {
      ElMessage.error('登录响应数据异常，请检查后端接口')
      return
    }
    const utype = account.utype
    if (utype) {
      const { useAppStore } = await import('@/store/modules/app')
      const appStore = useAppStore()
      await appStore.loadPermissionRoutes(String(utype))
    }
    ElMessage.success('登录成功')
    router.push('/')
  } catch (error) {
    console.error(error)
    ElMessage.error(error.message || '登录失败')
  }
}

const handleForgot = async () => {
  const valid = await forgotFormRef.value.validate().catch(() => false)
  if (!valid) return

  forgotLoading.value = true
  try {
    const { confirmNewPwd, ...payload } = forgotForm
    await forgotPassword(payload)
    ElMessage.success('密码重置成功，请登录')
    // 回填用户名并切换到登录
    loginForm.uname = forgotForm.uname
    switchMode('login')
  } catch (error) {
    console.error(error)
  } finally {
    forgotLoading.value = false
  }
}

const handleRegister = async () => {
  const valid = await registerFormRef.value.validate().catch(() => false)
  if (!valid) return

  registerLoading.value = true
  try {
    const { confirmPwd, ...payload } = registerForm
    await register(payload)
    ElMessage.success('注册成功，请登录')
    // 注册成功后回填登录用户名并切换到登录
    loginForm.uname = registerForm.uname
    loginForm.role = registerForm.utype
    switchMode('login')
  } catch (error) {
    console.error(error)
  } finally {
    registerLoading.value = false
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

  &.is-register {
    width: 480px;
    max-height: 86vh;
    overflow-y: auto;
  }

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

    .role-item {
      :deep(.el-form-item__content) {
        justify-content: center;
      }

      .role-group {
        :deep(.el-radio-button__inner) {
          border-radius: 0;
          padding: 10px 22px;
          font-size: 15px;
        }
      }
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

    :deep(.el-select .el-input__wrapper) {
      border-radius: 25px;
    }

    .full-width {
      width: 100%;
    }

    .doctor-extra-title {
      font-size: 14px;
      color: #0099cc;
      font-weight: 600;
      margin: 4px 0 14px;
      padding-left: 4px;
      border-left: 3px solid #00d9bf;
    }

    .inline-row {
      display: flex;
      gap: 16px;

      .inline-item {
        flex: 1;
        margin-bottom: 24px;
      }
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

    .register-btn {
      width: 100%;
      height: 48px;
      border-radius: 25px;
      font-size: 16px;
      letter-spacing: 4px;
      background: linear-gradient(135deg, #00d9bf, #0099cc);
      border: none;
      margin-top: 10px;
    }

    .switch-tip {
      text-align: center;
      font-size: 14px;
      color: #666;
      margin-top: 4px;
    }
  }
}
</style>
