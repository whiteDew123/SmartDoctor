package com.qst.medical.annotation;

import java.lang.annotation.*;

/**
 * 标记需要记录安全日志的 Controller 方法。
 * operation：操作名称（如 "登录"、"新增用户"、"删除用户"）
 */
@Target(ElementType.METHOD)
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface SecurityLoggable {
    String operation() default "";
}