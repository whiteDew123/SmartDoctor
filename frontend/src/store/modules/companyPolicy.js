import { defineStore } from 'pinia'
import { ref } from 'vue'
import {
  getCompanyPolicyPage,
  addCompanyPolicy,
  updateCompanyPolicy,
  deleteCompanyPolicy,
  getCompanyList
} from '@/api/companyPolicy'

export const useCompanyPolicyStore = defineStore('companyPolicy', () => {
  const policyList = ref([])
  const total = ref(0)
  const loading = ref(false)
  const companyList = ref([])

  const fetchPolicies = async (pageNum, pageSize, params = {}) => {
    loading.value = true
    try {
      const res = await getCompanyPolicyPage(pageNum, pageSize, params)
      const pageInfo = res.data || {}
      policyList.value = pageInfo.list || []
      total.value = pageInfo.total || 0
      return res
    } finally {
      loading.value = false
    }
  }

  const fetchCompanyList = async () => {
    const res = await getCompanyList(1, 1000)
    companyList.value = res.data?.list || []
  }

  const createPolicy = async (data) => {
    return await addCompanyPolicy(data)
  }

  const editPolicy = async (data) => {
    return await updateCompanyPolicy(data)
  }

  const removePolicy = async (id) => {
    return await deleteCompanyPolicy(id)
  }

  return {
    policyList,
    total,
    loading,
    companyList,
    fetchPolicies,
    fetchCompanyList,
    createPolicy,
    editPolicy,
    removePolicy
  }
})