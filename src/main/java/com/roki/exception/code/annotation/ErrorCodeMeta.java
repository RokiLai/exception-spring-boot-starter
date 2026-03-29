package com.roki.exception.code.annotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * Metadata attached to a declarative enum constant.
 * 声明式枚举常量上的错误码元数据。
 *
 * Extra fields are reserved for future capabilities such as i18n and protocol mapping.
 * 额外字段为后续国际化、协议映射等能力预留。
 */
@Target(ElementType.FIELD)
@Retention(RetentionPolicy.RUNTIME)
public @interface ErrorCodeMeta {

    String detailCode();

    String message();

    String i18nKey() default "";

    int httpStatus() default 0;

    boolean retryable() default false;
}
