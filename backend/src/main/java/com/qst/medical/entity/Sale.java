package com.qst.medical.entity;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;
import java.time.LocalDateTime;

/**
 * 销售地点（药店）实体类
 * 对应数据库表 sale（药店信息表）
 */
@Data
public class Sale {

    /** 药店 ID，主键自增 */
    private Long saleId;

    /** 药店名称 */
    private String saleName;

    /** 联系电话 */
    private String salePhone;

    /** 创建时间 */
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime createtime;

    /** 更新时间 */
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime updatetime;
}