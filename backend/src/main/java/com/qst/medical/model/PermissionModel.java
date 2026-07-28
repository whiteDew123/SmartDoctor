package com.qst.medical.model;

import lombok.Data;

import java.util.ArrayList;
import java.util.List;

@Data
public class PermissionModel {
    private Integer id;
    private Integer pid;
    private String name;
    private String path;
    private String component;
    private Integer level;
    private String title;
    private List<PermissionModel> children = new ArrayList<>();
}