package com.qst.medical.controller;

import com.github.pagehelper.PageInfo;
import com.qst.medical.common.Result;
import com.qst.medical.model.MedicalPolicyModel;
import com.qst.medical.param.MedicalPolicyParam;
import com.qst.medical.service.MedicalPolicyService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

/**
 * 医保政策 REST 接口控制器
 * 提供医保政策的分页查询和详情查询 API
 * 所有接口需要登录后才能访问（由 SecurityConfig 统一控制）
 */
@RestController
@RequestMapping("/api/medical-policy")
public class MedicalPolicyController {

    @Autowired
    private MedicalPolicyService medicalPolicyService;

    /**
     * 分页查询医保政策列表
     * 支持按标题模糊搜索和城市 ID 筛选
     * 使用 @RequestParam 逐个接收参数，避免 Swagger 界面产生重复空参数覆盖有效值
     *
     * 请求示例：GET /api/medical-policy/page?pageNum=1&pageSize=10&title=医保&cityId=1
     *
     * @param pageNum  当前页码，默认 1
     * @param pageSize 每页条数，默认 10
     * @param title    政策标题关键字（模糊搜索），可选
     * @param cityId   所属城市 ID，可选
     * @return 统一响应格式 Result 包裹 PageInfo 分页对象
     */
    @GetMapping("/page")
    public Result<PageInfo<MedicalPolicyModel>> getByPage(
            @RequestParam(defaultValue = "1") Integer pageNum,
            @RequestParam(defaultValue = "10") Integer pageSize,
            @RequestParam(required = false) String title,
            @RequestParam(required = false) Long cityId) {

        MedicalPolicyParam param = new MedicalPolicyParam();
        param.setPageNum(pageNum);
        param.setPageSize(pageSize);
        param.setTitle(title);
        param.setCityId(cityId);

        return Result.success(medicalPolicyService.getByPage(param));
    }

    /**
     * 根据 ID 查询单条医保政策详情
     *
     * 请求示例：GET /api/medical-policy/1
     *
     * @param id 医保政策主键 ID，从 URL 路径中提取
     * @return 统一响应格式 Result 包裹医保政策详情（含关联城市名称）
     */
    @GetMapping("/{id}")
    public Result<MedicalPolicyModel> getById(@PathVariable Long id) {
        return Result.success(medicalPolicyService.getById(id));
    }
}