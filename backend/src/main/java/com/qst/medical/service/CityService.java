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
 * 负责城市信息的查询和删除（级联删除关联的医保政策）
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
}