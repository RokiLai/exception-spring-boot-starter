package com.roki.exception.code.builtin;

import com.roki.exception.code.annotation.ErrorCodeMeta;
import com.roki.exception.code.annotation.ErrorCodeScopeName;
import com.roki.exception.code.definition.DeclarativeErrorCode;

/**
 * Built-in business error codes used by the starter itself.
 * starter 内部使用的内置业务错误码。
 */
@ErrorCodeScopeName("common")
public enum CommonBusinessErrorCode implements DeclarativeErrorCode {

    @ErrorCodeMeta(detailCode = "001", message = "业务处理失败")
    BUSINESS_ERROR
}
