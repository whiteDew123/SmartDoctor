package com.qst.medical.controller;

import com.qst.medical.common.Result;
import com.qst.medical.entity.Account;
import com.qst.medical.entity.MyUserDetails;
import com.qst.medical.service.LoginSecurityLogService;
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

    @Autowired
    private LoginSecurityLogService loginSecurityLogService;

    @PostMapping("/login")
    public Result<Map<String, Object>> login(@RequestBody Map<String, String> params) {
        String uname = params.get("uname");
        String pwd = params.get("pwd");
        // 前端选择的角色：1管理员，2医生，3患者
        String role = params.get("role");

        try {
            Authentication authentication = authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(uname, pwd));
            SecurityContextHolder.getContext().setAuthentication(authentication);

            MyUserDetails userDetails = (MyUserDetails) authentication.getPrincipal();
            Account account = userDetails.getAccount();

            // 角色校验：若前端选择了角色，则账户角色必须与所选角色一致
            if (role != null && !role.trim().isEmpty()
                    && !role.trim().equals(account.getUtype())) {
                String roleText = "1".equals(account.getUtype()) ? "管理员"
                        : "2".equals(account.getUtype()) ? "医生" : "患者";
                loginSecurityLogService.record(uname, account.getUtype(), "登录",
                        "角色不匹配，该账号是" + roleText, 0, "角色不匹配");
                return Result.error("该账号是" + roleText + "，请选择正确的角色登录");
            }

            String token = JwtUtil.getJwtToken(account.getId(), account.getUname(), account.getUtype());
            Map<String, Object> data = new HashMap<>();
            data.put("token", token);
            data.put("account", account);
            loginSecurityLogService.record(account.getUname(), account.getUtype(), "登录",
                    "用户登录成功", 1, "登录成功");
            return Result.success(data);
        } catch (BadCredentialsException e) {
            loginSecurityLogService.record(uname, role, "登录",
                    "用户名或密码错误", 0, "用户名或密码错误");
            return Result.error("用户名或密码错误");
        } catch (Exception e) {
            e.printStackTrace();
            loginSecurityLogService.record(uname, role, "登录",
                    "登录失败：" + e.getMessage(), 0, "登录失败");
            return Result.error("登录失败：" + e.getMessage());
        }
    }
}