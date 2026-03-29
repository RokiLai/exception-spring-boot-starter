package com.roki.exception.api;

/**
 * Structured error code contract exposed to business projects.
 * 面向业务项目暴露的结构化错误码契约。
 *
 * A complete code is composed from project, business, and detail segments.
 * 一个完整错误码由项目段、业务段和明细段共同组成。
 */
public interface ErrorCode {

    int PROJECT_CODE_LENGTH = 2;
    int BIZ_CODE_LENGTH = 2;
    int DETAIL_CODE_LENGTH = 3;

    String getProjectCode();

    String getBizCode();

    String getDetailCode();

    default int getCode() {
        return Integer.parseInt(getCodeValue());
    }

    default String getCodeValue() {
        return normalizeSegment("projectCode", getProjectCode(), PROJECT_CODE_LENGTH)
                + normalizeSegment("bizCode", getBizCode(), BIZ_CODE_LENGTH)
                + normalizeSegment("detailCode", getDetailCode(), DETAIL_CODE_LENGTH);
    }

    String getMessage();

    static String normalizeSegment(String segmentName, String value, int expectedLength) {
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
        return value;
    }
}
