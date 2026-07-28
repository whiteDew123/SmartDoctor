package com.qst.medical.filter;

import com.qst.medical.entity.Account;
import com.qst.medical.entity.MyUserDetails;
import com.qst.medical.mapper.AccountMapper;
import com.qst.medical.util.JwtUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@Component
public class JwtFilter extends OncePerRequestFilter {

    @Autowired
    private AccountMapper accountMapper;

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        // 方式一：从自定义头 "token" 中获取
        String token = request.getHeader("token");
        // 方式二：从标准 Authorization 头中获取（格式：Bearer <token>）
        if (token == null || token.isEmpty()) {
            String authHeader = request.getHeader("Authorization");
            if (authHeader != null && authHeader.startsWith("Bearer ")) {
                token = authHeader.substring(7);
            }
        }
        // 校验 Token 并设置认证信息
        if (token != null && !token.isEmpty() && JwtUtil.checkToken(token)) {
            Long id = JwtUtil.getId(token);
            Account account = accountMapper.selectById(id);
            if (account != null) {
                MyUserDetails userDetails = new MyUserDetails(account);
                UsernamePasswordAuthenticationToken authenticationToken =
                        new UsernamePasswordAuthenticationToken(userDetails, null, userDetails.getAuthorities());
                SecurityContextHolder.getContext().setAuthentication(authenticationToken);
            }
        }
        filterChain.doFilter(request, response);
    }
}