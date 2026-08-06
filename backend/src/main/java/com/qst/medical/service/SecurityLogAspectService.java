package com.qst.medical.service;

import com.qst.medical.annotation.SecurityLoggable;
import com.qst.medical.entity.MyUserDetails;
import com.qst.medical.entity.SecurityLog;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.AfterReturning;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.aspectj.lang.reflect.MethodSignature;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import javax.servlet.http.HttpServletRequest;
import java.lang.reflect.Method;

@Aspect
@Component
public class SecurityLogAspectService {

    private static final Logger log = LoggerFactory.getLogger(SecurityLogAspectService.class);

    @Autowired
    private SecurityLogService securityLogService;

    @Pointcut("@annotation(com.qst.medical.annotation.SecurityLoggable)")
    public void securityLogPointcut() {
    }

    @AfterReturning(pointcut = "securityLogPointcut()", returning = "result")
    public void logAfterReturning(JoinPoint joinPoint, Object result) {
        try {
            MethodSignature signature = (MethodSignature) joinPoint.getSignature();
            Method method = signature.getMethod();
            SecurityLoggable annotation = method.getAnnotation(SecurityLoggable.class);
            String operation = annotation.operation();

            // 获取当前用户
            Authentication auth = SecurityContextHolder.getContext().getAuthentication();
            String username = "anonymous";
            String userRole = "";
            if (auth != null && auth.getPrincipal() instanceof MyUserDetails) {
                MyUserDetails userDetails = (MyUserDetails) auth.getPrincipal();
                username = userDetails.getUsername();
                userRole = userDetails.getAccount().getUtype();
            }

            // 获取请求信息
            ServletRequestAttributes attributes = (ServletRequestAttributes) RequestContextHolder.getRequestAttributes();
            String ip = "";
            String requestUri = "";
            String httpMethod = "";
            if (attributes != null) {
                HttpServletRequest request = attributes.getRequest();
                ip = getClientIp(request);
                requestUri = request.getRequestURI();
                httpMethod = request.getMethod();
            }

            // 判断操作结果
            int status = 1;
            String resultMsg = "成功";
            if (result != null) {
                try {
                    Method getCode = result.getClass().getMethod("getCode");
                    Integer code = (Integer) getCode.invoke(result);
                    if (code == null || code != 20000) {
                        status = 0;
                        Method getMsg = result.getClass().getMethod("getMessage");
                        resultMsg = (String) getMsg.invoke(result);
                        if (resultMsg == null) resultMsg = "失败";
                    }
                } catch (Exception ignored) {
                }
            }

            // 获取方法参数详情
            String detail = buildDetail(joinPoint, operation);

            SecurityLog securityLog = new SecurityLog();
            securityLog.setUsername(username);
            securityLog.setUserRole(userRole);
            securityLog.setOperation(operation);
            securityLog.setDetail(detail);
            securityLog.setIp(ip);
            securityLog.setRequestUri(requestUri);
            securityLog.setHttpMethod(httpMethod);
            securityLog.setStatus(status);
            securityLog.setResult(resultMsg);

            securityLogService.saveLog(securityLog);
        } catch (Exception e) {
            log.error("记录安全日志异常", e);
        }
    }

    private String buildDetail(JoinPoint joinPoint, String operation) {
        Object[] args = joinPoint.getArgs();
        if (args == null || args.length == 0) {
            return operation;
        }
        StringBuilder sb = new StringBuilder();
        sb.append(operation).append(" | 参数: ");
        for (int i = 0; i < args.length; i++) {
            if (args[i] == null) {
                sb.append("null");
            } else {
                String argStr = args[i].toString();
                // 限制参数长度，避免日志过大
                if (argStr.length() > 200) {
                    argStr = argStr.substring(0, 200) + "...";
                }
                sb.append(argStr);
            }
            if (i < args.length - 1) {
                sb.append(", ");
            }
        }
        return sb.toString();
    }

    private String getClientIp(HttpServletRequest request) {
        String ip = request.getHeader("X-Forwarded-For");
        if (ip == null || ip.isEmpty() || "unknown".equalsIgnoreCase(ip)) {
            ip = request.getHeader("Proxy-Client-IP");
        }
        if (ip == null || ip.isEmpty() || "unknown".equalsIgnoreCase(ip)) {
            ip = request.getHeader("WL-Proxy-Client-IP");
        }
        if (ip == null || ip.isEmpty() || "unknown".equalsIgnoreCase(ip)) {
            ip = request.getRemoteAddr();
        }
        if (ip != null && ip.contains(",")) {
            ip = ip.split(",")[0].trim();
        }
        return ip;
    }
}