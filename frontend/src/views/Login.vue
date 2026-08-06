<template>
  <div class="login-container">
    <video
      autoplay
      loop
      muted
      playsinline
      class="bg-video"
    >
      <source :src="videoBg" type="video/mp4" />
    </video>
    <div class="bg-overlay"></div>
    <canvas ref="particleCanvas" class="particle-canvas"></canvas>
    <div class="typewriter" :class="{ 'shift-up': showLogin }">
      <span class="typewriter-text">{{ displayText }}</span>
      <span class="typewriter-cursor">|</span>
    </div>
    <div v-if="!showLogin" class="welcome-btn" @click="showLogin = true">
      <span>欢迎使用</span>
    </div>
    <div v-if="showLogin" class="panel-backdrop" @click="showLogin = false"></div>
    <Transition name="panel-slide">
      <div v-if="showLogin" class="login-panel">
        <template v-if="mode === 'login'">
          <div class="panel-header">
            <span class="panel-title">欢迎登录</span>
          </div>
          <el-form ref="loginFormRef" :model="loginForm" class="login-form" autocomplete="off">
            <el-form-item class="role-item">
              <el-radio-group v-model="loginForm.role" class="role-group">
                <el-radio-button label="1">管理员</el-radio-button>
                <el-radio-button label="2">医生</el-radio-button>
                <el-radio-button label="3">患者</el-radio-button>
              </el-radio-group>
            </el-form-item>
            <el-form-item>
              <el-input v-model="loginForm.uname" placeholder="请输入用户名">
                <template #prefix>
                  <el-icon><User /></el-icon>
                </template>
              </el-input>
            </el-form-item>
            <el-form-item>
              <el-input v-model="loginForm.pwd" type="password" placeholder="请输入密码" show-password>
                <template #prefix>
                  <el-icon><Lock /></el-icon>
                </template>
              </el-input>
            </el-form-item>
            <el-form-item>
              <el-button type="primary" class="login-btn" @click="handleLogin">登 录</el-button>
            </el-form-item>
            <div class="panel-footer">
              <el-link type="primary" :underline="false" @click="mode = 'forgot'">忘记密码？</el-link>
              <span class="divider">|</span>
              <el-link type="primary" :underline="false" @click="mode = 'register'">去注册</el-link>
            </div>
          </el-form>
        </template>

        <template v-else-if="mode === 'register'">
          <div class="panel-header">
            <span class="panel-title">用户注册</span>
          </div>
          <el-form ref="registerFormRef" :model="registerForm" :rules="registerRules" class="login-form" label-position="top" autocomplete="off">
            <el-form-item class="role-item">
              <el-radio-group v-model="registerForm.utype" class="role-group">
                <el-radio-button label="2">医生</el-radio-button>
                <el-radio-button label="3">患者</el-radio-button>
              </el-radio-group>
            </el-form-item>
            <el-form-item prop="realname">
              <el-input v-model="registerForm.realname" placeholder="真实姓名">
                <template #prefix>
                  <el-icon><User /></el-icon>
                </template>
              </el-input>
            </el-form-item>
            <el-form-item prop="uname">
              <el-input v-model="registerForm.uname" placeholder="登录用户名">
                <template #prefix>
                  <el-icon><User /></el-icon>
                </template>
              </el-input>
            </el-form-item>
            <el-form-item prop="pwd">
              <el-input v-model="registerForm.pwd" type="password" placeholder="密码" show-password>
                <template #prefix>
                  <el-icon><Lock /></el-icon>
                </template>
              </el-input>
            </el-form-item>
            <el-form-item prop="confirmPwd">
              <el-input v-model="registerForm.confirmPwd" type="password" placeholder="确认密码" show-password>
                <template #prefix>
                  <el-icon><Lock /></el-icon>
                </template>
              </el-input>
            </el-form-item>
            <el-form-item prop="phonenumber">
              <el-input v-model="registerForm.phonenumber" placeholder="手机号">
                <template #prefix>
                  <el-icon><Phone /></el-icon>
                </template>
              </el-input>
            </el-form-item>
            <template v-if="registerForm.utype === '2'">
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
              <el-button type="primary" class="login-btn" :loading="registerLoading" @click="handleRegister">注 册</el-button>
            </el-form-item>
            <div class="panel-footer">
              已有账号？<el-link type="primary" :underline="false" @click="mode = 'login'">去登录</el-link>
            </div>
          </el-form>
        </template>

        <template v-else-if="mode === 'forgot'">
          <div class="panel-header">
            <span class="panel-title">忘记密码</span>
          </div>
          <el-form ref="forgotFormRef" :model="forgotForm" :rules="forgotRules" class="login-form" autocomplete="off">
            <el-form-item prop="uname">
              <el-input v-model="forgotForm.uname" placeholder="请输入用户名">
                <template #prefix>
                  <el-icon><User /></el-icon>
                </template>
              </el-input>
            </el-form-item>
            <el-form-item prop="phonenumber">
              <el-input v-model="forgotForm.phonenumber" placeholder="请输入注册时手机号">
                <template #prefix>
                  <el-icon><Phone /></el-icon>
                </template>
              </el-input>
            </el-form-item>
            <el-form-item prop="newPwd">
              <el-input v-model="forgotForm.newPwd" type="password" placeholder="新密码" show-password>
                <template #prefix>
                  <el-icon><Lock /></el-icon>
                </template>
              </el-input>
            </el-form-item>
            <el-form-item prop="confirmPwd">
              <el-input v-model="forgotForm.confirmPwd" type="password" placeholder="确认新密码" show-password>
                <template #prefix>
                  <el-icon><Lock /></el-icon>
                </template>
              </el-input>
            </el-form-item>
            <el-form-item>
              <el-button type="primary" class="login-btn" :loading="forgotLoading" @click="handleForgotPassword">重置密码</el-button>
            </el-form-item>
            <div class="panel-footer">
              <el-link type="primary" :underline="false" @click="mode = 'login'">返回登录</el-link>
            </div>
          </el-form>
        </template>
      </div>
    </Transition>
  </div>
</template>

<script setup>
import { ref, onMounted, onBeforeUnmount, reactive } from 'vue'
import { useRouter } from 'vue-router'
import { ElMessage } from 'element-plus'
import { User, Lock, Phone } from '@element-plus/icons-vue'
import { useUserStore } from '@/store/user'
import { register, forgotPassword } from '@/api/account'
import { getDoctorLevels, getTreatTypes } from '@/api/doctor'
import videoBg from '@/assets/videos/medical_video.mp4'

const router = useRouter()
const userStore = useUserStore()

const showLogin = ref(false)
const mode = ref('login')
const particleCanvas = ref(null)
const displayText = ref('')

const fullText = '慧医数字医疗应用系统'
let typeTimer = null

const startTypewriter = () => {
  let index = 0
  let deleting = false
  displayText.value = ''

  const tick = () => {
    if (!deleting) {
      if (index < fullText.length) {
        displayText.value += fullText[index]
        index++
        typeTimer = setTimeout(tick, 200)
      } else {
        typeTimer = setTimeout(() => {
          deleting = true
          tick()
        }, 2000)
      }
    } else {
      if (index > 0) {
        index--
        displayText.value = fullText.slice(0, index)
        typeTimer = setTimeout(tick, 100)
      } else {
        deleting = false
        typeTimer = setTimeout(tick, 800)
      }
    }
  }

  tick()
}

const loginForm = reactive({
  uname: '',
  pwd: '',
  role: '1'
})

const handleLogin = async () => {
  if (!loginForm.uname || !loginForm.pwd) {
    ElMessage.warning('请输入用户名和密码')
    return
  }
  try {
    await userStore.handleLogin(loginForm)
    ElMessage.success('登录成功')
    router.push('/home')
  } catch (error) {
    ElMessage.error('登录失败，请检查用户名和密码')
  }
}

const registerFormRef = ref(null)
const registerLoading = ref(false)
const doctorLevels = ref([])
const treatTypes = ref([])

const registerForm = reactive({
  utype: '2',
  realname: '',
  uname: '',
  pwd: '',
  confirmPwd: '',
  phonenumber: '',
  hospital: '',
  levelId: null,
  typeId: null,
  age: null,
  sex: null
})

const validatePass = (rule, value, callback) => {
  if (value === '') {
    callback(new Error('请输入密码'))
  } else if (value.length < 6) {
    callback(new Error('密码不能少于6位'))
  } else {
    if (registerForm.confirmPwd !== '') {
      registerFormRef.value?.validateField('confirmPwd')
    }
    callback()
  }
}

const validatePass2 = (rule, value, callback) => {
  if (value === '') {
    callback(new Error('请再次输入密码'))
  } else if (value !== registerForm.pwd) {
    callback(new Error('两次输入密码不一致'))
  } else {
    callback()
  }
}

const registerRules = {
  realname: [{ required: true, message: '请输入真实姓名', trigger: 'blur' }],
  uname: [{ required: true, message: '请输入登录用户名', trigger: 'blur' }],
  pwd: [{ validator: validatePass, trigger: 'blur' }],
  confirmPwd: [{ validator: validatePass2, trigger: 'blur' }],
  phonenumber: [{ required: true, message: '请输入手机号', trigger: 'blur' }],
  hospital: [{ required: true, message: '请输入所属医院', trigger: 'blur' }],
  levelId: [{ required: true, message: '请选择医师级别', trigger: 'change' }],
  typeId: [{ required: true, message: '请选择诊治类型', trigger: 'change' }]
}

const handleRegister = () => {
  registerFormRef.value?.validate(async (valid) => {
    if (!valid) return
    registerLoading.value = true
    try {
      await register(registerForm)
      ElMessage.success('注册成功，请登录')
      mode.value = 'login'
    } catch {
      ElMessage.error('注册失败，请重试')
    } finally {
      registerLoading.value = false
    }
  })
}

const loadDoctorLevels = async () => {
  try {
    const res = await getDoctorLevels()
    doctorLevels.value = res.data || []
  } catch { /* ignore */ }
}

const loadTreatTypes = async () => {
  try {
    const res = await getTreatTypes()
    treatTypes.value = res.data || []
  } catch { /* ignore */ }
}

const forgotFormRef = ref(null)
const forgotLoading = ref(false)

const forgotForm = reactive({
  uname: '',
  phonenumber: '',
  newPwd: '',
  confirmPwd: ''
})

const validateForgotPass = (rule, value, callback) => {
  if (value === '') {
    callback(new Error('请输入新密码'))
  } else if (value.length < 6) {
    callback(new Error('密码不能少于6位'))
  } else {
    if (forgotForm.confirmPwd !== '') {
      forgotFormRef.value?.validateField('confirmPwd')
    }
    callback()
  }
}

const validateForgotPass2 = (rule, value, callback) => {
  if (value === '') {
    callback(new Error('请再次输入密码'))
  } else if (value !== forgotForm.newPwd) {
    callback(new Error('两次输入密码不一致'))
  } else {
    callback()
  }
}

const forgotRules = {
  uname: [{ required: true, message: '请输入用户名', trigger: 'blur' }],
  phonenumber: [{ required: true, message: '请输入注册时手机号', trigger: 'blur' }],
  newPwd: [{ validator: validateForgotPass, trigger: 'blur' }],
  confirmPwd: [{ validator: validateForgotPass2, trigger: 'blur' }]
}

const handleForgotPassword = () => {
  forgotFormRef.value?.validate(async (valid) => {
    if (!valid) return
    forgotLoading.value = true
    try {
      await forgotPassword({
        uname: forgotForm.uname,
        phonenumber: forgotForm.phonenumber,
        newPwd: forgotForm.newPwd
      })
      ElMessage.success('密码重置成功，请登录')
      mode.value = 'login'
    } catch {
      ElMessage.error('重置失败，请检查用户名和手机号')
    } finally {
      forgotLoading.value = false
    }
  })
}

let animationId = null
let particles = []
let mouseX = -9999
let mouseY = -9999
const PARTICLE_COUNT = 200
const CONNECT_DISTANCE = 180
const MOUSE_RADIUS = 100
const COLORS = [
  'rgba(0, 217, 191, @)',   // 青绿色
  'rgba(64, 158, 255, @)'   // 天蓝色
]

class Particle {
  constructor(w, h) {
    this.x = Math.random() * w
    this.y = Math.random() * h
    this.vx = (Math.random() - 0.5) * 0.6
    this.vy = (Math.random() - 0.5) * 0.6
    this.radius = Math.random() * 3 + 2
    this.color = COLORS[Math.floor(Math.random() * COLORS.length)]
  }

  update(w, h) {
    const dx = this.x - mouseX
    const dy = this.y - mouseY
    const dist = Math.sqrt(dx * dx + dy * dy)

    if (dist < MOUSE_RADIUS && dist > 0) {
      const force = (MOUSE_RADIUS - dist) / MOUSE_RADIUS
      this.vx -= (dx / dist) * force * 0.25
      this.vy -= (dy / dist) * force * 0.25
    }

    this.vx *= 0.99
    this.vy *= 0.99

    const speed = Math.sqrt(this.vx * this.vx + this.vy * this.vy)
    const minSpeed = 0.5
    if (speed < minSpeed) {
      const scale = minSpeed / (speed || 0.01)
      this.vx *= scale
      this.vy *= scale
    }

    this.x += this.vx
    this.y += this.vy

    if (this.x < 0 || this.x > w) this.vx *= -1
    if (this.y < 0 || this.y > h) this.vy *= -1
  }
}

const initParticles = () => {
  const canvas = particleCanvas.value
  if (!canvas) return

  canvas.width = window.innerWidth
  canvas.height = window.innerHeight

  const ctx = canvas.getContext('2d')
  particles = Array.from({ length: PARTICLE_COUNT }, () => new Particle(canvas.width, canvas.height))

  const animate = () => {
    ctx.clearRect(0, 0, canvas.width, canvas.height)

    particles.forEach((p) => {
      p.update(canvas.width, canvas.height)
      ctx.beginPath()
      ctx.arc(p.x, p.y, p.radius, 0, Math.PI * 2)
      ctx.fillStyle = p.color.replace('@', '0.4')
      ctx.fill()
    })

    for (let i = 0; i < particles.length; i++) {
      for (let j = i + 1; j < particles.length; j++) {
        const dx = particles[i].x - particles[j].x
        const dy = particles[i].y - particles[j].y
        const dist = Math.sqrt(dx * dx + dy * dy)

        if (dist < CONNECT_DISTANCE) {
          const opacity = 1 - dist / CONNECT_DISTANCE
          ctx.beginPath()
          ctx.moveTo(particles[i].x, particles[i].y)
          ctx.lineTo(particles[j].x, particles[j].y)
          ctx.strokeStyle = particles[i].color.replace('@', String(opacity * 0.4))
          ctx.lineWidth = 0.8
          ctx.stroke()
        }
      }
    }

    animationId = requestAnimationFrame(animate)
  }

  animate()
}

const handleResize = () => {
  if (particleCanvas.value) {
    particleCanvas.value.width = window.innerWidth
    particleCanvas.value.height = window.innerHeight
  }
}

const handleMouseMove = (e) => {
  mouseX = e.clientX
  mouseY = e.clientY
}

const handleMouseLeave = () => {
  mouseX = -9999
  mouseY = -9999
}

onMounted(() => {
  initParticles()
  startTypewriter()
  loadDoctorLevels()
  loadTreatTypes()
  window.addEventListener('resize', handleResize)
  window.addEventListener('mousemove', handleMouseMove)
  document.addEventListener('mouseleave', handleMouseLeave)
})

onBeforeUnmount(() => {
  cancelAnimationFrame(animationId)
  clearTimeout(typeTimer)
  window.removeEventListener('resize', handleResize)
  window.removeEventListener('mousemove', handleMouseMove)
  document.removeEventListener('mouseleave', handleMouseLeave)
})
</script>

<style lang="scss" scoped>
.login-container {
  width: 100%;
  min-height: 100vh;
  display: flex;
  justify-content: center;
  align-items: center;
  position: relative;
  overflow: hidden;
}

.bg-video {
  position: absolute;
  top: 0;
  left: 0;
  width: 100%;
  height: 100%;
  object-fit: cover;
  z-index: 1;
}

.bg-overlay {
  position: absolute;
  top: 0;
  left: 0;
  width: 100%;
  height: 37%;
  background: linear-gradient(to bottom, rgba(0, 0, 0, 0.7), rgba(0, 0, 0, 0));
  z-index: 2;
}

.particle-canvas {
  position: absolute;
  top: 0;
  left: 0;
  width: 100%;
  height: 100%;
  z-index: 3;
  pointer-events: none;
}

.typewriter {
  position: absolute;
  top: 25%;
  left: 0;
  right: 0;
  z-index: 10;
  text-align: center;
  transform: translateY(-50%);
  transition: all 0.6s ease;

  &.shift-up {
    top: 8%;
    left: -35%;
    transform: translateY(0) scale(0.6);
  }
}

.typewriter-text {
  font-size: 64px;
  font-weight: 700;
  color: #fff;
  letter-spacing: 8px;
}

.typewriter-cursor {
  font-size: 64px;
  color: #fff;
  animation: blink 0.8s infinite;
}

@keyframes blink {
  0%, 50% { opacity: 1; }
  51%, 100% { opacity: 0; }
}

/* ===== 背景遮罩 ===== */
.panel-backdrop {
  position: fixed;
  top: 0;
  left: 0;
  width: 100%;
  height: 100%;
  z-index: 19;
  background: rgba(0, 0, 0, 0.15);
}

/* ===== 登录面板 ===== */
.login-panel {
  position: fixed;
  right: 0;
  top: 0;
  width: 37%;
  min-width: 400px;
  height: 100vh;
  background: rgba(0, 0, 0, 0.35);
  border-left: 1px solid rgba(255, 255, 255, 0.15);
  z-index: 20;
  display: flex;
  flex-direction: column;
  justify-content: center;
  padding: 60px 50px 60px 50px;
  box-sizing: border-box;
  overflow-y: auto;
}

.panel-header {
  margin-bottom: 40px;
}

.panel-title {
  font-size: 28px;
  font-weight: 600;
  color: #fff;
  letter-spacing: 2px;
}

.login-form {
  :deep(.el-form-item) {
    margin-bottom: 24px;
  }
}

.role-item {
  :deep(.el-form-item__content) {
    justify-content: flex-start;
  }
}

.role-group {
  :deep(.el-radio-button__inner) {
    background: rgba(255, 255, 255, 0.1);
    border-color: rgba(255, 255, 255, 0.2);
    color: rgba(255, 255, 255, 0.6);
    padding: 8px 18px;

    &:hover {
      color: #fff;
    }
  }

  :deep(.el-radio-button__original-radio:checked + .el-radio-button__inner) {
    background: rgba(0, 217, 191, 0.3);
    border-color: rgba(0, 217, 191, 0.5);
    color: #fff;
  }
}

.login-btn {
  width: 100%;
  height: 48px;
  border-radius: 8px;
  font-size: 16px;
  letter-spacing: 4px;
  background: #00d9bf;
  border: none;

  &:hover {
    background: #00c4ad;
  }
}

.panel-footer {
  text-align: center;
  font-size: 14px;
  color: rgba(255, 255, 255, 0.5);

  .divider {
    margin: 0 10px;
  }

  :deep(.el-link) {
    color: rgba(255, 255, 255, 0.6);
  }

  :deep(.el-link:hover) {
    color: #00d9bf;
  }
}

.inline-row {
  display: flex;
  gap: 16px;
}

.inline-item {
  flex: 1;
}

.full-width {
  width: 100%;
}
.panel-slide-enter-active {
  transition: transform 0.5s ease;
}

.panel-slide-leave-active {
  transition: transform 0.35s ease;
}

.panel-slide-enter-from {
  transform: translateX(100%);
}

.panel-slide-leave-to {
  transform: translateX(100%);
}

.welcome-btn {
  position: relative;
  z-index: 10;
  display: inline-flex;
  align-items: center;
  justify-content: center;
  width: 240px;
  height: 64px;
  background: #00d9bf;
  border-radius: 32px;
  cursor: pointer;
  transition: transform 0.4s ease, box-shadow 0.4s ease;
  box-shadow: 0 4px 16px rgba(0, 217, 191, 0.3);

  span {
    font-size: 22px;
    font-weight: 600;
    color: #fff;
    letter-spacing: 4px;
  }

  &:hover {
    transform: scale(1.1);
    box-shadow: 0 6px 24px rgba(0, 217, 191, 0.5);
  }
}
</style>

<style lang="scss">
.login-panel {
  .el-input__wrapper {
    background: rgba(255, 255, 255, 0.15) !important;
    border-radius: 8px;
    box-shadow: none !important;
    border: 1px solid rgba(255, 255, 255, 0.2);

    &.is-focus {
      border-color: rgba(0, 217, 191, 0.5);
    }
  }

  .el-input__inner {
    color: #fff !important;
    font-size: 15px;

    &::placeholder {
      color: rgba(255, 255, 255, 0.5);
    }

    &:-webkit-autofill,
    &:-webkit-autofill:hover,
    &:-webkit-autofill:focus {
      -webkit-box-shadow: 0 0 0 30px rgba(0, 0, 0, 0.25) inset !important;
      -webkit-text-fill-color: #fff !important;
      background-color: rgba(255, 255, 255, 0.15) !important;
      transition: background-color 5000s ease-in-out 0s;
    }
  }

  .el-input__prefix {
    color: rgba(255, 255, 255, 0.5);
  }

  .el-select__wrapper {
    background: rgba(255, 255, 255, 0.15) !important;
    border-radius: 8px;
    box-shadow: none !important;
    border: 1px solid rgba(255, 255, 255, 0.2);
  }

  .el-select__placeholder {
    color: rgba(255, 255, 255, 0.5);
  }
}
</style>