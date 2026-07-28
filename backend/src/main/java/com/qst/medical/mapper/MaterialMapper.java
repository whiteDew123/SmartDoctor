package com.qst.medical.mapper;

import com.qst.medical.entity.Material;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import java.util.List;

@Mapper
public interface MaterialMapper {

    List<Material> selectAll();

    Material selectById(Long id);

    List<Material> selectByKeyword(@Param("keyword") String keyword);
}