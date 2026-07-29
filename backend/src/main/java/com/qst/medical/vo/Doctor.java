package com.qst.medical.vo;

import lombok.Data;

@Data
public class Doctor {
    private Long id;
    private String name;
    private Integer age;
    private Integer sex;
    private Long levelId;
    private String phoneNumber;
    private Long typeId;
    private Long accountId;
    private String treatType;
    private String doctorLevel;
}
