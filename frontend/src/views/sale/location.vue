<template>
  <div class="sale-management">
    <el-breadcrumb separator="/" class="breadcrumb">
      <el-breadcrumb-item :to="{ path: '/home' }">首页</el-breadcrumb-item>
      <el-breadcrumb-item>销售地点管理</el-breadcrumb-item>
    </el-breadcrumb>

    <el-card shadow="never" class="main-card">
      <template #header>
        <div class="card-header">
          <span class="title">销售地点列表</span>
          <div class="header-actions">
            <el-button-group class="view-toggle">
              <el-button :type="viewMode === 'table' ? 'primary' : ''" @click="viewMode = 'table'">
                报表视图
              </el-button>
              <el-button :type="viewMode === 'map' ? 'primary' : ''" @click="switchToMap">
                地图视图
              </el-button>
            </el-button-group>
            <el-button v-if="isAdmin" type="primary" :icon="Plus" circle @click="handleAdd" style="margin-left: 12px" />
          </div>
        </div>
      </template>

      <!-- 报表视图 -->
      <template v-if="viewMode === 'table'">
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

        <el-table :data="saleList" v-loading="loading" style="width: 100%">
          <el-table-column prop="saleId" label="编号" sortable />
          <el-table-column prop="saleName" label="药店名称" show-overflow-tooltip />
          <el-table-column prop="salePhone" label="联系电话" />
          <el-table-column prop="address" label="详细地址" min-width="200" show-overflow-tooltip />
          <el-table-column prop="createtime" label="创建时间" />
          <el-table-column v-if="isAdmin" label="操作" width="160" fixed="right" align="center">
            <template #default="{ row }">
              <el-button link type="danger" :icon="Delete" @click="handleDelete(row)">删除</el-button>
              <el-button link type="primary" :icon="Edit" @click="handleEdit(row)">编辑</el-button>
            </template>
          </el-table-column>
        </el-table>

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
      </template>

      <!-- 地图视图 -->
      <template v-if="viewMode === 'map'">
        <div v-loading="mapLoading" class="map-container">
          <div id="amapContainer" class="amap-wrapper"></div>
        </div>

        <!-- 地图统计面板 -->
        <div class="map-stats">
          <div class="stats-item">
            <div class="stats-value">{{ totalSales }}</div>
            <div class="stats-label">药店总数</div>
          </div>
          <div class="stats-item">
            <div class="stats-value">{{ coveredCities }}</div>
            <div class="stats-label">覆盖城市</div>
          </div>
          <div class="stats-item">
            <div class="stats-value">{{ withCoords }}</div>
            <div class="stats-label">已标注位置</div>
          </div>
          <div class="stats-item">
            <div class="stats-value">{{ withoutCoords }}</div>
            <div class="stats-label">待标注位置</div>
          </div>
        </div>

        <!-- 地图图例 -->
        <div class="map-legend">
          <span class="legend-title">图例：</span>
          <span class="legend-item">
            <span class="legend-marker blue"></span> 药店位置（点击查看详情）
          </span>
        </div>
      </template>
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
        <el-form-item label="详细地址" prop="address">
          <el-input v-model="addForm.address" placeholder="请输入详细地址" />
        </el-form-item>
        <el-form-item label="经度" prop="longitude">
          <el-input v-model="addForm.longitude" placeholder="请输入经度" />
        </el-form-item>
        <el-form-item label="纬度" prop="latitude">
          <el-input v-model="addForm.latitude" placeholder="请输入纬度" />
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
        <el-form-item label="详细地址" prop="address">
          <el-input v-model="editForm.address" placeholder="请输入详细地址" />
        </el-form-item>
        <el-form-item label="经度" prop="longitude">
          <el-input v-model="editForm.longitude" placeholder="请输入经度" />
        </el-form-item>
        <el-form-item label="纬度" prop="latitude">
          <el-input v-model="editForm.latitude" placeholder="请输入纬度" />
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
import { ref, computed, onMounted, nextTick, watch } from 'vue'
import { ElMessage, ElMessageBox } from 'element-plus'
import { Plus, Search, Edit, Delete } from '@element-plus/icons-vue'
import { getSaleList, getAllSale, addSale, updateSale, deleteSale } from '@/api/sale'
import { useUserStore } from '@/store/user'

const isAdmin = computed(() => useUserStore().userInfo?.utype == 1)

const viewMode = ref('table')
const searchQuery = ref('')
const pageNum = ref(1)
const pageSize = ref(10)
const total = ref(0)
const loading = ref(false)
const mapLoading = ref(false)
const saleList = ref([])
const allSaleData = ref([])

const totalSales = computed(() => allSaleData.value.length)
const coveredCities = computed(() => {
  const cities = new Set()
  allSaleData.value.forEach((item) => {
    if (item.address) {
      const match = item.address.match(/(北京|上海|广州|深圳|南京|杭州|成都|重庆|武汉|西安|长沙|天津|苏州|郑州|济南|青岛|大连|沈阳|哈尔滨|福州|厦门|南昌|合肥|昆明|贵阳|南宁|海口|兰州|银川|乌鲁木齐|拉萨)/)
      if (match) cities.add(match[1])
    }
  })
  return cities.size
})
const withCoords = computed(() => allSaleData.value.filter((item) => item.longitude != null && item.latitude != null).length)
const withoutCoords = computed(() => totalSales.value - withCoords.value)

const addDialogVisible = ref(false)
const editDialogVisible = ref(false)
const addFormRef = ref()
const editFormRef = ref()

const addForm = ref({
  saleName: '',
  salePhone: '',
  address: '',
  longitude: '',
  latitude: ''
})

const editForm = ref({
  saleId: null,
  saleName: '',
  salePhone: '',
  address: '',
  longitude: '',
  latitude: ''
})

const rules = {
  saleName: [{ required: true, message: '请输入药店名称', trigger: 'blur' }],
  salePhone: [{ required: true, message: '请输入联系电话', trigger: 'blur' }]
}

let map = null
let markers = []

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
  addForm.value = { saleName: '', salePhone: '', address: '', longitude: '', latitude: '' }
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
    salePhone: row.salePhone,
    address: row.address || '',
    longitude: row.longitude != null ? String(row.longitude) : '',
    latitude: row.latitude != null ? String(row.latitude) : ''
  }
  editDialogVisible.value = true
}

const submitEdit = async () => {
  await editFormRef.value.validate()
  const res = await updateSale(editForm.value.saleId, {
    saleName: editForm.value.saleName,
    salePhone: editForm.value.salePhone,
    address: editForm.value.address,
    longitude: editForm.value.longitude ? parseFloat(editForm.value.longitude) : null,
    latitude: editForm.value.latitude ? parseFloat(editForm.value.latitude) : null
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

const clearMarkers = () => {
  if (markers.length && map) {
    markers.forEach((m) => map.remove(m))
    markers = []
  }
}

const initMap = () => {
  if (!window.AMap) {
    ElMessage.warning('高德地图加载失败，请刷新页面重试')
    return
  }
  const isDark = document.documentElement.classList.contains('dark')
  map = new window.AMap.Map('amapContainer', {
    zoom: 12,
    center: [116.397428, 39.90923],
    mapStyle: isDark ? 'amap://styles/grey' : 'amap://styles/light'
  })
}

const syncMapTheme = () => {
  if (!map) return
  const isDark = document.documentElement.classList.contains('dark')
  map.setMapStyle(isDark ? 'amap://styles/grey' : 'amap://styles/light')
}

const loadMapMarkers = async () => {
  mapLoading.value = true
  try {
    const res = await getAllSale()
    const allList = res.data || []
    allSaleData.value = allList
    clearMarkers()

    if (allList.length === 0) {
      mapLoading.value = false
      return
    }

    const validList = allList.filter((item) => item.longitude != null && item.latitude != null)
    const centerList = validList.length > 0 ? validList : allList

    markers = centerList.map((item) => {
      const lng = item.longitude || 0
      const lat = item.latitude || 0
      const marker = new window.AMap.Marker({
        position: [lng, lat],
        title: item.saleName
      })
      const content = `
        <div style="padding:4px 8px;max-width:260px;">
          <strong>${item.saleName}</strong><br/>
          <span>电话：${item.salePhone || '-'}</span><br/>
          <span>地址：${item.address || '-'}</span>
        </div>
      `
      marker.on('click', () => {
        const infoWindow = new window.AMap.InfoWindow({
          content,
          offset: new window.AMap.Pixel(0, -30)
        })
        infoWindow.open(map, marker.getPosition())
      })
      map.add(marker)
      return marker
    })

    if (centerList.length > 0) {
      map.setFitView(null, false, [60, 60, 60, 60])
    }
  } catch (e) {
    console.error('加载地图数据失败：', e)
  } finally {
    mapLoading.value = false
  }
}

const switchToMap = async () => {
  viewMode.value = 'map'
  await nextTick()
  if (!map) {
    initMap()
  }
  await loadMapMarkers()
}

onMounted(() => {
  loadData()
  const observer = new MutationObserver(() => {
    syncMapTheme()
  })
  observer.observe(document.documentElement, { attributes: true, attributeFilter: ['class'] })
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
    .header-actions {
      display: flex;
      align-items: center;
    }
  }
  .search-input {
    margin-bottom: 16px;
  }
  .pagination {
    margin-top: 20px;
    display: flex;
    justify-content: flex-end;
  }
  .map-container {
    width: 100%;
    height: 70vh;
    min-height: 500px;
    border-radius: 4px;
    overflow: hidden;
    .amap-wrapper {
      width: 100%;
      height: 100%;
    }
  }
  .map-stats {
    display: flex;
    gap: 16px;
    margin-top: 16px;
    padding: 16px;
    background: #f5f7fa;
    border-radius: 8px;
    .stats-item {
      flex: 1;
      text-align: center;
      .stats-value {
        font-size: 28px;
        font-weight: 700;
        color: #409eff;
      }
      .stats-label {
        font-size: 13px;
        color: #909399;
        margin-top: 4px;
      }
    }
  }
  .map-legend {
    margin-top: 12px;
    padding: 10px 16px;
    background: #fafafa;
    border-radius: 6px;
    font-size: 13px;
    color: #606266;
    .legend-title {
      font-weight: 600;
      margin-right: 12px;
    }
    .legend-item {
      display: inline-flex;
      align-items: center;
      gap: 6px;
    }
    .legend-marker {
      display: inline-block;
      width: 12px;
      height: 12px;
      border-radius: 50%;
      &.blue {
        background: #409eff;
      }
    }
  }
}
</style>

<style lang="scss">
/* ===== 深色模式 ===== */
html.dark .main-card .map-stats {
  background: #1d1d1d !important;
}

html.dark .main-card .map-stats .stats-value {
  color: #52c4a2 !important;
}

html.dark .main-card .map-stats .stats-label {
  color: #999 !important;
}

html.dark .main-card .map-legend {
  background: #1d1d1d !important;
  color: #ccc !important;
}

html.dark .amap-info-content {
  background: #1d1d1d !important;
  color: #e0e0e0 !important;
}

html.dark .amap-info-sharp {
  background: #1d1d1d !important;
}
</style>