package com.qst.medical.service;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.qst.medical.entity.Sale;
import com.qst.medical.mapper.SaleMapper;
import com.qst.medical.param.SaleParam;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;

/**
 * 销售地点（药店）业务逻辑层
 * 负责销售地点的分页查询、详情查询、添加、修改和删除
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

    /**
     * 添加销售地点
     *
     * @param sale 销售地点实体，saleId 由数据库自增生成
     * @return 添加后的销售地点（含自增 ID）
     */
    @Transactional(rollbackFor = Exception.class)
    public Sale save(Sale sale) {
        sale.setCreatetime(LocalDateTime.now());
        sale.setUpdatetime(LocalDateTime.now());
        saleMapper.insert(sale);
        return sale;
    }

    /**
     * 修改销售地点
     *
     * @param sale 销售地点实体，saleId 为必填字段用于定位记录
     * @return 受影响的行数（1 表示成功，0 表示记录不存在）
     */
    @Transactional(rollbackFor = Exception.class)
    public int update(Sale sale) {
        sale.setUpdatetime(LocalDateTime.now());
        return saleMapper.update(sale);
    }

    /**
     * 根据 ID 删除销售地点
     *
     * @param saleId 药店主键 ID
     * @return 受影响的行数（1 表示成功，0 表示记录不存在）
     */
    @Transactional(rollbackFor = Exception.class)
    public int deleteById(Long saleId) {
        return saleMapper.deleteById(saleId);
    }
}