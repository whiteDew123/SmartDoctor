<template>
  <div class="module-placeholder">
    <el-card shadow="never">
      <template #header>
        <div class="card-header">
          <span class="title">销售地点管理</span>
        </div>
      </template>
      <el-empty description="销售地点管理 - 功能开发中" />
    </el-card>
  </div>
</template>

<script setup>
</script>

<style lang="scss" scoped>
.module-placeholder {
  padding: 20px;
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
