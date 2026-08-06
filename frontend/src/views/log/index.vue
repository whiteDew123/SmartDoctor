<template>
  <div class="log-container">
    <!-- 搜索区域 -->
    <el-card class="search-card" shadow="never">
      <el-form :model="queryParams" inline>
        <el-form-item label="操作类型">
          <el-input v-model="queryParams.operation" placeholder="输入操作类型" clearable style="width:160px" />
        </el-form-item>
        <el-form-item label="操作人">
          <el-input v-model="queryParams.username" placeholder="输入用户名" clearable style="width:160px" />
        </el-form-item>
        <el-form-item label="状态">
          <el-select v-model="queryParams.status" placeholder="请选择" clearable style="width:120px">
            <el-option label="全部" :value="null" />
            <el-option label="成功" :value="1" />
            <el-option label="失败" :value="0" />
          </el-select>
        </el-form-item>
        <el-form-item>
          <el-button type="primary" @click="handleSearch">查询</el-button>
          <el-button @click="handleReset">重置</el-button>
        </el-form-item>
      </el-form>
    </el-card>

    <!-- 日志表格 -->
    <el-card class="table-card" shadow="never">
      <el-table :data="logList" v-loading="loading" stripe style="width:100%">
        <el-table-column prop="id" label="ID" width="60" align="center" />
        <el-table-column prop="username" label="操作人" width="120" />
        <el-table-column prop="userRole" label="角色" width="100">
          <template #default="{ row }">
            <el-tag :type="roleTagType(row.userRole)" size="small">
              {{ roleName(row.userRole) }}
            </el-tag>
          </template>
        </el-table-column>
        <el-table-column prop="operation" label="操作类型" width="140" />
        <el-table-column prop="detail" label="操作详情" min-width="200" show-overflow-tooltip />
        <el-table-column prop="ip" label="IP地址" width="140" />
        <el-table-column prop="status" label="状态" width="80" align="center">
          <template #default="{ row }">
            <el-tag :type="row.status === 1 ? 'success' : 'danger'" size="small">
              {{ row.status === 1 ? '成功' : '失败' }}
            </el-tag>
          </template>
        </el-table-column>
        <el-table-column prop="createTime" label="操作时间" width="180" />
        <el-table-column label="操作" width="80" align="center" fixed="right">
          <template #default="{ row }">
            <el-button link type="primary" size="small" @click="showDetail(row)">详情</el-button>
          </template>
        </el-table-column>
      </el-table>

      <!-- 分页 -->
      <div class="pagination-wrapper">
        <el-pagination
          v-model:current-page="queryParams.page"
          v-model:page-size="queryParams.size"
          :page-sizes="[10, 20, 50, 100]"
          :total="total"
          layout="total, sizes, prev, pager, next, jumper"
          @size-change="fetchLogs"
          @current-change="fetchLogs"
        />
      </div>
    </el-card>

    <!-- 详情弹窗 -->
    <el-dialog v-model="detailVisible" title="安全日志详情" width="600px">
      <el-descriptions :column="2" border>
        <el-descriptions-item label="ID" :span="1">{{ detailData.id }}</el-descriptions-item>
        <el-descriptions-item label="操作人" :span="1">{{ detailData.username }}</el-descriptions-item>
        <el-descriptions-item label="角色" :span="1">{{ roleName(detailData.userRole) }}</el-descriptions-item>
        <el-descriptions-item label="操作类型" :span="1">{{ detailData.operation }}</el-descriptions-item>
        <el-descriptions-item label="IP地址" :span="1">{{ detailData.ip }}</el-descriptions-item>
        <el-descriptions-item label="HTTP方法" :span="1">{{ detailData.httpMethod }}</el-descriptions-item>
        <el-descriptions-item label="请求URI" :span="2">{{ detailData.requestUri }}</el-descriptions-item>
        <el-descriptions-item label="状态" :span="1">
          <el-tag :type="detailData.status === 1 ? 'success' : 'danger'" size="small">
            {{ detailData.status === 1 ? '成功' : '失败' }}
          </el-tag>
        </el-descriptions-item>
        <el-descriptions-item label="结果" :span="1">{{ detailData.result }}</el-descriptions-item>
        <el-descriptions-item label="操作时间" :span="2">{{ detailData.createTime }}</el-descriptions-item>
        <el-descriptions-item label="操作详情" :span="2">{{ detailData.detail }}</el-descriptions-item>
      </el-descriptions>
    </el-dialog>
  </div>
</template>

<script setup>
import { ref, reactive, onMounted } from 'vue'
import { getLogs, getLogDetail } from '@/api/log'

const loading = ref(false)
const logList = ref([])
const total = ref(0)
const detailVisible = ref(false)
const detailData = ref({})

const queryParams = reactive({
  page: 1,
  size: 10,
  operation: '',
  username: '',
  status: null
})

const roleName = (role) => {
  const map = { '1': '管理员', '2': '医生', '3': '患者', '4': '医药公司', '5': '销售点', '': '未知' }
  return map[role] || '未知'
}

const roleTagType = (role) => {
  const map = { '1': 'danger', '2': 'primary', '3': 'success', '4': 'warning', '5': 'info' }
  return map[role] || ''
}

const fetchLogs = async () => {
  loading.value = true
  try {
    const params = { page: queryParams.page, size: queryParams.size }
    if (queryParams.operation) params.operation = queryParams.operation
    if (queryParams.username) params.username = queryParams.username
    if (queryParams.status !== null) params.status = queryParams.status
    const res = await getLogs(params)
    if (res.code === 20000) {
      logList.value = res.data.list || []
      total.value = res.data.total || 0
    }
  } catch (e) {
    console.error('获取安全日志失败', e)
  } finally {
    loading.value = false
  }
}

const handleSearch = () => {
  queryParams.page = 1
  fetchLogs()
}

const handleReset = () => {
  queryParams.operation = ''
  queryParams.username = ''
  queryParams.status = null
  queryParams.page = 1
  fetchLogs()
}

const showDetail = async (row) => {
  try {
    const res = await getLogDetail(row.id)
    if (res.code === 20000) {
      detailData.value = res.data
    } else {
      detailData.value = row
    }
  } catch (e) {
    detailData.value = row
  }
  detailVisible.value = true
}

onMounted(() => {
  fetchLogs()
})
</script>

<style scoped>
.log-container {
  padding: 20px;
}
.search-card {
  margin-bottom: 16px;
}
.table-card {
  min-height: 400px;
}
.pagination-wrapper {
  display: flex;
  justify-content: flex-end;
  margin-top: 16px;
}
</style>