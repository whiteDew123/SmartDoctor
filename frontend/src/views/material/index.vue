<template>
  <div class="material-management">
    <!-- 面包屑 -->
    <el-breadcrumb separator="/" class="breadcrumb">
      <el-breadcrumb-item :to="{ path: '/home' }">首页</el-breadcrumb-item>
      <el-breadcrumb-item>必备材料管理</el-breadcrumb-item>
    </el-breadcrumb>

    <el-card shadow="never" class="main-card">
      <template #header>
        <div class="card-header">
          <span class="title">必备材料列表</span>
          <el-button v-if="isAdmin" type="primary" :icon="Plus" circle @click="handleAdd" />
        </div>
      </template>

      <!-- 搜索框 -->
      <el-input
        v-model="searchQuery"
        placeholder="请输入要查询的材料标题"
        class="search-input"
        clearable
        @clear="handleSearch"
        @keyup.enter="handleSearch"
      >
        <template #prefix>
          <el-icon><Search /></el-icon>
        </template>
      </el-input>

      <!-- 表格 -->
      <el-table
        :data="filteredList"
        v-loading="loading"
        style="width: 100%"
      >
        <el-table-column prop="id" label="材料编号" sortable />
        <el-table-column prop="title" label="材料标题" show-overflow-tooltip />
        <el-table-column prop="message" label="材料内容" min-width="300" show-overflow-tooltip />
        <el-table-column v-if="isAdmin" label="操作" width="160" fixed="right" align="center">
          <template #default="{ row }">
            <el-button link type="danger" :icon="Delete" @click="handleDelete(row)">
              删除
            </el-button>
            <el-button link type="primary" :icon="Edit" @click="handleEdit(row)">
              编辑
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
        layout="total, prev, pager, next, jumper"
        class="pagination"
        @size-change="handleSizeChange"
        @current-change="handleCurrentChange"
      />
    </el-card>

    <!-- 添加材料弹窗 -->
    <el-dialog
      v-model="addDialogVisible"
      title="新增必备材料"
      width="800px"
      :close-on-click-modal="false"
      destroy-on-close
    >
      <el-form :model="addForm" label-width="100px" :rules="rules" ref="addFormRef">
        <el-form-item label="材料标题" prop="title">
          <el-input v-model="addForm.title" placeholder="请输入材料标题" />
        </el-form-item>
        <el-form-item label="材料内容" prop="message">
          <el-input
            v-model="addForm.message"
            placeholder="请输入材料内容"
          />
        </el-form-item>
      </el-form>
      <template #footer>
        <el-button @click="addDialogVisible = false">取消</el-button>
        <el-button type="primary" @click="submitAdd">确定</el-button>
      </template>
    </el-dialog>

    <!-- 修改材料弹窗 -->
    <el-dialog
      v-model="editDialogVisible"
      title="修改必备材料信息"
      width="800px"
      :close-on-click-modal="false"
      destroy-on-close
    >
      <el-form :model="editForm" label-width="100px" :rules="rules" ref="editFormRef">
        <el-form-item label="材料标题" prop="title">
          <el-input v-model="editForm.title" placeholder="请输入材料标题" />
        </el-form-item>
        <el-form-item label="材料内容" prop="message">
          <el-input
            v-model="editForm.message"
            placeholder="请输入材料内容"
          />
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
import { getMaterialPage, addMaterial, updateMaterial, deleteMaterial } from '@/api/material'
import { useUserStore } from '@/store/user'

const isAdmin = computed(() => useUserStore().userInfo?.utype == 1)

const searchQuery = ref('')
const pageNum = ref(1)
const pageSize = ref(10)
const total = ref(0)
const loading = ref(false)
const materialList = ref([])

const addDialogVisible = ref(false)
const editDialogVisible = ref(false)
const addFormRef = ref()
const editFormRef = ref()

const addForm = ref({
  title: '',
  message: ''
})

const editForm = ref({
  id: null,
  title: '',
  message: ''
})

const rules = {
  title: [{ required: true, message: '请输入材料标题', trigger: 'blur' }]
}

const filteredList = computed(() => {
  if (!searchQuery.value) return materialList.value
  const query = searchQuery.value.trim().toLowerCase()
  return materialList.value.filter(
    (item) => item.title && item.title.toLowerCase().includes(query)
  )
})

const loadData = async () => {
  loading.value = true
  try {
    const res = await getMaterialPage(pageNum.value, pageSize.value)
    const pageInfo = res.data?.materialPageInfo || res.data || {}
    materialList.value = pageInfo.list || []
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

const handleAdd = () => {
  addForm.value = { title: '', message: '' }
  addDialogVisible.value = true
}

const submitAdd = async () => {
  await addFormRef.value.validate()
  const res = await addMaterial(addForm.value)
  ElMessage.success(res.message || '添加成功')
  addDialogVisible.value = false
  await loadData()
}

const handleEdit = (row) => {
  editForm.value = {
    id: row.id,
    title: row.title,
    message: row.message
  }
  editDialogVisible.value = true
}

const submitEdit = async () => {
  await editFormRef.value.validate()
  const res = await updateMaterial(editForm.value)
  ElMessage.success(res.message || '修改成功')
  editDialogVisible.value = false
  await loadData()
}

const handleDelete = async (row) => {
  try {
    await ElMessageBox.confirm('确定要删除该材料信息吗？', '提示', {
      confirmButtonText: '确定',
      cancelButtonText: '取消',
      type: 'warning'
    })
  } catch {
    return
  }
  const res = await deleteMaterial(row.id)
  ElMessage.success(res.message || '删除成功')
  await loadData()
}

onMounted(() => {
  loadData()
})
</script>

<style lang="scss" scoped>
.material-management {
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
  .search-input {
    margin-bottom: 16px;
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