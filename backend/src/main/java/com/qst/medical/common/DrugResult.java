package com.qst.medical.common;

import com.qst.medical.entity.DrugPageInfo;
import lombok.Data;

@Data
public class DrugResult {
    private Integer code;
    private String message;
    private Boolean success;
    private DataWrapper data;

    public static DrugResult success(DrugPageInfo drugPageInfo) {
        DrugResult result = new DrugResult();
        result.setCode(20000);
        result.setMessage("响应成功");
        result.setSuccess(true);
        DataWrapper wrapper = new DataWrapper();
        wrapper.setDrugPageInfo(drugPageInfo);
        result.setData(wrapper);
        return result;
    }

    public static DrugResult error(String message) {
        DrugResult result = new DrugResult();
        result.setCode(50000);
        result.setMessage(message);
        result.setSuccess(false);
        return result;
    }

    @Data
    public static class DataWrapper {
        private DrugPageInfo drugPageInfo;
    }
}
