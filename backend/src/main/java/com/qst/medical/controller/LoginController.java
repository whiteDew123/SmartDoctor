package com.qst.medical.controller;

import com.qst.medical.common.Result;
import com.qst.medical.entity.Account;
import com.qst.medical.entity.MyUserDetails;
import com.qst.medical.service.SecurityLogService;
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

import javax.servlet.http.HttpServletRequest;
import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/api")
public class LoginController {

    @Autowired
    private AuthenticationManager authenticationManager;

    @Autowired
    private SecurityLogService securityLogService;

    @Autowired
    private HttpServletRequest request;

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
                String reason = "该账号是" + roleText + "，请选择正确的角色登录";
                // 记录角色不匹配的日志
                securityLogService.log(account, "LOGIN", "角色不匹配: " + reason, "FAILURE", request);
                return Result.error(reason);
            }

            String token = JwtUtil.getJwtToken(account.getId(), account.getUname(), account.getUtype());
            Map<String, Object> data = new HashMap<>();
            data.put("token", token);
            data.put("account", account);

            // 记录登录成功日志
            securityLogService.log(account, "LOGIN", "用户登录成功", "SUCCESS", request);

            return Result.success(data);
        } catch (BadCredentialsException e) {
            // 记录登录失败日志（密码错误）
            securityLogService.log(uname, uname, "LOGIN",
                    "用户名或密码错误", "FAILURE", request);
            return Result.error("用户名或密码错误");
        } catch (Exception e) {
            e.printStackTrace();
            // 记录登录异常日志
            securityLogService.log(uname, uname, "LOGIN",
                    "登录异常: " + e.getMessage(), "FAILURE", request);
            return Result.error("登录失败：" + e.getMessage());
        }
    }
}