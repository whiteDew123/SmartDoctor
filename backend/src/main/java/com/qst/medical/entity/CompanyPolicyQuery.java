package com.qst.medical.entity;

import lombok.Data;

@Data
public class CompanyPolicyQuery {
    private String title;
    private Long companyId;
    private Integer pageNum = 1;
    private Integer pageSize = 10;
}