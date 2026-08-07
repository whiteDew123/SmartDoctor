package com.qst.medical.controller;

import com.github.pagehelper.PageInfo;
import com.qst.medical.common.Result;
import com.qst.medical.entity.MyUserDetails;
import com.qst.medical.entity.SecurityLog;
import com.qst.medical.service.SecurityLogService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * 安全日志控制器 —— 仅管理员可访问
 */
@RestController
@RequestMapping("/api/security-log")
public class SecurityLogController {

    @Autowired
    private SecurityLogService securityLogService;

    /**
     * 校验当前用户是否为管理员（utype=1）
     */
    private boolean isAdmin() {
        try {
            MyUserDetails userDetails = (MyUserDetails) SecurityContextHolder
                    .getContext().getAuthentication().getPrincipal();
            return userDetails != null && "1".equals(userDetails.getAccount().getUtype());
        } catch (Exception e) {
            return false;
        }
    }

    /**
     * 分页查询安全日志
     */
    @GetMapping
    public Result<PageInfo<SecurityLog>> page(
            @RequestParam(defaultValue = "1") int pageNum,
            @RequestParam(defaultValue = "10") int pageSize,
            SecurityLog condition) {
        try {
            if (!isAdmin()) {
                return Result.error("无权限：仅管理员可查看安全日志");
            }
            return securityLogService.page(pageNum, pageSize, condition);
        } catch (Exception e) {
            e.printStackTrace();
            return Result.error("查询安全日志失败：" + e.getMessage());
        }
    }

    /**
     * 删除单条日志
     */
    @DeleteMapping("/{id}")
    public Result<Void> delete(@PathVariable Long id) {
        try {
            if (!isAdmin()) {
                return Result.error("无权限：仅管理员可操作");
            }
            return securityLogService.delete(id);
        } catch (Exception e) {
            e.printStackTrace();
            return Result.error("删除日志失败：" + e.getMessage());
        }
    }

    /**
     * 批量删除日志
     */
    @PostMapping("/batch-delete")
    public Result<Void> deleteBatch(@RequestBody List<Long> ids) {
        try {
            if (!isAdmin()) {
                return Result.error("无权限：仅管理员可操作");
            }
            return securityLogService.deleteBatch(ids);
        } catch (Exception e) {
            e.printStackTrace();
            return Result.error("批量删除失败：" + e.getMessage());
        }
    }

    /**
     * 清理指定日期之前的日志
     */
    @PostMapping("/clean")
    public Result<Void> cleanBefore(@RequestBody java.util.Map<String, String> params) {
        try {
            if (!isAdmin()) {
                return Result.error("无权限：仅管理员可操作");
            }
            String beforeDate = params.get("beforeDate");
            return securityLogService.cleanBefore(beforeDate);
        } catch (Exception e) {
            e.printStackTrace();
            return Result.error("清理日志失败：" + e.getMessage());
        }
    }
}
