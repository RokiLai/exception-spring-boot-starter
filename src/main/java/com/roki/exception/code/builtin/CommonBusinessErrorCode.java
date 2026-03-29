package com.roki.exception.code.builtin;

import com.roki.exception.code.definition.FixedErrorCode;

/**
 * Built-in business error codes used by the starter itself.
 * starter 内部使用的内置业务错误码。
 *
 * These codes are globally fixed so every project returns the same public code.
 * 这些错误码为全局固定值，保证所有项目返回相同的公共错误码。
 */
public enum CommonBusinessErrorCode implements FixedErrorCode {

    BUSINESS_ERROR("9001001", "业务处理失败");

    private final String fixedCodeValue;
    private final String message;

    CommonBusinessErrorCode(String fixedCodeValue, String message) {
        this.fixedCodeValue = fixedCodeValue;
        this.message = message;
    }

    @Override
    public String getFixedCodeValue() {
        return fixedCodeValue;
    }

    @Override
    public String getMessage() {
        return message;
    }
}
