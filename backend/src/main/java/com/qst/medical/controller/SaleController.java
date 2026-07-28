package com.qst.medical.controller;

import com.github.pagehelper.PageInfo;
import com.qst.medical.common.Result;
import com.qst.medical.entity.Sale;
import com.qst.medical.param.SaleParam;
import com.qst.medical.service.SaleService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

/**
 * 销售地点（药店）REST 接口控制器
 * 提供销售地点的分页查询和详情查询 API
 * 所有接口需要登录后才能访问（由 SecurityConfig 统一控制）
 */
@RestController
@RequestMapping("/api/sale")
public class SaleController {

    @Autowired
    private SaleService saleService;

    /**
     * 分页查询销售地点列表
     * 支持按药店名称和联系电话模糊搜索
     *
     * 请求示例：GET /api/sale/page?pageNum=1&pageSize=10&saleName=药房&salePhone=010
     *
     * @param pageNum   当前页码，默认 1
     * @param pageSize  每页条数，默认 10
     * @param saleName  药店名称关键字（模糊搜索），可选
     * @param salePhone 联系电话关键字（模糊搜索），可选
     * @return 统一响应格式 Result 包裹 PageInfo 分页对象
     */
    @GetMapping("/page")
    public Result<PageInfo<Sale>> getByPage(
            @RequestParam(defaultValue = "1") Integer pageNum,
            @RequestParam(defaultValue = "10") Integer pageSize,
            @RequestParam(required = false) String saleName,
            @RequestParam(required = false) String salePhone) {

        SaleParam param = new SaleParam();
        param.setPageNum(pageNum);
        param.setPageSize(pageSize);
        param.setSaleName(saleName);
        param.setSalePhone(salePhone);

        return Result.success(saleService.getByPage(param));
    }

    /**
     * 根据 ID 查询单个销售地点详情
     *
     * 请求示例：GET /api/sale/1
     *
     * @param id 药店主键 ID，从 URL 路径中提取
     * @return 统一响应格式 Result 包裹销售地点详情
     */
    @GetMapping("/{id}")
    public Result<Sale> getById(@PathVariable Long id) {
        return Result.success(saleService.getById(id));
    }
}