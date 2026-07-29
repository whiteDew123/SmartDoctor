package com.qst.medical.common;

import lombok.Data;
import java.util.HashMap;
import java.util.Map;

@Data
public class UploadResult {
    private Integer code;
    private String message;
    private Boolean success;
    private Object data;

    public static UploadResult success(String url) {
        UploadResult result = new UploadResult();
        result.setCode(20000);
        result.setMessage("上传成功");
        result.setSuccess(true);
        Map<String, String> dataMap = new HashMap<>();
        dataMap.put("url", url);
        result.setData(dataMap);
        return result;
    }

    public static UploadResult error(String message) {
        UploadResult result = new UploadResult();
        result.setCode(50000);
        result.setMessage(message);
        result.setSuccess(false);
        return result;
    }
}
