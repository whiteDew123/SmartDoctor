<template>
  <div class="doctor-management">
    <!-- 面包屑 -->
    <el-breadcrumb separator="/" class="breadcrumb">
      <el-breadcrumb-item :to="{ path: '/home' }">首页</el-breadcrumb-item>
      <el-breadcrumb-item>医生信息管理</el-breadcrumb-item>
    </el-breadcrumb>

    <el-card shadow="never" class="main-card">
      <template #header>
        <div class="card-header">
          <span class="title">医生列表</span>
          <el-button v-if="isAdmin" type="primary" :icon="Plus" circle @click="handleAdd" />
        </div>
      </template>

      <!-- 搜索区域 -->
      <el-form :model="searchForm" class="search-form" inline>
        <el-form-item label="医师姓名">
          <el-input
            v-model="searchForm.keyword"
            placeholder="请输入医师姓名"
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
        <el-table-column prop="name" label="姓名" width="120" />
        <el-table-column prop="sex" label="性别" width="80">
          <template #default="{ row }">
            {{ row.sex === 1 ? '男' : '女' }}
          </template>
        </el-table-column>
        <el-table-column prop="age" label="年龄" width="80" />
        <el-table-column prop="phoneNumber" label="联系电话" width="150" />
        <el-table-column prop="doctorLevel" label="医师级别" min-width="120" show-overflow-tooltip />
        <el-table-column prop="treatType" label="诊治类型" min-width="120" show-overflow-tooltip />
        <el-table-column v-if="isAdmin" label="操作" width="240" fixed="right" align="center">
          <template #default="{ row }">
            <el-button link type="primary" :icon="Edit" @click="handleEdit(row)">编辑</el-button>
            <el-button link type="warning" :icon="Key" @click="handleResetPwd(row)">重置密码</el-button>
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
        layout="total, sizes, prev, pager, next, jumper"
        class="pagination"
        @size-change="handleSizeChange"
        @current-change="handleCurrentChange"
      />
    </el-card>

    <!-- 新增弹窗 -->
    <el-dialog
      v-model="addDialogVisible"
      title="新增医生"
      width="600px"
      :close-on-click-modal="false"
      destroy-on-close
    >
      <el-form :model="addForm" label-width="100px" :rules="rules" ref="addFormRef">
        <el-form-item label="姓名" prop="name">
          <el-input v-model="addForm.name" placeholder="请输入姓名" />
        </el-form-item>
        <el-row :gutter="20">
          <el-col :span="12">
            <el-form-item label="性别" prop="sex">
              <el-radio-group v-model="addForm.sex">
                <el-radio :value="1">男</el-radio>
                <el-radio :value="2">女</el-radio>
              </el-radio-group>
            </el-form-item>
          </el-col>
          <el-col :span="12">
            <el-form-item label="年龄" prop="age">
              <el-input-number v-model="addForm.age" :min="1" :max="120" />
            </el-form-item>
          </el-col>
        </el-row>
        <el-form-item label="所属医院" prop="hospital">
          <el-input v-model="addForm.hospital" placeholder="请输入所属医院" />
        </el-form-item>
        <el-form-item label="联系电话" prop="phoneNumber">
          <el-input v-model="addForm.phoneNumber" placeholder="请输入联系电话" />
        </el-form-item>
        <el-row :gutter="20">
          <el-col :span="12">
            <el-form-item label="医师级别" prop="levelId">
              <el-select v-model="addForm.levelId" placeholder="请选择医师级别" style="width: 100%">
                <el-option
                  v-for="item in levelOptions"
                  :key="item.id"
                  :label="item.name"
                  :value="item.id"
                />
              </el-select>
            </el-form-item>
          </el-col>
          <el-col :span="12">
            <el-form-item label="诊治类型" prop="typeId">
              <el-select v-model="addForm.typeId" placeholder="请选择诊治类型" style="width: 100%">
                <el-option
                  v-for="item in typeOptions"
                  :key="item.id"
                  :label="item.name"
                  :value="item.id"
                />
              </el-select>
            </el-form-item>
          </el-col>
        </el-row>
        <el-form-item label="密码" prop="pwd">
          <el-input v-model="addForm.pwd" type="password" placeholder="请输入密码" show-password />
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
      title="编辑医生信息"
      width="600px"
      :close-on-click-modal="false"
      destroy-on-close
    >
      <el-form :model="editForm" label-width="100px" :rules="rules" ref="editFormRef">
        <el-form-item label="姓名" prop="name">
          <el-input v-model="editForm.name" placeholder="请输入姓名" />
        </el-form-item>
        <el-row :gutter="20">
          <el-col :span="12">
            <el-form-item label="性别" prop="sex">
              <el-radio-group v-model="editForm.sex">
                <el-radio :value="1">男</el-radio>
                <el-radio :value="2">女</el-radio>
              </el-radio-group>
            </el-form-item>
          </el-col>
          <el-col :span="12">
            <el-form-item label="年龄" prop="age">
              <el-input-number v-model="editForm.age" :min="1" :max="120" />
            </el-form-item>
          </el-col>
        </el-row>
        <el-form-item label="所属医院" prop="hospital">
          <el-input v-model="editForm.hospital" placeholder="请输入所属医院" />
        </el-form-item>
        <el-form-item label="联系电话" prop="phoneNumber">
          <el-input v-model="editForm.phoneNumber" placeholder="请输入联系电话" />
        </el-form-item>
        <el-row :gutter="20">
          <el-col :span="12">
            <el-form-item label="医师级别" prop="levelId">
              <el-select v-model="editForm.levelId" placeholder="请选择医师级别" style="width: 100%">
                <el-option
                  v-for="item in levelOptions"
                  :key="item.id"
                  :label="item.name"
                  :value="item.id"
                />
              </el-select>
            </el-form-item>
          </el-col>
          <el-col :span="12">
            <el-form-item label="诊治类型" prop="typeId">
              <el-select v-model="editForm.typeId" placeholder="请选择诊治类型" style="width: 100%">
                <el-option
                  v-for="item in typeOptions"
                  :key="item.id"
                  :label="item.name"
                  :value="item.id"
                />
              </el-select>
            </el-form-item>
          </el-col>
        </el-row>
      </el-form>
      <template #footer>
        <el-button @click="editDialogVisible = false">取消</el-button>
        <el-button type="primary" @click="submitEdit">确定</el-button>
      </template>
    </el-dialog>

    <!-- 重置密码弹窗 -->
    <el-dialog
      v-model="pwdDialogVisible"
      title="重置密码"
      width="400px"
      :close-on-click-modal="false"
      destroy-on-close
    >
      <el-form :model="pwdForm" label-width="100px" :rules="pwdRules" ref="pwdFormRef">
        <el-form-item label="新密码" prop="pwd">
          <el-input
            v-model="pwdForm.pwd"
            type="password"
            placeholder="请输入新密码"
            show-password
          />
        </el-form-item>
      </el-form>
      <template #footer>
        <el-button @click="pwdDialogVisible = false">取消</el-button>
        <el-button type="primary" @click="submitResetPwd">确定</el-button>
      </template>
    </el-dialog>
  </div>
</template>

<script setup>
import { ref, reactive, computed, onMounted } from 'vue'
import { ElMessage, ElMessageBox } from 'element-plus'
import { Plus, Search, Refresh, Edit, Delete, Key } from '@element-plus/icons-vue'
import {
  getDoctorPage,
  getDoctorLevels,
  getTreatTypes,
  addDoctor,
  updateDoctor,
  deleteDoctor,
  resetPassword
} from '@/api/doctor'
import { useUserStore } from '@/store/user'

const isAdmin = computed(() => useUserStore().userInfo?.utype == 1)

const loading = ref(false)
const tableData = ref([])
const total = ref(0)
const pageNum = ref(1)
const pageSize = ref(10)

const levelOptions = ref([])
const typeOptions = ref([])

const searchForm = reactive({
  keyword: ''
})

const addDialogVisible = ref(false)
const editDialogVisible = ref(false)
const pwdDialogVisible = ref(false)
const addFormRef = ref()
const editFormRef = ref()
const pwdFormRef = ref()

const resetPwdDoctorId = ref(null)

const addForm = reactive({
  name: '',
  sex: 1,
  age: 30,
  hospital: '',
  phoneNumber: '',
  levelId: null,
  typeId: null,
  pwd: ''
})

const editForm = reactive({
  id: null,
  name: '',
  sex: 1,
  age: 30,
  hospital: '',
  phoneNumber: '',
  levelId: null,
  typeId: null,
  accountId: null
})

const pwdForm = reactive({
  pwd: ''
})

const rules = {
  name: [{ required: true, message: '请输入姓名', trigger: 'blur' }],
  phoneNumber: [{ required: true, message: '请输入联系电话', trigger: 'blur' }],
  levelId: [{ required: true, message: '请选择医师级别', trigger: 'change' }],
  typeId: [{ required: true, message: '请选择诊治类型', trigger: 'change' }],
  pwd: [{ required: true, message: '请输入密码', trigger: 'blur' }]
}

const pwdRules = {
  pwd: [{ required: true, message: '请输入新密码', trigger: 'blur' }]
}

const loadData = async () => {
  loading.value = true
  try {
    const params = { pn: pageNum.value, size: pageSize.value }
    if (searchForm.keyword) params.keyword = searchForm.keyword.trim()
    const res = await getDoctorPage(params)
    tableData.value = res.data.list || []
    total.value = res.data.total || 0
  } finally {
    loading.value = false
  }
}

const loadOptions = async () => {
  const [levelsRes, typesRes] = await Promise.all([getDoctorLevels(), getTreatTypes()])
  levelOptions.value = levelsRes.data || []
  typeOptions.value = typesRes.data || []
}

const computedIndex = (index) => {
  return (pageNum.value - 1) * pageSize.value + index + 1
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
  searchForm.keyword = ''
  pageNum.value = 1
  loadData()
}

const handleAdd = () => {
  addForm.name = ''
  addForm.sex = 1
  addForm.age = 30
  addForm.hospital = ''
  addForm.phoneNumber = ''
  addForm.levelId = null
  addForm.typeId = null
  addForm.pwd = ''
  addDialogVisible.value = true
}

const submitAdd = async () => {
  await addFormRef.value.validate()
  const res = await addDoctor({
    name: addForm.name,
    sex: addForm.sex,
    age: addForm.age,
    hospital: addForm.hospital,
    phoneNumber: addForm.phoneNumber,
    levelId: addForm.levelId,
    typeId: addForm.typeId,
    pwd: addForm.pwd
  })
  ElMessage.success(res.message || '添加成功')
  addDialogVisible.value = false
  await loadData()
}

const handleEdit = (row) => {
  editForm.id = row.id
  editForm.name = row.name
  editForm.sex = row.sex
  editForm.age = row.age
  editForm.hospital = row.hospital || ''
  editForm.phoneNumber = row.phoneNumber
  editForm.levelId = row.levelId
  editForm.typeId = row.typeId
  editForm.accountId = row.accountId
  editDialogVisible.value = true
}

const submitEdit = async () => {
  await editFormRef.value.validate()
  const res = await updateDoctor(editForm.id, {
    name: editForm.name,
    sex: editForm.sex,
    age: editForm.age,
    hospital: editForm.hospital,
    phoneNumber: editForm.phoneNumber,
    levelId: editForm.levelId,
    typeId: editForm.typeId,
    accountId: editForm.accountId
  })
  ElMessage.success(res.message || '修改成功')
  editDialogVisible.value = false
  await loadData()
}

const handleDelete = async (row) => {
  try {
    await ElMessageBox.confirm('确定要删除该医生信息吗？', '提示', {
      confirmButtonText: '确定',
      cancelButtonText: '取消',
      type: 'warning'
    })
  } catch {
    return
  }
  const res = await deleteDoctor(row.id)
  ElMessage.success(res.message || '删除成功')
  // 删除后如果当前页没数据了，且不是第一页，则回退一页
  if (tableData.value.length === 1 && pageNum.value > 1) {
    pageNum.value--
  }
  await loadData()
}

const handleResetPwd = (row) => {
  resetPwdDoctorId.value = row.id
  pwdForm.pwd = ''
  pwdDialogVisible.value = true
}

const submitResetPwd = async () => {
  await pwdFormRef.value.validate()
  const res = await resetPassword(resetPwdDoctorId.value, pwdForm.pwd)
  ElMessage.success(res.message || '密码重置成功')
  pwdDialogVisible.value = false
}

onMounted(() => {
  loadData()
  loadOptions()
})
</script>

<style lang="scss" scoped>
.doctor-management {
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