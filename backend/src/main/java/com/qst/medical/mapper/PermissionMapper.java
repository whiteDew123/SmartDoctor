package com.qst.medical.mapper;

import com.qst.medical.entity.Permission;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

@Mapper
public interface PermissionMapper {

    List<Permission> getPermission(@Param("roleName") String roleName);
}