package com.qst.medical.config;

import com.qst.medical.filter.JwtFilter;
import com.qst.medical.handler.AuthenticationEntryPointHandler;
import com.qst.medical.handler.CustomAccessDeniedHandler;
import com.qst.medical.service.AccountService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

@Configuration
@EnableWebSecurity
public class SecurityConfig extends WebSecurityConfigurerAdapter {

    @Autowired
    private AccountService accountService;

    @Autowired
    private JwtFilter jwtFilter;

    @Autowired
    private AuthenticationEntryPointHandler authenticationEntryPointHandler;

    @Autowired
    private CustomAccessDeniedHandler customAccessDeniedHandler;

    @Override
    protected void configure(AuthenticationManagerBuilder auth) throws Exception {
        auth.userDetailsService(accountService).passwordEncoder(passwordEncoder());
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    @Override
    public AuthenticationManager authenticationManagerBean() throws Exception {
        return super.authenticationManagerBean();
    }

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http.csrf().disable()
                .sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS)
                .and()
                .authorizeRequests()
                .antMatchers("/api/login", "/api/account/register", "/api/account/forgot-password",
                        "/api/drugs/**", "/api/base/upload", "/image/**", "/doc.html", "/swagger-resources/**",
                        "/swagger-ui.html", "/webjars/**", "/v2/api-docs", "/swagger-resources/configuration/**",
                        "/api/**/doc.json", "/api/permissions",
                        "/api/doctors/levels", "/api/doctors/treat-types").permitAll()
                .anyRequest().authenticated()
                .and()
                .exceptionHandling()
                .authenticationEntryPoint(authenticationEntryPointHandler)
                .accessDeniedHandler(customAccessDeniedHandler)
                .and()
                .addFilterBefore(jwtFilter, UsernamePasswordAuthenticationFilter.class);
    }
}