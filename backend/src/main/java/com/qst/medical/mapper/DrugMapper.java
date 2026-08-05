package com.qst.medical.mapper;

import com.qst.medical.entity.Drug;
import org.apache.ibatis.annotations.Mapper;
import java.util.List;

@Mapper
public interface DrugMapper {

    List<Drug> selectAll();

    Drug selectById(Long drugId);

    int insert(Drug drug);

    int updateById(Drug drug);

    int deleteById(Long drugId);

    Long countAll();
}
