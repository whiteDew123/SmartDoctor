package com.qst.medical.controller;

import com.github.pagehelper.PageInfo;
import com.qst.medical.common.Result;
import com.qst.medical.entity.MedicalPolicy;
import com.qst.medical.model.MedicalPolicyModel;
import com.qst.medical.param.MedicalPolicyParam;
import com.qst.medical.service.MedicalPolicyService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

/**
 * 医保政策 REST 接口控制器
 * 提供医保政策的分页查询、详情查询、添加、修改和删除 API
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

    /**
     * 添加医保政策
     * 请求方式：POST，请求体为 JSON 格式
     *
     * 请求示例：POST /api/medical-policy
     * Body: {
     *   "title": "2025年度医保新政策",
     *   "message": "政策详细内容...",
     *   "cityId": 1,
     *   "createTime": "2025-01-01",
     *   "updateTime": "2025-01-01"
     * }
     *
     * @param medicalPolicy 医保政策实体，id 由数据库自增生成无需传入
     * @return 统一响应格式 Result 包裹添加后的医保政策（含自增 ID）
     */
    @PostMapping
    public Result<MedicalPolicy> save(@RequestBody MedicalPolicy medicalPolicy) {
        return Result.success(medicalPolicyService.save(medicalPolicy));
    }

    /**
     * 修改医保政策（URL 路径传 ID）
     * 请求方式：PUT，请求体为 JSON 格式
     *
     * 请求示例：PUT /api/medical-policy/1
     * Body: {
     *   "title": "修改后的标题",
     *   "message": "修改后的内容",
     *   "cityId": 2,
     *   "updateTime": "2025-06-01"
     * }
     *
     * @param id             医保政策主键 ID，从 URL 路径中提取
     * @param medicalPolicy  医保政策实体，包含要修改的字段
     * @return 统一响应格式 Result，data 为受影响的行数（1 表示成功）
     */
    @PutMapping("/{id}")
    public Result<Integer> update(@PathVariable Long id, @RequestBody MedicalPolicy medicalPolicy) {
        medicalPolicy.setId(id);
        return Result.success(medicalPolicyService.update(medicalPolicy));
    }

    /**
     * 修改医保政策（请求体传 ID）
     * 请求方式：PUT，请求体为 JSON 格式
     * 兼容 Swagger 等工具将 id 放在请求体中的场景
     *
     * 请求示例：PUT /api/medical-policy
     * Body: {
     *   "id": 1,
     *   "title": "修改后的标题",
     *   "message": "修改后的内容",
     *   "cityId": 2,
     *   "updateTime": "2025-06-01"
     * }
     *
     * @param medicalPolicy 医保政策实体，id 为必填字段用于定位记录
     * @return 统一响应格式 Result，data 为受影响的行数（1 表示成功）
     */
    @PutMapping
    public Result<Integer> updateById(@RequestBody MedicalPolicy medicalPolicy) {
        return Result.success(medicalPolicyService.update(medicalPolicy));
    }

    /**
     * 删除医保政策
     * 请求方式：DELETE
     *
     * 请求示例：DELETE /api/medical-policy/1
     *
     * @param id 医保政策主键 ID，从 URL 路径中提取
     * @return 统一响应格式 Result，data 为受影响的行数（1 表示成功，0 表示记录不存在）
     */
    @DeleteMapping("/{id}")
    public Result<Integer> deleteById(@PathVariable Long id) {
        return Result.success(medicalPolicyService.deleteById(id));
    }
}