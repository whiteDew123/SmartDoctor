package com.qst.medical.mapper;

import com.qst.medical.entity.CompanyPolicy;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

@Mapper
public interface CompanyPolicyMapper {

    /**
     * 根据查询条件查询医药公司政策列表
     */
    List<CompanyPolicy> selectByCondition(@Param("title") String title,
                                          @Param("companyId") Long companyId);

    /**
     * 根据ID查询医药公司政策
     */
    CompanyPolicy selectById(Long id);
}