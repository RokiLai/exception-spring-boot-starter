package com.roki.exception.code.definition;

import com.roki.exception.api.ErrorCode;
import com.roki.exception.code.annotation.ErrorCodeScopeName;
import com.roki.exception.code.scope.ErrorCodeScope;
import com.roki.exception.code.scope.ErrorCodeScopes;

/**
 * Error code specialization that resolves project and business segments from a named scope.
 * 通过命名 scope 解析项目段和业务段的错误码扩展接口。
 */
public interface ScopedErrorCode extends ErrorCode {

    default ErrorCodeScope getScope() {
        ErrorCodeScopeName annotation = getClass().getAnnotation(ErrorCodeScopeName.class);
        if (annotation == null || annotation.value().isBlank()) {
            throw new IllegalStateException("missing @ErrorCodeScopeName on " + getClass().getName());
        }
        return ErrorCodeScopes.named(annotation.value());
    }

    @Override
    default String getProjectCode() {
        return getScope().getProjectCode();
    }

    @Override
    default String getBizCode() {
        return getScope().getBizCode();
    }
}
