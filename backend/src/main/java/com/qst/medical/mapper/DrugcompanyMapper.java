package com.qst.medical.mapper;

import com.qst.medical.entity.Drugcompany;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * 医药公司数据库操作接口
 * 对应映射文件 DrugcompanyMapper.xml
 */
@Mapper
public interface DrugcompanyMapper {

    /**
     * 条件分页查询医药公司列表
     * 支持按公司名称和联系电话模糊搜索
     *
     * @param companyName  公司名称关键字（模糊搜索），可为 null
     * @param companyPhone 公司电话关键字（模糊搜索），可为 null
     * @return 医药公司列表
     */
    List<Drugcompany> selectByPage(@Param("companyName") String companyName,
                                   @Param("companyPhone") String companyPhone);

    /**
     * 根据 ID 查询单个医药公司信息
     *
     * @param companyId 医药公司主键 ID
     * @return 医药公司实体，未找到则返回 null
     */
    Drugcompany selectById(Long companyId);

    /**
     * 添加医药公司
     *
     * @param drugcompany 医药公司实体，companyId 由数据库自增生成
     * @return 受影响的行数（1 表示成功）
     */
    int insert(Drugcompany drugcompany);

    /**
     * 修改医药公司
     *
     * @param drugcompany 医药公司实体，companyId 为必填字段用于定位记录
     * @return 受影响的行数（1 表示成功，0 表示记录不存在）
     */
    int update(Drugcompany drugcompany);

    /**
     * 根据 ID 删除医药公司
     *
     * @param companyId 医药公司主键 ID
     * @return 受影响的行数（1 表示成功，0 表示记录不存在）
     */
    int deleteById(Long companyId);

    Long countAll();
}
