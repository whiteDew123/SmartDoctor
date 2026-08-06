package com.qst.medical.service;

import com.qst.medical.entity.SecurityLog;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import javax.servlet.http.HttpServletRequest;

/**
 * 用于从非 Controller 层（如 Handler、Filter）记录安全日志。
 * 这些地方无法使用 AOP 注解，因此通过此 Service 手动调用。
 */
@Service
public class LoginSecurityLogService {

    private static final Logger log = LoggerFactory.getLogger(LoginSecurityLogService.class);

    @Autowired
    private SecurityLogService securityLogService;

    public void record(String username, String userRole, String operation, String detail,
                       Integer status, String resultMsg) {
        try {
            SecurityLog sl = new SecurityLog();
            sl.setUsername(username);
            sl.setUserRole(userRole);
            sl.setOperation(operation);
            sl.setDetail(detail);
            sl.setStatus(status);
            sl.setResult(resultMsg);

            // 获取请求信息
            ServletRequestAttributes attributes = (ServletRequestAttributes) RequestContextHolder.getRequestAttributes();
            if (attributes != null) {
                HttpServletRequest request = attributes.getRequest();
                sl.setIp(getClientIp(request));
                sl.setRequestUri(request.getRequestURI());
                sl.setHttpMethod(request.getMethod());
            }

            securityLogService.saveLog(sl);
        } catch (Exception e) {
            log.error("记录安全日志异常", e);
        }
    }

    private String getClientIp(HttpServletRequest request) {
        if (request == null) return "";
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