package com.roki.exception.code.scope;

import com.roki.exception.api.ErrorCode;
import com.roki.exception.code.definition.SimpleErrorCode;

/**
 * A resolved scope combines the current project code and one business code.
 * 一个解析后的 scope 同时携带当前项目码和单个业务码。
 *
 * Business projects usually obtain it from configuration-backed helpers instead of creating it directly.
 * 业务项目通常通过配置驱动的辅助方法获取它，而不是直接手动创建。
 */
public final class ErrorCodeScope {

    private final String projectCode;
    private final String bizCode;

    private ErrorCodeScope(String projectCode, String bizCode) {
        this.projectCode = projectCode;
        this.bizCode = bizCode;
    }

    public static ErrorCodeScope of(String projectCode, String bizCode) {
        validateSegment("projectCode", projectCode, ErrorCode.PROJECT_CODE_LENGTH);
        validateSegment("bizCode", bizCode, ErrorCode.BIZ_CODE_LENGTH);
        return new ErrorCodeScope(projectCode, bizCode);
    }

    public SimpleErrorCode error(String detailCode, String message) {
        return SimpleErrorCode.of(this, detailCode, message);
    }

    public String getProjectCode() {
        return projectCode;
    }

    public String getBizCode() {
        return bizCode;
    }

    private static void validateSegment(String segmentName, String value, int expectedLength) {
        if (value == null || value.isBlank()) {
            throw new IllegalArgumentException(segmentName + " must not be blank");
        }
        if (value.length() != expectedLength) {
            throw new IllegalArgumentException(segmentName + " must be exactly " + expectedLength + " digits");
        }
        for (int i = 0; i < value.length(); i++) {
            if (!Character.isDigit(value.charAt(i))) {
                throw new IllegalArgumentException(segmentName + " must contain only digits");
            }
        }
    }
}
