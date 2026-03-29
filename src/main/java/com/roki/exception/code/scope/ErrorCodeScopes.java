package com.roki.exception.code.scope;

import com.roki.exception.api.ErrorCode;

import java.util.Collections;
import java.util.LinkedHashMap;
import java.util.Map;

/**
 * Runtime registry of configured error code scopes.
 * 运行时错误码 scope 注册表。
 *
 * Auto-configuration initializes it from Spring properties once per application.
 * 自动配置会在每个应用启动时根据 Spring 配置完成初始化。
 */
public final class ErrorCodeScopes {

    private static volatile String projectCode = "99";
    private static volatile String defaultBizCode = "01";
    private static volatile Map<String, String> bizCodes = defaultBizCodes();

    private ErrorCodeScopes() {
    }

    public static void configure(String projectCode, String defaultBizCode, Map<String, String> bizCodes) {
        ErrorCode.normalizeSegment("projectCode", projectCode, ErrorCode.PROJECT_CODE_LENGTH);
        ErrorCode.normalizeSegment("defaultBizCode", defaultBizCode, ErrorCode.BIZ_CODE_LENGTH);
        Map<String, String> normalizedBizCodes = new LinkedHashMap<>();
        for (Map.Entry<String, String> entry : bizCodes.entrySet()) {
            if (entry.getKey() == null || entry.getKey().isBlank()) {
                throw new IllegalArgumentException("biz code name must not be blank");
            }
            ErrorCode.normalizeSegment("bizCodes[" + entry.getKey() + "]", entry.getValue(), ErrorCode.BIZ_CODE_LENGTH);
            normalizedBizCodes.put(entry.getKey(), entry.getValue());
        }
        ErrorCodeScopes.projectCode = projectCode;
        ErrorCodeScopes.defaultBizCode = defaultBizCode;
        ErrorCodeScopes.bizCodes = Collections.unmodifiableMap(normalizedBizCodes);
    }

    public static ErrorCodeScope defaultScope() {
        return ErrorCodeScope.of(projectCode, defaultBizCode);
    }

    public static ErrorCodeScope ofBizCode(String bizCode) {
        return ErrorCodeScope.of(projectCode, bizCode);
    }

    public static ErrorCodeScope named(String bizName) {
        String bizCode = bizCodes.get(bizName);
        if (bizCode == null) {
            throw new IllegalArgumentException("biz code is not configured for name: " + bizName);
        }
        return ofBizCode(bizCode);
    }

    private static Map<String, String> defaultBizCodes() {
        Map<String, String> defaults = new LinkedHashMap<>();
        defaults.put("common", "01");
        defaults.put("validation", "02");
        defaults.put("system", "99");
        return Collections.unmodifiableMap(defaults);
    }
}
