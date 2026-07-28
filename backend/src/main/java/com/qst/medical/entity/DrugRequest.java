package com.qst.medical.entity;

import lombok.Data;
import java.util.List;

@Data
public class DrugRequest {
    private Long drugId;
    private String drugName;
    private String drugInfo;
    private String drugEffect;
    private String drugImg;
    private String drugPublisher;
    private List<Long> saleIds;
}
