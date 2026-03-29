package com.roki.exception.code.definition;

import com.roki.exception.api.ErrorCode;
import com.roki.exception.code.scope.ErrorCodeScope;

/**
 * Lightweight immutable error code implementation used by factories and constant-style definitions.
 * 工厂方法和常量式定义使用的轻量不可变错误码实现。
 */
public final class SimpleErrorCode implements ErrorCode {

    private final String projectCode;
    private final String bizCode;
    private final String detailCode;
    private final String message;

    private SimpleErrorCode(String projectCode, String bizCode, String detailCode, String message) {
        this.projectCode = projectCode;
        this.bizCode = bizCode;
        this.detailCode = detailCode;
        this.message = message;
    }

    public static SimpleErrorCode of(String projectCode, String bizCode, String detailCode, String message) {
        return new SimpleErrorCode(projectCode, bizCode, detailCode, message);
    }

    public static SimpleErrorCode of(ErrorCodeScope scope, String detailCode, String message) {
        return new SimpleErrorCode(scope.getProjectCode(), scope.getBizCode(), detailCode, message);
    }

    @Override
    public String getProjectCode() {
        return projectCode;
    }

    @Override
    public String getBizCode() {
        return bizCode;
    }

    @Override
    public String getDetailCode() {
        return detailCode;
    }

    @Override
    public String getMessage() {
        return message;
    }
}
