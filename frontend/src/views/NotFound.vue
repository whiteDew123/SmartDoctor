<template>
  <el-card shadow="never" class="not-found">
    <el-result icon="warning" title="404" sub-title="抱歉，您访问的页面不存在">
      <template #extra>
        <el-button type="primary" @click="goHome">返回首页</el-button>
      </template>
    </el-result>
  </el-card>
</template>

<script setup>
import { useRouter } from 'vue-router'
const router = useRouter()
const goHome = () => router.push('/home')
</script>

<style lang="scss" scoped>
.not-found {
  margin: 40px auto;
  max-width: 720px;
}
</style>
