<template>
  <div class="module-placeholder">
    <el-card shadow="never">
      <template #header>
        <div class="card-header">
          <span class="title">医药公司</span>
        </div>
      </template>
      <el-empty description="医药公司管理 - 功能开发中" />
    </el-card>
  </div>
</template>

<script setup>
</script>

<style lang="scss" scoped>
.module-placeholder {
  .card-header {
    display: flex;
    align-items: center;
    justify-content: space-between;

    .title {
      font-size: 16px;
      font-weight: 600;
      color: #303133;
    }
  }
}
</style>
