package com.qst.medical.entity;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;

import java.time.LocalDateTime;

@Data
public class SecurityLog {
    private Long id;
    private String username;
    private String userRole;
    private String operation;
    private String detail;
    private String ip;
    private String requestUri;
    private String httpMethod;
    private Integer status;
    private String result;
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime createTime;
}