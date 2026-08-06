package com.qst.medical.controller;

import com.qst.medical.common.Result;
import com.qst.medical.entity.SecurityLog;
import com.qst.medical.service.SecurityLogService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequestMapping("/api/logs")
public class SecurityLogController {

    @Autowired
    private SecurityLogService securityLogService;

    @GetMapping
    public Result<Map<String, Object>> getAll(@RequestParam(defaultValue = "1") Integer page,
                                              @RequestParam(defaultValue = "10") Integer size,
                                              @RequestParam(required = false) String operation,
                                              @RequestParam(required = false) String username,
                                              @RequestParam(required = false) Integer status) {
        return securityLogService.getLogs(page, size, operation, username, status);
    }

    @GetMapping("/{id}")
    public Result<SecurityLog> getById(@PathVariable Long id) {
        try {
            SecurityLog log = securityLogService.getById(id);
            if (log == null) {
                return Result.error("日志不存在");
            }
            return Result.success(log);
        } catch (Exception e) {
            return Result.error("查询日志详情失败: " + e.getMessage());
        }
    }
}