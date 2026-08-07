package com.qst.medical.entity;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class SecurityLog {

    private Long id;
    /** 操作用户ID */
    private Long accountId;
    /** 用户名 */
    private String username;
    /** 真实姓名 */
    private String realname;
    /** 操作类型：LOGIN, LOGOUT, REGISTER, PASSWORD_CHANGE, PASSWORD_RESET */
    private String operation;
    /** 操作描述 */
    private String description;
    /** 客户端IP */
    private String ipAddress;
    /** 用户代理（浏览器信息） */
    private String userAgent;
    /** 操作状态：SUCCESS, FAILURE */
    private String status;
    /** 创建时间 */
    private Date createTime;

    // ===== 查询用扩展字段 =====
    private String beginTime;
    private String endTime;
}
