package com.qst.medical.entity;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;
import java.time.LocalDateTime;
import java.util.List;

@Data
public class Drug {
    private Long drugId;
    private String drugName;
    private String drugInfo;
    private String drugEffect;
    private String drugImg;
    private String publisher;
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime createtime;
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime updatetime;
    /** 关联的销售地点ID列表（非数据库字段） */
    private List<Long> saleIds;
}
