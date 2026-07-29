package com.qst.medical.service;

import com.qst.medical.entity.Permission;
import com.qst.medical.mapper.PermissionMapper;
import com.qst.medical.model.PermissionModel;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class PermissionService {

    @Autowired
    private PermissionMapper permissionMapper;

    public List<PermissionModel> getAllPermission(String roleName) {
        String finalRoleName = roleName.trim();
        List<Permission> allPermission = permissionMapper.getPermission(finalRoleName);
        List<PermissionModel> finalPermission = new ArrayList<>();

        List<PermissionModel> permissionModels = allPermission.stream()
                .map(perm -> {
                    PermissionModel model = new PermissionModel();
                    BeanUtils.copyProperties(perm, model);
                    return model;
                })
                .collect(Collectors.toList());

        for (PermissionModel per : permissionModels) {
            if (per.getPid() == 0) {
                finalPermission.add(selectChildren(per, permissionModels, finalRoleName));
            }
        }
        return finalPermission;
    }

    private PermissionModel selectChildren(PermissionModel father, List<PermissionModel> allPermission, String finalRoleName) {
        List<PermissionModel> list = new ArrayList<>();
        for (PermissionModel item : allPermission) {
            if ("1".equals(finalRoleName)) {
                String title = item.getTitle().replace("管理", "查询");
                item.setTitle(title);
            }
            if (father.getId().equals(item.getPid())) {
                list.add(selectChildren(item, allPermission, finalRoleName));
            }
        }
        father.setChildren(list);
        return father;
    }
}