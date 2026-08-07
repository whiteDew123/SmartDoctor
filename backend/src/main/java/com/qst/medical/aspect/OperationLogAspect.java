package com.qst.medical.aspect;

import com.qst.medical.annotation.LogOperation;
import com.qst.medical.entity.Account;
import com.qst.medical.entity.MyUserDetails;
import com.qst.medical.entity.SecurityLog;
import com.qst.medical.mapper.SecurityLogMapper;
import com.qst.medical.service.SecurityLogService;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.reflect.MethodSignature;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import javax.servlet.http.HttpServletRequest;
import java.lang.reflect.Method;
import java.util.Date;

/**
 * 操作日志切面：自动拦截 @LogOperation 注解标记的方法，记录到 sys_log 表。
 */
@Slf4j
@Aspect
@Component
public class OperationLogAspect {

    @Autowired
    private SecurityLogMapper securityLogMapper;

    @Around("@annotation(com.qst.medical.annotation.LogOperation)")
    public Object around(ProceedingJoinPoint joinPoint) throws Throwable {
        // 1. 提取注解信息
        MethodSignature signature = (MethodSignature) joinPoint.getSignature();
        Method method = signature.getMethod();
        LogOperation logAnno = method.getAnnotation(LogOperation.class);
        String description = logAnno.value();
        String operation = logAnno.operation();

        // 2. 提取当前用户信息
        String username = "";
        String realname = "";
        Long accountId = null;
        try {
            Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
            if (authentication != null) {
                Object principal = authentication.getPrincipal();
                Account account = null;
                if (principal instanceof MyUserDetails) {
                    // 标准登录流程：JwtFilter / AccountService 封装为 MyUserDetails
                    account = ((MyUserDetails) principal).getAccount();
                } else if (principal instanceof Account) {
                    // 兼容旧的直接 Account 方式
                    account = (Account) principal;
                }
                if (account != null) {
                    username = account.getUname();
                    realname = account.getRealname();
                    accountId = account.getId();
                }
            }
        } catch (Exception e) {
            // 匿名操作（如登录前），无用户信息
        }

        // 3. 提取客户端 IP 和 UserAgent
        String ipAddress = "";
        String userAgent = "";
        try {
            HttpServletRequest request = ((ServletRequestAttributes) RequestContextHolder.getRequestAttributes()).getRequest();
            ipAddress = SecurityLogService.getClientIp(request);
            userAgent = request.getHeader("User-Agent");
            if (userAgent != null && userAgent.length() > 500) {
                userAgent = userAgent.substring(0, 500);
            }
        } catch (Exception ignored) {
        }

        // 4. 执行目标方法
        String status = "SUCCESS";
        String errorMsg = null;
        Object result;
        try {
            result = joinPoint.proceed();
            // 判断业务结果是否失败（通过反射支持 Result/DrugResult 等）
            if (result != null) {
                try {
                    // 优先检查 getSuccess() 方法
                    java.lang.reflect.Method getSuccess = result.getClass().getMethod("getSuccess");
                    Boolean success = (Boolean) getSuccess.invoke(result);
                    if (success != null && !success) {
                        status = "FAILURE";
                        java.lang.reflect.Method getMessage = result.getClass().getMethod("getMessage");
                        Object msg = getMessage.invoke(result);
                        errorMsg = msg != null ? msg.toString() : null;
                    }
                } catch (NoSuchMethodException ignored) {
                    // 无 getSuccess 方法，回退到检查 getCode()
                    try {
                        java.lang.reflect.Method getCode = result.getClass().getMethod("getCode");
                        Object codeObj = getCode.invoke(result);
                        if (codeObj != null) {
                            int codeVal = codeObj instanceof Integer ? (Integer) codeObj : 0;
                            if (codeVal != 200 && codeVal != 20000) {
                                status = "FAILURE";
                                java.lang.reflect.Method getMessage = result.getClass().getMethod("getMessage");
                                Object msg = getMessage.invoke(result);
                                errorMsg = msg != null ? msg.toString() : null;
                            }
                        }
                    } catch (NoSuchMethodException ignored2) {
                        // 既无 getSuccess 也无 getCode，视为正常
                    }
                }
            }
        } catch (Throwable t) {
            status = "FAILURE";
            errorMsg = t.getMessage();
            if (errorMsg != null && errorMsg.length() > 500) {
                errorMsg = errorMsg.substring(0, 500);
            }
            // 仍然写入日志，再重新抛出原异常
            try {
                saveLog(accountId, username, realname, operation, description, ipAddress, userAgent, status, errorMsg);
            } catch (Exception ignored) {
            }
            throw t;
        }

        // 5. 正常分支写日志（即使业务失败也写）
        try {
            saveLog(accountId, username, realname, operation, description, ipAddress, userAgent, status, errorMsg);
        } catch (Exception e) {
            log.error("安全日志写入失败", e);
        }

        return result;
    }

    private void saveLog(Long accountId, String username, String realname,
                         String operation, String description,
                         String ipAddress, String userAgent,
                         String status, String errorMsg) {
        if (description != null && description.length() > 500) {
            description = description.substring(0, 500);
        }
        SecurityLog logEntry = SecurityLog.builder()
                .accountId(accountId)
                .username(username)
                .realname(realname)
                .operation(operation)
                .description(description)
                .ipAddress(ipAddress)
                .userAgent(userAgent)
                .status(status)
                .createTime(new Date())
                .build();
        securityLogMapper.insert(logEntry);
    }
}
