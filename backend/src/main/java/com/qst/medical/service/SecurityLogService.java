package com.qst.medical.service;

import com.qst.medical.common.Result;
import com.qst.medical.entity.SecurityLog;

import java.util.Map;

public interface SecurityLogService {

    void saveLog(SecurityLog log);

    Result<Map<String, Object>> getLogs(Integer page, Integer size, String operation, String username, Integer status);

    SecurityLog getById(Long id);
}