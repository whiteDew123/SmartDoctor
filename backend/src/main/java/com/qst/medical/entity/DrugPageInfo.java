package com.qst.medical.entity;

import lombok.Data;
import java.util.List;

@Data
public class DrugPageInfo {
    private Long total;
    private List<Drug> list;
}
