package com.qst.medical.entity;

import lombok.Data;

@Data
public class Permission {
    private Integer id;
    private Integer pid;
    private String name;
    private String path;
    private String component;
    private Integer level;
    private String title;
}