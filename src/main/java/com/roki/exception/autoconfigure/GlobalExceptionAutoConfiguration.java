package com.roki.exception.autoconfigure;

import com.roki.exception.code.validation.ErrorCodeDefinitionValidator;
import com.roki.exception.code.scope.ErrorCodeScopes;
import com.roki.exception.handler.GlobalExceptionHandler;
import org.springframework.beans.factory.config.ConfigurableListableBeanFactory;
import org.springframework.boot.autoconfigure.AutoConfiguration;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;

/**
 * Wires the starter's default beans: scope registry, startup validator, and global exception handler.
 * 装配 starter 默认提供的 Bean：scope 注册表、启动校验器和全局异常处理器。
 */
@AutoConfiguration
@EnableConfigurationProperties(ErrorCodeProperties.class)
public class GlobalExceptionAutoConfiguration {

    @Bean
    @ConditionalOnMissingBean
    public ErrorCodeScopeInitializer errorCodeScopeInitializer(ErrorCodeProperties properties) {
        return new ErrorCodeScopeInitializer(properties);
    }

    @Bean
    @ConditionalOnMissingBean
    public ErrorCodeDefinitionValidator errorCodeDefinitionValidator() {
        return new ErrorCodeDefinitionValidator();
    }

    @Bean
    @ConditionalOnMissingBean
    public DeclarativeErrorCodeValidationRunner declarativeErrorCodeValidationRunner(
            ConfigurableListableBeanFactory beanFactory,
            ErrorCodeDefinitionValidator validator) {
        return new DeclarativeErrorCodeValidationRunner(beanFactory, validator);
    }

    @Bean
    @ConditionalOnMissingBean
    public GlobalExceptionHandler globalExceptionHandler() {
        return new GlobalExceptionHandler();
    }

    public static final class ErrorCodeScopeInitializer {

        /**
         * Initializes the runtime scope registry from external configuration.
         * 根据外部配置初始化运行时错误码 scope 注册表。
         */
        public ErrorCodeScopeInitializer(ErrorCodeProperties properties) {
            ErrorCodeScopes.configure(
                    properties.getProjectCode(),
                    properties.getDefaultBizCode(),
                    properties.getBizCodes());
        }
    }
}
