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

    /**
     * 添加医药公司政策
     */
    int insert(CompanyPolicy companyPolicy);

    /**
     * 修改医药公司政策
     */
    int update(CompanyPolicy companyPolicy);

    /**
     * 删除医药公司政策
     */
    int deleteById(Long id);
}