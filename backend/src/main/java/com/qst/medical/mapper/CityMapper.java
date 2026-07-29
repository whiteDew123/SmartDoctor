package com.qst.medical.mapper;

import com.qst.medical.entity.City;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

@Mapper
public interface CityMapper {

    List<City> selectAll();

    City selectById(@Param("cityId") Long cityId);

    int deleteByCityId(@Param("cityId") Long cityId);

    int insert(City city);

    int deleteById(@Param("cityId") Long cityId);

    int checkCityExists(@Param("cityId") Long cityId);
}