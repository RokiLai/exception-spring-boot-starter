package com.roki.exception.exception;

import com.roki.exception.api.ErrorCode;
import com.roki.exception.code.builtin.CommonBusinessErrorCode;

/**
 * Runtime exception used by business code.
 * 业务代码抛出的运行时异常。
 *
 * It keeps both the numeric code used in API responses and the original string form.
 * 它同时保留响应中使用的数值错误码和原始字符串错误码。
 */
public class BusinessException extends RuntimeException {

    private final Integer code;
    private final String codeValue;

    public BusinessException(String message) {
        this(CommonBusinessErrorCode.BUSINESS_ERROR, message);
    }

    public BusinessException(ErrorCode errorCode) {
        this(errorCode, errorCode.getMessage());
    }

    public BusinessException(ErrorCode errorCode, String message) {
        this(errorCode.getCode(), errorCode.getCodeValue(), message);
    }

    protected BusinessException(Integer code, String codeValue, String message) {
        super(message);
        this.code = code;
        this.codeValue = codeValue;
    }

    public Integer getCode() {
        return code;
    }

    public String getCodeValue() {
        return codeValue;
    }
}
