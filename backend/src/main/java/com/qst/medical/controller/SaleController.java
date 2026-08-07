package com.qst.medical.controller;

import com.github.pagehelper.PageInfo;
import com.qst.medical.annotation.LogOperation;
import com.qst.medical.common.Result;
import com.qst.medical.entity.Sale;
import com.qst.medical.param.SaleParam;
import com.qst.medical.service.SaleService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

/**
 * 销售地点（药店）REST 接口控制器
 * 提供销售地点的分页查询、详情查询、添加、修改和删除 API
 * 所有接口需要登录后才能访问（由 SecurityConfig 统一控制）
 */
@RestController
@RequestMapping("/api/sale")
public class SaleController {

    @Autowired
    private SaleService saleService;

    /**
     * 查询全部销售地点（用于地图展示）
     *
     * 请求示例：GET /api/sale/all
     *
     * @return 所有销售地点列表
     */
    @GetMapping("/all")
    public Result<java.util.List<Sale>> getAll() {
        return Result.success(saleService.getAll());
    }

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

    /**
     * 添加销售地点
     *
     * 请求示例：POST /api/sale
     * 请求体：{ "saleName": "某某药房", "salePhone": "010-12345678" }
     *
     * @param sale 销售地点实体，saleId 由数据库自增生成
     * @return 添加后的销售地点（含自增 ID）
     */
    @PostMapping
    @LogOperation(value = "添加销售地点", operation = "ADD")
    public Result<Sale> save(@RequestBody Sale sale) {
        return Result.success(saleService.save(sale));
    }

    /**
     * 修改销售地点
     * 兼容 URL 传 ID 和 Body 传 ID 两种方式
     *
     * 请求示例：PUT /api/sale/1
     * 请求体：{ "saleName": "修改后的药房", "salePhone": "010-87654321" }
     *
     * @param sale 销售地点实体，saleId 为必填字段
     * @return 受影响的行数
     */
    @PutMapping("/{id}")
    @LogOperation(value = "修改销售地点", operation = "UPDATE")
    public Result<Integer> update(@PathVariable Long id, @RequestBody Sale sale) {
        sale.setSaleId(id);
        return Result.success(saleService.update(sale));
    }

    /**
     * 修改销售地点（Body 传 ID）
     * 兼容 Swagger 界面将 ID 放在请求体中的场景
     * 请求示例：PUT /api/sale
     * 请求体：{ "saleId": 1, "saleName": "修改后的药房", "salePhone": "010-87654321" }
     */
    @PutMapping
    @LogOperation(value = "修改销售地点", operation = "UPDATE")
    public Result<Integer> updateByBody(@RequestBody Sale sale) {
        return Result.success(saleService.update(sale));
    }

    /**
     * 删除销售地点
     *
     * 请求示例：DELETE /api/sale/1
     *
     * @param id 药店主键 ID
     * @return 受影响的行数
     */
    @DeleteMapping("/{id}")
    @LogOperation(value = "删除销售地点", operation = "DELETE")
    public Result<Integer> delete(@PathVariable Long id) {
        return Result.success(saleService.deleteById(id));
    }
}