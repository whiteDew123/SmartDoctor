package com.qst.medical.controller;

import com.qst.medical.common.Result;
import com.qst.medical.model.PermissionModel;
import com.qst.medical.service.PermissionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api")
public class PermissionController {

    @Autowired
    private PermissionService permissionService;

    @GetMapping("/permissions")
    public Result<Map<String, Object>> getPermissions(@RequestParam("roleName") String roleName) {
        try {
            List<PermissionModel> permissions = permissionService.getAllPermission(roleName);
            Map<String, Object> data = new HashMap<>();
            data.put("permissions", permissions);
            return Result.success(data);
        } catch (Exception e) {
            e.printStackTrace();
            return Result.error("获取权限列表失败：" + e.getMessage());
        }
    }
}