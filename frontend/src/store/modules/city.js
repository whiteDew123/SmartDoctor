import { defineStore } from 'pinia'
import { ref } from 'vue'
import { getCityList, addCity, deleteCity } from '@/api/city'

export const useCityStore = defineStore('city', () => {
  const cityList = ref([])
  const total = ref(0)
  const loading = ref(false)

  const fetchCities = async () => {
    loading.value = true
    try {
      const res = await getCityList()
      cityList.value = res.data || []
      total.value = cityList.value.length
      return res
    } finally {
      loading.value = false
    }
  }

  const createCity = async (data) => {
    return await addCity(data)
  }

  const removeCity = async (cityId) => {
    return await deleteCity(cityId)
  }

  return {
    cityList,
    total,
    loading,
    fetchCities,
    createCity,
    removeCity
  }
})