<template>
  <div class="drug-management">
    <!-- 面包屑 -->
    <el-breadcrumb separator="/" class="breadcrumb">
      <el-breadcrumb-item :to="{ path: '/home' }">首页</el-breadcrumb-item>
      <el-breadcrumb-item>药品信息管理</el-breadcrumb-item>
    </el-breadcrumb>

    <el-card shadow="never" class="main-card">
      <template #header>
        <div class="card-header">
          <span class="title">药品信息列表</span>
          <el-button type="primary" :icon="Plus" @click="handleAdd">
            添加药品
          </el-button>
        </div>
      </template>

      <!-- 搜索框 -->
      <el-input
        v-model="searchQuery"
        placeholder="请输入要查询的药品名称"
        clearable
        class="search-input"
        @clear="handleSearch"
        @keyup.enter="handleSearch"
      >
        <template #append>
          <el-button :icon="Search" @click="handleSearch">查询</el-button>
        </template>
      </el-input>

      <!-- 表格 -->
      <el-table
        :data="filteredList"
        v-loading="drugStore.loading"
        style="width: 100%"
        border
        stripe
      >
        <el-table-column type="expand" width="50">
          <template #default="{ row }">
            <div class="expand-content">
              <p><strong>药品功效：</strong>{{ row.drugEffect || '暂无' }}</p>
              <p><strong>药品说明：</strong>{{ row.drugInfo || '暂无' }}</p>
            </div>
          </template>
        </el-table-column>
        <el-table-column prop="drugId" label="药品编号" width="120" sortable />
        <el-table-column label="药品图片" width="120" align="center">
          <template #default="{ row }">
            <el-image
              v-if="row.drugImg"
              :src="row.drugImg"
              fit="cover"
              style="width: 70px; height: 70px; border-radius: 4px"
              :preview-src-list="[row.drugImg]"
              preview-teleported
            />
            <span v-else class="no-img">无图片</span>
          </template>
        </el-table-column>
        <el-table-column prop="drugName" label="药品名称" min-width="160" show-overflow-tooltip />
        <el-table-column label="销售地点" min-width="200" show-overflow-tooltip>
          <template #default="{ row }">
            <span>{{ formatSaleLocations(row.saleIds) }}</span>
          </template>
        </el-table-column>
        <el-table-column prop="publisher" label="发布者" width="120" />
        <el-table-column label="操作" width="150" fixed="right" align="center">
          <template #default="{ row }">
            <el-button link type="primary" :icon="Edit" @click="handleEdit(row)">
              编辑
            </el-button>
            <el-button link type="danger" :icon="Delete" @click="handleDelete(row)">
              删除
            </el-button>
          </template>
        </el-table-column>
      </el-table>

      <!-- 分页 -->
      <el-pagination
        v-model:current-page="pageNum"
        v-model:page-size="pageSize"
        :page-sizes="[5, 10, 20, 50]"
        :total="drugStore.total"
        layout="total, sizes, prev, pager, next, jumper"
        class="pagination"
        @size-change="handleSizeChange"
        @current-change="handleCurrentChange"
      />
    </el-card>

    <!-- 添加药品弹窗 -->
    <el-dialog
      v-model="addDialogVisible"
      title="添加药品"
      width="600px"
      :close-on-click-modal="false"
      destroy-on-close
    >
      <el-form :model="addForm" label-width="100px" :rules="rules" ref="addFormRef">
        <el-form-item label="药品名称" prop="drugName">
          <el-input v-model="addForm.drugName" placeholder="请输入药品名称" />
        </el-form-item>
        <el-form-item label="药品功效" prop="drugEffect">
          <el-input
            v-model="addForm.drugEffect"
            type="textarea"
            :rows="3"
            placeholder="请输入药品功效"
          />
        </el-form-item>
        <el-form-item label="药品说明" prop="drugInfo">
          <el-input
            v-model="addForm.drugInfo"
            type="textarea"
            :rows="3"
            placeholder="请输入药品说明"
          />
        </el-form-item>
        <el-form-item label="销售地点" prop="saleIds">
          <el-select
            v-model="addForm.saleIds"
            multiple
            placeholder="请选择销售地点"
            style="width: 100%"
          >
            <el-option
              v-for="sale in drugStore.saleList"
              :key="sale.saleId"
              :label="sale.saleName"
              :value="sale.saleId"
            />
          </el-select>
        </el-form-item>
        <el-form-item label="药品图片">
          <el-upload
            class="avatar-uploader"
            action="/api/base/upload"
            :headers="uploadHeaders"
            :before-upload="beforeImgUpload"
            :on-success="handleUploadSuccess"
            :on-error="handleUploadError"
            :show-file-list="false"
          >
            <img v-if="addForm.drugImg" :src="addForm.drugImg" class="avatar" />
            <el-icon v-else class="avatar-uploader-icon"><Plus /></el-icon>
          </el-upload>
        </el-form-item>
      </el-form>
      <template #footer>
        <el-button @click="addDialogVisible = false">取消</el-button>
        <el-button type="primary" @click="submitAdd">确定</el-button>
      </template>
    </el-dialog>

    <!-- 修改药品弹窗 -->
    <el-dialog
      v-model="editDialogVisible"
      title="修改药品"
      width="600px"
      :close-on-click-modal="false"
      destroy-on-close
    >
      <el-form :model="editForm" label-width="100px" :rules="rules" ref="editFormRef">
        <el-form-item label="药品名称" prop="drugName">
          <el-input v-model="editForm.drugName" placeholder="请输入药品名称" />
        </el-form-item>
        <el-form-item label="药品功效" prop="drugEffect">
          <el-input
            v-model="editForm.drugEffect"
            type="textarea"
            :rows="3"
            placeholder="请输入药品功效"
          />
        </el-form-item>
        <el-form-item label="药品说明" prop="drugInfo">
          <el-input
            v-model="editForm.drugInfo"
            type="textarea"
            :rows="3"
            placeholder="请输入药品说明"
          />
        </el-form-item>
        <el-form-item label="销售地点" prop="saleIds">
          <el-select
            v-model="editForm.saleIds"
            multiple
            placeholder="请选择销售地点"
            style="width: 100%"
          >
            <el-option
              v-for="sale in drugStore.saleList"
              :key="sale.saleId"
              :label="sale.saleName"
              :value="sale.saleId"
            />
          </el-select>
        </el-form-item>
        <el-form-item label="药品图片">
          <el-upload
            class="avatar-uploader"
            action="/api/base/upload"
            :headers="uploadHeaders"
            :before-upload="beforeImgUpload"
            :on-success="handleUploadSuccess"
            :on-error="handleUploadError"
            :show-file-list="false"
          >
            <img v-if="editForm.drugImg" :src="editForm.drugImg" class="avatar" />
            <el-icon v-else class="avatar-uploader-icon"><Plus /></el-icon>
          </el-upload>
        </el-form-item>
      </el-form>
      <template #footer>
        <el-button @click="editDialogVisible = false">取消</el-button>
        <el-button type="primary" @click="submitEdit">确定</el-button>
      </template>
    </el-dialog>
  </div>
</template>

<script setup>
import { ref, computed, onMounted } from 'vue'
import { ElMessage, ElMessageBox } from 'element-plus'
import { Plus, Search, Edit, Delete } from '@element-plus/icons-vue'
import { useDrugStore } from '@/store/modules/drug'

const drugStore = useDrugStore()

const searchQuery = ref('')
const pageNum = ref(1)
const pageSize = ref(10)

const addDialogVisible = ref(false)
const editDialogVisible = ref(false)
const addFormRef = ref()
const editFormRef = ref()

const addForm = ref({
  drugName: '',
  drugInfo: '',
  drugEffect: '',
  drugImg: '',
  saleIds: []
})

const editForm = ref({
  drugId: null,
  drugName: '',
  drugInfo: '',
  drugEffect: '',
  drugImg: '',
  saleIds: []
})

const rules = {
  drugName: [{ required: true, message: '请输入药品名称', trigger: 'blur' }]
}

const uploadHeaders = computed(() => ({
  token: localStorage.getItem('token') || ''
}))

const filteredList = computed(() => {
  if (!searchQuery.value) return drugStore.drugList
  const query = searchQuery.value.trim().toLowerCase()
  return drugStore.drugList.filter(
    (item) => item.drugName && item.drugName.toLowerCase().includes(query)
  )
})

const formatSaleLocations = (saleIds) => {
  if (!saleIds || !saleIds.length) return '-'
  return saleIds
    .map((id) => {
      const sale = drugStore.saleList.find((s) => s.saleId === id)
      return sale ? sale.saleName : id
    })
    .join(' ')
}

const loadData = async () => {
  await drugStore.fetchDrugs(pageNum.value, pageSize.value)
}

const handleSizeChange = (val) => {
  pageSize.value = val
  pageNum.value = 1
  loadData()
}

const handleCurrentChange = (val) => {
  pageNum.value = val
  loadData()
}

const handleSearch = () => {
  pageNum.value = 1
  // 前端过滤，无需重新请求
}

const handleAdd = () => {
  addForm.value = {
    drugName: '',
    drugInfo: '',
    drugEffect: '',
    drugImg: '',
    saleIds: []
  }
  addDialogVisible.value = true
}

const submitAdd = async () => {
  await addFormRef.value.validate()
  const userInfoStr = localStorage.getItem('userInfo')
  const userInfo = userInfoStr && userInfoStr !== 'undefined' ? JSON.parse(userInfoStr) : {}
  const payload = {
    drugName: addForm.value.drugName,
    drugInfo: addForm.value.drugInfo,
    drugEffect: addForm.value.drugEffect,
    drugImg: addForm.value.drugImg,
    drugPublisher: userInfo.realname || userInfo.uname || '管理员',
    saleIds: addForm.value.saleIds
  }
  const res = await drugStore.createDrug(payload)
  ElMessage.success(res.message || '添加成功')
  addDialogVisible.value = false
  await loadData()
}

const handleEdit = (row) => {
  editForm.value = {
    drugId: row.drugId,
    drugName: row.drugName,
    drugInfo: row.drugInfo,
    drugEffect: row.drugEffect,
    drugImg: row.drugImg || '',
    drugPublisher: row.publisher || '',
    saleIds: row.saleIds || []
  }
  editDialogVisible.value = true
}

const submitEdit = async () => {
  await editFormRef.value.validate()
  const payload = {
    drugName: editForm.value.drugName,
    drugInfo: editForm.value.drugInfo,
    drugEffect: editForm.value.drugEffect,
    drugImg: editForm.value.drugImg,
    drugPublisher: editForm.value.drugPublisher,
    saleIds: editForm.value.saleIds
  }
  const res = await drugStore.editDrug(editForm.value.drugId, payload)
  ElMessage.success(res.message || '修改成功')
  editDialogVisible.value = false
  await loadData()
}

const handleDelete = async (row) => {
  try {
    await ElMessageBox.confirm('确定要删除该药品信息吗？', '提示', {
      confirmButtonText: '确定',
      cancelButtonText: '取消',
      type: 'warning'
    })
  } catch {
    return
  }
  const res = await drugStore.removeDrug(row.drugId)
  ElMessage.success(res.message || '删除成功')
  await loadData()
}

// 对上传的文件类型及大小进行限制
const beforeImgUpload = (file) => {
  const isJPG = file.type === 'image/jpeg' || file.type === 'image/png'
  const isLt2M = file.size / 1024 / 1024 < 2
  if (!isJPG) {
    ElMessage.error('上传的图片只能是 JPG 格式或 PNG 格式')
  }
  if (!isLt2M) {
    ElMessage.error('上传药品图片大小不能超过 2MB')
  }
  return isJPG && isLt2M
}

// 选择完图片后自动上传，并拿到服务器返回的图片url地址
const handleUploadSuccess = (res) => {
  ElMessage.success('上传成功')
  if (addDialogVisible.value) {
    addForm.value.drugImg = res.data.url
  } else {
    editForm.value.drugImg = res.data.url
  }
}

const handleUploadError = () => {
  ElMessage.error('上传失败')
}

onMounted(() => {
  loadData()
  drugStore.fetchSaleList()
})
</script>

<style lang="scss" scoped>
.drug-management {
  padding: 20px;
}
.breadcrumb {
  margin-bottom: 16px;
}
.main-card {
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
  .search-input {
    width: 320px;
    margin-bottom: 16px;
  }
  .pagination {
    margin-top: 16px;
    display: flex;
    justify-content: flex-end;
  }
}
.expand-content {
  padding: 10px 20px;
  p {
    margin: 6px 0;
    line-height: 1.6;
  }
}
.no-img {
  color: #909399;
  font-size: 12px;
}
.avatar-uploader {
  :deep(.el-upload) {
    border: 1px dashed var(--el-border-color);
    border-radius: 6px;
    cursor: pointer;
    position: relative;
    overflow: hidden;
    transition: var(--el-transition-duration-fast);
    width: 120px;
    height: 120px;
    display: flex;
    align-items: center;
    justify-content: center;
    &:hover {
      border-color: var(--el-color-primary);
    }
  }
}
.avatar {
  width: 120px;
  height: 120px;
  object-fit: cover;
  display: block;
}
.avatar-uploader-icon {
  font-size: 28px;
  color: #8c939d;
}
</style>
