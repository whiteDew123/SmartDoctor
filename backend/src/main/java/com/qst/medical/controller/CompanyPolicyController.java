package com.qst.medical.controller;

import com.github.pagehelper.PageInfo;
import com.qst.medical.common.Result;
import com.qst.medical.entity.CompanyPolicy;
import com.qst.medical.service.CompanyPolicyService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/companyPolicy")
public class CompanyPolicyController {

    @Autowired
    private CompanyPolicyService companyPolicyService;

    /**
     * 分页查询医药公司政策
     */
    @GetMapping("/list")
    public Result<PageInfo<CompanyPolicy>> list(@RequestParam(defaultValue = "1") Integer pageNum,
                                                @RequestParam(defaultValue = "10") Integer pageSize,
                                                @RequestParam(required = false) String title,
                                                @RequestParam(required = false) Long companyId) {
        PageInfo<CompanyPolicy> pageInfo = companyPolicyService.queryByPage(pageNum, pageSize, title, companyId);
        return Result.success(pageInfo);
    }

    /**
     * 根据ID查询医药公司政策
     */
    @GetMapping("/{id}")
    public Result<CompanyPolicy> getById(@PathVariable Long id) {
        CompanyPolicy companyPolicy = companyPolicyService.getById(id);
        return Result.success(companyPolicy);
    }
}