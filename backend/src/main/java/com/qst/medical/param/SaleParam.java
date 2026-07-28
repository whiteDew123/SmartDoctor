package com.qst.medical.param;

import lombok.Data;

/**
 * 销售地点（药店）查询参数模型
 * 用于接收前端分页查询和条件搜索的参数
 */
@Data
public class SaleParam {

    /** 当前页码，默认 1 */
    private Integer pageNum;

    /** 每页条数，默认 10 */
    private Integer pageSize;

    /** 药店名称关键字（模糊搜索），可选 */
    private String saleName;

    /** 联系电话关键字（模糊搜索），可选 */
    private String salePhone;
}