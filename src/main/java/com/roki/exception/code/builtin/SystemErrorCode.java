package com.roki.exception.code.builtin;

import com.roki.exception.code.definition.FixedErrorCode;

/**
 * Built-in system fallback error codes.
 * 内置系统兜底错误码。
 *
 * These codes are globally fixed so cross-project monitoring can rely on one value.
 * 这些错误码为全局固定值，便于跨项目监控统一识别。
 */
public enum SystemErrorCode implements FixedErrorCode {

    SYSTEM_ERROR("9001901", "系统繁忙，请稍后重试");

    private final String fixedCodeValue;
    private final String message;

    SystemErrorCode(String fixedCodeValue, String message) {
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
