package com.qst.medical.param;

import lombok.Data;

/**
 * 医药公司查询参数模型
 * 用于接收前端分页查询和条件搜索的参数
 */
@Data
public class DrugcompanyParam {

    /** 当前页码，默认 1 */
    private Integer pageNum;

    /** 每页条数，默认 10 */
    private Integer pageSize;

    /** 公司名称关键字（模糊搜索），可选 */
    private String companyName;

    /** 公司电话关键字（模糊搜索），可选 */
    private String companyPhone;
}
