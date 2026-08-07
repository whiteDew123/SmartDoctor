package com.qst.medical.mapper;

import com.qst.medical.entity.SecurityLog;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface SecurityLogMapper {

    int insert(SecurityLog log);

    List<SecurityLog> selectByCondition(SecurityLog condition);

    int deleteById(Long id);

    int deleteBatch(List<Long> ids);

    int deleteBeforeDate(String date);
}
