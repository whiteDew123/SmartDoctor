<template>
  <div class="policy-management">
    <!-- 面包屑 -->
    <el-breadcrumb separator="/" class="breadcrumb">
      <el-breadcrumb-item :to="{ path: '/home' }">首页</el-breadcrumb-item>
      <el-breadcrumb-item>医保政策管理</el-breadcrumb-item>
    </el-breadcrumb>

    <el-card shadow="never" class="main-card">
      <template #header>
        <div class="card-header">
          <span class="title">医保政策列表</span>
          <el-button v-if="isAdmin" type="primary" :icon="Plus" circle @click="handleAdd" />
        </div>
      </template>

      <!-- 搜索区 -->
      <div class="search-bar">
        <el-input
          v-model="searchTitle"
          placeholder="请输入政策标题"
          class="search-input"
          clearable
          @clear="handleSearch"
          @keyup.enter="handleSearch"
        >
          <template #prefix>
            <el-icon><Search /></el-icon>
          </template>
        </el-input>
        <el-select
          v-model="searchCityId"
          placeholder="全部城市"
          clearable
          class="search-city"
          @change="handleSearch"
        >
          <el-option
            v-for="city in cityOptions"
            :key="city.cityId"
            :label="city.cityName || `城市${city.cityId}`"
            :value="city.cityId"
          />
        </el-select>
        <el-button type="primary" :icon="Search" @click="handleSearch">查询</el-button>
        <el-button :icon="Refresh" @click="handleReset">重置</el-button>
      </div>

      <!-- 表格 -->
      <el-table :data="policyList" v-loading="loading" style="width: 100%">
        <el-table-column prop="id" label="政策编号" sortable width="110" />
        <el-table-column prop="title" label="政策标题" min-width="220" show-overflow-tooltip />
        <el-table-column prop="cityName" label="所属城市" width="140">
          <template #default="{ row }">
            {{ row.cityName || '未关联' }}
          </template>
        </el-table-column>
        <el-table-column prop="message" label="政策简介" min-width="300" show-overflow-tooltip />
        <el-table-column prop="createTime" label="创建时间" width="170">
          <template #default="{ row }">
            {{ row.createTime || '-' }}
          </template>
        </el-table-column>
        <el-table-column v-if="isAdmin" label="操作" width="240" fixed="right" align="center">
          <template #default="{ row }">
            <el-button link type="primary" :icon="View" @click="handleDetail(row)">详情</el-button>
            <el-button link type="primary" :icon="Edit" @click="handleEdit(row)">编辑</el-button>
            <el-button link type="danger" :icon="Delete" @click="handleDelete(row)">删除</el-button>
          </template>
        </el-table-column>
      </el-table>

      <!-- 分页 -->
      <el-pagination
        v-model:current-page="pageNum"
        v-model:page-size="pageSize"
        :page-sizes="[5, 10, 20, 50]"
        :total="total"
        layout="total, prev, pager, next, jumper"
        class="pagination"
        @size-change="handleSizeChange"
        @current-change="handleCurrentChange"
      />
    </el-card>

    <!-- 新增/编辑弹窗 -->
    <el-dialog
      v-model="formDialogVisible"
      :title="isEdit ? '修改医保政策' : '新增医保政策'"
      width="800px"
      :close-on-click-modal="false"
      destroy-on-close
    >
      <el-form :model="form" label-width="100px" :rules="rules" ref="formRef">
        <el-form-item label="政策标题" prop="title">
          <el-input v-model="form.title" placeholder="请输入政策标题" maxlength="100" show-word-limit />
        </el-form-item>
        <el-form-item label="所属城市" prop="cityId">
          <el-select
            v-model="form.cityId"
            placeholder="请选择所属城市"
            filterable
            clearable
            style="width: 100%"
          >
            <el-option
              v-for="city in cityOptions"
              :key="city.cityId"
              :label="city.cityName || `城市${city.cityId}`"
              :value="city.cityId"
            />
          </el-select>
        </el-form-item>
        <el-form-item label="政策内容" prop="message">
          <el-input
            v-model="form.message"
            type="textarea"
            :rows="8"
            placeholder="请输入政策内容"
          />
        </el-form-item>
      </el-form>
      <template #footer>
        <el-button @click="formDialogVisible = false">取消</el-button>
        <el-button type="primary" @click="submitForm">确定</el-button>
      </template>
    </el-dialog>

    <!-- 详情弹窗 -->
    <el-dialog
      v-model="detailDialogVisible"
      title="医保政策详情"
      width="800px"
      :close-on-click-modal="false"
      destroy-on-close
    >
      <el-descriptions :column="1" border>
        <el-descriptions-item label="政策编号">{{ detail.id }}</el-descriptions-item>
        <el-descriptions-item label="政策标题">{{ detail.title }}</el-descriptions-item>
        <el-descriptions-item label="所属城市">{{ detail.cityName || '未关联' }}</el-descriptions-item>
        <el-descriptions-item label="创建时间">{{ detail.createTime || '-' }}</el-descriptions-item>
        <el-descriptions-item label="更新时间">{{ detail.updateTime || '-' }}</el-descriptions-item>
        <el-descriptions-item label="政策内容">
          <div class="detail-message">{{ detail.message }}</div>
        </el-descriptions-item>
      </el-descriptions>
      <template #footer>
        <el-button type="primary" @click="detailDialogVisible = false">关闭</el-button>
      </template>
    </el-dialog>
  </div>
</template>

<script setup>
import { ref, computed, onMounted } from 'vue'
import { ElMessage, ElMessageBox } from 'element-plus'
import { Plus, Search, Edit, Delete, View, Refresh } from '@element-plus/icons-vue'
import {
  getPolicyPage,
  addPolicy,
  updatePolicy,
  deletePolicy,
  getPolicyDetail,
  getCityOptions
} from '@/api/policy'
import { useUserStore } from '@/store/user'

const isAdmin = computed(() => useUserStore().userInfo?.utype == 1)

const searchTitle = ref('')
const searchCityId = ref(null)
const pageNum = ref(1)
const pageSize = ref(10)
const total = ref(0)
const loading = ref(false)
const policyList = ref([])
const cityOptions = ref([])

const formDialogVisible = ref(false)
const detailDialogVisible = ref(false)
const isEdit = ref(false)
const formRef = ref()

const form = ref({
  id: null,
  title: '',
  cityId: null,
  message: ''
})

const detail = ref({})

const rules = {
  title: [{ required: true, message: '请输入政策标题', trigger: 'blur' }],
  cityId: [{ required: true, message: '请选择所属城市', trigger: 'change' }],
  message: [{ required: true, message: '请输入政策内容', trigger: 'blur' }]
}

const loadCityOptions = async () => {
  try {
    const res = await getCityOptions()
    cityOptions.value = res.data || []
  } catch {
    cityOptions.value = []
  }
}

const loadData = async () => {
  loading.value = true
  try {
    const res = await getPolicyPage({
      pageNum: pageNum.value,
      pageSize: pageSize.value,
      title: searchTitle.value || undefined,
      cityId: searchCityId.value || undefined
    })
    const pageInfo = res.data || {}
    policyList.value = pageInfo.list || []
    total.value = pageInfo.total || 0
  } finally {
    loading.value = false
  }
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
  loadData()
}

const handleReset = () => {
  searchTitle.value = ''
  searchCityId.value = null
  pageNum.value = 1
  loadData()
}

const handleAdd = () => {
  isEdit.value = false
  form.value = { id: null, title: '', cityId: null, message: '' }
  formDialogVisible.value = true
}

const handleEdit = (row) => {
  isEdit.value = true
  form.value = {
    id: row.id,
    title: row.title,
    cityId: row.cityId,
    message: row.message
  }
  formDialogVisible.value = true
}

const submitForm = async () => {
  await formRef.value.validate()
  if (isEdit.value) {
    await updatePolicy(form.value.id, {
      title: form.value.title,
      cityId: form.value.cityId,
      message: form.value.message
    })
    ElMessage.success('修改成功')
  } else {
    await addPolicy({
      title: form.value.title,
      cityId: form.value.cityId,
      message: form.value.message
    })
    ElMessage.success('添加成功')
  }
  formDialogVisible.value = false
  await loadData()
}

const handleDelete = async (row) => {
  try {
    await ElMessageBox.confirm(`确定要删除政策"${row.title}"吗？`, '提示', {
      confirmButtonText: '确定',
      cancelButtonText: '取消',
      type: 'warning'
    })
  } catch {
    return
  }
  await deletePolicy(row.id)
  ElMessage.success('删除成功')
  if (policyList.value.length === 1 && pageNum.value > 1) {
    pageNum.value -= 1
  }
  await loadData()
}

const handleDetail = async (row) => {
  try {
    const res = await getPolicyDetail(row.id)
    detail.value = res.data || {}
    detailDialogVisible.value = true
  } catch {
    detail.value = { ...row }
    detailDialogVisible.value = true
  }
}

onMounted(() => {
  loadCityOptions()
  loadData()
})
</script>

<style lang="scss" scoped>
.policy-management {
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
      font-size: 14px;
      font-weight: 600;
      color: #303133;
    }
  }
  .search-bar {
    display: flex;
    align-items: center;
    gap: 12px;
    margin-bottom: 16px;
    .search-input {
      width: 280px;
    }
    .search-city {
      width: 200px;
    }
  }
  .pagination {
    margin-top: 20px;
    display: flex;
    justify-content: center;
    align-items: center;
    gap: 12px;
  }
}
.detail-message {
  white-space: pre-wrap;
  word-break: break-all;
  max-height: 320px;
  overflow-y: auto;
  line-height: 1.6;
}
</style>