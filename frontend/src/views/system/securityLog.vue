<template>
  <div class="security-log-page">
    <!-- 面包屑 -->
    <el-breadcrumb separator="/" class="breadcrumb">
      <el-breadcrumb-item :to="{ path: '/home' }">首页</el-breadcrumb-item>
      <el-breadcrumb-item>安全日志</el-breadcrumb-item>
    </el-breadcrumb>

    <el-card shadow="never" class="main-card">
      <template #header>
        <div class="card-header">
          <span class="title">安全日志记录</span>
          <div class="header-actions">
            <el-button type="warning" :icon="Delete" @click="handleClean" :loading="cleaning">
              清理过期日志
            </el-button>
            <el-button
              v-if="selectedIds.length > 0"
              type="danger"
              :icon="Delete"
              @click="handleBatchDelete"
            >
              批量删除 ({{ selectedIds.length }})
            </el-button>
          </div>
        </div>
      </template>

      <!-- 搜索筛选区 -->
      <el-form :inline="true" :model="searchForm" class="search-form">
        <el-form-item label="用户名">
          <el-input
            v-model="searchForm.username"
            placeholder="请输入用户名"
            clearable
            style="width: 150px"
            @keyup.enter="handleSearch"
          />
        </el-form-item>
        <el-form-item label="真实姓名">
          <el-input
            v-model="searchForm.realname"
            placeholder="请输入真实姓名"
            clearable
            style="width: 150px"
            @keyup.enter="handleSearch"
          />
        </el-form-item>
        <el-form-item label="操作类型">
          <el-select
            v-model="searchForm.operation"
            placeholder="全部"
            clearable
            style="width: 140px"
            @change="handleSearch"
          >
            <el-option label="新增" value="ADD" />
            <el-option label="修改" value="UPDATE" />
            <el-option label="删除" value="DELETE" />
            <el-option label="登录" value="LOGIN" />
            <el-option label="注册" value="REGISTER" />
            <el-option label="重置密码" value="PASSWORD_RESET" />
            <el-option label="忘记密码" value="RESET_PWD" />
            <el-option label="文件上传" value="UPLOAD" />
          </el-select>
        </el-form-item>
        <el-form-item label="操作状态">
          <el-select
            v-model="searchForm.status"
            placeholder="全部"
            clearable
            style="width: 120px"
            @change="handleSearch"
          >
            <el-option label="成功" value="SUCCESS" />
            <el-option label="失败" value="FAILURE" />
          </el-select>
        </el-form-item>
        <el-form-item label="操作时间">
          <el-date-picker
            v-model="dateRange"
            type="datetimerange"
            range-separator="至"
            start-placeholder="开始时间"
            end-placeholder="结束时间"
            value-format="YYYY-MM-DD HH:mm:ss"
            style="width: 360px"
            @change="handleDateRangeChange"
          />
        </el-form-item>
        <el-form-item>
          <el-button type="primary" :icon="Search" @click="handleSearch">查询</el-button>
          <el-button :icon="RefreshRight" @click="handleReset">重置</el-button>
        </el-form-item>
      </el-form>

      <!-- 表格 -->
      <el-table
        :data="tableData"
        v-loading="loading"
        style="width: 100%"
        border
        stripe
        @selection-change="handleSelectionChange"
      >
        <el-table-column type="selection" width="50" align="center" />
        <el-table-column type="index" label="序号" width="70" align="center" />
        <el-table-column prop="username" label="用户名" width="120" />
        <el-table-column prop="realname" label="真实姓名" width="110" />
        <el-table-column prop="operation" label="操作类型" width="110" align="center">
          <template #default="{ row }">
            <el-tag :type="getOperationTagType(row.operation)" size="small">
              {{ getOperationText(row.operation) }}
            </el-tag>
          </template>
        </el-table-column>
        <el-table-column prop="description" label="操作描述" min-width="200" show-overflow-tooltip />
        <el-table-column prop="ipAddress" label="IP地址" width="140" />
        <el-table-column prop="userAgent" label="浏览器信息" min-width="200" show-overflow-tooltip />
        <el-table-column prop="status" label="操作状态" width="100" align="center">
          <template #default="{ row }">
            <el-tag :type="row.status === 'SUCCESS' ? 'success' : 'danger'" size="small">
              {{ row.status === 'SUCCESS' ? '成功' : '失败' }}
            </el-tag>
          </template>
        </el-table-column>
        <el-table-column prop="createTime" label="操作时间" width="170" sortable />
        <el-table-column label="操作" width="80" fixed="right" align="center">
          <template #default="{ row }">
            <el-popconfirm
              title="确定要删除该日志吗？"
              confirm-button-text="确定"
              cancel-button-text="取消"
              @confirm="handleDelete(row)"
            >
              <template #reference>
                <el-button link type="danger" :icon="Delete">删除</el-button>
              </template>
            </el-popconfirm>
          </template>
        </el-table-column>
      </el-table>

      <!-- 分页 -->
      <el-pagination
        v-model:current-page="pageNum"
        v-model:page-size="pageSize"
        :page-sizes="[10, 20, 50, 100]"
        :total="total"
        layout="total, sizes, prev, pager, next, jumper"
        class="pagination"
        @size-change="handleSizeChange"
        @current-change="handleCurrentChange"
      />
    </el-card>

    <!-- 清理过期日志弹窗 -->
    <el-dialog
      v-model="cleanDialogVisible"
      title="清理过期日志"
      width="450px"
      :close-on-click-modal="false"
    >
      <el-form label-width="120px">
        <el-form-item label="清理时间点">
          <el-date-picker
            v-model="cleanBeforeDate"
            type="datetime"
            placeholder="选择日期"
            value-format="YYYY-MM-DD HH:mm:ss"
            style="width: 100%"
          />
        </el-form-item>
        <el-alert
          title="将删除所选日期之前的所有安全日志，此操作不可恢复！"
          type="warning"
          :closable="false"
          show-icon
          style="margin-top: 10px"
        />
      </el-form>
      <template #footer>
        <el-button @click="cleanDialogVisible = false">取消</el-button>
        <el-button type="warning" @click="submitClean" :loading="cleaning">确认清理</el-button>
      </template>
    </el-dialog>
  </div>
</template>

<script setup>
import { ref, reactive, onMounted } from 'vue'
import { ElMessage, ElMessageBox } from 'element-plus'
import { Search, RefreshRight, Delete } from '@element-plus/icons-vue'
import { getSecurityLogs, deleteSecurityLog, batchDeleteSecurityLog, cleanSecurityLog } from '@/api/securityLog'

const loading = ref(false)
const cleaning = ref(false)
const tableData = ref([])
const total = ref(0)
const pageNum = ref(1)
const pageSize = ref(10)
const selectedIds = ref([])
const cleanDialogVisible = ref(false)
const cleanBeforeDate = ref('')

const searchForm = reactive({
  username: '',
  realname: '',
  operation: '',
  status: ''
})

const dateRange = ref([])

const fetchData = async () => {
  loading.value = true
  try {
    const params = {
      pageNum: pageNum.value,
      pageSize: pageSize.value,
      ...searchForm
    }
    if (dateRange.value && dateRange.value.length === 2) {
      params.beginTime = dateRange.value[0]
      params.endTime = dateRange.value[1]
    }
    const res = await getSecurityLogs(params)
    // 兼容后端分页结果：list/total 可能在 data.list / data.total 或直接在 data 里
    if (res.data) {
      if (res.data.list) {
        tableData.value = res.data.list
        total.value = res.data.total
      } else {
        tableData.value = res.data
        total.value = res.total || 0
      }
    } else {
      tableData.value = []
      total.value = 0
    }
  } catch (e) {
    console.error('获取安全日志失败：', e)
    ElMessage.error('获取安全日志失败')
  } finally {
    loading.value = false
  }
}

const getOperationText = (op) => {
  const map = {
    'LOGIN': '登录',
    'LOGOUT': '登出',
    'REGISTER': '注册',
    'PASSWORD_RESET': '重置密码',
    'PASSWORD_CHANGE': '修改密码',
    'ADD': '新增',
    'UPDATE': '修改',
    'DELETE': '删除',
    'RESET_PWD': '忘记密码',
    'UPLOAD': '文件上传'
  }
  return map[op] || op
}

const getOperationTagType = (op) => {
  const map = {
    'LOGIN': '',
    'LOGOUT': 'info',
    'REGISTER': 'success',
    'PASSWORD_RESET': 'warning',
    'PASSWORD_CHANGE': 'warning',
    'ADD': 'primary',
    'UPDATE': 'warning',
    'DELETE': 'danger',
    'RESET_PWD': 'warning',
    'UPLOAD': 'success'
  }
  return map[op] || 'info'
}

const handleSearch = () => {
  pageNum.value = 1
  fetchData()
}

const handleReset = () => {
  searchForm.username = ''
  searchForm.realname = ''
  searchForm.operation = ''
  searchForm.status = ''
  dateRange.value = []
  pageNum.value = 1
  fetchData()
}

const handleDateRangeChange = () => {
  pageNum.value = 1
  fetchData()
}

const handleSizeChange = (val) => {
  pageSize.value = val
  pageNum.value = 1
  fetchData()
}

const handleCurrentChange = (val) => {
  pageNum.value = val
  fetchData()
}

const handleSelectionChange = (selection) => {
  selectedIds.value = selection.map(item => item.id)
}

const handleDelete = async (row) => {
  try {
    const res = await deleteSecurityLog(row.id)
    ElMessage.success(res.message || '删除成功')
    fetchData()
  } catch (e) {
    console.error('删除失败：', e)
    ElMessage.error('删除失败')
  }
}

const handleBatchDelete = async () => {
  try {
    await ElMessageBox.confirm(
      `确定要删除选中的 ${selectedIds.value.length} 条日志吗？`,
      '批量删除确认',
      { confirmButtonText: '确定', cancelButtonText: '取消', type: 'warning' }
    )
  } catch {
    return
  }
  try {
    const res = await batchDeleteSecurityLog(selectedIds.value)
    ElMessage.success(res.message || '批量删除成功')
    selectedIds.value = []
    fetchData()
  } catch (e) {
    console.error('批量删除失败：', e)
    ElMessage.error('批量删除失败')
  }
}

const handleClean = () => {
  cleanBeforeDate.value = ''
  cleanDialogVisible.value = true
}

const submitClean = async () => {
  if (!cleanBeforeDate.value) {
    ElMessage.warning('请选择清理时间点')
    return
  }
  try {
    await ElMessageBox.confirm(
      '此操作不可恢复，确定要清理所选日期之前的日志吗？',
      '清理确认',
      { confirmButtonText: '确定', cancelButtonText: '取消', type: 'warning' }
    )
  } catch {
    return
  }
  cleaning.value = true
  try {
    const res = await cleanSecurityLog(cleanBeforeDate.value)
    ElMessage.success(res.message || '清理成功')
    cleanDialogVisible.value = false
    fetchData()
  } catch (e) {
    console.error('清理失败：', e)
    ElMessage.error('清理失败')
  } finally {
    cleaning.value = false
  }
}

onMounted(() => {
  fetchData()
})
</script>

<style lang="scss" scoped>
.security-log-page {
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
    .header-actions {
      display: flex;
      gap: 10px;
    }
  }
  .search-form {
    margin-bottom: 4px;
    :deep(.el-form-item) {
      margin-bottom: 12px;
    }
  }
  .pagination {
    margin-top: 16px;
    display: flex;
    justify-content: flex-end;
  }
}
</style>
