<template>
  <div class="sale-management">
    <!-- 面包屑 -->
    <el-breadcrumb separator="/" class="breadcrumb">
      <el-breadcrumb-item :to="{ path: '/home' }">首页</el-breadcrumb-item>
      <el-breadcrumb-item>销售地点管理</el-breadcrumb-item>
    </el-breadcrumb>

    <el-card shadow="never" class="main-card">
      <template #header>
        <div class="card-header">
          <span class="title">销售地点列表</span>
          <el-button type="primary" :icon="Plus" circle @click="handleAdd" />
        </div>
      </template>

      <!-- 搜索框 -->
      <el-input
        v-model="searchQuery"
        placeholder="请输入要查询的药店名称"
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
      <el-table :data="saleList" v-loading="loading" style="width: 100%">
        <el-table-column prop="saleId" label="编号" sortable />
        <el-table-column prop="saleName" label="药店名称" show-overflow-tooltip />
        <el-table-column prop="salePhone" label="联系电话" />
        <el-table-column prop="createtime" label="创建时间" />
        <el-table-column label="操作" width="160" fixed="right" align="center">
          <template #default="{ row }">
            <el-button link type="danger" :icon="Delete" @click="handleDelete(row)">删除</el-button>
            <el-button link type="primary" :icon="Edit" @click="handleEdit(row)">编辑</el-button>
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

    <!-- 添加弹窗 -->
    <el-dialog
      v-model="addDialogVisible"
      title="新增销售地点"
      width="600px"
      :close-on-click-modal="false"
      destroy-on-close
    >
      <el-form :model="addForm" label-width="100px" :rules="rules" ref="addFormRef">
        <el-form-item label="药店名称" prop="saleName">
          <el-input v-model="addForm.saleName" placeholder="请输入药店名称" />
        </el-form-item>
        <el-form-item label="联系电话" prop="salePhone">
          <el-input v-model="addForm.salePhone" placeholder="请输入联系电话" />
        </el-form-item>
      </el-form>
      <template #footer>
        <el-button @click="addDialogVisible = false">取消</el-button>
        <el-button type="primary" @click="submitAdd">确定</el-button>
      </template>
    </el-dialog>

    <!-- 修改弹窗 -->
    <el-dialog
      v-model="editDialogVisible"
      title="修改销售地点信息"
      width="600px"
      :close-on-click-modal="false"
      destroy-on-close
    >
      <el-form :model="editForm" label-width="100px" :rules="rules" ref="editFormRef">
        <el-form-item label="药店名称" prop="saleName">
          <el-input v-model="editForm.saleName" placeholder="请输入药店名称" />
        </el-form-item>
        <el-form-item label="联系电话" prop="salePhone">
          <el-input v-model="editForm.salePhone" placeholder="请输入联系电话" />
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
import { ref, onMounted } from 'vue'
import { ElMessage, ElMessageBox } from 'element-plus'
import { Plus, Search, Edit, Delete } from '@element-plus/icons-vue'
import { getSaleList, addSale, updateSale, deleteSale } from '@/api/sale'

const searchQuery = ref('')
const pageNum = ref(1)
const pageSize = ref(10)
const total = ref(0)
const loading = ref(false)
const saleList = ref([])

const addDialogVisible = ref(false)
const editDialogVisible = ref(false)
const addFormRef = ref()
const editFormRef = ref()

const addForm = ref({
  saleName: '',
  salePhone: ''
})

const editForm = ref({
  saleId: null,
  saleName: '',
  salePhone: ''
})

const rules = {
  saleName: [{ required: true, message: '请输入药店名称', trigger: 'blur' }],
  salePhone: [{ required: true, message: '请输入联系电话', trigger: 'blur' }]
}

const loadData = async () => {
  loading.value = true
  try {
    const res = await getSaleList({
      pageNum: pageNum.value,
      pageSize: pageSize.value,
      saleName: searchQuery.value || undefined
    })
    const pageInfo = res.data || {}
    saleList.value = pageInfo.list || []
    total.value = pageInfo.total || 0
  } catch (e) {
    console.error('加载销售地点失败：', e)
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
  addForm.value = { saleName: '', salePhone: '' }
  addDialogVisible.value = true
}

const submitAdd = async () => {
  await addFormRef.value.validate()
  const res = await addSale(addForm.value)
  ElMessage.success(res.message || '添加成功')
  addDialogVisible.value = false
  await loadData()
}

const handleEdit = (row) => {
  editForm.value = {
    saleId: row.saleId,
    saleName: row.saleName,
    salePhone: row.salePhone
  }
  editDialogVisible.value = true
}

const submitEdit = async () => {
  await editFormRef.value.validate()
  const res = await updateSale(editForm.value.saleId, {
    saleName: editForm.value.saleName,
    salePhone: editForm.value.salePhone
  })
  ElMessage.success(res.message || '修改成功')
  editDialogVisible.value = false
  await loadData()
}

const handleDelete = async (row) => {
  try {
    await ElMessageBox.confirm('确定要删除该销售地点吗？', '提示', {
      confirmButtonText: '确定',
      cancelButtonText: '取消',
      type: 'warning'
    })
  } catch {
    return
  }
  const res = await deleteSale(row.saleId)
  ElMessage.success(res.message || '删除成功')
  await loadData()
}

onMounted(() => {
  loadData()
})
</script>

<style lang="scss" scoped>
.sale-management {
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
