package com.qst.medical.service;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.qst.medical.entity.CompanyPolicy;
import com.qst.medical.mapper.CompanyPolicyMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class CompanyPolicyService {

    @Autowired
    private CompanyPolicyMapper companyPolicyMapper;

    /**
     * 分页查询医药公司政策
     */
    public PageInfo<CompanyPolicy> queryByPage(Integer pageNum, Integer pageSize, String title, Long companyId) {
        PageHelper.startPage(pageNum, pageSize);
        List<CompanyPolicy> list = companyPolicyMapper.selectByCondition(title, companyId);
        return new PageInfo<>(list);
    }

    /**
     * 根据ID查询医药公司政策
     */
    public CompanyPolicy getById(Long id) {
        return companyPolicyMapper.selectById(id);
    }
}