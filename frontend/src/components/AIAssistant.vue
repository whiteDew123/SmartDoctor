<template>
  <el-drawer
    v-model="visible"
    direction="rtl"
    size="50%"
    :close-on-click-modal="true"
    :show-close="true"
    class="ai-assistant-drawer"
  >
    <div class="ai-assistant-container">
      <!-- 左侧历史记录面板 -->
      <div :class="['history-panel', { 'hidden': !showHistory }]">
        <div class="history-header">
          <h3>对话历史</h3>
        </div>
        
        <div class="history-list">
          <div v-if="chatHistory.length === 0" class="empty-history">
            <el-icon :size="48" color="#d1d5db"><ChatDotRound /></el-icon>
            <p>暂无历史记录</p>
          </div>
          
          <div
            v-for="chat in chatHistory"
            :key="chat.id"
            :class="['history-item', { 'active': currentChatId === chat.id }]"
            @click="loadChat(chat.id)"
          >
            <div class="history-item-content">
              <div class="history-item-title">{{ chat.title }}</div>
              <div class="history-item-time">{{ formatChatTime(chat.updatedAt) }}</div>
            </div>
            <el-icon class="history-item-delete" @click.stop="deleteChat(chat.id)"><Delete /></el-icon>
          </div>
        </div>
      </div>

      <!-- 右侧聊天区域 -->
      <div class="chat-container">
        <!-- 聊天头部 -->
        <div class="chat-header">
          <el-button @click="showHistory = !showHistory" class="toggle-history-btn">
            <el-icon><Fold v-if="showHistory" /><Expand v-else /></el-icon>
            {{ showHistory ? '隐藏历史' : '历史记录' }}
          </el-button>
          <div class="chat-title">小伊 AI助手</div>
          <el-button @click="createNewChat" class="new-chat-btn-header">
            <el-icon><Plus /></el-icon>
            新对话
          </el-button>
        </div>

        <!-- 聊天消息列表 -->
        <div class="chat-messages" ref="messagesContainer">
        <div v-if="messages.length === 0" class="welcome-message">
          <div class="welcome-icon">
            <img src="@/assets/images/xiaoyi.png" alt="小伊" class="welcome-avatar-img" />
          </div>
          <p class="welcome-text">你好！我是小伊，你的AI助手</p>
          <p class="welcome-hint">有什么可以帮助你的吗？</p>
        </div>

        <div
          v-for="(msg, index) in messages"
          :key="index"
          :class="['message-item', msg.role === 'user' ? 'user-message' : 'ai-message']"
        >
          <div class="message-avatar">
            <img src="@/assets/images/xiaoyi.png" alt="小伊" class="avatar-img" />
          </div>
          <div class="message-content">
            <div class="message-bubble">
              <!-- 图片消息（多张） -->
              <div v-if="msg.images && msg.images.length > 0" class="message-images">
                <div v-for="(img, imgIndex) in msg.images" :key="imgIndex" class="message-image">
                  <el-image
                    :src="img"
                    fit="cover"
                    :preview-src-list="msg.images"
                    :initial-index="imgIndex"
                    preview-teleported
                    class="chat-image"
                  />
                </div>
              </div>
              <!-- 文件消息（多个） -->
              <div v-else-if="msg.files && msg.files.length > 0" class="message-files">
                <div v-for="(file, fileIndex) in msg.files" :key="fileIndex" class="message-file">
                  <el-icon :size="24"><Document /></el-icon>
                  <div class="file-info">
                    <div class="file-name">{{ file.name }}</div>
                    <a :href="file.url" target="_blank" class="file-link">查看文件</a>
                  </div>
                </div>
              </div>
              <!-- 单张图片消息 -->
              <div v-else-if="msg.image" class="message-image">
                <el-image
                  :src="msg.image"
                  fit="cover"
                  :preview-src-list="[msg.image]"
                  preview-teleported
                  class="chat-image"
                />
              </div>
              <!-- 单个文件消息 -->
              <div v-else-if="msg.file" class="message-file">
                <el-icon :size="24"><Document /></el-icon>
                <div class="file-info">
                  <div class="file-name">{{ msg.fileName }}</div>
                  <a :href="msg.file" target="_blank" class="file-link">查看文件</a>
                </div>
              </div>
              <!-- 文本消息 -->
              <div v-else class="message-text">{{ msg.content }}</div>
            </div>
            <div class="message-time">{{ formatTime(msg.time) }}</div>
          </div>
        </div>

        <!-- 加载状态 -->
        <div v-if="loading" class="message-item ai-message">
          <div class="message-avatar">
            <img src="@/assets/images/xiaoyi.png" alt="小伊" class="avatar-img" />
          </div>
          <div class="message-content">
            <div class="message-bubble loading-bubble">
              <div class="loading-dots">
                <span></span>
                <span></span>
                <span></span>
              </div>
            </div>
          </div>
        </div>
      </div>

      <!-- 输入区域 -->
      <div class="chat-input-area">
        <!-- 上传按钮区域 -->
        <div class="upload-buttons">
          <el-upload
            :action="uploadUrl"
            :headers="uploadHeaders"
            :before-upload="beforeImageUpload"
            :on-success="handleImageUploadSuccess"
            :on-error="handleUploadError"
            :show-file-list="false"
            accept="image/jpeg,image/png,image/gif,image/webp"
            class="upload-btn"
          >
            <el-tooltip content="上传图片" placement="top">
              <el-button size="small" :icon="Picture" circle />
            </el-tooltip>
          </el-upload>

          <el-upload
            :action="uploadUrl"
            :headers="uploadHeaders"
            :before-upload="beforeFileUpload"
            :on-success="handleFileUploadSuccess"
            :on-error="handleUploadError"
            :show-file-list="false"
            class="upload-btn"
          >
            <el-tooltip content="上传文件" placement="top">
              <el-button size="small" :icon="Document" circle />
            </el-tooltip>
          </el-upload>
        </div>

        <!-- 待发送的图片预览列表 -->
        <div v-if="pendingImages.length > 0" class="pending-images-container">
          <div v-for="(img, index) in pendingImages" :key="index" class="pending-image-item">
            <img :src="img" alt="待发送图片" />
            <el-icon class="remove-pending" @click="removePendingImage(index)"><CircleClose /></el-icon>
          </div>
        </div>

        <!-- 待发送的文件预览列表 -->
        <div v-if="pendingFiles.length > 0" class="pending-files-container">
          <div v-for="(file, index) in pendingFiles" :key="index" class="pending-file-card">
            <el-icon :size="32" color="#2FB79B"><Document /></el-icon>
            <div class="pending-file-info">
              <div class="pending-file-name">{{ file.name }}</div>
              <div class="pending-file-size">{{ formatFileSize(file.size) }}</div>
            </div>
            <el-icon class="remove-pending" @click="removePendingFile(index)"><CircleClose /></el-icon>
          </div>
        </div>

        <el-input
          v-model="inputMessage"
          type="textarea"
          :rows="3"
          placeholder="输入消息... (Enter发送, Shift+Enter换行)"
          :disabled="loading"
          @keydown="handleKeyDown"
          resize="none"
          class="chat-input"
        />
        <el-button
          type="primary"
          :loading="loading"
          :disabled="!inputMessage.trim() && pendingImages.length === 0 && pendingFiles.length === 0"
          @click="sendMessage"
          class="send-btn"
        >
          <el-icon><Promotion /></el-icon>
          发送
        </el-button>
      </div>
    </div>
  </div>
  </el-drawer>
</template>

<script setup>
import { ref, nextTick, watch, computed, onMounted } from 'vue'
import { User, Promotion, Picture, Document, CircleClose, Plus, Delete, ChatDotRound, Fold, Expand } from '@element-plus/icons-vue'
import { ElMessage, ElMessageBox } from 'element-plus'
import { chatWithAI } from '@/api/ai'

const props = defineProps({
  modelValue: {
    type: Boolean,
    default: false
  }
})

const emit = defineEmits(['update:modelValue'])

const visible = ref(props.modelValue)
const messages = ref([])
const inputMessage = ref('')
const loading = ref(false)
const messagesContainer = ref(null)
const pendingImages = ref([])
const pendingFiles = ref([])

// 聊天记录相关
const chatHistory = ref([])
const currentChatId = ref('')
const showHistory = ref(true)

const MAX_PENDING_ITEMS = 3
const STORAGE_KEY = 'ai_chat_history'

const uploadUrl = '/api/base/upload'
const uploadHeaders = computed(() => ({
  token: localStorage.getItem('token') || ''
}))

// 生成唯一ID
const generateId = () => {
  return Date.now().toString(36) + Math.random().toString(36).substr(2)
}

// 从localStorage加载聊天记录
const loadChatHistory = () => {
  try {
    const stored = localStorage.getItem(STORAGE_KEY)
    if (stored) {
      chatHistory.value = JSON.parse(stored)
    }
  } catch (error) {
    console.error('加载聊天记录失败:', error)
    chatHistory.value = []
  }
}

// 保存聊天记录到localStorage
const saveChatHistory = () => {
  try {
    localStorage.setItem(STORAGE_KEY, JSON.stringify(chatHistory.value))
  } catch (error) {
    console.error('保存聊天记录失败:', error)
  }
}

// 创建新对话
const createNewChat = () => {
  const newChat = {
    id: generateId(),
    title: '新对话',
    messages: [],
    createdAt: Date.now(),
    updatedAt: Date.now()
  }
  
  chatHistory.value.unshift(newChat)
  currentChatId.value = newChat.id
  messages.value = []
  pendingImages.value = []
  pendingFiles.value = []
  inputMessage.value = ''
  
  saveChatHistory()
}

// 加载指定对话
const loadChat = (chatId) => {
  const chat = chatHistory.value.find(c => c.id === chatId)
  if (chat) {
    currentChatId.value = chatId
    messages.value = chat.messages || []
    pendingImages.value = []
    pendingFiles.value = []
    inputMessage.value = ''
    showHistory.value = false
    scrollToBottom()
  }
}

// 删除对话
const deleteChat = async (chatId) => {
  try {
    await ElMessageBox.confirm('确定要删除这个对话吗？', '提示', {
      confirmButtonText: '确定',
      cancelButtonText: '取消',
      type: 'warning'
    })
    
    chatHistory.value = chatHistory.value.filter(c => c.id !== chatId)
    saveChatHistory()
    
    // 如果删除的是当前对话，创建新对话
    if (currentChatId.value === chatId) {
      if (chatHistory.value.length > 0) {
        loadChat(chatHistory.value[0].id)
      } else {
        createNewChat()
      }
    }
    
    ElMessage.success('删除成功')
  } catch {
    // 用户取消
  }
}

// 更新当前对话标题和消息
const updateCurrentChat = () => {
  if (!currentChatId.value) {
    createNewChat()
    return
  }
  
  const chatIndex = chatHistory.value.findIndex(c => c.id === currentChatId.value)
  if (chatIndex !== -1) {
    chatHistory.value[chatIndex].messages = messages.value
    chatHistory.value[chatIndex].updatedAt = Date.now()
    
    // 根据第一条消息更新标题
    if (chatHistory.value[chatIndex].title === '新对话' && messages.value.length > 0) {
      const firstMessage = messages.value.find(m => m.role === 'user')
      if (firstMessage) {
        const title = firstMessage.content.substring(0, 20)
        chatHistory.value[chatIndex].title = title + (firstMessage.content.length > 20 ? '...' : '')
      }
    }
    
    saveChatHistory()
  }
}

// 格式化对话时间
const formatChatTime = (timestamp) => {
  const date = new Date(timestamp)
  const now = new Date()
  const diff = now - date
  
  // 今天
  if (diff < 24 * 60 * 60 * 1000) {
    const hours = String(date.getHours()).padStart(2, '0')
    const minutes = String(date.getMinutes()).padStart(2, '0')
    return `${hours}:${minutes}`
  }
  
  // 昨天
  if (diff < 48 * 60 * 60 * 1000) {
    return '昨天'
  }
  
  // 更早
  const month = date.getMonth() + 1
  const day = date.getDate()
  return `${month}/${day}`
}

// 组件挂载时加载历史记录
onMounted(() => {
  loadChatHistory()
  
  // 如果没有历史记录，创建新对话
  if (chatHistory.value.length === 0) {
    createNewChat()
  } else {
    // 加载最新的对话
    loadChat(chatHistory.value[0].id)
  }
})

const formatFileSize = (bytes) => {
  if (bytes === 0) return '0 B'
  const k = 1024
  const sizes = ['B', 'KB', 'MB', 'GB']
  const i = Math.floor(Math.log(bytes) / Math.log(k))
  return Math.round(bytes / Math.pow(k, i) * 100) / 100 + ' ' + sizes[i]
}

watch(() => props.modelValue, (val) => {
  visible.value = val
})

watch(visible, (val) => {
  emit('update:modelValue', val)
})

const formatTime = (timestamp) => {
  const date = new Date(timestamp)
  const hours = String(date.getHours()).padStart(2, '0')
  const minutes = String(date.getMinutes()).padStart(2, '0')
  return `${hours}:${minutes}`
}

const scrollToBottom = async () => {
  await nextTick()
  if (messagesContainer.value) {
    messagesContainer.value.scrollTop = messagesContainer.value.scrollHeight
  }
}

const handleKeyDown = (e) => {
  if (e.key === 'Enter' && !e.shiftKey) {
    e.preventDefault()
    sendMessage()
  }
}

const beforeImageUpload = (file) => {
  const isImage = file.type.startsWith('image/')
  const isLt5M = file.size / 1024 / 1024 < 5

  if (!isImage) {
    ElMessage.error('只能上传图片文件!')
    return false
  }
  if (!isLt5M) {
    ElMessage.error('图片大小不能超过 5MB!')
    return false
  }
  if (pendingImages.value.length >= MAX_PENDING_ITEMS) {
    ElMessage.warning(`最多只能同时上传${MAX_PENDING_ITEMS}个图片`)
    return false
  }

  // 显示图片预览
  const reader = new FileReader()
  reader.onload = (e) => {
    pendingImages.value.push(e.target.result)
  }
  reader.readAsDataURL(file)
  return true
}

const beforeFileUpload = (file) => {
  const isLt10M = file.size / 1024 / 1024 < 10

  if (!isLt10M) {
    ElMessage.error('文件大小不能超过 10MB!')
    return false
  }
  if (pendingFiles.value.length >= MAX_PENDING_ITEMS) {
    ElMessage.warning(`最多只能同时上传${MAX_PENDING_ITEMS}个文件`)
    return false
  }

  // 暂存文件信息
  pendingFiles.value.push({
    url: '',
    name: file.name,
    size: file.size,
    tempId: Date.now() + Math.random()
  })
  return true
}

const handleImageUploadSuccess = (response) => {
  if (response.code === 20000) {
    ElMessage.success('图片上传成功')
  } else {
    ElMessage.error('图片上传失败')
  }
}

const handleFileUploadSuccess = (response, file) => {
  if (response.code === 20000) {
    ElMessage.success('文件上传成功')
    // 更新暂存文件的URL
    const tempFile = pendingFiles.value.find(f => f.name === file.name)
    if (tempFile) {
      tempFile.url = response.data.url
    }
  } else {
    ElMessage.error('文件上传失败')
  }
}

const handleUploadError = () => {
  ElMessage.error('上传失败，请重试')
}

const removePendingImage = (index) => {
  pendingImages.value.splice(index, 1)
}

const removePendingFile = (index) => {
  pendingFiles.value.splice(index, 1)
}

const sendMessage = async () => {
  const content = inputMessage.value.trim()
  const hasImages = pendingImages.value.length > 0
  const hasFiles = pendingFiles.value.length > 0
  
  if ((!content && !hasImages && !hasFiles) || loading.value) return

  // 添加用户消息
  const userMessage = {
    role: 'user',
    time: Date.now()
  }

  if (hasImages) {
    userMessage.images = [...pendingImages.value]
    userMessage.content = `[${pendingImages.value.length}张图片]` + (content ? '\n' + content : '')
  }
  
  if (hasFiles) {
    userMessage.files = pendingFiles.value.map(f => ({
      url: f.url,
      name: f.name
    }))
    const fileNames = pendingFiles.value.map(f => f.name).join('、')
    userMessage.content = (userMessage.content ? userMessage.content + '\n' : '') + `[文件: ${fileNames}]`
  }
  
  if (!hasImages && !hasFiles) {
    userMessage.content = content
  }

  messages.value.push(userMessage)

  inputMessage.value = ''
  pendingImages.value = []
  pendingFiles.value = []
  loading.value = true
  await scrollToBottom()

  try {
    // 构建消息历史（保留最近10条消息作为上下文）
    const recentMessages = messages.value
      .filter(msg => msg.role !== 'system')
      .slice(-10)
      .map(msg => ({
        role: msg.role,
        content: msg.content
      }))

    // 添加系统提示词
    const apiMessages = [
      {
        role: 'system',
        content: '你是小伊，一个友好、专业的AI助手。请用简洁明了的语言回答问题。如果用户发送了图片，请描述你看到的图片内容。如果用户上传了文件，请告知已收到文件并询问用户需要什么帮助。'
      },
      ...recentMessages
    ]

    // 调用AI API
    const response = await chatWithAI(apiMessages)
    
    const aiContent = response.data.choices?.[0]?.message?.content || '抱歉，我暂时无法回答这个问题。'
    
    messages.value.push({
      role: 'assistant',
      content: aiContent,
      time: Date.now()
    })
  } catch (error) {
    console.error('AI聊天错误:', error)
    ElMessage.error('发送消息失败，请稍后重试')
    
    messages.value.push({
      role: 'assistant',
      content: '抱歉，出现了错误，请稍后重试。',
      time: Date.now()
    })
  } finally {
    loading.value = false
    await scrollToBottom()
    // 保存聊天记录
    updateCurrentChat()
  }
}
</script>

<style lang="scss" scoped>
.ai-assistant-drawer {
  :deep(.el-drawer__header) {
    margin-bottom: 0;
    padding: 0;
    border-bottom: none;
  }

  :deep(.el-drawer__title) {
    display: none;
  }

  :deep(.el-drawer__body) {
    padding: 0;
    display: flex;
    flex-direction: column;
    overflow: hidden;
  }

  :deep(.el-drawer) {
    border-radius: 12px 0 0 12px;
    box-shadow: -4px 0 20px rgba(0, 0, 0, 0.15);
  }
}

.ai-assistant-container {
  display: flex;
  height: 100%;
  position: relative;
  background: #f8fafb;
}

.history-panel {
  width: 240px;
  flex-shrink: 0;
  background: linear-gradient(180deg, #ffffff 0%, #f8fafb 100%);
  border-right: 1px solid #e5e7eb;
  display: flex;
  flex-direction: column;
  transition: width 0.3s cubic-bezier(0.4, 0, 0.2, 1);
  box-shadow: 2px 0 8px rgba(0, 0, 0, 0.05);
  overflow: hidden;

  &.hidden {
    width: 0 !important;
    border-right: none;
    box-shadow: none;
    
    .history-header,
    .history-list {
      opacity: 0;
      visibility: hidden;
      pointer-events: none;
    }
  }

  .history-header {
    padding: 24px 20px 16px;
    background: linear-gradient(135deg, #2FB79B 0%, #1a9c84 100%);
    color: #fff;
    position: relative;
    transition: opacity 0.2s ease, visibility 0.2s ease;
    flex-shrink: 0;

    &::after {
      content: '';
      position: absolute;
      bottom: 0;
      left: 0;
      right: 0;
      height: 3px;
      background: linear-gradient(90deg, transparent, rgba(255,255,255,0.3), transparent);
    }

    h3 {
      margin: 0;
      font-size: 18px;
      font-weight: 600;
      letter-spacing: 0.5px;
    }
  }

  .history-list {
    flex: 1;
    overflow-y: auto;
    padding: 16px 12px;
    transition: opacity 0.2s ease, visibility 0.2s ease;

    &::-webkit-scrollbar {
      width: 6px;
    }

    &::-webkit-scrollbar-thumb {
      background: rgba(47, 183, 155, 0.3);
      border-radius: 3px;

      &:hover {
        background: rgba(47, 183, 155, 0.5);
      }
    }

    .empty-history {
      display: flex;
      flex-direction: column;
      align-items: center;
      justify-content: center;
      padding: 80px 20px;
      text-align: center;
      color: #9ca3af;

      p {
        margin-top: 16px;
        font-size: 14px;
        color: #6b7280;
      }
    }

    .history-item {
      display: flex;
      align-items: center;
      padding: 14px 12px;
      margin-bottom: 8px;
      border-radius: 10px;
      cursor: pointer;
      transition: all 0.25s cubic-bezier(0.4, 0, 0.2, 1);
      background: #fff;
      border: 1px solid transparent;
      box-shadow: 0 1px 3px rgba(0, 0, 0, 0.05);

      &:hover {
        background: #ecfdf5;
        border-color: #2FB79B;
        transform: translateX(4px);
        box-shadow: 0 2px 8px rgba(47, 183, 155, 0.15);

        .history-item-delete {
          opacity: 1;
        }
      }

      &.active {
        background: linear-gradient(135deg, #2FB79B 0%, #22A688 100%);
        color: #fff;
        border-color: transparent;
        box-shadow: 0 4px 12px rgba(47, 183, 155, 0.3);
        transform: translateX(0);

        .history-item-title {
          color: #fff;
          font-weight: 600;
        }

        .history-item-time {
          color: rgba(255, 255, 255, 0.85);
        }

        .history-item-delete {
          color: #fff;
          opacity: 1;
        }
      }

      .history-item-content {
        flex: 1;
        min-width: 0;

        .history-item-title {
          font-size: 14px;
          font-weight: 500;
          color: #1f2937;
          margin-bottom: 6px;
          overflow: hidden;
          text-overflow: ellipsis;
          white-space: nowrap;
        }

        .history-item-time {
          font-size: 12px;
          color: #9ca3af;
        }
      }

      .history-item-delete {
        width: 20px;
        height: 20px;
        color: #9ca3af;
        opacity: 0;
        cursor: pointer;
        transition: all 0.2s;
        flex-shrink: 0;

        &:hover {
          color: #ff4757;
          transform: scale(1.1);
        }
      }
    }
  }
}

.chat-container {
  flex: 1;
  display: flex;
  flex-direction: column;
  height: 100%;
  background: linear-gradient(180deg, #f8fafb 0%, #eef2f5 100%);
}

.chat-header {
  display: flex;
  align-items: center;
  padding: 18px 24px;
  background: linear-gradient(135deg, #2FB79B 0%, #1a9c84 100%);
  color: #fff;
  gap: 12px;
  box-shadow: 0 2px 12px rgba(47, 183, 155, 0.25);
  position: relative;

  &::after {
    content: '';
    position: absolute;
    bottom: 0;
    left: 0;
    right: 0;
    height: 2px;
    background: linear-gradient(90deg, transparent, rgba(255,255,255,0.4), transparent);
  }

  .toggle-history-btn {
    background: rgba(255, 255, 255, 0.15);
    border: 1px solid rgba(255, 255, 255, 0.3);
    color: #fff;
    border-radius: 8px;
    padding: 8px 14px;
    transition: all 0.2s;

    &:hover {
      background: rgba(255, 255, 255, 0.25);
      transform: translateY(-1px);
      box-shadow: 0 2px 8px rgba(0, 0, 0, 0.1);
    }
  }

  .chat-title {
    flex: 1;
    font-size: 18px;
    font-weight: 600;
    text-align: center;
    letter-spacing: 0.5px;
    text-shadow: 0 1px 2px rgba(0, 0, 0, 0.1);
  }

  .new-chat-btn-header {
    background: rgba(255, 255, 255, 0.15);
    border: 1px solid rgba(255, 255, 255, 0.3);
    color: #fff;
    border-radius: 8px;
    padding: 8px 14px;
    transition: all 0.2s;

    &:hover {
      background: rgba(255, 255, 255, 0.25);
      transform: translateY(-1px);
      box-shadow: 0 2px 8px rgba(0, 0, 0, 0.1);
    }
  }
}

.chat-messages {
  flex: 1;
  overflow-y: auto;
  padding: 24px;
  background: linear-gradient(180deg, #f8fafb 0%, #eef2f5 100%);

  &::-webkit-scrollbar {
    width: 6px;
  }

  &::-webkit-scrollbar-thumb {
    background: rgba(47, 183, 155, 0.3);
    border-radius: 3px;

    &:hover {
      background: rgba(47, 183, 155, 0.5);
    }
  }
}

.welcome-message {
  display: flex;
  flex-direction: column;
  align-items: center;
  justify-content: center;
  padding: 80px 20px;
  text-align: center;
  animation: fadeInUp 0.6s ease-out;

  .welcome-icon {
    margin-bottom: 20px;
    animation: float 3s ease-in-out infinite;
    
    .welcome-avatar-img {
      width: 90px;
      height: 90px;
      border-radius: 50%;
      box-shadow: 0 8px 24px rgba(47, 183, 155, 0.3);
    }
  }

  .welcome-text {
    font-size: 20px;
    font-weight: 600;
    background: linear-gradient(135deg, #2FB79B 0%, #1a9c84 100%);
    -webkit-background-clip: text;
    -webkit-text-fill-color: transparent;
    background-clip: text;
    margin: 0 0 8px 0;
  }

  .welcome-hint {
    font-size: 14px;
    color: #6b7280;
    margin: 0;
  }
}

@keyframes float {
  0%, 100% {
    transform: translateY(0);
  }
  50% {
    transform: translateY(-10px);
  }
}

@keyframes fadeInUp {
  from {
    opacity: 0;
    transform: translateY(20px);
  }
  to {
    opacity: 1;
    transform: translateY(0);
  }
}

.message-item {
  display: flex;
  margin-bottom: 24px;
  gap: 12px;
  animation: fadeInUp 0.3s ease-out;

  &.user-message {
    flex-direction: row-reverse;

    .message-bubble {
      background: linear-gradient(135deg, #2FB79B 0%, #1a9c84 100%);
      color: #fff;
      border-radius: 18px 6px 18px 18px;
      box-shadow: 0 4px 12px rgba(47, 183, 155, 0.3);
    }

    .message-time {
      text-align: right;
    }
  }

  &.ai-message {
    .message-bubble {
      background: #fff;
      color: #1f2937;
      border-radius: 6px 18px 18px 18px;
      box-shadow: 0 2px 12px rgba(0, 0, 0, 0.08);
      border: 1px solid rgba(47, 183, 155, 0.1);
    }
  }
}

.message-avatar {
  width: 40px;
  height: 40px;
  border-radius: 50%;
  background: #fff;
  display: flex;
  align-items: center;
  justify-content: center;
  flex-shrink: 0;
  overflow: hidden;
  box-shadow: 0 2px 8px rgba(0, 0, 0, 0.1);
  border: 2px solid rgba(47, 183, 155, 0.2);

  .avatar-img {
    width: 100%;
    height: 100%;
    object-fit: cover;
  }
}

.message-content {
  max-width: 70%;
  display: flex;
  flex-direction: column;
  gap: 6px;
}

.message-bubble {
  padding: 14px 18px;
  font-size: 14px;
  line-height: 1.7;
  word-break: break-word;

  .message-images {
    display: flex;
    flex-direction: column;
    gap: 10px;
    margin-top: 10px;

    .message-image {
      .chat-image {
        max-width: 280px;
        max-height: 280px;
        border-radius: 10px;
        cursor: pointer;
        display: block;
        transition: transform 0.2s;

        &:hover {
          transform: scale(1.02);
        }
      }
    }
  }

  .message-files {
    display: flex;
    flex-direction: column;
    gap: 10px;
    margin-top: 10px;

    .message-file {
      display: flex;
      align-items: center;
      gap: 14px;
      padding: 14px;
      background: rgba(248, 250, 251, 0.9);
      border-radius: 10px;
      border: 1px solid rgba(47, 183, 155, 0.2);
      transition: all 0.2s;

      &:hover {
        background: #fff;
        border-color: #2FB79B;
        box-shadow: 0 2px 8px rgba(47, 183, 155, 0.15);
      }

      .el-icon {
        color: #2FB79B;
        flex-shrink: 0;
      }

      .file-info {
        flex: 1;
        min-width: 0;

        .file-name {
          font-size: 13px;
          font-weight: 500;
          color: #1f2937;
          margin-bottom: 6px;
          overflow: hidden;
          text-overflow: ellipsis;
          white-space: nowrap;
        }

        .file-link {
          font-size: 12px;
          color: #2FB79B;
          text-decoration: none;
          font-weight: 500;

          &:hover {
            text-decoration: underline;
          }
        }
      }
    }
  }

  .message-image {
    margin-top: 10px;

    .chat-image {
      max-width: 280px;
      max-height: 280px;
      border-radius: 10px;
      cursor: pointer;
      display: block;
      transition: transform 0.2s;

      &:hover {
        transform: scale(1.02);
      }
    }
  }

  .message-file {
    display: flex;
    align-items: center;
    gap: 14px;
    padding: 14px;
    background: rgba(248, 250, 251, 0.9);
    border-radius: 10px;
    margin-top: 10px;
    border: 1px solid rgba(47, 183, 155, 0.2);
    transition: all 0.2s;

    &:hover {
      background: #fff;
      border-color: #2FB79B;
      box-shadow: 0 2px 8px rgba(47, 183, 155, 0.15);
    }

    .el-icon {
      color: #2FB79B;
      flex-shrink: 0;
    }

    .file-info {
      flex: 1;
      min-width: 0;

      .file-name {
        font-size: 13px;
        font-weight: 500;
        color: #1f2937;
        margin-bottom: 6px;
        overflow: hidden;
        text-overflow: ellipsis;
        white-space: nowrap;
      }

      .file-link {
        font-size: 12px;
        color: #2FB79B;
        text-decoration: none;
        font-weight: 500;

        &:hover {
          text-decoration: underline;
        }
      }
    }
  }
}

.message-time {
  font-size: 12px;
  color: #9ca3af;
  padding: 0 6px;
}

.loading-bubble {
  padding: 14px 22px;
}

.loading-dots {
  display: flex;
  gap: 8px;
  align-items: center;

  span {
    width: 9px;
    height: 9px;
    border-radius: 50%;
    background: linear-gradient(135deg, #2FB79B 0%, #1a9c84 100%);
    animation: loading 1.4s infinite;

    &:nth-child(2) {
      animation-delay: 0.2s;
    }

    &:nth-child(3) {
      animation-delay: 0.4s;
    }
  }
}

@keyframes loading {
  0%, 80%, 100% {
    opacity: 0.3;
    transform: scale(0.8);
  }
  40% {
    opacity: 1;
    transform: scale(1);
  }
}

.chat-input-area {
  padding: 18px 20px;
  background: linear-gradient(180deg, #ffffff 0%, #f8fafb 100%);
  border-top: 1px solid #e5e7eb;
  display: flex;
  flex-direction: column;
  gap: 14px;
  box-shadow: 0 -2px 12px rgba(0, 0, 0, 0.05);

  .upload-buttons {
    display: flex;
    gap: 10px;
    align-items: center;

    .upload-btn {
      :deep(.el-upload) {
        border: none;
        cursor: pointer;
      }

      .el-button {
        background: linear-gradient(135deg, #f8fafb 0%, #eef2f5 100%);
        border: 1px solid #e5e7eb;
        color: #2FB79B;
        transition: all 0.2s;

        &:hover {
          background: linear-gradient(135deg, #ecfdf5 0%, #d1fae5 100%);
          border-color: #2FB79B;
          transform: translateY(-2px);
          box-shadow: 0 4px 12px rgba(47, 183, 155, 0.2);
        }
      }
    }
  }

  .pending-images-container {
    display: grid;
    grid-template-columns: repeat(auto-fill, minmax(130px, 1fr));
    gap: 10px;
    width: 100%;

    .pending-image-item {
      position: relative;
      width: 100%;
      height: 130px;
      border-radius: 10px;
      overflow: hidden;
      border: 2px solid #2FB79B;
      box-shadow: 0 2px 8px rgba(47, 183, 155, 0.2);
      transition: all 0.2s;

      &:hover {
        transform: translateY(-2px);
        box-shadow: 0 4px 12px rgba(47, 183, 155, 0.3);
      }

      img {
        width: 100%;
        height: 100%;
        object-fit: cover;
      }

      .remove-pending {
        position: absolute;
        top: 6px;
        right: 6px;
        width: 22px;
        height: 22px;
        background: rgba(0, 0, 0, 0.6);
        color: #fff;
        border-radius: 50%;
        cursor: pointer;
        display: flex;
        align-items: center;
        justify-content: center;
        transition: all 0.3s;

        &:hover {
          background: rgba(255, 71, 87, 0.9);
          transform: scale(1.15);
        }
      }
    }
  }

  .pending-files-container {
    display: flex;
    flex-direction: column;
    gap: 10px;
    width: 100%;

    .pending-file-card {
      display: flex;
      align-items: center;
      gap: 14px;
      padding: 14px 18px;
      background: linear-gradient(135deg, #f8fafb 0%, #eef2f5 100%);
      border: 2px solid #2FB79B;
      border-radius: 10px;
      position: relative;
      transition: all 0.2s;

      &:hover {
        transform: translateY(-2px);
        box-shadow: 0 4px 12px rgba(47, 183, 155, 0.2);
      }

      .pending-file-info {
        flex: 1;
        min-width: 0;

        .pending-file-name {
          font-size: 14px;
          font-weight: 500;
          color: #1f2937;
          margin-bottom: 6px;
          overflow: hidden;
          text-overflow: ellipsis;
          white-space: nowrap;
        }

        .pending-file-size {
          font-size: 12px;
          color: #6b7280;
        }
      }

      .remove-pending {
        width: 26px;
        height: 26px;
        color: #9ca3af;
        cursor: pointer;
        display: flex;
        align-items: center;
        justify-content: center;
        transition: all 0.3s;

        &:hover {
          color: #ff4757;
          transform: scale(1.15);
        }
      }
    }
  }
}

.chat-input {
  :deep(.el-textarea__inner) {
    border: 2px solid #e5e7eb;
    border-radius: 12px;
    padding: 14px;
    font-size: 14px;
    resize: none;
    transition: all 0.3s;

    &:focus {
      border-color: #2FB79B;
      box-shadow: 0 0 0 3px rgba(47, 183, 155, 0.1);
    }

    &:hover:not(:focus) {
      border-color: #d1d5db;
    }
  }
}

.send-btn {
  align-self: flex-end;
  background: linear-gradient(135deg, #2FB79B 0%, #1a9c84 100%);
  border: none;
  border-radius: 10px;
  padding: 12px 28px;
  font-weight: 500;
  transition: all 0.3s;
  box-shadow: 0 2px 8px rgba(47, 183, 155, 0.25);

  &:hover:not(:disabled) {
    transform: translateY(-2px);
    box-shadow: 0 4px 16px rgba(47, 183, 155, 0.35);
  }

  &:active:not(:disabled) {
    transform: translateY(0);
  }

  &:disabled {
    opacity: 0.5;
    cursor: not-allowed;
  }
}
</style>