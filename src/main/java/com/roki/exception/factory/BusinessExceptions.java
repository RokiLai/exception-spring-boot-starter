package com.roki.exception.factory;

import com.roki.exception.exception.BusinessException;

/**
 * Convenience factory for business exceptions.
 * 业务异常便捷工厂。
 *
 * Business projects can throw errors without manually creating {@link ErrorCode} objects.
 * 业务方可以不手动创建 {@link ErrorCode} 对象，直接生成异常。
 */
public final class BusinessExceptions {

    private BusinessExceptions() {
    }

    public static BusinessException of(String bizName, String detailCode, String message) {
        return new BusinessException(ErrorCodes.of(bizName, detailCode, message));
    }

    public static BusinessException ofDefault(String detailCode, String message) {
        return new BusinessException(ErrorCodes.ofDefault(detailCode, message));
    }
}
