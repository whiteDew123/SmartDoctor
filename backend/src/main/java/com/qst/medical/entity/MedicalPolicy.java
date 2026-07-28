package com.qst.medical.entity;

import lombok.Data;

/**
 * 医保政策实体类
 * 对应数据库表 medical_policy（医保政策表）
 */
@Data
public class MedicalPolicy {

    /** 医保政策主键 ID */
    private Long id;

    /** 政策标题 */
    private String title;

    /** 政策简介/内容 */
    private String message;

    /** 所属城市 ID，关联 city 表的 city_id */
    private Long cityId;

    /** 创建时间 */
    private String createTime;

    /** 更新时间 */
    private String updateTime;
}