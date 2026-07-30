package com.qst.medical.mapper;

import com.qst.medical.entity.DrugSale;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import java.util.List;

@Mapper
public interface DrugSaleMapper {

    List<DrugSale> selectByDrugId(Long drugId);

    List<Long> selectSaleIdsByDrugId(Long drugId);

    int insert(DrugSale drugSale);

    int deleteByDrugId(Long drugId);

    int batchInsert(@Param("drugId") Long drugId, @Param("saleIds") List<Long> saleIds);
}
