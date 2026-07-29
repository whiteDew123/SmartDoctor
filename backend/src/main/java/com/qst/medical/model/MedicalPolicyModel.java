package com.qst.medical.model;

import com.qst.medical.entity.MedicalPolicy;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * 医保政策数据模型（视图层使用）
 * 继承 MedicalPolicy 实体，额外包含联表查询的城市相关信息
 */
@Data
@EqualsAndHashCode(callSuper = true)
public class MedicalPolicyModel extends MedicalPolicy {

    /** 城市名称，来自 china 表的 name 字段 */
    private String cityName;

    /** 城市编号，来自 city 表的 city_number 字段 */
    private String cityNumber;
}