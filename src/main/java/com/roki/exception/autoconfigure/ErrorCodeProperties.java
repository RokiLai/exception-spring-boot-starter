package com.roki.exception.autoconfigure;

import org.springframework.boot.context.properties.ConfigurationProperties;

import java.util.LinkedHashMap;
import java.util.Map;

/**
 * External configuration for project-level and module-level error code segments.
 * 项目级和模块级错误码分段的外部配置。
 */
@ConfigurationProperties(prefix = "roki.exception.error-code")
public class ErrorCodeProperties {

    private String projectCode = "99";
    private String defaultBizCode = "01";
    private Map<String, String> bizCodes = createDefaultBizCodes();

    public String getProjectCode() {
        return projectCode;
    }

    public void setProjectCode(String projectCode) {
        this.projectCode = projectCode;
    }

    public String getDefaultBizCode() {
        return defaultBizCode;
    }

    public void setDefaultBizCode(String defaultBizCode) {
        this.defaultBizCode = defaultBizCode;
    }

    public Map<String, String> getBizCodes() {
        return bizCodes;
    }

    public void setBizCodes(Map<String, String> bizCodes) {
        this.bizCodes = bizCodes;
    }

    private static Map<String, String> createDefaultBizCodes() {
        Map<String, String> defaults = new LinkedHashMap<>();
        defaults.put("common", "01");
        defaults.put("validation", "02");
        defaults.put("system", "99");
        return defaults;
    }
}
