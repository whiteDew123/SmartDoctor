package com.qst.medical.service;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.qst.medical.entity.Sale;
import com.qst.medical.mapper.SaleMapper;
import com.qst.medical.param.SaleParam;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * 销售地点（药店）业务逻辑层
 * 负责销售地点的分页查询和详情查询
 */
@Service
public class SaleService {

    @Autowired
    private SaleMapper saleMapper;

    /**
     * 分页查询销售地点
     * 使用 PageHelper 插件实现物理分页，支持按药店名称和联系电话模糊搜索
     *
     * @param param 查询参数（页码、每页条数、药店名称、联系电话）
     * @return PageInfo 分页对象，包含总条数、总页数、当前页数据等
     */
    public PageInfo<Sale> getByPage(SaleParam param) {
        PageHelper.startPage(param.getPageNum(), param.getPageSize());
        List<Sale> list = saleMapper.selectByPage(param.getSaleName(), param.getSalePhone());
        return new PageInfo<>(list);
    }

    /**
     * 根据 ID 查询单个销售地点信息
     *
     * @param saleId 药店主键 ID
     * @return 销售地点实体，未找到则返回 null
     */
    public Sale getById(Long saleId) {
        return saleMapper.selectById(saleId);
    }
}