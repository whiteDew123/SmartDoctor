package com.qst.medical.entity;

import lombok.Data;

@Data
public class DrugSale {
    private Long id;
    private Long drugId;
    private Long saleId;
}
