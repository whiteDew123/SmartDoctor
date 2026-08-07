package com.qst.medical.service;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.qst.medical.common.Result;
import com.qst.medical.entity.Account;
import com.qst.medical.entity.SecurityLog;
import com.qst.medical.mapper.SecurityLogMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.servlet.http.HttpServletRequest;
import java.util.List;

@Service
public class SecurityLogService {

    @Autowired
    private SecurityLogMapper securityLogMapper;

    /**
     * 记录安全日志
     */
    public void log(Account account, String operation, String description,
                    String status, HttpServletRequest request) {
        SecurityLog log = SecurityLog.builder()
                .accountId(account.getId())
                .username(account.getUname())
                .realname(account.getRealname())
                .operation(operation)
                .description(description)
                .ipAddress(getClientIp(request))
                .userAgent(request.getHeader("User-Agent"))
                .status(status)
                .build();
        securityLogMapper.insert(log);
    }

    /**
     * 记录安全日志（用于无account对象场景，如登录失败时）
     */
    public void log(String username, String realname, String operation,
                    String description, String status, HttpServletRequest request) {
        SecurityLog log = SecurityLog.builder()
                .accountId(null)
                .username(username)
                .realname(realname != null ? realname : username)
                .operation(operation)
                .description(description)
                .ipAddress(getClientIp(request))
                .userAgent(request.getHeader("User-Agent"))
                .status(status)
                .build();
        securityLogMapper.insert(log);
    }

    /**
     * 分页查询安全日志
     */
    public Result<PageInfo<SecurityLog>> page(int pageNum, int pageSize, SecurityLog condition) {
        PageHelper.startPage(pageNum, pageSize);
        List<SecurityLog> list = securityLogMapper.selectByCondition(condition);
        return Result.success(new PageInfo<>(list));
    }

    /**
     * 删除单条日志
     */
    public Result<Void> delete(Long id) {
        securityLogMapper.deleteById(id);
        return Result.success();
    }

    /**
     * 批量删除日志
     */
    public Result<Void> deleteBatch(List<Long> ids) {
        if (ids != null && !ids.isEmpty()) {
            securityLogMapper.deleteBatch(ids);
        }
        return Result.success();
    }

    /**
     * 清理指定日期之前的日志
     */
    public Result<Void> cleanBefore(String date) {
        securityLogMapper.deleteBeforeDate(date);
        return Result.success();
    }

    /**
     * 获取客户端真实IP
     */
    public static String getClientIp(HttpServletRequest request) {
        String ip = request.getHeader("X-Forwarded-For");
        if (ip == null || ip.isEmpty() || "unknown".equalsIgnoreCase(ip)) {
            ip = request.getHeader("X-Real-IP");
        }
        if (ip == null || ip.isEmpty() || "unknown".equalsIgnoreCase(ip)) {
            ip = request.getHeader("Proxy-Client-IP");
        }
        if (ip == null || ip.isEmpty() || "unknown".equalsIgnoreCase(ip)) {
            ip = request.getHeader("WL-Proxy-Client-IP");
        }
        if (ip == null || ip.isEmpty() || "unknown".equalsIgnoreCase(ip)) {
            ip = request.getRemoteAddr();
        }
        // 多级代理取第一个
        if (ip != null && ip.contains(",")) {
            ip = ip.split(",")[0].trim();
        }
        return ip;
    }
}
