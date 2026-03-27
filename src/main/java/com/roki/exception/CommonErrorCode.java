package com.roki.exception;

public enum CommonErrorCode implements ErrorCode {

    BUSINESS_ERROR(40000, "业务处理失败"),
    PARAM_VALIDATION_FAILED(40001, "请求参数校验失败"),
    REQUEST_PARAM_MISSING(40002, "请求参数缺失"),
    REQUEST_PARAM_TYPE_MISMATCH(40003, "请求参数格式不正确"),
    REQUEST_BODY_INVALID(40004, "请求体格式不正确"),
    SYSTEM_ERROR(50000, "系统繁忙，请稍后重试");

    private final int code;
    private final String message;

    CommonErrorCode(int code, String message) {
        this.code = code;
        this.message = message;
    }

    @Override
    public int getCode() {
        return code;
    }

    @Override
    public String getMessage() {
        return message;
    }
}
