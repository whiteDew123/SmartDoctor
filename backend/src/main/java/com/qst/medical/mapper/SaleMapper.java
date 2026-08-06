package com.qst.medical.mapper;

import com.qst.medical.entity.Sale;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * 销售地点（药店）数据库操作接口
 * 对应映射文件 SaleMapper.xml
 */
@Mapper
public interface SaleMapper {

    /**
     * 条件分页查询销售地点列表
     * 支持按药店名称和联系电话模糊搜索
     *
     * @param saleName  药店名称关键字（模糊搜索），可为 null
     * @param salePhone 联系电话关键字（模糊搜索），可为 null
     * @return 销售地点列表
     */
    List<Sale> selectByPage(@Param("saleName") String saleName, @Param("salePhone") String salePhone);

    /**
     * 根据 ID 查询单个销售地点信息
     *
     * @param saleId 药店主键 ID
     * @return 销售地点实体，未找到则返回 null
     */
    Sale selectById(Long saleId);

    /**
     * 添加销售地点
     *
     * @param sale 销售地点实体，saleId 由数据库自增生成
     * @return 受影响的行数（1 表示成功）
     */
    int insert(Sale sale);

    /**
     * 修改销售地点
     *
     * @param sale 销售地点实体，saleId 为必填字段用于定位记录
     * @return 受影响的行数（1 表示成功，0 表示记录不存在）
     */
    int update(Sale sale);

    /**
     * 根据 ID 删除销售地点
     *
     * @param saleId 药店主键 ID
     * @return 受影响的行数（1 表示成功，0 表示记录不存在）
     */
    int deleteById(Long saleId);

    Long countAll();

    List<Sale> selectAll();
}