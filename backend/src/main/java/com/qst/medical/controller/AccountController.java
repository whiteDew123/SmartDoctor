package com.qst.medical.controller;

import com.qst.medical.annotation.LogOperation;
import com.qst.medical.common.Result;
import com.qst.medical.entity.Account;
import com.qst.medical.service.AccountService;
import com.qst.medical.vo.RegisterParam;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/account")
public class AccountController {

    @Autowired
    private AccountService accountService;

    /**
     * 登录页注册：支持医生(utype=2)与患者(utype=3)。
     */
    @PostMapping("/register")
    @LogOperation(value = "用户注册", operation = "REGISTER")
    public Result<Void> register(@RequestBody RegisterParam param) {
        return accountService.registerByParam(param);
    }

    /**
     * 忘记密码重置：通过用户名+手机号验证身份后重置密码。
     */
    @PostMapping("/forgot-password")
    @LogOperation(value = "忘记密码重置", operation = "RESET_PWD")
    public Result<Void> forgotPassword(@RequestBody java.util.Map<String, String> params) {
        String uname = params.get("uname");
        String phonenumber = params.get("phonenumber");
        String newPwd = params.get("newPwd");
        return accountService.resetPasswordByPhone(uname, phonenumber, newPwd);
    }

    @GetMapping("/{id}")
    public Result<Account> getById(@PathVariable Long id) {
        return Result.success(accountService.getById(id));
    }
}