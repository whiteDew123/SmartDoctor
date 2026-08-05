<template>
  <div class="company-management">
    <!-- 面包屑 -->
    <el-breadcrumb separator="/" class="breadcrumb">
      <el-breadcrumb-item :to="{ path: '/home' }">首页</el-breadcrumb-item>
      <el-breadcrumb-item>医药公司管理</el-breadcrumb-item>
    </el-breadcrumb>

    <el-card shadow="never" class="main-card">
      <template #header>
        <div class="card-header">
          <span class="title">医药公司列表</span>
          <el-button type="primary" :icon="Plus" circle @click="handleAdd" />
        </div>
      </template>

      <!-- 搜索区域 -->
      <el-form :model="searchForm" class="search-form" inline>
        <el-form-item label="公司名称">
          <el-input
            v-model="searchForm.companyName"
            placeholder="公司名称"
            clearable
            @keyup.enter="handleSearch"
          />
        </el-form-item>
        <el-form-item label="公司电话">
          <el-input
            v-model="searchForm.companyPhone"
            placeholder="公司电话"
            clearable
            @keyup.enter="handleSearch"
          />
        </el-form-item>
        <el-form-item>
          <el-button type="primary" :icon="Search" @click="handleSearch">查询</el-button>
          <el-button :icon="Refresh" @click="handleReset">重置</el-button>
        </el-form-item>
      </el-form>

      <!-- 表格 -->
      <el-table
        :data="tableData"
        v-loading="loading"
        style="width: 100%"
        border
      >
        <el-table-column type="index" label="序号" width="80" :index="computedIndex" />
        <el-table-column prop="companyName" label="公司名称" min-width="160" show-overflow-tooltip />
        <el-table-column prop="companyPhone" label="公司电话" width="160" />
        <el-table-column prop="createtime" label="创建时间" width="180" />
        <el-table-column prop="updatetime" label="更新时间" width="180" />
        <el-table-column label="操作" width="160" fixed="right" align="center">
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
        :total="total"
        layout="total, sizes, prev, pager, next, jumper"
        class="pagination"
        @size-change="handleSizeChange"
        @current-change="handleCurrentChange"
      />
    </el-card>

    <!-- 新增弹窗 -->
    <el-dialog
      v-model="addDialogVisible"
      title="新增医药公司"
      width="500px"
      :close-on-click-modal="false"
      destroy-on-close
    >
      <el-form :model="addForm" label-width="100px" :rules="rules" ref="addFormRef">
        <el-form-item label="公司名称" prop="companyName">
          <el-input v-model="addForm.companyName" placeholder="请输入公司名称" />
        </el-form-item>
        <el-form-item label="公司电话" prop="companyPhone">
          <el-input v-model="addForm.companyPhone" placeholder="请输入公司电话" />
        </el-form-item>
      </el-form>
      <template #footer>
        <el-button @click="addDialogVisible = false">取消</el-button>
        <el-button type="primary" @click="submitAdd">确定</el-button>
      </template>
    </el-dialog>

    <!-- 编辑弹窗 -->
    <el-dialog
      v-model="editDialogVisible"
      title="编辑医药公司"
      width="500px"
      :close-on-click-modal="false"
      destroy-on-close
    >
      <el-form :model="editForm" label-width="100px" :rules="rules" ref="editFormRef">
        <el-form-item label="公司名称" prop="companyName">
          <el-input v-model="editForm.companyName" placeholder="请输入公司名称" />
        </el-form-item>
        <el-form-item label="公司电话" prop="companyPhone">
          <el-input v-model="editForm.companyPhone" placeholder="请输入公司电话" />
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
import { ref, reactive, onMounted } from 'vue'
import { ElMessage, ElMessageBox } from 'element-plus'
import { Plus, Search, Refresh, Edit, Delete } from '@element-plus/icons-vue'
import { getCompanyPage, addCompany, updateCompany, deleteCompany } from '@/api/company'

const loading = ref(false)
const tableData = ref([])
const total = ref(0)
const pageNum = ref(1)
const pageSize = ref(10)

const searchForm = reactive({
  companyName: '',
  companyPhone: ''
})

const addDialogVisible = ref(false)
const editDialogVisible = ref(false)
const addFormRef = ref()
const editFormRef = ref()

const addForm = reactive({
  companyName: '',
  companyPhone: ''
})

const editForm = reactive({
  companyId: null,
  companyName: '',
  companyPhone: ''
})

const rules = {
  companyName: [{ required: true, message: '请输入公司名称', trigger: 'blur' }],
  companyPhone: [{ required: true, message: '请输入公司电话', trigger: 'blur' }]
}

const loadData = async () => {
  loading.value = true
  try {
    const params = {
      pageNum: pageNum.value,
      pageSize: pageSize.value
    }
    if (searchForm.companyName) params.companyName = searchForm.companyName.trim()
    if (searchForm.companyPhone) params.companyPhone = searchForm.companyPhone.trim()
    const res = await getCompanyPage(params)
    tableData.value = res.data.list || []
    total.value = res.data.total || 0
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
  searchForm.companyName = ''
  searchForm.companyPhone = ''
  pageNum.value = 1
  loadData()
}

const handleAdd = () => {
  addForm.companyName = ''
  addForm.companyPhone = ''
  addDialogVisible.value = true
}

const submitAdd = async () => {
  await addFormRef.value.validate()
  const res = await addCompany({
    companyName: addForm.companyName,
    companyPhone: addForm.companyPhone
  })
  ElMessage.success(res.message || '添加成功')
  addDialogVisible.value = false
  await loadData()
}

const handleEdit = (row) => {
  editForm.companyId = row.companyId
  editForm.companyName = row.companyName
  editForm.companyPhone = row.companyPhone
  editDialogVisible.value = true
}

const submitEdit = async () => {
  await editFormRef.value.validate()
  const res = await updateCompany(editForm.companyId, {
    companyName: editForm.companyName,
    companyPhone: editForm.companyPhone
  })
  ElMessage.success(res.message || '修改成功')
  editDialogVisible.value = false
  await loadData()
}

const handleDelete = async (row) => {
  try {
    await ElMessageBox.confirm('确定要删除该医药公司信息吗？', '提示', {
      confirmButtonText: '确定',
      cancelButtonText: '取消',
      type: 'warning'
    })
  } catch {
    return
  }
  const res = await deleteCompany(row.companyId)
  ElMessage.success(res.message || '删除成功')
  // 删除后如果当前页没数据了，且不是第一页，则回退一页
  if (tableData.value.length === 1 && pageNum.value > 1) {
    pageNum.value--
  }
  await loadData()
}

const computedIndex = (index) => {
  return (pageNum.value - 1) * pageSize.value + index + 1
}

onMounted(() => {
  loadData()
})
</script>

<style lang="scss" scoped>
.company-management {
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
  .search-form {
    margin-bottom: 16px;
    :deep(.el-form-item) {
      margin-bottom: 0;
      margin-right: 16px;
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
</style>