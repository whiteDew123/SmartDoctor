package com.qst.medical.service;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.qst.medical.entity.MedicalPolicy;
import com.qst.medical.mapper.MedicalPolicyMapper;
import com.qst.medical.model.MedicalPolicyModel;
import com.qst.medical.param.MedicalPolicyParam;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;

/**
 * 医保政策业务逻辑层
 * 负责医保政策的分页查询、详情查询、添加、修改和删除
 */
@Service
public class MedicalPolicyService {

    @Autowired
    private MedicalPolicyMapper medicalPolicyMapper;

    /**
     * 分页查询医保政策
     * 使用 PageHelper 插件实现物理分页
     *
     * @param param 查询参数（页码、每页条数、标题、城市 ID）
     * @return PageInfo 分页对象，包含总条数、总页数、当前页数据等
     */
    public PageInfo<MedicalPolicyModel> getByPage(MedicalPolicyParam param) {
        // 空值保护：防止 Apifox 等工具绕过默认值传入 null
        if (param.getPageNum() == null) {
            param.setPageNum(1);
        }
        if (param.getPageSize() == null) {
            param.setPageSize(10);
        }
        PageHelper.startPage(param.getPageNum(), param.getPageSize());
        List<MedicalPolicyModel> list = medicalPolicyMapper.selectByPage(param.getTitle(), param.getCityId());
        return new PageInfo<>(list);
    }

    /**
     * 根据 ID 查询医保政策详情
     *
     * @param id 医保政策主键 ID
     * @return 医保政策模型（含关联城市信息），未找到则返回 null
     */
    public MedicalPolicyModel getById(Long id) {
        return medicalPolicyMapper.selectById(id);
    }

    /**
     * 添加医保政策
     *
     * @param medicalPolicy 医保政策实体，id 字段由数据库自增生成
     * @return 添加后的医保政策（含自增 ID）
     */
    @Transactional(rollbackFor = Exception.class)
    public MedicalPolicy save(MedicalPolicy medicalPolicy) {
        medicalPolicy.setCreateTime(LocalDateTime.now());
        medicalPolicy.setUpdateTime(LocalDateTime.now());
        medicalPolicyMapper.insert(medicalPolicy);
        return medicalPolicy;
    }

    /**
     * 修改医保政策
     *
     * @param medicalPolicy 医保政策实体，id 为必填字段用于定位记录
     * @return 受影响的行数（1 表示成功，0 表示记录不存在）
     */
    @Transactional(rollbackFor = Exception.class)
    public int update(MedicalPolicy medicalPolicy) {
        medicalPolicy.setUpdateTime(LocalDateTime.now());
        return medicalPolicyMapper.update(medicalPolicy);
    }

    /**
     * 根据 ID 删除医保政策
     *
     * @param id 医保政策主键 ID
     * @return 受影响的行数（1 表示成功，0 表示记录不存在）
     */
    @Transactional(rollbackFor = Exception.class)
    public int deleteById(Long id) {
        return medicalPolicyMapper.deleteById(id);
    }

    /**
     * 查询所有城市选项（含城市名称）
     * 用于医保政策新增/编辑时的城市下拉选择
     *
     * @return 城市选项列表，仅填充 cityId、cityName、cityNumber
     */
    public List<MedicalPolicyModel> getAllCityOptions() {
        return medicalPolicyMapper.selectAllCityOptions();
    }
}