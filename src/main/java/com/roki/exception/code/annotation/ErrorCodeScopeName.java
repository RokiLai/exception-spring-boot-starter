package com.roki.exception.code.annotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * Declares which configured business scope an enum-based error code belongs to.
 * 声明枚举错误码所属的已配置业务 scope。
 */
@Target(ElementType.TYPE)
@Retention(RetentionPolicy.RUNTIME)
public @interface ErrorCodeScopeName {

    String value();
}
