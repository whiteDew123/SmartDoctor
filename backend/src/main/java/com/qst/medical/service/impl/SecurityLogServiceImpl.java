package com.qst.medical.service.impl;

import com.qst.medical.common.Result;
import com.qst.medical.entity.SecurityLog;
import com.qst.medical.mapper.SecurityLogMapper;
import com.qst.medical.service.SecurityLogService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class SecurityLogServiceImpl implements SecurityLogService {

    private static final Logger logger = LoggerFactory.getLogger(SecurityLogServiceImpl.class);

    @Autowired
    private SecurityLogMapper securityLogMapper;

    @Override
    public void saveLog(SecurityLog log) {
        try {
            securityLogMapper.insert(log);
        } catch (Exception e) {
            // 写入日志失败不应影响业务主流程
            logger.error("保存安全日志失败: {}", e.getMessage(), e);
        }
    }

    @Override
    public Result<Map<String, Object>> getLogs(Integer page, Integer size, String operation, String username, Integer status) {
        try {
            if (page == null || page < 1) page = 1;
            if (size == null || size < 1) size = 10;
            int offset = (page - 1) * size;

            Long total = securityLogMapper.selectCount(operation, username, status);
            List<SecurityLog> list = securityLogMapper.selectByPage(offset, size, operation, username, status);

            Map<String, Object> data = new HashMap<>();
            data.put("list", list);
            data.put("total", total);
            data.put("page", page);
            data.put("size", size);
            return Result.success(data);
        } catch (Exception e) {
            logger.error("查询安全日志失败: {}", e.getMessage(), e);
            return Result.error("查询安全日志失败: " + e.getMessage());
        }
    }

    @Override
    public SecurityLog getById(Long id) {
        try {
            return securityLogMapper.selectById(id);
        } catch (Exception e) {
            logger.error("查询安全日志详情失败: {}", e.getMessage(), e);
            return null;
        }
    }
}