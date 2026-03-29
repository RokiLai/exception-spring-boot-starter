package com.roki.exception.code.definition;

import com.roki.exception.code.annotation.ErrorCodeMeta;

import java.lang.reflect.Field;

/**
 * Declarative error code contract for enum-based definitions.
 * 基于枚举的声明式错误码契约。
 *
 * Business projects only need annotations on enum constants and no manual getters.
 * 业务方只需要在枚举常量上声明注解，无需手写 getter。
 */
public interface DeclarativeErrorCode extends ScopedErrorCode {

    @Override
    default String getDetailCode() {
        return getMeta().detailCode();
    }

    @Override
    default String getMessage() {
        return getMeta().message();
    }

    private ErrorCodeMeta getMeta() {
        if (!(this instanceof Enum<?> enumValue)) {
            throw new IllegalStateException("DeclarativeErrorCode only supports enum constants");
        }
        try {
            Field field = enumValue.getDeclaringClass().getField(enumValue.name());
            ErrorCodeMeta annotation = field.getAnnotation(ErrorCodeMeta.class);
            if (annotation == null) {
                throw new IllegalStateException("missing @ErrorCodeMeta on " + field);
            }
            return annotation;
        } catch (NoSuchFieldException e) {
            throw new IllegalStateException("failed to resolve error code metadata for " + enumValue.name(), e);
        }
    }
}
