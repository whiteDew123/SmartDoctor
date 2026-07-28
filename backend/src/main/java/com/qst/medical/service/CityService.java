package com.qst.medical.service;

import com.qst.medical.mapper.CityMapper;
import com.qst.medical.mapper.MedicalPolicyMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * 城市信息业务逻辑层
 */
@Service
public class CityService {

    @Autowired
    private CityMapper cityMapper;

    @Autowired
    private MedicalPolicyMapper medicalPolicyMapper;

    /**
     * 删除城市信息，同时删除该城市下所有医保政策
     * 使用事务确保数据一致性
     *
     * @param cityId 城市 ID
     */
    @Transactional(rollbackFor = Exception.class)
    public void deleteCity(Long cityId) {
        // 先删除该城市下的所有医保政策
        medicalPolicyMapper.deleteByCityId(cityId);
        // 再删除城市信息
        cityMapper.deleteByCityId(cityId);
    }
}