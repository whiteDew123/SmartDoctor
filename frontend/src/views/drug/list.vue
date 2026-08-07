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
          <el-button v-if="isEditable" type="primary" :icon="Plus" @click="handleAdd">
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
        <el-table-column label="药品图片" width="100" align="center">
          <template #default="{ row }">
            <img
              v-if="getDrugImage(row) && !errorRows.has(row.drugId)"
              :src="getDrugImage(row)"
              class="drug-thumb"
              alt="drug"
              @error="onImgError($event, row)"
              @load="onImgLoad($event, row)"
              @click="previewImg(row)"
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
        <el-table-column v-if="isEditable" label="操作" width="150" fixed="right" align="center">
          <template #default="{ row }">
            <el-button link type="primary" :icon="Edit" @click="handleEdit(row)">
              编辑
            </el-button>
            <el-button v-if="isAdmin" link type="danger" :icon="Delete" @click="handleDelete(row)">
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
            <img v-if="addForm.drugImg" :src="addForm.drugImg" class="avatar" referrerpolicy="no-referrer" />
            <el-icon v-else class="avatar-uploader-icon"><Plus /></el-icon>
          </el-upload>
          <el-input
            v-model="addForm.drugImg"
            placeholder="或直接粘贴在线图片URL"
            class="img-url-input"
            clearable
          />
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
            <img v-if="editForm.drugImg" :src="editForm.drugImg" class="avatar" referrerpolicy="no-referrer" />
            <el-icon v-else class="avatar-uploader-icon"><Plus /></el-icon>
          </el-upload>
          <el-input
            v-model="editForm.drugImg"
            placeholder="或直接粘贴在线图片URL"
            class="img-url-input"
            clearable
          />
        </el-form-item>
      </el-form>
      <template #footer>
        <el-button @click="editDialogVisible = false">取消</el-button>
        <el-button type="primary" @click="submitEdit">确定</el-button>
      </template>
    </el-dialog>

    <!-- 药品图片预览 -->
    <el-dialog
      v-model="previewVisible"
      width="500"
      :show-close="true"
      align-center
      class="img-preview-dialog"
      @close="previewImgUrl = ''"
    >
      <img v-if="previewImgUrl" :src="previewImgUrl" class="preview-full-img" alt="drug" />
    </el-dialog>
  </div>
</template>

<script setup>
import { ref, computed, onMounted } from 'vue'
import { ElMessage, ElMessageBox } from 'element-plus'
import { Plus, Search, Edit, Delete } from '@element-plus/icons-vue'
import { useDrugStore } from '@/store/modules/drug'
import { useUserStore } from '@/store/user'

// 默认药品图片池：按编号排序（hUDR01, hUDR02, ...），需要时按顺序分配
const drugImageModules = import.meta.glob('@/assets/images/hUDR*.png', { eager: true })
const defaultDrugImages = Object.entries(drugImageModules)
  .map(([path, mod]) => {
    const match = path.match(/hUDR(\d+)\.png$/)
    return match ? { index: parseInt(match[1]), url: mod.default } : null
  })
  .filter(Boolean)
  .sort((a, b) => a.index - b.index)
  .map((item) => item.url)

// 记录数据库图片加载失败的 drugId（用响应式 Set，驱动重渲染切换到后备图）
const dbImgFailed = ref(new Set())

const getDrugImage = (row) => {
  // 数据库图优先；若曾加载失败，改用后备图
  if (row.drugImg && !dbImgFailed.value.has(row.drugId)) {
    return row.drugImg
  }
  return fallbackImgMap.value.get(row.drugId) || ''
}

const isEditable = computed(() => {
  const utype = useUserStore().userInfo?.utype
  return utype == 1 || utype == 2
})
const isAdmin = computed(() => useUserStore().userInfo?.utype == 1)

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

// 按顺序分配默认图片：用单独的 Map 跟踪 drugId -> 后备图
const fallbackImgMap = ref(new Map())

const filteredList = computed(() => {
  const source = !searchQuery.value
    ? drugStore.drugList
    : drugStore.drugList.filter(
        (item) => item.drugName && item.drugName.toLowerCase().includes(searchQuery.value.trim().toLowerCase())
      )
  const newMap = new Map()
  let cursor = 0
  for (const item of source) {
    if (cursor < defaultDrugImages.length) {
      newMap.set(item.drugId, defaultDrugImages[cursor++])
    }
  }
  fallbackImgMap.value = newMap
  return source
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

// ===== 药品图片查看 =====
const previewVisible = ref(false)
const previewImgUrl = ref('')

const previewImg = (row) => {
  const url = getDrugImage(row)
  if (!url) return
  previewImgUrl.value = url
  previewVisible.value = true
}

const onImgLoad = (e, row) => {
  console.log(`[DrugImage] ✓ 加载成功 drugId=${row.drugId}: ${e.target.src.substring(0, 80)}`)
}

const errorRows = ref(new Set())

const onImgError = (e, row) => {
  // 1) 当前加载的如果是数据库图：标记失败，触发重渲染切换到后备图
  if (row.drugImg && !dbImgFailed.value.has(row.drugId)) {
    dbImgFailed.value.add(row.drugId)
    dbImgFailed.value = new Set(dbImgFailed.value)
    console.warn(`[DrugImage] ⚠ 数据库图失败 drugId=${row.drugId}，切到后备图`)
    return
  }
  // 2) 后备图也失败：标记为错误，显示"无图片"
  if (!errorRows.value.has(row.drugId)) {
    errorRows.value.add(row.drugId)
    errorRows.value = new Set(errorRows.value)
    console.error(`[DrugImage] ✗ 后备图也失败 drugId=${row.drugId}: ${e.target.src}`)
  }
}
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
.drug-thumb {
  width: 70px;
  height: 70px;
  object-fit: cover;
  border-radius: 4px;
  border: 1px solid #ebeef5;
  background: #fafafa;
  cursor: pointer;
  transition: transform 0.2s;
}
.drug-thumb:hover {
  transform: scale(1.08);
  box-shadow: 0 2px 8px rgba(0, 0, 0, 0.15);
}
.preview-full-img {
  display: block;
  width: 100%;
  max-height: 70vh;
  object-fit: contain;
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
.img-url-input {
  margin-top: 10px;
  max-width: 400px;
}
</style>