package com.qst.medical.mapper;

import com.qst.medical.entity.City;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

@Mapper
public interface CityMapper {

    /**
     * 查询所有城市信息
     */
    List<City> selectAll();

    /**
     * 根据ID查询城市信息
     */
    City selectById(Long cityId);

    /**
     * 根据ID删除城市
     */
    int deleteByCityId(Long cityId);
}