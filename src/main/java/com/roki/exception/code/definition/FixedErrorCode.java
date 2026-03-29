package com.roki.exception.code.definition;

import com.roki.exception.api.ErrorCode;

/**
 * Fixed global error code contract used by starter-level public errors.
 * starter 级公共错误使用的固定全局错误码契约。
 *
 * These codes do not depend on project configuration and stay stable across projects.
 * 这类错误码不依赖项目配置，在不同项目中保持稳定一致。
 */
public interface FixedErrorCode extends ErrorCode {

    int FIXED_CODE_LENGTH = PROJECT_CODE_LENGTH + BIZ_CODE_LENGTH + DETAIL_CODE_LENGTH;

    String getFixedCodeValue();

    @Override
    default String getProjectCode() {
        return getNormalizedFixedCodeValue().substring(0, PROJECT_CODE_LENGTH);
    }

    @Override
    default String getBizCode() {
        return getNormalizedFixedCodeValue().substring(PROJECT_CODE_LENGTH, PROJECT_CODE_LENGTH + BIZ_CODE_LENGTH);
    }

    @Override
    default String getDetailCode() {
        return getNormalizedFixedCodeValue().substring(PROJECT_CODE_LENGTH + BIZ_CODE_LENGTH);
    }

    @Override
    default int getCode() {
        return Integer.parseInt(getNormalizedFixedCodeValue());
    }

    @Override
    default String getCodeValue() {
        return getNormalizedFixedCodeValue();
    }

    private String getNormalizedFixedCodeValue() {
        return ErrorCode.normalizeSegment("fixedCode", getFixedCodeValue(), FIXED_CODE_LENGTH);
    }
}
