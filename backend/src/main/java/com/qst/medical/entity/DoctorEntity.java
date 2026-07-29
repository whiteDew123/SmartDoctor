package com.qst.medical.entity;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;
import java.time.LocalDateTime;

@Data
public class DoctorEntity {
    private Long id;
    private Long accountId;
    private String name;
    private Integer age;
    private Integer sex;
    private String hospital;
    private Long levelId;
    private String phoneNumber;
    private Long typeId;
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime createtime;
}
