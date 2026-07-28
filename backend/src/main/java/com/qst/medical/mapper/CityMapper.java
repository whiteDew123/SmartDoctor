package com.qst.medical.mapper;

import org.apache.ibatis.annotations.Mapper;

/**
 * 城市信息数据库操作接口
 */
@Mapper
public interface CityMapper {

    /**
     * 根据城市 ID 删除城市信息
     *
     * @param cityId 城市 ID
     * @return 受影响的行数
     */
    int deleteByCityId(Long cityId);
}