package com.qst.medical.vo;

import lombok.Data;

@Data
public class DoctorQueryParam {
    private Integer pn = 1;
    private Integer size = 10;
    private String keyword;
}
