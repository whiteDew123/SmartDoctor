package com.qst.medical.common;

import com.qst.medical.entity.DrugPageInfo;
import lombok.Data;
import java.util.HashMap;
import java.util.Map;

@Data
public class DrugResult {
    private Integer code;
    private String message;
    private Boolean success;
    private Object data;

    public static DrugResult success(DrugPageInfo drugPageInfo) {
        DrugResult result = new DrugResult();
        result.setCode(20000);
        result.setMessage("响应成功");
        result.setSuccess(true);
        Map<String, Object> dataMap = new HashMap<>();
        dataMap.put("drugPageInfo", drugPageInfo);
        result.setData(dataMap);
        return result;
    }

    public static DrugResult success(String message) {
        DrugResult result = new DrugResult();
        result.setCode(20000);
        result.setMessage(message);
        result.setSuccess(true);
        Map<String, Object> dataMap = new HashMap<>();
        result.setData(dataMap);
        return result;
    }

    public static DrugResult success(String message, Integer pages) {
        DrugResult result = new DrugResult();
        result.setCode(20000);
        result.setMessage(message);
        result.setSuccess(true);
        Map<String, Object> dataMap = new HashMap<>();
        dataMap.put("pages", pages);
        result.setData(dataMap);
        return result;
    }

    public static DrugResult error(String message) {
        DrugResult result = new DrugResult();
        result.setCode(50000);
        result.setMessage(message);
        result.setSuccess(false);
        return result;
    }
}
