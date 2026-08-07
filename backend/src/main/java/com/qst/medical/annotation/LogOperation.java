package com.qst.medical.annotation;

import java.lang.annotation.*;

/**
 * 操作日志注解，标注在 Controller 方法上即可自动记录安全日志。
 */
@Target(ElementType.METHOD)
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface LogOperation {

    /** 操作描述，如"添加药品"、"删除医生" */
    String value();

    /** 操作类型：ADD / UPDATE / DELETE / QUERY / LOGIN / REGISTER / RESET_PWD 等 */
    String operation();
}
