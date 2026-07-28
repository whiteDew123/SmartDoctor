package com.qst.medical.param;

import lombok.Data;

/**
 * 医保政策查询参数模型
 * 用于接收前端传来的分页和筛选条件
 */
@Data
public class MedicalPolicyParam {

    /** 当前页码，默认第 1 页 */
    private Integer pageNum = 1;

    /** 每页显示条数，默认 10 条 */
    private Integer pageSize = 10;

    /** 政策标题（模糊搜索），为空则不筛选 */
    private String title;

    /** 所属城市 ID，为空则不筛选 */
    private Long cityId;
}