package com.qst.medical.vo;

import lombok.Data;

/**
 * 医师新增/修改请求参数。
 * 一个医师对应一个用户账户，因此需要同时维护 account 与 doctor 两张表。
 */
@Data
public class DoctorSaveParam {
    /** 医师姓名（写入 account.realname） */
    private String name;
    /** 年龄 */
    private Integer age;
    /** 性别：1男，2女 */
    private Integer sex;
    /** 所属医院 */
    private String hospital;
    /** 医师级别id */
    private Long levelId;
    /** 联系电话（写入 account.phonenumber，同时作为登录用户名） */
    private String phoneNumber;
    /** 诊治类型id */
    private Long typeId;
    /** 关联的用户账户id（修改时必传，新增时由后端生成后回填） */
    private Long accountId;
    /** 密码（新增时必传，修改时为空则保持原密码不变） */
    private String pwd;
}
