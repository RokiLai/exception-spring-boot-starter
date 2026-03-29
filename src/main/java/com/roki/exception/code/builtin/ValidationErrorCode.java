package com.roki.exception.code.builtin;

import com.roki.exception.code.annotation.ErrorCodeMeta;
import com.roki.exception.code.annotation.ErrorCodeScopeName;
import com.roki.exception.code.definition.DeclarativeErrorCode;

/**
 * Built-in validation-related error codes.
 * 内置参数校验相关错误码。
 */
@ErrorCodeScopeName("validation")
public enum ValidationErrorCode implements DeclarativeErrorCode {

    @ErrorCodeMeta(detailCode = "001", message = "请求参数校验失败")
    PARAM_VALIDATION_FAILED,

    @ErrorCodeMeta(detailCode = "002", message = "请求参数缺失")
    REQUEST_PARAM_MISSING,

    @ErrorCodeMeta(detailCode = "003", message = "请求参数格式不正确")
    REQUEST_PARAM_TYPE_MISMATCH,

    @ErrorCodeMeta(detailCode = "004", message = "请求体格式不正确")
    REQUEST_BODY_INVALID
}
