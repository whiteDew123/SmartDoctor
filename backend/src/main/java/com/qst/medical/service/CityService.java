package com.qst.medical.service;

import com.qst.medical.entity.City;
import com.qst.medical.mapper.CityMapper;
import com.qst.medical.mapper.MedicalPolicyMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * 城市信息业务逻辑层
 * 负责城市信息的查询、添加、删除（含级联删除医保政策）
 */
@Service
public class CityService {

    @Autowired
    private CityMapper cityMapper;

    @Autowired
    private MedicalPolicyMapper medicalPolicyMapper;

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
     * 删除城市（级联删除该城市下的所有医保政策）
     * 使用 @Transactional 确保两个删除操作原子性：要么全部成功，要么全部回滚
     *
     * @param cityId 城市主键 ID
     * @return 受影响的行数
     */
    @Transactional(rollbackFor = Exception.class)
    public int deleteCity(Long cityId) {
        // 先删除该城市下的所有医保政策
        medicalPolicyMapper.deleteByCityId(cityId);
        // 再删除城市本身
        return cityMapper.deleteByCityId(cityId);
    }

    /**
     * 删除城市信息（简单删除，不含级联）
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