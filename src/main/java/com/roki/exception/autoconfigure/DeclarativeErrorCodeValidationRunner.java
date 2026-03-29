package com.roki.exception.autoconfigure;

import com.roki.exception.code.definition.DeclarativeErrorCode;
import com.roki.exception.code.validation.ErrorCodeDefinitionValidator;
import org.springframework.beans.factory.SmartInitializingSingleton;
import org.springframework.boot.autoconfigure.AutoConfigurationPackages;
import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.beans.factory.config.ConfigurableListableBeanFactory;
import org.springframework.context.annotation.ClassPathScanningCandidateComponentProvider;
import org.springframework.core.type.filter.AssignableTypeFilter;

import java.util.LinkedHashSet;
import java.util.List;
import java.util.Set;

/**
 * Scans application packages for declarative error code enums and validates them during startup.
 * 在应用启动阶段扫描声明式错误码枚举并执行校验。
 */
public final class DeclarativeErrorCodeValidationRunner implements SmartInitializingSingleton {

    private final ConfigurableListableBeanFactory beanFactory;
    private final ErrorCodeDefinitionValidator validator;

    public DeclarativeErrorCodeValidationRunner(
            ConfigurableListableBeanFactory beanFactory,
            ErrorCodeDefinitionValidator validator) {
        this.beanFactory = beanFactory;
        this.validator = validator;
    }

    @Override
    public void afterSingletonsInstantiated() {
        if (!AutoConfigurationPackages.has(beanFactory)) {
            return;
        }
        validator.validate(scanDeclarativeErrorCodeClasses(AutoConfigurationPackages.get(beanFactory)));
    }

    private Set<Class<?>> scanDeclarativeErrorCodeClasses(List<String> basePackages) {
        ClassPathScanningCandidateComponentProvider scanner =
                new ClassPathScanningCandidateComponentProvider(false);
        scanner.addIncludeFilter(new AssignableTypeFilter(DeclarativeErrorCode.class));
        Set<Class<?>> classes = new LinkedHashSet<>();
        for (String basePackage : basePackages) {
            for (BeanDefinition candidate : scanner.findCandidateComponents(basePackage)) {
                try {
                    classes.add(Class.forName(candidate.getBeanClassName()));
                } catch (ClassNotFoundException e) {
                    throw new IllegalStateException("Failed to load declarative error code class: "
                            + candidate.getBeanClassName(), e);
                }
            }
        }
        return classes;
    }
}
