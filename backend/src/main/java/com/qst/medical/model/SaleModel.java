package com.qst.medical.model;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;
import java.time.LocalDateTime;

/**
 * 销售地点（药店）数据模型
 * 用于查询结果返回，与 Sale 实体对应
 */
@Data
public class SaleModel {

    /** 药店 ID */
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