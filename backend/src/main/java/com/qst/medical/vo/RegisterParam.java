package com.qst.medical.vo;

import lombok.Data;

/**
 * 登录页注册请求参数。
 * - 角色 utype：2医生，3患者（不允许注册管理员 utype=1）
 * - 医生注册需额外填写 age/sex/hospital/levelId/typeId，注册时同步创建 doctor 记录
 * - 患者注册仅创建 account 记录
 */
@Data
public class RegisterParam {
    /** 真实姓名 */
    private String realname;
    /** 登录用户名 */
    private String uname;
    /** 密码（明文，由后端加密存储） */
    private String pwd;
    /** 手机号 */
    private String phonenumber;
    /** 角色类型：2医生，3患者 */
    private String utype;

    /* ===== 医生额外字段（患者注册时可不填） ===== */
    /** 年龄 */
    private Integer age;
    /** 性别：1男，2女 */
    private Integer sex;
    /** 所属医院 */
    private String hospital;
    /** 医师级别id */
    private Long levelId;
    /** 诊治类型id */
    private Long typeId;
}
