import { defineStore } from 'pinia'
import { ref } from 'vue'
import { getDrugPage, addDrug, updateDrug, deleteDrug } from '@/api/drug'
import { getSaleList } from '@/api/sale'

export const useDrugStore = defineStore('drug', () => {
  const drugList = ref([])
  const total = ref(0)
  const loading = ref(false)
  const saleList = ref([])

  const fetchDrugs = async (pageNum, pageSize) => {
    loading.value = true
    try {
      const res = await getDrugPage(pageNum, pageSize)
      const pageInfo = res.data?.drugPageInfo || {}
      drugList.value = pageInfo.list || []
      total.value = pageInfo.total || 0
      return res
    } finally {
      loading.value = false
    }
  }

  const fetchSaleList = async () => {
    const res = await getSaleList({ pageNum: 1, pageSize: 1000 })
    saleList.value = res.data?.list || []
  }

  const createDrug = async (data) => {
    return await addDrug(data)
  }

  const editDrug = async (drugId, data) => {
    return await updateDrug(drugId, data)
  }

  const removeDrug = async (drugId) => {
    return await deleteDrug(drugId)
  }

  return {
    drugList,
    total,
    loading,
    saleList,
    fetchDrugs,
    fetchSaleList,
    createDrug,
    editDrug,
    removeDrug
  }
})
