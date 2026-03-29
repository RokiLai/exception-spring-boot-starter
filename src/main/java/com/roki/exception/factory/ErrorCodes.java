package com.roki.exception.factory;

import com.roki.exception.api.ErrorCode;
import com.roki.exception.code.scope.ErrorCodeScopes;

/**
 * Convenience factory for lightweight constant-style error code definitions.
 * 轻量常量式错误码定义工厂。
 */
public final class ErrorCodes {

    private ErrorCodes() {
    }

    public static ErrorCode of(String bizName, String detailCode, String message) {
        return ErrorCodeScopes.named(bizName).error(detailCode, message);
    }

    public static ErrorCode ofDefault(String detailCode, String message) {
        return ErrorCodeScopes.defaultScope().error(detailCode, message);
    }
}
