package com.qst.medical.mapper;

import com.qst.medical.model.MedicalPolicyModel;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * 医保政策数据库操作接口
 * 对应映射文件 MedicalPolicyMapper.xml
 */
@Mapper
public interface MedicalPolicyMapper {

    /**
     * 分页查询医保政策列表
     * 支持按标题模糊搜索和城市 ID 筛选
     * 使用 @Param 逐个传参，避免对象绑定问题
     *
     * @param title  政策标题关键字（模糊搜索），可为 null
     * @param cityId 所属城市 ID，可为 null
     * @return 医保政策模型列表
     */
    List<MedicalPolicyModel> selectByPage(@Param("title") String title, @Param("cityId") Long cityId);

    /**
     * 根据 ID 查询单条医保政策详情
     * 联表查询城市名称等信息
     *
     * @param id 医保政策主键 ID
     * @return 医保政策模型（含城市信息），未找到则返回 null
     */
    MedicalPolicyModel selectById(Long id);
}