package com.qst.medical.controller;

import com.github.pagehelper.PageInfo;
import com.qst.medical.common.DrugResult;
import com.qst.medical.common.Result;
import com.qst.medical.entity.Drugcompany;
import com.qst.medical.param.DrugcompanyParam;
import com.qst.medical.service.DrugcompanyService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

/**
 * 医药公司 REST 接口控制器
 * 提供医药公司的分页查询、详情查询 API
 * 所有接口需要登录后才能访问（由 SecurityConfig 统一控制）
 */
@RestController
@RequestMapping("/api/companies")
public class DrugcompanyController {

    @Autowired
    private DrugcompanyService drugcompanyService;

    /**
     * 分页查询医药公司列表
     * 支持按公司名称和联系电话模糊搜索
     *
     * 请求示例：GET /api/companies/page?pageNum=1&pageSize=10&companyName=国药&companyPhone=010
     *
     * @param pageNum     当前页码，默认 1
     * @param pageSize    每页条数，默认 10
     * @param companyName 公司名称关键字（模糊搜索），可选
     * @param companyPhone 公司电话关键字（模糊搜索），可选
     * @return 统一响应格式 Result 包裹 PageInfo 分页对象
     */
    @GetMapping("/page")
    public Result<PageInfo<Drugcompany>> getByPage(
            @RequestParam(defaultValue = "1") Integer pageNum,
            @RequestParam(defaultValue = "10") Integer pageSize,
            @RequestParam(required = false) String companyName,
            @RequestParam(required = false) String companyPhone) {

        DrugcompanyParam param = new DrugcompanyParam();
        param.setPageNum(pageNum);
        param.setPageSize(pageSize);
        param.setCompanyName(companyName);
        param.setCompanyPhone(companyPhone);

        return Result.success(drugcompanyService.getByPage(param));
    }

    /**
     * 根据 ID 查询单个医药公司详情
     *
     * 请求示例：GET /api/companies/1
     *
     * @param companyId 医药公司主键 ID，从 URL 路径中提取
     * @return 统一响应格式 Result 包裹医药公司详情
     */
    @GetMapping("/{companyId}")
    public Result<Drugcompany> getById(@PathVariable Long companyId) {
        return Result.success(drugcompanyService.getById(companyId));
    }

    /**
     * 添加医药公司
     *
     * 请求示例：POST /api/companies
     * 请求体：{ "companyName": "张三大药堂", "companyPhone": "11223334455" }
     *
     * @param drugcompany 医药公司实体，companyId 由数据库自增生成
     * @return 添加成功后的总页数信息
     */
    @PostMapping("")
    public DrugResult save(@RequestBody Drugcompany drugcompany) {
        drugcompanyService.save(drugcompany);
        // 查询总页数供前端刷新列表使用
        DrugcompanyParam param = new DrugcompanyParam();
        param.setPageNum(1);
        param.setPageSize(10);
        PageInfo<Drugcompany> pageInfo = drugcompanyService.getByPage(param);
        return DrugResult.success("添加成功", pageInfo.getPages());
    }

    /**
     * 修改医药公司
     *
     * 请求示例：PUT /api/companies/1
     * 请求体：{ "companyName": "修改后的药堂", "companyPhone": "010-12345678" }
     *
     * @param companyId   医药公司主键 ID，从 URL 路径中提取
     * @param drugcompany 医药公司实体
     * @return 统一响应格式
     */
    @PutMapping("/{companyId}")
    public DrugResult update(@PathVariable Long companyId, @RequestBody Drugcompany drugcompany) {
        drugcompany.setCompanyId(companyId);
        drugcompanyService.update(drugcompany);
        return DrugResult.success("修改成功");
    }

    /**
     * 删除医药公司
     *
     * 请求示例：DELETE /api/companies/1
     *
     * @param companyId 医药公司主键 ID
     * @return 统一响应格式
     */
    @DeleteMapping("/{companyId}")
    public DrugResult delete(@PathVariable Long companyId) {
        drugcompanyService.deleteById(companyId);
        return DrugResult.success("删除成功");
    }
}
