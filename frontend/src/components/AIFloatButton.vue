<template>
  <div class="ai-float-button" @click="$emit('click')">
    <div class="float-button-content">
      <img :src="aiAvatar" alt="小伊" class="ai-avatar-img" />
    </div>
    <div class="float-button-label">小伊</div>
    <!-- 未读消息提示（可选） -->
    <div v-if="showBadge" class="float-badge">1</div>
  </div>
</template>

<script setup>
import aiAvatar from '@/assets/images/xiaoyi.png'

defineProps({
  showBadge: {
    type: Boolean,
    default: false
  }
})

defineEmits(['click'])
</script>

<style lang="scss" scoped>
.ai-float-button {
  position: fixed;
  right: 30px;
  bottom: 30px;
  z-index: 2000;
  cursor: pointer;
  display: flex;
  flex-direction: column;
  align-items: center;
  gap: 8px;
  transition: all 0.3s ease;

  &:hover {
    transform: scale(1.1);

    .float-button-content {
      box-shadow: 0 8px 24px rgba(47, 183, 155, 0.4);
    }
  }

  .float-button-content {
    width: 64px;
    height: 64px;
    border-radius: 50%;
    background: linear-gradient(135deg, #2FB79B 0%, #22A688 100%);
    display: flex;
    align-items: center;
    justify-content: center;
    box-shadow: 0 4px 16px rgba(47, 183, 155, 0.3);
    transition: all 0.3s ease;
    border: 3px solid #fff;

    .ai-avatar-img {
      width: 48px;
      height: 48px;
      border-radius: 50%;
      object-fit: cover;
    }
  }

  .float-button-label {
    background: #fff;
    padding: 4px 12px;
    border-radius: 12px;
    font-size: 12px;
    font-weight: 600;
    color: #2FB79B;
    box-shadow: 0 2px 8px rgba(0, 0, 0, 0.1);
    white-space: nowrap;
  }

  .float-badge {
    position: absolute;
    top: -5px;
    right: -5px;
    min-width: 20px;
    height: 20px;
    background: #ff4757;
    color: #fff;
    border-radius: 10px;
    font-size: 12px;
    font-weight: 600;
    display: flex;
    align-items: center;
    justify-content: center;
    padding: 0 6px;
    border: 2px solid #fff;
    animation: badge-pulse 2s infinite;
  }
}

@keyframes badge-pulse {
  0%, 100% {
    transform: scale(1);
  }
  50% {
    transform: scale(1.1);
  }
}
</style>