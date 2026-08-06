package com.qst.medical.mapper;

import com.qst.medical.entity.SecurityLog;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

@Mapper
public interface SecurityLogMapper {

    int insert(SecurityLog log);

    List<SecurityLog> selectByPage(@Param("offset") Integer offset,
                                   @Param("limit") Integer limit,
                                   @Param("operation") String operation,
                                   @Param("username") String username,
                                   @Param("status") Integer status);

    Long selectCount(@Param("operation") String operation,
                     @Param("username") String username,
                     @Param("status") Integer status);

    SecurityLog selectById(Long id);

    int deleteOlderThan(@Param("before") String before);
}