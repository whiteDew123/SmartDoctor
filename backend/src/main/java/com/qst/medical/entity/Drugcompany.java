package com.qst.medical.entity;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;
import java.time.LocalDateTime;

/**
 * 医药公司实体类
 * 对应数据库表 drugcompany（医药公司表）
 */
@Data
public class Drugcompany {

    /** 医药公司ID，主键自增 */
    private Long companyId;

    /** 公司名称 */
    private String companyName;

    /** 公司电话 */
    private String companyPhone;

    /** 创建时间 */
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime createtime;

    /** 更新时间 */
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime updatetime;
}
