package com.qst.medical.entity;

import lombok.Data;
import java.util.List;

@Data
public class MaterialPageInfo {
    private Long total;
    private List<Material> list;
}