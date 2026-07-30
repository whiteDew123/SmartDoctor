package com.qst.medical.controller;

import com.qst.medical.common.Result;
import com.qst.medical.entity.Account;
import com.qst.medical.entity.MyUserDetails;
import com.qst.medical.util.JwtUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/api")
public class LoginController {

    @Autowired
    private AuthenticationManager authenticationManager;

    @PostMapping("/login")
    public Result<Map<String, Object>> login(@RequestBody Map<String, String> params) {
        String uname = params.get("uname");
        String pwd = params.get("pwd");

        try {
            Authentication authentication = authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(uname, pwd));
            SecurityContextHolder.getContext().setAuthentication(authentication);

            MyUserDetails userDetails = (MyUserDetails) authentication.getPrincipal();
            Account account = userDetails.getAccount();
            String token = JwtUtil.getJwtToken(account.getId(), account.getUname(), account.getUtype());
            Map<String, Object> data = new HashMap<>();
            data.put("token", token);
            data.put("account", account);
            return Result.success(data);
        } catch (BadCredentialsException e) {
            return Result.error("用户名或密码错误");
        } catch (Exception e) {
            e.printStackTrace();
            return Result.error("登录失败：" + e.getMessage());
        }
    }
}