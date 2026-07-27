package com.qst.medical.entity;

import lombok.Data;

@Data
public class LoginVo {

    private Long id;
    private String realname;
    private String uname;
    private String phonenumber;
    private String utype;
    private String token;

    public LoginVo(Account account, String token) {
        this.id = account.getId();
        this.realname = account.getRealname();
        this.uname = account.getUname();
        this.phonenumber = account.getPhonenumber();
        this.utype = account.getUtype();
        this.token = token;
    }
}