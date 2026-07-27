package com.qst.medical.controller;

import com.qst.medical.common.Result;
import com.qst.medical.entity.Account;
import com.qst.medical.service.AccountService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/account")
public class AccountController {

    @Autowired
    private AccountService accountService;

    @PostMapping("/register")
    public Result<Void> register(@RequestBody Account account) {
        accountService.register(account);
        return Result.success();
    }

    @GetMapping("/{id}")
    public Result<Account> getById(@PathVariable Long id) {
        return Result.success(accountService.getById(id));
    }
}