<template>
  <div class="city-management">
    <!-- 面包屑 -->
    <el-breadcrumb separator="/" class="breadcrumb">
      <el-breadcrumb-item :to="{ path: '/home' }">首页</el-breadcrumb-item>
      <el-breadcrumb-item>城市信息管理</el-breadcrumb-item>
    </el-breadcrumb>

    <el-card shadow="never" class="main-card">
      <template #header>
        <div class="card-header">
          <span class="title">城市信息列表</span>
          <el-button type="primary" :icon="Plus" @click="handleAdd">
            添加城市
          </el-button>
        </div>
      </template>

      <!-- 搜索框 -->
      <el-input
        v-model="searchQuery"
        placeholder="请输入城市编号、省份或城市名称进行模糊查询"
        clearable
        class="search-input"
        @input="handleSearch"
      >
        <template #append>
          <el-button :icon="Search" @click="handleSearch">查询</el-button>
        </template>
      </el-input>

      <!-- 表格 -->
      <el-table
        :data="filteredList"
        v-loading="cityStore.loading"
        style="width: 100%"
        border
        stripe
      >
        <el-table-column type="index" label="序号" width="80" />
        <el-table-column prop="cityId" label="城市ID" width="120" sortable />
        <el-table-column prop="cityNumber" label="城市编号" width="150" />
        <el-table-column prop="province" label="省份" width="150" />
        <el-table-column prop="city" label="城市" width="150" />
        <el-table-column prop="createtime" label="创建时间" width="180" />
        <el-table-column prop="updatetime" label="更新时间" width="180" />
        <el-table-column label="操作" width="150" fixed="right" align="center">
          <template #default="{ row }">
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
        :total="cityStore.total"
        layout="total, sizes, prev, pager, next, jumper"
        class="pagination"
        @size-change="handleSizeChange"
        @current-change="handleCurrentChange"
      />
    </el-card>

    <!-- 添加城市弹窗 -->
    <el-dialog
      v-model="addDialogVisible"
      title="添加城市"
      width="500px"
      :close-on-click-modal="false"
      destroy-on-close
    >
      <el-form :model="addForm" label-width="100px" :rules="rules" ref="addFormRef">
        <el-form-item label="省份" prop="province">
          <el-select
            v-model="addForm.province"
            placeholder="请选择省份"
            style="width: 100%"
            @change="handleProvinceChange"
          >
            <el-option
              v-for="province in provinceList"
              :key="province"
              :label="province"
              :value="province"
            />
          </el-select>
        </el-form-item>
        <el-form-item label="城市" prop="city">
          <el-select
            v-model="addForm.city"
            placeholder="请选择城市"
            style="width: 100%"
            :disabled="!addForm.province"
          >
            <el-option
              v-for="city in cityListByProvince"
              :key="city"
              :label="city"
              :value="city"
            />
          </el-select>
        </el-form-item>
        <el-form-item label="城市编号" prop="cityNumber">
          <el-input v-model="addForm.cityNumber" placeholder="请输入城市编号" />
        </el-form-item>
      </el-form>
      <template #footer>
        <el-button @click="addDialogVisible = false">取消</el-button>
        <el-button type="primary" @click="submitAdd">确定</el-button>
      </template>
    </el-dialog>
  </div>
</template>

<script setup>
import { ref, computed, onMounted } from 'vue'
import { ElMessage, ElMessageBox } from 'element-plus'
import { Plus, Search, Delete } from '@element-plus/icons-vue'
import { useCityStore } from '@/store/modules/city'

const cityStore = useCityStore()

const searchQuery = ref('')
const pageNum = ref(1)
const pageSize = ref(10)

const addDialogVisible = ref(false)
const addFormRef = ref()

const addForm = ref({
  province: '',
  city: '',
  cityNumber: ''
})

const rules = {
  province: [{ required: true, message: '请选择省份', trigger: 'change' }],
  city: [{ required: true, message: '请选择城市', trigger: 'change' }],
  cityNumber: [{ required: true, message: '请输入城市编号', trigger: 'blur' }]
}

// 省市数据
const provinceCityData = {
  '北京市': ['北京市'],
  '天津市': ['天津市'],
  '上海市': ['上海市'],
  '重庆市': ['重庆市'],
  '河北省': ['石家庄市', '唐山市', '秦皇岛市', '邯郸市', '邢台市', '保定市', '张家口市', '承德市', '沧州市', '廊坊市', '衡水市'],
  '山西省': ['太原市', '大同市', '阳泉市', '长治市', '晋城市', '朔州市', '晋中市', '运城市', '忻州市', '临汾市', '吕梁市'],
  '辽宁省': ['沈阳市', '大连市', '鞍山市', '抚顺市', '本溪市', '丹东市', '锦州市', '营口市', '阜新市', '辽阳市', '盘锦市', '铁岭市', '朝阳市', '葫芦岛市'],
  '吉林省': ['长春市', '吉林市', '四平市', '辽源市', '通化市', '白山市', '松原市', '白城市', '延边朝鲜族自治州'],
  '黑龙江省': ['哈尔滨市', '齐齐哈尔市', '鸡西市', '鹤岗市', '双鸭山市', '大庆市', '伊春市', '佳木斯市', '七台河市', '牡丹江市', '黑河市', '绥化市', '大兴安岭地区'],
  '江苏省': ['南京市', '无锡市', '徐州市', '常州市', '苏州市', '南通市', '连云港市', '淮安市', '盐城市', '扬州市', '镇江市', '泰州市', '宿迁市'],
  '浙江省': ['杭州市', '宁波市', '温州市', '嘉兴市', '湖州市', '绍兴市', '金华市', '衢州市', '舟山市', '台州市', '丽水市'],
  '安徽省': ['合肥市', '芜湖市', '蚌埠市', '淮南市', '马鞍山市', '淮北市', '铜陵市', '安庆市', '黄山市', '滁州市', '阜阳市', '宿州市', '六安市', '亳州市', '池州市', '宣城市'],
  '福建省': ['福州市', '厦门市', '莆田市', '三明市', '泉州市', '漳州市', '南平市', '龙岩市', '宁德市'],
  '江西省': ['南昌市', '景德镇市', '萍乡市', '九江市', '新余市', '鹰潭市', '赣州市', '吉安市', '宜春市', '抚州市', '上饶市'],
  '山东省': ['济南市', '青岛市', '淄博市', '枣庄市', '东营市', '烟台市', '潍坊市', '济宁市', '泰安市', '威海市', '日照市', '临沂市', '德州市', '聊城市', '滨州市', '菏泽市'],
  '河南省': ['郑州市', '开封市', '洛阳市', '平顶山市', '安阳市', '鹤壁市', '新乡市', '焦作市', '濮阳市', '许昌市', '漯河市', '三门峡市', '南阳市', '商丘市', '信阳市', '周口市', '驻马店市'],
  '湖北省': ['武汉市', '黄石市', '十堰市', '宜昌市', '襄阳市', '鄂州市', '荆门市', '孝感市', '荆州市', '黄冈市', '咸宁市', '随州市', '恩施土家族苗族自治州'],
  '湖南省': ['长沙市', '株洲市', '湘潭市', '衡阳市', '邵阳市', '岳阳市', '常德市', '张家界市', '益阳市', '郴州市', '永州市', '怀化市', '娄底市', '湘西土家族苗族自治州'],
  '广东省': ['广州市', '韶关市', '深圳市', '珠海市', '汕头市', '佛山市', '江门市', '湛江市', '茂名市', '肇庆市', '惠州市', '梅州市', '汕尾市', '河源市', '阳江市', '清远市', '东莞市', '中山市', '潮州市', '揭阳市', '云浮市'],
  '广西壮族自治区': ['南宁市', '柳州市', '桂林市', '梧州市', '北海市', '防城港市', '钦州市', '贵港市', '玉林市', '百色市', '贺州市', '河池市', '来宾市', '崇左市'],
  '海南省': ['海口市', '三亚市', '三沙市', '儋州市'],
  '四川省': ['成都市', '自贡市', '攀枝花市', '泸州市', '德阳市', '绵阳市', '广元市', '遂宁市', '内江市', '乐山市', '南充市', '眉山市', '宜宾市', '广安市', '达州市', '雅安市', '巴中市', '资阳市', '阿坝藏族羌族自治州', '甘孜藏族自治州', '凉山彝族自治州'],
  '贵州省': ['贵阳市', '六盘水市', '遵义市', '安顺市', '毕节市', '铜仁市', '黔西南布依族苗族自治州', '黔东南苗族侗族自治州', '黔南布依族苗族自治州'],
  '云南省': ['昆明市', '曲靖市', '玉溪市', '保山市', '昭通市', '丽江市', '普洱市', '临沧市', '楚雄彝族自治州', '红河哈尼族彝族自治州', '文山壮族苗族自治州', '西双版纳傣族自治州', '大理白族自治州', '德宏傣族景颇族自治州', '怒江傈僳族自治州', '迪庆藏族自治州'],
  '西藏自治区': ['拉萨市', '日喀则市', '昌都市', '林芝市', '山南市', '那曲市', '阿里地区'],
  '陕西省': ['西安市', '铜川市', '宝鸡市', '咸阳市', '渭南市', '延安市', '汉中市', '榆林市', '安康市', '商洛市'],
  '甘肃省': ['兰州市', '嘉峪关市', '金昌市', '白银市', '天水市', '武威市', '张掖市', '平凉市', '酒泉市', '庆阳市', '定西市', '陇南市', '临夏回族自治州', '甘南藏族自治州'],
  '青海省': ['西宁市', '海东市', '海北藏族自治州', '黄南藏族自治州', '海南藏族自治州', '果洛藏族自治州', '玉树藏族自治州', '海西蒙古族藏族自治州'],
  '宁夏回族自治区': ['银川市', '石嘴山市', '吴忠市', '固原市', '中卫市'],
  '新疆维吾尔自治区': ['乌鲁木齐市', '克拉玛依市', '吐鲁番市', '哈密市', '昌吉回族自治州', '博尔塔拉蒙古自治州', '巴音郭楞蒙古自治州', '阿克苏地区', '克孜勒苏柯尔克孜自治州', '喀什地区', '和田地区', '伊犁哈萨克自治州', '塔城地区', '阿勒泰地区']
}

const provinceList = computed(() => Object.keys(provinceCityData))

const cityListByProvince = computed(() => {
  if (!addForm.value.province) return []
  return provinceCityData[addForm.value.province] || []
})

const filteredList = computed(() => {
  if (!searchQuery.value) {
    const start = (pageNum.value - 1) * pageSize.value
    const end = start + pageSize.value
    return cityStore.cityList.slice(start, end)
  }
  const query = searchQuery.value.trim().toLowerCase()
  return cityStore.cityList.filter(
    (item) => {
      const cityNumber = item.cityNumber ? item.cityNumber.toString().toLowerCase() : ''
      const province = item.province ? item.province.toLowerCase() : ''
      const city = item.city ? item.city.toLowerCase() : ''
      return cityNumber.includes(query) || province.includes(query) || city.includes(query)
    }
  )
})

const handleProvinceChange = () => {
  addForm.value.city = ''
}

const loadData = async () => {
  await cityStore.fetchCities()
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
}

const handleAdd = () => {
  addForm.value = {
    province: '',
    city: '',
    cityNumber: ''
  }
  addDialogVisible.value = true
}

const submitAdd = async () => {
  await addFormRef.value.validate()
  const payload = {
    cityNumber: parseInt(addForm.value.cityNumber),
    province: addForm.value.province,
    city: addForm.value.city
  }
  const res = await cityStore.createCity(payload)
  ElMessage.success(res.message || '添加成功')
  addDialogVisible.value = false
  await loadData()
}

const handleDelete = async (row) => {
  try {
    await ElMessageBox.confirm(
      '确定要删除该城市信息吗？删除城市后，该城市对应的医保政策也将一并删除，此操作不可恢复！',
      '提示',
      {
        confirmButtonText: '确定',
        cancelButtonText: '取消',
        type: 'warning'
      }
    )
  } catch {
    return
  }
  const res = await cityStore.removeCity(row.cityId)
  ElMessage.success(res.message || '删除成功')
  await loadData()
}

onMounted(() => {
  loadData()
})
</script>

<style lang="scss" scoped>
.city-management {
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
  .search-input {
    width: 320px;
    margin-bottom: 16px;
  }
  .pagination {
    margin-top: 16px;
    display: flex;
    justify-content: flex-end;
  }
}
</style>