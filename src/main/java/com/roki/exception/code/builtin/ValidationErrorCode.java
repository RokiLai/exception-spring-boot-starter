package com.roki.exception.code.builtin;

import com.roki.exception.code.definition.FixedErrorCode;

/**
 * Built-in validation-related error codes.
 * 内置参数校验相关错误码。
 *
 * These codes are globally fixed so clients can treat validation failures uniformly.
 * 这些错误码为全局固定值，便于客户端统一识别参数校验失败。
 */
public enum ValidationErrorCode implements FixedErrorCode {

    PARAM_VALIDATION_FAILED("9001101", "请求参数校验失败"),

    REQUEST_PARAM_MISSING("9001102", "请求参数缺失"),

    REQUEST_PARAM_TYPE_MISMATCH("9001103", "请求参数格式不正确"),

    REQUEST_BODY_INVALID("9001104", "请求体格式不正确");

    private final String fixedCodeValue;
    private final String message;

    ValidationErrorCode(String fixedCodeValue, String message) {
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
