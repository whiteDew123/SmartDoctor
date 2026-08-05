<template>
  <div class="home-page">
    <!-- ===== 轮播图 ===== -->
    <el-carousel
      class="home-banner"
      :interval="4000"
      height="380px"
      arrow="hover"
      indicator-position="outside"
    >
      <el-carousel-item v-for="(item, idx) in banners" :key="idx">
        <div class="banner-slide">
          <img :src="item.image" class="banner-img" alt="" />
          <div class="banner-overlay"></div>
          <div class="banner-text">
            <h2 class="banner-title">{{ item.title }}</h2>
            <p v-if="item.subTitle" class="banner-sub">{{ item.subTitle }}</p>
          </div>
        </div>
      </el-carousel-item>
    </el-carousel>

    <!-- ===== 数据面板 ===== -->
    <div class="dashboard-section" v-loading="dashboardLoading">
      <!-- 统计卡片 -->
      <el-row :gutter="20" class="stat-row">
        <el-col :xs="12" :sm="6">
          <div class="stat-card stat-doctor">
            <div class="stat-icon">
              <el-icon :size="32"><User /></el-icon>
            </div>
            <div class="stat-info">
              <div class="stat-value">{{ totalCounts.doctorCount ?? 0 }}</div>
              <div class="stat-label">医师人数</div>
            </div>
          </div>
        </el-col>
        <el-col :xs="12" :sm="6">
          <div class="stat-card stat-drug">
            <div class="stat-icon">
              <el-icon :size="32"><FirstAidKit /></el-icon>
            </div>
            <div class="stat-info">
              <div class="stat-value">{{ totalCounts.drugCount ?? 0 }}</div>
              <div class="stat-label">药物种类</div>
            </div>
          </div>
        </el-col>
        <el-col :xs="12" :sm="6">
          <div class="stat-card stat-company">
            <div class="stat-icon">
              <el-icon :size="32"><OfficeBuilding /></el-icon>
            </div>
            <div class="stat-info">
              <div class="stat-value">{{ totalCounts.companyCount ?? 0 }}</div>
              <div class="stat-label">合作企业</div>
            </div>
          </div>
        </el-col>
        <el-col :xs="12" :sm="6">
          <div class="stat-card stat-pharmacy">
            <div class="stat-icon">
              <el-icon :size="32"><Shop /></el-icon>
            </div>
            <div class="stat-info">
              <div class="stat-value">{{ totalCounts.pharmacyCount ?? 0 }}</div>
              <div class="stat-label">入驻药店</div>
            </div>
          </div>
        </el-col>
      </el-row>

      <!-- 医师职级柱状图 -->
      <el-card shadow="never" class="chart-card">
        <template #header>
          <span class="chart-title">医师职级</span>
        </template>
        <div ref="levelChartRef" class="chart-container"></div>
      </el-card>

      <!-- 底部：科室饼图 + 政策列表 -->
      <el-row :gutter="20" class="bottom-row">
        <el-col :xs="24" :md="12" class="bottom-col">
          <el-card shadow="never" class="chart-card full">
            <template #header>
              <span class="chart-title">医院科室</span>
            </template>
            <div ref="deptChartRef" class="chart-container"></div>
          </el-card>
        </el-col>
        <el-col :xs="24" :md="12" class="bottom-col">
          <div class="policy-stack">
            <el-card shadow="never" class="chart-card policy-card">
              <template #header>
                <div class="policy-header">
                  <span class="chart-title">最新医保政策</span>
                  <el-button link type="primary" size="small" @click="goPolicy('/policy/list')">More</el-button>
                </div>
              </template>
              <div class="policy-list">
                <div
                  v-for="p in latestMedicalPolicies"
                  :key="'med-' + p.id"
                  class="policy-item"
                  @click="goPolicy('/policy/list')"
                >
                  <span class="policy-dot"></span>
                  <span class="policy-text">{{ p.message }}</span>
                  <span class="policy-date">{{ formatDate(p.createTime) }}</span>
                </div>
                <el-empty v-if="!latestMedicalPolicies.length" description="暂无政策" :image-size="60" />
              </div>
            </el-card>

            <el-card shadow="never" class="chart-card policy-card">
              <template #header>
                <div class="policy-header">
                  <span class="chart-title">最新医药公司政策</span>
                  <el-button link type="primary" size="small" @click="goPolicy('/company/policy')">More</el-button>
                </div>
              </template>
              <div class="policy-list">
                <div
                  v-for="p in latestCompanyPolicies"
                  :key="'comp-' + p.id"
                  class="policy-item"
                  @click="goPolicy('/company/policy')"
                >
                  <span class="policy-dot dot-orange"></span>
                  <span class="policy-text">{{ p.message }}</span>
                  <span class="policy-date">{{ formatDate(p.createTime) }}</span>
                </div>
                <el-empty v-if="!latestCompanyPolicies.length" description="暂无政策" :image-size="60" />
              </div>
            </el-card>
          </div>
        </el-col>
      </el-row>
    </div>

    <!-- ===== 六大模块管理卡片 ===== -->
    <section class="modules">
      <div class="modules-inner">
        <div
          v-for="mod in modules"
          :key="mod.title"
          class="mod-card"
          @click="toModule(mod)"
        >
          <div class="mod-thumb">
            <img :src="mod.image" alt="" />
          </div>
          <div class="mod-title">{{ mod.title }}</div>
        </div>
      </div>
    </section>
  </div>
</template>

<script setup>
import { ref, reactive, onMounted, onUnmounted, nextTick } from 'vue'
import { useRouter } from 'vue-router'
import * as echarts from 'echarts'
import { User, FirstAidKit, OfficeBuilding, Shop } from '@element-plus/icons-vue'
import { getDashboardData } from '@/api/dashboard'

import banner1 from '@/assets/images/hUlOtf.jpg'
import banner2 from '@/assets/images/hUlLAP.png'
import banner3 from '@/assets/images/hUl7nA.jpg'
import banner4 from '@/assets/images/hUlb7t.jpg'
import banner5 from '@/assets/images/hUlH0I.png'
import modImg1 from '@/assets/images/hU0yG9.jpg'
import modImg2 from '@/assets/images/hU0KDf.jpg'
import modImg3 from '@/assets/images/hUBref.jpg'
import modImg4 from '@/assets/images/hUDHDP.jpg'
import modImg5 from '@/assets/images/hUB2Wj.jpg'
import modImg6 from '@/assets/images/hUDYn0.jpg'

const router = useRouter()
const dashboardLoading = ref(false)

const totalCounts = reactive({
  doctorCount: 0,
  drugCount: 0,
  companyCount: 0,
  pharmacyCount: 0
})

const latestMedicalPolicies = ref([])
const latestCompanyPolicies = ref([])

const banners = [
  { title: '关爱健康，从预防开始', subTitle: 'CARING FOR HEALTH STARTS FROM PREVENTION', image: banner1 },
  { title: '智能医疗，开启未来', subTitle: 'SMART MEDICINE, OPENING THE FUTURE', image: banner2 },
  { title: '传承医疗，服务大众', subTitle: 'INHERITING MEDICINE, SERVING THE PUBLIC', image: banner3 },
  { title: '专业医疗，守护生命', subTitle: 'PROFESSIONAL MEDICINE, GUARDING LIFE', image: banner4 },
  { title: '医疗呵护，伴你成长', subTitle: 'MEDICAL CARE, GROWING WITH YOU', image: banner5 }
]

const modules = [
  { title: '基础信息管理', image: modImg1, path: '/system/account' },
  { title: '药品信息管理', image: modImg2, path: '/drug/list' },
  { title: '医保政策管理', image: modImg3, path: '/policy/list' },
  { title: '医药公司政策管理', image: modImg4, path: '/company/policy' },
  { title: '医生信息管理', image: modImg5, path: '/doctor' },
  { title: '必备材料管理', image: modImg6, path: '/material' }
]

const levelChartRef = ref(null)
const deptChartRef = ref(null)
let levelChart = null
let deptChart = null

const formatDate = (dateStr) => {
  if (!dateStr) return ''
  return dateStr.replace('T', ' ').substring(0, 10)
}

const goPolicy = (path) => {
  router.push(path)
}

const toModule = (mod) => {
  router.push(mod.path)
}

const initLevelChart = (stats) => {
  if (!levelChartRef.value) return
  levelChart = echarts.init(levelChartRef.value)
  const option = {
    tooltip: { trigger: 'axis', axisPointer: { type: 'shadow' } },
    grid: { left: '3%', right: '4%', bottom: '3%', containLabel: true },
    xAxis: {
      type: 'category',
      data: stats.map(s => s.name),
      axisLabel: { color: '#666', fontSize: 12 }
    },
    yAxis: {
      type: 'value',
      minInterval: 1,
      axisLabel: { color: '#999' },
      splitLine: { lineStyle: { color: '#f0f0f0' } }
    },
    series: [{
      type: 'bar',
      data: stats.map(s => s.count),
      barWidth: '50%',
      itemStyle: {
        color: new echarts.graphic.LinearGradient(0, 0, 0, 1, [
          { offset: 0, color: '#67C23A' },
          { offset: 1, color: '#85CE61' }
        ]),
        borderRadius: [4, 4, 0, 0]
      },
      label: {
        show: true,
        position: 'top',
        color: '#666',
        fontSize: 12
      }
    }]
  }
  levelChart.setOption(option)
}

const initDeptChart = (stats) => {
  if (!deptChartRef.value) return
  deptChart = echarts.init(deptChartRef.value)
  const colors = ['#5470C6', '#91CC75', '#FAC858', '#EE6666', '#73C0DE', '#3BA272', '#FC8452', '#9A60B4']
  const option = {
    tooltip: { trigger: 'item', formatter: '{b}: {c} ({d}%)' },
    legend: {
      orient: 'vertical',
      left: 'left',
      top: 'middle',
      textStyle: { color: '#666', fontSize: 12 }
    },
    series: [{
      type: 'pie',
      radius: ['45%', '70%'],
      center: ['60%', '50%'],
      avoidLabelOverlap: true,
      itemStyle: {
        borderRadius: 4,
        borderColor: '#fff',
        borderWidth: 2
      },
      label: {
        show: false
      },
      data: stats.map((s, i) => ({
        name: s.name,
        value: s.count,
        itemStyle: { color: colors[i % colors.length] }
      }))
    }]
  }
  deptChart.setOption(option)
}

const handleResize = () => {
  levelChart?.resize()
  deptChart?.resize()
}

const loadDashboard = async () => {
  dashboardLoading.value = true
  try {
    const res = await getDashboardData()
    const data = res.data
    if (data) {
      Object.assign(totalCounts, data.totalCounts || {})
      latestMedicalPolicies.value = data.latestMedicalPolicies || []
      latestCompanyPolicies.value = data.latestCompanyPolicies || []
      await nextTick()
      initLevelChart(data.doctorLevelStats || [])
      initDeptChart(data.departmentStats || [])
    }
  } catch (e) {
    console.error('加载数据面板失败：', e)
  } finally {
    dashboardLoading.value = false
  }
}

onMounted(() => {
  loadDashboard()
  window.addEventListener('resize', handleResize)
})

onUnmounted(() => {
  window.removeEventListener('resize', handleResize)
  levelChart?.dispose()
  deptChart?.dispose()
})
</script>

<style lang="scss" scoped>
.home-page {
  width: 100%;
  min-height: 100%;
  padding: 0;
  background: #f2f4f7;
}

.home-banner {
  width: 100%;
  box-shadow: 0 2px 12px rgba(0, 0, 0, 0.05);
  background: #fff;

  .banner-slide {
    position: relative;
    width: 100%;
    height: 100%;
    overflow: hidden;

    .banner-img {
      position: absolute;
      inset: 0;
      width: 100%;
      height: 100%;
      object-fit: cover;
    }

    .banner-overlay {
      position: absolute;
      inset: 0;
      background: linear-gradient(90deg, rgba(255,255,255,0.85) 0%, rgba(255,255,255,0.4) 40%, rgba(255,255,255,0.15) 100%);
    }

    .banner-text {
      position: relative;
      z-index: 2;
      height: 100%;
      display: flex;
      flex-direction: column;
      justify-content: center;
      padding-left: 8%;
      max-width: 60%;

      .banner-title {
        margin: 0 0 12px;
        font-size: 48px;
        font-weight: 800;
        letter-spacing: 4px;
        color: #13759d;
        line-height: 1.15;
        text-shadow: 1px 2px 4px rgba(255,255,255,0.8);
      }

      .banner-sub {
        margin: 0;
        font-size: 18px;
        letter-spacing: 3px;
        color: #2fb79b;
        font-weight: 600;
      }
    }
  }
}

.dashboard-section {
  max-width: 1400px;
  margin: 0 auto;
  padding: 24px 28px 0;
}

.stat-row {
  margin-bottom: 20px;
}

.stat-card {
  display: flex;
  align-items: center;
  gap: 16px;
  padding: 20px 24px;
  background: #fff;
  border-radius: 12px;
  box-shadow: 0 2px 12px rgba(0, 0, 0, 0.06);
  margin-bottom: 16px;
  transition: transform 0.2s ease, box-shadow 0.2s ease;

  &:hover {
    transform: translateY(-2px);
    box-shadow: 0 6px 20px rgba(0, 0, 0, 0.1);
  }

  .stat-icon {
    width: 64px;
    height: 64px;
    border-radius: 12px;
    display: flex;
    align-items: center;
    justify-content: center;
    color: #fff;
    flex-shrink: 0;
  }

  &.stat-doctor .stat-icon { background: linear-gradient(135deg, #667eea, #764ba2); }
  &.stat-drug .stat-icon { background: linear-gradient(135deg, #f093fb, #f5576c); }
  &.stat-company .stat-icon { background: linear-gradient(135deg, #4facfe, #00f2fe); }
  &.stat-pharmacy .stat-icon { background: linear-gradient(135deg, #43e97b, #38f9d7); }

  .stat-info {
    .stat-value {
      font-size: 32px;
      font-weight: 700;
      color: #1f2937;
      line-height: 1.2;
    }
    .stat-label {
      font-size: 14px;
      color: #9ca3af;
      margin-top: 4px;
    }
  }
}

.chart-card {
  border-radius: 12px;
  margin-bottom: 20px;
  display: flex;
  flex-direction: column;

  :deep(.el-card__header) {
    padding: 14px 20px;
    border-bottom: 1px solid #f0f0f0;
  }

  :deep(.el-card__body) {
    padding: 12px 20px;
    flex: 1;
    display: flex;
    flex-direction: column;
  }

  .chart-title {
    font-size: 15px;
    font-weight: 600;
    color: #1f2937;
  }

  .chart-container {
    width: 100%;
    height: 260px;
  }

  &.full {
    flex: 1;

    .chart-container {
      height: 100%;
      min-height: 260px;
    }
  }
}

.bottom-row {
  margin-bottom: 8px;
  display: flex;
  flex-wrap: wrap;
  align-items: stretch;

  > .bottom-col {
    display: flex;
    flex-direction: column;
  }
}

.bottom-col {
  flex: 1;

  > .chart-card {
    margin-bottom: 0;
  }
}

.policy-stack {
  display: flex;
  flex-direction: column;
  gap: 16px;
  flex: 1;

  .policy-card {
    flex: 1;
    display: flex;
    flex-direction: column;
    margin-bottom: 0;

    :deep(.el-card__body) {
      flex: 1;
      overflow: hidden;
      display: flex;
      flex-direction: column;
    }
  }
}

.policy-header {
  display: flex;
  justify-content: space-between;
  align-items: center;
  width: 100%;
}

.policy-list {
  padding: 8px 0;
  flex: 1;
  overflow-y: auto;
}

.policy-item {
  display: flex;
  align-items: center;
  gap: 10px;
  padding: 10px 4px;
  border-bottom: 1px dashed #f0f0f0;
  cursor: pointer;
  transition: background 0.15s;

  &:last-child {
    border-bottom: none;
  }

  &:hover {
    background: #f9fafb;
  }

  .policy-dot {
    width: 6px;
    height: 6px;
    border-radius: 50%;
    background: #409EFF;
    flex-shrink: 0;

    &.dot-orange {
      background: #E6A23C;
    }
  }

  .policy-text {
    flex: 1;
    font-size: 13px;
    color: #374151;
    overflow: hidden;
    text-overflow: ellipsis;
    white-space: nowrap;
  }

  .policy-date {
    font-size: 12px;
    color: #9ca3af;
    flex-shrink: 0;
  }
}

.modules {
  width: 100%;
  padding: 24px 0 40px;

  .modules-inner {
    max-width: 1400px;
    margin: 0 auto;
    padding: 0 28px;
    display: grid;
    grid-template-columns: repeat(6, 1fr);
    gap: 24px;
  }
}

.mod-card {
  background: #fff;
  border-radius: 10px;
  overflow: hidden;
  cursor: pointer;
  box-shadow: 0 4px 14px rgba(0, 0, 0, 0.06);
  transition: transform 0.25s ease, box-shadow 0.25s ease;

  &:hover {
    transform: translateY(-4px);
    box-shadow: 0 10px 24px rgba(0, 0, 0, 0.1);
  }

  .mod-thumb {
    height: 160px;
    overflow: hidden;

    img {
      width: 100%;
      height: 100%;
      object-fit: cover;
      transition: transform 0.3s ease;
    }
  }

  &:hover .mod-thumb img {
    transform: scale(1.05);
  }

  .mod-title {
    padding: 14px 18px 16px;
    font-size: 15px;
    font-weight: 600;
    color: #1f2937;
    letter-spacing: 1px;
    border-top: 1px solid #f0f0f0;
    text-align: center;
  }
}

@media (max-width: 1300px) {
  .modules .modules-inner {
    grid-template-columns: repeat(3, 1fr);
  }
  .home-banner .banner-slide .banner-text .banner-title {
    font-size: 36px;
  }
}

@media (max-width: 860px) {
  .modules .modules-inner {
    grid-template-columns: repeat(2, 1fr);
  }
  .home-banner .banner-slide .banner-text .banner-title {
    font-size: 28px;
  }
  .dashboard-section {
    padding: 16px;
  }
  .chart-card .chart-container {
    height: 240px;
  }
}
</style>