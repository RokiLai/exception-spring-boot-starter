package com.roki.exception.code.builtin;

import com.roki.exception.code.annotation.ErrorCodeMeta;
import com.roki.exception.code.annotation.ErrorCodeScopeName;
import com.roki.exception.code.definition.DeclarativeErrorCode;

/**
 * Built-in system fallback error codes.
 * 内置系统兜底错误码。
 */
@ErrorCodeScopeName("system")
public enum SystemErrorCode implements DeclarativeErrorCode {

    @ErrorCodeMeta(detailCode = "001", message = "系统繁忙，请稍后重试")
    SYSTEM_ERROR
}
