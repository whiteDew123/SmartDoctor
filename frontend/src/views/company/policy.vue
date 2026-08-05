<template>
  <div class="company-policy-management">
    <el-breadcrumb separator="/" class="breadcrumb">
      <el-breadcrumb-item :to="{ path: '/home' }">首页</el-breadcrumb-item>
      <el-breadcrumb-item>医药公司政策管理</el-breadcrumb-item>
    </el-breadcrumb>

    <el-card shadow="never" class="main-card">
      <template #header>
        <div class="card-header">
          <span class="title">医药公司政策列表</span>
          <el-button v-if="isAdmin" type="primary" :icon="Plus" @click="handleAdd">
            添加政策
          </el-button>
        </div>
      </template>

      <div class="search-bar">
        <el-input
          v-model="searchTitle"
          placeholder="请输入政策标题"
          clearable
          class="search-input"
          @clear="handleSearch"
          @keyup.enter="handleSearch"
        >
          <template #append>
            <el-button :icon="Search" @click="handleSearch">查询</el-button>
          </template>
        </el-input>
        <el-select
          v-model="searchCompanyId"
          placeholder="请选择医药公司"
          clearable
          class="search-select"
          @change="handleSearch"
          @clear="handleSearch"
        >
          <el-option
            v-for="company in policyStore.companyList"
            :key="company.companyId"
            :label="company.companyName"
            :value="company.companyId"
          />
        </el-select>
      </div>

      <el-table
        :data="policyStore.policyList"
        v-loading="policyStore.loading"
        style="width: 100%"
        border
        stripe
      >
        <el-table-column prop="id" label="编号" width="80" sortable />
        <el-table-column prop="title" label="政策标题" min-width="180" show-overflow-tooltip />
        <el-table-column label="政策内容" min-width="300" show-overflow-tooltip>
          <template #default="{ row }">
            <span>{{ row.message || '-' }}</span>
          </template>
        </el-table-column>
        <el-table-column label="所属医药公司" width="160" show-overflow-tooltip>
          <template #default="{ row }">
            <span>{{ getCompanyName(row.companyId) }}</span>
          </template>
        </el-table-column>
        <el-table-column prop="createTime" label="创建时间" width="180" sortable />
        <el-table-column v-if="isAdmin" label="操作" width="150" fixed="right" align="center">
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

      <el-pagination
        v-model:current-page="pageNum"
        v-model:page-size="pageSize"
        :page-sizes="[5, 10, 20, 50]"
        :total="policyStore.total"
        layout="total, sizes, prev, pager, next, jumper"
        class="pagination"
        @size-change="handleSizeChange"
        @current-change="handleCurrentChange"
      />
    </el-card>

    <el-dialog
      v-model="addDialogVisible"
      title="添加医药公司政策"
      width="600px"
      :close-on-click-modal="false"
      destroy-on-close
    >
      <el-form :model="addForm" label-width="120px" :rules="rules" ref="addFormRef">
        <el-form-item label="政策标题" prop="title">
          <el-input v-model="addForm.title" placeholder="请输入政策标题" />
        </el-form-item>
        <el-form-item label="政策内容" prop="message">
          <el-input
            v-model="addForm.message"
            type="textarea"
            :rows="5"
            placeholder="请输入政策内容"
          />
        </el-form-item>
        <el-form-item label="所属医药公司" prop="companyId">
          <el-select
            v-model="addForm.companyId"
            placeholder="请选择医药公司"
            style="width: 100%"
          >
            <el-option
              v-for="company in policyStore.companyList"
              :key="company.companyId"
              :label="company.companyName"
              :value="company.companyId"
            />
          </el-select>
        </el-form-item>
      </el-form>
      <template #footer>
        <el-button @click="addDialogVisible = false">取消</el-button>
        <el-button type="primary" @click="submitAdd">确定</el-button>
      </template>
    </el-dialog>

    <el-dialog
      v-model="editDialogVisible"
      title="修改医药公司政策"
      width="600px"
      :close-on-click-modal="false"
      destroy-on-close
    >
      <el-form :model="editForm" label-width="120px" :rules="rules" ref="editFormRef">
        <el-form-item label="政策标题" prop="title">
          <el-input v-model="editForm.title" placeholder="请输入政策标题" />
        </el-form-item>
        <el-form-item label="政策内容" prop="message">
          <el-input
            v-model="editForm.message"
            type="textarea"
            :rows="5"
            placeholder="请输入政策内容"
          />
        </el-form-item>
        <el-form-item label="所属医药公司" prop="companyId">
          <el-select
            v-model="editForm.companyId"
            placeholder="请选择医药公司"
            style="width: 100%"
          >
            <el-option
              v-for="company in policyStore.companyList"
              :key="company.companyId"
              :label="company.companyName"
              :value="company.companyId"
            />
          </el-select>
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
import { useCompanyPolicyStore } from '@/store/modules/companyPolicy'
import { useUserStore } from '@/store/user'

const policyStore = useCompanyPolicyStore()
const userStore = useUserStore()

const isAdmin = computed(() => {
  const utype = userStore.userInfo?.utype
  return utype === 1
})

const searchTitle = ref('')
const searchCompanyId = ref(null)
const pageNum = ref(1)
const pageSize = ref(10)

const addDialogVisible = ref(false)
const editDialogVisible = ref(false)
const addFormRef = ref()
const editFormRef = ref()

const addForm = ref({
  title: '',
  message: '',
  companyId: null
})

const editForm = ref({
  id: null,
  title: '',
  message: '',
  companyId: null
})

const rules = {
  title: [{ required: true, message: '请输入政策标题', trigger: 'blur' }],
  message: [{ required: true, message: '请输入政策内容', trigger: 'blur' }],
  companyId: [{ required: true, message: '请选择医药公司', trigger: 'change' }]
}

const getCompanyName = (companyId) => {
  if (!companyId) return '-'
  const company = policyStore.companyList.find((c) => c.companyId === companyId)
  return company ? company.companyName : companyId
}

const loadData = async () => {
  const params = {}
  if (searchTitle.value.trim()) {
    params.title = searchTitle.value.trim()
  }
  if (searchCompanyId.value) {
    params.companyId = searchCompanyId.value
  }
  await policyStore.fetchPolicies(pageNum.value, pageSize.value, params)
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

const handleAdd = () => {
  addForm.value = {
    title: '',
    message: '',
    companyId: null
  }
  addDialogVisible.value = true
}

const submitAdd = async () => {
  await addFormRef.value.validate()
  const res = await policyStore.createPolicy({
    title: addForm.value.title,
    message: addForm.value.message,
    companyId: addForm.value.companyId
  })
  ElMessage.success(res.message || '添加成功')
  addDialogVisible.value = false
  await loadData()
}

const handleEdit = (row) => {
  editForm.value = {
    id: row.id,
    title: row.title,
    message: row.message,
    companyId: row.companyId
  }
  editDialogVisible.value = true
}

const submitEdit = async () => {
  await editFormRef.value.validate()
  const res = await policyStore.editPolicy({
    id: editForm.value.id,
    title: editForm.value.title,
    message: editForm.value.message,
    companyId: editForm.value.companyId
  })
  ElMessage.success(res.message || '修改成功')
  editDialogVisible.value = false
  await loadData()
}

const handleDelete = async (row) => {
  try {
    await ElMessageBox.confirm('确定要删除该医药公司政策吗？', '提示', {
      confirmButtonText: '确定',
      cancelButtonText: '取消',
      type: 'warning'
    })
  } catch {
    return
  }
  const res = await policyStore.removePolicy(row.id)
  ElMessage.success(res.message || '删除成功')
  await loadData()
}

onMounted(() => {
  policyStore.fetchCompanyList()
  loadData()
})
</script>

<style lang="scss" scoped>
.company-policy-management {
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
  .search-bar {
    display: flex;
    gap: 12px;
    margin-bottom: 16px;
    .search-input {
      width: 320px;
    }
    .search-select {
      width: 200px;
    }
  }
  .pagination {
    margin-top: 16px;
    display: flex;
    justify-content: flex-end;
  }
}
</style>