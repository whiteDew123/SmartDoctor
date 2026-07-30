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

        List<PermissionModel> permissionModels = allPermission.stream()
                .map(perm -> {
                    PermissionModel model = new PermissionModel();
                    BeanUtils.copyProperties(perm, model);
                    return model;
                })
                .collect(Collectors.toList());

        List<PermissionModel> finalPermission = new ArrayList<>();
        for (PermissionModel per : permissionModels) {
            if (per.getPid() == null || per.getPid() == 0) {
                finalPermission.add(buildTree(per, permissionModels));
            }
        }
        return finalPermission;
    }

    private PermissionModel buildTree(PermissionModel current, List<PermissionModel> all) {
        List<PermissionModel> children = new ArrayList<>();
        for (PermissionModel item : all) {
            if (current.getId().equals(item.getPid())) {
                children.add(buildTree(item, all));
            }
        }
        current.setChildren(children);
        return current;
    }
}