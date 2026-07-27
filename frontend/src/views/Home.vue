<template>
  <div class="home">
    <el-card>
      <template #header>
        <span>欢迎使用慧医数字医疗应用系统</span>
      </template>
      <div class="welcome">
        <h3>系统功能模块</h3>
        <el-row :gutter="20">
          <el-col :span="6">
            <div class="module-card">
              <el-icon size="40"><User /></el-icon>
              <p>用户管理</p>
            </div>
          </el-col>
          <el-col :span="6">
            <div class="module-card">
              <el-icon size="40"><FirstAidKit /></el-icon>
              <p>医生管理</p>
            </div>
          </el-col>
          <el-col :span="6">
            <div class="module-card">
              <el-icon size="40"><MedicineBox /></el-icon>
              <p>药品管理</p>
            </div>
          </el-col>
          <el-col :span="6">
            <div class="module-card">
              <el-icon size="40"><Document /></el-icon>
              <p>政策管理</p>
            </div>
          </el-col>
        </el-row>
      </div>
    </el-card>
  </div>
</template>

<script setup>
</script>

<style lang="scss" scoped>
.home {
  padding: 20px;

  .welcome {
    h3 {
      margin-bottom: 20px;
      color: #333;
    }

    .module-card {
      padding: 30px;
      text-align: center;
      background: #f5f7fa;
      border-radius: 8px;
      cursor: pointer;
      transition: all 0.3s;

      &:hover {
        background: #ecf5ff;
        transform: translateY(-5px);
      }

      p {
        margin-top: 15px;
        font-size: 16px;
        color: #666;
      }
    }
  }
}
</style>