package com.roki.exception;

public class BusinessException extends RuntimeException {

    private final Integer code;

    public BusinessException(String message) {
        this(CommonErrorCode.BUSINESS_ERROR.getCode(), message);
    }

    public BusinessException(ErrorCode errorCode) {
        this(errorCode.getCode(), errorCode.getMessage());
    }

    protected BusinessException(Integer code, String message) {
        super(message);
        this.code = code;
    }

    public Integer getCode() {
        return code;
    }
}
