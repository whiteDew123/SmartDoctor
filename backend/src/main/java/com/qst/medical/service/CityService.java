package com.qst.medical.service;

import com.qst.medical.entity.City;
import com.qst.medical.mapper.CityMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class CityService {

    @Autowired
    private CityMapper cityMapper;

    /**
     * 查询所有城市信息
     */
    public List<City> getAll() {
        return cityMapper.selectAll();
    }

    /**
     * 根据ID查询城市信息
     */
    public City getById(Long cityId) {
        return cityMapper.selectById(cityId);
    }

    /**
     * 添加城市信息
     */
    public int add(City city) {
        return cityMapper.insert(city);
    }

    /**
     * 删除城市信息
     */
    public int delete(Long cityId) {
        return cityMapper.deleteById(cityId);
    }

    /**
     * 查询城市是否存在
     */
    public boolean checkExists(Long cityId) {
        return cityMapper.checkCityExists(cityId) > 0;
    }
}